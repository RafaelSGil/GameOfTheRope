package clientSide.main;

import clientSide.entities.Coach;
import clientSide.stubs.*;
import genclass.GenericIO;
import serverSide.main.SimulationParams;

public class ClientGameOfTheRopeCoach {
    public ClientGameOfTheRopeCoach(){};
    public static void main(String[] args){
        int contestantsBenchServerPortNumb = -1;
        int generalRepositoryServerPortNumb = -1;
        int playgroundServerPortNumb = -1;
        int refereeSiteServerPortNumb = -1;
        Coach[] coach = new Coach[2];
        if(args.length != 8){
            GenericIO.writelnString("Wrong Number of parameters!");
            System.exit(1);
        }

        String contestantsBenchServerHostName = args[0];
        try {
            contestantsBenchServerPortNumb = Integer.parseInt(args[1]);
        }catch (NumberFormatException var12){
            GenericIO.writelnString("args[1] is not a number!");
            System.exit(1);
        }
        if(contestantsBenchServerPortNumb < 4000 || contestantsBenchServerPortNumb > 65536){
            GenericIO.writelnString("args[1] is not a valid port number!");
            System.exit(1);
        }

        String generalRepositoryServerHostName = args[2];
        try {
            generalRepositoryServerPortNumb = Integer.parseInt(args[3]);
        } catch (NumberFormatException var12) {
            GenericIO.writelnString("args[3] is not a number!");
            System.exit(1);
        }
        if (generalRepositoryServerPortNumb < 4000 || generalRepositoryServerPortNumb > 65536) {
            GenericIO.writelnString("args[3] is not a valid port number!");
            System.exit(1);
        }

        String playgroundServerHostName = args[4];
        try {
            playgroundServerPortNumb = Integer.parseInt(args[5]);
        } catch (NumberFormatException var12) {
            GenericIO.writelnString("args[5] is not a number!");
            System.exit(1);
        }
        if (playgroundServerPortNumb < 4000 || playgroundServerPortNumb > 65536) {
            GenericIO.writelnString("args[5] is not a valid port number!");
            System.exit(1);
        }
        String refereeSiteServerHostName = args[6];
        try {
            refereeSiteServerPortNumb = Integer.parseInt(args[7]);
        } catch (NumberFormatException var12) {
            GenericIO.writelnString("args[6] is not a number!");
            System.exit(1);
        }
        if (refereeSiteServerPortNumb < 4000 || refereeSiteServerPortNumb > 65536) {
            GenericIO.writelnString("args[6] is not a valid port number!");
            System.exit(1);
        }

        ContestantsBenchStub contestantsBenchStub = new ContestantsBenchStub(contestantsBenchServerHostName, contestantsBenchServerPortNumb);
        GeneralRepositoryStub generalRepositoryStub = new GeneralRepositoryStub(generalRepositoryServerHostName, generalRepositoryServerPortNumb);
        PlaygroundStub playgroundStub = new PlaygroundStub(playgroundServerHostName, playgroundServerPortNumb);
        RefereeSiteStub refereeSite = new RefereeSiteStub(refereeSiteServerHostName,refereeSiteServerPortNumb);
        for(int i = 0; i< SimulationParams.NTEAMS;i++){

        }


    }
}
