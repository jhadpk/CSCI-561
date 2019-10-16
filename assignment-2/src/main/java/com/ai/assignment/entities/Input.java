package com.ai.assignment.entities;

import com.ai.assignment.entities.board.Halma;
import com.ai.assignment.entities.enums.GameType;
import com.ai.assignment.entities.enums.PlayerType;

import java.util.ArrayList;


/**
 * @author deepakjha on 10/15/19
 * @project ai-assignments
 */
public class Input {
    private GameType gameType;
    private PlayerType playerType;
    private long timeRemainingInSeconds;
    private Halma halma;

    public void setGameType(final String gameType) {
        this.gameType = GameType.getGameType(gameType);
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public void setPlayerType(final String playerColor) {
        this.playerType = PlayerType.getPlayerByColor(playerColor);
    }

    public PlayerType getPlayerType() {
        return this.playerType;
    }

    public void setTimeRemainingInSeconds(final String time) {
        this.timeRemainingInSeconds = Long.parseLong(time);
    }

    public long getTimeRemainingInSeconds() {
        return this.timeRemainingInSeconds;
    }

    public void setHalma(ArrayList<ArrayList<String>> boardConfiguration) {
        this.halma = new Halma(boardConfiguration);
    }

    public Halma getHalma() {
        return this.halma;
    }
}
