package com.ai.assignment.api;


import com.ai.assignment.entities.Camp;
import com.ai.assignment.entities.Input;
import com.ai.assignment.entities.Jump;
import com.ai.assignment.entities.Move;
import com.ai.assignment.entities.MoveToPlay;
import com.ai.assignment.entities.board.Cell;
import com.ai.assignment.entities.board.Coordinates;
import com.ai.assignment.entities.enums.MoveType;
import com.ai.assignment.entities.enums.PlayerType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.ai.assignment.entities.MoveToPlay.getHeuristicForMove;
import static com.ai.assignment.entities.board.Halma.getCellByCoordinate;
import static com.ai.assignment.entities.board.Halma.makeMove;
import static com.ai.assignment.entities.board.Halma.undoMove;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */


public abstract class PlayerImpl implements Player {
    protected Cell BLACK_CORNER_CELL = new Cell(0, 0);
    protected Cell WHITE_CORNER_CELL = new Cell(15, 15);

    private final int MOVE_OUT_OF_CAMP = 100;
    private final int MOVE_INSIDE_CAMP_FAR_FROM_CORNER = 80;
    private final int MOVE_ENTERS_OPPOSITION_CAMP = 70;

    private final int JUMP_MOVE_AWAY_FROM_CAMP = 50;
    private final int SINGLE_MOVE_AWAY_FROM_CAMP = 20;
    private final int JUMP_MOVE = 10;
    private final int IS_NOT_SURROUNDED_BY_OPPONENTS = 15;


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
     * Else, get all possible moves and execute one of them based on minimax (aplha beta pruned)
     */
    protected Move decideNextMove(final ArrayList<Cell> allPlayers) {
        ArrayList<MoveToPlay> allBestMoves = new ArrayList<>();
        ArrayList<Cell> playersInCamp = new ArrayList<>();
        for (Cell cell : allPlayers) {
            if (isInCamp(cell)) {
                playersInCamp.add(cell);
            }
        }
        ArrayList<ArrayList<Move>> movesForCampPlayers = new ArrayList<>();
        if (playersInCamp.size() != 0) {
            for (Cell cell : playersInCamp) {
                movesForCampPlayers.add(getAvailableMoves(cell));
            }
        }
        if (movesForCampPlayers.size() != 0) {
            for (ArrayList<Move> possibleMove : movesForCampPlayers) {
                if (possibleMove.size() != 0) {
                    if (possibleMove.size() == 1) {
                        allBestMoves.add(new MoveToPlay(possibleMove.get(0)));
                    } else {
                        allBestMoves.add(iterativeDeepeningAlphaBeta(possibleMove));
                        //allBestMoves.add(executeMinMax(0, possibleMove, null, true, 0, 0));
                    }
                }
            }
        } else {
            //No moves available for players in camp. Check only for players outside of camp.
            allPlayers.removeAll(playersInCamp);
            for (Cell cell : allPlayers) {
                ArrayList<Move> possibleMoves = getAvailableMoves(cell);
                if (possibleMoves.size() != 0) {
                    if (possibleMoves.size() == 1) {
                        allBestMoves.add(new MoveToPlay(possibleMoves.get(0)));
                    } else {
                        allBestMoves.add(iterativeDeepeningAlphaBeta(possibleMoves));
                        //allBestMoves.add(executeMinMax(0, possibleMoves, null, true, 0, 0));
                    }
                }
            }
        }

        //remove moves with null move

        allBestMoves.sort(Comparator.comparing(MoveToPlay::getHeuristic).reversed());
        return allBestMoves.size() != 0 ? allBestMoves.get(0).getMove() : null;
    }


