package GameOfTheRopeDistributedVersion.serverSide.sharedRegions;

import GameOfTheRopeDistributedVersion.commInfra.Message;
import GameOfTheRopeDistributedVersion.commInfra.MessageException;
import GameOfTheRopeDistributedVersion.commInfra.MessageType;
import sharedregions.RefereeSite;

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
                break;
            case MessageType.SETDGW:
                break;
            case MessageType.SETDMW:
                break;
        }

        return  outMessage;
    }

}
