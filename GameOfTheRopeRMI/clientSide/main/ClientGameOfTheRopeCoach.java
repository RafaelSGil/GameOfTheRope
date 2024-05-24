package clientSide.main;

import clientSide.entities.Coach;
import genclass.GenericIO;
import interfaces.IContestantsBench;
import interfaces.IGeneralRepository;
import interfaces.IPlayground;
import interfaces.IRefereeSite;
import serverSide.main.SimulationParams;
import serverSide.utils.Strategy;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Client side of the Game of the Rope (coaches).
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class ClientGameOfTheRopeCoach {
    /**
     * Main method.
     *
     * @param args runtime arguments
     *    args[0] - name of the platform where is located the RMI registering service
     *    args[1] - port number where the registering service is listening to service requests
     */
    public static void main(String[] args) {
        String rmiRegHostName;                                         // name of the platform where is located the RMI registering service
        int rmiRegPortNumb = -1;                                       // port number where the registering service is listening to service requests

        /* getting problem runtime parameters */

        if (args.length != 2)
        { GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }
        rmiRegHostName = args[0];
        try
        { rmiRegPortNumb = Integer.parseInt (args[1]);
        }
        catch (NumberFormatException e)
        { GenericIO.writelnString ("args[1] is not a number!");
            System.exit (1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536))
        { GenericIO.writelnString ("args[1] is not a valid port number!");
            System.exit (1);
        }

        /* initialization */
        String nameEntryGeneralRepository = "GeneralRepository";                    // public name of the general repository object
        IGeneralRepository reposStub = null;                                        // remote reference to the general repository object
        String nameEntryRefereeSite = "RefereeSite";                                // public name of the referee site object
        IRefereeSite refereeSiteStub = null;                                        // remote reference to the referee site object
        String nameEntryContestantBench = "ContestantBench";                        // public name of the contestant bench object
        IContestantsBench contestantsBenchStub = null;                              // remote reference to the contetant bench object
        String nameEntryPlayground = "Playground";                                  // public name of the playground object
        IPlayground playgroundStub = null;                                          // remote reference to the playground object

        Registry registry = null;                                                   // remote reference for registration in the RMI registry service
        Coach[] coaches = new Coach[SimulationParams.NTEAMS];                       //array of coach threads

        /* initialization */

        try
        { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { reposStub = (IGeneralRepository) registry.lookup (nameEntryGeneralRepository);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("GeneralRepos lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("GeneralRepos not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { refereeSiteStub = (IRefereeSite) registry.lookup (nameEntryRefereeSite);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RefereeSite lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("RefereeSite not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { contestantsBenchStub = (IContestantsBench) registry.lookup (nameEntryContestantBench);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("ContestantsBench lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("ContestantsBench not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try
        { playgroundStub = (IPlayground) registry.lookup (nameEntryPlayground);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("Playground lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { GenericIO.writelnString ("Playground not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            coaches[i] = new Coach("Coa" + (i + 1), (i % 2 == 0 ? 0 : 1), (i % 2 == 0 ? Strategy.STRENGTH : Strategy.MODERATE), contestantsBenchStub, playgroundStub, refereeSiteStub);
        }

        /* start of the simulation */
        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            GenericIO.writelnString("The coach " + (i + 1) + " has started");
            coaches[i].start();
        }

        /* waiting for the end of the simulation */
        GenericIO.writelnString();
        try {
            refereeSiteStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString ("RefereeSite shutdown: " + e.getMessage ());
            e.printStackTrace();
            System.exit(1);
        }
        try {
            contestantsBenchStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString ("Contestantsbench shutdown: " + e.getMessage ());
            e.printStackTrace();
            System.exit(1);
        }
        try {
            playgroundStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString ("Playground shutdown: " + e.getMessage ());
            e.printStackTrace();
            System.exit(1);
        }
        try {
            reposStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString ("General Repository shutdown: " + e.getMessage ());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
