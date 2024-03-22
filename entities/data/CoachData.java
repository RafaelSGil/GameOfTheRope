package entities.data;

import entities.CoachStates;

public class CoachData {
    private int state;
    private int team;

    public CoachData(int team){
        this.team = team;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
}
