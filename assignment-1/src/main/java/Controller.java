import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author deepakjha on 9/14/19
 * @project ai-assignments
 */
public class Controller {
    private static final String INPUT_FILE = "/Users/deepakjha/input.txt";
    private static final String OUTPUT_FILE = "/Users/deepakjha/output.txt";
    private static final String BLANK_SPACE = " ";
    private static final String FAIL = "FAIL";


    protected void startSearch() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
            Input input = validateAndExtractInput(br);
            if (null != input) {
                ServiceInitializer.init();
                SearchService adapter = ServiceInitializer.getSearchAdapter(input);
                generateOutput(null != adapter ? adapter.getOptimalPathToTargets() : null);
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

            input.setSearchType(SearchType.getById(br.readLine()));

            final List<String> mapSize = Arrays.asList(br.readLine().trim().split(BLANK_SPACE));
            if (mapSize.size() == 2 && Integer.parseInt(mapSize.get(0)) > 0 && Integer.parseInt(mapSize.get(1)) > 0) {
                input.setMapSize(Arrays.asList(Integer.parseInt(mapSize.get(0)), Integer.parseInt(mapSize.get(1))));
            }

            final List<String> landingCell = Arrays.asList(br.readLine().trim().split(BLANK_SPACE));
            if (landingCell.size() == 2 && Integer.parseInt(landingCell.get(0)) >= 0 && Integer.parseInt(
                    landingCell.get(1)) >= 0) {
                input.setLandingCell(
                        new Cell(Integer.parseInt(landingCell.get(0)), Integer.parseInt(landingCell.get(1))));
            }

            final String allowedSteepness = br.readLine().trim();
            if (Integer.parseInt(allowedSteepness) >= 0) {
                input.setAllowedSteepness(Integer.parseInt(allowedSteepness));
            }

            final String targetCount = br.readLine().trim();
            if (Integer.parseInt(targetCount) > 0) {
                input.setTargetCount(Integer.parseInt(targetCount));
                input.setTargetList(getTargetList(br, input));
            }

            if (null != input.getMapSize()) {
                input.setElevationMap(getElevationMap(br, input));
            }
            return input;
        } catch (IOException | NumberFormatException e) {
            return null;
        }
    }


    private ArrayList<Cell> getTargetList(final BufferedReader br, final Input input) throws IOException {
        ArrayList<Cell> targetList = new ArrayList<>();
        try {
            for (int i = 0; i < input.getTargetCount(); i++) {
                final List<String> target = Arrays.asList(br.readLine().trim().split(BLANK_SPACE));
                targetList.add(target.size() == 2 && Integer.parseInt(target.get(0)) >= 0
                        && Integer.parseInt(target.get(1)) >= 0 ? new Cell(Integer.parseInt(target.get(0)), Integer.parseInt(target.get(1))) : null);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return targetList;
    }


    private ArrayList<ArrayList<Integer>> getElevationMap(final BufferedReader br, final Input input)
            throws IOException {
        ArrayList<ArrayList<Integer>> elevationMap = new ArrayList<>();
        String line;
        int y = 0;
        while ((line = br.readLine()) != null) {
            if (y < input.getMapSize().get(1)) {
                final ArrayList<Integer> zList = new ArrayList<>();
                line = line.trim().replaceAll(" +", BLANK_SPACE);
                final String[] zInput = line.split(BLANK_SPACE);
                int x = 0;
                for (String z : zInput) {
                    if (z != null && z.length() != 0 && !z.equals(BLANK_SPACE) && x < input.getMapSize().get(0)) {
                        zList.add(Integer.parseInt(z.trim()));
                        x++;
                    }
                }
                elevationMap.add(zList);
                y++;
            }
        }
        return transpose(elevationMap);
    }


    private ArrayList<ArrayList<Integer>> transpose(ArrayList<ArrayList<Integer>> givenMap) {
        ArrayList<ArrayList<Integer>> transposedMap = new ArrayList<>();
        if (!givenMap.isEmpty()) {
            int rowSize = givenMap.get(0).size();
            for (int i = 0; i < rowSize; i++) {
                ArrayList<Integer> col = new ArrayList<>();
                for (ArrayList<Integer> row : givenMap) {
                    col.add(row.get(i));
                }
                transposedMap.add(col);
            }
        }
        return transposedMap;
    }


    private void generateOutput(final ArrayList<ArrayList<Cell>> optimalPaths) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(OUTPUT_FILE, false);
            if (null != optimalPaths && optimalPaths.size() != 0) {
                StringBuilder output = new StringBuilder();
                for (ArrayList<Cell> optimalPath : optimalPaths) {
                    output.append(getPath(optimalPath));
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


    private String getPath(ArrayList<Cell> path) {
        StringBuilder outputText = new StringBuilder();
        if (path == null) {
            outputText = new StringBuilder(FAIL);
        } else {
            for (Cell cell : path) {
                outputText.append(cell.getX()).append(",").append(cell.getY()).append(BLANK_SPACE);
            }
        }
        return outputText.append("\n").toString();
    }
}
