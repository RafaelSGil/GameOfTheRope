package sharedregions;

import entities.Coach;
import entities.Contestant;
import entities.ContestantStates;
import entities.Referee;
import entities.data.CoachData;
import entities.data.ContestantData;
import entities.data.RefereeData;
import genclass.GenericIO;
import genclass.TextFile;
import main.SimulationParams;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class represents a GeneralRepository entity in the game of the rope simulation.
 * Its purpose is to serve has a centralized data repository,
 * which will be printing any alterations and updates to the game data to a file,
 * in order to provide a way to check and debug the unfolding of the game
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */

public class GeneralRepository {
    /**
     * Reference to the {@link RefereeData} object
     * which holds the data relative to the {@link Referee} object
     */
    private final RefereeData referee;

    /**
     * Array to store the references to the {@link CoachData} object
     * which hold the data relevant to the {@link Coach} objects
     */
    private final CoachData[] coaches;

    /**
     * Array to store the references to the {@link ContestantData} object
     * which hold the data relevant to the {@link Contestant} objects
     */
    private final ContestantData[] contestants;
    /**
     * Store the current game
     */
    private int game;

    /**
     * Store the information about the winning cause of the game
     */
    private String gameWinMsg;

    /**
     * Store the current trial
     */
    private int trial;

    /**
     * Store the current position of the rope
     */
    private int ropePosition;

    /**
     * Store the name of the file to which will write
     */
    private final String fileName;

    /**
     * Creates a new GeneralRepository instance
     *
     * @param fileName name of the file to write to
     */
    public GeneralRepository(String fileName) {
        if ((fileName == null) || Objects.equals(fileName, ""))
            this.fileName = "logger";
        else this.fileName = fileName;

        this.referee = new RefereeData();
        coaches = new CoachData[SimulationParams.NTEAMS];
        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            coaches[i] = new CoachData(i);
        }

        contestants = new ContestantData[SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = new ContestantData(i);
        }

        this.game = 0;
        this.gameWinMsg = "";
        this.trial = 0;
        this.ropePosition = 0;

