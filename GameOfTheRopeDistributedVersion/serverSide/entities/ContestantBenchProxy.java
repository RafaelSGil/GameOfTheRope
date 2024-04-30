package serverSide.entities;

import clientSide.entities.*;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.main.SimulationParams;
import serverSide.sharedRegions.ContestantBenchInterface;
import serverSide.sharedRegions.GeneralRepositoryInterface;

public class ContestantBenchProxy extends Thread implements CoachCloning, ContestantCloning {

    /**
     *  Communication channel.
     */

    private ServerCom sconi;

    /**
     * Interface for the contestant bench
     *
     */
    private ContestantBenchInterface contestantBenchInterface;

    /**
     *  Number of instantiated threads.
     */

    private static int nProxy = 0;

    private static int getProxyId ()
    {
        Class<?> cl = null;                                            // representation of the BarberShopClientProxy object in JVM
        int proxyId;                                                   // instantiation identifier

        try
        { cl = Class.forName ("serverSide.entities.ContestantBenchProxy");
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("Data type ContestantBenchProxy was not found!");
            e.printStackTrace ();
            System.exit (1);
        }
        synchronized (cl)
        { proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    public ContestantBenchProxy(ServerCom sconi, ContestantBenchInterface contestantBenchInterface){
        super("ContestantBenchProxy_" + ContestantBenchProxy.getProxyId());
        this.sconi = sconi;
        this.contestantBenchInterface = contestantBenchInterface;
    }

    @Override
    public int getCoachState() {
        return 0;
    }

    @Override
    public void setCoachState(int coachState) {

    }

    @Override
    public int getCoachTeam() {
        return 0;
    }

    @Override
    public int getStrategy() {
        return 0;
    }

    @Override
    public void setStrategy(int strategy) {

    }

    @Override
    public int getContestantState() {
        return 0;
    }

    @Override
    public void setContestantState(int contestantState) {

    }

    @Override
    public int getContestantTeam() {
        return 0;
    }

    @Override
    public void setContestantTeam(int contestantTeam) {

    }

    @Override
    public int getContestantStrength() {
        return 0;
    }

    @Override
    public void setContestantStrength(int contestantStrength) {

    }

    @Override
    public int getContestantId() {
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void setPlaying(boolean playing) {

    }

    /**
     * Adjusts the contestant's strength based on their playing state.
     * If playing, strength decreases by 1 (up to a minimum of {@link SimulationParams#MINSTRENGTH}).
     * If not playing, strength increases by 1 (up to a maximum of {@link SimulationParams#MAXSTRENGTH}).
     */
    @Override
    public void manageStrength() {

    }

    @Override
    public void run() {
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject ();                     // get service request
        try
        { outMessage = contestantBenchInterface.processAndReply (inMessage);         // process it
        }
        catch (MessageException e)
        { GenericIO.writelnString ("Thread " + getName () + ": " + e.getMessage () + "!");
            GenericIO.writelnString (e.getMessageVal ().toString ());
            System.exit (1);
        }
        sconi.writeObject (outMessage);                                // send service reply
        sconi.close ();                                                // close the communication channel
    }
}
