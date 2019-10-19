package com.ai.assignment.entities;

import com.ai.assignment.entities.board.Cell;
import com.ai.assignment.entities.board.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ai.assignment.entities.board.Halma.getCellByCoordinate;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class Camp {
    public static List<String> blackCamp;
    public static List<Cell> blackCampCells = new ArrayList<>();

    public static List<String> whiteCamp;
    public static List<Cell> whiteCampCells = new ArrayList<>();

    static {
        blackCamp = Arrays.asList("0,0", "0,1", "0,2", "0,3", "0,4", "1,0", "1,1", "1,2", "1,3", "1,4", "2,0", "2,1",
                "2,2", "2,3", "3,0", "3,1", "3,2", "4,0", "4,1");

        whiteCamp = Arrays.asList("11,14", "11,15", "12,13", "12,14", "12,15", "13,12", "13,13", "13,14", "13,15",
                "14,11", "14,12", "14,13", "14,14", "14,15", "15,11", "15,12", "15,13", "15,14", "15,15");

        for (String coordinate : blackCamp) {
            blackCampCells.add(getCellByCoordinate(new Coordinates(coordinate)));
        }
        for (String coordinate : whiteCamp) {
            whiteCampCells.add(getCellByCoordinate(new Coordinates(coordinate)));
        }
    }
}
