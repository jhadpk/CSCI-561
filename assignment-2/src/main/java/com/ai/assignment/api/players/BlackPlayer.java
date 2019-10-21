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
        if (!isNotNull(cell)) {
            return availableMoves;
        }
        if (isInCamp(cell)) {
            final ArrayList<Move> allSingleMoves = getSingleMoves(cell);
            final ArrayList<Move> allJumpMoves = getJumpMoves(cell);

            final ArrayList<Move> firstPriorityMoves = new ArrayList<>();
            final ArrayList<Move> secondPriorityMoves = new ArrayList<>();
            for (Move move : allSingleMoves) {
                if (!isInCamp(move.getDestinationCell())) {
                    firstPriorityMoves.add(move);
                }
            }
            if (firstPriorityMoves.size() != 0) {
                allSingleMoves.retainAll(firstPriorityMoves);
            } else {
                //moves with destination cell within camp
                for (Move move : allSingleMoves) {
                    if (isFarFromCorner(BLACK_CORNER_CELL, cell, move.getDestinationCell())) {
                        secondPriorityMoves.add(move);
                    }
                }
                if (secondPriorityMoves.size() != 0) {
                    allSingleMoves.retainAll(secondPriorityMoves);
                }
            }


            final ArrayList<Move> firstPriorityJumps = new ArrayList<>();
            final ArrayList<Move> secondPriorityJumps = new ArrayList<>();
            for (Move move : allJumpMoves) {
                if (!isInCamp(move.getDestinationCell())) {
                    firstPriorityJumps.add(move);
                }
            }
            if (firstPriorityJumps.size() != 0) {
                allJumpMoves.retainAll(firstPriorityJumps);
            } else {
                //moves with destination cell within camp
                for (Move move : allJumpMoves) {
                    if (isFarFromCorner(BLACK_CORNER_CELL, cell, move.getDestinationCell())) {
                        secondPriorityJumps.add(move);
                    }
                }
                if (secondPriorityJumps.size() != 0) {
                    allJumpMoves.retainAll(secondPriorityJumps);
                }
            }
            availableMoves.addAll(allSingleMoves);
            availableMoves.addAll(allJumpMoves);
        } else if (isInOpposingCamp(cell)) {
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
                if (isCloserToCorner(WHITE_CORNER_CELL, cell, move.getDestinationCell())) {
                    availableMoves.add(move);
                }
            }
        } else {
            findMovesOutsideCamp(cell, availableMoves);

            //if any move can enter the opposite camp - make the best of that move
            final ArrayList<Move> bestMovesToEnterOppositionCamp = getOppositionCampEnteringMoves(availableMoves);
            if (bestMovesToEnterOppositionCamp.size() != 0) {
                availableMoves.retainAll(bestMovesToEnterOppositionCamp);
            }
        }
        return availableMoves;
    }


    @Override
    public ArrayList<Move> getOppositionCampEnteringMoves(ArrayList<Move> allAvailableMoves) {
        final ArrayList<Move> bestMovesToEnterOppositionCamp = new ArrayList<>();
        for (Move move : allAvailableMoves) {
            if (Camp.whiteCamp.contains(
                    move.getDestinationCell().getRow() + "," + move.getDestinationCell().getCol())) {
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
