package io.github.jlzhjp.chess.common.messages.request;

import io.github.jlzhjp.chess.common.model.ChessBoardPoint;
import io.github.jlzhjp.chess.common.model.Piece;

import java.util.Objects;

public class RetractReplyRequest extends Request {
    private boolean agree;
    private ChessBoardPoint from;
    private ChessBoardPoint to;
    private Piece pieceJustEaten;

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

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

    public Piece getPieceJustEaten() {
        return pieceJustEaten;
    }

    public void setPieceJustEaten(Piece pieceJustEaten) {
        this.pieceJustEaten = pieceJustEaten;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RetractReplyRequest that = (RetractReplyRequest) o;
        return agree == that.agree && Objects.equals(from, that.from) && Objects.equals(to, that.to) && pieceJustEaten == that.pieceJustEaten;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), agree, from, to, pieceJustEaten);
    }

    @Override
    public String toString() {
        return "RetractReplyRequest{" +
                "agree=" + agree +
                ", from=" + from +
                ", to=" + to +
                ", pieceJustEaten=" + pieceJustEaten +
                "} " + super.toString();
    }
}
