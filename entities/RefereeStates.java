package entities;
/**
 * This class defines the possible states that the {@link Referee} can be in during the game of the rope simulation
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class RefereeStates {

    /**
     * The initial state before the match begins.
     * */
    public static final int STARTMATCH = 0;

    /**
     * The state when a new game is about to start.
     * */
    public static final int STARTGAME = 1;

    /**
     * The state when both teams are ready to begin a game.
     * */
    public static final int TEAMSREADY = 2;

    /**
     * The state during a trial, waiting for a decision from the Playground.
     * */
    public static final int WAITTRIALCONCLUSION = 3;

    /**
     * The state after a game has ended.
     * */
    public static final int ENDGAME = 4;

    /**
     *  The state after all games have been played and the match is over.
     *  */
    public static final int ENDMATCH = 5;

    /**
     * Private constructor to prevent instantiation.
     */
    private RefereeStates(){}
}
