package sharedregions;

import entities.Contestant;
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


    public ContestantsBench(Contestant[] contestants, GeneralRepository repository) {
        this.contestants = contestants;
        this.bench = new ArrayList<Integer>(SimulationParams.NPLAYERS-SimulationParams.NPLAYERSINCOMPETITION);
        this.playing = new ArrayList<Integer>(SimulationParams.NPLAYERSINCOMPETITION);
        this.repository = repository;
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

    public synchronized void followCoachAdvice(){}
    public synchronized void seatDown(){}
    public synchronized void reviewNotes(){}
}
