package commInfra;

import java.io.*;
import genclass.GenericIO;

/**
 *   Internal structure of the exchanged messages.
 *
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */

public class Message implements Serializable
{
    /**
     *  Serialization key.
     */

    private static final long serialVersionUID = 2021L;

    /**
     *  Message type.
     */

    private int msgType = -1;

    /**
     * Referee state
     */
    private int refereeState = -1;

    /**
     *  Coach identification.
     */

    private int coachId = -1;

    /**
     *  Coach state.
     */

    private int coachState = -1;

    /**
     *  Contestant identification.
     */

    private int contestantId = -1;

    /**
     *  Contestant state.
     */

    private int contestantState = -1;

    /**
     *  End of operations (referee).
     */

    private boolean endOp = false;

    /**
     *  Name of the logging file.
     */

    private String fName = null;

    /**
     *  Message instantiation (form 1).
     *
     *     @param type message type
     */

    public Message (int type)
    {
        msgType = type;
    }

    /**
     *  Message instantiation (form 2).
     *
     *     @param type message type
     *     @param id coach / contestant identification
     *     @param state coach / contestant state
     */

    public Message (int type, int id, int state)
    {
        msgType = type;
        if ((msgType == MessageType.SETCC) || (msgType == MessageType.SETIR) || (msgType == MessageType.SETRN)){
            coachId= id;
            coachState = state;
        }
        else if ((msgType == MessageType.SETFCA) || (msgType == MessageType.FCADONE) || (msgType == MessageType.SETGR) ||
                (msgType == MessageType.SETAID) || (msgType == MessageType.SETSD)){
            contestantId= id;
            contestantState = state;
        }
        else if((msgType == MessageType.SETANG) || (msgType == MessageType.SETCT) || (msgType == MessageType.SETST)
                || (msgType == MessageType.SETATD)){
            refereeState = state;
        }
        else { GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }

    /**
     *  Message instantiation (form 3).
     *
     *     @param type message type
     *     @param id coach identification
     */

    public Message (int type, int id)
    {
        msgType = type;
        coachId= id;
    }

    /**
     *  Message instantiation (form 4).
     *
     *     @param type message type
     *     @param endOp end of operations flag
     */

    public Message (int type, boolean endOp)
    {
        msgType = type;
        this.endOp = endOp;
    }

    /**
     *  Message instantiation (form 5).
     *
     *     @param type message type
     *     @param coachId coach identification
     *     @param coachState barber state
     *     @param contestantId customer identification
     *     @param refereeState referee state
     */

    public Message (int type, int coachId, int coachState, int contestantId, int refereeState)
    {
        msgType = type;
        this.coachId= coachId;
        this.coachState = coachState;
        this.contestantId= contestantId;
        this.refereeState = refereeState;
    }

    /**
     *  Message instantiation (form 6).
     *
     *     @param type message type
     *     @param coachId coach identification
     *     @param coachState coach state
     *     @param contestantId customer identification
     *     @param contestantState customer state
     *     @param refereeState referee state
     */

    public Message (int type, int coachId, int coachState, int contestantId, int contestantState, int refereeState)
    {
        msgType = type;
        this.coachId= coachId;
        this.coachState = coachState;
        this.contestantId= contestantId;
        this.contestantState = contestantState;
        this.refereeState = refereeState;
    }

    /**
     *  Message instantiation (form 7).
     *
     *     @param type message type
     *     @param name name of the logging file
     */

    public Message (int type, String name)
    {
        msgType = type;
        fName= name;
    }

    /**
     *  Getting message type.
     *
     *     @return message type
     */
    public int getMsgType ()
    {
        return (msgType);
    }

    /**
     *  Getting coach identification.
     *
     *     @return coach identification
     */

    public int getCoachId ()
    {
        return (coachId);
    }

    /**
     *  Getting coach state.
     *
     *     @return coach state
     */

    public int getCoachState ()
    {
        return (coachState);
    }

    /**
     *  Getting contestant identification.
     *
     *     @return contestant identification
     */

    public int getContestantId ()
    {
        return (contestantId);
    }

    /**
     *  Getting contestant state.
     *
     *     @return contestant state
     */

    public int getContestantState ()
    {
        return (contestantState);
    }

    /**
     *  Getting end of operations flag (referee).
     *
     *     @return end of operations flag
     */
    public boolean getEndOp ()
    {
        return (endOp);
    }

    /**
     * Getting referee state
     *
     * @return referee state
     */
    public int getRefereeState(){
        return refereeState;
    }

    /**
     *  Getting name of logging file.
     *
     *     @return name of the logging file
     */

    public String getLogFName ()
    {
        return (fName);
    }


    /**
     *  Printing the values of the internal fields.
     *
     *  It is used for debugging purposes.
     *
     *     @return string containing, in separate lines, the pair field name - field value
     */

    @Override
    public String toString ()
    {
        return ("Message type = " + msgType +
                "\nReferee State = " + refereeState +
                "\nCoach Id = " + coachId +
                "\nCoach State = " + coachState +
                "\nContestant Id = " + contestantId +
                "\nContestant State = " + contestantState +
                "\nEnd of Operations (referee) = " + endOp +
                "\nName of logging file = " + fName);
    }
}
