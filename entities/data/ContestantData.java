package entities.data;

import entities.ContestantStates;

public class ContestantData {
    private int id;
    private int strength;
    private int team;
    private int state;

    public ContestantData(int id){
        this.id = id;
        this.state = ContestantStates.SEATATBENCH;
        this.team = 0;
        this.strength = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
