package com.ai.assignment.api.game;


import com.ai.assignment.entities.Jump;
import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.enums.MoveType;
import com.ai.assignment.entities.enums.PlayerType;
import com.ai.assignment.entities.board.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */


public abstract class Player implements Game {
    protected Cell BLACK_CORNER_CELL = new Cell(0, 0);
    protected Cell WHITE_CORNER_CELL = new Cell(15, 15);

    public void addMove(MoveType moveType, Cell cell, ArrayList<Move> availableMoves) {
        availableMoves.add(getMove(moveType, Collections.singletonList(cell)));
    }


    public Move getMove(MoveType moveType, List<Cell> cells) {
        return new Move(moveType, cells);
    }


    public ArrayList<Move> getJumpMoves(final Cell cell) {
        //hard coding board size to 16x16. Validate.
        ArrayList<Jump> jumps = new ArrayList<>();
        getJumps(cell, null, jumps);
        ArrayList<Move> jumpMoves = new ArrayList<>();
        for (ArrayList<Cell> move : getJumpingPaths(getParentInfo(jumps, 16, 16), cell)) {
            jumpMoves.add(new Move(MoveType.JUMP, move));
        }
        return jumpMoves;
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
        final int horizontalDistanceAtStart = Math.abs(startingCell.getCol() - corner.getCol());
        final int verticalDistanceAtStart = Math.abs(startingCell.getRow() - corner.getRow());
        final int horizontalDistanceAtEnd = Math.abs(destinationCell.getCol() - corner.getCol());
        final int verticalDistanceAtEnd = Math.abs(destinationCell.getRow() - corner.getRow());
        return horizontalDistanceAtEnd >= horizontalDistanceAtStart && verticalDistanceAtEnd >= verticalDistanceAtStart;
    }
}
