package com.ai.assignment;

import com.ai.assignment.api.game.Game;
import com.ai.assignment.api.game.GameInitializer;
import com.ai.assignment.entities.Input;
import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.board.Cell;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class Controller {
    private static final String INPUT_FILE = "/Users/deepakjha/assignment2/input.txt";
    private static final String OUTPUT_FILE = "/Users/deepakjha/assignment2/output.txt";
    private static final String BLANK_SPACE = " ";
    private static final String NEW_LINE = "\n";
    private static final String FAIL = "FAIL";


    protected void play() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
            Input input = validateAndExtractInput(br);
            if (null != input) {
                GameInitializer.init();
                Game adapter = GameInitializer.getPlayer(input);
                generateOutput(null != adapter ? adapter.getNextMove(input) : null);
            } else {
                generateOutput(null);
            }
        } catch (Exception e) {
            generateOutput(null);
        }
    }


    private Input validateAndExtractInput(final BufferedReader br) {
        try {
            Input input = new Input();
            input.setGameType(br.readLine());
            input.setPlayerType(br.readLine());
            input.setTimeRemainingInSeconds(br.readLine());
            input.setHalma(getBoardConfig(br));
            input.setBoard(input.getHalma().getBoard());
            return input;
        } catch (IOException e) {
            return null;
        }
    }


    private ArrayList<ArrayList<String>> getBoardConfig(final BufferedReader br) throws IOException {
        ArrayList<ArrayList<String>> boardConfiguration = new ArrayList<>();
        String line;
        int y = 0;
        while ((line = br.readLine()) != null) {
            if (y < 16) {
                final ArrayList<String> row = new ArrayList<>();
                final String[] input = line.split("");
                int x = 0;
                for (String z : input) {
                    if (z != null && z.length() != 0 && !z.equals(BLANK_SPACE) && x < 16) {
                        row.add(z.trim());
                        x++;
                    }
                }
                boardConfiguration.add(row);
                y++;
            }
        }
        return transpose(boardConfiguration);
    }


    private ArrayList<ArrayList<String>> transpose(ArrayList<ArrayList<String>> givenBoard) {
        ArrayList<ArrayList<String>> transposedMap = new ArrayList<>();
        if (!givenBoard.isEmpty()) {
            int rowSize = givenBoard.get(0).size();
            for (int i = 0; i < rowSize; i++) {
                ArrayList<String> col = new ArrayList<>();
                for (ArrayList<String> row : givenBoard) {
                    col.add(row.get(i));
                }
                transposedMap.add(col);
            }
        }
        return transposedMap;
    }


    private void generateOutput(final ArrayList<Move> optimalMoves) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(OUTPUT_FILE, false);
            if (null != optimalMoves && optimalMoves.size() != 0) {
                StringBuilder output = new StringBuilder();
                for (Move move : optimalMoves) {
                    if (null != move.getMove() && 0 != move.getPath().size()) {
                        output.append(move.getMove().move).append(BLANK_SPACE).append(getMove(move.getPath())).append(
                                NEW_LINE);
                    }
                }
                fw.write(output.substring(0, output.toString().length() - 1));
            } else {
                fw.write(FAIL);
            }
        } catch (IOException e) {
            try {
                fw.write(FAIL);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {fw.close();} catch (Exception ex) {System.out.println("Exception in closing fw");}
        }
    }


    private String getMove(List<Cell> cells) {
        StringBuilder outputText = new StringBuilder();
        for (Cell cell : cells) {
            outputText.append(cell.getRow()).append(",").append(cell.getCol()).append(BLANK_SPACE);
        }

        return outputText.toString();
    }
}
