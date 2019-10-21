import java.util.List;



/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */

public class Move {
    private PlayerType playerType;
    private MoveType moveType;
    private List<Cell> path;
    private Cell startingCell;
    private Cell destinationCell;

    public Move(PlayerType playerType, MoveType moveType, List<Cell> path, Cell startingCell, Cell destinationCell) {
        this.playerType = playerType;
        this.moveType = moveType;
        this.path = path;
        this.startingCell = startingCell;
        this.destinationCell = destinationCell;
    }


    public PlayerType getPlayerType() {
        return this.playerType;
    }


    public MoveType getMoveType() {
        return this.moveType;
    }


    public List<Cell> getPath() {
        return this.path;
    }


    public Cell getStartingCell() {
        return this.startingCell;
    }


    public Cell getDestinationCell() {
        return this.destinationCell;
    }

    public static int getHeuristic(Move move) {
        return MoveToPlay.getHeuristicForMove(move);
    }

}
