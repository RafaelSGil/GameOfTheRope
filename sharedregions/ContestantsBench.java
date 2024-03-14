package sharedregions;

import entities.Contestant;
import entities.ContestantStates;
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

    private final GeneralRepository repository;


    public ContestantsBench(GeneralRepository repository) {
        this.bench = new ArrayList<Integer>(SimulationParams.NPLAYERS-SimulationParams.NPLAYERSINCOMPETITION);
        this.playing = new ArrayList<Integer>(SimulationParams.NPLAYERSINCOMPETITION);
        this.repository = repository;
        this.contestants = new Contestant[SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = null;
        }
    }

    public synchronized void callContestants(int team) {
        for(int i = 0; i < SimulationParams.NPLAYERSINCOMPETITION; i++) {
            if(team == 0){
                playing.add(strategy("strength",playing));
            }
            else{
                playing.add(strategy("strength",playing));            }

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
    public synchronized void reviewNotes(){}
}
