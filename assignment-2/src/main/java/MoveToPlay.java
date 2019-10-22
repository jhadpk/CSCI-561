
/**
 * @author deepakjha on 10/17/19
 * @project ai-assignments
 */
public class MoveToPlay {
    public int heuristic;
    public Move move;

    public MoveToPlay() {
        this.heuristic = 0;
    }

    public MoveToPlay(Move move) {
        Halma.makeMove(move);
        this.heuristic = Halma.getBoardSituation(move.getPlayerType());
        this.move = move;
        Halma.undoMove(move);
    }


    public MoveToPlay(Move move, int heuristic) {
        this.move = move;
        this.heuristic = heuristic;
    }


    public int getHeuristic() {
        return this.heuristic;
    }


    public Move getMove() {
        return this.move;
    }


    public static int getHeuristicForMove(Move move) {
        Halma.makeMove(move);
        int heuristicForMove = Halma.getBoardSituation(move.getPlayerType());
        Halma.undoMove(move);
        return heuristicForMove;
    }
}
