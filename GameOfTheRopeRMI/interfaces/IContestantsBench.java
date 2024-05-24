package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IContestantsBench extends Remote {
    /**
     * Signal the coaches and contestants to wake up
     */
    void unblockContestantBench() throws RemoteException;

    /**
     * start of new trial iteration
     * wake up the coaches
     */
    void refereeCallTrial() throws RemoteException;

    /**
     * Sets the value of attribute hasTrialEnded
     *
     * @param hasTrialEnded new value for the attribute
     */
    void setHasTrialEnded(boolean hasTrialEnded) throws RemoteException;

    /**
     * Coaches will wait until they receive a signal from the referee and until their team is not ready
     * Afterward, pick which contestants will be playing and which will be benched
     * Wake up the contestatns
     *
     * @param team to which team does the coach belong
     */
    void callContestants(int team) throws RemoteException;

    /**
     * Contestants will wait until the coach wakes them up,
     * checking whether they were chosen to play or not,
     * acting accordingly
     */
    void followCoachAdvice() throws RemoteException;

    /**
     * Contestants, which have played in the last trial, will wait until the referee signals the end of the trial
     */
    void seatDown() throws RemoteException;

    /**
     * Coaches will wait until the referee signals the end of the trial
     */
    void reviewNotes() throws RemoteException;

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
