package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Playground interface.
 * Declares the operations that can be performed on the playground.
 * Extends the Remote interface, to be able to make RMI calls.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public interface IPlayground extends Remote {
    /**
     * Initiates the trial, updating referee state, trial count, and notifying coaches.
     */
    ReturnReferee callTrial(int trial) throws RemoteException;

    /**
     * Referee wait for the last coach to signal it's ready to
     * Wake up the contestants
     */
    ReturnReferee startTrial() throws RemoteException;

    /**
     * Assert trial results and updates the game status accordingly.
     */
    ReturnReferee assertTrialDecision(int trial, int game) throws RemoteException;

    /**
     * Coach waits for every contestant of its team to be ready and
     * informs the referee that the team is ready.
     */
    ReturnCoach informReferee(int team) throws RemoteException;

    /**
     * Contestant signals it's ready for the trial,
     * calculates team power, and waits for the referee to signal the trial start.
     */
    ReturnContestant getReady(int contId, int contTeam, int contStrength) throws RemoteException;

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
