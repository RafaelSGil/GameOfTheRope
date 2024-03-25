package sharedregions;

import entities.*;
import genclass.GenericIO;
import main.SimulationParams;

import java.beans.IntrospectionException;

public class Playground {
    private Contestant[] contestants;
    private Coach[] coaches;
    private final GeneralRepository repository;
    private Referee referee;
    private int coachesSignal;
    private int ropesPulled;
    private boolean trialStarted;

    private int team0Power;

    private int team1Power;

    private int ropePosition;

    private int numTrials;

    private int numGames;

    private int team0TrialWins;

    private int team1TrialWins;

    private RefereeSite refereeSite;

    public Playground(GeneralRepository repository, RefereeSite refereeSite){
        this.repository = repository;
        this.refereeSite = refereeSite;
        this.contestants = new Contestant[SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = null;
        }
        this.coaches = new Coach[SimulationParams.NTEAMS];
        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            coaches[i] = null;
        }
        this.referee = null;
        this.coachesSignal = 0;
        this.ropesPulled = 0;
        this.trialStarted = false;
        this.team0Power = 0;
        this.team1Power = 0;
        this.ropePosition = 0;
        this.numTrials = 0;
        this.numGames = 0;
        this.team0TrialWins = 0;
        this.team1TrialWins = 0;
    }

    public synchronized void unblockPlayground(){
        notifyAll();
    }

    public synchronized void callTrial(ContestantsBench bench){
        this.referee = ((Referee) Thread.currentThread());
        referee.setRefereeSate(RefereeStates.TEAMSREADY);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());
        referee.setTrial(referee.getTrial() + 1);
        repository.setTrial(referee.getTrial());

        // will wake up the coaches
        bench.refereeCallTrial(referee.getTrial(), referee.getGame());
    }

    private boolean haveCoachesChosenTeams(){
        try {
            for (Coach c : coaches){
                if(c.getCoachState() != CoachStates.WATCHTRIAL){
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public synchronized void startTrial(){
        trialStarted = false;

        // synchronize, will get waken up by the last coach
        while(!haveCoachesChosenTeams()){
            try{
                wait();
            }catch (InterruptedException e){
            }
        }

        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.WAITTRIALCONCLUSION);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());
        repository.setRopePosition(repository.getRopePosition());

        trialStarted = true;
        // wake up contestants
        notifyAll();
    }

    private boolean haveContestantsPulledRope(){
        return ropesPulled == SimulationParams.NPLAYERSINCOMPETITION*2;
    }

    public synchronized boolean assertTrialDecision(ContestantsBench bench){
        // synchronize, will get waken up by the last contestant
        while(!haveContestantsPulledRope()){
            try{
                wait();
            }catch (InterruptedException e){
            }
        }
        numTrials++;
        int ropeMove = Math.abs(team0Power-team1Power);
        if(team1Power > team0Power){                      // team 1 wins trial
            repository.setRopePosition(ropeMove);
            ropePosition += ropeMove;

        }
        else {           //team 0 wins trial
            repository.setRopePosition(ropeMove*-1);
            ropePosition -= ropeMove;
        }


        // reset counters
        this.ropesPulled = 0;
        this.team0Power = 0;
        this.team1Power = 0;

        bench.setHasTrialEnded(true);
        bench.unblockContestantBench();

        if(numTrials == SimulationParams.NTRIALS || ropePosition > 3 || ropePosition < -3){
            numTrials = 0;
            numGames++;
            if(ropePosition > 0){
                if(ropePosition > 3){
                    refereeSite.setGameWinCause("knockout");
                }
                team1TrialWins++;
                refereeSite.updateTrialWins(1);
                repository.setTrialWinner(1);
                refereeSite.setGameWinCause("points");


            }
            else if(ropePosition < 0){
                if(ropePosition < -3){
                    refereeSite.setGameWinCause("knockout");
                }
                team0TrialWins++;
                refereeSite.updateTrialWins(0);
                repository.setTrialWinner(0);
                refereeSite.setGameWinCause("points");


            }
            else{
                repository.setRopePosition(0);
                repository.setTrialWinner(2);
                refereeSite.setGameWinCause("draw");
            }
            if(numGames == SimulationParams.GAMES || team0TrialWins > 3 || team1TrialWins > 3){
                return true;
            }
            //TODO The game may end sooner if the produced shift is greater or equal to four length units
        }

        return false;
    }

    // checks if there are 3 contestants of the team in state STANDINPOSITION
    private boolean checkIfTeamIsReady(int team){
        int numReadyContestants = 0;

        for (Contestant contestant : contestants) {
            try {
                if (contestant.getContestantState() == ContestantStates.STANDINPOSITION && contestant.getContestantTeam() == team) {
                    numReadyContestants++;
                }
            } catch (Exception e) {
            }
        }

        return numReadyContestants == SimulationParams.NPLAYERSINCOMPETITION;
    }

    public synchronized void informReferee(){
        int coachId = ((Coach) Thread.currentThread()).getCoachTeam();
        coaches[coachId] = ((Coach) Thread.currentThread());

        // waits for the team to be ready
        while(!checkIfTeamIsReady(coaches[coachId].getCoachTeam())){
            try {
                wait();
            }catch (InterruptedException e){
            }
        }

        coaches[coachId].setCoachState(CoachStates.WATCHTRIAL);
        repository.updateCoach(coaches[coachId].getCoachState(), coachId);

        // alerts the referee that its team is ready
        notifyAll();
    }

    public synchronized void getReady(){
        int contestantId = ((Contestant) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((Contestant) Thread.currentThread());
        int team = contestants[contestantId].getContestantTeam();
        if(team == 0) {
            team0Power += contestants[contestantId].getContestantStrength();
        }
        else{
            team1Power += contestants[contestantId].getContestantStrength();
        }

//        GenericIO.writelnString("CONTESTANT " + contestantId);
//        for (Contestant c : contestants){
//            try {
//                GenericIO.writelnString("CONT " + c.getContestantId() + " STATE " + c.getContestantState() + " TEAM " + c.getContestantTeam());
//            } catch (Exception e) {
//                GenericIO.writelnString("Empty slot");
//            }
//        }
//        GenericIO.writelnString();

        // wake up the coach
        notifyAll();

        // wait for referee sign
        while(!trialStarted){
            try{
                wait();
            }catch (InterruptedException e){
            }
        }

        contestants[contestantId].setContestantState(ContestantStates.DOYOURBEST);
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());
    }

    public synchronized void amIDone(){
        int contestantId = ((Contestant) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((Contestant) Thread.currentThread());

        ropesPulled++;

        // wake up the referee
        notifyAll();

//        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
//                contestants[contestantId].getContestantState(),
//                contestants[contestantId].getContestantTeam());
    }
}
