package serverSide.sharedRegions;


import clientSide.entities.RefereeStates;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.RefereeSiteProxy;

public class RefereeSiteInterface {
    /**
     * Reference to the referee site
     */
    private final RefereeSite refereeSite;

    /**
     * Instantiation of an interface for referee site
     * @param refereeSite reference to the referee site
     */
    public RefereeSiteInterface(RefereeSite refereeSite){
        this.refereeSite = refereeSite;
    }

    /**
     *  Processing of the incoming messages.
     *
     *  Validation, execution of the corresponding method and generation of the outgoing message.
     *
     *    @param inMessage service request
     *    @return service reply
     *    @throws MessageException if the incoming message is not valid
     */

    public Message processAndReply (Message inMessage) throws MessageException{
        Message outMessage = null;                                     // outgoing message

        /* validation of the incoming message */
        switch (inMessage.getMsgType ()){
            case MessageType.SETANG:
                if((inMessage.getRefereeState() != RefereeStates.STARTMATCH) && (inMessage.getRefereeState() != RefereeStates.ENDGAME)){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.SETDGW:
                if(inMessage.getRefereeState() != RefereeStates.WAITTRIALCONCLUSION){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.SETDMW:
                if(inMessage.getRefereeState() != RefereeStates.ENDGAME){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.SETEOFC:
                break;
            case MessageType.MATCHEND:
                break;
            case MessageType.END:
                    break;
            case MessageType.SHUT:
                break;
        }

        /* processing */

        switch (inMessage.getMsgType ()){
            case MessageType.SETANG:
                ((RefereeSiteProxy) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
                ((RefereeSiteProxy) Thread.currentThread()).setGame(inMessage.getGame());
                ((RefereeSiteProxy) Thread.currentThread()).setTrial(inMessage.getTrial());
                refereeSite.announceNewGame();
                outMessage = new Message(MessageType.ANGDONE, ((RefereeSiteProxy) Thread.currentThread()).getRefereeSate(),
                        ((RefereeSiteProxy) Thread.currentThread()).getGame(), ((RefereeSiteProxy) Thread.currentThread()).getTrial());
                break;
            case MessageType.SETDGW:
                ((RefereeSiteProxy) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
                ((RefereeSiteProxy) Thread.currentThread()).setGame(inMessage.getGame());
                ((RefereeSiteProxy) Thread.currentThread()).setGameResult(inMessage.getGameResult());
                ((RefereeSiteProxy) Thread.currentThread()).setWinCause(inMessage.getWinningCause());
                refereeSite.declareGameWinner();
                outMessage = new Message(MessageType.DGWDONE, ((RefereeSiteProxy) Thread.currentThread()).getRefereeSate());
                break;
            case MessageType.SETDMW:
                ((RefereeSiteProxy) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
                ((RefereeSiteProxy) Thread.currentThread()).setFinalResults(inMessage.getFinalResult());
                refereeSite.declareMatchWinner();
                outMessage = new Message(MessageType.DMWDONE, 0, ((RefereeSiteProxy) Thread.currentThread()).getRefereeSate());
                break;
            case MessageType.SETEOFC:
                ((RefereeSiteProxy) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
                refereeSite.setMatchEnd(true);
                outMessage = new Message(MessageType.EOFCDONE, 0, ((RefereeSiteProxy) Thread.currentThread()).getRefereeSate());
                break;
            case MessageType.MATCHEND:
                outMessage = new Message(MessageType.MATCHENDDONE, refereeSite.endOfMatch());
                break;
            case MessageType.END:
                refereeSite.endOperation();
                outMessage = new Message(MessageType.ENDREPLY);
                break;
            case MessageType.SHUT:
                refereeSite.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return  outMessage;
    }

}
