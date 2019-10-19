package com.ai.assignment.api;

import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.board.Cell;

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
     *
     *
     * If player is in camp or is in opposition camp, then only moves allowed :
     *      Single Move : right, bottom, bottomRight
     *      Jump Move :
     *          If in camp -> destinationCell should be farther from corner than startingCell
     *          If in opposition camp -> destinationCell should be closer to corner than startingCell
     * Else find all possible moves allowed.
     */
    ArrayList<Move> getAvailableMoves(Cell cell);

    ArrayList<Move> getBestMoveToEnterOppositionCamp(ArrayList<Move> allAvailableMoves);

    boolean isInCamp(Cell cell);

    boolean isInOpposingCamp(Cell cell);

    boolean returnsToCamp(final Cell startingCell, final Cell destinationCell);
}
