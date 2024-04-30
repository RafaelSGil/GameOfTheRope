package commInfra;

/**
 *   Type of the exchanged messages.
 *
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */

public class MessageType
{
    /**
     *  Initialization of the logging file name and the number of iterations (service request).
     */
    public static final int SETNFIC = 1;

    /**
     *  Logging file was initialized (reply).
     */
    public static final int NFICDONE = 2;

    /**
     *  Server shutdown (service request).
     */
    public static final int SHUT = 3;

    /**
     *  Server was shutdown (reply).
     */
    public static final int SHUTDONE = 4;

    /**
     * Set end of match (service request).
     */
    public static final int SETEOFC = 5;

    /**
     * End of match (reply).
     */
    public static final int EOFCDONE = 6;

    //todo ################### COACH ###################

    /**
     * Call Contestant (service request).
     */
    public static final int SETCC = 7;

    /**
     * Call Contestant (reply).
     */
    public static final int CCDONE = 8;

    /**
     * Inform Referee (service request).
     */

    public static final int SETIR = 9;

    /**
     * Inform Referee (reply).
     */

    public static final int IRDONE = 10;

    /**
     * Review Notes (service request).
     */

    public static final int SETRN = 11;

    /**
     *  Review Notes (reply).
     */

    public static final int RNDONE = 12;

    //todo ################### CONTESTANT ###################

    /**
     * Follow Coach Advice (service request).
     */
    public static final int SETFCA = 13;
    /**
     *  Follow Coach Advice (reply).
     */
    public static final int FCADONE = 14;

    /**
     * Get Ready (service request).
     */
    public static final int SETGR = 15;

    /**
     * Get Ready (reply).
     */
    public static final int GRDONE = 16;

    /**
     * Set Am I Done (service request).
     */

    public static final int SETAID = 17;

    /**
     * Am I Done (reply).
     */
    public static final int AIDDONE = 18;

    /**
     * Seat Down (service request).
     */
    public static final int SETSD = 19;

    /**
     * Seat Down (reply).
     */
    public static final int SDDONE = 20;



    //todo ################### REFEREE ###################

    /**
     * Announce New Game (service request).
     */
    public static final int SETANG = 23;

    /**
     * Announce New Game (reply).
     */
    public static final int ANGDONE = 24;

    /**
     * Call Trial (service request).
     */

    public static final int SETCT = 25;

    /**
     * Call Trial (reply).
     */
    public static final int CTDONE = 26;

    /**
     * Start Trial (service request).
     */
    public static final int SETST = 27;

    /**
     * Start Trial (reply).
     */
    public static final int STDONE = 28;

    /**
     * Assert Trial Decision (service request).
     */
    public static final int SETATD = 29;

    /**
     * Assert Trial Decision (reply).
     */
    public static final int ATDDONE = 30;

    /**
     * Declare Game Winner (service request).
     */
    public static final int SETDGW = 31;

    /**
     * Declare Game Winner (reply).
     */
    public static final int DGWDONE = 32;

    /**
     * Declare Match Winner (service request).
     */
    public static final int SETDMW = 33;

    /**
     * Declare Match Winner (reply).
     */
    public static final int DMWDONE = 34;

    /**
     * Unblock the contestant bench (service request)
     */
    public static final int UCB = 35;

    /**
     * Unblock the contestant bench (reply)
     */
    public static final int UCBDONE = 36;

    //todo ################### GENERAL REPOSITORY ###################

    /**
     * Set Game (service request).
     */
    public static final int SETG = 37;

    /**
     * Set Game (reply).
     */
    public static final int GDONE = 38;

    /**
     * Get Game (service request).
     */
    public static final int GETG = 39;

    /**
     * Get Game (reply).
     */
    public static final int GETGDONE = 40;

    /**
     * Get Trial (service request).
     */
    public static final int GETT = 41;

    /**
     * Get Trial (reply).
     */
    public static final int GETTDONE = 42;

    /**
     * Set Trial (service request).
     */
    public static final int SETT = 43;

    /**
     * Set Trial (reply).
     */
    public static final int SETTDONE = 44;

    /**
     * Set Rope Position (service request).
     */
    public static final int SETRP = 45;

    /**
     * Set Rope Position (reply).
     */
    public static final int RPDONE = 46;

    /**
     * Update Referee (service request).
     */
    public static final int UPREF = 47;

    /**
     * Update Referee (reply).
     */
    public static final int UPREFDONE = 48;

    /**
     * Update Coach (service request).
     */
    public static final int UPCOA = 49;

    /**
     * Update Coach (reply).
     */
    public static final int UPCOADONE = 50;

    /**
     * Update Contestant (service request).
     */
    public static final int UPCONT = 51;

    /**
     * Update Contestant (reply).
     */
    public static final int UPCONTDONE = 52;

    /**
     * Set Game Winner in General Repository (service request).
     */
    public static final int SETGW = 53;

    /**
     * Set Game Winner in General Repository (reply).
     */
    public static final int GWDONE = 54;

    /**
     * Set Match Winner in General Repository (service request).
     */
    public static final int SETMW = 55;

    /**
     * Set Match Winner in General Repository (reply).
     */
    public static final int MWDONE = 56;

    /**
     * Report the general state of the game (service request)
     * Write to logger
     */
    public static final int RS = 57;

    /**
     * Report the general state of the game (reply)
     * Write to logger
     */
    public static final int RSDONE = 58;

    /**
     * Report the game state of the game (service request)
     * Write to logger
     */
    public static final int RGS = 59;

    /**
     * Report the game state of the game (reply)
     * Write to logger
     */
    public static final int RGSDONE = 60;

    /**
     * Report the start game state of the game (service request)
     * Write to logger
     */
    public static final int RGSTR = 61;

    /**
     * Report the start game state of the game (reply)
     * Write to logger
     */
    public static final int RGSTRDONE = 62;

    //todo ===============================

    /**
     * end of the match flag (service request)
     */
    public static final int END = 63;

    /**
     * end of the match flag (reply)
     */
    public static final int ENDREPLY = 64;

}
