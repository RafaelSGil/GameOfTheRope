package sharedregions;
import entities.Contestant;
import entities.ContestantStates;
import entities.RefereeStates;
import entities.data.*;
import genclass.GenericIO;
import genclass.TextFile;
import main.SimulationParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GeneralRepository {
    private RefereeData referee;
    private CoachData[] coaches;
    private ContestantData[] contestants;
    private int game;

    private String gameWinMsg;

    private int trial;

    private int ropePosition;
    private String fileName;

    //private int[][] gameRecord;


    public GeneralRepository(String fileName){
        if ((fileName == null) || Objects.equals (fileName, ""))
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

        //this.gameRecord = new int[SimulationParams.GAMES][SimulationParams.NTRIALS];

        reportInitialStatus();
    }

    public synchronized void setGame(int game) {
        this.game = game;
        reportGameStatus();
    }

    public synchronized int getGame() {
        return game;
    }

    public synchronized String getGameWinMsg() {
        return gameWinMsg;
    }

    public synchronized void setGameWinMsg(String gameWinMsg) {
        this.gameWinMsg = gameWinMsg;
    }

    public synchronized int getTrial() {
        return trial;
    }

    public synchronized void setTrial(int trial) {
        this.trial = Math.min(trial, SimulationParams.NTRIALS);
    }

    public synchronized int getRopePosition() {
        return ropePosition;
    }

    public synchronized void setRopePosition(int ropePosition) {
        this.ropePosition = ropePosition;
    }

    public synchronized void updateReferee(int refereeState){
        referee.setState(refereeState);
        reportStatus();
    }

    public synchronized void updateContestant(int contestantId, int contestantStrength, int contestantState, int contestantTeam){
        try{
            contestants[contestantId].setState(contestantState);
            contestants[contestantId].setStrength(contestantStrength);
            contestants[contestantId].setTeam(contestantTeam);
        }catch (ArrayIndexOutOfBoundsException e){
            GenericIO.writelnString("Error while updating contestant " + contestantId);
            System.exit(1);
        }

        reportStatus();
    }

    public synchronized void updateCoach( int coachState, int coachTeam){
        try{
            coaches[coachTeam].setState(coachState);
        }catch (ArrayIndexOutOfBoundsException e){
            GenericIO.writelnString("Error while updating coach " + coachTeam);
            System.exit(1);
        }

        reportStatus();
    }

    public synchronized void declareGameWinner(int team, String cause){
        /*int winnerTeam;
        int team1TrialsWon = 0;

        for (int i = 0; i < SimulationParams.NTRIALS; i++) {
            if (gameRecord[game-1][i] == 0){
                team1TrialsWon++;
            }
        }

        if(team1TrialsWon > (SimulationParams.NTRIALS/2)){
            winnerTeam = 1;
            gameWinMsg = " was won by team " + winnerTeam + " by knock out in " + team1TrialsWon + " trials.";
        }
        if (team1TrialsWon < (SimulationParams.NTRIALS/2)){
            winnerTeam = 2;
            gameWinMsg = " was won by team " + winnerTeam + " by knock out in " + (SimulationParams.NTRIALS - team1TrialsWon) + " trials.";
        }
        if (team1TrialsWon == (SimulationParams.NTRIALS/2)){
            gameWinMsg = " was a draw.";
        }*/
        if(!cause.equals("draw")){
            gameWinMsg = " was won by team " + (team + 1) + " by " + cause + " in " + trial + " trials.";
        }
        else{
            gameWinMsg = " was a draw.";
        }
        reportGameStatus();
        gameWinMsg = "";
    }

    public synchronized String ropePositionToString() {
        StringBuilder sb = new StringBuilder("-------"); // Use StringBuilder for efficiency
        if (this.ropePosition > 0) {
            if (ropePosition > 3) {
                sb.replace(6, 7, "."); // Replace the character at index 4 with "."
            } else {
                sb.setCharAt(ropePosition + 3, '.'); // Set the character at adjusted position to '.'
            }
        } else if (this.ropePosition < 0) {
            if (ropePosition < -3) {
                sb.replace(0, 1, "."); // Replace the character at index 0 with "."
            } else {
                sb.setCharAt(ropePosition + 3, '.'); // Set the character at adjusted position to '-'
            }
        }
        else {
            sb.setCharAt(3, '.');
        }
        return sb.append("\t").toString();
    }

    /**
     * Set the winner of the trial
     * 0-team 1 1 - team 2 2 - draw
     * @param
     */
//    public void setTrialWinner(int winner){
//        gameRecord[game-1][trial-1] = winner;
//    }
    public synchronized void declareMatchWinner(String msg){
        TextFile log = new TextFile();

        if (!log.openForAppending(".", fileName)){
            GenericIO.writelnString("Failed creating " + fileName + " file.");
            System.exit(1);
        }

        log.writelnString(msg);

        if (!log.close ())
        { GenericIO.writelnString ("The operation of closing the file " + fileName + " failed!");
            System.exit (1);
        }
    }

    private synchronized void reportInitialStatus(){
        TextFile log = new TextFile();

        if (!log.openForWriting(".", fileName)){
            GenericIO.writelnString("Failed creating " + fileName + " file.");
            System.exit(1);
        }

        log.writelnString("\t\t\t\t\t\tGame of the Rope - Description of the internal state");
        log.writelnString(printHeader());
        log.writelnString(printValues());
        log.writelnString();

        if (!log.close ())
        { GenericIO.writelnString ("The operation of closing the file " + fileName + " failed!");
            System.exit (1);
        }
    }

    public synchronized void reportStatus(){
        TextFile log = new TextFile();

        if (!log.openForAppending(".", fileName)){
            GenericIO.writelnString("Failed creating " + fileName + " file.");
            System.exit(1);
        }

        log.writelnString(printHeader());
        log.writelnString(printValues());
        log.writelnString();

        if (!log.close ())
        { GenericIO.writelnString ("The operation of closing the file " + fileName + " failed!");
            System.exit (1);
        }
    }

    public synchronized void reportGameStatus(){
        TextFile log = new TextFile();

        if (!log.openForAppending(".", fileName)){
            GenericIO.writelnString("Failed creating " + fileName + " file.");
            System.exit(1);
        }

        log.writelnString(printGameInfo());

        if (!log.close ())
        { GenericIO.writelnString ("The operation of closing the file " + fileName + " failed!");
            System.exit (1);
        }
    }

    private synchronized String printHeader(){
        StringBuilder sb = new StringBuilder();

        sb.append("Ref\t");

        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            sb.append("Coa ").append(i + 1).append("\t");
            for (int j = 0; j < SimulationParams.NPLAYERS; j++) {
                sb.append("Cont ").append(j+1).append("\t");
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



    private synchronized String printValues(){
        StringBuilder sb = new StringBuilder();

        sb.append(translateRefereeStates(referee.getState())).append("\t");

        int aux = 0;

        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            sb.append(translateCoachStates(coaches[i].getState())).append("\t");
            for (int j = 0; j < SimulationParams.NCONTESTANTS; j++) {
                if (contestants[j].getTeam() == -1){
                    sb.append("## ##\t");
                    if(j == SimulationParams.NPLAYERS - 1) break;
                    continue;
                }

                if(contestants[j].getTeam() == coaches[i].getTeam()){
                    sb.append(translateContestantStates(contestants[j].getState())).append("\t")
                            .append(contestants[j].getStrength()).append("\t");
                }
            }
        }

        sb.append(getPos());

        sb.append("\t").append(trial).append("\t").append(ropePosition);

        return sb.toString();
    }

    private synchronized String getPos(){
        ArrayList<Integer> team1 = new ArrayList<>();
        ArrayList<Integer> team2 = new ArrayList<>();

        StringBuilder sb = new StringBuilder("- - - . - - -");

        for (ContestantData c : contestants){
            if(c.getTeam() == 0){
                team1.add(c.getState());
                continue;
            }
            if(c.getTeam() == 1){
                team2.add(c.getState());
            }
        }

        int pos1 = 4;
        for (int i = 0; i < team1.size(); i++){
            if (team1.get(i) == ContestantStates.STANDINPOSITION || team1.get(i) == ContestantStates.DOYOURBEST){
                try {
                    sb.setCharAt(pos1, (char) ('0' + (i + 1)));
                    pos1 = pos1-2;
                } catch (Exception ignored) {
                }
            }
        }

        int pos2 = 8;
        for (int i = 0; i < team2.size(); i++){
            if (team2.get(i) == ContestantStates.STANDINPOSITION || team2.get(i) == ContestantStates.DOYOURBEST){
                try {
                    sb.setCharAt(pos2, (char) ('0' + (i + 1)));
                    pos2 = pos2 + 2;
                } catch (Exception ignored) {
                }
            }
        }

        return sb.toString();
    }

    private synchronized String printGameInfo(){
        return "Game " + game + gameWinMsg + "\n";
    }

    private synchronized String translateRefereeStates(int state){
        switch (state){
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

    private synchronized String translateCoachStates(int state){
        switch (state){
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

    private synchronized String translateContestantStates(int state){
        switch (state){
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