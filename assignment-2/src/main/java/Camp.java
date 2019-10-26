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

    public static List<Coordinates> diagonalPath = new ArrayList<>();

    static {
        blackCamp = Arrays.asList("0,0", "0,1", "0,2", "0,3", "0,4", "1,0", "1,1", "1,2", "1,3", "1,4", "2,0", "2,1",
                "2,2", "2,3", "3,0", "3,1", "3,2", "4,0", "4,1");

        whiteCamp = Arrays.asList("11,14", "11,15", "12,13", "12,14", "12,15", "13,12", "13,13", "13,14", "13,15",
                "14,11", "14,12", "14,13", "14,14", "14,15", "15,11", "15,12", "15,13", "15,14", "15,15");


        for (String coordinate : blackCamp) {
            blackCampCells.add(Halma.getCellByCoordinate(new Coordinates(coordinate)));
        }
        for (String coordinate : whiteCamp) {
            whiteCampCells.add(Halma.getCellByCoordinate(new Coordinates(coordinate)));
        }

        diagonalPath.add(new Coordinates(1, 5));
        diagonalPath.add(new Coordinates(2, 4));
        diagonalPath.add(new Coordinates(2, 5));
        diagonalPath.add(new Coordinates(2, 6));
        diagonalPath.add(new Coordinates(3, 3));
        diagonalPath.add(new Coordinates(3, 4));
        diagonalPath.add(new Coordinates(3, 5));
        diagonalPath.add(new Coordinates(3, 6));
        diagonalPath.add(new Coordinates(3, 7));
        diagonalPath.add(new Coordinates(4, 2));
        diagonalPath.add(new Coordinates(4, 3));
        diagonalPath.add(new Coordinates(4, 4));
        diagonalPath.add(new Coordinates(4, 5));
        diagonalPath.add(new Coordinates(4, 6));
        diagonalPath.add(new Coordinates(4, 7));
        diagonalPath.add(new Coordinates(4, 8));
        diagonalPath.add(new Coordinates(5, 1));
        diagonalPath.add(new Coordinates(5, 2));
        diagonalPath.add(new Coordinates(5, 3));
        diagonalPath.add(new Coordinates(5, 4));
        diagonalPath.add(new Coordinates(5, 5));
        diagonalPath.add(new Coordinates(5, 6));
        diagonalPath.add(new Coordinates(5, 7));
        diagonalPath.add(new Coordinates(5, 8));
        diagonalPath.add(new Coordinates(5, 9));
        diagonalPath.add(new Coordinates(6, 2));
        diagonalPath.add(new Coordinates(6, 3));
        diagonalPath.add(new Coordinates(6, 4));
        diagonalPath.add(new Coordinates(6, 5));
        diagonalPath.add(new Coordinates(6, 6));
        diagonalPath.add(new Coordinates(6, 7));
        diagonalPath.add(new Coordinates(6, 8));
        diagonalPath.add(new Coordinates(6, 9));
        diagonalPath.add(new Coordinates(6, 10));
        diagonalPath.add(new Coordinates(7, 3));
        diagonalPath.add(new Coordinates(7, 4));
        diagonalPath.add(new Coordinates(7, 5));
        diagonalPath.add(new Coordinates(7, 6));
        diagonalPath.add(new Coordinates(7, 7));
        diagonalPath.add(new Coordinates(7, 8));
        diagonalPath.add(new Coordinates(7, 9));
        diagonalPath.add(new Coordinates(7, 10));
        diagonalPath.add(new Coordinates(7, 11));
        diagonalPath.add(new Coordinates(8, 4));
        diagonalPath.add(new Coordinates(8, 5));
        diagonalPath.add(new Coordinates(8, 6));
        diagonalPath.add(new Coordinates(8, 7));
        diagonalPath.add(new Coordinates(8, 8));
        diagonalPath.add(new Coordinates(8, 9));
        diagonalPath.add(new Coordinates(8, 10));
        diagonalPath.add(new Coordinates(8, 11));
        diagonalPath.add(new Coordinates(8, 12));
        diagonalPath.add(new Coordinates(9, 5));
        diagonalPath.add(new Coordinates(9, 6));
        diagonalPath.add(new Coordinates(9, 7));
        diagonalPath.add(new Coordinates(9, 8));
        diagonalPath.add(new Coordinates(9, 9));
        diagonalPath.add(new Coordinates(9, 10));
        diagonalPath.add(new Coordinates(9, 11));
        diagonalPath.add(new Coordinates(9, 12));
        diagonalPath.add(new Coordinates(9, 13));
        diagonalPath.add(new Coordinates(10, 6));
        diagonalPath.add(new Coordinates(10, 7));
        diagonalPath.add(new Coordinates(10, 8));
        diagonalPath.add(new Coordinates(10, 9));
        diagonalPath.add(new Coordinates(10, 10));
        diagonalPath.add(new Coordinates(10, 11));
        diagonalPath.add(new Coordinates(10, 12));
        diagonalPath.add(new Coordinates(10, 13));
        diagonalPath.add(new Coordinates(10, 14));
        diagonalPath.add(new Coordinates(11, 7));
        diagonalPath.add(new Coordinates(11, 8));
        diagonalPath.add(new Coordinates(11, 9));
        diagonalPath.add(new Coordinates(11, 10));
        diagonalPath.add(new Coordinates(11, 11));
        diagonalPath.add(new Coordinates(11, 12));
        diagonalPath.add(new Coordinates(11, 13));
        diagonalPath.add(new Coordinates(12, 8));
        diagonalPath.add(new Coordinates(12, 9));
        diagonalPath.add(new Coordinates(12, 10));
        diagonalPath.add(new Coordinates(12, 11));
        diagonalPath.add(new Coordinates(12, 12));
        diagonalPath.add(new Coordinates(13, 9));
        diagonalPath.add(new Coordinates(13, 10));
        diagonalPath.add(new Coordinates(13, 11));
        diagonalPath.add(new Coordinates(14, 10));

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
