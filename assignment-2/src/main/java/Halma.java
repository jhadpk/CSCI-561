import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @author deepakjha on 10/14/19
 * @project ai-assignments
 */

public class Halma {
    private static ArrayList<ArrayList<Cell>> board;
    private static HashMap<Coordinates, Cell> coordinatesCellMap;


    public Halma(ArrayList<ArrayList<String>> boardInput) {
        board = new ArrayList<>();
        coordinatesCellMap = new HashMap<>();
        for (int row = 0; row < boardInput.size(); row++) {
            final ArrayList<Cell> boardRow = new ArrayList<>();
            for (int col = 0; col < boardInput.get(row).size(); col++) {
                Cell cell = new Cell(row, col);
                cell.setPlayerType(PlayerType.getPlayerByValue(boardInput.get(row).get(col)));
                boardRow.add(cell);
                coordinatesCellMap.put(new Coordinates(row, col), cell);
            }
            board.add(boardRow);
        }
    }


    public ArrayList<ArrayList<Cell>> getBoard() {
        return board;
    }


    public static Cell getLeft(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (col > 0) {
            col -= 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getRight(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (col < 15) {
            col += 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getTop(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row > 0) {
            row -= 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getBottom(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row < 15) {
            row += 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getTopLeft(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row > 0 && col > 0) {
            row -= 1;
            col -= 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getTopRight(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row > 0 && col < 15) {
            row -= 1;
            col += 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getBottomLeft(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row < 15 && col > 0) {
            row += 1;
            col -= 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getBottomRight(Coordinates coordinates) {
        int row = coordinates.getRow();
        int col = coordinates.getCol();
        if (row < 15 && col < 15) {
            row += 1;
            col += 1;
            return coordinatesCellMap.get(new Coordinates(row, col));
        }
        return null;
    }


    public static Cell getCellByCoordinate(final Coordinates coordinates) {
        return board.get(coordinates.getRow()).get(coordinates.getCol());
    }


    public static void makeMove(Move move) {
        int sRow = move.getStartingCell().getRow();
        int sCol = move.getStartingCell().getCol();
        int dRow = move.getDestinationCell().getRow();
        int dCol = move.getDestinationCell().getCol();
        if (board.get(sRow).get(sCol).getPlayerType().equals(move.getPlayerType()) && board.get(dRow).get(dCol)
                .getPlayerType().equals(PlayerType.NONE)) {
            board.get(sRow).get(sCol).setPlayerType(PlayerType.NONE);
            board.get(dRow).get(dCol).setPlayerType(move.getPlayerType());
        }
    }


    public static void undoMove(Move move) {
        int sRow = move.getStartingCell().getRow();
        int sCol = move.getStartingCell().getCol();
        int dRow = move.getDestinationCell().getRow();
        int dCol = move.getDestinationCell().getCol();
        if (board.get(sRow).get(sCol).getPlayerType().equals(PlayerType.NONE) && board.get(dRow).get(dCol)
                .getPlayerType().equals(move.getPlayerType())) {
            board.get(sRow).get(sCol).setPlayerType(move.getPlayerType());
            board.get(dRow).get(dCol).setPlayerType(PlayerType.NONE);
        }
    }

    public static int evaluateBoard(final PlayerType playerType) {
        int heuristic = 0;
        Coordinates targetCorner = Coordinates.getTargetCornerCordinatesByPlayer(playerType);
        List<String> oppositeCamp = Camp.getOpposingCampCoordinates(playerType);

        for (ArrayList<Cell> row : board) {
            for (Cell tile : row) {
                if (tile.getPlayerType() == playerType) {
                    heuristic = heuristic + evaluateCell(oppositeCamp, targetCorner, tile);
                }
            }
        }
        return heuristic;
    }


    private static int evaluateCell(List<String> oppositionCamp, final Coordinates targetCorner, final Cell cell) {
        int heuristic = 0;
        if (oppositionCamp.contains(cell.getRow() + "," + cell.getCol())) {
            heuristic += HeuristicValues.CELL_IN_OPPOSITION_CAMP_BONUS;
        }
        if (Camp.diagonalPath.contains((cell.getRow() + "," + cell.getCol()))) {
            heuristic += HeuristicValues.CELL_IN_DIAGONAL_PATH_BONUS;
        }
        heuristic += (30 - (Math.abs(cell.getRow() - targetCorner.getRow()) + Math.abs(
                cell.getCol() - targetCorner.getCol()))) * 5;
        return heuristic;
    }





























    //Next Lines are for generating next input file

    public static void generateNextInput(final ArrayList<Output> outputMoves, final PlayerType playerType) {
        final Cell starting = outputMoves.get(0).getStartingCell();
        final Cell destination = outputMoves.get(outputMoves.size() - 1).getDestinationCell();

        for (ArrayList<Cell> row : board) {
            for (Cell tile : row) {
                if (tile.getRow() == starting.getRow() && tile.getCol() == starting.getCol()) {
                    tile.setPlayerType(PlayerType.NONE);
                } else if (tile.getRow() == destination.getRow() && tile.getCol() == destination.getCol()) {
                    tile.setPlayerType(playerType);
                }

            }
        }

        final ArrayList<ArrayList<String>> transposedBoard = transpose(board);

        //for (int i = 0; i < transposedBoard.size(); i++) {
        //    for (int j = 0; j < transposedBoard.get(i).size(); j++) {
        //        System.out.print(transposedBoard.get(i).get(j) + ",");
        //    }
        //    System.out.println();
        //}

        generateInput(transposedBoard, playerType);

    }


    private static ArrayList<ArrayList<String>> transpose(ArrayList<ArrayList<Cell>> givenBoard) {
        ArrayList<ArrayList<String>> transposedMap = new ArrayList<>();
        if (!givenBoard.isEmpty()) {
            int rowSize = givenBoard.get(0).size();
            for (int i = 0; i < rowSize; i++) {
                ArrayList<String> col = new ArrayList<>();
                for (ArrayList<Cell> row : givenBoard) {
                    col.add(row.get(i).getPlayerType().player);
                }
                transposedMap.add(col);
            }
        }
        return transposedMap;
    }

    private static void generateInput(final ArrayList<ArrayList<String>> board, final PlayerType playerType) {
        FileWriter fw = null;
        try {
            fw = new FileWriter("/Users/deepakjha/input.txt", false);

            fw.write("GAME\n");
            fw.write(playerType.switchPlayer() + "\n");
            fw.write("300.0\n");
            if (null != board && board.size() != 0) {
                StringBuilder output = new StringBuilder();
                for (ArrayList<String> row : board) {
                    for (String tile : row) {
                        output.append(tile);
                    }
                    output.append("\n");
                }
                fw.write(output.substring(0, output.toString().length() - 1));
            } else {
                fw.write("NO_OUTPUT");
            }
        } catch (IOException e) {
            try {
                System.out.println("IOException occurred");
                fw.write("NO_OUTPUT");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {fw.close();} catch (Exception ex) {System.out.println("Exception in closing fw");}
        }
    }
}
