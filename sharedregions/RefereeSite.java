package sharedregions;

import entities.Referee;
import entities.RefereeStates;
import genclass.GenericIO;

public class RefereeSite {
    private GeneralRepository repository;
    private boolean matchEnd;

    private int team0TrialWins;

    private int team1TrialWins;

    private int team1GameWins;

    private int team0GameWins;

    private String gameWinCause;

    public RefereeSite(GeneralRepository repository){
        this.repository = repository;
        this.matchEnd = false;
        this.team0TrialWins = 0;
        this.team1GameWins = 0;
        this.team1TrialWins = 0;
        this.team0GameWins = 0;
    }

    public synchronized void unblockRefereeSite(){
        notifyAll();
    }

    public synchronized void updateTrialWins(int team){
        if(team == 0){
            this.team0TrialWins++;
        }else{
            this.team1TrialWins++;
        }
    }
    public synchronized void updateGameWins(int team){
        if(team == 0){
            this.team0GameWins++;
        }else{
            this.team1GameWins++;
        }
    }

    public synchronized void setGameWinCause(String cause){
        this.gameWinCause = cause;

    }
    public synchronized void announceNewGame(){
        ((Referee) Thread.currentThread()).setGame(((Referee) Thread.currentThread()).getGame() + 1);
        ((Referee) Thread.currentThread()).setTrial(0);
        repository.setTrial(0);
        repository.setGame(((Referee) Thread.currentThread()).getGame());
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.STARTGAME);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());
        GenericIO.writelnString("GAME " + ((Referee) Thread.currentThread()).getGame());
    }

    public synchronized void declareGameWinner(){
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.ENDGAME);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());

        if(team0TrialWins > team1TrialWins){
            updateGameWins(0);
            repository.declareGameWinner(0, gameWinCause);
        }else if (team1TrialWins > team0TrialWins){
            updateGameWins(1);
            repository.declareGameWinner(1, gameWinCause);
        }
        else{
            repository.declareGameWinner(2, gameWinCause);
        }
        this.team0TrialWins = 0;
        this.team1TrialWins = 0;

    }

    public synchronized void declareMatchWinner(){
        this.matchEnd = true;
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.ENDMATCH);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());

        repository.declareMatchWinner(((Referee) Thread.currentThread()).finalResults());

        // TODO DELETE THIS
        GenericIO.writelnString("MATCH RESULTS");
        ((Referee) Thread.currentThread()).printMatchResults();
    }

    public synchronized boolean endOfMatch(){
        return matchEnd;
    }

    public synchronized void setMatchEnd(boolean matchEnd){
        this.matchEnd = matchEnd;
    }


}
