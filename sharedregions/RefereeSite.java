package sharedregions;

public class RefereeSite {
    public RefereeSite(){

    }

    public synchronized void announceNewGame(){

    }

    public synchronized void declareTrialWinner(){

    }

    public synchronized void declareMatchWinner(){

    }

    public synchronized boolean endOfMatch(){
        return false;
    }

    public synchronized void informReferee(){}
}
