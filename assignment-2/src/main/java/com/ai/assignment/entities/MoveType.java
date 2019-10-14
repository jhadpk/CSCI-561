package com.ai.assignment.entities;

/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public enum MoveType {
    JUMP("J"),
    EMPTY("E");

    private final String move;

    MoveType(String move) {
        this.move = move;
    }
}
