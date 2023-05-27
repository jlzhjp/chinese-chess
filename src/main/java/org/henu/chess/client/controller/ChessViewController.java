package org.henu.chess.client.controller;

import org.henu.chess.client.model.GameInfo;
import org.henu.chess.client.view.ChessWindow;
import org.henu.chess.common.SocketMessageReceiver;
import org.henu.chess.common.messages.request.MovePieceRequest;
import org.henu.chess.common.model.ChessBoardPoint;
import org.henu.chess.common.model.ChessLogicImpl;
import org.henu.chess.common.model.ChessPanelModel;
import org.henu.chess.common.model.Piece;

import java.util.Objects;

public class ChessViewController {
    ChessPanelModel chessModel;
    ChessWindow view;

    GameInfo gameInfo;

    SocketMessageReceiver receiver;

    boolean canMove;

    public ChessViewController(ChessWindow view, GameInfo gameInfo, SocketMessageReceiver receiver) {
        this.view = view;
        this.chessModel = new ChessPanelModel(new ChessLogicImpl());
        this.gameInfo = gameInfo;
        this.receiver = receiver;

        this.view.getRedPlayerLabel().setText("红方: " + gameInfo.getRedPlayer());
        this.view.getBlackPlayerLabel().setText("黑方: " + gameInfo.getBlackPlayer());

        initializeChessModel(chessModel);
        view.getChessPanel().setModel(chessModel);
    }

    private void initializeChessModel(ChessPanelModel model) {
        if (gameInfo.isRed()) {
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
        } else {
            model.put(new ChessBoardPoint(0, 0), Piece.RED_CHARIOT);
            model.put(new ChessBoardPoint(1, 0), Piece.RED_HORSE);
            model.put(new ChessBoardPoint(2, 0), Piece.RED_ELEPHANT);
            model.put(new ChessBoardPoint(3, 0), Piece.RED_GUARD);
            model.put(new ChessBoardPoint(4, 0), Piece.RED_GENERAL);
            model.put(new ChessBoardPoint(5, 0), Piece.RED_GUARD);
            model.put(new ChessBoardPoint(6, 0), Piece.RED_ELEPHANT);
            model.put(new ChessBoardPoint(7, 0), Piece.RED_HORSE);
            model.put(new ChessBoardPoint(8, 0), Piece.RED_CHARIOT);
            model.put(new ChessBoardPoint(1, 2), Piece.RED_CANNON);
            model.put(new ChessBoardPoint(7, 2), Piece.RED_CANNON);
            model.put(new ChessBoardPoint(0, 3), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(2, 3), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(4, 3), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(6, 3), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(8, 3), Piece.RED_SOLDIER);

            model.put(new ChessBoardPoint(0, 9), Piece.BLACK_CHARIOT);
            model.put(new ChessBoardPoint(1, 9), Piece.BLACK_HORSE);
            model.put(new ChessBoardPoint(2, 9), Piece.BLACK_ELEPHANT);
            model.put(new ChessBoardPoint(3, 9), Piece.BLACK_GUARD);
            model.put(new ChessBoardPoint(4, 9), Piece.BLACK_GENERAL);
            model.put(new ChessBoardPoint(5, 9), Piece.BLACK_GUARD);
            model.put(new ChessBoardPoint(6, 9), Piece.BLACK_ELEPHANT);
            model.put(new ChessBoardPoint(7, 9), Piece.BLACK_HORSE);
            model.put(new ChessBoardPoint(8, 9), Piece.BLACK_CHARIOT);
            model.put(new ChessBoardPoint(1, 7), Piece.BLACK_CANNON);
            model.put(new ChessBoardPoint(7, 7), Piece.BLACK_CANNON);
            model.put(new ChessBoardPoint(0, 6), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(2, 6), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(4, 6), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(6, 6), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(8, 6), Piece.BLACK_SOLDIER);
        }

        view.getChessPanel().setListener(this::handleChessPanelClick);
    }

    private void handleChessPanelClick(ChessBoardPoint point) {
        if (!canMove) {
            return;
        }

        Piece selectedPiece = chessModel.pieceAt(point);

        if (point.equals(chessModel.getSelectedPoint())) {
            chessModel.unselect();
        } else if (Objects.nonNull(selectedPiece) && selectedPiece.isRed() == gameInfo.isRed()) {
            chessModel.select(point);
        } else if (Objects.nonNull(chessModel.getSelectedPoint()) && chessModel.getAvailableMoves().contains(point)) {
            var from = chessModel.getSelectedPoint();

            chessModel.move(from, point);
            chessModel.unselect();
            requestMove(from, point);
        } else {
            chessModel.unselect();
        }
    }

    private void requestMove(ChessBoardPoint from, ChessBoardPoint to) {
        MovePieceRequest request = new MovePieceRequest();

        request.setFrom(from);
        request.setTo(to);

        this.receiver.send(request);
    }
}
