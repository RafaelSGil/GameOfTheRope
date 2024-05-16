package clientSide.entities;

import serverSide.main.SimulationParams;
/**
 *  Contestant Cloning
 *
 *  It specifies his own attributes
 *  Implementation of a client-server model of type 2 (server replication).
 *  Communication is based on a communication channel under the TCP protocol.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public interface ContestantCloning {
    /**
     * Gets the current state of the contestant.
     *
     * @return The current contestant state as defined in {@link ContestantStates}.
     */
    int getContestantState();

    /**
     * Sets the internal state of the contestant.
     *
     * @param contestantState The new state for the contestant.
     */
    void setContestantState(int contestantState);

    /**
     * Gets the team that the contestant belongs to.
     *
     * @return The team number (0 or 1).
     */
    int getContestantTeam();

    /**
     * Sets the team that the contestant belongs to.
     *
     * @param contestantTeam The new team number (0 or 1).
     */
    void setContestantTeam(int contestantTeam);

    /**
     * Gets the current strength level of the contestant.
     *
     * @return The contestant's strength as an integer between {@link SimulationParams#MINSTRENGTH} and {@link SimulationParams#MAXSTRENGTH}.
     */

    int getContestantStrength();

    /**
     * Sets the strength level of the contestant.
     *
     * @param contestantStrength The contestant's strength as an integer between {@link SimulationParams#MINSTRENGTH} and {@link SimulationParams#MAXSTRENGTH}.
     */
    void setContestantStrength(int contestantStrength);

    /**
     * Gets the unique identifier for the contestant.
     *
     * @return The contestant's ID.
     */
    int getContestantId();

    /**
     * Set contestant id
     * @param id value of id
     */
    void setContestantId(int id);


    /**
     * Checks if the contestant is currently participating in a trial.
     *
     * @return True if the contestant is playing, false otherwise.
     */
    boolean isPlaying();

    /**
     * Sets the playing state of the contestant.
     *
     * @param playing True if the contestant is now playing, false otherwise.
     */
    void setPlaying(boolean playing);

    /**
     * Adjusts the contestant's strength based on their playing state.
     * If playing, strength decreases by 1 (up to a minimum of {@link serverSide.main.SimulationParams#MINSTRENGTH}).
     * If not playing, strength increases by 1 (up to a maximum of {@link serverSide.main.SimulationParams#MAXSTRENGTH}).
     */
    void manageStrength();
}
