package com.ai.assignment.player;


import com.ai.assignment.entities.board.Cell;
import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.MoveType;
import com.ai.assignment.entities.board.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */


public abstract class Player {

    protected HashMap<Coordinates, ArrayList<Cell>> jumps = new HashMap<>();

    public abstract ArrayList<Move> getAvailableMoves(Cell cell);

    public abstract boolean isInCamp(Cell cell);

    public abstract void addMove(MoveType moveType, Cell cell, ArrayList<Move> availableMoves);

    public abstract Move getMove(MoveType moveType, List<Cell> cells);

}
