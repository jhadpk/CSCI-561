package com.ai.assignment.entities;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public enum PlayerType {
    BLACK("B"),
    WHITE("W"),
    NONE(".");

    private final String player;

    PlayerType(String player) {
        this.player = player;
    }

    public static PlayerType getPlayerType(final String player) {
        for (PlayerType pt : PlayerType.values()) {
            if (pt.player.equals(player)) {
                return pt;
            }
        }
        return null;
    }
}
