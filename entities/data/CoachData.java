package entities.data;

/**
 * This class encapsulates data related to a Coach entity in the Game of the Rope simulation.
 * It holds information about the Coach's current state and the team they are assigned to.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class CoachData {

    /**
     * The current state of the Coach, represented by an integer value.
     */
    private int state;

    /**
     * The team number assigned to the Coach.
     */
    private int team;

    /**
     * Creates a new CoachData instance with the specified team number.
     *
     * @param team The team number for the Coach.
     */
    public CoachData(int team){
        this.team = team;
    }

    /**
     * Retrieves the current state of the Coach.
     *
     * @return The integer value representing the Coach's state.
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the current state of the Coach.
     *
     * @param state The new state value for the Coach.
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Retrieves the team number assigned to the Coach.
     *
     * @return The team number of the Coach.
     */
    public int getTeam() {
        return team;
    }

    /**
     * Sets the team number assigned to the Coach.
     *
     * @param team The new team number of the Coach
     */
    public void setTeam(int team) {
        this.team = team;
    }
}
