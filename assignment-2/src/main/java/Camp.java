import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class Camp {
    public static List<String> blackCamp;
    public static List<Cell> blackCampCells = new ArrayList<>();
    public static List<String> whiteCamp;
    public static List<Cell> whiteCampCells = new ArrayList<>();

    public static List<String> diagonalPath;

    static {
        blackCamp = Arrays.asList("0,0", "0,1", "0,2", "0,3", "0,4", "1,0", "1,1", "1,2", "1,3", "1,4", "2,0", "2,1",
                "2,2", "2,3", "3,0", "3,1", "3,2", "4,0", "4,1");

        whiteCamp = Arrays.asList("11,14", "11,15", "12,13", "12,14", "12,15", "13,12", "13,13", "13,14", "13,15",
                "14,11", "14,12", "14,13", "14,14", "14,15", "15,11", "15,12", "15,13", "15,14", "15,15");

        diagonalPath = Arrays.asList("1,5", "2,4", "2,5", "2,6", "3,3", "3,4", "3,5", "3,6", "3,7", "4,2", "4,3", "4,4",
                "4,5", "4,6", "4,7", "4,8", "5,1", "5,2", "5,3", "5,4", "5,5", "5,6", "5,7", "5,8", "5,9", "6,2", "6,3",
                "6,4", "6,5", "6,6", "6,7", "6,8", "6,9", "6,10", "7,3", "7,4", "7,5", "7,6", "7,7", "7,8", "7,9",
                "7,10", "7,11", "8,4", "8,5", "8,6", "8,7", "8,8", "8,9", "8,10", "8,11", "8,12", "9,5", "9,6", "9,7",
                "9,8", "9,9", "9,10", "9,11", "9,12", "9,13", "10,6", "10,7", "10,8", "10,9", "10,10", "10,11", "10,12",
                "10,13", "10,14", "11,7", "11,8", "11,9", "11,10", "11,11", "11,12", "11,13", "12,8", "12,9", "12,10",
                "12,11", "12,12", "13,9", "13,10", "13,11", "14,10");
                //"0,5", "1,6", "2,7", "3,8", "4,9", "5,10", "6,11", "7,12", "8,13", "9,14", "10,15",
                //"5,0", "6,1", "7,2", "8,3", "9,4", "10,5", "11,6", "12,7", "13,8", "14,9", "15,10");

        //diagonalPath = Arrays.asList("2,4", "2,5", "3,3", "3,4", "3,5", "3,6", "4,2", "4,3", "4,4",
        //        "4,5", "4,6", "4,7", "5,2", "5,3", "5,4", "5,5", "5,6", "5,7", "5,8", "6,3",
        //        "6,4", "6,5", "6,6", "6,7", "6,8", "6,9", "7,4", "7,5", "7,6", "7,7", "7,8", "7,9",
        //        "7,10", "8,5", "8,6", "8,7", "8,8", "8,9", "8,10", "8,11", "9,6", "9,7",
        //        "9,8", "9,9", "9,10", "9,11", "9,12", "10,7", "10,8", "10,9", "10,10", "10,11", "10,12",
        //        "10,13", "11,8", "11,9", "11,10", "11,11", "11,12", "11,13", "12,9", "12,10",
        //        "12,11", "12,12", "13,10", "13,11");

        for (String coordinate : blackCamp) {
            blackCampCells.add(Halma.getCellByCoordinate(new Coordinates(coordinate)));
        }
        for (String coordinate : whiteCamp) {
            whiteCampCells.add(Halma.getCellByCoordinate(new Coordinates(coordinate)));
        }
    }

    public static List<String> getOpposingCampCoordinates(final PlayerType playerType) {
        return playerType == PlayerType.WHITE ? blackCamp : whiteCamp;
    }


    public static ArrayList<Cell> getAvailablePositionsInOpposition(final PlayerType playerType) {
        ArrayList<Cell> availablePositions = new ArrayList<>();
        switch (playerType) {
            case BLACK:
                for (Cell cell : whiteCampCells) {
                    if (cell.getPlayerType() == PlayerType.NONE) {
                        availablePositions.add(cell);
                    }
                }
                return availablePositions;
            case WHITE:
                for (Cell cell : blackCampCells) {
                    if (cell.getPlayerType() == PlayerType.NONE) {
                        availablePositions.add(cell);
                    }
                }
                return availablePositions;
            default:
                return availablePositions;
        }
    }
}
