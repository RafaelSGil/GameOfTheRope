package clientSide.entities;

import serverSide.main.SimulationParams;

public interface ContestantCloning {
    /**
     * Gets the current state of the contestant.
     *
     * @return The current contestant state as defined in {@link ContestantStates}.
     */
    public int getContestantState();

    /**
     * Sets the internal state of the contestant.
     *
     * @param contestantState The new state for the contestant.
     */
    public void setContestantState(int contestantState);

    /**
     * Gets the team that the contestant belongs to.
     *
     * @return The team number (0 or 1).
     */
    public int getContestantTeam();

    /**
     * Sets the team that the contestant belongs to.
     *
     * @param contestantTeam The new team number (0 or 1).
     */
    public void setContestantTeam(int contestantTeam);

    /**
     * Gets the current strength level of the contestant.
     *
     * @return The contestant's strength as an integer between {@link SimulationParams#MINSTRENGTH} and {@link SimulationParams#MAXSTRENGTH}.
     */

    public int getContestantStrength();

    /**
     * Sets the strength level of the contestant.
     *
     * @param contestantStrength The contestant's strength as an integer between {@link SimulationParams#MINSTRENGTH} and {@link SimulationParams#MAXSTRENGTH}.
     */
    public void setContestantStrength(int contestantStrength);

    /**
     * Gets the unique identifier for the contestant.
     *
     * @return The contestant's ID.
     */
    public int getContestantId();


    /**
     * Checks if the contestant is currently participating in a trial.
     *
     * @return True if the contestant is playing, false otherwise.
     */
    public boolean isPlaying();

    /**
     * Sets the playing state of the contestant.
     *
     * @param playing True if the contestant is now playing, false otherwise.
     */
    public void setPlaying(boolean playing);
}
