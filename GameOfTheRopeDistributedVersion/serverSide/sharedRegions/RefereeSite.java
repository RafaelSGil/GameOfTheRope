package serverSide.sharedRegions;


import clientSide.entities.RefereeStates;
import clientSide.stubs.GeneralRepositoryStub;
import serverSide.entities.RefereeSiteProxy;
import serverSide.main.ServerGameOfTheRopeRefereeSite;
import serverSide.main.SimulationParams;

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
    private final GeneralRepositoryStub repository;

    /**
     * Flag that indicates the end of the match
     */
    private boolean matchEnd;

    /**
     *   Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * Creates a new RefereeSite instance.
     *
     * @param repository The reference to the GeneralRepository object.
     */
    public RefereeSite(GeneralRepositoryStub repository){
        this.repository = repository;
        this.matchEnd = false;
    }



    /**
     * Announces the start of a new game to all entities.
     * Updates the game number, trial number, and referee state in the repository.
     */
    public synchronized void announceNewGame(){
        ((RefereeSiteProxy) Thread.currentThread()).setGame(((RefereeSiteProxy) Thread.currentThread()).getGame() + 1);
        ((RefereeSiteProxy) Thread.currentThread()).setTrial(0);
        repository.setTrial(0);
        repository.setGame(((RefereeSiteProxy) Thread.currentThread()).getGame());
        repository.updateReferee(((RefereeSiteProxy) Thread.currentThread()).getRefereeSate());
        ((RefereeSiteProxy) Thread.currentThread()).setRefereeSate(RefereeStates.STARTGAME);
        repository.updateReferee(((RefereeSiteProxy) Thread.currentThread()).getRefereeSate());

        repository.reportGameStart();
        repository.reportStatus(true);
    }

    /**
     * Declares the winner of a completed game based on the trial results.
     * Updates the referee state.
     */
    public synchronized void declareGameWinner(){
        ((RefereeSiteProxy) Thread.currentThread()).setRefereeSate(RefereeStates.ENDGAME);
        repository.updateReferee(((RefereeSiteProxy) Thread.currentThread()).getRefereeSate());

        switch (((RefereeSiteProxy) Thread.currentThread()).getGameResult(((RefereeSiteProxy) Thread.currentThread()).getGame() - 1)){
            case -1:
                repository.declareGameWinner(0, ((RefereeSiteProxy) Thread.currentThread()).getWinCause());
                break;
            case 1:
                repository.declareGameWinner(1, ((RefereeSiteProxy) Thread.currentThread()).getWinCause());
                break;
            case 0:
                repository.declareGameWinner(2, ((RefereeSiteProxy) Thread.currentThread()).getWinCause());
                break;
        }

        repository.reportStatus(false);
        repository.reportGameStatus();
        repository.setRopePosition(0);
    }

    /**
     * Declares the winner of the entire match based on the final results.
     * Updates the referee state and inform that the match is ended.
     */
    public synchronized void declareMatchWinner(){
        this.matchEnd = true;
        ((RefereeSiteProxy) Thread.currentThread()).setRefereeSate(RefereeStates.ENDMATCH);
        repository.updateReferee(((RefereeSiteProxy) Thread.currentThread()).getRefereeSate());

        repository.reportStatus(false);

        repository.declareMatchWinner(((RefereeSiteProxy) Thread.currentThread()).finalResults());
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

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     */
    public synchronized void endOperation ()
    {
        Thread.currentThread().interrupt();
    }

    /**
     *Operation shut down
     */
    public synchronized void shutdown ()
    {
        nEntities += 1;
        if(nEntities >= SimulationParams.NENTITIES){
            ServerGameOfTheRopeRefereeSite.waitConnection = false;
        }
        notifyAll ();                                        // the barber may now terminate
    }

}
