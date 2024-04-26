package clientSide.stubs;

import clientSide.entities.Coach;
import clientSide.entities.Contestant;
import clientSide.entities.Referee;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import genclass.GenericIO;

/**
 *  Stub to the playground
 *
 *    It instantiates a remote reference to the playground site.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class PlaygroundStub {
    /**
     *  Name of the platform where is located the barber shop server.
     */

    private final String serverHostName;

    /**
     *  Port number for listening to service requests.
     */

    private final int serverPortNumb;

    /**
     *   Instantiation of a stub to the playground.
     *
     *     @param serverHostName name of the platform where is located the barber shop server
     *     @param serverPortNumb port number for listening to service requests
     */
    public PlaygroundStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation call trial
     *
     * It is called when the referee signals a trial will start
     */
    public void callTrial(){
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        {
            try{
                Thread.currentThread ().sleep ((long) (10));
            }catch (InterruptedException ignored) {}
        }

        // send message
        outMessage = new Message(MessageType.SETCT, 0, ((Referee) Thread.currentThread()).getRefereeSate());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.CTDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if((inMessage.getRefereeState() != ((Referee) Thread.currentThread()).getRefereeSate())){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid referee state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();

        ((Referee) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
    }

    /**
     * Operation start trial
     *
     * It is called when the referee signals the start of a trial
     */
    public void startTrial(){
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        {
            try{
                Thread.currentThread ().sleep ((long) (10));
            }catch (InterruptedException ignored) {}
        }

        // send message
        outMessage = new Message(MessageType.SETST, 0, ((Referee) Thread.currentThread()).getRefereeSate());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.STDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if((inMessage.getRefereeState() != ((Referee) Thread.currentThread()).getRefereeSate())){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid referee state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();

        ((Referee) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
    }

    /**
     * Operation assert trial decision
     *
     * It is called when the referee asserts the result of the trial
     */
    public boolean assertTrialDecision(){
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        {
            try{
                Thread.currentThread ().sleep ((long) (10));
            }catch (InterruptedException ignored) {}
        }

        // send message
        outMessage = new Message(MessageType.SETATD, 0, ((Referee) Thread.currentThread()).getRefereeSate());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.ATDDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if((inMessage.getRefereeState() != ((Referee) Thread.currentThread()).getRefereeSate())){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid referee state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();

        ((Referee) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());

        return inMessage.getEndOp();
    }

    /**
     * Operation inform referee
     *
     * It is called when a coach informs the referee his team is ready
     */
    public void informReferee(){
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        {
            try{
                Thread.currentThread ().sleep ((long) (10));
            }catch (InterruptedException ignored) {}
        }

        // send message
        outMessage = new Message(MessageType.SETIR, ((Coach) Thread.currentThread()).getCoachTeam(), ((Coach) Thread.currentThread()).getCoachState());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.IRDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if((inMessage.getCoachState() != ((Coach) Thread.currentThread()).getCoachState())){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid coach state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();

        ((Coach) Thread.currentThread()).setCoachState(inMessage.getCoachState());
    }

    /**
     * Operation get ready
     *
     * It is called when a contestant takes place and informs its coach
     */
    public void getReady(){
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        {
            try{
                Thread.currentThread ().sleep ((long) (10));
            }catch (InterruptedException ignored) {}
        }

        // send message
        outMessage = new Message(MessageType.SETGR, ((Contestant) Thread.currentThread()).getContestantId(), ((Contestant) Thread.currentThread()).getContestantState());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.GRDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if((inMessage.getContestantState() != ((Contestant) Thread.currentThread()).getContestantState())){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid contestant state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();

        ((Contestant) Thread.currentThread()).setContestantState(inMessage.getContestantState());
    }

    /**
     * Operation am done
     *
     * It is called when a contestant informs the referee it is done pulling the rope
     */
    public void amIDone(){
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        {
            try{
                Thread.currentThread ().sleep ((long) (10));
            }catch (InterruptedException ignored) {}
        }

        // send message
        outMessage = new Message(MessageType.SETAID, ((Contestant) Thread.currentThread()).getContestantId(), ((Contestant) Thread.currentThread()).getContestantState());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.AIDDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if((inMessage.getContestantState() != ((Contestant) Thread.currentThread()).getContestantState())){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid contestant state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();

        ((Contestant) Thread.currentThread()).setContestantState(inMessage.getContestantState());
    }
}
