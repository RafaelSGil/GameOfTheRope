package entities.data;

import entities.RefereeStates;

public class RefereeData {
    private int state;

    public RefereeData(){
        this.state = RefereeStates.STARTMATCH;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
