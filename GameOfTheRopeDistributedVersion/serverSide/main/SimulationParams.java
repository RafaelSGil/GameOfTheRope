package serverSide.main;

/**
 * This class defines constants representing various parameters used in the Game of the Rope simulation.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class SimulationParams {

    /**
     * The total number of contestants participating in the simulation.
     */
    public static final int NCONTESTANTS = 10;

    /**
     * The number of teams competing in the Game Of The Rope
     */
    public static final int NTEAMS = 2;

    /**
     * The number of trials in each game.
     */
    public static final int NTRIALS = 6;

    /**
     * The number of players per team
     */
    public static final int NPLAYERS = NCONTESTANTS / 2;

    /**
     * The number of players actively pulling the rope in each trial.
     */
    public static final int NPLAYERSINCOMPETITION = (NPLAYERS / 2) + 1;

    /**
     * The number of games played in the entire match.
     */
    public static final int GAMES = 3;

    /**
     * The maximum possible strength value for a contestant.
     */
    public static final int MAXSTRENGTH = 10;

    /**
     * The minimum possible strength value for a contestant.
     */
    public static final int MINSTRENGTH = 6;

    /**
     *   Number of entities requesting shutdown.
     */
    public static final int NENTITIES = 3;

    /**
     * String identification for contestants
     */
    public static final String CONTESTANT = "contestant";

    /**
     * String identification for coaches
     */
    public static final String COACH = "coach";

    /**
     * String identification for referee
     */
    public static final String REFEREE = "referee";
}
