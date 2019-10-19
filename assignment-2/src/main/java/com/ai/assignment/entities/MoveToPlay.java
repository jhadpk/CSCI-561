package com.ai.assignment.entities;

import static com.ai.assignment.entities.board.Halma.calculateBoardSituation;
import static com.ai.assignment.entities.board.Halma.makeMove;
import static com.ai.assignment.entities.board.Halma.undoMove;


/**
 * @author deepakjha on 10/17/19
 * @project ai-assignments
 */
public class MoveToPlay {
    public int heuristic;
    public Move move;

    public MoveToPlay() {
        this.heuristic = 0;
    }

    public MoveToPlay(Move move) {
        makeMove(move);
        this.heuristic = calculateBoardSituation(move.getPlayerType());
        this.move = move;
        undoMove(move);
    }


    public MoveToPlay(Move move, int heuristic) {
        this.heuristic = heuristic;
        this.move = move;
    }


    public int getHeuristic() {
        return this.heuristic;
    }


    public Move getMove() {
        return this.move;
    }


    public static int getHeuristicForMove(Move move) {
        makeMove(move);
        int heuristicForMove = calculateBoardSituation(move.getPlayerType());
        undoMove(move);
        return heuristicForMove;
    }
}
