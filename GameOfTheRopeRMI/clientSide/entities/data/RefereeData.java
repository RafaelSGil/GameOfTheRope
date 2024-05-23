package clientSide.entities.data;

import clientSide.entities.RefereeStates;

/**
 * This class encapsulates data related to the Referee entity in the Game of the Rope simulation.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class RefereeData {

    /**
     * The current state of the Referee, represented by an integer value.
     * The specific meaning of state values is defined in the {@link RefereeStates} enum.
     */
    private int state;

    /**
     * Creates a new RefereeData instance.
     */
    public RefereeData() {
        this.state = RefereeStates.STARTMATCH;
    }

    /**
     * Retrieves the current state of the Referee.
     *
     * @return The integer value representing the Referee's state.
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the current state of the Referee.
     *
     * @param state The new state value for the Referee (an element of the RefereeStates enum).
     */
    public void setState(int state) {
        this.state = state;
    }
}
