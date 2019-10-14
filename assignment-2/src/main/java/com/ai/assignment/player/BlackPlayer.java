package com.ai.assignment.player;


import com.ai.assignment.entities.Jump;
import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.MoveType;
import com.ai.assignment.entities.PlayerType;
import com.ai.assignment.entities.board.Cell;
import com.ai.assignment.entities.board.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class BlackPlayer extends Player {

    @Override
    public ArrayList<Move> getAvailableMoves(Cell cell) {
        ArrayList<Move> availableMoves = new ArrayList<>();


        if (isInCamp(cell)) {
            if (cell.getRight() == null) {
                addMove(MoveType.EMPTY, cell.getRight(), availableMoves);
            } else if (cell.getRight().getRight() == null) {
                addMove(MoveType.JUMP, cell.getRight().getRight(), availableMoves);
                //jumpMove.add(cell.getRight().getRight());
            }
            if (cell.getBottom() == null) {
                addMove(MoveType.EMPTY, cell.getBottom(), availableMoves);
            } else if (cell.getBottom().getBottom() == null) {
                addMove(MoveType.JUMP, cell.getBottom().getBottom(), availableMoves);
                //jumpMove.add(cell.getBottom().getBottom());
            }
            if (cell.getBottomRight() == null) {
                addMove(MoveType.EMPTY, cell.getBottomRight(), availableMoves);
            } else if (cell.getBottomRight().getBottomRight() == null) {
                addMove(MoveType.JUMP, cell.getBottomRight().getBottomRight(), availableMoves);
                //jumpMove.add(cell.getBottomRight().getBottomRight());
            }
        } else {
            // consider multiple jumping moves

            if (cell.getLeft() == null) {
                addMove(MoveType.EMPTY, cell.getLeft(), availableMoves);
            } else if (cell.getLeft().getLeft() == null) {
                addMove(MoveType.JUMP, cell.getLeft().getLeft(), availableMoves);
                //jumpMove.add(cell.getLeft().getLeft());
            }


            if (cell.getRight() == null) {
                addMove(MoveType.EMPTY, cell.getRight(), availableMoves);
            } else if (cell.getRight().getRight() == null) {
                addMove(MoveType.JUMP, cell.getRight().getRight(), availableMoves);
                //jumpMove.add(cell.getRight().getRight());
            }


            if (cell.getTop() == null) {
                addMove(MoveType.EMPTY, cell.getTop(), availableMoves);
            } else if (cell.getTop().getTop() == null) {
                addMove(MoveType.JUMP, cell.getTop().getTop(), availableMoves);
                //jumpMove.add(cell.getTop().getTop());
            }

            if (cell.getBottom() == null) {
                addMove(MoveType.EMPTY, cell.getBottom(), availableMoves);
            } else if (cell.getBottom().getBottom() == null) {
                addMove(MoveType.JUMP, cell.getBottom().getBottom(), availableMoves);
                //jumpMove.add(cell.getBottom().getBottom());
            }

            if (cell.getTopLeft() == null) {
                addMove(MoveType.EMPTY, cell.getTopLeft(), availableMoves);
            } else if (cell.getTopLeft().getTopLeft() == null) {
                addMove(MoveType.JUMP, cell.getTopLeft().getTopLeft(), availableMoves);
                //jumpMove.add(cell.getTopLeft().getTopLeft());
            }


            if (cell.getTopRight() == null) {
                addMove(MoveType.EMPTY, cell.getTopRight(), availableMoves);
            } else if (cell.getTopRight().getTopRight() == null) {
                addMove(MoveType.JUMP, cell.getTopRight().getTopRight(), availableMoves);
                //jumpMove.add(cell.getTopRight().getTopRight());
            }

            if (cell.getBottomLeft() == null) {
                addMove(MoveType.EMPTY, cell.getBottomLeft(), availableMoves);
            } else if (cell.getBottomLeft().getBottomLeft() == null) {
                addMove(MoveType.JUMP, cell.getBottomLeft().getBottomLeft(), availableMoves);
                //jumpMove.add(cell.getBottomLeft().getBottomLeft());
            }

            if (cell.getBottomRight() == null) {
                addMove(MoveType.EMPTY, cell.getBottomRight(), availableMoves);
            } else if (cell.getBottomRight().getBottomRight() == null) {
                addMove(MoveType.JUMP, cell.getBottomRight().getBottomRight(), availableMoves);
                //jumpMove.add(cell.getBottomRight().getBottomRight());
            }
        }
        return availableMoves;
    }


    @Override
    public boolean isInCamp(Cell cell) {
        return Camp.blackCamp.contains(cell);
    }


    @Override
    public void addMove(MoveType moveType, Cell cell, ArrayList<Move> availableMoves) {
        if (!isInCamp(cell)) {
            availableMoves.add(getMove(moveType, Collections.singletonList(cell)));
        }
    }


    @Override
    public Move getMove(MoveType moveType, List<Cell> cells) {
        return new Move(moveType, cells);
    }


    private ArrayList<Move> getJumpMove(Cell cell) {
        final ArrayList<Jump> jumps = new ArrayList<>();
        getJumpableCells(cell, null, jumps);
        return formMove(cell, jumps);
    }

    private ArrayList<Move> formMove(Cell startingCell, ArrayList<Jump> jumps) {
        ArrayList<Move> jumpMoves = new ArrayList<>();
        HashMap<Coordinates, ArrayList<Cell>> map = new HashMap<>();











        Cell start = startingCell;

        for (int i=0; i<jumps.size(); i++) {
            ArrayList<Move> jumpMove = new ArrayList<>();
            jumpMove.add(new Move(MoveType.JUMP, Arrays.asList(jumps.get(i).getParent(), jumps.get(i).getCurrent())));
            for(int j=i+1; j<jumps.size(); j++) {
                if (jumps.get(j).getParent().getRow() == jumps.get(i).getCurrent().getRow() &&
                        jumps.get(j).getParent().getCol() == jumps.get(i).getCurrent().getCol()) {
                    jumpMove.add(new Move(MoveType.JUMP, Arrays.asList(jumps.get(j).getParent(), jumps.get(j).getCurrent())));
                }
            }
        }

        for (Jump jumpOuter : jumps) {
            for (Jump jump : jumps) {
                ArrayList<Cell> cells = new ArrayList<>();
                if (jump.getParent().getRow() == start.getRow() && jump.getParent().getCol() == start.getCol()) {

                }
                cells.add(jump.getParent());
                cells.add(jump.getCurrent());
                Move move = new Move(MoveType.JUMP, cells);
                jumpMoves.add(move);
            }
        }
        return jumpMoves;
    }
    //public static void main(String[] args) {
    //    ArrayList<ArrayList<String>> boardInput = new ArrayList<>();
    //
    //    ArrayList<String> row1 = new ArrayList<>();
    //    row1.add(".");
    //    row1.add(".");
    //    row1.add(".");
    //    row1.add(".");
    //    row1.add(".");
    //    row1.add(".");
    //    row1.add(".");
    //
    //    ArrayList<String> row2 = new ArrayList<>();
    //    row2.add(".");
    //    row2.add(".");
    //    row2.add(".");
    //    row2.add("B");
    //    row2.add("W");
    //    row2.add(".");
    //    row2.add(".");
    //
    //    ArrayList<String> row3 = new ArrayList<>();
    //    row3.add(".");
    //    row3.add("B");
    //    row3.add("B");
    //    row3.add(",");
    //    row3.add(".");
    //    row3.add(".");
    //    row3.add(".");
    //
    //    ArrayList<String> row4 = new ArrayList<>();
    //    row4.add(".");
    //    row4.add(".");
    //    row4.add(".");
    //    row4.add("W");
    //    row4.add(".");
    //    row4.add("W");
    //    row4.add(".");
    //
    //    ArrayList<String> row5 = new ArrayList<>();
    //    row5.add(".");
    //    row5.add(".");
    //    row5.add(".");
    //    row5.add(".");
    //    row5.add(".");
    //    row5.add(".");
    //    row5.add(".");
    //
    //    boardInput.add(row1);
    //    boardInput.add(row2);
    //    boardInput.add(row3);
    //    boardInput.add(row4);
    //    boardInput.add(row5);
    //
    //    new Halma(boardInput);
    //
    //    ArrayList<Jump> jumpableMoves = new ArrayList<>();
    //    new BlackPlayer().getAvailableJumpableCells(Halma.getCellByCoordinate(new Coordinates(2, 2)), null,
    //            jumpableMoves);
    //    for (Jump jump : jumpableMoves) {
    //        System.out.println(jump.getParent().getRow() + "," + jump.getParent().getCol() + " -> " + jump.getCurrent().getRow() + "," + jump.getCurrent().getCol());
    //    }
    //}


    public void getJumpableCells(Cell cell, Cell parent, ArrayList<Jump> jumpableCells) {
        if (isNotNull(cell.getLeft()) && isNotNull(cell.getLeft().getLeft()) && (
                cell.getLeft().getPlayerType() == PlayerType.BLACK
                        || cell.getLeft().getPlayerType() == PlayerType.WHITE)
                && cell.getLeft().getLeft().getPlayerType() == PlayerType.NONE && !isParentCell(parent,
                cell.getLeft().getLeft())) {
            jumpableCells.add(new Jump(cell, cell.getLeft().getLeft()));
            getJumpableCells(cell.getLeft().getLeft(), cell, jumpableCells);
        }
        if (isNotNull(cell.getRight()) && isNotNull(cell.getRight().getRight()) && (
                cell.getRight().getPlayerType() == PlayerType.BLACK
                        || cell.getRight().getPlayerType() == PlayerType.WHITE)
                && cell.getRight().getRight().getPlayerType() == PlayerType.NONE && !isParentCell(parent,
                cell.getRight().getRight())) {
            jumpableCells.add(new Jump(cell, cell.getRight().getRight()));
            getJumpableCells(cell.getRight().getRight(), cell, jumpableCells);
        }
        if (isNotNull(cell.getTop()) && isNotNull(cell.getTop().getTop()) && (
                cell.getTop().getPlayerType() == PlayerType.BLACK || cell.getTop().getPlayerType() == PlayerType.WHITE)
                && cell.getTop().getTop().getPlayerType() == PlayerType.NONE && !isParentCell(parent,
                cell.getTop().getTop())) {
            jumpableCells.add(new Jump(cell, cell.getTop().getTop()));
            getJumpableCells(cell.getTop().getTop(), cell, jumpableCells);
        }
        if (isNotNull(cell.getBottom()) && isNotNull(cell.getBottom().getBottom()) && (
                cell.getBottom().getPlayerType() == PlayerType.BLACK
                        || cell.getBottom().getPlayerType() == PlayerType.WHITE)
                && cell.getBottom().getBottom().getPlayerType() == PlayerType.NONE && !isParentCell(parent,
                cell.getBottom().getBottom())) {
            jumpableCells.add(new Jump(cell, cell.getBottom().getBottom()));
            getJumpableCells(cell.getBottom().getBottom(), cell, jumpableCells);
        }
        if (isNotNull(cell.getTopLeft()) && isNotNull(cell.getTopLeft().getTopLeft()) && (
                cell.getTopLeft().getPlayerType() == PlayerType.BLACK
                        || cell.getTopLeft().getPlayerType() == PlayerType.WHITE)
                && cell.getTopLeft().getTopLeft().getPlayerType() == PlayerType.NONE && !isParentCell(parent,
                cell.getTopLeft().getTopLeft())) {
            jumpableCells.add(new Jump(cell, cell.getTopLeft().getTopLeft()));
            getJumpableCells(cell.getTopLeft().getTopLeft(), cell, jumpableCells);
        }
        if (isNotNull(cell.getTopRight()) && isNotNull(cell.getTopRight().getTopRight()) && (
                cell.getTopRight().getPlayerType() == PlayerType.BLACK
                        || cell.getTopRight().getPlayerType() == PlayerType.WHITE)
                && cell.getTopRight().getTopRight().getPlayerType() == PlayerType.NONE && !isParentCell(parent,
                cell.getTopRight().getTopRight())) {
            jumpableCells.add(new Jump(cell, cell.getTopRight().getTopRight()));
            getJumpableCells(cell.getTopRight().getTopRight(), cell, jumpableCells);
        }
        if (isNotNull(cell.getBottomLeft()) && isNotNull(cell.getBottomLeft().getBottomLeft()) && (
                cell.getBottomLeft().getPlayerType() == PlayerType.BLACK
                        || cell.getBottomLeft().getPlayerType() == PlayerType.WHITE)
                && cell.getBottomLeft().getBottomLeft().getPlayerType() == PlayerType.NONE && !isParentCell(parent,
                cell.getBottomLeft().getBottomLeft())) {
            jumpableCells.add(new Jump(cell, cell.getBottomLeft().getBottomLeft()));
            getJumpableCells(cell.getBottomLeft().getBottomLeft(), cell, jumpableCells);
        }
        if (isNotNull(cell.getBottomRight()) && isNotNull(cell.getBottomRight().getBottomRight()) && (
                cell.getBottomRight().getPlayerType() == PlayerType.BLACK
                        || cell.getBottomRight().getPlayerType() == PlayerType.WHITE)
                && cell.getBottomRight().getBottomRight().getPlayerType() == PlayerType.NONE && !isParentCell(parent,
                cell.getBottomRight().getBottomRight())) {
            jumpableCells.add(new Jump(cell, cell.getBottomRight().getBottomRight()));
            getJumpableCells(cell.getBottomRight().getBottomRight(), cell, jumpableCells);
        }
    }


    public boolean isNotNull(Object obj) {
        return null != obj;
    }


    public boolean isParentCell(Cell cell1, Cell cell2) {
        if (null == cell1) { return false; }
        return cell1.getRow() == cell2.getRow() && cell1.getCol() == cell2.getCol();
    }
}
