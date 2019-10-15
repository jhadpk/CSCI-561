package com.ai.assignment.player;


import com.ai.assignment.entities.Camp;
import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.MoveType;
import com.ai.assignment.entities.board.Cell;

import java.util.ArrayList;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class BlackPlayer extends Player {
    @Override
    public ArrayList<Move> getAvailableMoves(Cell cell) {
        if (isInOpposingCamp(cell)) {
            // keep cell within opposing camp and it should not move outside.
            // for now not finding any move if player has reached opposing camp, later add logic to remain in opp camp
            return null;
        }

        ArrayList<Move> availableMoves = new ArrayList<>();

        //Note : A cell is null when its not on board. An available cell is not identified by null, rather by
        // playertype None


        /*
        a) Players cannot make a move that starts outside their own camp and causes one of their
        pieces to end up in their own camp.
        b) If a player has at least one piece left in their own camp, they have to
        • Move a piece out of their camp (i.e. at the end of the whole move the piece ends up
        outside of their camp).
        • If that’s not possible, move a piece in their camp further away from the corner of their
        own camp ([0,0] or [15,15] respectively).
         */


        if (isInCamp(cell)) {
            if (isNotNull(cell.getRight())) {
                addMove(MoveType.EMPTY, cell.getRight(), availableMoves);
            }
            if (isNotNull(cell.getBottom())) {
                addMove(MoveType.EMPTY, cell.getBottom(), availableMoves);
            }
            if (isNotNull(cell.getBottomRight())) {
                addMove(MoveType.EMPTY, cell.getBottomRight(), availableMoves);
            }
            for (Move move : getJumpMoves(cell)) {
                int numJumps = move.getCells().size();
                Cell destinationCell = move.getCells().get(numJumps - 1);
                //if after jumping its still in camp, find if it moved farther away from corner
                if (isInCamp(destinationCell)) {
                    if (isFarFromCorner(BLACK_CORNER_CELL, cell, destinationCell)) {
                        availableMoves.add(move);
                    }
                } else {
                    availableMoves.add(move);
                }
            }
        } else {
            if (isMoveValid(cell.getLeft())) {
                addMove(MoveType.EMPTY, cell.getLeft(), availableMoves);
            }
            if (isMoveValid(cell.getRight())) {
                addMove(MoveType.EMPTY, cell.getRight(), availableMoves);
            }
            if (isMoveValid(cell.getTop())) {
                addMove(MoveType.EMPTY, cell.getTop(), availableMoves);
            }
            if (isMoveValid(cell.getBottom())) {
                addMove(MoveType.EMPTY, cell.getBottom(), availableMoves);
            }
            if (isMoveValid(cell.getTopLeft())) {
                addMove(MoveType.EMPTY, cell.getTopLeft(), availableMoves);
            }
            if (isMoveValid(cell.getTopRight())) {
                addMove(MoveType.EMPTY, cell.getTopRight(), availableMoves);
            }
            if (isMoveValid(cell.getBottomLeft())) {
                addMove(MoveType.EMPTY, cell.getBottomLeft(), availableMoves);
            }
            if (isMoveValid(cell.getBottomRight())) {
                addMove(MoveType.EMPTY, cell.getBottomRight(), availableMoves);
            }
            availableMoves.addAll(getJumpMoves(cell));
        }
        return availableMoves;
    }


    @Override
    public boolean isInCamp(Cell cell) {
        return Camp.blackCamp.contains(cell.getRow() + "," + cell.getCol());
    }


    @Override
    public boolean isInOpposingCamp(Cell cell) {
        return Camp.whiteCamp.contains(cell.getRow() + "," + cell.getCol());
    }
}
