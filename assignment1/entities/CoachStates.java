package assignment1.entities;

/**
 * This class defines the possible states that a {@link Coach} can be in during the game of the rope simulation.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class CoachStates {
    /**
     * The coach is waiting for a command from the referee.
     */
    public static final int WATFORREFEREECOMMAND = 0;
    /**
     * The coach is assembling their team for the next trial.
     */
    public static final int ASSEMBLETEAM = 1;

    /**
     * The coach is watching the current trial taking place.
     */
    public static final int WATCHTRIAL = 2;

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private CoachStates() {
    }
}
