package serverSide.objects;

import clientSide.entities.CoachStates;
import clientSide.entities.ContestantStates;
import clientSide.stubs.GeneralRepositoryStub;
import serverSide.entities.ContestantBenchProxy;
import serverSide.main.ServerGameOfTheRopeContestantsBench;
import serverSide.main.SimulationParams;
import serverSide.utils.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a ContestantsBench entity in the game of the rope simulation.
 * Its purpose is to serve has a waiting room for the contestant,
 * where they wait to receive instructions, from the coach,
 * about whether they are going to play or not
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class ContestantsBench {
    /**
     * Array to store all Contestants
     */
    private final ContestantBenchProxy[] contestants;

    /**
     * Array to store all coaches
     */
    private final ContestantBenchProxy[] coaches;

    /**
     * List to store Playing players
     */
    private final List<Integer> playing;

    /**
     * List to store Benched players
     */
    private final List<Integer> benched;

    /**
     * General Repository
     */
    private final GeneralRepositoryStub repository;

    /**
     * Checks if trial has ended for coaches
     */
    private final boolean[] hasTrialEndedCoaches;

    /**
     * Checks if trial has ended for contestants
     */
    private final boolean[] hasTrialEndedContestants;

    /**
     * conditional flags for coaches
     */
    private final boolean[] callTrial;

    /**
     * Number of entity groups requesting the shutdown.
     */
    private int nEntities;


    /**
     * Creates a new ContestantsBench instance with a reference to the repository
     *
     * @param repository The {@link GeneralRepositoryStub} object representing the repository.
     */
    public ContestantsBench(GeneralRepositoryStub repository) {
        this.playing = new ArrayList<>();
        this.benched = new ArrayList<>();
        this.repository = repository;
        this.hasTrialEndedCoaches = new boolean[SimulationParams.NTEAMS];
        Arrays.fill(hasTrialEndedCoaches, false);
        this.hasTrialEndedContestants = new boolean[SimulationParams.NCONTESTANTS];
        Arrays.fill(hasTrialEndedContestants, false);
        this.callTrial = new boolean[SimulationParams.NTEAMS];
        Arrays.fill(callTrial, false);
        this.contestants = new ContestantBenchProxy[SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = null;
        }
        this.coaches = new ContestantBenchProxy[SimulationParams.NTEAMS];
        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            coaches[i] = null;
        }
    }

    /**
     * Signal the coaches and contestants to wake up
     */
    public synchronized void unblockContestantBench() {
        notifyAll();
    }

    /**
     * start of new trial iteration
     * wake up the coaches
     */
    public synchronized void refereeCallTrial() {
        Arrays.fill(callTrial, true);
        Arrays.fill(hasTrialEndedContestants, false);
        Arrays.fill(hasTrialEndedCoaches, false);
        notifyAll();
    }

    /**
     * Sets the value of attribute hasTrialEnded
     *
     * @param hasTrialEnded new value for the attribute
     */
    public synchronized void setHasTrialEnded(boolean hasTrialEnded) {
        Arrays.fill(hasTrialEndedCoaches, hasTrialEnded);
        Arrays.fill(hasTrialEndedContestants, hasTrialEnded);
        notifyAll();
    }

    /**
     * Coaches check if every contestant of its team is in a state to be picked up
     *
     * @param team team to which the contestants belong
     * @return true if every contestant of the team is ready to be picked
     */
    private boolean isEveryoneSeated(int team) {
        int contSeated = 0;
        for (ContestantBenchProxy c : contestants) {
            try {
                if (c.getContestantTeam() == team && c.getContestantState() == ContestantStates.SEATATBENCH) {
                    contSeated++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contSeated == SimulationParams.NPLAYERS;
    }

    /**
     * Coaches will wait until they receive a signal from the referee and until their team is not ready
     * Afterward, pick which contestants will be playing and which will be benched
     * Wake up the contestatns
     *
     * @param team to which team does the coach belong
     */
    public synchronized void callContestants(int team) {
        // wait for the first notify of every iteration
        // that corresponds to the call trial of the referee
        while (!callTrial[team] || !isEveryoneSeated(team)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int coachId = ((ContestantBenchProxy) Thread.currentThread()).getCoachTeam();
        coaches[coachId] = ((ContestantBenchProxy) Thread.currentThread());

        repository.updateCoach(coaches[coachId].getCoachState(), coaches[coachId].getCoachTeam());

        //choose the players
        Strategy.useStrategy(coaches[coachId].getStrategy(), coaches[coachId].getCoachTeam(), contestants, playing, benched);


        coaches[coachId].setCoachState(CoachStates.ASSEMBLETEAM);
        repository.updateCoach(coaches[coachId].getCoachState(), coaches[coachId].getCoachTeam());
        repository.reportStatus(false);

        // wake up contestants
        notifyAll();
    }


    /**
     * Contestants will wait until the coach wakes them up,
     * checking whether they were chosen to play or not,
     * acting accordingly
     */
    public synchronized void followCoachAdvice() {
        int contestantId = ((ContestantBenchProxy) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((ContestantBenchProxy) Thread.currentThread());
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());

        // wait for coach to choose team
        while (!playing.contains(contestantId) && !benched.contains(contestantId)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (benched.contains(contestantId)) {
            return;
        }

        contestants[contestantId].setPlaying(true);
        contestants[contestantId].setContestantState(ContestantStates.STANDINPOSITION);
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());
        repository.reportStatus(false);
    }

    /**
     * Contestants, which have played in the last trial, will wait until the referee signals the end of the trial
     */
    public synchronized void seatDown() {
        int contestantId = ((ContestantBenchProxy) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((ContestantBenchProxy) Thread.currentThread());
        while (!hasTrialEndedContestants[contestantId]) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        contestants[contestantId].manageStrength();
        playing.clear();
        benched.clear();
        contestants[contestantId].setPlaying(false);
        hasTrialEndedContestants[contestantId] = false;

        contestants[contestantId].setContestantState(ContestantStates.SEATATBENCH);
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());
        repository.reportStatus(false);
    }

    /**
     * Coaches will wait until the referee signals the end of the trial
     */
    public synchronized void reviewNotes() {
        int coachId = ((ContestantBenchProxy) Thread.currentThread()).getCoachTeam();
        coaches[coachId] = ((ContestantBenchProxy) Thread.currentThread());
        callTrial[coachId] = false;
        while (!hasTrialEndedCoaches[coachId]) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        hasTrialEndedCoaches[coachId] = false;
        coaches[coachId].setCoachState(CoachStates.WATFORREFEREECOMMAND);
        repository.updateCoach(coaches[coachId].getCoachState(), coaches[coachId].getCoachTeam());
        repository.reportStatus(false);
    }

    /**
     * Operation server shutdown.
     * <p>
     * New operation.
     */
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
    public synchronized void shutdown() {
        nEntities += 1;
        if (nEntities >= SimulationParams.NENTITIES) {
            ServerGameOfTheRopeContestantsBench.waitConnection = false;
        }
        notifyAll();
    }
}
