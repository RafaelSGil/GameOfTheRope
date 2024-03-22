package sharedregions;

import entities.Referee;
import entities.RefereeStates;

public class RefereeSite {
    private GeneralRepository repository;
    private boolean matchEnd;

    public RefereeSite(GeneralRepository repository){
        this.repository = repository;
        this.matchEnd = false;
    }

    public synchronized void unblockRefereeSite(){
        notifyAll();
    }

    public synchronized void announceNewGame(){
        ((Referee) Thread.currentThread()).setGame(((Referee) Thread.currentThread()).getGame() + 1);
        ((Referee) Thread.currentThread()).setTrial(0);
        repository.setGame(((Referee) Thread.currentThread()).getGame());
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.STARTGAME);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());
    }

    public synchronized void declareGameWinner(){
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.ENDGAME);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());

        // implement game winner logic
        repository.declareGameWinner();
    }

    public synchronized void declareMatchWinner(){
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.ENDMATCH);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());

        // implement match end logic
        repository.declareMatchWinner();

        this.matchEnd = true;
    }

    public synchronized boolean endOfMatch(){
        return matchEnd;
    }




}
