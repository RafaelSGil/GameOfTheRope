package entities;

import main.SimulationParams;
import sharedregions.ContestantsBench;
import sharedregions.Playground;
import sharedregions.RefereeSite;

public class Contestant extends Thread{
    /**
     * Internal Coach State
     */
    private int contestantState;

    /**
     * Contestant Team
     */
    private final int contestantTeam;

    /**
     * Contestant Strength
     */
    private int contestantStrength;
    /**
     * Contestant Number
     */
    private final int contestantId;

    /**
     * Contestants Bench
     */
    private final ContestantsBench bench;

    private final Playground playground;

    private final RefereeSite refereeSite;

    /**
     * Create new Coach
     */

    public Contestant(int contestantId, int team, int strength, ContestantsBench bench, Playground playground, RefereeSite refereeSite){
        this.bench = bench;
        this.contestantState = ContestantStates.SEATATBENCH;
        this.contestantTeam = team;
        this.contestantStrength = strength;
        this.contestantId = contestantId;
        this.playground = playground;
        this.refereeSite = refereeSite;
    }

    public int getContestantState(){
        return contestantState;
    }

    public void setContestantState(int contestantState) {
        this.contestantState = contestantState;
    }

    public int getContestantTeam(){
        return contestantTeam;
    }

    public int getContestantStrength() {
        return contestantStrength;
    }

    public void setContestantStrength(int contestantStrength) {
        this.contestantStrength = contestantStrength;
    }

    public int getContestantId() {
        return contestantId;
    }

    @Override
    public void run(){
        while(!refereeSite.endOfMatch()){
            bench.followCoachAdvice();
            playground.getReady();
            playground.pullTheRope();
            playground.amIDone();
            bench.seatDown();
        }
    }

    public static int GenerateRandomStrength(){
        return (int) (SimulationParams.MINSTRENGTH + Math.random() * (SimulationParams.MAXSTRENGTH - SimulationParams.MINSTRENGTH));
    }
}
