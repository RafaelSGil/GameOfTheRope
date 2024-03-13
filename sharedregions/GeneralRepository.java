package sharedregions;

import entities.CoachStates;
import entities.ContestantStates;
import entities.Referee;
import genclass.GenericIO;
import genclass.TextFile;
import main.SimulationParams;

import java.util.Objects;

public class GeneralRepository {
    private Referee referee;
    private int[] coachesState;
    private int[] contestantsState;
    private int Game;
    private int Trial;
    private int ropePosition;
    private String fileName;

    public GeneralRepository(String fileName){
        if ((fileName == null) || Objects.equals (fileName, ""))
            this.fileName = "logger";
        else this.fileName = fileName;

        coachesState = new int [SimulationParams.NTEAMS];
        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            coachesState[i] = CoachStates.WATFORREFEREECOMMAND;
        }

        contestantsState = new int [SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestantsState[i] = ContestantStates.SEATATBENCH;
        }

        reportInitialStatus();
    }

    private void reportInitialStatus(){
        TextFile log = new TextFile();

        if (!log.openForWriting(".", fileName)){
            GenericIO.writelnString("Failed creating " + fileName + " file.");
            System.exit(1);
        }

        log.writelnString("\t\t\t\t\t\tGame of the Rope - Description of the internal state");
        log.writelnString(printHeader());

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
}
