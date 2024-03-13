package sharedregions;

import entities.Contestant;
import entities.ContestantStates;
import entities.Referee;
import entities.RefereeStates;
import main.SimulationParams;

public class Playground {
    private Contestant[] contestants;
    private final GeneralRepository repository;
    public Playground(GeneralRepository repository){
        this.repository = repository;
        this.contestants = new Contestant[SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = null;
        }
    }

    public synchronized void callTrial(){
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.TEAMSREADY);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());
        repository.setTrial(repository.getTrial() + 1);
        // TODO synchronize, will wake up the coaches
    }

    public synchronized void startTrial(){
        // TODO synchronize, will get waken up by the last coach
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.WAITTRIALCONCLUSION);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());
        repository.setRopePosition(0);
    }

    public synchronized boolean assertTrialDecision(){
        // TODO synchronize, will get waken up by the last contestant
        return false;

    }

    public synchronized void getReady(){
        int contestantId = ((Contestant) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((Contestant) Thread.currentThread());
        contestants[contestantId].setContestantState(ContestantStates.DOYOURBEST);
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());
    }

    public synchronized void amIDone(){
        int contestantId = ((Contestant) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((Contestant) Thread.currentThread());
        contestants[contestantId].setContestantState(ContestantStates.DOYOURBEST);
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());

        //TODO synchronization, will wake up the referee and then wait for referee
    }
}
