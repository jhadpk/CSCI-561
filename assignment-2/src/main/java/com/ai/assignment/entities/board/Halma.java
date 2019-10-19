package com.ai.assignment.entities.board;

import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.enums.PlayerType;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author deepakjha on 10/14/19
 * @project ai-assignments
 */

public class Halma {
    private static ArrayList<ArrayList<Cell>> board;
    private static HashMap<Coordinates, Cell> coordinatesCellMap;


    public Halma(ArrayList<ArrayList<String>> boardInput) {
        board = new ArrayList<>();
        coordinatesCellMap = new HashMap<>();
        for (int row = 0; row < boardInput.size(); row++) {
            final ArrayList<Cell> boardRow = new ArrayList<>();
            for (int col = 0; col < boardInput.get(row).size(); col++) {
                Cell cell = new Cell(row, col);
                cell.setPlayerType(PlayerType.getPlayerByValue(boardInput.get(row).get(col)));
                boardRow.add(cell);
                coordinatesCellMap.put(new Coordinates(row, col), cell);
            }
            board.add(boardRow);
        }
    }


    public ArrayList<ArrayList<Cell>> getBoard() {
        return board;
    }


    public static Cell getLeft(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (col > 0) {
            col -= 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getRight(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (col < 15) {
            col += 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getTop(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row > 0) {
            row -= 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getBottom(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row < 15) {
            row += 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getTopLeft(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row > 0 && col > 0) {
            row -= 1;
            col -= 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getTopRight(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row > 0 && col < 15) {
            row -= 1;
            col += 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getBottomLeft(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row < 15 && col > 0) {
            row += 1;
            col -= 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getBottomRight(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row < 15 && col < 15) {
            row += 1;
            col += 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getCellByCoordinate(final Coordinates coordinates) {
        return board.get(coordinates.getRow()).get(coordinates.getCol());
    }


    public static void makeMove(Move move) {
        int sRow = move.getStartingCell().getRow();
        int sCol = move.getStartingCell().getCol();
        int dRow = move.getDestinationCell().getRow();
        int dCol = move.getDestinationCell().getCol();
        if (board.get(sRow).get(sCol).getPlayerType().equals(move.getPlayerType()) && board.get(dRow).get(dCol)
                .getPlayerType().equals(PlayerType.NONE)) {
            board.get(sRow).get(sCol).setPlayerType(PlayerType.NONE);
            board.get(dRow).get(dCol).setPlayerType(move.getPlayerType());
        }
    }


    public static void undoMove(Move move) {
        int sRow = move.getStartingCell().getRow();
        int sCol = move.getStartingCell().getCol();
        int dRow = move.getDestinationCell().getRow();
        int dCol = move.getDestinationCell().getCol();
        if (board.get(sRow).get(sCol).getPlayerType().equals(PlayerType.NONE) && board.get(dRow).get(dCol)
                .getPlayerType().equals(move.getPlayerType())) {
            board.get(sRow).get(sCol).setPlayerType(move.getPlayerType());
            board.get(dRow).get(dCol).setPlayerType(PlayerType.NONE);
        }
    }


    /***
     * Improve the complexity
     */
    public static int calculateBoardSituation(final PlayerType playerType) {
        int heuristic = 0;
        for (ArrayList<Cell> row : board) {
            for (Cell tile : row) {
                if (tile.getPlayerType() == playerType) {
                    heuristic = heuristic + calculateDistanceFromCorner(playerType, tile);
                }
            }
        }
        return heuristic;
    }


    private static int calculateDistanceFromCorner(final PlayerType playerType, final Cell cell) {
        final Coordinates goal = playerType == PlayerType.WHITE ? new Coordinates(0, 0) : new Coordinates(15, 15);
        return 100 - (Math.abs(cell.getRow() - goal.getRow()) + Math.abs(cell.getCol() - goal.getCol()));
    }
}
