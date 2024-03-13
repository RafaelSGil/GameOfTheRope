package entities;

import sharedregions.ContestantsBench;
import sharedregions.Playground;
import sharedregions.RefereeSite;

public class Coach extends Thread {

    /**
     * Internal Coach State
     */
    private int coachState;

    /**
     * Coach Team
     */
    private int team;

    /**
     * Contestants Bench
     */
    private final ContestantsBench bench;

    private final RefereeSite refereeSite;

    private final Playground playground;

    /**
     * Create new Coach
     */
    public Coach(String threadName, int team, ContestantsBench bench, Playground playground, RefereeSite refereeSite){
        super(threadName);
        this.coachState = CoachStates.WATFORREFEREECOMMAND;
        this.team = team;
        this.bench = bench;
        this.refereeSite = refereeSite;
        this.playground = playground;
    }

    public int getCoachState(){
        return this.coachState;
    }

    public void setCoachState(int coachState) {
        this.coachState = coachState;
    }

    @Override
    public void run(){
        while(!refereeSite.endOfMatch()){
            bench.callContestants(team);
            refereeSite.informReferee();
            bench.reviewNotes();
        }
    }
}
