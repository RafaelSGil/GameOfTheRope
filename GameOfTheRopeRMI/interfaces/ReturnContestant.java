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
public class ReturnContestant implements Serializable {
    /**
     *  Serialization key.
     */
    public static final long serialVersionUID = 2021L;

    /**
     * Contestant state
     */
    private int state;

    /**
     * Contestant strength
     */
    private int strength;

    /**
     * contestant playing
     */
    private boolean isPlaying;

    /**
     * Constructor for contestant state and strength
     * @param state contestant state
     */
    public ReturnContestant(int state){
        this.state = state;
    }

    /**
     * Constructor for contestant state and strength
     * @param state contestant state
     * @param strength contestant strength
     */
    public ReturnContestant(int state, int strength){
        this.state = state;
        this.strength = strength;
    }

    /**
     * Constructor for contestant state and strength
     * @param state contestant state
     * @param strength contestant strength
     * @param isPlaying contestant playing
     */
    public ReturnContestant(int state, int strength, boolean isPlaying){
        this.state = state;
        this.strength = strength;
        this.isPlaying = isPlaying;
    }

    /**
     * Get state
     * @return contestant state
     */
    public int getState() {
        return state;
    }

    /**
     * Set state
     * @param state contestant state
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Get strength
     * @return contestant strength
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Set strength
     * @param strength contestant strength
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * Get isPlaying
     * @return whether the contestant is playing or not
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Set isPlaying
     * @param playing whether the contestant is playing or not
     */
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
