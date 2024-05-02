package serverSide.sharedRegions;

import clientSide.entities.RefereeStates;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.main.SimulationParams;

/**
 *  Interface to the General Repository of Information.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    General Repository and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class GeneralRepositoryInterface {
    /**
     *  Reference to the general repository.
     */

    private final GeneralRepository repos;

    /**
     *  Instantiation of an interface to the general repository.
     *
     *    @param repos reference to the general repository
     */

    public GeneralRepositoryInterface (GeneralRepository repos)
    {
        this.repos = repos;
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

    public Message processAndReply (Message inMessage) throws MessageException
    {
        Message outMessage = null;                                     // mensagem de resposta

        /* validation of the incoming message */
        switch (inMessage.getMsgType ()){
            case MessageType.SETNFIC:
                if (inMessage.getLogFName () == null){
                    throw new MessageException ("Name of the logging file is not present!", inMessage);
                }
                break;
            case MessageType.SETG:
                if((inMessage.getRefereeState() != RefereeStates.STARTMATCH) || (inMessage.getRefereeState() != RefereeStates.ENDGAME)){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.SETT:
                if((inMessage.getRefereeState() != RefereeStates.STARTMATCH) || (inMessage.getRefereeState() != RefereeStates.TEAMSREADY)){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.SETRP:
                if((inMessage.getRefereeState() != RefereeStates.WAITTRIALCONCLUSION)){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.UPREF:
                break;
            case MessageType.UPCOA:
                if((inMessage.getCoachId() < 0) || (inMessage.getCoachId() >= SimulationParams.NTEAMS)){
                    throw new MessageException("Invalid coach id!", inMessage);
                }
                break;
            case MessageType.UPCONT:
                if((inMessage.getContestantId() < 0) || (inMessage.getContestantId() >= SimulationParams.NCONTESTANTS)){
                    throw new MessageException("Invalid contestant id!", inMessage);
                }
                break;
            case MessageType.SETGW:
                if((inMessage.getRefereeState() != RefereeStates.ENDGAME)){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.SETMW:
                if((inMessage.getRefereeState() != RefereeStates.ENDMATCH)){
                    throw new MessageException("Invalid referee state!", inMessage);
                }
                break;
            case MessageType.RS:
                break;
            case MessageType.RGS:
                break;
            case MessageType.RGSTR:
                break;
        }

        /* processing */
        switch (inMessage.getMsgType ()){
            case MessageType.SETNFIC:
                repos.initSimul(inMessage.getLogFName());
                outMessage = new Message(MessageType.NFICDONE);
                break;
            case MessageType.SETG:
                repos.setGame(inMessage.getGame());
                outMessage = new Message(MessageType.GDONE);
                break;
            case MessageType.SETT:
                repos.setTrial(inMessage.getTrial());
                outMessage = new Message(MessageType.SETTDONE);
                break;
            case MessageType.SETRP:
                repos.setRopePosition(inMessage.getRopePosition());
                outMessage = new Message(MessageType.RPDONE);
                break;
            case MessageType.UPREF:
                repos.updateReferee(inMessage.getRefereeState());
                outMessage = new Message(MessageType.UPREFDONE);
                break;
            case MessageType.UPCOA:
                repos.updateCoach(inMessage.getCoachState(), inMessage.getCoachId());
                outMessage = new Message(MessageType.UPCOADONE);
                break;
            case MessageType.UPCONT:
                repos.updateContestant(inMessage.getContestantId(), inMessage.getContestantStrength(), inMessage.getContestantState(), inMessage.getContestantTeam());
                outMessage = new Message(MessageType.UPCONTDONE);
                break;
            case MessageType.SETGW:
                repos.declareGameWinner(inMessage.getWinningTeam(), inMessage.getWinningCause());
                outMessage = new Message(MessageType.GWDONE);
                break;
            case MessageType.SETMW:
                repos.declareMatchWinner(inMessage.getEndMatchMsg());
                outMessage = new Message(MessageType.MWDONE);
                break;
            case MessageType.RS:
                repos.reportStatus(inMessage.isPrintHeader());
                outMessage = new Message(MessageType.RSDONE);
                break;
            case MessageType.RGS:
                repos.reportGameStatus();
                outMessage = new Message(MessageType.RGSDONE);
                break;
            case MessageType.RGSTR:
                repos.reportGameStart();
                outMessage = new Message(MessageType.RGSTRDONE);
                break;
        }

        return (outMessage);
    }
}
