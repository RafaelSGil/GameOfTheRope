package sharedregions;


import entities.*;
import main.SimulationParams;

import java.util.ArrayList;
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
     * List to store Benched players
     */
    private final List<Integer> bench;

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
    private final int numTrials;

    /**
     * Number of Games
     */
    private final int numGames;

    /**
     * Checks if trial has ended
     * */
    private final boolean hasTrialEnded;

    /**
     * Checks if contestants are ready
     * */
    private boolean hasContestansready;




    public ContestantsBench(GeneralRepository repository) {
        this.bench = new ArrayList<Integer>((SimulationParams.NPLAYERS-SimulationParams.NPLAYERSINCOMPETITION)*2);
        this.playing = new ArrayList<Integer>(SimulationParams.NPLAYERSINCOMPETITION*2);
        this.repository = repository;
        this.numTrials = 0;
        this.numGames = 0;
        this.hasTrialEnded = true;
        this.hasContestansready = false;
        this.contestants = new Contestant[SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = null;
        }
    }

    public synchronized void callContestants(int team) {

        if (numTrials > 1 || numGames > 1) {
            while (bench.size() < SimulationParams.NPLAYERS - SimulationParams.NPLAYERSINCOMPETITION * 2)  //waiting for all players to be benched
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e.getMessage());
                }
        }
        while (!hasTrialEnded) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        notifyAll();
        //choose the players
        for(int i = 0; i < SimulationParams.NPLAYERSINCOMPETITION*2; i++) {
            if(team == 0){
                int idx = strategy("strength",playing);
                contestants[idx].setContestantState(ContestantStates.STANDINPOSITION);
                playing.add(idx);


            }
            else{
                int idx = strategy("random",playing);
                contestants[idx].setContestantState(ContestantStates.STANDINPOSITION);
                playing.add(idx);          }

        }
        hasContestansready = true;
        ((Coach) Thread.currentThread()).setCoachState(CoachStates.ASSEMBLETEAM);
        repository.updateCoach(((Coach) Thread.currentThread()).getCoachTeam(), ((Coach) Thread.currentThread()).getCoachState());
        while (playing.size() < SimulationParams.NPLAYERSINCOMPETITION * 2) { //waiting for all players to be playing
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }


    }

    private int strategy(String strategy,List<Integer> playing) {
        if(strategy.equals("strength")) {
            int max = 0;
            int index = 0;
            for(int i = 0; i < SimulationParams.NPLAYERS; i++) {
                if(contestants[i].getContestantStrength() > max && !playing.contains(i)) {
                    max = contestants[i].getContestantStrength();
                    index = i;
                }
            }
            return index;
        }
        else if(strategy.equals("random")) {
            return (int) (Math.random() * SimulationParams.NPLAYERS);
        }
        else {
            return -1;
        }
    }

    public synchronized void followCoachAdvice(){
        int contestantId = ((Contestant) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((Contestant) Thread.currentThread());
        while(contestants[contestantId].getContestantState() != ContestantStates.STANDINPOSITION){
            try{
                wait();
            }catch (InterruptedException e){
            }
        }

        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());

        // wake up the coach
        notifyAll();
    }
    public synchronized void seatDown(){
        int contestantId = ((Contestant) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((Contestant) Thread.currentThread());
        contestants[contestantId].setContestantState(ContestantStates.SEATATBENCH);
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());
    }

    public synchronized void reviewNotes(){
        while(!hasTrialEnded){
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        ((Coach) Thread.currentThread()).setCoachState(CoachStates.WATFORREFEREECOMMAND);
        repository.updateCoach(((Coach) Thread.currentThread()).getCoachTeam(), ((Coach) Thread.currentThread()).getCoachState());
        notifyAll(); //notify referee
    }
}
