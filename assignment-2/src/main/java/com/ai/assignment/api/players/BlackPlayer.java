package com.ai.assignment.api.players;


import com.ai.assignment.api.PlayerImpl;
import com.ai.assignment.entities.Camp;
import com.ai.assignment.entities.Input;
import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.board.Cell;
import com.ai.assignment.entities.enums.MoveType;
import com.ai.assignment.entities.enums.PlayerType;

import java.util.ArrayList;

import static com.ai.assignment.entities.enums.PlayerType.BLACK;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class BlackPlayer extends PlayerImpl {

    public BlackPlayer(Input input) {
        super(input);
    }


    @Override
    public Move getNextMove() {
        return decideNextMove(getAllPlayerPositions(BLACK));
    }

    @Override
    public ArrayList<Move> getAvailableMoves(Cell cell) {
        ArrayList<Move> availableMoves = new ArrayList<>();
        if (isInCamp(cell) || isInOpposingCamp(cell)) {
            if (isNotNull(cell.getRight()) && cell.getRight().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getRight(), availableMoves);
            }
            if (isNotNull(cell.getBottom()) && cell.getBottom().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getBottom(), availableMoves);
            }
            if (isNotNull(cell.getBottomRight()) && cell.getBottomRight().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getBottomRight(), availableMoves);
            }
            for (Move move : getJumpMoves(cell)) {
                //if after jumping its still in camp, find if it moved farther away from corner
                if (isInCamp(move.getDestinationCell())) {
                    if (isFarFromCorner(BLACK_CORNER_CELL, cell, move.getDestinationCell())) {
                        availableMoves.add(move);
                    }
                } else if (isInOpposingCamp(cell)) {
                    //cell was in whiteCamp before beginning the move. Only move towards white corner cell (15,15)
                    if (isCloserToCorner(WHITE_CORNER_CELL, cell, move.getDestinationCell())) {
                        availableMoves.add(move);
                    }
                } else {
                    availableMoves.add(move);
                }
            }
        } else {
            findMovesOutsideCamp(cell, availableMoves);

            //if any move can enter the opposite camp - make the best of that move
            final ArrayList<Move> bestMovesToEnterOppositionCamp = getBestMoveToEnterOppositionCamp(availableMoves);
            if (bestMovesToEnterOppositionCamp.size() != 0) {
                availableMoves.retainAll(bestMovesToEnterOppositionCamp);
            }
        }
        return availableMoves;
    }


    @Override
    public ArrayList<Move> getBestMoveToEnterOppositionCamp(ArrayList<Move> allAvailableMoves) {
        final ArrayList<Move> bestMovesToEnterOppositionCamp = new ArrayList<>();
        for (Move move : allAvailableMoves) {
            if (Camp.whiteCamp.contains(move.getDestinationCell().getRow() + "," + move.getDestinationCell().getCol())) {
                bestMovesToEnterOppositionCamp.add(move);
            }
        }
        return bestMovesToEnterOppositionCamp;
    }

    @Override
    public boolean isInCamp(Cell cell) {
        return Camp.blackCamp.contains(cell.getRow() + "," + cell.getCol());
    }


    @Override
    public boolean isInOpposingCamp(Cell cell) {
        return Camp.whiteCamp.contains(cell.getRow() + "," + cell.getCol());
    }


    @Override
    public boolean returnsToCamp(Cell startingCell, Cell destinationCell) {
        if (isNotNull(startingCell) && isNotNull(destinationCell)) {
            if (!Camp.blackCamp.contains(startingCell.getRow() + "," + startingCell.getCol())) {
                return Camp.blackCamp.contains(destinationCell.getRow() + "," + destinationCell.getCol());
            }
        }
        return false;
    }
}
