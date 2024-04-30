package clientSide.entities;

import clientSide.stubs.*;
import serverSide.sharedRegions.*;
import serverSide.main.SimulationParams;


/**
 * This class represents the Referee entity in the game of the rope simulation.
 * The referee is responsible for managing the match, including starting games,
 * overseeing trials, announcing results, and declaring the overall winner.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class Referee extends Thread {

    /**
     * Internal state of the referee. Possible states are defined in {@link RefereeStates}.
     */
    private int refereeSate;

    /**
     * Reference to the {@link ContestantsBench} object.
     */
    private final ContestantsBenchStub bench;

    /**
     * Reference to the {@link RefereeSite} object.
     */
    private final RefereeSiteStub refereeSite;

    /**
     * Reference to the {@link Playground} object
     */
    private final PlaygroundStub playground;

    /**
     * Current game number (1 to {@link SimulationParams#GAMES}
     */
    private int game;

    /**
     * Current Trial number (1 to {@link SimulationParams#NTRIALS})
     */
    private int trial;

    /**
     * Stores the reason for a team's victory if applicable.
     */
    private String winCause;

    /**
     * Stores the results for each trial of each game
     * 3 rows --> 3 games
     * 6 columns --> 6 trials
     * -1 --> team 1 won
     * 0 --> draw
     * 1 --> team 2 won
     */
    private final int[] matchRecords;

    /**
     * Creates a new Referee instance
     *
     * @param threadName  The name to be assigned to the referee thread.
     * @param refereeSite The {@link RefereeSite} object
     * @param playground  The {@link Playground} object
     * @param bench       The {@link ContestantsBench} object
     */
    public Referee(String threadName, RefereeSiteStub refereeSite, PlaygroundStub playground, ContestantsBenchStub bench) {
        super(threadName);
        this.playground = playground;
        this.refereeSite = refereeSite;
        this.bench = bench;
        this.refereeSate = RefereeStates.STARTMATCH;
        this.game = 0;
        this.trial = 0;
        this.matchRecords = new int[SimulationParams.GAMES];
    }

    /**
     * Gets the current state of the referee.
     *
     * @return The current referee state as defined in {@link RefereeStates}.
     */
    public synchronized int getRefereeSate() {
        return refereeSate;
    }

    /**
     * Sets the internal state of the referee.
     *
     * @param refereeSate The new state for the referee.
     */
    public synchronized void setRefereeSate(int refereeSate) {
        this.refereeSate = refereeSate;
    }

    /**
     * Gets the current game number (1 to {@link SimulationParams#GAMES}).
     *
     * @return The current game number.
     */
    public synchronized int getGame() {
        return game;
    }

    /**
     * Sets the current game number.
     *
     * @param game The new game number.
     */
    public synchronized void setGame(int game) {
        this.game = game;
    }

    /**
     * Gets the current trial number within the game.
     *
     * @return The current trial number.
     */
    public synchronized int getTrial() {
        return trial;
    }

    /**
     * Sets the current trial number within the game.
     *
     * @param trial The new trial number.
     */
    public synchronized void setTrial(int trial) {
        this.trial = trial;
    }

    /**
     * Sets the result of a completed trial (-1: team 1 won, 0: draw, 1: team 2 won).
     *
     * @param result The result of the trial.
     */
    public synchronized void setGameResult(int result) {
        this.matchRecords[game - 1] = result;
    }

    /**
     * Gets the reason provided for a team's victory in the current game.
     *
     * @return The reason provided for a team's victory in the current game.
     */

    public String getWinCause() {
        return winCause;
    }

    /**
     * Sets the team win cause
     *
     * @param winCause The reason for the team's victory.
     */
    public void setWinCause(String winCause) {
        this.winCause = winCause;
    }

    /**
     * Retrieves the outcome of a specific game (-1: team 1 won, 0: draw, 1: team 2 won).
     *
     * @param game The game number (1 to {@link SimulationParams#GAMES}).
     * @return The result of the specified game.
     */

    public synchronized int getGameResult(int game) {
        return matchRecords[game];
    }

    /**
     * Calculates the overall match winner and presents the results.
     *
     * @return A string describing the final match results.
     */
    public synchronized String finalResults() {
        int team1 = 0;
        int team2 = 0;

        for (int i = 0; i < SimulationParams.GAMES; i++) {
            switch (getGameResult(i)) {
                case -1:
                    team1++;
                    break;
                case 1:
                    team2++;
                    break;
                case 0:
                    break;
            }
        }

        StringBuilder sb = new StringBuilder();

        if (team1 == team2) {
            return "Match was a draw";
        } else if (team1 > team2) {
            sb.append("Match was won by team " + 1);
        } else {
            sb.append("Match was won by team " + 2);
        }

        sb.append(" (").append(team1).append("-").append(team2).append(").");

        return sb.toString();
    }

    /**
     * Signals the end of the match to the {@link RefereeSite} by setting the corresponding flag.
     */
    public synchronized void signalMatchEnded() {
        refereeSite.setMatchEnd();
    }

    /**
     * The main execution loop of the Referee thread.
     */
    @Override
    public void run() {
        waitForGameStart();
        for (int i = 0; i < SimulationParams.GAMES; ++i) {
            refereeSite.announceNewGame();
            do {
                playground.callTrial(bench);
                playground.startTrial();
            } while (!playground.assertTrialDecision(bench));
            refereeSite.declareGameWinner();
        }
        refereeSite.declareMatchWinner();
    }

    /**
     * Introduces a simulated delay before the match begins.
     */
    private void waitForGameStart() {
        try {
            sleep((long) (1 + 50 * Math.random()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}