package org.henu.chess.common.model;

import java.util.Map;
import java.util.List;

public interface ChessLogic {
    int getBoardWidth();
    int getBoardHeight();

    List<ChessBoardPoint> getAvailablePointForPieceAt(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point);
}
