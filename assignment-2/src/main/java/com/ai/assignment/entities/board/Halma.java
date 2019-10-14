package com.ai.assignment.entities.board;

import com.ai.assignment.entities.PlayerType;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ai.assignment.entities.PlayerType.getPlayerType;


/**
 * @author deepakjha on 10/14/19
 * @project ai-assignments
 */
public class Halma {
    private ArrayList<ArrayList<Cell>> board;
    private PlayerType currentPlayer;

    private static HashMap<Coordinates, Cell> coordinatesCellMap;

    public Halma(ArrayList<ArrayList<String>> boardInput) {
        board = new ArrayList<>();
        coordinatesCellMap = new HashMap<>();
        for (int row = 0; row < boardInput.size(); row++) {
            final ArrayList<Cell> boardRow = new ArrayList<>();
            for (int col = 0; col < boardInput.get(row).size(); col++) {
                Cell cell = new Cell(row, col, getPlayerType(boardInput.get(row).get(col)));
                boardRow.add(cell);
                coordinatesCellMap.put(new Coordinates(row, col), cell);
            }
            board.add(boardRow);
        }
    }

    public ArrayList<ArrayList<Cell>> getBoard() {
        return this.board;
    }

    public void setCurrentPlayer(final PlayerType playerType) {
        this.currentPlayer = playerType;
    }

    public PlayerType getCurrentPlayer() {
        return this.currentPlayer;
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
        return coordinatesCellMap.get(coordinates);
    }
}
