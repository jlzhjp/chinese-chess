package org.henu.chess.client.controller;

import org.henu.chess.client.model.GameInfo;
import org.henu.chess.client.view.ChessWindow;
import org.henu.chess.common.MessageListener;
import org.henu.chess.common.SocketMessageReceiver;
import org.henu.chess.common.messages.Result;
import org.henu.chess.common.messages.request.AdmitDefeatRequest;
import org.henu.chess.common.messages.request.MovePieceRequest;
import org.henu.chess.common.messages.response.GameOverResponse;
import org.henu.chess.common.messages.response.MovePieceResponse;
import org.henu.chess.common.model.ChessBoardPoint;
import org.henu.chess.common.model.ChessPanelModel;
import org.henu.chess.common.model.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChessViewController {
    private static final HashSet<Piece> possibleCheckMatePieces = new HashSet<>();

    static {
        possibleCheckMatePieces.add(Piece.RED_CHARIOT);
        possibleCheckMatePieces.add(Piece.RED_CANNON);
        possibleCheckMatePieces.add(Piece.RED_HORSE);
        possibleCheckMatePieces.add(Piece.RED_SOLDIER);

        possibleCheckMatePieces.add(Piece.BLACK_CHARIOT);
        possibleCheckMatePieces.add(Piece.BLACK_CANNON);
        possibleCheckMatePieces.add(Piece.BLACK_HORSE);
        possibleCheckMatePieces.add(Piece.BLACK_SOLDIER);
    }

    ChessPanelModel chessModel;
    ChessWindow view;
    GameInfo gameInfo;
    SocketMessageReceiver receiver;
    Timer timer;
    int remainingTime = 60;
    boolean canMove;

    public ChessViewController(ChessWindow view, GameInfo gameInfo, SocketMessageReceiver receiver) {
        this.view = view;
        this.chessModel = ChessPanelModel.initial(gameInfo.isRed());
        this.gameInfo = gameInfo;
        this.receiver = receiver;

        // 红方先行
        this.canMove = gameInfo.isRed();

        timer = new Timer(1000, e -> {
            if (canMove) {
                --remainingTime;
                SwingUtilities.invokeLater(() -> view.getRemainingTimeLabel().setText(Integer.toString(remainingTime)));
                if (remainingTime <= 0) {
                    sendAdmitDefeatRequest();
                    timer.stop();
                }
            } else {
                remainingTime = 60;
                SwingUtilities.invokeLater(() -> view.getRemainingTimeLabel().setText("--"));
            }
        });

        timer.start();

        view.getRedPlayerLabel().setText("红方: " + gameInfo.getRedPlayer());
        view.getBlackPlayerLabel().setText("黑方: " + gameInfo.getBlackPlayer());

        if (Objects.equals(gameInfo.getUserName(), gameInfo.getRedPlayer())) {
            view.getRedPlayerLabel().setForeground(Color.RED);
        } else {
            view.getBlackPlayerLabel().setForeground(Color.RED);
        }

        view.getChessPanel().setListener(this::handleChessPanelClicked);
        view.getChessPanel().setModel(chessModel);
        view.getAdmitDefeatButton().addActionListener(this::handleAdmitDefeatButtonClicked);

        var listener = new MessageListener()
                .on(MovePieceResponse.class, this::handleMovePieceResponse)
                .on(GameOverResponse.class, this::handleGameOverResponse);

        receiver.setListener(listener);
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

    private void handleMovePieceResponse(MovePieceResponse response) {
        if (response.getResult() != Result.SUCCESS) {
            return;
        }
        if (Objects.isNull(response.getFrom()) || Objects.isNull(response.getTo())) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            Piece piece = chessModel.pieceAt(response.getFrom());
            chessModel.move(response.getFrom(), response.getTo());

            if (isCheckMate()) {
                view.showInfoMessageBox("将军!", "警告");
            }
            appendLog(piece, response.getFrom(), response.getTo());
        });
        canMove = true;
    }

    private boolean isCheckMate() {
        Piece general = gameInfo.isRed() ? Piece.RED_GENERAL : Piece.BLACK_GENERAL;
        ChessBoardPoint generalPoint = chessModel.getPieces().entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), general))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        for (ChessBoardPoint point : chessModel.getPieces().keySet()) {
            Piece piece = chessModel.pieceAt(point);

            if (Objects.isNull(piece) || piece.isRed() == gameInfo.isRed()) {
                continue;
            }

            if (!possibleCheckMatePieces.contains(piece)) {
                continue;
            }

            List<ChessBoardPoint> availableMoves = chessModel.getLogic().getAvailablePointForPieceAt(chessModel.getPieces(), point);
            if (availableMoves.contains(generalPoint)) {
                return true;
            }
        }

        return false;
    }

    private void handleGameOverResponse(GameOverResponse response) {
        canMove = false;
        receiver.close();
        SwingUtilities.invokeLater(() -> {
            view.showInfoMessageBox("胜利者: " + response.getWinner(), "游戏结束");
        });
    }

    private void handleChessPanelClicked(ChessBoardPoint point) {
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

    private void handleAdmitDefeatButtonClicked(ActionEvent e) {
        int result = view.showConfirmDialog("确定认输吗?", "认输");
        if (result == JOptionPane.YES_OPTION) {
            sendAdmitDefeatRequest();
        }
    }

    private void sendAdmitDefeatRequest() {
        AdmitDefeatRequest request = new AdmitDefeatRequest();
        request.setRoomID(gameInfo.getRoomID());
        request.setUserName(gameInfo.getUserName());
        receiver.send(request);
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

        String dyChinese = piece.isRed() ? convertToChinese(Math.abs(dy)) : Integer.toString(Math.abs(dy));

        switch (piece) {
            case RED_GUARD, BLACK_GUARD, RED_HORSE, BLACK_HORSE, RED_ELEPHANT, BLACK_ELEPHANT -> {
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
}

