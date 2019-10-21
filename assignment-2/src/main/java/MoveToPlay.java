
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
        //if destinationCell is not near opposite camp, then give preference to jumps and diagonal movement.

        //take other parameter into equation too to find heuristic

        /*b) If a player has at least one piece left in their own camp, they have to
            • Move a piece out of their camp (i.e. at the end of the whole move the piece ends up
            outside of their camp).
            • If that’s not possible, move a piece in their camp further away from the corner of their
            own camp ([0,0] or [15,15] respectively).*/

        Halma.makeMove(move);


        this.heuristic = Halma.calculateBoardSituation(move.getPlayerType());
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
        int heuristicForMove = Halma.calculateBoardSituation(move.getPlayerType());
        Halma.undoMove(move);
        return heuristicForMove;
    }
}
