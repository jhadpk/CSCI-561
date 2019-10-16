package com.ai.assignment.api.game.players;


import com.ai.assignment.api.game.Player;
import com.ai.assignment.entities.Camp;
import com.ai.assignment.entities.Input;
import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.enums.MoveType;
import com.ai.assignment.entities.enums.PlayerType;
import com.ai.assignment.entities.board.Cell;

import java.util.ArrayList;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class WhitePlayer extends Player {

    @Override
    public ArrayList<Move> getNextMove(final Input input) {

        return null;
    }


    @Override
    public ArrayList<Move> getAvailableMoves(Cell cell) {
        if (isInOpposingCamp(cell)) {
            // keep cell within opposing camp and it should not move outside.
            // for now not finding any move if player has reached opposing camp, later add logic to remain in opp camp
            return null;
        }
        ArrayList<Move> availableMoves = new ArrayList<>();
        if (isInCamp(cell)) {
            if (isNotNull(cell.getLeft()) && cell.getLeft().getPlayerType() == PlayerType.NONE) {
                addMove(MoveType.EMPTY, cell.getRight(), availableMoves);
            }
            if (isNotNull(cell.getTop()) && cell.getTop().getPlayerType() == PlayerType.NONE) {
                addMove(MoveType.EMPTY, cell.getBottom(), availableMoves);
            }
            if (isNotNull(cell.getTopLeft()) && cell.getTopLeft().getPlayerType() == PlayerType.NONE) {
                addMove(MoveType.EMPTY, cell.getBottomRight(), availableMoves);
            }
            for (Move move : getJumpMoves(cell)) {
                int numJumps = move.getCells().size();
                Cell destinationCell = move.getCells().get(numJumps - 1);
                //if after jumping its still in camp, find if it moved farther away from corner
                if (isInCamp(destinationCell)) {
                    if (isFarFromCorner(WHITE_CORNER_CELL, cell, destinationCell)) {
                        availableMoves.add(move);
                    }
                } else {
                    availableMoves.add(move);
                }
            }
        } else {
            if (isValidMove(cell.getLeft())) {
                addMove(MoveType.EMPTY, cell.getLeft(), availableMoves);
            }
            if (isValidMove(cell.getRight())) {
                addMove(MoveType.EMPTY, cell.getRight(), availableMoves);
            }
            if (isValidMove(cell.getTop())) {
                addMove(MoveType.EMPTY, cell.getTop(), availableMoves);
            }
            if (isValidMove(cell.getBottom())) {
                addMove(MoveType.EMPTY, cell.getBottom(), availableMoves);
            }
            if (isValidMove(cell.getTopLeft())) {
                addMove(MoveType.EMPTY, cell.getTopLeft(), availableMoves);
            }
            if (isValidMove(cell.getTopRight())) {
                addMove(MoveType.EMPTY, cell.getTopRight(), availableMoves);
            }
            if (isValidMove(cell.getBottomLeft())) {
                addMove(MoveType.EMPTY, cell.getBottomLeft(), availableMoves);
            }
            if (isValidMove(cell.getBottomRight())) {
                addMove(MoveType.EMPTY, cell.getBottomRight(), availableMoves);
            }
            availableMoves.addAll(getJumpMoves(cell));
        }
        return availableMoves;
    }


    @Override
    public boolean isInCamp(Cell cell) {
        return Camp.whiteCamp.contains(cell.getRow() + "," + cell.getCol());
    }


    @Override
    public boolean isInOpposingCamp(Cell cell) {
        return Camp.blackCamp.contains(cell.getRow() + "," + cell.getCol());
    }
}
