package io.github.jlzhjp.chess.common.messages.response;

import java.util.Objects;

public class GameOverResponse extends Response {
    private String winner;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GameOverResponse that = (GameOverResponse) o;
        return Objects.equals(winner, that.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), winner);
    }

    @Override
    public String toString() {
        return "GameOverResponse{" +
                "winner='" + winner + '\'' +
                "} " + super.toString();
    }
}
