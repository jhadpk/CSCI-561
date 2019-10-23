import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class Controller {
    private static final String INPUT_FILE = "/Users/deepakjha/input.txt";
    private static final String OUTPUT_FILE = "/Users/deepakjha/output.txt";
    private static final String CALIBRATE_OUTPUT = "/Users/deepakjha/calibration.txt";
    private static final String PLAYDATA = "/Users/deepakjha/playdata.txt";

    private static final String BLANK_SPACE = " ";
    private static final String NEW_LINE = "\n";
    private static final String NO_OUTPUT = "";


    protected void calibrate() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
            Input input = validateAndExtractInput(br);
            if (null != input) {
                GameInitializer.init();
                Player adapter = GameInitializer.getPlayer(input);
                generateOutput(null != adapter ? generateOutputMoves(adapter.getNextMove()) : null, CALIBRATE_OUTPUT);
            } else {
                generateOutput(null, OUTPUT_FILE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            generateOutput(null, OUTPUT_FILE);
        }
    }

    protected void play() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
            Input input = validateAndExtractInput(br);
            if (null != input) {
                GameInitializer.init();
                Player adapter = GameInitializer.getPlayer(input);
                generateOutput(null != adapter ? generateOutputMoves(adapter.getNextMove()) : null, OUTPUT_FILE);
            } else {
                generateOutput(null, OUTPUT_FILE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            generateOutput(null, OUTPUT_FILE);
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
            input.setMaxDepth(input.getGameType() == GameType.SINGLE ? 5 : 3);
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
        //final ArrayList<ArrayList<String>> transposedBoard = transpose(boardConfiguration);
        //
        //for (int i=0; i<transposedBoard.size(); i++) {
        //    for (int j=0; j<transposedBoard.get(i).size(); j++) {
        //        System.out.print(transposedBoard.get(i).get(j)+",");
        //    }
        //    System.out.println();
        //}

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


    private ArrayList<Output> generateOutputMoves(final Move optimalMove) {
        if (null == optimalMove) {
            return null;
        }
        ArrayList<Output> outputMoves = new ArrayList<>();
        if (optimalMove.getMoveType().equals(MoveType.JUMP)) {
            for (int i = 1; i < optimalMove.getPath().size(); i++) {
                List<Cell> path = Arrays.asList(optimalMove.getPath().get(i - 1), optimalMove.getPath().get(i));
                outputMoves.add(new Output(optimalMove.getMoveType(), path.get(0), path.get(1)));
            }
        } else {
            outputMoves.add(new Output(optimalMove.getMoveType(), optimalMove.getStartingCell(),
                    optimalMove.getDestinationCell()));
        }
        return outputMoves;
    }


    private void generateOutput(final ArrayList<Output> optimalMoves, String outputFile) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(outputFile, false);
            if (null != optimalMoves && optimalMoves.size() != 0) {
                StringBuilder output = new StringBuilder();
                for (Output move : optimalMoves) {
                    if (move.isOutputNotNull()) {
                        output.append(move.getMoveType().getMoveCode()).append(BLANK_SPACE).append(getMove(
                                move.getStartingCell()).append(BLANK_SPACE).append(getMove(move.getDestinationCell()))
                                .append(NEW_LINE));
                    }
                }
                //System.out.println(output.toString());
                fw.write(output.substring(0, output.toString().length() - 1));
            } else {
                //System.out.println("No move found");
                fw.write(NO_OUTPUT);
            }
        } catch (IOException e) {
            try {
                System.out.println("IOException occurred");
                fw.write(NO_OUTPUT);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {fw.close();} catch (Exception ex) {System.out.println("Exception in closing fw");}
        }
    }


    private StringBuilder getMove(Cell cell) {
        StringBuilder outputText = new StringBuilder();
        outputText.append(cell.getRow()).append(",").append(cell.getCol());
        return outputText;
    }
}
