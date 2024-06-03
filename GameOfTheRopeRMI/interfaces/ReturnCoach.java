package interfaces;

import java.io.Serializable;

/**
 *  Data type to return the data to contestants.
 *
 *  Used in calls on remote objects.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class ReturnCoach implements Serializable {
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
