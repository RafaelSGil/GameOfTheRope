package clientSide.entities;

import clientSide.stubs.ContestantsBenchStub;
import clientSide.stubs.PlaygroundStub;
import clientSide.stubs.RefereeSiteStub;
import
/**
 * This class represents a Contestant entity in the game of the rope simulation.
 * Each contestant belongs to a team and has a specific strength level.
 * The contestant interacts with the {@link ContestantsBench}, {@link Playground},
 * and {@link RefereeSite} objects to follow instructions and participate in trials.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class Contestant extends Thread {
    /**
     * Internal state of the contestant
     * Possible states are defined in {@link ContestantStates}
     */
    private int contestantState;

    /**
     * Team that the contestant belongs to (0 or 1).
     */
    private int contestantTeam;

    /**
     * Strength level of the contestant.
     * Strength is an integer between {@link SimulationParams#MINSTRENGTH} and {@link SimulationParams#MAXSTRENGTH}.
     */
    private int contestantStrength;

    /**
     * Unique identifier for the contestant.
     */
    private final int contestantId;

    /**
     * Reference to the {@link ContestantsBenchStub} object representing the bench where the contestant sits.
     */
    private final ContestantsBenchStub bench;

    /**
     * Reference to the {@link PlaygroundStub} object representing the playground area.
     */
    private final PlaygroundStub playground;

    /**
     * Reference to the {@link RefereeSiteStub} object representing the referee's post.
     * The contestant receives instructions and signals the referee through the referee site.
     */
    private final RefereeSiteStub refereeSite;
    /**
     * Indicates whether the contestant is currently participating in a trial.
     */
    private boolean isPlaying;

    /**
     * Creates a new Contestant instance
     *
     * @param threadName   The name to be assigned to the contestant thread.
     * @param contestantId The unique identifier for the contestant.
     * @param team         The team that the contestant belongs to (0 or 1).
     * @param strength     The initial strength level of the contestant.
     * @param bench        The {@link ContestantsBenchStub} object representing the team's bench.
     * @param playground   The {@link PlaygroundStub} object representing the playground area.
     * @param refereeSite  The {@link RefereeSiteStub} object representing the referee's post.
     */
    public Contestant(String threadName, int contestantId, int team, int strength, ContestantsBenchStub bench, PlaygroundStub playground, RefereeSiteStub refereeSite) {
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

    /**
     * Gets the current state of the contestant.
     *
     * @return The current contestant state as defined in {@link ContestantStates}.
     */
    public synchronized int getContestantState() {
        return contestantState;
    }

    /**
     * Sets the internal state of the contestant.
     *
     * @param contestantState The new state for the contestant.
     */
    public synchronized void setContestantState(int contestantState) {
        this.contestantState = contestantState;
    }

    /**
     * Gets the team that the contestant belongs to.
     *
     * @return The team number (0 or 1).
     */
    public synchronized int getContestantTeam() {
        return contestantTeam;
    }

    /**
     * Sets the team that the contestant belongs to.
     *
     * @param contestantTeam The new team number (0 or 1).
     */
    public synchronized void setContestantTeam(int contestantTeam) {
        this.contestantTeam = contestantTeam;
    }

    /**
     * Gets the current strength level of the contestant.
     *
     * @return The contestant's strength as an integer between {@link serverSide.main.SimulationParams#MINSTRENGTH} and {@link serverSide.main.SimulationParams#MAXSTRENGTH}.
     */

    public synchronized int getContestantStrength() {
        return contestantStrength;
    }

    /**
     * Sets the strength level of the contestant.
     *
     * @param contestantStrength The contestant's strength as an integer between {@link serverSide.main.SimulationParams#MINSTRENGTH} and {@link serverSide.main.SimulationParams#MAXSTRENGTH}.
     */
    public synchronized void setContestantStrength(int contestantStrength) {
        this.contestantStrength = contestantStrength;
    }

    /**
     * Gets the unique identifier for the contestant.
     *
     * @return The contestant's ID.
     */
    public synchronized int getContestantId() {
        return contestantId;
    }

    /**
     * Checks if the contestant is currently participating in a trial.
     *
     * @return True if the contestant is playing, false otherwise.
     */
    public synchronized boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Sets the playing state of the contestant.
     *
     * @param playing True if the contestant is now playing, false otherwise.
     */
    public synchronized void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    /**
     * The main execution loop of the contestant thread.
     * The contestant keeps iterating until the end of the match is signaled by the referee.
     */
    @Override
    public void run() {
        while (!refereeSite.endOfMatch()) {
            bench.followCoachAdvice();
            if (isPlaying) {
                playground.getReady();
                pullTheRope();
                playground.amIDone();
            }
            bench.seatDown();
        }
    }

    /**
     * Generates a random integer between {@link serverSide.main.SimulationParams#MINSTRENGTH} and {@link serverSide.main.SimulationParams#MAXSTRENGTH}
     * to represent the initial strength of a contestant.
     *
     * @return The randomly generated strength level.
     */
    public static int GenerateRandomStrength() {
        return (int) (SimulationParams.MINSTRENGTH + Math.random() * (SimulationParams.MAXSTRENGTH - SimulationParams.MINSTRENGTH));
    }

    /**
     * Simulates the contestant pulling the rope for a random time between 1 and 100 milliseconds.
     */
    private void pullTheRope() {
        try {
            sleep((long) (1 + 100 * Math.random()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adjusts the contestant's strength based on their playing state.
     * If playing, strength decreases by 1 (up to a minimum of {@link serverSide.main.SimulationParams#MINSTRENGTH}).
     * If not playing, strength increases by 1 (up to a maximum of {@link serverSide.main.SimulationParams#MAXSTRENGTH}).
     */
    public synchronized void manageStrength() {
        if (isPlaying && contestantStrength > 0) {
            contestantStrength = contestantStrength - 1;
        } else if(!isPlaying && contestantStrength >0){
            contestantStrength = contestantStrength + 1;
        }
    }

}
