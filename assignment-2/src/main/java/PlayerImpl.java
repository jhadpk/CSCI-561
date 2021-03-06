import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */


public abstract class PlayerImpl implements Player {
    protected final Cell BLACK_CORNER_CELL = new Cell(0, 0);
    protected final Cell WHITE_CORNER_CELL = new Cell(15, 15);

    private final ArrayList<ArrayList<Cell>> board;
    private final double timeRemainingInMillis;
    private final PlayerType player;
    private final int maxDepth;


    public PlayerImpl(Input input) {
        this.board = input.getBoard();
        this.timeRemainingInMillis = input.getTimeRemainingInSeconds() * 1000;
        this.player = input.getPlayerType();
        this.maxDepth = input.getMaxDepth();
    }


    protected ArrayList<Cell> getAllPlayerPositions(final PlayerType playerType) {
        ArrayList<Cell> playerPositions = new ArrayList<>();
        for (ArrayList<Cell> row : board) {
            for (Cell cell : row) {
                if (cell.getPlayerType().equals(playerType)) {
                    playerPositions.add(cell);
                }
            }
        }
        return playerPositions;
    }


    /***
     * If there are any players in camp, find their possible move.
     * If there is a possible move for any of such camp players - execute that move.
     * Priority of moves:
     *  1. Movement outside camp
     *  2. Movement farther away from corner.
     *  3. If 1 and 2 are not possible, make any move out of camp.
     */
    protected Move decideNextMove(final ArrayList<Cell> allPlayers) {
        MoveToPlay nextMove = null;
        ArrayList<Cell> playersInCamp = new ArrayList<>();
        for (Cell cell : allPlayers) {
            if (isInCamp(cell)) {
                playersInCamp.add(cell);
            }
        }

        // Finding moves for players present in camp
        ArrayList<ArrayList<Move>> movesForCampPlayers = new ArrayList<>();
        if (playersInCamp.size() != 0) {
            for (Cell cell : playersInCamp) {
                final ArrayList<Move> availableMoves = getAvailableMoves(cell);
                if (!availableMoves.isEmpty()) {
                    movesForCampPlayers.add(availableMoves);
                }
            }
        }

        final ArrayList<Move> movesGoingOutOfCamp = new ArrayList<>();
        if (movesForCampPlayers.size() != 0) {
            // Find any move which is taking out of camp
            for (ArrayList<Move> possibleMove : movesForCampPlayers) {
                for (Move move : possibleMove) {
                    if (!isInCamp(move.getDestinationCell())) {
                        movesGoingOutOfCamp.add(move);
                    }
                }
            }
            //If any move going out of camp exists, execute that
            if (movesGoingOutOfCamp.size() != 0) {
                nextMove = iterativeDeepeningSearch(movesGoingOutOfCamp);
            } else {
                //Since no move is going out of camp, execute move going farther away (we have only found moves which
                // are either going out of camp or moving father away, hence only moving away moves are present)
                ArrayList<Move> movesInsideCamp = new ArrayList<>();
                for (ArrayList<Move> possibleMoves : movesForCampPlayers) {
                    movesInsideCamp.addAll(possibleMoves);
                }
                if (movesInsideCamp.size() != 0) {
                    nextMove = iterativeDeepeningSearch(movesInsideCamp);
                }
            }
        } else {
            //No moves available for players in camp. Check only for players outside of camp.
            allPlayers.removeAll(playersInCamp);
            ArrayList<ArrayList<Move>> allPlayersMoves = new ArrayList<>();
            for (Cell cell : allPlayers) {
                allPlayersMoves.add(getAvailableMoves(cell));
            }
            ArrayList<Move> movesOutsideCamp = new ArrayList<>();
            for (ArrayList<Move> moves : allPlayersMoves) {
                movesOutsideCamp.addAll(moves);
            }

            if (movesOutsideCamp.size() != 0) {
                nextMove = iterativeDeepeningSearch(movesOutsideCamp);
            }
        }
        return nextMove != null ? nextMove.getMove() : null;
    }


