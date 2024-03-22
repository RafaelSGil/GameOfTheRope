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
        this.playing = new ArrayList<Integer>(SimulationParams.NPLAYERSINCOMPETITION*2);
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
        this.numTrials = numTrials;
        this.numGames = numGames;
        unblockContestantBench();
    }

    public void setHasTrialEnded(boolean hasTrialEnded) {
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
        this.callTrial = false;

        // wait for the first notify of every iteration
        // that corresponds to the call trial of the referee
        while(!callTrial){
            try {
                wait();
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

    private void strategy(String strategy,int team) {
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
        }
        else if(strategy.equals("random")) {
            for (int i = 0; i < SimulationParams.NPLAYERSINCOMPETITION-1; i++) {
                playing.add(aux.get(i).getContestantId());
            }
            playing.add(aux.get(aux.size() - 1).getContestantId());
        }
    }

    public synchronized void followCoachAdvice(){
        int contestantId = ((Contestant) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((Contestant) Thread.currentThread());
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());

        // wait for coach to choose team
        while(!playing.contains(contestantId)){
            try{
                wait();
            }catch (InterruptedException e){
            }
        }

        contestants[contestantId].setContestantState(ContestantStates.STANDINPOSITION);
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());
    }
    public synchronized void seatDown(){
        int contestantId = ((Contestant) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((Contestant) Thread.currentThread());
        playing.clear();
        while(!hasTrialEnded){
            try{
                wait();
            }catch (InterruptedException e){
            }
        }

        contestants[contestantId].setContestantState(ContestantStates.SEATATBENCH);
    }

    public synchronized void reviewNotes(){
        this.hasTrialEnded = false;

        while(!hasTrialEnded){
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        ((Coach) Thread.currentThread()).setCoachState(CoachStates.WATFORREFEREECOMMAND);
        repository.updateCoach(((Coach) Thread.currentThread()).getCoachState(), ((Coach) Thread.currentThread()).getCoachTeam());
    }
}
