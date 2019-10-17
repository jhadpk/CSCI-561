package com.ai.assignment.api.game.players;


import com.ai.assignment.api.game.Player;
import com.ai.assignment.entities.Camp;
import com.ai.assignment.entities.Input;
import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.MoveToPlay;
import com.ai.assignment.entities.board.Cell;
import com.ai.assignment.entities.enums.MoveType;
import com.ai.assignment.entities.enums.PlayerType;

import java.util.ArrayList;
import java.util.Comparator;

import static com.ai.assignment.entities.enums.PlayerType.BLACK;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class BlackPlayer extends Player {

    @Override
    public Move getNextMove(final Input input) {
        ArrayList<Cell> blackPlayers = getPlayerPositions(input.getBoard());
        ArrayList<MoveToPlay> allBestMoves = new ArrayList<>();
        for (Cell cell : blackPlayers) {
            allBestMoves.add(executeMinMax(0, getAvailableMoves(cell), null, true,0,0));
        }
        allBestMoves.sort(Comparator.comparing(MoveToPlay::getHeuristic).reversed());
        return allBestMoves.get(0).getMove();
    }


    @Override
    public ArrayList<Cell> getPlayerPositions(ArrayList<ArrayList<Cell>> board) {
        ArrayList<Cell> blackPlayerPositions = new ArrayList<>();
        for (ArrayList<Cell> row : board) {
            for (Cell cell : row) {
                if (cell.getPlayerType().equals(BLACK)) {
                    blackPlayerPositions.add(cell);
                }
            }
        }
        return blackPlayerPositions;
    }


    @Override
    public ArrayList<Move> getAvailableMoves(Cell cell) {
        if (isInOpposingCamp(cell)) {
            // keep cell within opposing camp and it should not move outside.
            // for now not finding any move if player has reached opposing camp, later add logic to remain in opp camp
            return null;
        }

        ArrayList<Move> availableMoves = new ArrayList<>();

        //IDENTIFY MOVES WHICH ARE FOR CELLS IN BASECAMP, AND DO MINMAX ON THOSE MOVES ONLY

        if (isInCamp(cell)) {
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
                } else {
                    availableMoves.add(move);
                }
            }
        } else {
            if (isValidMove(cell.getLeft())) {
                addSingleMove(MoveType.EMPTY, cell, cell.getLeft(), availableMoves);
            }
            if (isValidMove(cell.getRight())) {
                addSingleMove(MoveType.EMPTY, cell, cell.getRight(), availableMoves);
            }
            if (isValidMove(cell.getTop())) {
                addSingleMove(MoveType.EMPTY, cell, cell.getTop(), availableMoves);
            }
            if (isValidMove(cell.getBottom())) {
                addSingleMove(MoveType.EMPTY, cell, cell.getBottom(), availableMoves);
            }
            if (isValidMove(cell.getTopLeft())) {
                addSingleMove(MoveType.EMPTY, cell, cell.getTopLeft(), availableMoves);
            }
            if (isValidMove(cell.getTopRight())) {
                addSingleMove(MoveType.EMPTY, cell, cell.getTopRight(), availableMoves);
            }
            if (isValidMove(cell.getBottomLeft())) {
                addSingleMove(MoveType.EMPTY, cell, cell.getBottomLeft(), availableMoves);
            }
            if (isValidMove(cell.getBottomRight())) {
                addSingleMove(MoveType.EMPTY, cell, cell.getBottomRight(), availableMoves);
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


    @Override
    public boolean returnsToCamp(Cell startingCell, Cell destinationCell) {
        if (!Camp.blackCamp.contains(startingCell.getRow() + "," + startingCell.getCol())) {
            return Camp.blackCamp.contains(destinationCell.getRow() + "," + destinationCell.getCol());
        }
        return false;
    }
}
