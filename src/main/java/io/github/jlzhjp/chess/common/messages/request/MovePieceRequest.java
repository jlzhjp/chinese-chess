package io.github.jlzhjp.chess.common.messages.request;

import io.github.jlzhjp.chess.common.model.ChessBoardPoint;

import java.util.Objects;

public class MovePieceRequest extends Request {
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
    public String toString() {
        return "MovePieceRequest{" +
                "from=" + from +
                ", to=" + to +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MovePieceRequest that = (MovePieceRequest) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to);
    }
}
