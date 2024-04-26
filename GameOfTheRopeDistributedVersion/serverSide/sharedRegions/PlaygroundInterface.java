package serverSide.sharedRegions;

import clientSide.entities.CoachStates;
import clientSide.entities.ContestantStates;
import clientSide.entities.RefereeStates;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.RefereeSiteProxy;
import serverSide.main.SimulationParams;

public class PlaygroundInterface {
    /**
     * Reference to the playground
     */
    private final Playground playground;

    /**
     * Instantiation of an interface for playground
     * @param playground reference to playground
     */
    public PlaygroundInterface(Playground playground){
        this.playground = playground;
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
            case MessageType.SETCT:
                if((inMessage.getRefereeState() != RefereeStates.STARTGAME) || (inMessage.getRefereeState() != RefereeStates.WAITTRIALCONCLUSION)){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.SETST:
                if(inMessage.getRefereeState() != RefereeStates.TEAMSREADY){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.SETATD:
                if(inMessage.getRefereeState() != RefereeStates.WAITTRIALCONCLUSION){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.SETIR:
                if(inMessage.getCoachState() != CoachStates.ASSEMBLETEAM){
                    throw new MessageException("Invalid coach state!", inMessage);
                }
                if((inMessage.getCoachId() < 0) || (inMessage.getCoachId() > SimulationParams.NTEAMS)){
                    throw new MessageException ("Invalid coach id!", inMessage);
                }
                break;
            case MessageType.SETGR:
                if(inMessage.getContestantState() != ContestantStates.STANDINPOSITION){
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                if((inMessage.getContestantId() < 0) || (inMessage.getContestantId() > SimulationParams.NCONTESTANTS)){
                    throw new MessageException ("Invalid contestant id!", inMessage);
                }
                break;
            case MessageType.SETAID:
                if(inMessage.getContestantState() != ContestantStates.DOYOURBEST){
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                if((inMessage.getContestantId() < 0) || (inMessage.getContestantId() > SimulationParams.NCONTESTANTS)){
                    throw new MessageException ("Invalid contestant id!", inMessage);
                }
                break;
        }

        /* processing */
        switch (inMessage.getMsgType ()){
            case MessageType.SETCT:
                ((RefereeSiteProxy) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
                playground.callTrial();
                outMessage = new Message(MessageType.ANGDONE, 0, ((RefereeSiteProxy) Thread.currentThread()).getRefereeSate());
                break;
            case MessageType.SETST:
                ((RefereeSiteProxy) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
                playground.startTrial();
                outMessage = new Message(MessageType.STDONE, 0, ((RefereeSiteProxy) Thread.currentThread()).getRefereeSate());
                break;
            case MessageType.SETATD:
                ((RefereeSiteProxy) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
                playground.assertTrialDecision();
                outMessage = new Message(MessageType.ATDDONE, 0, ((RefereeSiteProxy) Thread.currentThread()).getRefereeSate());
                break;
            case MessageType.SETIR:
                ((RefereeSiteProxy) Thread.currentThread()).setCoachState(inMessage.getCoachState());
                playground.informReferee();
                outMessage = new Message(MessageType.IRDONE, inMessage.getCoachId(), inMessage.getCoachState());
                break;
            case MessageType.SETGR:
                ((RefereeSiteProxy) Thread.currentThread()).setContestantState(inMessage.getContestantState());
                playground.getReady();
                outMessage = new Message(MessageType.GRDONE, inMessage.getContestantId(), inMessage.getContestantState());
                break;
            case MessageType.SETAID:
                ((RefereeSiteProxy) Thread.currentThread()).setContestantState(inMessage.getContestantState());
                playground.amIDone();
                outMessage = new Message(MessageType.GRDONE, inMessage.getContestantId(), inMessage.getContestantState());
                break;
        }

        return  outMessage;
    }
}
