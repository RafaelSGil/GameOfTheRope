package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRefereeSite extends Remote {
    /**
     * Announces the start of a new game to all entities.
     * Updates the game number, trial number, and referee state in the repository.
     *
     * @param game current game
     * @param startMatch whether the match just started or not
     */
    ReturnReferee announceNewGame(int game, boolean startMatch) throws RemoteException;

    /**
     * Declares the winner of a completed game based on the trial results.
     * Updates the referee state.
     */
    ReturnReferee declareGameWinner(int gameResult, String winCause) throws RemoteException;

    /**
     * Declares the winner of the entire match based on the final results.
     * Updates the referee state and inform that the match is ended.
     */
    ReturnReferee declareMatchWinner(String finalResults) throws RemoteException;

    /**
     * Check if the match is ended or not
     *
     * @return True if the match has ended, false otherwise.
     */
    boolean endOfMatch() throws RemoteException;

    /**
     * Set the match end flag to true is the match is ended, false otherwise
     *
     * @param matchEnd The new value for the match end flag.
     */
    void setMatchEnd(boolean matchEnd) throws RemoteException;

    /**
     * Operation server shutdown.
     * <p>
     * New operation.
     */
    void endOperation() throws RemoteException;

    /**
     * Operation shut down
     */
    void shutdown() throws RemoteException;
}
