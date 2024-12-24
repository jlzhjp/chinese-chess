package io.github.jlzhjp.chess.common.messages.response;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StartGameResponse that = (StartGameResponse) o;
        return Objects.equals(redPlayerName, that.redPlayerName) && Objects.equals(blackPlayerName, that.blackPlayerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), redPlayerName, blackPlayerName);
    }

    @Override
    public String toString() {
        return "StartGameResponse{" +
                "redPlayerName='" + redPlayerName + '\'' +
                ", blackPlayerName='" + blackPlayerName + '\'' +
                "} " + super.toString();
    }
}
