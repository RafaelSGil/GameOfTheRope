package serverSide.objects;


import clientSide.entities.*;
import genclass.GenericIO;
import interfaces.*;
import serverSide.main.ServerGameOfTheRopePlayground;
import serverSide.main.SimulationParams;

import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * This class represents the Playground entity in the game of the rope simulation.
 * Its purpose is to serve has the playground for the contestants to play the game of the rope
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class Playground implements IPlayground {
    /**
     * Number of entity groups requesting the shutdown.
     */
    private int nEntities;
    /**
     * Array of instances of the {@link Contestant} objects
     */
    private final Thread[] contestants;

    /**
     * Array to store the states of the contestants
     */
    private final int[] contStates;

    /**
     * Array to store the strength of the contestants
     */
    private final int[] contStrength;

    /**
     * Array to store the team of the contestants
     */
    private final int[] contTeam;

    /**
     * Array of instances of the {@link Coach} objects
     */
    private final Thread[] coaches;

    /**
     * Array to store the states of the coaches
     */
    private final int[] coaStates;

    /**
     * Instance of the {@link GeneralRepository} object
     */
    private final IGeneralRepository repository;
    /**
     * Instance of the {@link Referee} object
     */
    private Thread referee;

    /**
     * Variable to store the states of the referee
     */
    private int refState;

    /**
     * Store the amount of times the rope has been pulled in the current trial
     */
    private int ropesPulled;

    /**
     * Flag that signals if the trial has started
     */
    private final boolean[] trialStarted;
    /**
     * Accumulated power of the team 1
     */
    private int team0Power;
    /**
     * Accumulated power of the team 2
     */
    private int team1Power;

    /**
     * Current position of the rope
     */
    private int ropePosition;

    /**
     * stores counter of contestant that signal they are ready for both teams
     */
    private final int[] readyCounters;

    /**
     * Creates a new Playground instance with a reference to the repository
     *
     * @param repository The {@link GeneralRepository} object representing the repository.
     */
    public Playground(IGeneralRepository repository) {
        this.repository = repository;
        this.contestants = new Thread[SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = null;
        }
        this.contStrength = new int[SimulationParams.NCONTESTANTS];
        this.contStates = new int[SimulationParams.NCONTESTANTS];
        this.contTeam = new int[SimulationParams.NCONTESTANTS];

        this.coaches = new Thread[SimulationParams.NTEAMS];
        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            coaches[i] = null;
        }
        this.coaStates = new int[SimulationParams.NTEAMS];
        this.referee = null;
        this.refState = 0;
        this.ropesPulled = 0;
        this.trialStarted = new boolean[SimulationParams.NCONTESTANTS];
        this.team0Power = 0;
        this.team1Power = 0;
        this.ropePosition = 0;
        this.nEntities = 0;
        this.readyCounters = new int[SimulationParams.NTEAMS];
        Arrays.fill(readyCounters, 0);
    }

    /**
     * Initiates the trial, updating referee state, trial count, and notifying coaches.
     */
    @Override
    public synchronized ReturnReferee callTrial(int trial) {
        this.referee = Thread.currentThread();
        refState = RefereeStates.TEAMSREADY;
        try {
            repository.updateReferee(refState);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on callTrial - updateReferee 2: " + e.getMessage ());
            System.exit (1);
        }

        try {
            repository.setTrial(trial + 1);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on callTrial - setTrial: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.reportStatus(false);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on callTrial - reportStatus: " + e.getMessage ());
            System.exit (1);
        }

        Arrays.fill(trialStarted, false);

        return new ReturnReferee(refState, trial);
    }

    /**
     * check if the coach have already chosen their team
     *
     * @return true if both coaches  have chosen teams, false otherwise
     */
    private boolean haveCoachesChosenTeams() {
        try {
            for (int c : coaStates) {
                if (c != CoachStates.WATCHTRIAL) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Referee wait for the last coach to signal it's ready to
     * Wake up the contestants
     */
    @Override
    public synchronized ReturnReferee startTrial() {
        this.referee = Thread.currentThread();

        // synchronize, will get waken up by the last coach
        while (!haveCoachesChosenTeams()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        refState = RefereeStates.WAITTRIALCONCLUSION;

        try {
            repository.updateReferee(refState);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on startTrial - updateReferee 3: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.setRopePosition(ropePosition);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on startTrial - setRopePosition: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.reportStatus(false);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on startTrial - reportStatus: " + e.getMessage ());
            System.exit (1);
        }

        Arrays.fill(trialStarted, true);
        // wake up contestants
        notifyAll();

        return  new ReturnReferee(refState);
    }

    /**
     * Check if ever playing contestant has pulled the rope
     *
     * @return false if every contestant has pulled the rope, false otherwise
     */
    private boolean haveContestantsPulledRope() {
        return ropesPulled == SimulationParams.NPLAYERSINCOMPETITION * 2;
    }

    /**
     * Assert trial results and updates the game status accordingly.
     *
     * @return True if this trial has concluded the game, false otherwise.
     */
    @Override
    public synchronized ReturnReferee assertTrialDecision(int trial, int game) {
        this.referee = Thread.currentThread();

        // synchronize, will get waken up by the last contestant
        while (!haveContestantsPulledRope()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        int ropeMove = Math.abs(team0Power - team1Power);
        if (team1Power > team0Power) {                      // team 1 wins trial
            ropePosition += ropeMove;
        } else {           //team 0 wins trial
            ropePosition -= ropeMove;
        }

        try {
            repository.setRopePosition(ropePosition);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on assertTrialDecision - setRopePosition: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.reportStatus(false);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Referee remote exception on assertTrialDecision - reportStatus: " + e.getMessage ());
            System.exit (1);
        }

        // reset counters
        this.ropesPulled = 0;
        this.team0Power = 0;
        this.team1Power = 0;

        if (trial == SimulationParams.NTRIALS || ropePosition > 3 || ropePosition < -3) {
            int gameResult = 0;
            String winCause = null;
            if (ropePosition > 0) {
                gameResult = 1;
                winCause = ropePosition > 3 ? "knockout" : "points";
            } else if (ropePosition < 0) {
                gameResult = -1;
                winCause = ropePosition < -3 ? "knockout" : "points";
            } else {
                winCause = "draw";
            }

            ropePosition = 0;

            return new ReturnReferee(refState, gameResult, winCause, true, game == SimulationParams.GAMES);
        }

        return new ReturnReferee(refState, false);
    }

    /**
     * checks if there are 3 contestants of the team in state STANDINPOSITION
     *
     * @param team to which team the contestants belong
     * @return true if the contestants are ready, false otherwise
     */
    private boolean checkIfTeamIsReady(int team) {
//        int numReadyContestants = 0;
//
//        for (PlaygroundProxy contestant : contestants) {
//            try {
//                if (contestant.getContestantState() == ContestantStates.STANDINPOSITION && contestant.getContestantTeam() == team) {
//                    numReadyContestants++;
//                }
//            } catch (Exception e) {}
//        }

        return readyCounters[team] == SimulationParams.NPLAYERSINCOMPETITION;
    }

    /**
     * Coach waits for every contestant of its team to be ready and
     * informs the referee that the team is ready.
     */
    @Override
    public synchronized ReturnCoach informReferee(int team) {
        coaches[team] = Thread.currentThread();

        // waits for the team to be ready
        while (!checkIfTeamIsReady(team)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        readyCounters[team] = 0;
        coaStates[team] = CoachStates.WATCHTRIAL;

        try {
            repository.updateCoach(coaStates[team], team);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Coach" + team + " remote exception on informReferee - updateCoach 2: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.reportStatus(false);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Coach" + team + " remote exception on informReferee - reportStatus: " + e.getMessage ());
            System.exit (1);
        }

        // alerts the referee that its team is ready
        notifyAll();

        return new ReturnCoach(coaStates[team]);
    }

    /**
     * Contestant signals it's ready for the trial,
     * calculates team power, and waits for the referee to signal the trial start.
     */
    @Override
    public synchronized ReturnContestant getReady(int contId, int contTeam, int contStrength) {
        contestants[contId] = Thread.currentThread();
        this.contTeam[contId] = contTeam;
        this.contStrength[contId] = contStrength;
        readyCounters[this.contTeam[contId]] += 1;

        if (this.contTeam[contId] == 0) {
            team0Power += this.contStrength[contId];
        } else {
            team1Power += this.contStrength[contId];
        }

        // wake up the coach
        notifyAll();

        // wait for referee sign
        while (!trialStarted[contId]) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        trialStarted[contId] = false;

        this.contStates[contId] = ContestantStates.DOYOURBEST;

        try {
            repository.updateContestant(contId, this.contStrength[contId],
                    this.contStates[contId],
                    contTeam);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Contestant " + contId + " remote exception on getReady - updateContestant 2: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.reportStatus(false);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Contestant " + contId + " remote exception on getReady - reportStatus: " + e.getMessage ());
            System.exit (1);
        }

        return new ReturnContestant(this.contStates[contId]);
    }

    /**
     * Contestant signals that it's done pulling the rope
     */
    @Override
    public synchronized void amIDone() {
        ropesPulled++;

        // wake up the referee
        notifyAll();
    }

    /**
     * Operation server shutdown.
     * <p>
     * New operation.
     */
    @Override
    public synchronized void endOperation(String entity, int id) {
        while (nEntities == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        switch (entity) {
            case SimulationParams.REFEREE:
                Thread.currentThread().interrupt();
                break;
            case SimulationParams.COACH:
                coaches[id].interrupt();
                break;
            case SimulationParams.CONTESTANT:
                contestants[id].interrupt();
                break;
        }
    }

    /**
     * Operation shut down
     */
    @Override
    public synchronized void shutdown() {
        nEntities += 1;
        if (nEntities >= SimulationParams.NENTITIES) {
            ServerGameOfTheRopePlayground.shutdown();
        }
        notifyAll();                                        // the barber may now terminate
    }
}
