
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
}
