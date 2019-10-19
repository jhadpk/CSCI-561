package com.ai.assignment.api.players;


import com.ai.assignment.api.PlayerImpl;
import com.ai.assignment.entities.Camp;
import com.ai.assignment.entities.Input;
import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.board.Cell;
import com.ai.assignment.entities.enums.MoveType;
import com.ai.assignment.entities.enums.PlayerType;

import java.util.ArrayList;

import static com.ai.assignment.entities.enums.PlayerType.WHITE;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class WhitePlayer extends PlayerImpl {

    public WhitePlayer(Input input) {
        super(input);
    }


    @Override
    public Move getNextMove() {
        return decideNextMove(getAllPlayerPositions(WHITE));
    }


    @Override
    public ArrayList<Move> getAvailableMoves(Cell cell) {
        ArrayList<Move> availableMoves = new ArrayList<>();
        if (isInCamp(cell) || isInOpposingCamp(cell)) {
            if (isNotNull(cell.getLeft()) && cell.getLeft().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getLeft(), availableMoves);
            }
            if (isNotNull(cell.getTop()) && cell.getTop().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getTop(), availableMoves);
            }
            if (isNotNull(cell.getTopLeft()) && cell.getTopLeft().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getTopLeft(), availableMoves);
            }
            for (Move move : getJumpMoves(cell)) {
                int numJumps = move.getPath().size();
                Cell destinationCell = move.getPath().get(numJumps - 1);
                //if after jumping its still in camp, find if it moved farther away from corner
                if (isInCamp(destinationCell)) {
                    if (isFarFromCorner(WHITE_CORNER_CELL, cell, destinationCell)) {
                        availableMoves.add(move);
                    }
                } else if (isInOpposingCamp(cell)) {
                    //cell was in black camp before beginning the move. Only move towards black corner cell (0, 0)
                    if (isCloserToCorner(BLACK_CORNER_CELL, cell, move.getDestinationCell())) {
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
            if (Camp.blackCamp.contains(move.getDestinationCell().getRow() + "," + move.getDestinationCell().getCol())) {
                bestMovesToEnterOppositionCamp.add(move);
            }
        }
        return bestMovesToEnterOppositionCamp;
    }


    @Override
    public boolean isInCamp(Cell cell) {
        return Camp.whiteCamp.contains(cell.getRow() + "," + cell.getCol());
    }


    @Override
    public boolean isInOpposingCamp(Cell cell) {
        return Camp.blackCamp.contains(cell.getRow() + "," + cell.getCol());
    }


    @Override
    public boolean returnsToCamp(Cell startingCell, Cell destinationCell) {
        if (isNotNull(startingCell) && isNotNull(destinationCell)) {
            if (!Camp.whiteCamp.contains(startingCell.getRow() + "," + startingCell.getCol())) {
                return Camp.whiteCamp.contains(destinationCell.getRow() + "," + destinationCell.getCol());
            }
        }
        return false;
    }
}
