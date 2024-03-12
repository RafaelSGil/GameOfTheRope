package entities;

import sharedregions.ContestantsBench;

public class Contestant extends Thread{
    /**
     * Internal Coach State
     */
    private int state;

    /**
     * Contestant Team
     */
    private int team;

    /**
     * Contestant Strength
     */
    private int strength;
    /**
     * Contestant Number
     */
    private int number;

    /**
     * Contestants Bench
     */
    private final ContestantsBench bench;

    /**
     * Create new Coach
     */

    public Contestant(int team, int strength, int number, ContestantsBench bench){
        this.bench = bench;
        this.state = ContestantStates.SEATATBENCH;
        this.team = team;
        this.strength = strength;
        this.number = number;


    }

    public int getContestantState(){
        return state;
    }

    public int getContestantTeam(){
        return team;
    }

    public int getContestantStrength() {
        return strength;
    }

    public int getContestantNumber() {
        return number;
    }

    @Override
    public void run(){
        while(!endOfMatch())
            followcoachAdvice();
            getready()
            pullTheRope();
            amIDone();
            seatDown();

    }
}
