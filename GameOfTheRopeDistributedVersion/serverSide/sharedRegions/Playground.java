package serverSide.sharedRegions;


import clientSide.entities.CoachStates;
import clientSide.entities.ContestantStates;
import clientSide.entities.RefereeStates;
import serverSide.entities.PlaygroundProxy;
import serverSide.main.SimulationParams;

/**
 * This class represents the Playground entity in the game of the rope simulation.
 * Its purpose is to serve has the playground for the contestants to play the game of the rope
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class Playground {
    /**
     * Array of instances of the {@link PlaygroundProxy} objects
     */
    private final PlaygroundProxy[] contestants;
    /**
     * Array of instances of the {@link PlaygroundProxy} objects
     */
    private final PlaygroundProxy[] coaches;

    /**
     * Instance of the {@link GeneralRepository} object
     */
    private final GeneralRepository repository;
    /**
     * Instance of the {@link PlaygroundProxy} object
     */
    private PlaygroundProxy referee;

    /**
     * Store the amount of times the rope has been pulled in the current trial
     */
    private int ropesPulled;

    /**
     * Flag that signals if the trial has started
     */
    private boolean trialStarted;
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
     * Creates a new Playground instance with a reference to the repository
     *
     * @param repository The {@link GeneralRepository} object representing the repository.
     */
    public Playground(GeneralRepository repository) {
        this.repository = repository;
        this.contestants = new PlaygroundProxy[SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = null;
        }
        this.coaches = new PlaygroundProxy[SimulationParams.NTEAMS];
        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            coaches[i] = null;
        }
        this.referee = null;
        this.ropesPulled = 0;
        this.trialStarted = false;
        this.team0Power = 0;
        this.team1Power = 0;
        this.ropePosition = 0;
    }

    /**
     * Initiates the trial, updating referee state, trial count, and notifying coaches.
     */
    public synchronized void callTrial() {
        this.referee = ((PlaygroundProxy) Thread.currentThread());
        referee.setRefereeSate(RefereeStates.TEAMSREADY);
        repository.updateReferee(((PlaygroundProxy) Thread.currentThread()).getRefereeSate());
        referee.setTrial(referee.getTrial() + 1);
        repository.setTrial(referee.getTrial());
    }

    /**
     * check if the coach have already chosen their team
     *
     * @return true if both coaches  have chosen teams, false otherwise
     */
    private boolean haveCoachesChosenTeams() {
        try {
            for (PlaygroundProxy c : coaches) {
                if (c.getCoachState() != CoachStates.WATCHTRIAL) {
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
    public synchronized void startTrial() {
        trialStarted = false;

        // synchronize, will get waken up by the last coach
        while (!haveCoachesChosenTeams()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ((PlaygroundProxy) Thread.currentThread()).setRefereeSate(RefereeStates.WAITTRIALCONCLUSION);
        repository.updateReferee(((PlaygroundProxy) Thread.currentThread()).getRefereeSate());
        repository.setRopePosition(ropePosition);

        trialStarted = true;
        // wake up contestants
        notifyAll();
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
    public synchronized boolean assertTrialDecision() {
        this.referee = ((PlaygroundProxy) Thread.currentThread());

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

        repository.setRopePosition(ropePosition);

        // reset counters
        this.ropesPulled = 0;
        this.team0Power = 0;
        this.team1Power = 0;

        if (referee.getTrial() == SimulationParams.NTRIALS || ropePosition > 3 || ropePosition < -3) {
            if (ropePosition > 0) {
                referee.setGameResult(1);
                referee.setWinCause(ropePosition > 3 ? "knockout" : "points");


            } else if (ropePosition < 0) {
                referee.setGameResult(-1);
                referee.setWinCause(ropePosition < -3 ? "knockout" : "points");


            } else {
                referee.setGameResult(0);
                referee.setWinCause("draw");
            }

            ropePosition = 0;
            repository.setRopePosition(ropePosition);

            if (referee.getGame() == SimulationParams.GAMES) {
                referee.signalMatchEnded();
            }

            return true;
        }

        return false;
    }

    /**
     * checks if there are 3 contestants of the team in state STANDINPOSITION
     *
     * @param team to which team the contestants belong
     * @return true if the contestants are ready, false otherwise
     */
    private boolean checkIfTeamIsReady(int team) {
        int numReadyContestants = 0;

        for (PlaygroundProxy contestant : contestants) {
            try {
                if (contestant.getContestantState() == ContestantStates.STANDINPOSITION && contestant.getContestantTeam() == team) {
                    numReadyContestants++;
                }
            } catch (Exception e) {}
        }

        return numReadyContestants == SimulationParams.NPLAYERSINCOMPETITION;
    }

    /**
     * Coach waits for every contestant of its team to be ready and
     * informs the referee that the team is ready.
     */
    public synchronized void informReferee() {
        int coachId = ((PlaygroundProxy) Thread.currentThread()).getCoachTeam();
        coaches[coachId] = ((PlaygroundProxy) Thread.currentThread());

        // waits for the team to be ready
        while (!checkIfTeamIsReady(coaches[coachId].getCoachTeam())) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        coaches[coachId].setCoachState(CoachStates.WATCHTRIAL);
        repository.updateCoach(coaches[coachId].getCoachState(), coachId);

        // alerts the referee that its team is ready
        notifyAll();
    }

    /**
     * Contestant signals it's ready for the trial,
     * calculates team power, and waits for the referee to signal the trial start.
     */
    public synchronized void getReady() {
        int contestantId = ((PlaygroundProxy) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((PlaygroundProxy) Thread.currentThread());
        int team = contestants[contestantId].getContestantTeam();
        if (team == 0) {
            team0Power += contestants[contestantId].getContestantStrength();
        } else {
            team1Power += contestants[contestantId].getContestantStrength();
        }

        // wake up the coach
        notifyAll();

        // wait for referee sign
        while (!trialStarted) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        contestants[contestantId].setContestantState(ContestantStates.DOYOURBEST);
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());
    }

    /**
     * Contestant signals that it's done pulling the rope
     */
    public synchronized void amIDone() {
        int contestantId = ((PlaygroundProxy) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((PlaygroundProxy) Thread.currentThread());

        ropesPulled++;

        // wake up the referee
        notifyAll();
    }
}
