package clientSide.entities;

/**
 * This class defines the possible states that a {@link Contestant} can be in during the game of the rope simulation.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class ContestantStates {
    /**
     * The contestant is seated on the bench, waiting for instructions.
     */
    public static final int SEATATBENCH = 0;

    /**
     * The contestant is standing in position on the playground, ready to pull the rope.
     */
    public static final int STANDINPOSITION = 1;
    /**
     * The contestant is actively pulling the rope during a trial, giving their best effort.
     */
    public static final int DOYOURBEST = 2;

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private ContestantStates() {
    }

}
