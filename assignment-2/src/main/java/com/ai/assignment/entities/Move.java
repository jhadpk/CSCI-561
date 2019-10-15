package com.ai.assignment.entities;

import com.ai.assignment.entities.board.Cell;

import java.util.List;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */

public class Move {
    private MoveType moveType;
    private List<Cell> cells;

    public Move(MoveType moveType, List<Cell> cells) {
        this.moveType = moveType;
        this.cells = cells;
    }

    public MoveType getMove() {
        return this.moveType;
    }

    public List<Cell> getCells() {
        return this.cells;
    }

}
