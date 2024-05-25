package interfaces;

/**
 /**
 *  Data type to return the data to contestants.
 *
 *  Used in calls on remote objects.
 */
public class ReturnCoach {
    /**
     *  Serialization key.
     */
    public static final long serialVersionUID = 2021L;

    /**
     * Coach state
     */
    private int state;

    /**
     * Constructor for coach state
     * @param state coach state
     */
    public ReturnCoach(int state){
        this.state = state;
    }

    /**
     * Get state
     * @return coach state
     */
    public int getState() {
        return state;
    }

    /**
     * Set state
     * @param state coach state
     */
    public void setState(int state) {
        this.state = state;
    }
}
