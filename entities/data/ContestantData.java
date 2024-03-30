package entities.data;

import entities.ContestantStates;

/**
 * This class encapsulates data related to a Contestant entity in the Game of the Rope simulation.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class ContestantData {
    /**
     * The unique identifier for the Contestant.
     */
    private int id;
    /**
     * The Contestant's strength level, used for determining pulling power in trials.
     */
    private int strength;
    /**
     * The team number assigned to the Contestant.
     */
    private int team;
    /**
     * The current state of the Contestant, represented by an integer value.
     */
    private int state;

    /**
     * Creates a new ContestantData instance.
     *
     * @param id The unique identifier for the Contestant.
     */
    public ContestantData(int id) {
        this.id = id;
        this.state = ContestantStates.SEATATBENCH;
        this.team = -1;
        this.strength = 0;
    }

    /**
     * Retrieves the Contestant's unique identifier.
     *
     * @return The integer value representing the Contestant's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the Contestant's unique identifier.
     *
     * @param id The new identifier value for the Contestant.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the Contestant's strength level.
     *
     * @return The integer value representing the Contestant's strength.
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Sets the Contestant's strength level.
     *
     * @param strength The new strength value for the Contestant.
     */

    public void setStrength(int strength) {
        this.strength = strength;
    }

    /**
     * Retrieves the team number assigned to the Contestant.
     *
     * @return The team number of the Contestant, or -1 if unassigned.
     */
    public int getTeam() {
        return team;
    }


    /**
     * Sets the team number assigned to the Contestant.
     *
     * @param team The new team number for the Contestant.
     */
    public void setTeam(int team) {
        this.team = team;
    }

    /**
     * Retrieves the current state of the Contestant.
     *
     * @return The integer value representing the Contestant's state.
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the current state of the Contestant.
     *
     * @param state The new state value for the Contestant.
     */
    public void setState(int state) {
        this.state = state;
    }
}
