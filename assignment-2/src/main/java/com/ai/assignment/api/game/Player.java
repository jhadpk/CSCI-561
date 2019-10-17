package com.ai.assignment.api.game;


import com.ai.assignment.entities.Camp;
import com.ai.assignment.entities.Jump;
import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.MoveToPlay;
import com.ai.assignment.entities.board.Cell;
import com.ai.assignment.entities.board.Coordinates;
import com.ai.assignment.entities.enums.MoveType;
import com.ai.assignment.entities.enums.PlayerType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ai.assignment.entities.board.Halma.getCellByCoordinate;
import static com.ai.assignment.entities.board.Halma.makeMove;
import static com.ai.assignment.entities.board.Halma.undoMove;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */


public abstract class Player implements Game {
    protected Cell BLACK_CORNER_CELL = new Cell(0, 0);
    protected Cell WHITE_CORNER_CELL = new Cell(15, 15);
    protected int MAX = 1000;
    protected int MIN = -1000;

    public void addSingleMove(MoveType moveType, Cell startingCell, Cell destinationCell,
            ArrayList<Move> availableMoves) {
        availableMoves.add(getMove(startingCell.getPlayerType(), moveType, Arrays.asList(startingCell, destinationCell),
                startingCell, destinationCell));
    }


    public ArrayList<Move> getJumpMoves(final Cell cell) {
        //hard coding board size to 16x16. Validate.
        ArrayList<Jump> jumps = new ArrayList<>();
        getJumps(cell, null, jumps);
        ArrayList<Move> jumpMoves = new ArrayList<>();
        for (ArrayList<Cell> path : getJumpingPaths(getParentInfo(jumps, 16, 16), cell)) {
            jumpMoves.add(getMove(cell.getPlayerType(), MoveType.JUMP, path, path.get(0), path.get(path.size() - 1)));
        }
        return jumpMoves;
    }


    public Move getMove(PlayerType playerType, MoveType moveType, List<Cell> path, Cell startingCell,
            Cell destinationCell) {
        return new Move(playerType, moveType, path, startingCell, destinationCell);
    }


    public void getJumps(Cell cell, Cell parent, ArrayList<Jump> jumps) {
        if (isNotNull(cell.getLeft()) && isNotNull(cell.getLeft().getLeft()) && isJumpValid(parent, cell.getLeft(),
                cell.getLeft().getLeft())) {
            recursiveGetJumps(cell, cell.getLeft().getLeft(), jumps);
        }
        if (isNotNull(cell.getRight()) && isNotNull(cell.getRight().getRight()) && isJumpValid(parent, cell.getRight(),
                cell.getRight().getRight())) {
            recursiveGetJumps(cell, cell.getRight().getRight(), jumps);
        }
        if (isNotNull(cell.getTop()) && isNotNull(cell.getTop().getTop()) && isJumpValid(parent, cell.getTop(),
                cell.getTop().getTop())) {
            recursiveGetJumps(cell, cell.getTop().getTop(), jumps);
        }
        if (isNotNull(cell.getBottom()) && isNotNull(cell.getBottom().getBottom()) && isJumpValid(parent,
                cell.getBottom(), cell.getBottom().getBottom())) {
            recursiveGetJumps(cell, cell.getBottom().getBottom(), jumps);
        }
        if (isNotNull(cell.getTopLeft()) && isNotNull(cell.getTopLeft().getTopLeft()) && isJumpValid(parent,
                cell.getTopLeft(), cell.getTopLeft().getTopLeft())) {
            recursiveGetJumps(cell, cell.getTopLeft().getTopLeft(), jumps);
        }
        if (isNotNull(cell.getTopRight()) && isNotNull(cell.getTopRight().getTopRight()) && isJumpValid(parent,
                cell.getTopRight(), cell.getTopRight().getTopRight())) {
            recursiveGetJumps(cell, cell.getTopRight().getTopRight(), jumps);
        }
        if (isNotNull(cell.getBottomLeft()) && isNotNull(cell.getBottomLeft().getBottomLeft()) && isJumpValid(parent,
                cell.getBottomLeft(), cell.getBottomLeft().getBottomLeft())) {
            recursiveGetJumps(cell, cell.getBottomLeft().getBottomLeft(), jumps);
        }
        if (isNotNull(cell.getBottomRight()) && isNotNull(cell.getBottomRight().getBottomRight()) && isJumpValid(parent,
                cell.getBottomRight(), cell.getBottomRight().getBottomRight())) {
            recursiveGetJumps(cell, cell.getBottomRight().getBottomRight(), jumps);
        }
    }


    public void recursiveGetJumps(Cell startingCell, Cell destinationCell, ArrayList<Jump> jumps) {
        jumps.add(new Jump(startingCell, destinationCell));
        getJumps(destinationCell, startingCell, jumps);
    }


    public boolean isJumpValid(final Cell startingCell, final Cell inBetweenCell, final Cell destinationCell) {
        return (inBetweenCell.getPlayerType() == PlayerType.BLACK || inBetweenCell.getPlayerType() == PlayerType.WHITE)
                && destinationCell.getPlayerType() == PlayerType.NONE && isNotParentCell(startingCell, destinationCell)
                && !isInCamp(destinationCell);
    }


    public boolean isValidMove(final Cell destinationCell) {
        //should not move back to camp
        return isNotNull(destinationCell) && !isInCamp(destinationCell)
                && destinationCell.getPlayerType() == PlayerType.NONE;
    }


    public boolean isNotNull(Object obj) {
        return null != obj;
    }


