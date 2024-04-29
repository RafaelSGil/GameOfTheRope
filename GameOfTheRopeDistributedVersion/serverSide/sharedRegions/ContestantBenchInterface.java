package serverSide.sharedRegions;

import clientSide.entities.CoachStates;
import clientSide.entities.ContestantStates;
import clientSide.entities.RefereeStates;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.ContestantBenchProxy;
import serverSide.entities.RefereeSiteProxy;
import serverSide.main.SimulationParams;

public class ContestantBenchInterface {
    /**
     * Reference for the contestant bench
     */
    private final ContestantsBench contestantsBench;

    /**
     * Instantiation of an interface for contestant bench
     * @param contestantsBench reference to the contestant bench
     */
    public ContestantBenchInterface(ContestantsBench contestantsBench){
        this.contestantsBench = contestantsBench;
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
            case MessageType.UCB:
                break;
            case MessageType.SETCT:
                break;
            case MessageType.SETATD:
                break;
            case MessageType.SETCC:
                if((inMessage.getCoachId() < 0) || inMessage.getCoachId() > SimulationParams.NTEAMS){
                    throw new MessageException("Invalid coach id!", inMessage);
                }
                if((inMessage.getCoachState() != CoachStates.WATFORREFEREECOMMAND)){
                    throw new MessageException("Invalid coach state!", inMessage);
                }
                break;
            case MessageType.SETFCA:
                if((inMessage.getContestantId() < 0) || inMessage.getContestantId() > SimulationParams.NCONTESTANTS){
                    throw new MessageException("Invalid contestant id!", inMessage);
                }
                if((inMessage.getContestantState() != ContestantStates.SEATATBENCH)){
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                break;
            case MessageType.SETSD:
                if((inMessage.getContestantId() < 0) || inMessage.getContestantId() > SimulationParams.NCONTESTANTS){
                    throw new MessageException("Invalid contestant id!", inMessage);
                }
                if((inMessage.getContestantState() != ContestantStates.DOYOURBEST)){
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                break;
            case MessageType.SETRN:
                if((inMessage.getCoachId() < 0) || inMessage.getCoachId() > SimulationParams.NTEAMS){
                    throw new MessageException("Invalid coach id!", inMessage);
                }
                if((inMessage.getCoachState() != CoachStates.WATCHTRIAL)){
                    throw new MessageException("Invalid coach state!", inMessage);
                }
                break;
        }

        /* processing */

        switch (inMessage.getMsgType ()){
            case MessageType.UCB:
                contestantsBench.unblockContestantBench();
                outMessage = new Message(MessageType.UCBDONE);
                break;
            case MessageType.SETCT:
                contestantsBench.refereeCallTrial();
                outMessage = new Message(MessageType.CTDONE);
                break;
            case MessageType.SETATD:
                contestantsBench.setHasTrialEnded(inMessage.getEndOp());
                outMessage = new Message(MessageType.ATDDONE);
                break;
            case MessageType.SETCC:
                ((ContestantBenchProxy) Thread.currentThread()).setCoachState(inMessage.getCoachState());
                contestantsBench.callContestants(inMessage.getCoachId());
                outMessage = new Message(MessageType.CCDONE);
                break;
            case MessageType.SETFCA:
                ((ContestantBenchProxy) Thread.currentThread()).setContestantState(inMessage.getContestantState());
                ((ContestantBenchProxy) Thread.currentThread()).setContestantStrength(inMessage.getContestantStrength());
                ((ContestantBenchProxy) Thread.currentThread()).setContestantTeam(inMessage.getContestantTeam());
                contestantsBench.followCoachAdvice();
                outMessage = new Message(MessageType.FCADONE);
                break;
            case MessageType.SETSD:
                ((ContestantBenchProxy) Thread.currentThread()).setContestantState(inMessage.getContestantState());
                ((ContestantBenchProxy) Thread.currentThread()).setContestantStrength(inMessage.getContestantStrength());
                ((ContestantBenchProxy) Thread.currentThread()).setContestantTeam(inMessage.getContestantTeam());
                contestantsBench.seatDown();
                outMessage = new Message(MessageType.SDDONE);
                break;
            case MessageType.SETRN:
                ((ContestantBenchProxy) Thread.currentThread()).setCoachState(inMessage.getCoachState());
                contestantsBench.reviewNotes();
                outMessage = new Message(MessageType.RNDONE);
                break;
        }

        return  outMessage;
    }
}
