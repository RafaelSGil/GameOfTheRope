package entities;
import genclass.GenericIO;
import main.SimulationParams;
import sharedregions.ContestantsBench;
import sharedregions.Playground;
import sharedregions.RefereeSite;

import java.util.Arrays;

public class Referee extends Thread {
    private int refereeSate;
    private final ContestantsBench bench;
    private final RefereeSite refereeSite;
    private final Playground playground;
    private int game;
    private int trial;

    /**
     * stores the results for each trial of each game
     * 3 rows --> 3 games
     * 6 columns --> 6 trials
     *
     * -1 --> team 1 won
     * 0 --> draw
     * 1 --> team 2 won
     */
    private int[] matchRecords;

    public Referee(String threadName, RefereeSite refereeSite, Playground playground, ContestantsBench bench){
        super(threadName);
        this.playground = playground;
        this.refereeSite = refereeSite;
        this.bench = bench;
        this.refereeSate = RefereeStates.STARTMATCH;
        this.game = 0;
        this.trial = 0;
        this.matchRecords = new int[SimulationParams.GAMES];
    }

    public synchronized int getRefereeSate() {
        return refereeSate;
    }

    public synchronized void setRefereeSate(int refereeSate) {
        this.refereeSate = refereeSate;
    }

    public synchronized int getGame() {
        return game;
    }

    public synchronized void setGame(int game) {
        this.game = game;
    }

    public synchronized int getTrial() {
        return trial;
    }

    public synchronized void setTrial(int trial) {
        this.trial = trial;
    }

    public synchronized void setTrialResult(int result){
        this.matchRecords[game-1] = result;
    }

    public synchronized int getGameResult(int game){
        return matchRecords[game];
    }

    public synchronized String finalResults(){
        int team1 = 0;
        int team2 = 0;

        for (int i = 0; i < SimulationParams.GAMES; i++) {
            switch (getGameResult(i)){
                case -1:
                    GenericIO.writelnString("Game " + (i+1) + " won by team 1");
                    team1++;
                    break;
                case 1:
                    GenericIO.writelnString("Game " + (i+1) + " won by team 2");
                    team2++;
                    break;
                case 0:
                    GenericIO.writelnString("Game " + (i+1) + " was a draw");
                    break;
            }
        }

        StringBuilder sb = new StringBuilder();

        if(team1 == team2){
            return "Match was a draw";
        }else if(team1 > team2){
            sb.append("Match was won by team " + 1);
        }else {
            sb.append("Match was won by team " + 2);
        }

        sb.append(" (").append(team1).append("-").append(team2).append(").");

        return sb.toString();
    }

    public synchronized void printMatchResults(){
        GenericIO.writelnString(Arrays.toString(matchRecords));
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