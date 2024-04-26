package GameOfTheRopeDistributedVersion.serverSide.entities;

import GameOfTheRopeDistributedVersion.clientSide.entities.*;
public class ContestantBenchProxy extends Thread implements CoachCloning, ContestantCloning {
    @Override
    public int getCoachState() {
        return 0;
    }

    @Override
    public void setCoachState(int coachState) {

    }

    @Override
    public int getCoachTeam() {
        return 0;
    }

    @Override
    public int getStrategy() {
        return 0;
    }

    @Override
    public void setStrategy(int strategy) {

    }

    @Override
    public int getContestantState() {
        return 0;
    }

    @Override
    public void setContestantState(int contestantState) {

    }

    @Override
    public int getContestantTeam() {
        return 0;
    }

    @Override
    public void setContestantTeam(int contestantTeam) {

    }

    @Override
    public int getContestantStrength() {
        return 0;
    }

    @Override
    public void setContestantStrength(int contestantStrength) {

    }

    @Override
    public int getContestantId() {
        return 0;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void setPlaying(boolean playing) {

    }

    @Override
    public void run() {

    }
}
