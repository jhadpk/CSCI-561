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

import static com.ai.assignment.entities.enums.PlayerType.WHITE;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class WhitePlayer extends Player {

    @Override
    public Move getNextMove(final Input input) {
        ArrayList<Cell> whitePlayers = getPlayerPositions(input.getBoard());
        ArrayList<MoveToPlay> allBestMoves = new ArrayList<>();
        for (Cell cell : whitePlayers) {
            allBestMoves.add(executeMinMax(0, getAvailableMoves(cell), null, true, 0, 0));
        }
        allBestMoves.sort(Comparator.comparing(MoveToPlay::getHeuristic).reversed());
        return allBestMoves.get(0).getMove();
    }


    @Override
    public ArrayList<Cell> getPlayerPositions(ArrayList<ArrayList<Cell>> board) {
        ArrayList<Cell> whitePlayerPositions = new ArrayList<>();
        for (ArrayList<Cell> row : board) {
            for (Cell cell : row) {
                if (cell.getPlayerType().equals(WHITE)) {
                    whitePlayerPositions.add(cell);
                }
            }
        }
        return whitePlayerPositions;
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
                addSingleMove(MoveType.EMPTY, cell, cell.getRight(), availableMoves);
            }
            if (isNotNull(cell.getTop()) && cell.getTop().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getBottom(), availableMoves);
            }
            if (isNotNull(cell.getTopLeft()) && cell.getTopLeft().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getBottomRight(), availableMoves);
            }
            for (Move move : getJumpMoves(cell)) {
                int numJumps = move.getPath().size();
                Cell destinationCell = move.getPath().get(numJumps - 1);
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
        return Camp.whiteCamp.contains(cell.getRow() + "," + cell.getCol());
    }


    @Override
    public boolean isInOpposingCamp(Cell cell) {
        return Camp.blackCamp.contains(cell.getRow() + "," + cell.getCol());
    }
}
