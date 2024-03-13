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

    public synchronized void announceNewGame(){
        repository.setGame(repository.getGame() + 1);
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.STARTGAME);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());
    }

    public synchronized void declareGameWinner(){
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.ENDGAME);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());

        // TODO implement game winner logic
    }

    public synchronized void declareMatchWinner(){
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.ENDMATCH);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());

        // TODO implement match end logic

        this.matchEnd = true;
    }

    public synchronized boolean endOfMatch(){
        return matchEnd;
    }

    public synchronized void informReferee(){}
}
