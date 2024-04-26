package serverSide.sharedRegions;




import clientSide.entities.CoachStates;
import clientSide.entities.ContestantStates;
import serverSide.entities.ContestantBenchProxy;
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
    private final GeneralRepository repository;

    /**
     * Checks if trial has ended
     */
    private boolean hasTrialEnded;

    /**
     * conditional flags for coaches
     */
    private final boolean[] callTrial;


    /**
     * Creates a new ContestantsBench instance with a reference to the repository
     *
     * @param repository The {@link GeneralRepository} object representing the repository.
     */
    public ContestantsBench(GeneralRepository repository) {
        this.playing = new ArrayList<>();
        this.benched = new ArrayList<>();
        this.repository = repository;
        this.hasTrialEnded = false;
        this.callTrial = new boolean[SimulationParams.NTEAMS];
        this.contestants = new ContestantBenchProxy[SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = null;
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
        this.hasTrialEnded = false;
        unblockContestantBench();
    }

    /**
     * Sets the value of attribute hasTrialEnded
     *
     * @param hasTrialEnded new value for the attribute
     */
    public synchronized void setHasTrialEnded(boolean hasTrialEnded) {
        this.hasTrialEnded = hasTrialEnded;
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

        repository.updateCoach(((ContestantBenchProxy) Thread.currentThread()).getCoachState(), ((ContestantBenchProxy) Thread.currentThread()).getCoachTeam());

        //choose the players
        Strategy.useStrategy(((ContestantBenchProxy) Thread.currentThread()).getStrategy(), ((ContestantBenchProxy) Thread.currentThread()).getCoachTeam(), contestants, playing, benched);


        ((ContestantBenchProxy) Thread.currentThread()).setCoachState(CoachStates.ASSEMBLETEAM);
        repository.updateCoach(((ContestantBenchProxy) Thread.currentThread()).getCoachState(), ((ContestantBenchProxy) Thread.currentThread()).getCoachTeam());

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
    }

    /**
     * Contestants, which have played in the last trial, will wait until the referee signals the end of the trial
     */
    public synchronized void seatDown() {
        int contestantId = ((ContestantBenchProxy) Thread.currentThread()).getContestantId();
        contestants[contestantId] = ((ContestantBenchProxy) Thread.currentThread());
        while (!hasTrialEnded) {
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

        contestants[contestantId].setContestantState(ContestantStates.SEATATBENCH);
        repository.updateContestant(contestantId, contestants[contestantId].getContestantStrength(),
                contestants[contestantId].getContestantState(),
                contestants[contestantId].getContestantTeam());
    }

    /**
     * Coaches will wait until the referee signals the end of the trial
     */
    public synchronized void reviewNotes() {
        callTrial[((ContestantBenchProxy) Thread.currentThread()).getCoachTeam()] = false;
        while (!hasTrialEnded) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        ((ContestantBenchProxy) Thread.currentThread()).setCoachState(CoachStates.WATFORREFEREECOMMAND);
        repository.updateCoach(((ContestantBenchProxy) Thread.currentThread()).getCoachState(), ((ContestantBenchProxy) Thread.currentThread()).getCoachTeam());
    }
}
