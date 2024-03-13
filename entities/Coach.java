package entities;

import sharedregions.ContestantsBench;

public class Coach extends Thread {

    /**
     * Internal Coach State
     */
    private int state;

    /**
     * Coach Team
     */
    private int team;

    /**
     * Contestants Bench
     */
    private final ContestantsBench bench;

    /**
     * Create new Coach
     */
    public Coach(int team, ContestantsBench bench){
        this.state = CoachStates.WATFORREFEREECOMMAND;
        this.team = team;
        this.bench = bench;

    }

    @Override
    public void run(){
        while(!endOfMatch()){
            bench.callContestants(team);
            informReferee();
            reviewNotes();
        }
    }
}