        reportInitialStatus();
    }

    /**
     * Set the new value of the attribute game
     *
     * @param game new value
     */
    public synchronized void setGame(int game) {
        this.game = game;
        reportGameStatus();
    }

    /**
     * Get the value of the attribute game
     *
     * @return value of game
     */
    public synchronized int getGame() {
        return game;
    }

    /**
     * Get the value of the attribute gameWinMsg
     *
     * @return value of gameWinMsg
     */
    public synchronized String getGameWinMsg() {
        return gameWinMsg;
    }

    /**
     * Set the new value of the attribute gameWinMsg
     *
     * @param gameWinMsg new value
     */
    public synchronized void setGameWinMsg(String gameWinMsg) {
        this.gameWinMsg = gameWinMsg;
    }

    /**
     * Get the value of the attribute trial
     *
     * @return value of the attribute trail
     */
    public synchronized int getTrial() {
        return trial;
    }

    /**
     * Sets the trial count for the simulation, limiting it to the maximum number of trials defined in SimulationParams.
     *
     * @param trial The trial count to set.
     */
    public synchronized void setTrial(int trial) {
        this.trial = Math.min(trial, SimulationParams.NTRIALS);
    }

    /**
     * Retrieves the current position of the rope.
     *
     * @return The current position of the rope.
     */
    public synchronized int getRopePosition() {
        return ropePosition;
    }

    /**
     * Sets the position of the rope.
     *
     * @param ropePosition The position to set for the rope.
     */
    public synchronized void setRopePosition(int ropePosition) {
        this.ropePosition = ropePosition;
    }

    /**
     * Updates the state of the referee and reports the status.
     *
     * @param refereeState The new state of the referee.
     */
    public synchronized void updateReferee(int refereeState) {
        referee.setState(refereeState);
        reportStatus();
    }

    /**
     * Updates the state, strength, and team of a contestant identified by their ID, and reports the status.
     *
     * @param contestantId       The ID of the contestant to update.
     * @param contestantStrength The strength of the contestant.
     * @param contestantState    The state of the contestant.
     * @param contestantTeam     The team of the contestant.
     */
    public synchronized void updateContestant(int contestantId, int contestantStrength, int contestantState, int contestantTeam) {
        try {
            contestants[contestantId].setState(contestantState);
            contestants[contestantId].setStrength(contestantStrength);
            contestants[contestantId].setTeam(contestantTeam);
        } catch (ArrayIndexOutOfBoundsException e) {
            GenericIO.writelnString("Error while updating contestant " + contestantId);
            System.exit(1);
        }

        reportStatus();
    }

    /**
     * Updates the state of a coach and reports the status.
     *
     * @param coachState The new state of the coach.
     * @param coachTeam  The team of the coach.
     */
    public synchronized void updateCoach(int coachState, int coachTeam) {
        try {
            coaches[coachTeam].setState(coachState);
        } catch (ArrayIndexOutOfBoundsException e) {
            GenericIO.writelnString("Error while updating coach " + coachTeam);
            System.exit(1);
        }
        reportStatus();
    }

    /**
     * Declares the winner of the game.
     *
     * @param team  The winning team.
     * @param cause The cause of the win.
     */
    public synchronized void declareGameWinner(int team, String cause) {
        if (!cause.equals("draw")) {
            gameWinMsg = " was won by team " + (team + 1) + " by " + cause + " in " + trial + " trials.";
        } else {
            gameWinMsg = " was a draw.";
        }
        reportGameStatus();
        gameWinMsg = "";
    }

    /**
     * Declares the winner of the match.
     *
     * @param msg The message declaring the match winner.
     */
    public synchronized void declareMatchWinner(String msg) {
        TextFile log = new TextFile();

        if (!log.openForAppending(".", fileName)) {
            GenericIO.writelnString("Failed creating " + fileName + " file.");
            System.exit(1);
        }

        log.writelnString(msg);

        if (!log.close()) {
            GenericIO.writelnString("The operation of closing the file " + fileName + " failed!");
            System.exit(1);
        }
    }

    /**
     * Reports the initial status of the game.
     */
    private synchronized void reportInitialStatus() {
        TextFile log = new TextFile();

        if (!log.openForWriting(".", fileName)) {
            GenericIO.writelnString("Failed creating " + fileName + " file.");
            System.exit(1);
        }

        log.writelnString("\t\t\t\t\t\tGame of the Rope - Description of the internal state");
        log.writelnString(printHeader());
        log.writelnString(printValues());
        log.writelnString();

        if (!log.close()) {
            GenericIO.writelnString("The operation of closing the file " + fileName + " failed!");
            System.exit(1);
        }
    }

    /**
     * Reports the current status of the game.
     */
    public synchronized void reportStatus() {
        TextFile log = new TextFile();

        if (!log.openForAppending(".", fileName)) {
            GenericIO.writelnString("Failed creating " + fileName + " file.");
            System.exit(1);
        }

        log.writelnString(printHeader());
        log.writelnString(printValues());
        log.writelnString();

        if (!log.close()) {
            GenericIO.writelnString("The operation of closing the file " + fileName + " failed!");
            System.exit(1);
        }
    }

    /**
     * Reports the status of the game after its completion.
     */
    public synchronized void reportGameStatus() {
        TextFile log = new TextFile();

        if (!log.openForAppending(".", fileName)) {
            GenericIO.writelnString("Failed creating " + fileName + " file.");
            System.exit(1);
        }

        log.writelnString(printGameInfo());

        if (!log.close()) {
            GenericIO.writelnString("The operation of closing the file " + fileName + " failed!");
            System.exit(1);
        }
    }

    /**
     * Generates and returns the header for the status report.
     *
     * @return The header string for the status report.
     */
    private synchronized String printHeader() {
        StringBuilder sb = new StringBuilder();

        sb.append("Ref\t");

        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            sb.append("Coa ").append(i + 1).append("\t");
            for (int j = 0; j < SimulationParams.NPLAYERS; j++) {
                sb.append("Cont ").append(j + 1).append("\t");
            }
        }

        sb.append("\tTrial\t");
        sb.append("\nSta\t");

        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            sb.append("Stat\t");
            for (int j = 0; j < SimulationParams.NPLAYERS; j++) {
                sb.append("Sta SG\t");
            }
        }

        for (int i = SimulationParams.NPLAYERSINCOMPETITION; i > 0; i--) {
            sb.append(i).append(" ");
        }

        sb.append(". ");

        for (int i = 1; i <= SimulationParams.NPLAYERSINCOMPETITION; i++) {
            sb.append(i).append(" ");
        }

        sb.append("\tNB\tPS");

        return sb.toString();
    }

    /**
     * Generates and returns the values for the status report.
     *
     * @return The values string for the status report.
     */
    private synchronized String printValues() {
        StringBuilder sb = new StringBuilder();

        sb.append(translateRefereeStates(referee.getState())).append("\t");

        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            sb.append(translateCoachStates(coaches[i].getState())).append("\t");
            for (int j = 0; j < SimulationParams.NCONTESTANTS; j++) {
                if (contestants[j].getTeam() == -1) {
                    sb.append("## ##\t");
                    if (j == SimulationParams.NPLAYERS - 1) break;
                    continue;
                }

                if (contestants[j].getTeam() == coaches[i].getTeam()) {
                    sb.append(translateContestantStates(contestants[j].getState())).append("\t")
                            .append(contestants[j].getStrength()).append("\t");
                }
            }
        }

        sb.append(getPos());

        sb.append("\t").append(trial).append("\t").append(ropePosition);

        return sb.toString();
    }

    /**
     * Generates and returns the positions of the teams on the rope.
     *
     * @return The positions of the teams on the rope.
     */
    private synchronized String getPos() {
        ArrayList<Integer> team1 = new ArrayList<>();
        ArrayList<Integer> team2 = new ArrayList<>();

        StringBuilder sb = new StringBuilder("- - - . - - -");

        for (ContestantData c : contestants) {
            if (c.getTeam() == 0) {
                team1.add(c.getState());
                continue;
            }
            if (c.getTeam() == 1) {
                team2.add(c.getState());
            }
        }

        int pos1 = 4;
        for (int i = 0; i < team1.size(); i++) {
            if (team1.get(i) == ContestantStates.STANDINPOSITION || team1.get(i) == ContestantStates.DOYOURBEST) {
                try {
                    sb.setCharAt(pos1, (char) ('0' + (i + 1)));
                    pos1 = pos1 - 2;
                } catch (Exception ignored) {
                }
            }
        }

        int pos2 = 8;
        for (int i = 0; i < team2.size(); i++) {
            if (team2.get(i) == ContestantStates.STANDINPOSITION || team2.get(i) == ContestantStates.DOYOURBEST) {
                try {
                    sb.setCharAt(pos2, (char) ('0' + (i + 1)));
                    pos2 = pos2 + 2;
                } catch (Exception ignored) {
                }
            }
        }

        return sb.toString();
    }

    /**
     * Generates and returns the game information for reporting.
     *
     * @return The game information string.
     */
    private synchronized String printGameInfo() {
        return "Game " + game + gameWinMsg + "\n";
    }

    /**
     * Translates referee states to their corresponding strings.
     *
     * @param state The state of the referee.
     * @return The string representation of the referee state.
     */
    private synchronized String translateRefereeStates(int state) {
        switch (state) {
            case 0:
                return "SOM";
            case 1:
                return "SOG";
            case 2:
                return "TRY";
            case 3:
                return "WTC";
            case 4:
                return "EOG";
            case 5:
                return "EOM";
            default:
                return "";
        }
    }

    /**
     * Translates coach states to their corresponding strings.
     *
     * @param state The state of the coach.
     * @return The string representation of the coach state.
     */
    private synchronized String translateCoachStates(int state) {
        switch (state) {
            case 0:
                return "WFRC";
            case 1:
                return "ASTM";
            case 2:
                return "WATL";
            default:
                return "";
        }
    }

    /**
     * Translates contestant states to their corresponding strings.
     *
     * @param state The state of the contestant.
     * @return The string representation of the contestant state.
     */
    private synchronized String translateContestantStates(int state) {
        switch (state) {
            case 0:
                return "SAB";
            case 1:
                return "SIP";
            case 2:
                return "DYB";
            default:
                return "";
        }
    }
}