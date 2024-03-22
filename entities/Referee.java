package entities;
import genclass.GenericIO;
import main.SimulationParams;
import sharedregions.ContestantsBench;
import sharedregions.Playground;
import sharedregions.RefereeSite;

public class Referee extends Thread {
    private int refereeSate;
    private final ContestantsBench bench;
    private final RefereeSite refereeSite;
    private final Playground playground;
    private int game;
    private int trial;

    public Referee(String threadName, RefereeSite refereeSite, Playground playground, ContestantsBench bench){
        super(threadName);
        this.playground = playground;
        this.refereeSite = refereeSite;
        this.bench = bench;
        this.refereeSate = RefereeStates.STARTMATCH;
        this.game = 0;
        this.trial = 0;
    }

    public int getRefereeSate() {
        return refereeSate;
    }

    public void setRefereeSate(int refereeSate) {
        this.refereeSate = refereeSate;
    }

    public int getGame() {
        return game;
    }

    public void setGame(int game) {
        this.game = game;
    }

    public int getTrial() {
        return trial;
    }

    public void setTrial(int trial) {
        this.trial = trial;
    }

    @Override
    public void run() {
        waitForGameStart();
        for(int i = 0; i < SimulationParams.GAMES; ++i){
            refereeSite.announceNewGame();
            do {
                playground.callTrial(bench);
                playground.startTrial();
            }while(!playground.assertTrialDecision(bench));
            refereeSite.declareGameWinner();
        }
        refereeSite.declareMatchWinner();
    }

    private void waitForGameStart(){
        try
        { sleep ((long) (1 + 50 * Math.random ()));
        }
        catch (InterruptedException e) {}
    }
}