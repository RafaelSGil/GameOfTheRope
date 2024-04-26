package clientSide.entities;

import serverSide.main.SimulationParams;
import serverSide.sharedRegions.RefereeSite;

public interface RefereeCloning {

    /**
     * Gets the current state of the referee.
     *
     * @return The current referee state as defined in {@link RefereeStates}.
     */
    public int getRefereeSate();

    /**
     * Sets the internal state of the referee.
     *
     * @param refereeSate The new state for the referee.
     */
    public void setRefereeSate(int refereeSate);

    /**
     * Gets the current game number (1 to {@link SimulationParams#GAMES}).
     *
     * @return The current game number.
     */
    public int getGame();

    /**
     * Sets the current game number.
     *
     * @param game The new game number.
     */
    public void setGame(int game);

    /**
     * Gets the current trial number within the game.
     *
     * @return The current trial number.
     */
    public int getTrial();

    /**
     * Sets the current trial number within the game.
     *
     * @param trial The new trial number.
     */
    public void setTrial(int trial);

    /**
     * Sets the result of a completed trial (-1: team 1 won, 0: draw, 1: team 2 won).
     *
     * @param result The result of the trial.
     */
    public void setGameResult(int result) ;

    /**
     * Gets the reason provided for a team's victory in the current game.
     *
     * @return The reason provided for a team's victory in the current game.
     */

    public String getWinCause();

    /**
     * Sets the team win cause
     *
     * @param winCause The reason for the team's victory.
     */
    public void setWinCause(String winCause);

    /**
     * Retrieves the outcome of a specific game (-1: team 1 won, 0: draw, 1: team 2 won).
     *
     * @param game The game number (1 to {@link SimulationParams#GAMES}).
     * @return The result of the specified game.
     */

    public int getGameResult(int game);

    /**
     * Calculates the overall match winner and presents the results.
     *
     * @return A string describing the final match results.
     */
    public String finalResults();

    /**
     * Signals the end of the match to the {@link RefereeSite} by setting the corresponding flag.
     */
    public void signalMatchEnded();
}
