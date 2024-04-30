package serverSide.main;

import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.entities.GeneralRepositoryProxy;
import serverSide.sharedRegions.GeneralRepository;
import serverSide.sharedRegions.GeneralRepositoryInterface;

import java.net.SocketTimeoutException;

/**
 *    Server side of the General Repository of Information.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ServerGameOfTheRopeGeneralRepository {
    /**
     *  Flag signaling the service is active.
     */
    public static boolean waitConnection;

    /**
     *  Main method.
     *
     *    @param args runtime arguments
     *        args[0] - port nunber for listening to service requests
     */
    public static void main (String [] args){
        GeneralRepository generalRepository;                           // general repository of information (service to be rendered)
        GeneralRepositoryInterface generalRepositoryInterface;         // interface to the general repository of information
        ServerCom scon, sconi;                                         // communication channels
        int portNumb = -1;                                             // port number for listening to service requests

        if (args.length != 1){
            GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }
        try{
            portNumb = Integer.parseInt (args[0]);
        }catch (NumberFormatException e){
            GenericIO.writelnString ("args[0] is not a number!");
            System.exit (1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536)){
            GenericIO.writelnString ("args[0] is not a valid port number!");
            System.exit (1);
        }

        /* service is established */
        generalRepository = new GeneralRepository();                                        // service is instantiated
        generalRepositoryInterface = new GeneralRepositoryInterface(generalRepository);     // interface to the service is instantiated
        scon = new ServerCom (portNumb);                                                    // listening channel at the public port is established
        scon.start ();
        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");

        /* service request processing */
        GeneralRepositoryProxy generalRepositoryProxy;

        waitConnection = true;
        while (waitConnection){
            try{
                sconi = scon.accept ();                                                                           // enter listening procedure
                generalRepositoryProxy = new GeneralRepositoryProxy (sconi, generalRepositoryInterface);          // start a service provider agent to address
                generalRepositoryProxy.start ();                                                                  //   the request of service
            }catch (SocketTimeoutException e) {}
        }
        scon.end ();                                                   // operations termination
        GenericIO.writelnString ("Server was shutdown.");
    }
}
