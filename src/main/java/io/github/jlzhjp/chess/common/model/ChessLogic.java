package io.github.jlzhjp.chess.common.model;

import java.util.List;
import java.util.Map;

public interface ChessLogic {
    int getBoardWidth();

    int getBoardHeight();

    List<ChessBoardPoint> getAvailablePointForPieceAt(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point);
}
