package com.ai.assignment.player;


import com.ai.assignment.entities.MoveType;
import com.ai.assignment.entities.board.Cell;
import com.ai.assignment.entities.Move;

import java.util.ArrayList;
import java.util.List;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class WhitePlayer extends Player {


    @Override
    public ArrayList<Move> getAvailableMoves(Cell cell) {
        return null;
    }


    @Override
    public boolean isInCamp(Cell cell) {
        return false;
    }


    @Override
    public void addMove(MoveType moveType, Cell cell, ArrayList<Move> availableMoves) {

    }


    @Override
    public Move getMove(MoveType moveType, List<Cell> cells) {
        return null;
    }
}
