package io.github.jlzhjp.chess.common.model;

import io.github.jlzhjp.chess.common.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessPanelModel extends Model {
    private final HashMap<ChessBoardPoint, Piece> pieces = new HashMap<>();
    private final ArrayList<ChessBoardPoint> availableMoves = new ArrayList<>();
    private final ChessLogic logic;
    private boolean isRed = true;
    private ChessBoardPoint selectedPoint;

    public ChessPanelModel(ChessLogic logic) {
        this.logic = logic;
    }

    public static ChessPanelModel initial(boolean isRed) {
        ChessPanelModel model = new ChessPanelModel(new ChessLogicImpl());

        model.setIsRed(isRed);

        model.put(new ChessBoardPoint(0, 9), Piece.RED_CHARIOT);
        model.put(new ChessBoardPoint(1, 9), Piece.RED_HORSE);
        model.put(new ChessBoardPoint(2, 9), Piece.RED_ELEPHANT);
        model.put(new ChessBoardPoint(3, 9), Piece.RED_GUARD);
        model.put(new ChessBoardPoint(4, 9), Piece.RED_GENERAL);
        model.put(new ChessBoardPoint(5, 9), Piece.RED_GUARD);
        model.put(new ChessBoardPoint(6, 9), Piece.RED_ELEPHANT);
        model.put(new ChessBoardPoint(7, 9), Piece.RED_HORSE);
        model.put(new ChessBoardPoint(8, 9), Piece.RED_CHARIOT);
        model.put(new ChessBoardPoint(1, 7), Piece.RED_CANNON);
        model.put(new ChessBoardPoint(7, 7), Piece.RED_CANNON);
        model.put(new ChessBoardPoint(0, 6), Piece.RED_SOLDIER);
        model.put(new ChessBoardPoint(2, 6), Piece.RED_SOLDIER);
        model.put(new ChessBoardPoint(4, 6), Piece.RED_SOLDIER);
        model.put(new ChessBoardPoint(6, 6), Piece.RED_SOLDIER);
        model.put(new ChessBoardPoint(8, 6), Piece.RED_SOLDIER);

        model.put(new ChessBoardPoint(0, 0), Piece.BLACK_CHARIOT);
        model.put(new ChessBoardPoint(1, 0), Piece.BLACK_HORSE);
        model.put(new ChessBoardPoint(2, 0), Piece.BLACK_ELEPHANT);
        model.put(new ChessBoardPoint(3, 0), Piece.BLACK_GUARD);
        model.put(new ChessBoardPoint(4, 0), Piece.BLACK_GENERAL);
        model.put(new ChessBoardPoint(5, 0), Piece.BLACK_GUARD);
        model.put(new ChessBoardPoint(6, 0), Piece.BLACK_ELEPHANT);
        model.put(new ChessBoardPoint(7, 0), Piece.BLACK_HORSE);
        model.put(new ChessBoardPoint(8, 0), Piece.BLACK_CHARIOT);
        model.put(new ChessBoardPoint(1, 2), Piece.BLACK_CANNON);
        model.put(new ChessBoardPoint(7, 2), Piece.BLACK_CANNON);
        model.put(new ChessBoardPoint(0, 3), Piece.BLACK_SOLDIER);
        model.put(new ChessBoardPoint(2, 3), Piece.BLACK_SOLDIER);
        model.put(new ChessBoardPoint(4, 3), Piece.BLACK_SOLDIER);
        model.put(new ChessBoardPoint(6, 3), Piece.BLACK_SOLDIER);
        model.put(new ChessBoardPoint(8, 3), Piece.BLACK_SOLDIER);
        return model;
    }

    public ChessLogic getLogic() {
        return logic;
    }

    public boolean isRed() {
        return isRed;
    }

    public void setIsRed(boolean isRed) {
        this.isRed = isRed;
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

    public void move(ChessBoardPoint from, ChessBoardPoint to) {
        Piece piece = pieces.get(from);
        if (piece == null) {
            throw new IllegalArgumentException("No piece at " + from);
        }
        pieces.remove(from);
        pieces.put(to, piece);
        raisePropertyChange("pieces", null, pieces);
    }

    public void unselect() {
        selectedPoint = null;
        availableMoves.clear();
        raisePropertyChange("selectedPoint", null, null);
        raisePropertyChange("availablePoints", null, availableMoves);
    }

    public List<ChessBoardPoint> getAvailablePointForPieceAt(ChessBoardPoint point) {
        return logic.getAvailablePointForPieceAt(pieces, point);
    }
}
