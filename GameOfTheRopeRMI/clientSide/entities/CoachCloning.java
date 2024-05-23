package clientSide.entities;


/**
 *  Coach Cloning
 *
 *  It specifies his own attributes
 *  Implementation of a client-server model of type 2 (server replication).
 *  Communication is based on a communication channel under the TCP protocol.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public interface CoachCloning {
    int getCoachState();



    /**
     * Sets the internal state of the coach.
     *
     * @param coachState The new state for the coach.
     */
    void setCoachState(int coachState);

    /**
     * Gets the team that the coach belongs to.
     *
     * @return The team number (0 or 1).
     */
    int getCoachTeam();

    /**
     * Sets the team that the coach belongs
     * @param team value to set
     */
    void setCoachTeam(int team);

    /**
     * Get the strategy
     *
     * @return strategy value
     */
    int getStrategy();

    /**
     * Set the new strategy
     *
     * @param strategy the new strategy
     */
    void setStrategy(int strategy);
}