    protected void getMovesOutsideCamp(final Cell cell, final ArrayList<Move> availableMoves) {
        if (isValidMoveForCellOutsideCamp(cell.getLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getLeft(), availableMoves);
        }
        if (isValidMoveForCellOutsideCamp(cell.getRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getRight(), availableMoves);
        }
        if (isValidMoveForCellOutsideCamp(cell.getTop())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTop(), availableMoves);
        }
        if (isValidMoveForCellOutsideCamp(cell.getBottom())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottom(), availableMoves);
        }
        if (isValidMoveForCellOutsideCamp(cell.getTopLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTopLeft(), availableMoves);
        }
        if (isValidMoveForCellOutsideCamp(cell.getTopRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTopRight(), availableMoves);
        }
        if (isValidMoveForCellOutsideCamp(cell.getBottomLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottomLeft(), availableMoves);
        }
        if (isValidMoveForCellOutsideCamp(cell.getBottomRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottomRight(), availableMoves);
        }
        availableMoves.addAll(getJumpMoves(cell));
    }


    /***
     * Should not move back to camp, and no player should be occupying the cell.
     */
    public boolean isValidMoveForCellOutsideCamp(final Cell destination) {
        return isNotNull(destination) && !isInCamp(destination) && destination.getPlayerType() == PlayerType.NONE;
    }


    public void addSingleMove(MoveType moveType, Cell startingCell, Cell destinationCell,
            ArrayList<Move> availableMoves) {
        availableMoves.add(getMove(startingCell.getPlayerType(), moveType, Arrays.asList(startingCell, destinationCell),
                startingCell, destinationCell));
    }


    public Move getMove(PlayerType playerType, MoveType moveType, List<Cell> path, Cell startingCell,
            Cell destinationCell) {
        return new Move(playerType, moveType, path, startingCell, destinationCell);
    }


    public ArrayList<Move> getSingleMoves(final Cell cell) {
        ArrayList<Move> availableMoves = new ArrayList<>();
        if (isValidMoveForCellInCamp(cell, cell.getLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getLeft(), availableMoves);
        }
        if (isValidMoveForCellInCamp(cell, cell.getRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getRight(), availableMoves);
        }
        if (isValidMoveForCellInCamp(cell, cell.getTop())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTop(), availableMoves);
        }
        if (isValidMoveForCellInCamp(cell, cell.getBottom())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottom(), availableMoves);
        }
        if (isValidMoveForCellInCamp(cell, cell.getTopLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTopLeft(), availableMoves);
        }
        if (isValidMoveForCellInCamp(cell, cell.getTopRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTopRight(), availableMoves);
        }
        if (isValidMoveForCellInCamp(cell, cell.getBottomLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottomLeft(), availableMoves);
        }
        if (isValidMoveForCellInCamp(cell, cell.getBottomRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottomRight(), availableMoves);
        }
        return availableMoves;
    }


    public boolean isValidMoveForCellInCamp(final Cell startingCell, final Cell destinationCell) {
        return isNotNull(destinationCell) && destinationCell.getPlayerType() == PlayerType.NONE && !returnsToCamp(
                startingCell, destinationCell);
    }


    public ArrayList<Move> getJumpMoves(final Cell cell) {
        ArrayList<Jump> jumps = new ArrayList<>();
        ArrayList<Cell> visitedCells = new ArrayList<>();
        visitedCells.add(cell);
        getJumps(null, cell, jumps, visitedCells);
        ArrayList<Move> jumpMoves = new ArrayList<>();
        if (jumps.size() != 0) {
            for (ArrayList<Cell> path : getJumpingPaths(getParentInfo(jumps), cell)) {
                jumpMoves.add(
                        getMove(cell.getPlayerType(), MoveType.JUMP, path, path.get(0), path.get(path.size() - 1)));
            }
        }
        return jumpMoves;
    }


    public void getJumps(Cell parent, Cell cell, ArrayList<Jump> jumps, ArrayList<Cell> visitedCells) {
        if (isNotNull(cell.getLeft()) && isNotNull(cell.getLeft().getLeft()) && isJumpValid(parent, cell.getLeft(),
                cell.getLeft().getLeft(), visitedCells)) {
            visitedCells.add(cell.getLeft().getLeft());
            recursiveGetJumps(cell, cell.getLeft().getLeft(), jumps, visitedCells);
        }
        if (isNotNull(cell.getRight()) && isNotNull(cell.getRight().getRight()) && isJumpValid(parent, cell.getRight(),
                cell.getRight().getRight(), visitedCells)) {
            visitedCells.add(cell.getRight().getRight());
            recursiveGetJumps(cell, cell.getRight().getRight(), jumps, visitedCells);
        }
        if (isNotNull(cell.getTop()) && isNotNull(cell.getTop().getTop()) && isJumpValid(parent, cell.getTop(),
                cell.getTop().getTop(), visitedCells)) {
            visitedCells.add(cell.getTop().getTop());
            recursiveGetJumps(cell, cell.getTop().getTop(), jumps, visitedCells);
        }
        if (isNotNull(cell.getBottom()) && isNotNull(cell.getBottom().getBottom()) && isJumpValid(parent,
                cell.getBottom(), cell.getBottom().getBottom(), visitedCells)) {
            visitedCells.add(cell.getBottom().getBottom());
            recursiveGetJumps(cell, cell.getBottom().getBottom(), jumps, visitedCells);
        }
        if (isNotNull(cell.getTopLeft()) && isNotNull(cell.getTopLeft().getTopLeft()) && isJumpValid(parent,
                cell.getTopLeft(), cell.getTopLeft().getTopLeft(), visitedCells)) {
            visitedCells.add(cell.getTopLeft().getTopLeft());
            recursiveGetJumps(cell, cell.getTopLeft().getTopLeft(), jumps, visitedCells);
        }
        if (isNotNull(cell.getTopRight()) && isNotNull(cell.getTopRight().getTopRight()) && isJumpValid(parent,
                cell.getTopRight(), cell.getTopRight().getTopRight(), visitedCells)) {
            visitedCells.add(cell.getTopRight().getTopRight());
            recursiveGetJumps(cell, cell.getTopRight().getTopRight(), jumps, visitedCells);
        }
        if (isNotNull(cell.getBottomLeft()) && isNotNull(cell.getBottomLeft().getBottomLeft()) && isJumpValid(parent,
                cell.getBottomLeft(), cell.getBottomLeft().getBottomLeft(), visitedCells)) {
            visitedCells.add(cell.getBottomLeft().getBottomLeft());
            recursiveGetJumps(cell, cell.getBottomLeft().getBottomLeft(), jumps, visitedCells);
        }
        if (isNotNull(cell.getBottomRight()) && isNotNull(cell.getBottomRight().getBottomRight()) && isJumpValid(parent,
                cell.getBottomRight(), cell.getBottomRight().getBottomRight(), visitedCells)) {
            visitedCells.add(cell.getBottomRight().getBottomRight());
            recursiveGetJumps(cell, cell.getBottomRight().getBottomRight(), jumps, visitedCells);
        }
    }


    public void recursiveGetJumps(Cell startingCell, Cell destinationCell, ArrayList<Jump> jumps,
            ArrayList<Cell> visitedCells) {
        jumps.add(new Jump(startingCell, destinationCell));
        getJumps(startingCell, destinationCell, jumps, visitedCells);
    }


    /***
     * InBetweenCell should be having either Black or White player and should not be empty.
     * DestinationCell should be empty and should not be same as parent of starting cell
     * Player should not return to camp (from outside of camp)
     * Check visited cells are not revisited to avoid cycles/loops
     */
    public boolean isJumpValid(final Cell parentCell, final Cell inBetweenCell, final Cell destinationCell,
            ArrayList<Cell> visitedCells) {
        return (inBetweenCell.getPlayerType() == PlayerType.BLACK || inBetweenCell.getPlayerType() == PlayerType.WHITE)
                && destinationCell.getPlayerType() == PlayerType.NONE && isNotSame(parentCell, destinationCell)
                && !returnsToCamp(parentCell, destinationCell) && !visitedCells.contains(destinationCell);
    }


    public boolean isNotNull(Object obj) {
        return null != obj;
    }


    public boolean isNotSame(Cell cell1, Cell cell2) {
        if (null == cell1) { return true; }
        return cell1.getRow() != cell2.getRow() || cell1.getCol() != cell2.getCol();
    }


    /***
     * Board is currently hardcoded to be 16x16
     */
    public Cell[][] getParentInfo(ArrayList<Jump> jumps) {
        Cell[][] parentInfo = new Cell[16][16];
        for (Jump jump : jumps) {
            Cell current = jump.getCurrent();
            parentInfo[current.getRow()][current.getCol()] = jump.getParent();
        }
        return parentInfo;
    }


    public ArrayList<ArrayList<Cell>> getJumpingPaths(final Cell[][] parentInfo, final Cell startingCell) {
        ArrayList<ArrayList<Cell>> jumpingPaths = new ArrayList<>();
        for (int i = 0; i < parentInfo.length; i++) {
            for (int j = 0; j < parentInfo[i].length; j++) {
                if (null != parentInfo[i][j]) {
                    jumpingPaths.add(
                            retracePath(parentInfo, startingCell, Halma.getCellByCoordinate(new Coordinates(i, j)),
                                    parentInfo[i][j]));
                }
            }
        }
        return jumpingPaths;
    }


    public ArrayList<Cell> retracePath(final Cell[][] parentInfo, final Cell startingCell, final Cell currentCell,
            final Cell target) {
        final ArrayList<Cell> path = new ArrayList<>();
        path.add(currentCell);
        if (currentCell.getRow() == startingCell.getRow() && currentCell.getCol() == startingCell.getCol()) {
            path.add(startingCell);
            return path;
        }
        Cell cell = target;
        path.add(cell);
        while (cell.getRow() != startingCell.getRow() || cell.getCol() != startingCell.getCol()) {
            if (cell.getRow() < 0 || cell.getRow() >= 16 || cell.getCol() < 0 || cell.getCol() >= 16) {
                return null;
            }
            cell = parentInfo[cell.getRow()][cell.getCol()];
            path.add(cell);
        }
        Collections.reverse(path);
        return path;
    }


    /***
     * To be checked if the move happens within camp.
     * Distance found is the manhatten distance from the corner of the camp.
     * horizontalDistanceAtEnd >= horizontalDistanceAtStart && verticalDistanceAtEnd >= verticalDistanceAtStart
     */
    public boolean isFarFromCorner(final Cell corner, final Cell startingCell, final Cell destinationCell) {
        if (isInCamp(startingCell) && isInCamp(destinationCell)) {
            return Math.abs(destinationCell.getCol() - corner.getCol()) >= Math.abs(
                    startingCell.getCol() - corner.getCol()) && Math.abs(destinationCell.getRow() - corner.getRow())
                    >= Math.abs(startingCell.getRow() - corner.getRow());
        }
        return true;
    }


    public boolean isCloserToCorner(final Cell corner, final Cell startingCell, final Cell destinationCell) {
        if (isInOpposingCamp(startingCell)) {
            return Math.abs(destinationCell.getCol() - corner.getCol()) <= Math.abs(
                    startingCell.getCol() - corner.getCol()) && Math.abs(destinationCell.getRow() - corner.getRow())
                    <= Math.abs(startingCell.getRow() - corner.getRow());
        }
        return true;
    }


    public MoveToPlay iterativeDeepeningSearch(ArrayList<Move> moves) {
        MoveToPlay bestMove = new MoveToPlay();
        try {
            for (int depth = 1; depth <= maxDepth; depth++) {
                MoveToPlay nextMove = alphaBetaSearch(depth, moves);
                if (nextMove.getHeuristic() > bestMove.getHeuristic()) {
                    bestMove = nextMove;
                }
            }
        } catch (AgentTimeoutException e) {
            return bestMove;
        }
        return bestMove;
    }


    /***
     * Minimax with alphabeta pruning.
     * Searches for a given depth
     */
    private MoveToPlay alphaBetaSearch(int depth, ArrayList<Move> moves) throws AgentTimeoutException {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int currentValue;
        MoveToPlay bestMove = new MoveToPlay();
        for (Move move : moves) {
            if (System.currentTimeMillis() - homework.START_TIME >= timeRemainingInMillis) {
                throw new AgentTimeoutException();
            }
            Halma.makeMove(move);
            currentValue = maximizer(depth, move, alpha, beta, depth);
            if (currentValue > alpha || (currentValue == alpha && isInOpposingCamp(move.getDestinationCell()))) {
                alpha = currentValue;
                bestMove = new MoveToPlay(move, currentValue);
            }
            Halma.undoMove(move);
        }
        return bestMove;
    }


    private int maximizer(int depth, Move move, int alpha, int beta, final int maxDepth) throws AgentTimeoutException {
        final ArrayList<Move> availableMoves = getAvailableMoves(move.getDestinationCell());
        if (depth == 1 || isGameOver(player) || availableMoves.size() == 0) {
            if (null != move.getDestinationCell()) {
                return Halma.evaluateBoard(move.getPlayerType()) + evaluateMove(move, maxDepth);
            }
        }

        if (System.currentTimeMillis() - homework.START_TIME >= timeRemainingInMillis) {
            throw new AgentTimeoutException();
        }
        int v = Integer.MIN_VALUE;
        for (Move play : availableMoves) {
            if (play.getDestinationCell().getRow() == move.getStartingCell().getRow()
                    && play.getDestinationCell().getCol() == move.getStartingCell().getCol()) {
                //not allowing reverse move
                continue;
            }
            Halma.makeMove(play);
            v = Math.max(v, minimizer(depth - 1, play, alpha, beta, maxDepth));
            Halma.undoMove(play);
            if (v >= beta) { return v; }
            alpha = Math.max(alpha, v);
        }
        return v;
    }


    private int minimizer(int depth, Move move, int alpha, int beta, final int maxDepth) throws AgentTimeoutException {
        final ArrayList<Move> availableMoves = getAvailableMoves(move.getDestinationCell());
        if (depth == 1 || isGameOver(player) || availableMoves.size() == 0) {
            if (null != move.getDestinationCell()) {
                return Halma.evaluateBoard(move.getPlayerType()) + evaluateMove(move, maxDepth);
            }
        }

        if (System.currentTimeMillis() - homework.START_TIME >= timeRemainingInMillis) {
            throw new AgentTimeoutException();
        }
        int v = Integer.MAX_VALUE;
        for (Move play : availableMoves) {
            if (play.getDestinationCell().getRow() == move.getStartingCell().getRow()
                    && play.getDestinationCell().getCol() == move.getStartingCell().getCol()) {
                //not allowing reverse move
                continue;
            }
            Halma.makeMove(play);
            v = Math.min(v, maximizer(depth - 1, play, alpha, beta, maxDepth));
            Halma.undoMove(play);
            if (v <= alpha) { return v; }
            beta = Math.min(beta, v);
        }
        return v;
    }


    private boolean isGameOver(PlayerType player) {
        switch (player) {
            case WHITE:
                return isBlackCampOccupiedByWhite();
            case BLACK:
                return isWhiteCampOccupiedByBlack();
            default:
                return false;
        }
    }


    private boolean isWhiteCampOccupiedByBlack() {
        if (allWhitesStillInCamp()) {
            return false;
        }
        for (final Cell whiteCampTile : Camp.whiteCampCells) {
            if (whiteCampTile.getPlayerType().equals(PlayerType.NONE)) {
                return false;
            }
        }
        return true;
    }


    private boolean allWhitesStillInCamp() {
        for (final Cell whiteCampTile : Camp.whiteCampCells) {
            if (whiteCampTile.getPlayerType().equals(PlayerType.NONE) || whiteCampTile.getPlayerType().equals(
                    PlayerType.BLACK)) {
                return false;
            }
        }
        return true;
    }


    private boolean isBlackCampOccupiedByWhite() {
        if (allBlacksStillInCamp()) {
            return false;
        }
        for (final Cell blackCampTile : Camp.blackCampCells) {
            if (blackCampTile.getPlayerType().equals(PlayerType.NONE)) {
                return false;
            }
        }
        return true;
    }


    private boolean allBlacksStillInCamp() {
        for (final Cell blackCampTile : Camp.blackCampCells) {
            if (blackCampTile.getPlayerType().equals(PlayerType.NONE) || blackCampTile.getPlayerType().equals(
                    PlayerType.WHITE)) {
                return false;
            }
        }
        return true;
    }


    private int getManhattenDistance(final Cell cell1, final Cell cell2) {
        return Math.abs(cell1.getRow() - cell2.getRow()) + Math.abs(cell1.getCol() - cell2.getCol());
    }


    /***
     * Heuristic values :
     * 10 - moving closer to the opposition camp
     * 20 - moving into opposition camp from outside
     */
    private int evaluateMove(final Move move, final int depth) {
        int heuristic = 0;
        Cell startingCell = move.getStartingCell();
        Cell destinationCell = move.getDestinationCell();

        if (move.getPlayerType() == PlayerType.WHITE) {
            if (startingCell.getRow() > destinationCell.getRow()) {
                heuristic += HeuristicValues.VERTICAL_MOVE_BONUS;
                if (startingCell.getCol() > destinationCell.getCol()) {
                    heuristic += HeuristicValues.DIAGONAL_MOVE_BONUS;
                }
            }
        } else if (move.getPlayerType() == PlayerType.BLACK) {
            if (startingCell.getRow() < destinationCell.getRow()) {
                heuristic += HeuristicValues.VERTICAL_MOVE_BONUS;
                if (startingCell.getCol() < destinationCell.getCol()) {
                    heuristic += HeuristicValues.DIAGONAL_MOVE_BONUS;
                }
            }
        }

        ArrayList<Cell> availablePositions = Camp.getAvailablePositionsInOpposition(move.getPlayerType());
        for (Cell availableCell : availablePositions) {
            if (getManhattenDistance(availableCell, startingCell) > getManhattenDistance(availableCell,
                    destinationCell)) {
                heuristic += HeuristicValues.MOVING_CLOSER_TO_EMPTY_OPPOSITION_CELL;
                break;
            }
        }

        if (isInOpposingCamp(startingCell) && isInOpposingCamp(destinationCell)) {
            heuristic += HeuristicValues.MOVING_INSIDE_OPPOSITION_CAMP;
        }
        if (!isInOpposingCamp(startingCell) && isInOpposingCamp(destinationCell)) {
            heuristic += HeuristicValues.MOVING_INTO_OPPOSITION_CAMP_FROM_OUTSIDE;
        }

        return heuristic / depth;
    }
}
