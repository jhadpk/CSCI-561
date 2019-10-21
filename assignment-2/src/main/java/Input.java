import java.util.ArrayList;


/**
 * @author deepakjha on 10/15/19
 * @project ai-assignments
 */
public class Input {
    private GameType gameType;
    private PlayerType playerType;
    private double timeRemainingInSeconds;
    private Halma halma;
    private ArrayList<ArrayList<Cell>> board;
    private int maxDepth;

    public void setGameType(final String gameType) {
        this.gameType = GameType.getGameType(gameType);
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public void setPlayerType(final String playerColor) {
        this.playerType = PlayerType.getPlayerByColor(playerColor);
    }

    public PlayerType getPlayerType() {
        return this.playerType;
    }

    public void setTimeRemainingInSeconds(final String time) {
        this.timeRemainingInSeconds = Double.parseDouble(time);
    }

    public double getTimeRemainingInSeconds() {
        return this.timeRemainingInSeconds;
    }

    public void setHalma(ArrayList<ArrayList<String>> boardConfiguration) {
        this.halma = new Halma(boardConfiguration);
    }

    public Halma getHalma() {
        return this.halma;
    }

    public void setBoard(final ArrayList<ArrayList<Cell>> board) {
        this.board = board;
    }

    public ArrayList<ArrayList<Cell>> getBoard() {
        return this.board;
    }

    public void setMaxDepth(final int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getMaxDepth() {
        return this.maxDepth;
    }
}
