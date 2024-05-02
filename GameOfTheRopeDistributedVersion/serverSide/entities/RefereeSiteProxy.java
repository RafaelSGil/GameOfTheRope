package serverSide.entities;

import clientSide.entities.*;
import clientSide.stubs.RefereeSiteStub;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.main.SimulationParams;
import serverSide.sharedRegions.RefereeSite;
import serverSide.sharedRegions.RefereeSiteInterface;


public class RefereeSiteProxy extends Thread implements RefereeCloning {

    /**
     *  Communication channel.
     */

    private ServerCom sconi;

    /**
     * Interface for the referee site
     *
     */
    private RefereeSiteInterface refereeSiteInterface;

    /**
     * Internal state of the referee. Possible states are defined in {@link RefereeStates}.
     */
    private int refereeState;

    /**
     * Current game number (1 to {@link SimulationParams#GAMES}
     */
    private int game;

    /**
     * Current Trial number (1 to {@link SimulationParams#NTRIALS})
     */
    private int trial;

    /**
     * Stores the reason for a team's victory if applicable.
     */
    private String winCause;

    /**
     * Stores the results for each trial of each game
     * 3 rows --> 3 games
     * 6 columns --> 6 trials
     * -1 --> team 1 won
     * 0 --> draw
     * 1 --> team 2 won
     */
    private final int[] matchRecords;

    /**
     * flag for Match end
     */
    private boolean matchEnd;

    /**
     * Final results of the match
     */
    private String finalResults;

    /**
     *  Number of instantiated threads.
     */
    private static int nProxy = 0;

    private static int getProxyId ()
    {
        Class<?> cl = null;                                            // representation of the BarberShopClientProxy object in JVM
        int proxyId;                                                   // instantiation identifier

        try
        { cl = Class.forName ("serverSide.entities.RefereeSiteProxy");
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("Data type RefereeSiteProxy was not found!");
            e.printStackTrace ();
            System.exit (1);
        }
        synchronized (cl)
        { proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    public RefereeSiteProxy(ServerCom sconi, RefereeSiteInterface refereeSiteInterface) {
        super("RefereeSiteProxy_" + RefereeSiteProxy.getProxyId());
        this.sconi = sconi;
        this.refereeSiteInterface = refereeSiteInterface;
        this.matchRecords = new int[SimulationParams.GAMES];
    }



    @Override
    public void run() {
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject ();                     // get service request
        try{
            outMessage = refereeSiteInterface.processAndReply (inMessage);         // process it
        }
        catch (MessageException e){
            GenericIO.writelnString ("Thread " + getName () + ": " + e.getMessage () + "!");
            GenericIO.writelnString (e.getMessageVal ().toString ());
            System.exit (1);
        }
        sconi.writeObject (outMessage);                                // send service reply
        sconi.close ();                                                // close the communication channel
    }

    /**
     * Gets the current state of the referee.
     *
     * @return The current referee state as defined in {@link RefereeStates}.
     */
    @Override
    public int getRefereeSate() {
        return refereeState;
    }

    /**
     * Sets the internal state of the referee.
     *
     * @param refereeSate The new state for the referee.
     */
    @Override
    public void setRefereeSate(int refereeSate) {
        this.refereeState = refereeSate;
    }

    /**
     * Gets the current game number (1 to {@link SimulationParams#GAMES}).
     *
     * @return The current game number.
     */
    @Override
    public int getGame() {
        return game;
    }

    /**
     * Sets the current game number.
     *
     * @param game The new game number.
     */
    @Override
    public void setGame(int game) {
        this.game = game;
    }

    /**
     * Gets the current trial number within the game.
     *
     * @return The current trial number.
     */
    @Override
    public int getTrial() {
        return trial;
    }

    /**
     * Sets the current trial number within the game.
     *
     * @param trial The new trial number.
     */
    @Override
    public void setTrial(int trial) {
        this.trial = trial;
    }

    /**
     * Sets the result of a completed trial (-1: team 1 won, 0: draw, 1: team 2 won).
     *
     * @param result The result of the trial.
     */
    @Override
    public void setGameResult(int result) {
        this.matchRecords[game - 1] = result;
    }

    /**
     * Gets the reason provided for a team's victory in the current game.
     *
     * @return The reason provided for a team's victory in the current game.
     */
    @Override
    public String getWinCause() {
        return winCause;
    }

    /**
     * Sets the team win cause
     *
     * @param winCause The reason for the team's victory.
     */
    @Override
    public void setWinCause(String winCause) {
        this.winCause = winCause;
    }

    /**
     * Retrieves the outcome of a specific game (-1: team 1 won, 0: draw, 1: team 2 won).
     *
     * @param game The game number (1 to {@link SimulationParams#GAMES}).
     * @return The result of the specified game.
     */
    @Override
    public int getGameResult(int game) {
        return matchRecords[game];
    }

    /**
     * Calculates the overall match winner and presents the results.
     *
     * @return A string describing the final match results.
     */
    @Override
    public String finalResults() {
        return finalResults;
    }

    /**
     * Sets the value of the final result of the match
     *
     * @param finalResults value to be set
     */
    @Override
    public void setFinalResults(String finalResults) {
        this.finalResults = finalResults;
    }


    /**
     * Signals the end of the match to the {@link RefereeSite} by setting the corresponding flag.
     */
    @Override
    public void signalMatchEnded() {
        matchEnd = true;
    }

    /**
     * Retrieve the value of the flag
     *
     * @return whether the game ended or not
     */
    @Override
    public boolean getMatchEnd() {
        return matchEnd;
    }

}
