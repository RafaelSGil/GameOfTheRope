package sharedregions;

public class RefereeSite {
    GeneralRepository repository;
    public RefereeSite(GeneralRepository repository){
        this.repository = repository;
    }

    public synchronized void announceNewGame(){

    }

    public synchronized void declareTrialWinner(){

    }

    public synchronized void declareMatchWinner(){

    }

    public synchronized boolean endOfMatch(){
        return true;
    }

    public synchronized void informReferee(){}
}
