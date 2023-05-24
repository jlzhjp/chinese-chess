package org.henu.chess.common.messages.request;

import org.henu.chess.common.model.ChessBoardPoint;

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
}
