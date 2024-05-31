package serverSide.objects;

import clientSide.entities.CoachStates;
import clientSide.entities.Contestant;
import clientSide.entities.ContestantStates;
import genclass.GenericIO;
import interfaces.IContestantsBench;
import interfaces.IGeneralRepository;
import interfaces.ReturnCoach;
import interfaces.ReturnContestant;
import serverSide.main.ServerGameOfTheRopeContestantsBench;
import serverSide.main.SimulationParams;
import serverSide.utils.Strategy;

import java.rmi.RemoteException;
import java.util.*;

/**
 * This class represents a ContestantsBench entity in the game of the rope simulation.
 * Its purpose is to serve has a waiting room for the contestant,
 * where they wait to receive instructions, from the coach,
 * about whether they are going to play or not
 *
 * @author [Miguel Cabral]
 * @author [Rafael Gil]
 */
public class ContestantsBench implements IContestantsBench {
    /**
     * Array to store all Contestants
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
     * Array to store all coaches
     */
    private final Thread[] coaches;

    /**
     * Array to store the states of the coaches
     */
    private final int[] coaStates;

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
    private final IGeneralRepository repository;

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
     * @param repository The {@link IGeneralRepository} object representing the repository.
     */
    public ContestantsBench(IGeneralRepository repository) {
        this.playing = new ArrayList<>();
        this.benched = new ArrayList<>();
        this.repository = repository;
        this.hasTrialEndedCoaches = new boolean[SimulationParams.NTEAMS];
        Arrays.fill(hasTrialEndedCoaches, false);
        this.hasTrialEndedContestants = new boolean[SimulationParams.NCONTESTANTS];
        Arrays.fill(hasTrialEndedContestants, false);
        this.callTrial = new boolean[SimulationParams.NTEAMS];
        Arrays.fill(callTrial, false);
        this.contestants = new Thread[SimulationParams.NCONTESTANTS];
        this.contStates = new int[SimulationParams.NCONTESTANTS];
        this.contStrength = new int[SimulationParams.NCONTESTANTS];
        this.contTeam = new int[SimulationParams.NCONTESTANTS];
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            contestants[i] = null;
        }
        this.coaches = new Thread[SimulationParams.NTEAMS];
        this.coaStates = new int[SimulationParams.NTEAMS];
        for (int i = 0; i < SimulationParams.NTEAMS; i++) {
            coaches[i] = null;
        }
    }

    /**
     * Signal the coaches and contestants to wake up
     */
    @Override
    public synchronized void unblockContestantBench() {
        notifyAll();
    }

    /**
     * start of new trial iteration
     * wake up the coaches
     */
    @Override
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
    @Override
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
        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            try {
                if (this.contTeam[i] == team && this.contStates[i] == ContestantStates.SEATATBENCH) {
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
     * Wake up the contestants
     *
     * @param team to which team does the coach belong
     */
    @Override
    public synchronized ReturnCoach callContestants(int team, int strategy) {
        coaches[team] = Thread.currentThread();
        coaStates[team] = CoachStates.WATFORREFEREECOMMAND;

        // wait for the first notify of every iteration
        // that corresponds to the call trial of the referee
        while (!callTrial[team] || !isEveryoneSeated(team)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            repository.updateCoach(coaStates[team], team);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Coach " + team + " remote exception on callContestants - updateCoach 0: " + e.getMessage ());
            System.exit (1);
        }

        //choose the players
        ArrayList<Integer> auxIds = new ArrayList<>(SimulationParams.NPLAYERS);

        for (int i = 0; i < SimulationParams.NCONTESTANTS; i++) {
            if(contTeam[i] == team){
                auxIds.add(i);
            }
        }

        auxIds.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return Integer.compare(contStrength[b], contStrength[a]);
            }
        });

        // strength strat
        if(strategy == 0){
            for (int i = 0; i < SimulationParams.NPLAYERSINCOMPETITION; i++) {
                playing.add(auxIds.get(i));
            }
            for (int i = SimulationParams.NPLAYERSINCOMPETITION; i < SimulationParams.NPLAYERS; i++) {
                benched.add(auxIds.get(i));
            }
        }

        // moderate strat
        if(strategy == 1){
            for (int i = 0; i < SimulationParams.NPLAYERSINCOMPETITION - 1; i++) {
                playing.add(auxIds.get(i));
            }
            playing.add(auxIds.get(auxIds.size() - 1));
            for (int i = SimulationParams.NPLAYERSINCOMPETITION - 1; i < SimulationParams.NPLAYERS - 1; i++) {
                benched.add(auxIds.get(i));
            }
        }


        coaStates[team] = CoachStates.ASSEMBLETEAM;
        try {
            repository.updateCoach(coaStates[team], team);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Coach " + team + " remote exception on callContestants - updateCoach 1: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.reportStatus(false);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Coach " + team + " remote exception on callContestants - reportStatus: " + e.getMessage ());
            System.exit (1);
        }

        // wake up contestants
        notifyAll();

        return new ReturnCoach(coaStates[team]);
    }


    /**
     * Contestants will wait until the coach wakes them up,
     * checking whether they were chosen to play or not,
     * acting accordingly
     */
    @Override
    public synchronized ReturnContestant followCoachAdvice(int contId, int contTeam, int contStrength) {
        contestants[contId] = Thread.currentThread();
        this.contStrength[contId] = contStrength;
        this.contStates[contId] = ContestantStates.SEATATBENCH;
        this.contTeam[contId] = contTeam;

        try {
            repository.updateContestant(contId, this.contStrength[contId],
                    this.contStates[contId],
                    contTeam);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Contestant " + contId + " remote exception on followCoachAdvice - updateContestant 0: " + e.getMessage ());
            System.exit (1);
        }

        // wait for coach to choose team
        while (!playing.contains(contId) && !benched.contains(contId)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (benched.contains(contId)) {
            return new ReturnContestant(this.contStates[contId], this.contStrength[contId], false);
        }

        this.contStates[contId] = ContestantStates.STANDINPOSITION;
        try {
            repository.updateContestant(contId, this.contStrength[contId],
                    this.contStates[contId],
                    contTeam);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Contestant " + contId + " remote exception on followCoachAdvice - updateContestant 1: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.reportStatus(false);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Contestant " + contId + " remote exception on followCoachAdvice - reportStatus: " + e.getMessage ());
            System.exit (1);
        }

        return new ReturnContestant(this.contStates[contId], this.contStrength[contId], true);
    }

    /**
     * Contestants, which have played in the last trial, will wait until the referee signals the end of the trial
     */
    @Override
    public synchronized ReturnContestant seatDown(int contId, int contTeam, int contStrength, boolean isPlaying) {
        contestants[contId] = Thread.currentThread();
        this.contStrength[contId] = contStrength;
        this.contStates[contId] = ContestantStates.DOYOURBEST;
        this.contTeam[contId] = contTeam;

        while (!hasTrialEndedContestants[contId]) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.contStrength[contId] = Contestant.manageStrength(isPlaying, contStrength);
        playing.clear();
        benched.clear();
        hasTrialEndedContestants[contId] = false;

        this.contStates[contId] = ContestantStates.SEATATBENCH;

        try {
            repository.updateContestant(contId, this.contStrength[contId],
                    this.contStates[contId],
                    contTeam);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Contestant " + contId + " remote exception on seatDown - updateContestant 0: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.reportStatus(false);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Contestant " + contId + " remote exception on seatDown - reportStatus: " + e.getMessage ());
            System.exit (1);
        }

        return new ReturnContestant(this.contStates[contId], this.contStrength[contId], false);
    }

    /**
     * Coaches will wait until the referee signals the end of the trial
     */
    @Override
    public synchronized ReturnCoach reviewNotes(int team) {
        coaches[team] = Thread.currentThread();
        callTrial[team] = false;
        while (!hasTrialEndedCoaches[team]) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        hasTrialEndedCoaches[team] = false;
        coaStates[team] = CoachStates.WATFORREFEREECOMMAND;

        try {
            repository.updateCoach(coaStates[team], team);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Coach " + team + " remote exception on reviewNotes - updateCoach 0: " + e.getMessage ());
            System.exit (1);
        }
        try {
            repository.reportStatus(false);
        } catch (RemoteException e) {
            GenericIO.writelnString ("Coach " + team + " remote exception on reviewNotes - reportStatus: " + e.getMessage ());
            System.exit (1);
        }

        return new ReturnCoach(coaStates[team]);
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
            ServerGameOfTheRopeContestantsBench.shutdown();
        }
        notifyAll();
    }
}
