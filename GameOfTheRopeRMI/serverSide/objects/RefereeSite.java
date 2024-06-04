package serverSide.objects;


import clientSide.entities.RefereeStates;
import genclass.GenericIO;
import interfaces.IGeneralRepository;
import interfaces.IRefereeSite;
import interfaces.ReturnReferee;
import serverSide.main.ServerGameOfTheRopeRefereeSite;
import serverSide.main.SimulationParams;

import java.rmi.RemoteException;

/**
 * This class represents the RefereeSite shared region in the Game of the Rope simulation.
 * It provides a communication point between the Referee entity and other entities (Contestants, Coach)
 * for match related information and updates.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class RefereeSite implements IRefereeSite {

    /**
     * Reference to the GeneralRepository object.
     */
    private final IGeneralRepository repository;

    /**
     * Current game
     */
    private int game;

    /**
     * Current trial
     */
    private int trial;

    /**
     * State of the referee
     */
    private int refState;

    /**
     * Flag that indicates the end of the match
     */
    private boolean matchEnd;

    /**
     * Number of entity groups requesting the shutdown.
     */
    private int nEntities;

    /**
     * Creates a new RefereeSite instance.
     *
     * @param repository The reference to the GeneralRepository object.
     */
    public RefereeSite(IGeneralRepository repository) {
        this.repository = repository;
        this.matchEnd = false;
    }


    /**
     * Announces the start of a new game to all entities.
     * Updates the game number, trial number, and referee state in the repository.
     */
    @Override
    public synchronized ReturnReferee announceNewGame(int game, boolean startMatch) {
        try {
            repository.setTrial(0);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on announceNewGame - setTrial: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.setGame(game + 1);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on announceNewGame - setGame: " + e.getMessage ());
            System.exit (1);
        }
        if(startMatch){
            try {
                repository.updateReferee(RefereeStates.STARTMATCH);
            } catch (RemoteException e) {
                GenericIO.writelnString ("Referee remote exception on announceNewGame - updateReferee 0: " + e.getMessage ());
                System.exit (1);
            }
        }
        refState = RefereeStates.STARTGAME;
        try {
            repository.updateReferee(refState);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on announceNewGame - updateReferee 1: " + e.getMessage ());
            System.exit (1);
        }

        try {
            repository.reportGameStart();
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on announceNewGame - reportGameStart: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.reportStatus(true);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on announceNewGame - reportStatus: " + e.getMessage ());
            System.exit (1);
        }

        return new ReturnReferee(refState, game + 1, true);
    }

    /**
     * Declares the winner of a completed game based on the trial results.
     * Updates the referee state.
     */
    @Override
    public synchronized ReturnReferee declareGameWinner(int gameResult, String winCause) {
        refState = RefereeStates.ENDGAME;
        try {
            repository.updateReferee(refState);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on declareGameWinner - updateReferee 4: " + e.getMessage ());
            System.exit (1);
        }

        switch (gameResult) {
            case -1:
                try {
                    repository.declareGameWinner(0, winCause);
                } catch (RemoteException e) {
                    GenericIO.writelnString ("Referee remote exception on declareGameWinner - declareGameWinner -1: " + e.getMessage ());
                    System.exit (1);
                }
                break;
            case 1:
                try {
                    repository.declareGameWinner(1, winCause);
                } catch (RemoteException e) {
                    GenericIO.writelnString ("Referee remote exception on declareGameWinner - declareGameWinner 1: " + e.getMessage ());
                    System.exit (1);
                }
                break;
            case 0:
                try {
                    repository.declareGameWinner(2, winCause);
                } catch (RemoteException e) {
                    GenericIO.writelnString ("Referee remote exception on declareGameWinner - declareGameWinner 0: " + e.getMessage ());
                    System.exit (1);
                }
                break;
        }

        try {
            repository.reportStatus(false);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on declareGameWinner - reportStatus: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.reportGameStatus();
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on declareGameWinner - reportGameStatus: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.setRopePosition(0);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on declareGameWinner - setRopePosition: " + e.getMessage ());
            System.exit (1);
        }

        return new ReturnReferee(refState);
    }

    /**
     * Declares the winner of the entire match based on the final results.
     * Updates the referee state and inform that the match is ended.
     */
    @Override
    public synchronized ReturnReferee declareMatchWinner(String finalResults) {
        this.matchEnd = true;
        refState = RefereeStates.ENDMATCH;
        try {
            repository.updateReferee(refState);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on declareMatchWinner - updateReferee 5: " + e.getMessage ());
            System.exit (1);
        }

        try {
            repository.reportStatus(false);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on declareMatchWinner - reportStatus: " + e.getMessage ());
            System.exit (1);
        }

        try {
            repository.declareMatchWinner(finalResults);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on declareMatchWinner - declareMatchWinner: " + e.getMessage ());
            System.exit (1);
        }

        return new ReturnReferee(refState);
    }

    /**
     * Check if the match is ended or not
     *
     * @return True if the match has ended, false otherwise.
     */
    @Override
    public synchronized boolean endOfMatch() {

        return matchEnd;
    }

    /**
     * Set the match end flag to true is the match is ended, false otherwise
     *
     * @param matchEnd The new value for the match end flag.
     */
    @Override
    public synchronized void setMatchEnd(boolean matchEnd) {
        this.matchEnd = matchEnd;
    }

    /**
     * Operation server shutdown.
     * <p>
     * New operation.
     */
    @Override
    public synchronized void endOperation() {
        Thread.currentThread().interrupt();
    }

    /**
     * Operation shut down
     */
    @Override
    public synchronized void shutdown() {
        nEntities += 1;
        if (nEntities >= SimulationParams.NENTITIES) {
            ServerGameOfTheRopeRefereeSite.shutdown();
        }
        notifyAll();                                        // the barber may now terminate
    }

}