    public boolean isNotParentCell(Cell cell1, Cell cell2) {
        if (null == cell1) { return true; }
        return cell1.getRow() != cell2.getRow() || cell1.getCol() != cell2.getCol();
    }


    public Cell[][] getParentInfo(ArrayList<Jump> jumps, final int rowSize, final int colSize) {
        Cell[][] parentInfo = new Cell[rowSize][colSize];
        for (Jump jump : jumps) {
            Cell current = jump.getCurrent();
            parentInfo[current.getRow()][current.getCol()] = jump.getParent();
        }
        return parentInfo;
    }


    public ArrayList<ArrayList<Cell>> getJumpingPaths(final Cell[][] parentInfo, final Cell startingCell) {
        ArrayList<ArrayList<Cell>> jumpingPaths = new ArrayList<>();
        for (int i = 0; i < parentInfo.length; i++) {
            for (int j = 0; j < parentInfo[i].length; j++) {
                if (null != parentInfo[i][j]) {
                    jumpingPaths.add(retracePath(parentInfo, startingCell, new Cell(i, j), parentInfo[i][j]));
                }
            }
        }
        return jumpingPaths;
    }


    public ArrayList<Cell> retracePath(final Cell[][] parentInfo, final Cell startingCell, final Cell currentCell,
            final Cell target) {
        final ArrayList<Cell> path = new ArrayList<>();
        path.add(currentCell);
        if (currentCell.getRow() == startingCell.getRow() && currentCell.getCol() == startingCell.getCol()) {
            path.add(startingCell);
            return path;
        }
        Cell cell = target;
        path.add(cell);
        while (cell.getRow() != startingCell.getRow() || cell.getCol() != startingCell.getCol()) {
            if (cell.getRow() < 0 || cell.getRow() >= 16 || cell.getCol() < 0 || cell.getCol() >= 16) {
                return null;
            }
            cell = parentInfo[cell.getRow()][cell.getCol()];
            path.add(cell);
        }
        Collections.reverse(path);
        return path;
    }

    public boolean isFarFromCorner(final Cell corner, final Cell startingCell, final Cell destinationCell) {
        //check only if start and destination is in camp
        //horizontalDistanceAtEnd >= horizontalDistanceAtStart && verticalDistanceAtEnd >= verticalDistanceAtStart
        return Math.abs(destinationCell.getCol() - corner.getCol()) >= Math.abs(startingCell.getCol() - corner.getCol())
                && Math.abs(destinationCell.getRow() - corner.getRow()) >= Math.abs(
                startingCell.getRow() - corner.getRow());
    }

    public MoveToPlay executeMinMax(int depth, ArrayList<Move> moves, Move play, boolean maximizing, int alpha,
            int beta) {
        if (depth == 3 || isGameOver(play.getPlayerType())) {
            if (null != play && null != play.getDestinationCell()) {
                return new MoveToPlay(play);
            } else {
                return new MoveToPlay();
            }
        }

        MoveToPlay returnMove = new MoveToPlay();
        if (maximizing) {
            int bestScore = MIN;
            for (Move move : moves) {
                makeMove(move);
                returnMove = executeMinMax(depth + 1, getAvailableMoves(move.getDestinationCell()), move,
                        false, alpha, beta);
                undoMove(move);
                bestScore = Math.max(bestScore, returnMove.heuristic);
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha) { break; }
            }
            return returnMove;
        } else {
            int bestScore = MAX;
            for (Move move : moves) {
                makeMove(move);
                returnMove = executeMinMax(depth + 1, getAvailableMoves(move.getDestinationCell()), move, true,
                        alpha, beta);
                undoMove(move);
                bestScore = Math.min(bestScore, returnMove.heuristic);
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) { break; }
            }
            return returnMove;
        }
    }


    private boolean isGameOver(final PlayerType playerType) {
        switch (playerType) {
            case BLACK:
                return isWhiteCampOccupiedByBlack();
            case WHITE:
                return isBlackCampOccupiedByWhite();
        }
        return false;
    }


    private boolean isWhiteCampOccupiedByBlack() {
        if (hasWhiteNotLeftCamp()) {
            System.out.println("Can be start of game OR Opponent is spoiling,");
            return false;
        }
        for (final String coord : Camp.whiteCamp) {
            Cell cell = getCellByCoordinate(new Coordinates(coord));
            if (cell.getPlayerType().equals(PlayerType.NONE)) {
                return false;
            }
        }
        return true;
    }


    private boolean hasWhiteNotLeftCamp() {
        for (final String coord : Camp.whiteCamp) {
            Cell cell = getCellByCoordinate(new Coordinates(coord));
            if (cell.getPlayerType().equals(PlayerType.NONE) || cell.getPlayerType().equals(PlayerType.BLACK)) {
                return false;
            }
        }
        return true;
    }


    private boolean isBlackCampOccupiedByWhite() {
        if (hasBlackNotLeftCamp()) {
            System.out.println("Can be start of game OR Opponent is spoiling,");
            return false;
        }
        for (final String coord : Camp.blackCamp) {
            Cell cell = getCellByCoordinate(new Coordinates(coord));
            if (cell.getPlayerType().equals(PlayerType.NONE)) {
                return false;
            }
        }
        return true;
    }


    private boolean hasBlackNotLeftCamp() {
        for (final String coord : Camp.blackCamp) {
            Cell cell = getCellByCoordinate(new Coordinates(coord));
            if (cell.getPlayerType().equals(PlayerType.NONE) || cell.getPlayerType().equals(PlayerType.WHITE)) {
                return false;
            }
        }
        return true;
    }

}
