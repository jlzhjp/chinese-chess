package org.henu.chess.common.model;

import java.util.List;
import java.util.Map;

public class ChessLogicBlackImpl extends ChessLogicRedImpl {
    @Override
    public List<ChessBoardPoint> getAvailablePointForPieceAt(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        point = new ChessBoardPoint(point.getX(), getBoardHeight() - point.getY() - 1);
        return super.getAvailablePointForPieceAt(pieces, point).stream().map(p -> new ChessBoardPoint(p.getX(), getBoardHeight() - p.getY() - 1)).toList();
    }
}
