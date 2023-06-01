package org.henu.chess.client.controller;

import org.henu.chess.client.model.GameInfo;
import org.henu.chess.client.view.ChessWindow;
import org.henu.chess.common.MessageListener;
import org.henu.chess.common.SocketMessageReceiver;
import org.henu.chess.common.messages.Result;
import org.henu.chess.common.messages.request.MovePieceRequest;
import org.henu.chess.common.messages.response.GameOverResponse;
import org.henu.chess.common.messages.response.MovePieceResponse;
import org.henu.chess.common.model.ChessBoardPoint;
import org.henu.chess.common.model.ChessPanelModel;
import org.henu.chess.common.model.Piece;

import javax.swing.*;
import java.util.Objects;

public class ChessViewController {
    ChessPanelModel chessModel;
    ChessWindow view;
    GameInfo gameInfo;
    SocketMessageReceiver receiver;
    boolean canMove;

    public ChessViewController(ChessWindow view, GameInfo gameInfo, SocketMessageReceiver receiver) {
        this.view = view;
        this.chessModel = ChessPanelModel.initial(gameInfo.isRed());
        this.gameInfo = gameInfo;
        this.receiver = receiver;

        // 红方先行
        this.canMove = gameInfo.isRed();

        view.getRedPlayerLabel().setText("红方: " + gameInfo.getRedPlayer());
        view.getBlackPlayerLabel().setText("黑方: " + gameInfo.getBlackPlayer());

        view.getChessPanel().setListener(this::handleChessPanelClick);
        view.getChessPanel().setModel(chessModel);

        var listener = new MessageListener()
                .on(MovePieceResponse.class, this::handleMovePieceResponse)
                .on(GameOverResponse.class, this::handleGameOverResponse);

        receiver.setListener(listener);
    }

    private void handleMovePieceResponse(MovePieceResponse response) {
        if (response.getResult() == Result.SUCCESS) {
            if (Objects.nonNull(response.getFrom()) && Objects.nonNull(response.getTo())) {
                SwingUtilities.invokeLater(() -> {
                    Piece piece = chessModel.pieceAt(response.getFrom());
                    chessModel.move(response.getFrom(), response.getTo());
                    appendLog(piece, response.getFrom(), response.getTo());
                });
                canMove = true;
            }
        }
    }

    private void handleGameOverResponse(GameOverResponse response) {
        canMove = false;
        receiver.close();
        SwingUtilities.invokeLater(() -> {
            view.showInfoMessageBox("胜利者: " + response.getWinner(), "游戏结束");
        });
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
            var piece = chessModel.pieceAt(from);
            chessModel.move(from, point);
            chessModel.unselect();
            sendMoveRequest(piece, from, point);
        } else {
            chessModel.unselect();
        }
    }

    private void sendMoveRequest(Piece piece, ChessBoardPoint from, ChessBoardPoint to) {
        MovePieceRequest request = new MovePieceRequest();

        request.setRoomID(gameInfo.getRoomID());
        request.setUserName(gameInfo.getUserName());
        request.setFrom(from);
        request.setTo(to);

        receiver.send(request);
        appendLog(piece, from, to);
        canMove = false;
    }

    private void appendLog(Piece piece, ChessBoardPoint from, ChessBoardPoint to) {
        StringBuilder result = new StringBuilder();
        result.append(piece.getSymbol());

        int startX, endX;

        if (piece.isRed()) {
            startX = chessModel.getLogic().getBoardWidth() - from.getX();
            endX = chessModel.getLogic().getBoardWidth() - to.getX();
        } else {
            startX = from.getX() + 1;
            endX = to.getX() + 1;
        }

        String startXChinese = piece.isRed() ? convertToChinese(startX) : Integer.toString(startX);
        String endXChinese = piece.isRed() ? convertToChinese(endX) : Integer.toString(endX);

        int dy = to.getY() - from.getY();
        if (piece.isRed()) {
            dy = -dy;
        }

        String dyChinese = piece.isRed() ? convertToChinese(dy) : Integer.toString(dy);

        switch (piece) {
            case RED_SOLDIER, BLACK_SOLDIER, RED_HORSE, BLACK_HORSE, RED_ELEPHANT, BLACK_ELEPHANT -> {
                assert dy != 0;

                result.append(startXChinese);
                if (dy > 0) {
                    result.append("进").append(endXChinese);
                } else {
                    result.append("退").append(endXChinese);
                }
            }
            default -> {
                result.append(startXChinese);

                if (dy > 0) {
                    result.append("进").append(dyChinese);
                } else if (dy < 0) {
                    result.append("退").append(dyChinese);
                } else {
                    result.append("平").append(endXChinese);
                }
            }
        }

        view.appendToLog(result.toString());
    }


    public static String convertToChinese(int n) {
        return switch (n) {
            case 1 -> "一";
            case 2 -> "二";
            case 3 -> "三";
            case 4 -> "四";
            case 5 -> "五";
            case 6 -> "六";
            case 7 -> "七";
            case 8 -> "八";
            case 9 -> "九";
            default -> "";
        };
    }
}