    protected void findMovesOutsideCamp(final Cell cell, final ArrayList<Move> availableMoves) {
        if (isValidMove(cell.getLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getLeft(), availableMoves);
        }
        if (isValidMove(cell.getRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getRight(), availableMoves);
        }
        if (isValidMove(cell.getTop())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTop(), availableMoves);
        }
        if (isValidMove(cell.getBottom())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottom(), availableMoves);
        }
        if (isValidMove(cell.getTopLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTopLeft(), availableMoves);
        }
        if (isValidMove(cell.getTopRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTopRight(), availableMoves);
        }
        if (isValidMove(cell.getBottomLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottomLeft(), availableMoves);
        }
        if (isValidMove(cell.getBottomRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottomRight(), availableMoves);
        }
        availableMoves.addAll(getJumpMoves(cell));
    }


    public void addSingleMove(MoveType moveType, Cell startingCell, Cell destinationCell,
            ArrayList<Move> availableMoves) {
        availableMoves.add(getMove(startingCell.getPlayerType(), moveType, Arrays.asList(startingCell, destinationCell),
                startingCell, destinationCell));
    }


    public ArrayList<Move> getSingleMoves(final Cell cell) {
        ArrayList<Move> availableMoves = new ArrayList<>();
        if (isCellAvailable(cell.getLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getLeft(), availableMoves);
        }
        if (isCellAvailable(cell.getRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getRight(), availableMoves);
        }
        if (isCellAvailable(cell.getTop())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTop(), availableMoves);
        }
        if (isCellAvailable(cell.getBottom())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottom(), availableMoves);
        }
        if (isCellAvailable(cell.getTopLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTopLeft(), availableMoves);
        }
        if (isCellAvailable(cell.getTopRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getTopRight(), availableMoves);
        }
        if (isCellAvailable(cell.getBottomLeft())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottomLeft(), availableMoves);
        }
        if (isCellAvailable(cell.getBottomRight())) {
            addSingleMove(MoveType.EMPTY, cell, cell.getBottomRight(), availableMoves);
        }
        return availableMoves;
    }


    public boolean isCellAvailable(final Cell cell) {
        return isNotNull(cell) && cell.getPlayerType() == PlayerType.NONE;
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


    public Move getMove(PlayerType playerType, MoveType moveType, List<Cell> path, Cell startingCell,
            Cell destinationCell) {
        return new Move(playerType, moveType, path, startingCell, destinationCell);
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


    /***
     * Should not move back to camp, and no player should be occupying the cell.
     */
    public boolean isValidMove(final Cell destination) {
        return isNotNull(destination) && !isInCamp(destination) && destination.getPlayerType() == PlayerType.NONE;
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
                    jumpingPaths.add(retracePath(parentInfo, startingCell, getCellByCoordinate(new Coordinates(i, j)),
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


    public MoveToPlay iterativeDeepeningAlphaBeta(ArrayList<Move> moves) {
        MoveToPlay bestMove = new MoveToPlay();
        //for (int depth = 1; depth <= MAX_DEPTH; depth++) {
        //    if (System.currentTimeMillis() - START_TIME >= timeRemainingInMillis) {
        //        try {
        //            throw new AgentTimeoutException();
        //        } catch (AgentTimeoutException e) {
        //            return bestMove;
        //        }
        //    }
        //    MoveToPlay nextMove = alphaBetaSearch(depth, moves);
        //    if (nextMove.getHeuristic() > bestMove.getHeuristic()) {
        //        bestMove = nextMove;
        //    }
        //}
        bestMove = alphaBetaSearch(maxDepth, moves);
        return bestMove;
    }


    /***
     * Minimax algorithm with alpha beta pruning and using ierative deepening for search.
     * If time taken exceeds time remaining, abruptly returning the best move as per heuristic.
     */
    public MoveToPlay executeMinMax(int depth, ArrayList<Move> moves, Move play, boolean maximizing, int alpha,
            int beta) {
        if (depth <= 0 || isGameOver(player) || moves.isEmpty()) {
            if (null != play && null != play.getDestinationCell()) {
                return new MoveToPlay(play);
            }
        }
        moves.sort(Comparator.comparing(Move::getHeuristic).reversed());
        MoveToPlay bestMove = new MoveToPlay();
        MoveToPlay nextMove;
        if (maximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (Move move : moves) {
                makeMove(move);
                nextMove = executeMinMax(depth - 1, getAvailableMoves(move.getDestinationCell()), move, false, alpha,
                        beta);
                undoMove(move);
                if (nextMove.getHeuristic() > bestScore) {
                    bestScore = nextMove.getHeuristic();
                    bestMove = new MoveToPlay(move, bestScore);
                }
                if (bestScore >= beta) {
                    return new MoveToPlay(move, bestScore);
                }
                alpha = Integer.max(alpha, bestScore);
            }
            return bestMove;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (Move move : moves) {
                makeMove(move);
                nextMove = executeMinMax(depth - 1, getAvailableMoves(move.getDestinationCell()), move, true, alpha,
                        beta);
                undoMove(move);
                if (nextMove.getHeuristic() < bestScore) {
                    bestScore = nextMove.getHeuristic();
                    bestMove = new MoveToPlay(move, bestScore);
                }
                if (bestScore <= alpha) {
                    return new MoveToPlay(move, bestScore);
                }
                beta = Math.min(beta, bestScore);
            }
            return bestMove;
        }
    }


    private MoveToPlay alphaBetaSearch(int depth, ArrayList<Move> moves) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int currentValue;
        MoveToPlay bestMove = new MoveToPlay();
        //moves.sort(Comparator.comparing(Move::getHeuristic).reversed());
        for (Move move : moves) {
            makeMove(move);
            currentValue = maximizer(depth, move, alpha, beta);
            if (currentValue > alpha) {
                alpha = currentValue;
                bestMove = new MoveToPlay(move, currentValue);
            }
            undoMove(move);
        }
        return bestMove;
    }


    private int maximizer(int depth, Move move, int alpha, int beta) {
        final ArrayList<Move> availableMoves = getAvailableMoves(move.getDestinationCell());
        if (depth == 0 || isGameOver(player) || availableMoves.size() == 0) {
            if (null != move.getDestinationCell()) {
                return getHeuristicForMove(move);
            }
        }
        int v = Integer.MIN_VALUE;
        for (Move play : availableMoves) {
            makeMove(play);
            v = Math.max(v, minimizer(depth - 1, play, alpha, beta));
            undoMove(play);
            if (v >= beta) { return v; }
            alpha = Math.max(alpha, v);
        }
        return v;
    }


    private int minimizer(int depth, Move move, int alpha, int beta) {
        final ArrayList<Move> availableMoves = getAvailableMoves(move.getDestinationCell());
        if (depth == 0 || isGameOver(player) || availableMoves.size() == 0) {
            if (null != move.getDestinationCell()) {
                return getHeuristicForMove(move);
            }
        }
        int v = Integer.MAX_VALUE;
        for (Move play : availableMoves) {
            makeMove(play);
            v = Math.min(v, maximizer(depth - 1, play, alpha, beta));
            undoMove(play);
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


    private int evaluate(final Move move) {
        int heuristic = 0;

        if (isInCamp(move.getStartingCell()) && !isInCamp(move.getDestinationCell())) { heuristic = heuristic + 10; }

        return heuristic;
    }
}
