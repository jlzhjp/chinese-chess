package io.github.jlzhjp.chess.client.controller;

import io.github.jlzhjp.chess.client.model.GameInfo;
import io.github.jlzhjp.chess.client.view.ChessWindow;
import io.github.jlzhjp.chess.client.view.LoginWindow;
import io.github.jlzhjp.chess.common.*;
import io.github.jlzhjp.chess.common.messages.Result;
import io.github.jlzhjp.chess.common.messages.request.AdmitDefeatRequest;
import io.github.jlzhjp.chess.common.messages.request.MovePieceRequest;
import io.github.jlzhjp.chess.common.messages.request.RetractReplyRequest;
import io.github.jlzhjp.chess.common.messages.request.RetractRequest;
import io.github.jlzhjp.chess.common.messages.response.GameOverResponse;
import io.github.jlzhjp.chess.common.messages.response.MovePieceResponse;
import io.github.jlzhjp.chess.common.messages.response.RetractResponse;
import io.github.jlzhjp.chess.common.model.ChessBoardPoint;
import io.github.jlzhjp.chess.common.model.ChessPanelModel;
import io.github.jlzhjp.chess.common.model.ChessRecord;
import io.github.jlzhjp.chess.common.model.Piece;

import javax.swing.*;
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
    private final ChessRecorder chessRecorder;
    private final MessageSink sink;
    MessageSocketManager socketManager;
    private Timer timer;
    private int remainingTime = 60;
    private boolean canMove;
    private boolean isGameOver;
    private ChessBoardPoint lastMoveFrom;
    private ChessBoardPoint lastMoveTo;
    private Piece pieceJustEaten;


    public ChessViewController(ChessWindow view, GameInfo gameInfo, MessageSocketManager socketManager) {
        this.view = view;
        this.chessModel = ChessPanelModel.initial(gameInfo.isRed());
        this.gameInfo = gameInfo;
        this.sink = socketManager.getSinkFromConnected();
        this.socketManager = socketManager;

        chessRecorder = new ChessRecorder(chessModel.getLogic().getBoardWidth(), chessModel.getLogic().getBoardHeight());

        timer = new Timer(1000, e -> {
            SwingUtilities.invokeLater(() -> view.getRemainingTimeLabel().setText(Integer.toString(remainingTime)));
            --remainingTime;
            if (remainingTime < 0) {
                sendAdmitDefeatRequest(sink);
                timer.stop();
            }
        });

        // 红方先行
        if (gameInfo.isRed()) {
            enterMyTurn();
        }


        if (Objects.equals(gameInfo.getUserName(), gameInfo.getRedPlayer())) {
            view.getRedPlayerLabel().setText("红方: " + gameInfo.getRedPlayer() + "(*)");
            view.getBlackPlayerLabel().setText("黑方: " + gameInfo.getBlackPlayer());

            view.getRedPlayerLabel().setFont(view.getDefaultBoldFont());
        } else {
            view.getRedPlayerLabel().setText("红方: " + gameInfo.getRedPlayer());
            view.getBlackPlayerLabel().setText("黑方: " + gameInfo.getBlackPlayer() + "(*)");

            view.getBlackPlayerLabel().setFont(view.getDefaultBoldFont());
        }

        view.getWindow().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (!isGameOver) {
                    sendAdmitDefeatRequest(sink);
                }
                returnToLoginWindow();
            }
        });
        view.getChessPanel().setModel(chessModel);
        view.getChessPanel().setListener(this::handleChessPanelClicked);
        view.getAdmitDefeatButton().addActionListener(this::handleAdmitDefeatButtonClicked);
        view.getRetractButton().addActionListener(this::handleRetractButtonClicked);
        view.getRetractButton().setEnabled(false);
        view.getLogList().setCellRenderer(chessRecorder.getCellRenderer(view.getDefaultFont()));

        MessageListener listener = new MessageListener()
                .on(MovePieceResponse.class, this::handleMovePieceResponse)
                .on(RetractResponse.class, this::handleRetractResponse)
                .on(GameOverResponse.class, this::handleGameOverResponse);

        socketManager.setMessageListener(listener);
        socketManager.setErrorHandler(new DefaultErrorHandler(view));
    }

    private void handleMovePieceResponse(MovePieceResponse response, MessageSink sink) {
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
            addRecord(piece, response.getFrom(), response.getTo());
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

    private void handleGameOverResponse(GameOverResponse response, MessageSink sink) {
        leaveMyTurn();
        view.getRetractButton().setEnabled(false);
        isGameOver = true;

        SwingUtilities.invokeLater(() -> {
            String message = String.format("%s\n胜利者: %s!\n是否返回?", response.getMessage(), response.getWinner());
            int result = view.showConfirmDialog(message, "游戏结束");
            if (result == JOptionPane.YES_OPTION) {
                returnToLoginWindow();
            }
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

            sendMoveRequest(sink, from, point);
            addRecord(piece, from, point);
            leaveMyTurn();
        } else {
            chessModel.unselect();
        }
    }

    private void addRecord(Piece piece, ChessBoardPoint from, ChessBoardPoint to) {
        ChessRecord record = chessRecorder.add(piece, from, to);
        view.getLogListModel().addElement(record);
    }

    private void handleRetractButtonClicked(ActionEvent e) {
        sendRetractRequest(sink);
        view.getRetractButton().setEnabled(false);
    }

    private void handleRetractResponse(RetractResponse response, MessageSink sink) {
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
            sink.add(request);
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
            sendAdmitDefeatRequest(sink);
        }
    }

    private void sendRetractRequest(MessageSink sink) {
        RetractRequest request = new RetractRequest();
        request.setRoomID(gameInfo.getRoomID());
        request.setUserName(gameInfo.getUserName());
        request.setFrom(lastMoveTo);
        request.setTo(lastMoveFrom);
        request.setPieceJustEaten(pieceJustEaten);
        sink.add(request);
    }

    private void sendAdmitDefeatRequest(MessageSink sink) {
        AdmitDefeatRequest request = new AdmitDefeatRequest();
        request.setRoomID(gameInfo.getRoomID());
        request.setUserName(gameInfo.getUserName());
        sink.add(request);
    }

    private void sendMoveRequest(MessageSink sink, ChessBoardPoint from, ChessBoardPoint to) {
        MovePieceRequest request = new MovePieceRequest();

        request.setRoomID(gameInfo.getRoomID());
        request.setUserName(gameInfo.getUserName());
        request.setFrom(from);
        request.setTo(to);

        sink.add(request);
    }

    private void returnToLoginWindow() {
        view.close();
        view.dispose();
        LoginWindow loginWindow = new LoginWindow();
        loginWindow.getUserNameTextField().setText(gameInfo.getUserName());
        LoginViewController loginViewController = new LoginViewController(loginWindow, socketManager);
        loginWindow.show();
    }
}
