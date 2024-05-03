package serverSide.sharedRegions;

import clientSide.entities.CoachStates;
import clientSide.entities.ContestantStates;
import clientSide.entities.RefereeStates;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.PlaygroundProxy;
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
                if((inMessage.getRefereeState() != RefereeStates.STARTGAME) && (inMessage.getRefereeState() != RefereeStates.WAITTRIALCONCLUSION)){
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
                if((inMessage.getCoachId() < 0) || (inMessage.getCoachId() >= SimulationParams.NTEAMS)){
                    throw new MessageException ("Invalid coach id!", inMessage);
                }
                break;
            case MessageType.SETGR:
                if(inMessage.getContestantState() != ContestantStates.STANDINPOSITION){
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                if((inMessage.getContestantId() < 0) || (inMessage.getContestantId() >= SimulationParams.NCONTESTANTS)){
                    throw new MessageException ("Invalid contestant id!", inMessage);
                }
                if((inMessage.getContestantTeam() < 0) || (inMessage.getContestantTeam() >= SimulationParams.NTEAMS)){
                    throw new MessageException("Invalid contestant team", inMessage);
                }
                break;
            case MessageType.SETAID:
                if(inMessage.getContestantState() != ContestantStates.DOYOURBEST){
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                if((inMessage.getContestantId() < 0) || (inMessage.getContestantId() >= SimulationParams.NCONTESTANTS)){
                    throw new MessageException ("Invalid contestant id!", inMessage);
                }
                if((inMessage.getContestantTeam() < 0) || (inMessage.getContestantTeam() >= SimulationParams.NTEAMS)){
                    throw new MessageException("Invalid contestant team", inMessage);
                }
                break;
            case MessageType.END:
                if((!inMessage.getEntity().equals(SimulationParams.REFEREE)) && (!inMessage.getEntity().equals(SimulationParams.COACH)) && (!inMessage.getEntity().equals(SimulationParams.CONTESTANT))){
                    throw new MessageException("Invalid entity!", inMessage);
                }
                break;
            case MessageType.SHUT:
                break;
        }

        /* processing */
        switch (inMessage.getMsgType ()){
            case MessageType.SETCT:
                ((PlaygroundProxy) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
                ((PlaygroundProxy) Thread.currentThread()).setGame(inMessage.getGame());
                ((PlaygroundProxy) Thread.currentThread()).setTrial(inMessage.getTrial());
                playground.callTrial();
                outMessage = new Message(MessageType.CTDONE, ((PlaygroundProxy) Thread.currentThread()).getRefereeSate(), ((PlaygroundProxy) Thread.currentThread()).getGame(), ((PlaygroundProxy) Thread.currentThread()).getTrial());
                break;
            case MessageType.SETST:
                ((PlaygroundProxy) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
                playground.startTrial();
                outMessage = new Message(MessageType.STDONE, 0, ((PlaygroundProxy) Thread.currentThread()).getRefereeSate());
                break;
            case MessageType.SETATD:
                ((PlaygroundProxy) Thread.currentThread()).setRefereeSate(inMessage.getRefereeState());
                ((PlaygroundProxy) Thread.currentThread()).setGame(inMessage.getGame());
                ((PlaygroundProxy) Thread.currentThread()).setTrial(inMessage.getTrial());
                boolean result = playground.assertTrialDecision();
                if(result){
                    outMessage = new Message(MessageType.ATDDONE, ((PlaygroundProxy) Thread.currentThread()).getRefereeSate(),
                            ((PlaygroundProxy) Thread.currentThread()).getGameResult(((PlaygroundProxy) Thread.currentThread()).getGame() - 1), result,
                            ((PlaygroundProxy) Thread.currentThread()).getWinCause(), ((PlaygroundProxy) Thread.currentThread()).getMatchEnd());
                }else {
                    outMessage = new Message(MessageType.ATDDONE, ((PlaygroundProxy) Thread.currentThread()).getRefereeSate(),
                            ((PlaygroundProxy) Thread.currentThread()).getGameResult(((PlaygroundProxy) Thread.currentThread()).getGame() - 1), result);
                }
                break;
            case MessageType.SETIR:
                ((PlaygroundProxy) Thread.currentThread()).setCoachState(inMessage.getCoachState());
                ((PlaygroundProxy) Thread.currentThread()).setCoachTeam(inMessage.getCoachId());
                playground.informReferee();
                outMessage = new Message(MessageType.IRDONE, ((PlaygroundProxy) Thread.currentThread()).getCoachTeam(), ((PlaygroundProxy) Thread.currentThread()).getCoachState());
                break;
            case MessageType.SETGR:
                ((PlaygroundProxy) Thread.currentThread()).setContestantState(inMessage.getContestantState());
                ((PlaygroundProxy) Thread.currentThread()).setContestantId(inMessage.getContestantId());
                ((PlaygroundProxy) Thread.currentThread()).setContestantStrength(inMessage.getContestantStrength());
                ((PlaygroundProxy) Thread.currentThread()).setContestantTeam(inMessage.getContestantTeam());
                playground.getReady();
                outMessage = new Message(MessageType.GRDONE, ((PlaygroundProxy) Thread.currentThread()).getContestantId(), ((PlaygroundProxy) Thread.currentThread()).getContestantState());
                break;
            case MessageType.SETAID:
                ((PlaygroundProxy) Thread.currentThread()).setContestantState(inMessage.getContestantState());
                ((PlaygroundProxy) Thread.currentThread()).setContestantId(inMessage.getContestantId());
                ((PlaygroundProxy) Thread.currentThread()).setContestantStrength(inMessage.getContestantStrength());
                ((PlaygroundProxy) Thread.currentThread()).setContestantTeam(inMessage.getContestantTeam());
                playground.amIDone();
                outMessage = new Message(MessageType.AIDDONE, ((PlaygroundProxy) Thread.currentThread()).getContestantId(), ((PlaygroundProxy) Thread.currentThread()).getContestantState());
                break;
            case MessageType.END:
                playground.endOperation(inMessage.getEntity(), inMessage.getEntity().equals(SimulationParams.CONTESTANT) ? inMessage.getContestantId() :
                        inMessage.getEntity().equals(SimulationParams.COACH) ? inMessage.getCoachId() : 0);
                outMessage = new Message(MessageType.ENDREPLY);
                break;
            case MessageType.SHUT:
                playground.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return  outMessage;
    }
}
