package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * General repository interface.
 * Declares the operations that can be performed on the general repository.
 * Extends the Remote interface, to be able to make RMI calls.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public interface IGeneralRepository extends Remote {
    /**
     * Operation initialization of simulation.
     * <p>
     * New operation.
     *
     * @param logFileName name of the logging file
     */
    void initSimul(String logFileName) throws RemoteException;

    /**
     * Set the new value of the attribute game
     *
     * @param game new value
     */
    void setGame(int game) throws RemoteException;

    /**
     * Get the value of the attribute game
     *
     * @return value of game
     */
    int getGame() throws RemoteException;

    /**
     * Get the value of the attribute gameWinMsg
     *
     * @return value of gameWinMsg
     */
    String getGameWinMsg() throws RemoteException;

    /**
     * Set the new value of the attribute gameWinMsg
     *
     * @param gameWinMsg new value
     */
    void setGameWinMsg(String gameWinMsg) throws RemoteException;

    /**
     * Get the value of the attribute trial
     *
     * @return value of the attribute trail
     */
    int getTrial() throws RemoteException;

    /**
     * Sets the trial count for the simulation, limiting it to the maximum number of trials defined in SimulationParams.
     *
     * @param trial The trial count to set.
     */
    void setTrial(int trial) throws RemoteException;

    /**
     * Retrieves the current position of the rope.
     *
     * @return The current position of the rope.
     */
    int getRopePosition() throws RemoteException;

    /**
     * Sets the position of the rope.
     *
     * @param ropePosition The position to set for the rope.
     */
    void setRopePosition(int ropePosition) throws RemoteException;

    /**
     * Updates the state of the referee and reports the status.
     *
     * @param refereeState The new state of the referee.
     */
    void updateReferee(int refereeState) throws RemoteException;

    /**
     * Updates the state, strength, and team of a contestant identified by their ID, and reports the status.
     *
     * @param contestantId       The ID of the contestant to update.
     * @param contestantStrength The strength of the contestant.
     * @param contestantState    The state of the contestant.
     * @param contestantTeam     The team of the contestant.
     */
    void updateContestant(int contestantId, int contestantStrength, int contestantState, int contestantTeam) throws RemoteException;

    /**
     * Updates the state of a coach and reports the status.
     *
     * @param coachState The new state of the coach.
     * @param coachTeam  The team of the coach.
     */
    void updateCoach(int coachState, int coachTeam) throws RemoteException;

    /**
     * Declares the winner of the game.
     *
     * @param team  The winning team.
     * @param cause The cause of the win.
     */
    void declareGameWinner(int team, String cause) throws RemoteException;

    /**
     * Declares the winner of the match.
     *
     * @param msg The message declaring the match winner.
     */
    void declareMatchWinner(String msg) throws RemoteException;

    /**
     * Reports the current status of the game.
     *
     * @param header whether the header should be printed or not
     */
    void reportStatus(boolean header) throws RemoteException;

    /**
     * Reports the status of the game after its completion.
     */
    void reportGameStatus() throws RemoteException;

    /**
     * Reports the status of the game start.
     */
    void reportGameStart() throws RemoteException;

    /**
     * Operation server shutdown.
     * <p>
     * New operation.
     */
    void shutdown() throws RemoteException;
}
