package io.github.jlzhjp.chess.common.model;

public class ChessRecord {
    private ChessBoardPoint from;
    private ChessBoardPoint to;
    private Piece piece;
    private String stepString;

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

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public String getStepString() {
        return stepString;
    }

    public void setStepString(String stepString) {
        this.stepString = stepString;
    }

    @Override
    public String toString() {
        return stepString;
    }
}
