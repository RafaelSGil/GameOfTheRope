package sharedregions;

import entities.Contestant;
import entities.ContestantStates;
import entities.Referee;
import entities.RefereeStates;

public class Playground {
    private final GeneralRepository repository;
    public Playground(GeneralRepository repository){
        this.repository = repository;
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

    public synchronized void informReferee(){
        


    }
    public synchronized void getReady() {

    }

    public synchronized void amIDone(){}
}
