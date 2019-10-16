package com.ai.assignment.entities.enums;

/**
 * @author deepakjha on 10/15/19
 * @project ai-assignments
 */
public enum GameType {
    SINGLE("SINGLE"),
    GAME("GAME");

    private final String typeOfGame;


    GameType(final String typeOfGame) {
        this.typeOfGame = typeOfGame;
    }


    public static GameType getGameType(final String type) {
        for (GameType gameType : GameType.values()) {
            if (gameType.typeOfGame.equals(type)) {
                return gameType;
            }
        }
        return null;
    }
}
