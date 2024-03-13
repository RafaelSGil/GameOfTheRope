package entities;
import main.SimulationParams;
import sharedregions.Playground;
import sharedregions.RefereeSite;

public class Referee extends Thread {
    private int refereeSate;
    private final RefereeSite refereeSite;
    private final Playground playground;

    public Referee(String threadName, RefereeSite refereeSite, Playground playground){
        super(threadName);
        this.playground = playground;
        this.refereeSite = refereeSite;
        this.refereeSate = RefereeStates.STARTMATCH;
    }

    public int getRefereeSate() {
        return refereeSate;
    }

    public void setRefereeSate(int refereeSate) {
        this.refereeSate = refereeSate;
    }

    @Override
    public void run() {
        for(int i = 0; i < SimulationParams.GAMES; ++i){
            refereeSite.announceNewGame();
            do {
                playground.callTrial();
                playground.startTrial();
            }while(playground.assertTrialDecision());
            refereeSite.declareGameWinner();
        }
        refereeSite.declareMatchWinner();
    }
}