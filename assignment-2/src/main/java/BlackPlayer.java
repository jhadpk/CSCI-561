import java.util.ArrayList;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class BlackPlayer extends PlayerImpl {

    public BlackPlayer(Input input) {
        super(input);
    }


    @Override
    public Move getNextMove() {
        return decideNextMove(getAllPlayerPositions(PlayerType.BLACK));
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
                    if (isFarFromCorner(BLACK_CORNER_CELL, cell, move.getDestinationCell())) {
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
                    if (isFarFromCorner(BLACK_CORNER_CELL, cell, move.getDestinationCell())) {
                        secondPriorityJumps.add(move);
                    }
                }
            }
            availableMoves.addAll(firstPriorityMoves);
            availableMoves.addAll(secondPriorityMoves);
            availableMoves.addAll(firstPriorityJumps);
            availableMoves.addAll(secondPriorityJumps);
        } else if (isInOpposingCamp(cell)) {
            if (isNotNull(cell.getRight()) && cell.getRight().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getRight(), availableMoves);
            }
            if (isNotNull(cell.getBottom()) && cell.getBottom().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getBottom(), availableMoves);
            }
            if (isNotNull(cell.getBottomRight()) && cell.getBottomRight().getPlayerType() == PlayerType.NONE) {
                addSingleMove(MoveType.EMPTY, cell, cell.getBottomRight(), availableMoves);
            }
            for (Move move : getJumpMoves(cell)) {
                if (isCloserToCorner(WHITE_CORNER_CELL, cell, move.getDestinationCell())) {
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
            if (Camp.whiteCamp.contains(
                    move.getDestinationCell().getRow() + "," + move.getDestinationCell().getCol())) {
                oppositionCampEnteringMoves.add(move);
            }
        }
        return oppositionCampEnteringMoves;
    }


    @Override
    public boolean isInCamp(Cell cell) {
        return Camp.blackCamp.contains(cell.getRow() + "," + cell.getCol());
    }


    @Override
    public boolean isInOpposingCamp(Cell cell) {
        return Camp.whiteCamp.contains(cell.getRow() + "," + cell.getCol());
    }


    @Override
    public boolean returnsToCamp(Cell startingCell, Cell destinationCell) {
        if (isNotNull(startingCell) && isNotNull(destinationCell)) {
            if (!Camp.blackCamp.contains(startingCell.getRow() + "," + startingCell.getCol())) {
                return Camp.blackCamp.contains(destinationCell.getRow() + "," + destinationCell.getCol());
            }
        }
        return false;
    }
}
