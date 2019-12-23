import java.util.ArrayList;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class WhitePlayer extends PlayerImpl {

    public WhitePlayer(Input input) {
        super(input);
    }


    @Override
    public Move getNextMove() {
        return decideNextMove(getAllPlayerPositions(PlayerType.WHITE));
    }


    @Override
    public ArrayList<Move> getAvailableMoves(Cell cell) {
        ArrayList<Move> availableMoves = new ArrayList<>();
        if (!isNotNull(cell)) {
            return availableMoves;
        }
        if (isInCamp(cell)) {
            final ArrayList<Move> allSingleMoves = getSingleMoves(cell);
            final ArrayList<Move> allJumpMoves = getJumpMoves(cell);

            final ArrayList<Move> firstPriorityMoves = new ArrayList<>();
            final ArrayList<Move> secondPriorityMoves = new ArrayList<>();
            for (Move move : allSingleMoves) {
                if (!isInCamp(move.getDestinationCell())) {
                    firstPriorityMoves.add(move);
                }
            }
            if (firstPriorityMoves.size() == 0) {
                //moves with destination cell within camp
                for (Move move : allSingleMoves) {
                    if (isFarFromCorner(WHITE_CORNER_CELL, cell, move.getDestinationCell())) {
                        secondPriorityMoves.add(move);
                    }
                }
            }


            final ArrayList<Move> firstPriorityJumps = new ArrayList<>();
            final ArrayList<Move> secondPriorityJumps = new ArrayList<>();
            for (Move move : allJumpMoves) {
                if (!isInCamp(move.getDestinationCell())) {
                    firstPriorityJumps.add(move);
                }
            }
            if (firstPriorityJumps.size() == 0) {
                //moves with destination cell within camp
                for (Move move : allJumpMoves) {
                    if (isFarFromCorner(WHITE_CORNER_CELL, cell, move.getDestinationCell())) {
                        secondPriorityJumps.add(move);
                    }
                }
            }
            availableMoves.addAll(firstPriorityMoves);
            availableMoves.addAll(secondPriorityMoves);
            availableMoves.addAll(firstPriorityJumps);
            availableMoves.addAll(secondPriorityJumps);
        } else if (isInOpposingCamp(cell)) {
            if (isNotNull(cell.getLeft()) && cell.getLeft().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getLeft(), availableMoves);
            }
            if (isNotNull(cell.getTop()) && cell.getTop().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getTop(), availableMoves);
            }
            if (isNotNull(cell.getTopLeft()) && cell.getTopLeft().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getTopLeft(), availableMoves);
            }
            for (Move move : getJumpMoves(cell)) {
                if (isCloserToCorner(BLACK_CORNER_CELL, cell, move.getDestinationCell())) {
                    availableMoves.add(move);
                }
            }
        } else {
            getMovesOutsideCamp(cell, availableMoves);

            //if any move is entering the opposite camp - make that move
            final ArrayList<Move> oppositionCampEnteringMoves = getOppositionCampEnteringMoves(availableMoves);
            if (oppositionCampEnteringMoves.size() != 0) {
                availableMoves.retainAll(oppositionCampEnteringMoves);
            }
        }
        return availableMoves;
    }


    @Override
    public ArrayList<Move> getOppositionCampEnteringMoves(ArrayList<Move> allAvailableMoves) {
        final ArrayList<Move> oppositionCampEnteringMoves = new ArrayList<>();
        for (Move move : allAvailableMoves) {
            if (Camp.blackCamp.contains(
                    move.getDestinationCell().getRow() + "," + move.getDestinationCell().getCol())) {
                oppositionCampEnteringMoves.add(move);
            }
        }
        return oppositionCampEnteringMoves;
    }


    @Override
    public boolean isInCamp(Cell cell) {
        return Camp.whiteCamp.contains(cell.getRow() + "," + cell.getCol());
    }


    @Override
    public boolean isInOpposingCamp(Cell cell) {
        return Camp.blackCamp.contains(cell.getRow() + "," + cell.getCol());
    }


    @Override
    public boolean returnsToCamp(Cell startingCell, Cell destinationCell) {
        if (isNotNull(startingCell) && isNotNull(destinationCell)) {
            if (!Camp.whiteCamp.contains(startingCell.getRow() + "," + startingCell.getCol())) {
                return Camp.whiteCamp.contains(destinationCell.getRow() + "," + destinationCell.getCol());
            }
        }
        return false;
    }
}
