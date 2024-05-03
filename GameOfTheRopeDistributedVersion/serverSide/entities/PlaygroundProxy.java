package serverSide.entities;


import clientSide.entities.*;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.main.SimulationParams;
import serverSide.sharedRegions.PlaygroundInterface;
import serverSide.sharedRegions.RefereeSite;


public class PlaygroundProxy extends Thread implements CoachCloning, ContestantCloning, RefereeCloning {

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
    private boolean isPlaying;

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
    private boolean matchEnd = false;

    /**
     * Final results of the match
     */
    private String finalResults;

    /**
     *  Number of instantiated threads.
     */
    private static int nProxy = 0;


    /**
     *  Communication channel.
     */

    private ServerCom sconi;

    /**
     * Interface for the playground
     *
     */
    private PlaygroundInterface playgroundInterface;

    private static int getProxyId ()
    {
        Class<?> cl = null;                                            // representation of the BarberShopClientProxy object in JVM
        int proxyId;                                                   // instantiation identifier

        try
        { cl = Class.forName ("serverSide.entities.PlaygroundProxy");
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("Data type PlaygroundProxy was not found!");
            e.printStackTrace ();
            System.exit (1);
        }
        synchronized (cl)
        { proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    public PlaygroundProxy(ServerCom sconi, PlaygroundInterface playgroundInterface){
        super("PlaygroundProxy_" + PlaygroundProxy.getProxyId());
        this.sconi = sconi;
        this.playgroundInterface = playgroundInterface;
        this.matchRecords = new int[SimulationParams.GAMES];

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
        } else if(!isPlaying && contestantStrength >0){
            contestantStrength = contestantStrength + 1;
        }
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


    @Override
    public void run() {
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject ();                     // get service request
        try
        { outMessage = playgroundInterface.processAndReply (inMessage);         // process it
        }
        catch (MessageException e)
        { GenericIO.writelnString ("Thread " + getName () + ": " + e.getMessage () + "!");
            GenericIO.writelnString (e.getMessageVal ().toString ());
            System.exit (1);
        }
        sconi.writeObject (outMessage);                                // send service reply
        sconi.close ();                                                // close the communication channel
    }
}
