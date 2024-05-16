package clientSide.main;

import clientSide.entities.Contestant;
import clientSide.stubs.ContestantsBenchStub;
import clientSide.stubs.GeneralRepositoryStub;
import clientSide.stubs.PlaygroundStub;
import clientSide.stubs.RefereeSiteStub;
import genclass.GenericIO;
import serverSide.main.SimulationParams;

/**
 * Client side of the Game of the Rope (contestants).
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class ClientGameOfTheRopeContestant {
    /**
     * Main method.
     *
     * @param args runtime arguments
     *             args[0] - name of the platform where is located the general repository server
     *             args[1] - port number for listening to service requests
     *             args[2] - name of the platform where is located the contestant bench server
     *             args[3] - port number for listening to service requests
     *             args[4] - name of the platform where is located the referee site server
     *             args[5] - port number for listening to service requests
     *             args[6] - name of the platform where is located the playground server
     *             args[7] - port number for listening to service requests
     */
    public static void main(String[] args) {
        String generalRepositoryServerHostName;                 //name of the platform where is located the general repository server
        int generalRepositoryServerPortNumber = -1;             //port number for listening to service requests
        String contestantBenchServerHostName;                   //name of the platform where is located the contestant bench server
        int contestantBenchServerPortNumber = -1;               //port number for listening to service requests
        String refereeSiteServerHostName;                       //name of the platform where is located the referee site server
        int refereeSiteServerPortNumber = -1;                   //port number for listening to service requests
        String playgroundServerHostName;                        //name of the platform where is located the playground server
        int playgroundServerPortNumber = -1;                    //port number for listening to service requests

        Contestant[] contestants = new Contestant[SimulationParams.NCONTESTANTS];   //array of contestant threads

        GeneralRepositoryStub generalRepositoryStub;            //remote reference to the general repository
        ContestantsBenchStub contestantsBenchStub;              //remote reference to the contestant bench
        PlaygroundStub playgroundStub;                          //remote reference to the playground
        RefereeSiteStub refereeSiteStub;                        //remote reference to the referee site

        /* getting problem runtime parameters */
        if (args.length != 8) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }

        generalRepositoryServerHostName = args[0];
        try {
            generalRepositoryServerPortNumber = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[1] is not a number!");
            System.exit(1);
        }
        if ((generalRepositoryServerPortNumber < 4000) || (generalRepositoryServerPortNumber >= 65536)) {
            GenericIO.writelnString("args[1] is not a valid port number!");
            System.exit(1);
        }

        contestantBenchServerHostName = args[2];
        try {
            contestantBenchServerPortNumber = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[3] is not a number!");
            System.exit(1);
        }
        if ((contestantBenchServerPortNumber < 4000) || (contestantBenchServerPortNumber >= 65536)) {
            GenericIO.writelnString("args[3] is not a valid port number!");
            System.exit(1);
        }

        refereeSiteServerHostName = args[4];
        try {
            refereeSiteServerPortNumber = Integer.parseInt(args[5]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[5] is not a number!");
            System.exit(1);
        }
        if ((refereeSiteServerPortNumber < 4000) || (refereeSiteServerPortNumber >= 65536)) {
            GenericIO.writelnString("args[5] is not a valid port number!");
            System.exit(1);
        }

        playgroundServerHostName = args[6];
        try {
            playgroundServerPortNumber = Integer.parseInt(args[7]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[7] is not a number!");
            System.exit(1);
        }
        if ((playgroundServerPortNumber < 4000) || (playgroundServerPortNumber >= 65536)) {
            GenericIO.writelnString("args[7] is not a valid port number!");
            System.exit(1);
        }

        /* initialization */
        generalRepositoryStub = new GeneralRepositoryStub(generalRepositoryServerHostName, generalRepositoryServerPortNumber);
        refereeSiteStub = new RefereeSiteStub(refereeSiteServerHostName, refereeSiteServerPortNumber);
        contestantsBenchStub = new ContestantsBenchStub(contestantBenchServerHostName, contestantBenchServerPortNumber);
        playgroundStub = new PlaygroundStub(playgroundServerHostName, playgroundServerPortNumber);

        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = new Contestant("Cont_" + (i + 1), i, (i % 2 == 0 ? 0 : 1), Contestant.GenerateRandomStrength(), contestantsBenchStub, playgroundStub, refereeSiteStub);
        }

        /* start of the simulation */
        for (int i = 0; i < SimulationParams.NCONTESTANTS; ++i) {
            GenericIO.writelnString("The contestant " + (i) + " has started");
            contestants[i].start();
        }

        /* waiting for the end of the simulation */
        GenericIO.writelnString();
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            try {
                contestants[i].join();
            } catch (InterruptedException e) {
            }
            GenericIO.writelnString("The contestant " + (i + 1) + " has terminated.");
        }
        GenericIO.writelnString();
        refereeSiteStub.shutdown();
        contestantsBenchStub.shutdown();
        playgroundStub.shutdown();
        generalRepositoryStub.shutdown();
    }
}
