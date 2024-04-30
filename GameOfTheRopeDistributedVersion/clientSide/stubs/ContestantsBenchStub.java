package clientSide.stubs;

import clientSide.entities.Coach;
import clientSide.entities.Contestant;
import clientSide.entities.Referee;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import genclass.GenericIO;

public class ContestantsBenchStub {
    /**
     *  Name of the platform where is located the barber shop server.
     */

    private final String serverHostName;

    /**
     *  Port number for listening to service requests.
     */

    private final int serverPortNumb;

    /**
     *   Instantiation of a stub to the contestant bench.
     *
     *     @param serverHostName name of the platform where is located the barber shop server
     *     @param serverPortNumb port number for listening to service requests
     */
    public ContestantsBenchStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation unblock contestant bench
     *
     * It is called when the referee gives the signal to unblock the coaches and contestants
     */

    public void unblockContestantBench(){
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
        outMessage = new Message(MessageType.UCB);
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.UCBDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();
    }

    /**
     * operation referee call trial
     *
     * It is called when referee signals the start of new trial iteration
     */
    public void refereeCallTrial(){
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
        outMessage = new Message(MessageType.SETCT);
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.CTDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();
    }

    /**
     * operation set has trial ended
     *
     * It is called when referee signals the end of a trial
     *
     * @param hasTrialEnded new value for the attribute
     */
    public void setHasTrialEnded(boolean hasTrialEnded){
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
        outMessage = new Message(MessageType.SETATD, hasTrialEnded);
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.ATDDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();
    }

    /**
     * Operation call contestants
     *
     * It is called when coaches get the referee signal to pick the team
     *
     * @param team to which team does the coach belong
     */
    public void callContestants(int team){
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
        outMessage = new Message(MessageType.SETCC, team, ((Coach) Thread.currentThread()).getCoachState());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.CCDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();
    }

    /**
     * Operation follow coach advice
     *
     * It is called when contestants get the order from the coach
     */
    public void followCoachAdvice(){
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
        outMessage = new Message(MessageType.SETFCA, ((Contestant) Thread.currentThread()).getContestantId(), ((Contestant) Thread.currentThread()).getContestantStrength(), ((Contestant) Thread.currentThread()).getContestantState(),
                ((Contestant) Thread.currentThread()).getContestantTeam());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.FCADONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();
    }

    /**
     * Operation seat down
     */
    public void seatDown(){
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
        outMessage = new Message(MessageType.SETSD, ((Contestant) Thread.currentThread()).getContestantId(), ((Contestant) Thread.currentThread()).getContestantStrength(), ((Contestant) Thread.currentThread()).getContestantState(),
                ((Contestant) Thread.currentThread()).getContestantTeam());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.SDDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();
    }

    /**
     * operation review notes
     */
    public void reviewNotes(){
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
        outMessage = new Message(MessageType.SETRN, ((Coach) Thread.currentThread()).getCoachTeam(), ((Coach) Thread.currentThread()).getCoachState());
        com.writeObject(outMessage);

        // receive response
        inMessage = (Message) com.readObject();

        // process response
        if((inMessage.getMsgType() != MessageType.RNDONE)){
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        // close communication channel
        com.close ();
    }

    /**
     * Operation end of work.
     * New operation.
     * @param entity entity identification
     * @param id id of the entity
     */
    public void endOperation (String entity, int id)
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
        outMessage = new Message (MessageType.END, entity, id);
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
