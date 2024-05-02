package clientSide.stubs;


import clientSide.entities.Referee;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import genclass.GenericIO;

/**
 *  Stub to the referee site.
 *
 *    It instantiates a remote reference to the referee site.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class RefereeSiteStub {
    /**
     *  Name of the platform where is located the barber shop server.
     */

    private final String serverHostName;

    /**
     *  Port number for listening to service requests.
     */

    private final int serverPortNumb;

    /**
     *   Instantiation of a stub to the referee site.
     *
     *     @param serverHostName name of the platform where is located the barber shop server
     *     @param serverPortNumb port number for listening to service requests
     */

    public RefereeSiteStub (String serverHostName, int serverPortNumb)
    {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation announce new game
     *
     * It is called when the referee signals that a new game will begin
     */
    public void announceNewGame(){
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
        outMessage = new Message(MessageType.SETANG, 0, ((Referee) Thread.currentThread()).getRefereeSate());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.ANGDONE)){
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
     * Operation declare game winner
     *
     * It is called when the referee signals the winner of the current winner
     */
    public void declareGameWinner(){
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
        outMessage = new Message(MessageType.SETDGW, 0, ((Referee) Thread.currentThread()).getRefereeSate());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.DGWDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if((inMessage.getRefereeState() != ((Referee) Thread.currentThread()).getRefereeSate())){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid customer state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();

        ((Referee) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
    }

    /**
     * Operation announce new game
     *
     * It is called when the referee signals that a new game will begin
     */
    public void declareMatchWinner(){
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
        outMessage = new Message(MessageType.SETDMW, 0, ((Referee) Thread.currentThread()).getRefereeSate());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.DMWDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if((inMessage.getRefereeState() != ((Referee)  Thread.currentThread()).getRefereeSate())){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid customer state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();

        ((Referee) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
    }

    /**
     * Operation announce new game
     *
     * It is called when the referee signals that a new game will begin
     */
    public void setMatchEnd(){
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
        outMessage = new Message(MessageType.SETEOFC, 0, ((Referee) Thread.currentThread()).getRefereeSate());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.EOFCDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if((inMessage.getRefereeState() != ((Referee)  Thread.currentThread()).getRefereeSate())){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid customer state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();

        ((Referee) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
    }

    /**
     * operation end of match
     *
     * It is called when contestants and coaches want to know if the match is over
     */
    public boolean endOfMatch(){
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
        outMessage = new Message(MessageType.SETEOFC);
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.EOFCDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();

        return inMessage.getEndOp();
    }

    /**
     *  Operation end of work.
     *
     *   New operation.
     */
    public void endOperation ()
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.END);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.ENDREPLY)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     */

    public void shutdown ()
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SHUT);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SHUTDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

}
