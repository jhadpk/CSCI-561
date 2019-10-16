package com.ai.assignment.entities.board;


import com.ai.assignment.entities.Camp;
import com.ai.assignment.entities.enums.PlayerType;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class Cell {
    private int row;
    private int col;
    private PlayerType playerType;

    private int distance;


    public Cell(final int row, final int col) {
        this.row = row;
        this.col = col;
    }


    public int getRow() {
        return this.row;
    }


    public int getCol() {
        return this.col;
    }


    public void setPlayerType(final PlayerType playerType) {
        this.playerType = playerType;
    }


    public PlayerType getPlayerType() {
        return this.playerType;
    }


    public void setDistance(int distance) {
        this.distance = distance;
    }


    public int getDistance(final PlayerType playerType) {
        int distance = 100000000; //setting to very high value
        switch (playerType) {
            case BLACK:
                if (Camp.whiteCamp.contains(this.getRow() + "," + this.getCol())) {
                    return 0;
                }
                for (String coord : Camp.whiteCamp) {
                    final int cellRow = Integer.parseInt(coord.split(",")[0]);
                    final int cellCol = Integer.parseInt(coord.split(",")[1]);
                    final int manhattenDist = Math.abs(this.getRow() - cellRow + this.getCol() - cellCol);
                    if (manhattenDist < distance) {
                        distance = manhattenDist;
                    }
                }
                return distance;
            case WHITE:
                if (Camp.blackCamp.contains(this.getRow() + "," + this.getCol())) {
                    return 0;
                }
                for (String coord : Camp.blackCamp) {
                    final int cellRow = Integer.parseInt(coord.split(",")[0]);
                    final int cellCol = Integer.parseInt(coord.split(",")[1]);
                    final int manhattenDist = Math.abs(this.getRow() - cellRow + this.getCol() - cellCol);
                    if (manhattenDist < distance) {
                        distance = manhattenDist;
                    }
                }
                return distance;
            default:
                return 0;
        }
    }


    public Cell getLeft() {
        return Halma.getLeft(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getRight() {
        return Halma.getRight(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getTop() {
        return Halma.getTop(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getBottom() {
        return Halma.getBottom(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getTopLeft() {
        return Halma.getTopLeft(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getTopRight() {
        return Halma.getTopRight(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getBottomLeft() {
        return Halma.getBottomLeft(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getBottomRight() {
        return Halma.getBottomRight(new Coordinates(this.getRow(), this.getCol()));
    }

}
