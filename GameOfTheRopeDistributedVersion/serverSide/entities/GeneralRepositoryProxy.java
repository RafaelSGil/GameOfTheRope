package serverSide.entities;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.sharedRegions.GeneralRepositoryInterface;

/**
 * Service provider agent for access to the General Repository of Information.
 * <p>
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class GeneralRepositoryProxy extends Thread {
    /**
     * Communication channel.
     */

    private final ServerCom sconi;

    /**
     * Interface for the referee site
     */
    private final GeneralRepositoryInterface generalRepositoryInterface;

    /**
     * Number of instantiated threads.
     */

    private static int nProxy = 0;

    private static int getProxyId() {
        Class<?> cl = null;                                            // representation of the BarberShopClientProxy object in JVM
        int proxyId;                                                   // instantiation identifier

        try {
            cl = Class.forName("serverSide.entities.GeneralRepositoryProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type GeneralRepositoryProxy was not found!");
            e.printStackTrace();
            System.exit(1);
        }
        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    public GeneralRepositoryProxy(ServerCom sconi, GeneralRepositoryInterface generalRepositoryInterface) {
        super("GeneralRepositoryProxy_" + GeneralRepositoryProxy.getProxyId());
        this.sconi = sconi;
        this.generalRepositoryInterface = generalRepositoryInterface;
    }

    /**
     * Life cycle of the service provider agent.
     */

    @Override
    public void run() {
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject();                     // get service request
        try {
            outMessage = generalRepositoryInterface.processAndReply(inMessage);         // process it
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);                                // send service reply
        sconi.close();                                                // close the communication channel
    }
}
