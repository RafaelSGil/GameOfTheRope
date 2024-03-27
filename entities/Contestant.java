package entities;

import genclass.GenericIO;
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
    private int contestantTeam;

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

    private boolean isPlaying;

    public Contestant(String threadName, int contestantId, int team, int strength, ContestantsBench bench, Playground playground, RefereeSite refereeSite){
        super(threadName);
        this.bench = bench;
        this.contestantState = ContestantStates.SEATATBENCH;
        this.contestantTeam = team;
        this.contestantStrength = strength;
        this.contestantId = contestantId;
        this.playground = playground;
        this.refereeSite = refereeSite;
        this.isPlaying = false;
    }

    public synchronized int getContestantState(){
        return contestantState;
    }

    public synchronized void setContestantState(int contestantState) {
        this.contestantState = contestantState;
    }

    public synchronized int getContestantTeam(){
        return contestantTeam;
    }

    public synchronized void setContestantTeam(int contestantTeam) {
        this.contestantTeam = contestantTeam;
    }

    public synchronized int getContestantStrength() {
        return contestantStrength;
    }

    public synchronized void setContestantStrength(int contestantStrength) {
        this.contestantStrength = contestantStrength;
    }

    public synchronized int getContestantId() {
        return contestantId;
    }

    public synchronized boolean isPlaying() {
        return isPlaying;
    }

    public synchronized void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    public void run(){
        while(!refereeSite.endOfMatch()){
            bench.followCoachAdvice();
            if (isPlaying) {
                playground.getReady();
                pullTheRope();
                playground.amIDone();
            }
            bench.seatDown();
        }
    }

    public static int GenerateRandomStrength(){
        return (int) (SimulationParams.MINSTRENGTH + Math.random() * (SimulationParams.MAXSTRENGTH - SimulationParams.MINSTRENGTH));
    }

    private void pullTheRope(){
        try
        { sleep ((long) (1 + 100 * Math.random ()));
        }
        catch (InterruptedException e) {}
    }

    public synchronized void manageStrength() {
        if (isPlaying) {
            contestantStrength = Math.max(contestantStrength - 1, SimulationParams.MINSTRENGTH);
        } else {
            contestantStrength = Math.min(contestantStrength + 1, SimulationParams.MAXSTRENGTH);
        }
    }

}
