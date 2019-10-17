package com.ai.assignment.entities;

/**
 * @author deepakjha on 10/17/19
 * @project ai-assignments
 */
public class MoveToPlay {
    public int heuristic;
    public Move move;


    public MoveToPlay() {
        heuristic = 0;
    }


    public MoveToPlay(int heuristic) {
        this.heuristic = heuristic;
    }


    public MoveToPlay(Move move) {
        this.heuristic = move.getDestinationCell().getDistance();
        this.move = move;
    }


    public MoveToPlay(int heuristic, Move move) {
        this.heuristic = heuristic;
        this.move = move;
    }

    public int getHeuristic() {
        return this.heuristic;
    }

    public Move getMove() {
        return this.move;
    }
}
