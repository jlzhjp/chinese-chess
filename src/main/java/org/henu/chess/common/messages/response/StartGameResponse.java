package org.henu.chess.common.messages.response;

public class StartGameResponse extends Response {
    private String redPlayerName;

    private String blackPlayerName;

    public String getRedPlayerName() {
        return redPlayerName;
    }
    public void setRedPlayerName(String redPlayerName) {
        this.redPlayerName = redPlayerName;
    }

    public String getBlackPlayerName() {
        return blackPlayerName;
    }

    public void setBlackPlayerName(String blackPlayerName) {
        this.blackPlayerName = blackPlayerName;
    }
}
