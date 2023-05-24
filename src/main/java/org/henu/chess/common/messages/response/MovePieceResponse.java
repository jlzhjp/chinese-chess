package org.henu.chess.common.messages.response;

import org.henu.chess.common.model.ChessBoardPoint;

public class MovePieceResponse extends Response {
    private ChessBoardPoint from;
    private ChessBoardPoint to;

    public void setFrom(ChessBoardPoint from) {
        this.from = from;
    }
    public ChessBoardPoint getFrom() {
        return from;
    }

    public void setTo(ChessBoardPoint to) {
        this.to = to;
    }

    public ChessBoardPoint getTo() {
        return to;
    }
}
