package edu.henu.chinesechess.client.controller;

import edu.henu.chinesechess.client.model.GameInfo;
import edu.henu.chinesechess.client.view.ChessWindow;
import edu.henu.chinesechess.common.SocketMessageReceiver;
import edu.henu.chinesechess.common.messages.Result;
import edu.henu.chinesechess.common.messages.request.AdmitDefeatRequest;
import edu.henu.chinesechess.common.messages.request.RetractRequest;
import edu.henu.chinesechess.common.messages.response.GameOverResponse;
import edu.henu.chinesechess.common.model.ChessBoardPoint;
import edu.henu.chinesechess.common.model.ChessPanelModel;
import edu.henu.chinesechess.common.model.Piece;
import edu.henu.chinesechess.common.MessageListener;
import edu.henu.chinesechess.common.messages.request.MovePieceRequest;
import edu.henu.chinesechess.common.messages.request.RetractReplyRequest;
import edu.henu.chinesechess.common.messages.response.MovePieceResponse;
import edu.henu.chinesechess.common.messages.response.RetractResponse;

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

    private final ChessPanelModel chessModel;
    private final ChessWindow view;
    private final GameInfo gameInfo;
    private final SocketMessageReceiver receiver;
    private Timer timer;
    private int remainingTime = 60;
    private boolean canMove;

    private ChessBoardPoint lastMoveFrom;
    private ChessBoardPoint lastMoveTo;
    private Piece pieceJustEaten;

    public ChessViewController(ChessWindow view, GameInfo gameInfo, SocketMessageReceiver receiver) {
        this.view = view;
        this.chessModel = ChessPanelModel.initial(gameInfo.isRed());
        this.gameInfo = gameInfo;
        this.receiver = receiver;

        timer = new Timer(1000, e -> {
            SwingUtilities.invokeLater(() -> view.getRemainingTimeLabel().setText(Integer.toString(remainingTime)));
            --remainingTime;
            if (remainingTime < 0) {
                sendAdmitDefeatRequest();
                timer.stop();
            }
        });

        // 红方先行
        if (gameInfo.isRed()) {
            enterMyTurn();
        }


        view.getRedPlayerLabel().setText("红方: " + gameInfo.getRedPlayer());
        view.getBlackPlayerLabel().setText("黑方: " + gameInfo.getBlackPlayer());

        if (Objects.equals(gameInfo.getUserName(), gameInfo.getRedPlayer())) {
            view.getRedPlayerLabel().setForeground(Color.RED);
        } else {
            view.getBlackPlayerLabel().setForeground(Color.RED);
        }

        view.getChessPanel().setModel(chessModel);
        view.getChessPanel().setListener(this::handleChessPanelClicked);
        view.getAdmitDefeatButton().addActionListener(this::handleAdmitDefeatButtonClicked);
        view.getRetractButton().addActionListener(this::handleRetractButtonClicked);
        view.getRetractButton().setEnabled(false);

        MessageListener listener = new MessageListener()
                .on(MovePieceResponse.class, this::handleMovePieceResponse)
                .on(RetractResponse.class, this::handleRetractResponse)
                .on(GameOverResponse.class, this::handleGameOverResponse);

        receiver.setListener(listener);
    }

    public static String convertToChinese(int n) {
        switch (n) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "七";
            case 8:
                return "八";
            case 9:
                return "九";
            default:
                return "";
        }
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

        enterMyTurn();
    }

    private void enterMyTurn() {
        canMove = true;
        view.getRetractButton().setEnabled(false);
        remainingTime = 60;
        timer.start();
    }

    private void leaveMyTurn() {
        canMove = false;
        view.getRetractButton().setEnabled(true);
        timer.stop();
        SwingUtilities.invokeLater(() -> view.getRemainingTimeLabel().setText("--"));
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
        leaveMyTurn();
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
            ChessBoardPoint from = chessModel.getSelectedPoint();
            Piece piece = chessModel.pieceAt(from);

            chessModel.move(from, point);
            chessModel.unselect();

            lastMoveFrom = from;
            lastMoveTo = point;
            pieceJustEaten = selectedPiece;

            sendMoveRequest(from, point);
            appendLog(piece, from, point);
            leaveMyTurn();
        } else {
            chessModel.unselect();
        }
    }

    private void handleRetractButtonClicked(ActionEvent e) {
        sendRetractRequest();
        view.getRetractButton().setEnabled(false);
    }

    private void handleRetractResponse(RetractResponse response) {
        if (canMove) {
            // 在自己的回合收到悔棋的响应，对方发送的悔棋请求
            RetractReplyRequest request = new RetractReplyRequest();
            int which = view.showConfirmDialog("对方请求悔棋, 是否同意?", "悔棋");

            request.setAgree(which == JOptionPane.YES_OPTION);
            request.setRoomID(gameInfo.getRoomID());
            request.setUserName(gameInfo.getUserName());
            request.setFrom(response.getFrom());
            request.setTo(response.getTo());
            request.setPieceJustEaten(response.getPieceJustEaten());

            if (request.isAgree()) {
                leaveMyTurn();
                chessModel.move(response.getFrom(), response.getTo());
                if (Objects.nonNull(response.getPieceJustEaten())) {
                    chessModel.put(response.getFrom(), response.getPieceJustEaten());
                }
            }
            receiver.send(request);
        } else {
            // 自己发送的悔棋请求
            if (response.getResult() != Result.SUCCESS) {
                SwingUtilities.invokeLater(() -> {
                    view.showErrorMessageBox("悔棋失败: " + response.getMessage(), "错误");
                });
            } else {
                chessModel.move(response.getFrom(), response.getTo());
                if (Objects.nonNull(response.getPieceJustEaten())) {
                    chessModel.put(response.getFrom(), response.getPieceJustEaten());
                }
                enterMyTurn();
                SwingUtilities.invokeLater(() -> {
                    view.showInfoMessageBox("悔棋成功: " + response.getMessage(), "成功");
                });
            }
        }
    }

    private void handleAdmitDefeatButtonClicked(ActionEvent e) {
        int result = view.showConfirmDialog("确定认输吗?", "认输");
        if (result == JOptionPane.YES_OPTION) {
            sendAdmitDefeatRequest();
        }
    }

    private void sendRetractRequest() {
        RetractRequest request = new RetractRequest();
        request.setRoomID(gameInfo.getRoomID());
        request.setUserName(gameInfo.getUserName());
        request.setFrom(lastMoveTo);
        request.setTo(lastMoveFrom);
        request.setPieceJustEaten(pieceJustEaten);
        receiver.send(request);
    }

    private void sendAdmitDefeatRequest() {
        AdmitDefeatRequest request = new AdmitDefeatRequest();
        request.setRoomID(gameInfo.getRoomID());
        request.setUserName(gameInfo.getUserName());
        receiver.send(request);
    }

    private void sendMoveRequest(ChessBoardPoint from, ChessBoardPoint to) {
        MovePieceRequest request = new MovePieceRequest();

        request.setRoomID(gameInfo.getRoomID());
        request.setUserName(gameInfo.getUserName());
        request.setFrom(from);
        request.setTo(to);

        receiver.send(request);
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
            case RED_GUARD:
            case BLACK_GUARD:
            case RED_HORSE:
            case BLACK_HORSE:
            case RED_ELEPHANT:
            case BLACK_ELEPHANT: {
                assert dy != 0;

                result.append(startXChinese);
                if (dy > 0) {
                    result.append("进").append(endXChinese);
                } else {
                    result.append("退").append(endXChinese);
                }
                break;
            }
            default: {
                result.append(startXChinese);

                if (dy > 0) {
                    result.append("进").append(dyChinese);
                } else if (dy < 0) {
                    result.append("退").append(dyChinese);
                } else {
                    result.append("平").append(endXChinese);
                }
                break;
            }
        }

        view.appendToLog(result.toString());
    }
}
