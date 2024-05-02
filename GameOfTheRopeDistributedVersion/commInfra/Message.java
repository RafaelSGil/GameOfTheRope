package commInfra;

import java.io.*;
import genclass.GenericIO;
import serverSide.main.SimulationParams;

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
     * contestant strength
     */
    private int contestantStrength = -1;

    /**
     * contestant team
     */
    private int contestantTeam = -1;

    /**
     *  End of operations (referee).
     */

    private boolean endOp = false;

    /**
     *  Name of the logging file.
     */

    private String fName = null;

    /**
     * Current game
     */
    private int game = -1;

    /**
     * Current trial
     */
    private int trial = -1;

    /**
     * Current position of the rope
     */
    private int ropePosition = 0;

    /**
     * winning team
     */
    private int winningTeam = -1;

    /**
     * winning cause
     */
    private String winningCause = null;

    /**
     * match end message
     */
    private String endMatchMsg = null;

    /**
     * flag symbolizing whether to print the header or not
     */
    private boolean printHeader = false;

    /**
     * flag to represent which entity asked to end the operations
     */
    private String entity = null;

    /**
     * result of the game
     */
    private int gameResult;

    /**
     * match final result
     */
    private String finalResult = null;

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
        if ((msgType == MessageType.SETCC) || (msgType == MessageType.SETIR) || (msgType == MessageType.SETRN) || (msgType == MessageType.UPCOA)){
            coachId= id;
            coachState = state;
        }
        else if ((msgType == MessageType.SETFCA) || (msgType == MessageType.FCADONE) || (msgType == MessageType.SETGR) ||
                (msgType == MessageType.SETAID) || (msgType == MessageType.SETSD)){
            contestantId= id;
            contestantState = state;
        }
        else if((msgType == MessageType.SETANG) || (msgType == MessageType.SETCT) || (msgType == MessageType.SETST)
                || (msgType == MessageType.SETATD) || (msgType == MessageType.STDONE)){
            refereeState = state;
        }else if((msgType == MessageType.SETT)){
            refereeState = state;
            trial = id;
        } else if ((msgType == MessageType.SETG)) {
            refereeState = state;
            game = id;
        } else { GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }

    /**
     *  Message instantiation (form 3).
     *
     *     @param type message type
     *     @param value integer value, assigned depending on the type
     */

    public Message (int type, int value)
    {
        msgType = type;

        switch (type){
            case MessageType.SETATD:
            case MessageType.SETCT:
            case MessageType.UPREF:
            case MessageType.ANGDONE:
            case MessageType.DGWDONE:
                refereeState = value;
                break;
            case MessageType.SETG:
                game = value;
                break;
            case MessageType.SETT:
                trial = value;
                break;
            case MessageType.SETRP:
                ropePosition = value;
                break;

        }
    }

    /**
     *  Message instantiation (form 4).
     *
     *     @param type message type
     *     @param flag end of operations flag / print header flag
     */

    public Message (int type, boolean flag)
    {
        msgType = type;
        switch (type){
            case MessageType.RS:
                this.printHeader = flag;
                break;
            case MessageType.EOFCDONE:
            case MessageType.SETATD:
            case MessageType.ATDDONE:
            case MessageType.ENDREPLY:
                this.endOp = flag;
                break;
        }
    }

    /**
     *  Message instantiation (form 5).
     *
     *     @param type message type
     *     @param value1 integer value
     *     @param value2 integer value
     *     @param value3 integer value
     */

    public Message (int type, int value1, int value2, int value3)
    {
        msgType = type;

        switch (type){
            case MessageType.SETANG:
            case MessageType.ANGDONE:
            case MessageType.SETCT:
            case MessageType.CTDONE:
            case MessageType.SETATD:
                this.refereeState = value1;
                this.game = value2;
                this.trial = value3;
                break;
        }
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
     *     @param name name of the logging file / match end message
     */

    public Message (int type, String name)
    {
        msgType = type;
        if(type == MessageType.SETNFIC){
            fName= name;
        }
        switch (type){
            case MessageType.SETNFIC:
                fName= name;
                break;
            case MessageType.SETMW:
                endMatchMsg = name;
                break;

        }
    }

    /**
     *  Message instantiation (form 8)
     *
     *  @param type               message type
     *  @param contestantId       The ID of the contestant to update.
     *  @param contestantStrength The strength of the contestant.
     *  @param contestantState    The state of the contestant.
     *  @param contestantTeam     The team of the contestant.
     */
    public Message(int type, int contestantId, int contestantStrength, int contestantState, int contestantTeam){
        this.msgType = type;
        this.contestantId = contestantId;
        this.contestantState = contestantState;
        this.contestantStrength = contestantStrength;
        this.contestantTeam = contestantTeam;
    }

    /**
     * Message instantiation (form 9)
     *
     * @param type message type
     * @param value1 integer value
     * @param value2 string value
     */
    public Message(int type, int value1, String value2){
        this.msgType = type;

        switch (type){
            case MessageType.SETGW:
                this.winningTeam = value1;
                this.winningCause = value2;
                break;
            case MessageType.SETDMW:
                this.refereeState = value1;
                this.finalResult = value2;
        }
    }

    /**
     *  Message instantiation (form 10).
     *
     *     @param type message type
     *     @param entity type of entity
     *     @param id id of the entity
     */
    public Message(int type, String entity, int id){
        msgType = type;

        switch (entity){
            case SimulationParams.REFEREE:
                this.entity = entity;
                break;
            case SimulationParams.COACH:
                this.entity = entity;
                this.coachId = id;
                break;
            case SimulationParams.CONTESTANT:
                this.entity = entity;
                this.contestantId = id;
                break;
        }
    }

    /**
     *  Message instantiation (form 11).
     *
     *     @param type message type
     *     @param state state of the referee
     *     @param flag end of operations flag / print header flag
     */
    public Message (int type, int state, boolean flag){
        msgType = type;
        refereeState = state;
        endOp = flag;
    }

    /**
     *  Message instantiation (form 12).
     *
     *     @param type message type
     *     @param state state of the referee
     *     @param gameResult result of the game
     *     @param flag end of operations flag / print header flag
     */
    public Message (int type, int state, int gameResult, boolean flag){
        msgType = type;
        refereeState = state;
        this.gameResult = gameResult;
        endOp = flag;
    }

    /**
     *  Message instantiation (form 13).
     *
     *     @param type message type
     *     @param value1 integer value
     *     @param value2 integer value
     *     @param value3 integer value
     *     @param winCause winning cause
     */

    public Message (int type, int value1, int value2, int value3, String winCause){
        msgType = type;
        refereeState = value1;
        game = value2;
        gameResult = value3;
        winningCause = winCause;
    }

    /**
     * get entity
     * @return entity type
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Set new entity
     * @param entity entity type
     */
    public void setEntity(String entity) {
        this.entity = entity;
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
     * Gets the strength of the contestant.
     *
     * @return The strength of the contestant.
     */
    public int getContestantStrength() {
        return contestantStrength;
    }

    /**
     * Sets the strength of the contestant.
     *
     * @param contestantStrength The strength of the contestant to set.
     */
    public void setContestantStrength(int contestantStrength) {
        this.contestantStrength = contestantStrength;
    }

    /**
     * Gets the team of the contestant.
     *
     * @return The team of the contestant.
     */
    public int getContestantTeam() {
        return contestantTeam;
    }

    /**
     * Sets the team of the contestant.
     *
     * @param contestantTeam The team of the contestant to set.
     */
    public void setContestantTeam(int contestantTeam) {
        this.contestantTeam = contestantTeam;
    }

    /**
     * Gets the game ID.
     *
     * @return The ID of the game.
     */
    public int getGame() {
        return game;
    }

    /**
     * Sets the game ID.
     *
     * @param game The ID of the game to set.
     */
    public void setGame(int game) {
        this.game = game;
    }

    /**
     * Gets the trial number.
     *
     * @return The trial number.
     */
    public int getTrial() {
        return trial;
    }

    /**
     * Sets the trial number.
     *
     * @param trial The trial number to set.
     */
    public void setTrial(int trial) {
        this.trial = trial;
    }

    /**
     * Gets the position of the rope.
     *
     * @return The position of the rope.
     */
    public int getRopePosition() {
        return ropePosition;
    }

    /**
     * Sets the position of the rope.
     *
     * @param ropePosition The position of the rope to set.
     */
    public void setRopePosition(int ropePosition) {
        this.ropePosition = ropePosition;
    }

    /**
     * Gets the ID of the winning team.
     *
     * @return The ID of the winning team.
     */
    public int getWinningTeam() {
        return winningTeam;
    }

    /**
     * Sets the ID of the winning team.
     *
     * @param winningTeam The ID of the winning team to set.
     */
    public void setWinningTeam(int winningTeam) {
        this.winningTeam = winningTeam;
    }

    /**
     * Gets the cause of winning.
     *
     * @return The cause of winning.
     */
    public String getWinningCause() {
        return winningCause;
    }

    /**
     * Sets the cause of winning.
     *
     * @param winningCause The cause of winning to set.
     */
    public void setWinningCause(String winningCause) {
        this.winningCause = winningCause;
    }

    /**
     * Gets the end match message.
     *
     * @return The end match message.
     */
    public String getEndMatchMsg() {
        return endMatchMsg;
    }

    /**
     * Sets the end match message.
     *
     * @param endMatchMsg The end match message to set.
     */
    public void setEndMatchMsg(String endMatchMsg) {
        this.endMatchMsg = endMatchMsg;
    }

    /**
     * Checks if the header should be printed.
     *
     * @return True if the header should be printed, false otherwise.
     */
    public boolean isPrintHeader() {
        return printHeader;
    }

    /**
     * Sets whether the header should be printed.
     *
     * @param printHeader True if the header should be printed, false otherwise.
     */
    public void setPrintHeader(boolean printHeader) {
        this.printHeader = printHeader;
    }

    /**
     * returns the result of the game
     * @return game result
     */
    public int getGameResult() {
        return gameResult;
    }

    /**
     * Set the result of the game
     * @param gameResult result of the game
     */
    public void setGameResult(int gameResult) {
        this.gameResult = gameResult;
    }

    /**
     *
     * @return the final result of the match
     */
    public String getFinalResult() {
        return finalResult;
    }

    /**
     * Set the final result of the match
     * @param finalResult string with the result
     */
    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
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
