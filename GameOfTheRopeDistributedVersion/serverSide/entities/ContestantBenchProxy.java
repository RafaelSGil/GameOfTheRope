package serverSide.entities;

import clientSide.entities.CoachCloning;
import clientSide.entities.CoachStates;
import clientSide.entities.ContestantCloning;
import clientSide.entities.ContestantStates;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.main.SimulationParams;
import serverSide.sharedRegions.ContestantBenchInterface;

/**
 * Service provider agent for access to the Contestant Bench.
 * <p>
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class ContestantBenchProxy extends Thread implements CoachCloning, ContestantCloning {

    /**
     * Internal Coach State
     * Possible states are defined in {@link CoachStates}
     */
    private int coachState;

    /**
     * Team that the coach belongs to.
     */
    private int coachTeam;

    /**
     * Which strategy is going to use to choose the contestants
     */
    private int strategy;

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
    private int contestantId;

    /**
     * Indicates whether the contestant is currently participating in a trial.
     */
    private boolean isPlaying = false;

    /**
     * Communication channel.
     */

    private final ServerCom sconi;

    /**
     * Interface for the contestant bench
     */
    private final ContestantBenchInterface contestantBenchInterface;

    /**
     * Number of instantiated threads.
     */

    private static int nProxy = 0;

    private static int getProxyId() {
        Class<?> cl = null;                                            // representation of the BarberShopClientProxy object in JVM
        int proxyId;                                                   // instantiation identifier

        try {
            cl = Class.forName("serverSide.entities.ContestantBenchProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type ContestantBenchProxy was not found!");
            e.printStackTrace();
            System.exit(1);
        }
        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    public ContestantBenchProxy(ServerCom sconi, ContestantBenchInterface contestantBenchInterface) {
        super("ContestantBenchProxy_" + ContestantBenchProxy.getProxyId());
        this.sconi = sconi;
        this.contestantBenchInterface = contestantBenchInterface;
    }

    @Override
    public int getCoachState() {
        return coachState;
    }

    /**
     * Sets the internal state of the coach.
     *
     * @param coachState The new state for the coach.
     */
    @Override
    public void setCoachState(int coachState) {
        this.coachState = coachState;
    }

    /**
     * Gets the team that the coach belongs to.
     *
     * @return The team number (0 or 1).
     */
    @Override
    public int getCoachTeam() {
        return coachTeam;
    }

    /**
     * Sets the team that the coach belongs
     */
    @Override
    public void setCoachTeam(int team) {
        this.coachTeam = team;
    }

    /**
     * Get the strategy
     *
     * @return strategy value
     */
    @Override
    public int getStrategy() {
        return strategy;
    }

    /**
     * Set the new strategy
     *
     * @param strategy the new strategy
     */
    @Override
    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    /**
     * Gets the current state of the contestant.
     *
     * @return The current contestant state as defined in {@link ContestantStates}.
     */
    @Override
    public int getContestantState() {
        return contestantState;
    }

    /**
     * Sets the internal state of the contestant.
     *
     * @param contestantState The new state for the contestant.
     */
    @Override
    public void setContestantState(int contestantState) {
        this.contestantState = contestantState;
    }

    /**
     * Gets the team that the contestant belongs to.
     *
     * @return The team number (0 or 1).
     */
    @Override
    public int getContestantTeam() {
        return contestantTeam;
    }

    /**
     * Sets the team that the contestant belongs to.
     *
     * @param contestantTeam The new team number (0 or 1).
     */
    @Override
    public void setContestantTeam(int contestantTeam) {
        this.contestantTeam = contestantTeam;
    }

    /**
     * Gets the current strength level of the contestant.
     *
     * @return The contestant's strength as an integer between {@link SimulationParams#MINSTRENGTH} and {@link SimulationParams#MAXSTRENGTH}.
     */
    @Override
    public int getContestantStrength() {
        return contestantStrength;
    }

    /**
     * Sets the strength level of the contestant.
     *
     * @param contestantStrength The contestant's strength as an integer between {@link SimulationParams#MINSTRENGTH} and {@link SimulationParams#MAXSTRENGTH}.
     */
    @Override
    public void setContestantStrength(int contestantStrength) {
        this.contestantStrength = contestantStrength;
    }

    /**
     * Gets the unique identifier for the contestant.
     *
     * @return The contestant's ID.
     */
    @Override
    public int getContestantId() {
        return contestantId;
    }

    /**
     * Set contestant id
     *
     * @param id value of id
     */
    @Override
    public void setContestantId(int id) {
        this.contestantId = id;
    }

    /**
     * Checks if the contestant is currently participating in a trial.
     *
     * @return True if the contestant is playing, false otherwise.
     */
    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Sets the playing state of the contestant.
     *
     * @param playing True if the contestant is now playing, false otherwise.
     */
    @Override
    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    /**
     * Adjusts the contestant's strength based on their playing state.
     * If playing, strength decreases by 1 (up to a minimum of {@link SimulationParams#MINSTRENGTH}).
     * If not playing, strength increases by 1 (up to a maximum of {@link SimulationParams#MAXSTRENGTH}).
     */
    @Override
    public void manageStrength() {
        if (isPlaying && contestantStrength > 0) {
            contestantStrength = contestantStrength - 1;
        } else if (!isPlaying && contestantStrength > 0) {
            contestantStrength = contestantStrength + 1;
        }
    }

    /**
     * Life cycle of the service provider agent.
     */
    @Override
    public void run() {
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject();                     // get service request
        try {
            outMessage = contestantBenchInterface.processAndReply(inMessage);         // process it
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);                                // send service reply
        sconi.close();                                                // close the communication channel
    }
}
