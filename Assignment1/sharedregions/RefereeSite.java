package sharedregions;

import entities.Referee;
import entities.RefereeStates;

/**
 * This class represents the RefereeSite shared region in the Game of the Rope simulation.
 * It provides a communication point between the Referee entity and other entities (Contestants, Coach)
 * for match related information and updates.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class RefereeSite {

    /**
     * Reference to the GeneralRepository object.
     */
    private final GeneralRepository repository;

    /**
     * Flag that indicates the end of the match
     */
    private boolean matchEnd;

    /**
     * Creates a new RefereeSite instance.
     *
     * @param repository The reference to the GeneralRepository object.
     */
    public RefereeSite(GeneralRepository repository){
        this.repository = repository;
        this.matchEnd = false;
    }

    /**
     * Announces the start of a new game to all entities.
     * Updates the game number, trial number, and referee state in the repository.
     */
    public synchronized void announceNewGame(){
        ((Referee) Thread.currentThread()).setGame(((Referee) Thread.currentThread()).getGame() + 1);
        ((Referee) Thread.currentThread()).setTrial(0);
        repository.setTrial(0);
        repository.setGame(((Referee) Thread.currentThread()).getGame());
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.STARTGAME);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());
    }

    /**
     * Declares the winner of a completed game based on the trial results.
     * Updates the referee state.
     */
    public synchronized void declareGameWinner(){
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.ENDGAME);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());

        switch (((Referee) Thread.currentThread()).getGameResult(((Referee) Thread.currentThread()).getGame() - 1)){
            case -1:
                repository.declareGameWinner(0, ((Referee) Thread.currentThread()).getWinCause());
                break;
            case 1:
                repository.declareGameWinner(1, ((Referee) Thread.currentThread()).getWinCause());
                break;
            case 0:
                repository.declareGameWinner(2, ((Referee) Thread.currentThread()).getWinCause());
                break;
        }
    }

    /**
     * Declares the winner of the entire match based on the final results.
     * Updates the referee state and inform that the match is ended.
     */
    public synchronized void declareMatchWinner(){
        this.matchEnd = true;
        ((Referee) Thread.currentThread()).setRefereeSate(RefereeStates.ENDMATCH);
        repository.updateReferee(((Referee) Thread.currentThread()).getRefereeSate());

        repository.declareMatchWinner(((Referee) Thread.currentThread()).finalResults());
    }

    /**
     * Check if the match is ended or not
     * @return True if the match has ended, false otherwise.
     */
    public synchronized boolean endOfMatch(){
        return matchEnd;
    }

    /**
     * Set the match end flag to true is the match is ended, false otherwise
     * @param matchEnd The new value for the match end flag.
     */
    public synchronized void setMatchEnd(boolean matchEnd){
        this.matchEnd = matchEnd;
    }


}
