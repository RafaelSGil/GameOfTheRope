package sharedregions;
import entities.data.*;
import genclass.GenericIO;
import genclass.TextFile;
import main.SimulationParams;

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

    public GeneralRepository(String fileName){
        if ((fileName == null) || Objects.equals (fileName, ""))
            this.fileName = "logger";
        else this.fileName = fileName;

        this.referee = new RefereeData();
        coaches = new CoachData[SimulationParams.NTEAMS];
        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            coaches[i] = new CoachData();
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

    public void setGame(int game) {
        this.game = game;
    }

    public int getGame() {
        return game;
    }

    public String getGameWinMsg() {
        return gameWinMsg;
    }

    public void setGameWinMsg(String gameWinMsg) {
        this.gameWinMsg = gameWinMsg;
    }

    public int getTrial() {
        return trial;
    }

    public void setTrial(int trial) {
        this.trial = trial;
    }

    public int getRopePosition() {
        return ropePosition;
    }

    public void setRopePosition(int ropePosition) {
        this.ropePosition = ropePosition;
    }

    public synchronized void updateReferee(int refereeState){
        referee.setState(refereeState);
        //reportStatus();
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

    public void updateCoach( int coachState, int coachTeam){
        try{
            coaches[coachTeam].setState(coachState);
        }catch (ArrayIndexOutOfBoundsException e){
            GenericIO.writelnString("Error while updating coach " + coachTeam);
            System.exit(1);
        }
    }

    private void reportInitialStatus(){
        TextFile log = new TextFile();

        if (!log.openForWriting(".", fileName)){
            GenericIO.writelnString("Failed creating " + fileName + " file.");
            System.exit(1);
        }

        log.writelnString("\t\t\t\t\t\tGame of the Rope - Description of the internal state");
        log.writelnString(printHeader());
        log.writelnString(printValues());
        log.writelnString(printGameInfo());

        if (!log.close ())
        { GenericIO.writelnString ("The operation of closing the file " + fileName + " failed!");
            System.exit (1);
        }
    }

    private void reportStatus(){
        GenericIO.writelnString("Will write on file");
        TextFile log = new TextFile();

        if (!log.openForWriting(".", fileName)){
            GenericIO.writelnString("Failed creating " + fileName + " file.");
            System.exit(1);
        }
        log.writelnString(printHeader());
        log.writelnString(printValues());
        log.writelnString(printGameInfo());
        if (!log.close ())
        { GenericIO.writelnString ("The operation of closing the file " + fileName + " failed!");
            System.exit (1);
        }
    }

    private String printHeader(){
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

    private String printValues(){
        StringBuilder sb = new StringBuilder();

        sb.append(referee.getState()).append("\t");

        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            sb.append(coaches[i].getState()).append("\t\t");
            for (int j = 0; j < SimulationParams.NPLAYERS; j++) {
                if (contestants[j].getTeam() == 0){
                    sb.append("## ##\t");
                    continue;
                }

                if(contestants[j].getTeam() == coaches[i].getTeam()){
                    sb.append(contestants[j].getState()).append(" ")
                            .append(contestants[j].getStrength()).append("\t");
                }
            }
        }

        for (int i = SimulationParams.NPLAYERSINCOMPETITION; i > 0; i--) {
            sb.append("-").append(" ");
        }

        sb.append(". ");

        for (int i = 1; i <= SimulationParams.NPLAYERSINCOMPETITION; i++) {
            sb.append("-").append(" ");
        }

        sb.append("\t").append(trial).append("\t").append(ropePosition);

        return sb.toString();
    }

    private String printGameInfo(){
        return "Game " + game + gameWinMsg;
    }
}