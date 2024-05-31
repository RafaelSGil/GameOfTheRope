package clientSide.entities;

import genclass.GenericIO;
import interfaces.*;

import java.rmi.RemoteException;

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
     * Reference to the {@link IContestantsBench} object representing the bench
     * where the coach's team contestants are seated.
     */
    private final IContestantsBench bench;

    /**
     * Reference to the {@link IRefereeSite} object representing the referee's post.
     */
    private final IRefereeSite refereeSite;

    /**
     * Reference to the {@link IPlayground} object representing the playground area.
     */
    private final IPlayground playground;

    /**
     * Which strategy is going to use to choose the contestants
     */
    private int strategy;

    /**
     * Creates a new Coach instance with the specified name, team, and references to shared regions.
     *
     * @param threadName  The name to be assigned to the coach thread.
     * @param team        The team that the coach belongs to (0 or 1).
     * @param bench       The {@link IContestantsBench} object representing the team's bench.
     * @param playground  The {@link IPlayground} object representing the playground area.
     * @param refereeSite The {@link IRefereeSite} object representing the referee's post.
     */
    public Coach(String threadName, int team, int strategy, IContestantsBench bench, IPlayground playground, IRefereeSite refereeSite) {
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
        while (!endOfMatch()) {
            callContestants();
            informReferee();
            reviewNotes();
        }
    }

    /**
     * Introduces a simulated delay.
     */
    private void sleep() {
        try {
            sleep((long) (1 + 50 * Math.random()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the match is ended or not
     *
     * @return True if the match has ended, false otherwise.
     */
    private boolean endOfMatch(){
        boolean ret = false;                                 // return value

        try
        {
            ret = refereeSite.endOfMatch();
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Coach " + team + " remote exception on enOfMatch: " + e.getMessage ());
            System.exit (1);
        }
        return ret;
    }

    /**
     * Coaches will wait until they receive a signal from the referee and until their team is not ready
     * Afterward, pick which contestants will be playing and which will be benched
     * Wake up the contestants
     */
    private void callContestants(){
        ReturnCoach ret = null;
        try
        {
            ret = bench.callContestants(team, strategy);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Coach " + team + " remote exception on callContestants: " + e.getMessage ());
            System.exit (1);
        }

        coachState = ret.getState();
    }

    /**
     * Coach waits for every contestant of its team to be ready and
     * informs the referee that the team is ready.
     */
    private void informReferee(){
        ReturnCoach ret = null;
        try
        {
            ret = playground.informReferee(team);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Coach " + team + " remote exception on informReferee: " + e.getMessage ());
            System.exit (1);
        }

        coachState = ret.getState();
    }

    /**
     * Coaches will wait until the referee signals the end of the trial
     */
    private void reviewNotes(){
        ReturnCoach ret = null;
        try
        {
            ret = bench.reviewNotes(team);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Coach " + team + " remote exception on reviewNotes: " + e.getMessage ());
            System.exit (1);
        }

        coachState = ret.getState();
    }
}
