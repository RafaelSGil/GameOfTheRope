package clientSide.stubs;

import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import genclass.GenericIO;
import genclass.TextFile;
import serverSide.main.SimulationParams;

/**
 *  Stub to the general repository.
 *
 *    It instantiates a remote reference to the general repository.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class GeneralRepositoryStub {
    /**
     *  Name of the platform where is located the general repository server.
     */

    private String serverHostName;

    /**
     *  Port number for listening to service requests.
     */

    private int serverPortNumb;

    /**
     *   Instantiation of a stub to the general repository.
     *
     *     @param serverHostName name of the platform where is located the barber shop server
     *     @param serverPortNumb port number for listening to service requests
     */
    public GeneralRepositoryStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     *   Operation initialization of the simulation.
     *
     *     @param fileName logging file name
     */
    public void initSimul (String fileName)
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
        outMessage = new Message (MessageType.SETNFIC, fileName);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.NFICDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Set the current game
     */
    public void setGame(int game){
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
        outMessage = new Message (MessageType.SETG, game);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.GDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Sets the trial count for the simulation
     *
     * @param trial The trial count to set.
     */
    public synchronized void setTrial(int trial) {
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
        outMessage = new Message (MessageType.SETT, trial);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SETTDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Sets the position of the rope.
     *
     * @param ropePosition The position to set for the rope.
     */
    public synchronized void setRopePosition(int ropePosition) {
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
        outMessage = new Message (MessageType.SETRP, ropePosition);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.RPDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Updates the state of the referee and reports the status.
     *
     * @param refereeState The new state of the referee.
     */
    public synchronized void updateReferee(int refereeState) {
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
        outMessage = new Message (MessageType.UPREF, refereeState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.UPREFDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Updates the state, strength, and team of a contestant identified by their ID, and reports the status.
     *
     * @param contestantId       The ID of the contestant to update.
     * @param contestantStrength The strength of the contestant.
     * @param contestantState    The state of the contestant.
     * @param contestantTeam     The team of the contestant.
     */
    public synchronized void updateContestant(int contestantId, int contestantStrength, int contestantState, int contestantTeam) {
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
        outMessage = new Message (MessageType.UPCONT, contestantId, contestantStrength, contestantState, contestantTeam);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.UPCONTDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Updates the state of a coach and reports the status.
     *
     * @param coachState The new state of the coach.
     * @param coachTeam  The team of the coach.
     */
    public synchronized void updateCoach(int coachState, int coachTeam) {
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
        outMessage = new Message (MessageType.UPCOA, coachTeam, coachState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.UPCOADONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Declares the winner of the game.
     *
     * @param team  The winning team.
     * @param cause The cause of the win.
     */
    public synchronized void declareGameWinner(int team, String cause) {
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
        outMessage = new Message (MessageType.SETGW, team, cause);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.GWDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Declares the winner of the match.
     *
     * @param msg The message declaring the match winner.
     */
    public synchronized void declareMatchWinner(String msg) {
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
        outMessage = new Message (MessageType.SETMW, msg);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.MWDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Reports the current status of the game.
     */
    public synchronized void reportStatus(boolean header) {
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
        outMessage = new Message (MessageType.RS, header);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.RSDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Reports the status of the game after its completion.
     */
    public synchronized void reportGameStatus() {
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
        outMessage = new Message (MessageType.RGS);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.RGSDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }
    /**
     * Reports the status of the game start.
     */
    public synchronized void reportGameStart() {
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
        outMessage = new Message (MessageType.RGSTR);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.RGSTRDONE)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     *  Operation end of work.
     *
     *   New operation.
     *
     *      @param id id
     */

    public void endOperation (int id)
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
        outMessage = new Message (MessageType.SETEOFC, id);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.EOFCDONE)
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
