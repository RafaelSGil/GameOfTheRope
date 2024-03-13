package sharedregions;

public class Playground {
    private final GeneralRepository repository;
    public Playground(GeneralRepository repository){
        this.repository = repository;
    }

    public synchronized void callTrial(){

    }

    public synchronized void startTrial(){

    }

    public synchronized boolean assertTrialDecision(){
        return false;

    }

    public synchronized void getReady(){}
    public synchronized void pullTheRope(){}

    public synchronized void amIDone(){}
}
