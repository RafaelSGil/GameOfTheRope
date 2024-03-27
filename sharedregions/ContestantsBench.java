package sharedregions;


import entities.*;
import genclass.GenericIO;
import main.SimulationParams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Contestants Bench
 *
 * Used by the contestants to wait for the referee commands
 *
 */
public class ContestantsBench {

    /**
     * Array to store all Contestants
     */
    private final Contestant[] contestants;

    /**
     * List to store Playing players
     */
    private final List<Integer> playing;

    private final List<Integer> benched;

    /**
     * General Repository
     */
    private final GeneralRepository repository;

    /**
     * Number of Trials
     */
    private int numTrials;

    /**
     * Number of Games
     */
    private int numGames;

    /**
     * Checks if trial has ended
     * */
    private boolean hasTrialEnded;

    private boolean callTrial;



    public ContestantsBench(GeneralRepository repository) {
        this.playing = new ArrayList<>();
        this.benched = new ArrayList<>();
        this.repository = repository;
        this.numTrials = 0;
        this.numGames = 0;
        this.hasTrialEnded = false;
        this.callTrial = false;
        this.contestants = new Contestant[SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = null;
        }
    }

    public synchronized void unblockContestantBench(){
        notifyAll();
    }

    public synchronized void refereeCallTrial(int numTrials, int numGames){
        callTrial = true;
        this.hasTrialEnded = false;
        this.numTrials = numTrials;
        this.numGames = numGames;
        unblockContestantBench();
    }

    public synchronized void setHasTrialEnded(boolean hasTrialEnded) {
        this.hasTrialEnded = hasTrialEnded;
    }

    private boolean isEveryoneSeated(int team){
        int contSeated = 0;
        for (Contestant c : contestants){
            try {
                if(c.getContestantTeam() == team && c.getContestantState() == ContestantStates.SEATATBENCH){
                    contSeated++;
                }
            } catch (Exception e) {
            }
        }
        return contSeated == SimulationParams.NPLAYERS;
    }

    public synchronized void callContestants(int team) {
        // wait for the first notify of every iteration
        // that corresponds to the call trial of the referee
        while(!callTrial || !isEveryoneSeated(team)){
            try {
                wait();
                GenericIO.writelnString("Coach " + team + " blocked CALL " + callTrial + " " + isEveryoneSeated(team));
            }catch (InterruptedException e){
            }
        }


        repository.updateCoach(((Coach) Thread.currentThread()).getCoachState(), ((Coach) Thread.currentThread()).getCoachTeam());

        //choose the players
        strategy(team == 0 ? "strength" : "random",team);


        ((Coach) Thread.currentThread()).setCoachState(CoachStates.ASSEMBLETEAM);
        repository.updateCoach(((Coach) Thread.currentThread()).getCoachState(), ((Coach) Thread.currentThread()).getCoachTeam());

        // wake up contestants
        notifyAll();
    }

    private synchronized void strategy(String strategy,int team) {
        ArrayList<Contestant> aux = new ArrayList<>();

        for(Contestant c : contestants){
            if (c.getContestantTeam() == team) {
                aux.add(c);
            }
        }

        aux.sort(Comparator.comparingInt(Contestant::getContestantStrength).reversed());

        if(strategy.equals("strength")) {
            for (int i = 0; i < SimulationParams.NPLAYERSINCOMPETITION; i++) {
                playing.add(aux.get(i).getContestantId());
            }
            for (int i = SimulationParams.NPLAYERSINCOMPETITION; i < SimulationParams.NPLAYERS; i++) {
                benched.add(aux.get(i).getContestantId());
            }
        }
        else if(strategy.equals("random")) {
            // Get first 2 contestanst
            for (int i = 0; i < SimulationParams.NPLAYERSINCOMPETITION-1; i++) {
                playing.add(aux.get(i).getContestantId());
            }
            // Get last contestant
            playing.add(aux.get(aux.size() - 1).getContestantId());
            for (int i = SimulationParams.NPLAYERSINCOMPETITION-1; i < SimulationParams.NPLAYERS-1; i++) {
                benched.add(aux.get(i).getContestantId());
            }
        }
    }

    public synchronized void followCoachAdvice(){
        int contestantId = ((Contestant) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((Contestant) Thread.currentThread());
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());

        // wait for coach to choose team
        while(!playing.contains(contestantId) && !benched.contains(contestantId)){
            try{
                wait();
            }catch (InterruptedException e){
            }
        }


        if(benched.contains(contestantId)){
            GenericIO.writelnString("Player " + contestantId + " will NOT play");
            return;
        }

        GenericIO.writelnString("Player " + contestantId + " will play");
        contestants[contestantId].setPlaying(true);
        contestants[contestantId].setContestantState(ContestantStates.STANDINPOSITION);
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());
    }
    public synchronized void seatDown(){
        int contestantId = ((Contestant) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((Contestant) Thread.currentThread());
        while(!hasTrialEnded){
            try{
                GenericIO.writelnString("Player " + contestantId + " blocked in SEATDOWN");
                wait();
            }catch (InterruptedException e){
            }
        }

        contestants[contestantId].manageStrength();
        playing.clear();
        benched.clear();
        contestants[contestantId].setPlaying(false);

        contestants[contestantId].setContestantState(ContestantStates.SEATATBENCH);
    }

    public synchronized void reviewNotes(){
        this.callTrial = false;
        while(!hasTrialEnded){
            try {
                GenericIO.writelnString("Coach " + ((Coach) Thread.currentThread()).getCoachTeam() + " bloqued REVIEW");
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        ((Coach) Thread.currentThread()).setCoachState(CoachStates.WATFORREFEREECOMMAND);
        repository.updateCoach(((Coach) Thread.currentThread()).getCoachState(), ((Coach) Thread.currentThread()).getCoachTeam());
    }
}
