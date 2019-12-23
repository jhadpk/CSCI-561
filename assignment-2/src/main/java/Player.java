import java.util.ArrayList;


/**
 * @author deepakjha on 10/15/19
 * @project ai-assignments
 */
public interface Player {

    Move getNextMove();

    /***
     *  a) Players cannot make a move that starts outside their own camp and causes one of their
     *  pieces to end up in their own camp.
     *  b) If a player has at least one piece left in their own camp, they have to
     *      • Move a piece out of their camp (i.e. at the end of the whole move the piece ends up
     *      outside of their camp).
     *      • If that’s not possible, move a piece in their camp further away from the corner of their
     *      own camp ([0,0] or [15,15] respectively).
     */
    ArrayList<Move> getAvailableMoves(Cell cell);

    ArrayList<Move> getOppositionCampEnteringMoves(ArrayList<Move> allAvailableMoves);

    boolean isInCamp(Cell cell);

    boolean isInOpposingCamp(Cell cell);

    boolean returnsToCamp(final Cell startingCell, final Cell destinationCell);
}
