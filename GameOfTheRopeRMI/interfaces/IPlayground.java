package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPlayground extends Remote {
    /**
     * Initiates the trial, updating referee state, trial count, and notifying coaches.
     */
    void callTrial() throws RemoteException;

    /**
     * Referee wait for the last coach to signal it's ready to
     * Wake up the contestants
     */
    void startTrial() throws RemoteException;

    /**
     * Assert trial results and updates the game status accordingly.
     *
     * @return True if this trial has concluded the game, false otherwise.
     */
    boolean assertTrialDecision() throws RemoteException;

    /**
     * Coach waits for every contestant of its team to be ready and
     * informs the referee that the team is ready.
     */
    void informReferee() throws RemoteException;

    /**
     * Contestant signals it's ready for the trial,
     * calculates team power, and waits for the referee to signal the trial start.
     */
    void getReady() throws RemoteException;

    /**
     * Contestant signals that it's done pulling the rope
     */
    void amIDone() throws RemoteException;

    /**
     * Operation server shutdown.
     * <p>
     * New operation.
     */
    void endOperation(String entity, int id) throws RemoteException;

    /**
     * Operation shut down
     */
    void shutdown() throws RemoteException;
}
