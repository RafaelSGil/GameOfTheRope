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
    ReturnCoach callContestants(int team, int strategy) throws RemoteException;

    /**
     * Contestants will wait until the coach wakes them up,
     * checking whether they were chosen to play or not,
     * acting accordingly
     */
    ReturnContestant followCoachAdvice(int contId, int contTeam, int contStrength) throws RemoteException;

    /**
     * Contestants, which have played in the last trial, will wait until the referee signals the end of the trial
     */
    ReturnContestant seatDown(int contId, int contTeam, int contStrength, boolean isPlaying) throws RemoteException;

    /**
     * Coaches will wait until the referee signals the end of the trial
     */
    ReturnCoach reviewNotes(int team) throws RemoteException;

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
