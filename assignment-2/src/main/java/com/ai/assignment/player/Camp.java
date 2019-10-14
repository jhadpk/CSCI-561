package com.ai.assignment.player;

import com.ai.assignment.entities.board.Cell;
import com.ai.assignment.entities.PlayerType;

import java.util.ArrayList;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class Camp {
    public static ArrayList<Cell> blackCamp = new ArrayList<>();
    public static ArrayList<Cell> whiteCamp = new ArrayList<>();

    static {
        blackCamp.add(new Cell(0,0, PlayerType.BLACK));
        blackCamp.add(new Cell(1,0, PlayerType.BLACK));
        blackCamp.add(new Cell(2,0, PlayerType.BLACK));
        blackCamp.add(new Cell(3,0, PlayerType.BLACK));
        blackCamp.add(new Cell(4,0, PlayerType.BLACK));
        blackCamp.add(new Cell(0,1, PlayerType.BLACK));
        blackCamp.add(new Cell(1,1, PlayerType.BLACK));
        blackCamp.add(new Cell(2,1, PlayerType.BLACK));
        blackCamp.add(new Cell(3,1, PlayerType.BLACK));
        blackCamp.add(new Cell(4,1, PlayerType.BLACK));
        blackCamp.add(new Cell(0,2, PlayerType.BLACK));
        blackCamp.add(new Cell(1,2, PlayerType.BLACK));
        blackCamp.add(new Cell(2,2, PlayerType.BLACK));
        blackCamp.add(new Cell(3,2, PlayerType.BLACK));
        blackCamp.add(new Cell(0,3, PlayerType.BLACK));
        blackCamp.add(new Cell(1,3, PlayerType.BLACK));
        blackCamp.add(new Cell(2,3, PlayerType.BLACK));
        blackCamp.add(new Cell(0,4, PlayerType.BLACK));
        blackCamp.add(new Cell(1,4, PlayerType.BLACK));
    }

    static {
        whiteCamp.add(new Cell(14,11, PlayerType.WHITE));
        whiteCamp.add(new Cell(15,11, PlayerType.WHITE));
        whiteCamp.add(new Cell(13,12, PlayerType.WHITE));
        whiteCamp.add(new Cell(14,12, PlayerType.WHITE));
        whiteCamp.add(new Cell(15,12, PlayerType.WHITE));
        whiteCamp.add(new Cell(12,13, PlayerType.WHITE));
        whiteCamp.add(new Cell(13,13, PlayerType.WHITE));
        whiteCamp.add(new Cell(14,13, PlayerType.WHITE));
        whiteCamp.add(new Cell(15,13, PlayerType.WHITE));
        whiteCamp.add(new Cell(11,14, PlayerType.WHITE));
        whiteCamp.add(new Cell(12,14, PlayerType.WHITE));
        whiteCamp.add(new Cell(13,14, PlayerType.WHITE));
        whiteCamp.add(new Cell(14,14, PlayerType.WHITE));
        whiteCamp.add(new Cell(15,14, PlayerType.WHITE));
        whiteCamp.add(new Cell(11,15, PlayerType.WHITE));
        whiteCamp.add(new Cell(12,15, PlayerType.WHITE));
        whiteCamp.add(new Cell(13,15, PlayerType.WHITE));
        whiteCamp.add(new Cell(14,15, PlayerType.WHITE));
        whiteCamp.add(new Cell(15,15, PlayerType.WHITE));
    }
}
