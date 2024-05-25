package interfaces;

import java.io.Serializable;

/**
 *  Data type to return the data to referee.
 *
 *  Used in calls on remote objects.
 */
public class ReturnReferee implements Serializable {
    /**
     *  Serialization key.
     */
    public static final long serialVersionUID = 2021L;

    /**
     * Referee state
     */
    private int state;

    /**
     * Current game
     */
    private int game;

    /**
     * Current trial
     */
    private int trial;

    /**
     * Match winning cause
     */
    private String winCause;

    /**
     * Game result
     */
    private int gameresult;

    /**
     * Game ended
     */
    private boolean gameEnded;

    /**
     * Match ended
     */
    private boolean matchEnded;

    /**
     * Constructor for referee state
     * @param state current state of the referee
     */
    public ReturnReferee(int state){
        this.state = state;
    }

    /**
     * Constructor for referee state and current game & trial
     * @param state current state
     * @param game current game
     * @param trial current trial
     */
    public ReturnReferee(int state, int game, int trial){
        this.state = state;
        this.game = game;
        this.trial = trial;
    }

    /**
     * Constructor for referee state and winning cause
     * @param state current state
     * @param winCause winning cause
     * @param gameresult result of the game
     * @param gameEnded whether the game has ended or not
     * @param matchEnded whether the match has ended or not
     */
    public ReturnReferee(int state, int gameresult, String winCause, boolean gameEnded, boolean matchEnded){
        this.state = state;
        this.gameresult = gameresult;
        this.winCause = winCause;
        this.gameEnded = gameEnded;
        this.matchEnded = matchEnded;
    }

    /**
     * Constructor for referee state and winning cause
     * @param state current state
     * @param gameEnded whether the game has ended or not
     */
    public ReturnReferee(int state, boolean gameEnded){
        this.state = state;
        this.gameEnded = gameEnded;
    }

    /**
     * Constructor for referee state and current game & trial
     * @param state current state
     * @param game current game
     */
    public ReturnReferee(int state, int game, boolean thisIsForGame){
        this.state = state;
        this.game = game;
    }

    /**
     * Constructor for referee state and current game & trial
     * @param state current state
     * @param trial current trial
     */
    public ReturnReferee(int state, int trial){
        this.state = state;
        this.trial = trial;
    }

    /**
     * Get state
     * @return referee state
     */
    public int getState() {
        return state;
    }

    /**
     * Set state
     * @param state current referee state
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Get game
     * @return current game
     */
    public int getGame() {
        return game;
    }

    /**
     * Set game
     * @param game current game
     */
    public void setGame(int game) {
        this.game = game;
    }

    /**
     * Get trial
     * @return current trial
     */
    public int getTrial() {
        return trial;
    }

    /**
     * Set trial
     * @param trial current trial
     */
    public void setTrial(int trial) {
        this.trial = trial;
    }

    /**
     * Get winning cause
     * @return winning cause
     */
    public String getWinCause() {
        return winCause;
    }

    /**
     * Set winning cause
     * @param winCause winning cause
     */
    public void setWinCause(String winCause) {
        this.winCause = winCause;
    }

    /**
     * Get game result
     * @return result of the game
     */
    public int getGameresult() {
        return gameresult;
    }

    /**
     * set game result
     * @param gameresult result of the game
     */
    public void setGameresult(int gameresult) {
        this.gameresult = gameresult;
    }

    /**
     * Get flag for end of game
     * @return whether it is the end of the game
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Set flag for end of game
     * @param gameEnded whether it is the end of the game
     */
    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    /**
     * Get flag for end of match
     * @return whether it is the end of the match
     */
    public boolean isMatchEnded() {
        return matchEnded;
    }

    /**
     * Set flag for end of match
     * @param matchEnded whether it is the end of the match
     */
    public void setMatchEnded(boolean matchEnded) {
        this.matchEnded = matchEnded;
    }
}
