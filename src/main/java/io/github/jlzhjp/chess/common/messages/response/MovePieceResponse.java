package io.github.jlzhjp.chess.common.messages.response;

import io.github.jlzhjp.chess.common.model.ChessBoardPoint;

import java.util.Objects;

public class MovePieceResponse extends Response {
    private ChessBoardPoint from;
    private ChessBoardPoint to;

    public ChessBoardPoint getFrom() {
        return from;
    }

    public void setFrom(ChessBoardPoint from) {
        this.from = from;
    }

    public ChessBoardPoint getTo() {
        return to;
    }

    public void setTo(ChessBoardPoint to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovePieceResponse that = (MovePieceResponse) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "MovePieceResponse{" +
                "from=" + from +
                ", to=" + to +
                "} " + super.toString();
    }
}
