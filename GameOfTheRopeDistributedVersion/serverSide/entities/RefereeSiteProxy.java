package serverSide.entities;

import clientSide.entities.*;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.main.SimulationParams;
import serverSide.sharedRegions.RefereeSite;
import serverSide.sharedRegions.RefereeSiteInterface;


public class RefereeSiteProxy extends Thread implements CoachCloning, ContestantCloning, RefereeCloning {

    /**
     *  Communication channel.
     */

    private ServerCom sconi;

    /**
     * Interface for the referee site
     *
     */
    private RefereeSiteInterface refereeSiteInterface;

    public RefereeSiteProxy(ServerCom sconi, RefereeSiteInterface refereeSiteInterface) {
        this.sconi = sconi;
        this.refereeSiteInterface = refereeSiteInterface;
    }

    /**
     * Get coach state
     * @return coach state
     */
    @Override
    public int getCoachState() {
        return 0;
    }

    /**
     * Sets the internal state of the coach.
     *
     * @param coachState The new state for the coach.
     */
    @Override
    public void setCoachState(int coachState) {

    }

    /**
     * Gets the team that the coach belongs to.
     *
     * @return The team number (0 or 1).
     */
    @Override
    public int getCoachTeam() {
        return 0;
    }

    /**
     * Get the strategy
     *
     * @return strategy value
     */
    @Override
    public int getStrategy() {
        return 0;
    }

    /**
     * Set the new strategy
     *
     * @param strategy the new strategy
     */
    @Override
    public void setStrategy(int strategy) {

    }

    /**
     * Gets the current state of the contestant.
     *
     * @return The current contestant state as defined in {@link ContestantStates}.
     */
    @Override
    public int getContestantState() {
        return 0;
    }

    /**
     * Sets the internal state of the contestant.
     *
     * @param contestantState The new state for the contestant.
     */
    @Override
    public void setContestantState(int contestantState) {

    }

    /**
     * Gets the team that the contestant belongs to.
     *
     * @return The team number (0 or 1).
     */
    @Override
    public int getContestantTeam() {
        return 0;
    }

    /**
     * Sets the team that the contestant belongs to.
     *
     * @param contestantTeam The new team number (0 or 1).
     */
    @Override
    public void setContestantTeam(int contestantTeam) {

    }

    /**
     * Gets the current strength level of the contestant.
     *
     * @return The contestant's strength as an integer between {@link SimulationParams#MINSTRENGTH} and {@link SimulationParams#MAXSTRENGTH}.
     */
    @Override
    public int getContestantStrength() {
        return 0;
    }

    /**
     * Sets the strength level of the contestant.
     *
     * @param contestantStrength The contestant's strength as an integer between {@link SimulationParams#MINSTRENGTH} and {@link SimulationParams#MAXSTRENGTH}.
     */
    @Override
    public void setContestantStrength(int contestantStrength) {

    }

    /**
     * Gets the unique identifier for the contestant.
     *
     * @return The contestant's ID.
     */
    @Override
    public int getContestantId() {
        return 0;
    }

    /**
     * Checks if the contestant is currently participating in a trial.
     *
     * @return True if the contestant is playing, false otherwise.
     */
    @Override
    public boolean isPlaying() {
        return false;
    }

    /**
     * Sets the playing state of the contestant.
     *
     * @param playing True if the contestant is now playing, false otherwise.
     */
    @Override
    public void setPlaying(boolean playing) {

    }

    /**
     * Gets the current state of the referee.
     *
     * @return The current referee state as defined in {@link RefereeStates}.
     */
    @Override
    public int getRefereeSate() {
        return 0;
    }

    /**
     * Sets the internal state of the referee.
     *
     * @param refereeSate The new state for the referee.
     */
    @Override
    public void setRefereeSate(int refereeSate) {

    }

    /**
     * Gets the current game number (1 to {@link SimulationParams#GAMES}).
     *
     * @return The current game number.
     */
    @Override
    public int getGame() {
        return 0;
    }

    /**
     * Sets the current game number.
     *
     * @param game The new game number.
     */
    @Override
    public void setGame(int game) {

    }

    /**
     * Gets the current trial number within the game.
     *
     * @return The current trial number.
     */
    @Override
    public int getTrial() {
        return 0;
    }

    /**
     * Sets the current trial number within the game.
     *
     * @param trial The new trial number.
     */
    @Override
    public void setTrial(int trial) {

    }

    /**
     * Sets the result of a completed trial (-1: team 1 won, 0: draw, 1: team 2 won).
     *
     * @param result The result of the trial.
     */
    @Override
    public void setGameResult(int result) {

    }

    /**
     * Gets the reason provided for a team's victory in the current game.
     *
     * @return The reason provided for a team's victory in the current game.
     */
    @Override
    public String getWinCause() {
        return null;
    }

    /**
     * Sets the team win cause
     *
     * @param winCause The reason for the team's victory.
     */
    @Override
    public void setWinCause(String winCause) {

    }

    /**
     * Retrieves the outcome of a specific game (-1: team 1 won, 0: draw, 1: team 2 won).
     *
     * @param game The game number (1 to {@link SimulationParams#GAMES}).
     * @return The result of the specified game.
     */
    @Override
    public int getGameResult(int game) {
        return 0;
    }

    /**
     * Calculates the overall match winner and presents the results.
     *
     * @return A string describing the final match results.
     */
    @Override
    public String finalResults() {
        return null;
    }

    /**
     * Signals the end of the match to the {@link RefereeSite} by setting the corresponding flag.
     */
    @Override
    public void signalMatchEnded() {

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
}
