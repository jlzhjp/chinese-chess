package org.henu.chess.common.model;

import org.henu.chess.common.Model;

import java.util.*;

public class ChessPanelModel extends Model {
    private final HashMap<ChessBoardPoint, Piece> pieces = new HashMap<>();
    private final ArrayList<ChessBoardPoint> availableMoves = new ArrayList<>();
    private ChessBoardPoint selectedPoint;

    private final ChessLogic logic;

    public ChessPanelModel(ChessLogic logic) {
       this.logic = logic;
    }

    public Map<ChessBoardPoint, Piece> getPieces() {
        return pieces;
    }

    public List<ChessBoardPoint> getAvailableMoves() {
        return availableMoves;
    }

    public ChessBoardPoint getSelectedPoint() {
        return selectedPoint;
    }

    public void select(ChessBoardPoint point) {
        if (pieces.containsKey(point)) {
            selectedPoint = point;

            availableMoves.clear();
            availableMoves.addAll(getAvailablePointForPieceAt(point));
            raisePropertyChange("availablePoints", null, availableMoves);
        }

    }

    public Piece pieceAt(ChessBoardPoint point) {
        return pieces.get(point);
    }

    public void put(ChessBoardPoint point, Piece piece) {
        pieces.put(point, piece);
        raisePropertyChange("pieces", null, pieces);
    }

    public void remove(ChessBoardPoint point) {
        pieces.remove(point);
        raisePropertyChange("pieces", null, pieces);
    }

    public List<ChessBoardPoint> getAvailablePointForPieceAt(ChessBoardPoint point) {
        return logic.getAvailablePointForPieceAt(pieces, point);
    }
}
