package entities.data;

import entities.CoachStates;

public class CoachData {
    private int state;
    private int team;

    public CoachData(){
        this.state = CoachStates.WATFORREFEREECOMMAND;
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
