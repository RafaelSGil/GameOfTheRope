package assignment1.entities;

import assignment1.sharedregions.ContestantsBench;
import assignment1.sharedregions.Playground;
import assignment1.sharedregions.RefereeSite;

/**
 * This class represents a Coach entity in the game of the rope simulation.
 * Each coach is associated with a specific team and is responsible for
 * interacting with the contestants and the referee.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class Coach extends Thread {

    /**
     * Internal Coach State
     * Possible states are defined in {@link CoachStates}
     */
    private int coachState;

    /**
     * Team that the coach belongs to.
     */
    private final int team;

    /**
     * Reference to the {@link ContestantsBench} object representing the bench
     * where the coach's team contestants are seated.
     */
    private final ContestantsBench bench;

    /**
     * Reference to the {@link RefereeSite} object representing the referee's post.
     */
    private final RefereeSite refereeSite;

    /**
     * Reference to the {@link Playground} object representing the playground area.
     */
    private final Playground playground;

    /**
     * Which strategy is going to use to choose the contestants
     */
    private int strategy;

    /**
     * Creates a new Coach instance with the specified name, team, and references to shared regions.
     *
     * @param threadName  The name to be assigned to the coach thread.
     * @param team        The team that the coach belongs to (0 or 1).
     * @param bench       The {@link ContestantsBench} object representing the team's bench.
     * @param playground  The {@link Playground} object representing the playground area.
     * @param refereeSite The {@link RefereeSite} object representing the referee's post.
     */
    public Coach(String threadName, int team, int strategy, ContestantsBench bench, Playground playground, RefereeSite refereeSite) {
        super(threadName);
        this.coachState = CoachStates.WATFORREFEREECOMMAND;
        this.team = team;
        this.bench = bench;
        this.refereeSite = refereeSite;
        this.playground = playground;
        this.strategy = strategy;
    }

    /**
     * Gets the current state of the coach.
     *
     * @return The current coach state as defined in {@link CoachStates}.
     */
    public synchronized int getCoachState() {
        return this.coachState;
    }


    /**
     * Sets the internal state of the coach.
     *
     * @param coachState The new state for the coach.
     */
    public synchronized void setCoachState(int coachState) {
        this.coachState = coachState;
    }

    /**
     * Gets the team that the coach belongs to.
     *
     * @return The team number (0 or 1).
     */
    public synchronized int getCoachTeam() {
        return this.team;
    }

    /**
     * Get the strategy
     *
     * @return strategy value
     */
    public int getStrategy() {
        return strategy;
    }

    /**
     * Set the new strategy
     *
     * @param strategy the new strategy
     */
    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    /**
     * The main execution loop of the coach thread.
     * The coach keeps iterating until the end of the match is signaled by the referee.
     */


    @Override
    public void run() {
        while (!refereeSite.endOfMatch()) {
            bench.callContestants(team);
            playground.informReferee();
            bench.reviewNotes();
        }
    }

}
