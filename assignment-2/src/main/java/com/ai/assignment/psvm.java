package com.ai.assignment;

import com.ai.assignment.entities.MoveToPlay;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * @author deepakjha on 10/15/19
 * @project ai-assignments
 */
public class psvm {


    public static void main(String[] args) {

        ArrayList<MoveToPlay> ar = new ArrayList<>();
        MoveToPlay m = new MoveToPlay(10, null);
        MoveToPlay n = new MoveToPlay(6, null);
        MoveToPlay o = new MoveToPlay(15, null);
        MoveToPlay p = new MoveToPlay(12, null);

        ar.add(m);
        ar.add(n);
        ar.add(o);
        ar.add(p);

        ar.sort(Comparator.comparing(MoveToPlay::getHeuristic).reversed());


        for (MoveToPlay mtp : ar) {
            System.out.println(mtp.getHeuristic());
        }

        //ArrayList<ArrayList<String>> boardInput = new ArrayList<>();
        //
        //ArrayList<String> row1 = new ArrayList<>();
        //row1.add(".");
        //row1.add(".");
        //row1.add(".");
        //row1.add(".");
        //row1.add(".");
        //row1.add(".");
        //row1.add(".");
        //
        //ArrayList<String> row2 = new ArrayList<>();
        //row2.add(".");
        //row2.add(".");
        //row2.add(".");
        //row2.add("B");
        //row2.add("W");
        //row2.add(".");
        //row2.add(".");
        //
        //ArrayList<String> row3 = new ArrayList<>();
        //row3.add(".");
        //row3.add("B");
        //row3.add("B");
        //row3.add(",");
        //row3.add(".");
        //row3.add(".");
        //row3.add(".");
        //
        //ArrayList<String> row4 = new ArrayList<>();
        //row4.add(".");
        //row4.add(".");
        //row4.add(".");
        //row4.add("W");
        //row4.add(".");
        //row4.add("W");
        //row4.add(".");
        //
        //ArrayList<String> row5 = new ArrayList<>();
        //row5.add(".");
        //row5.add(".");
        //row5.add(".");
        //row5.add(".");
        //row5.add(".");
        //row5.add(".");
        //row5.add(".");
        //
        //boardInput.add(row1);
        //boardInput.add(row2);
        //boardInput.add(row3);
        //boardInput.add(row4);
        //boardInput.add(row5);
        //
        //new Halma(boardInput);
        //
        //ArrayList<Jump> jumpableMoves = new ArrayList<>();
        //
        //new BlackPlayer().getJumps(Halma.getCellByCoordinate(new Coordinates(2, 2)), null, jumpableMoves);
        //
        //Cell[][] parentInfo = new BlackPlayer().getParentInfo(jumpableMoves, 5, 7);
        //
        //
        //for (int i = 0; i < 5; i++) {
        //    for (int j = 0; j < 7; j++) {
        //        if (null != parentInfo[i][j]) {
        //            System.out.println();
        //            System.out.print(i + "," + j + " ----> ");
        //            ArrayList<Cell> path = new BlackPlayer().retracePath(parentInfo, new Cell(2, 2),
        //                    new Cell(i, j), parentInfo[i][j]);
        //
        //            for (Cell cell : path) {
        //                System.out.print(cell.getRow() + "," + cell.getCol() + "   ");
        //            }
        //        }
        //    }
        //}

    }

}
