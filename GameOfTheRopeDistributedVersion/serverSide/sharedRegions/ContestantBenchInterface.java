package serverSide.sharedRegions;

import clientSide.entities.CoachStates;
import clientSide.entities.ContestantStates;
import clientSide.entities.RefereeStates;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.ContestantBenchProxy;
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
                if(inMessage.getRefereeState() != RefereeStates.WAITTRIALCONCLUSION){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.SETCT:
                break;
            case MessageType.SETATD:
                if(inMessage.getRefereeState() != RefereeStates.WAITTRIALCONCLUSION){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.SETCC:
                if((inMessage.getCoachId() < 0) || inMessage.getCoachId() >= SimulationParams.NTEAMS){
                    throw new MessageException("Invalid coach id!", inMessage);
                }
                if((inMessage.getCoachState() != CoachStates.WATFORREFEREECOMMAND)){
                    throw new MessageException("Invalid coach state!", inMessage);
                }
                break;
            case MessageType.SETFCA:
                if((inMessage.getContestantId() < 0) || inMessage.getContestantId() >= SimulationParams.NCONTESTANTS){
                    throw new MessageException("Invalid contestant id!", inMessage);
                }
                if((inMessage.getContestantState() != ContestantStates.SEATATBENCH)){
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                if((inMessage.getContestantTeam() < 0) || (inMessage.getContestantTeam() >= SimulationParams.NTEAMS)){
                    throw new MessageException("Invalid contestant team", inMessage);
                }
                break;
            case MessageType.SETSD:
                if((inMessage.getContestantId() < 0) || inMessage.getContestantId() > SimulationParams.NCONTESTANTS){
                    throw new MessageException("Invalid contestant id!", inMessage);
                }
                if((inMessage.getContestantState() != ContestantStates.DOYOURBEST) && (inMessage.getContestantState() != ContestantStates.SEATATBENCH)){
                    throw new MessageException("Invalid contestant state!", inMessage);
                }
                if((inMessage.getContestantTeam() < 0) || (inMessage.getContestantTeam() >= SimulationParams.NTEAMS)){
                    throw new MessageException("Invalid contestant team", inMessage);
                }
                break;
            case MessageType.SETRN:
                if((inMessage.getCoachId() < 0) || inMessage.getCoachId() >= SimulationParams.NTEAMS){
                    throw new MessageException("Invalid coach id!", inMessage);
                }
                if((inMessage.getCoachState() != CoachStates.WATCHTRIAL)){
                    throw new MessageException("Invalid coach state!", inMessage);
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
                ((ContestantBenchProxy) Thread.currentThread()).setCoachTeam(inMessage.getCoachId());
                ((ContestantBenchProxy) Thread.currentThread()).setStrategy(inMessage.getStrategy());
                contestantsBench.callContestants(inMessage.getCoachId());
                outMessage = new Message(MessageType.CCDONE, ((ContestantBenchProxy) Thread.currentThread()).getCoachState());
                break;
            case MessageType.SETFCA:
                ((ContestantBenchProxy) Thread.currentThread()).setContestantState(inMessage.getContestantState());
                ((ContestantBenchProxy) Thread.currentThread()).setContestantStrength(inMessage.getContestantStrength());
                ((ContestantBenchProxy) Thread.currentThread()).setContestantTeam(inMessage.getContestantTeam());
                ((ContestantBenchProxy) Thread.currentThread()).setContestantId(inMessage.getContestantId());
                contestantsBench.followCoachAdvice();
                outMessage = new Message(MessageType.FCADONE, ((ContestantBenchProxy) Thread.currentThread()).getContestantId(),
                        ((ContestantBenchProxy) Thread.currentThread()).getContestantState(), ((ContestantBenchProxy) Thread.currentThread()).isPlaying());
                break;
            case MessageType.SETSD:
                ((ContestantBenchProxy) Thread.currentThread()).setContestantState(inMessage.getContestantState());
                ((ContestantBenchProxy) Thread.currentThread()).setContestantStrength(inMessage.getContestantStrength());
                ((ContestantBenchProxy) Thread.currentThread()).setContestantTeam(inMessage.getContestantTeam());
                ((ContestantBenchProxy) Thread.currentThread()).setContestantId(inMessage.getContestantId());
                ((ContestantBenchProxy) Thread.currentThread()).setPlaying(inMessage.isPlaying());
                contestantsBench.seatDown();
                outMessage = new Message(MessageType.SDDONE, ((ContestantBenchProxy) Thread.currentThread()).getContestantId(), ((ContestantBenchProxy) Thread.currentThread()).getContestantStrength(),
                        ((ContestantBenchProxy) Thread.currentThread()).getContestantState(), ((ContestantBenchProxy) Thread.currentThread()).getContestantTeam(),
                        ((ContestantBenchProxy) Thread.currentThread()).isPlaying());
                break;
            case MessageType.SETRN:
                ((ContestantBenchProxy) Thread.currentThread()).setCoachState(inMessage.getCoachState());
                ((ContestantBenchProxy) Thread.currentThread()).setCoachTeam(inMessage.getCoachId());
                contestantsBench.reviewNotes();
                outMessage = new Message(MessageType.RNDONE, ((ContestantBenchProxy) Thread.currentThread()).getCoachState());
                break;
            case MessageType.END:
                contestantsBench.endOperation(inMessage.getEntity(), inMessage.getEntity().equals(SimulationParams.CONTESTANT) ? inMessage.getContestantId() :
                        inMessage.getEntity().equals(SimulationParams.COACH) ? inMessage.getCoachId() : 0);
                outMessage = new Message(MessageType.ENDREPLY);
                break;
            case MessageType.SHUT:
                contestantsBench.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return  outMessage;
    }
}
