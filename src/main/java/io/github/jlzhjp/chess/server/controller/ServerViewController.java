package io.github.jlzhjp.chess.server.controller;

import io.github.jlzhjp.chess.common.*;
import io.github.jlzhjp.chess.common.messages.Message;
import io.github.jlzhjp.chess.common.messages.Result;
import io.github.jlzhjp.chess.common.messages.request.*;
import io.github.jlzhjp.chess.common.messages.response.*;
import io.github.jlzhjp.chess.common.model.ChessPanelModel;
import io.github.jlzhjp.chess.common.model.Piece;
import io.github.jlzhjp.chess.common.socketManager.SocketManager;
import io.github.jlzhjp.chess.server.model.GameTable;
import io.github.jlzhjp.chess.server.view.ServerWindow;
import io.github.jlzhjp.chess.server.view.WatchBattleWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.SocketException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class ServerViewController {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private final HashSet<String> usedRoomID = new HashSet<>();
    Random random = new Random();
    HashMap<String, GameTable> gameTables = new HashMap<>();
    ServerWindow view;
    MessageSocketManager messageSocketManager;

    public ServerViewController(ServerWindow view) {
        this.view = view;
        view.getStartButton().addActionListener(this::handleStartButtonClicked);
        view.getStopButton().addActionListener(this::handleStopButtonClicked);
        view.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);

                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String roomID = (String) table.getValueAt(row, 0);
                    GameTable gameTable = gameTables.get(roomID);

                    if (Objects.nonNull(gameTable) && gameTable.getStatus() == GameStatus.PLAYING) {
                        WatchBattleWindow watchBattleWindow = new WatchBattleWindow();
                        WatchBattleWindowController controller = new WatchBattleWindowController(watchBattleWindow, gameTable);
                        watchBattleWindow.show();
                    }
                }
            }
        });
    }

    private void handleStartButtonClicked(ActionEvent e) {
        view.getStartButton().setEnabled(false);
        view.getStopButton().setEnabled(true);
        view.getUseNIOCheckBox().setEnabled(false);

        int port = Integer.parseInt(view.getPortTextField().getText());

        MessageListener listener = new MessageListener()
                .on(CreateRoomRequest.class, this::handleCreateRoomRequest)
                .on(JoinRoomRequest.class, this::handleJoinRoomRequest)
                .on(MovePieceRequest.class, (message, sink) -> handleMovePieceRequest(message))
                .on(AdmitDefeatRequest.class, (message, sink) -> handleAdmitDefeatRequest(message))
                .on(RetractRequest.class, (message, sink) -> handleRetractRequest(message))
                .on(RetractReplyRequest.class, (message, sink) -> handleRetractReplyRequest(message));

        SocketManager socketManager = SocketManager.listen(port, view.getUseNIOCheckBox().isSelected());

        messageSocketManager = new MessageSocketManager(socketManager);
        messageSocketManager.setMessageListener(listener);
        messageSocketManager.setErrorHandler(new DefaultErrorHandler(view) {
            @Override
            public void onError(Exception ex) {
                if (ex instanceof SocketException && ex.getMessage().equals("Connection reset")) {
                    return;
                }
                super.onError(ex);
            }
        });

        try {
            messageSocketManager.start();
        } catch (Exception Exception) {
            SwingUtilities.invokeLater(() -> {
                view.showErrorMessageBox(Exception.getMessage(), "启动失败");
            });
        }
    }

    private void handleRetractRequest(RetractRequest request) {
        GameTable gameTable = gameTables.get(request.getRoomID());
        if (Objects.isNull(gameTable)) {
            return;
        }

        RetractResponse response = new RetractResponse();
        response.setResult(Result.SUCCESS);
        response.setFrom(request.getFrom());
        response.setTo(request.getTo());
        response.setPieceJustEaten(request.getPieceJustEaten());

        sendToAnother(gameTable, request.getUserName(), response);
    }

    private void handleRetractReplyRequest(RetractReplyRequest request) {
        GameTable gameTable = gameTables.get(request.getRoomID());
        if (Objects.isNull(gameTable)) {
            return;
        }

        RetractResponse response = new RetractResponse();
        response.setFrom(request.getFrom());
        response.setTo(request.getTo());
        response.setPieceJustEaten(request.getPieceJustEaten());

        if (request.isAgree()) {
            response.setResult(Result.SUCCESS);
            response.setMessage("对方已同意");
            gameTable.getChessPanelModel().move(request.getFrom(), request.getTo());
            if (Objects.nonNull(request.getPieceJustEaten())) {
                gameTable.getChessPanelModel().put(request.getFrom(), request.getPieceJustEaten());
            }
        } else {
            response.setResult(Result.ERROR);
            response.setMessage("对方已拒绝");
        }

        sendToAnother(gameTable, request.getUserName(), response);
    }

    private void handleCreateRoomRequest(CreateRoomRequest request, MessageSink sink) {
        String roomID = generateRandomRoomID();
        CreateRoomResponse response = new CreateRoomResponse();
        response.setRoomID(roomID);
        response.setResult(Result.SUCCESS);
        response.setMessage("");
        GameTable table = new GameTable();
        table.setRow(view.getTableModel().getRowCount());
        table.setStatus(GameStatus.WAITING_FOR_PLAYER);
        gameTables.put(roomID, table);
        view.getTableModel().addRow(new Object[]{roomID, "", "", "等待中"});
        sink.add(response);
    }

    private void handleJoinRoomRequest(JoinRoomRequest request, MessageSink sink) {
        GameTable table = gameTables.get(request.getRoomID());
        if (Objects.isNull(table)) {
            JoinRoomResponse response = new JoinRoomResponse();
            response.setResult(Result.ERROR);
            response.setMessage("房间不存在");
            sink.add(response);
            return;
        }

        if (Objects.isNull(table.getRedPlayer())) {
            table.setRedPlayer(request.getUserName());
            table.setRedPlayerMessageSink(sink);
            JoinRoomResponse response = new JoinRoomResponse();
            response.setResult(Result.SUCCESS);
            view.getTableModel().setValueAt(request.getUserName(), table.getRow(), 1);
            sink.add(response);
        } else if (Objects.isNull(table.getBlackPlayer())) {
            table.setBlackPlayer(request.getUserName());
            table.setBlackPlayerMessageSink(sink);
            JoinRoomResponse response = new JoinRoomResponse();
            response.setResult(Result.SUCCESS);
            view.getTableModel().setValueAt(request.getUserName(), table.getRow(), 2);
            sink.add(response);
        } else {
            JoinRoomResponse response = new JoinRoomResponse();
            response.setResult(Result.ERROR);
            response.setMessage("房间已满");
            sink.add(response);
        }

        if (Objects.nonNull(table.getRedPlayer()) && Objects.nonNull(table.getBlackPlayer())) {
            StartGameResponse response = new StartGameResponse();
            response.setRedPlayerName(table.getRedPlayer());
            response.setBlackPlayerName(table.getBlackPlayer());
            response.setResult(Result.SUCCESS);

            table.getRedPlayerMessageSink().add(response);
            table.getBlackPlayerMessageSink().add(response);

            setGameStatus(table, "游戏中");
            table.setStatus(GameStatus.PLAYING);
            table.setChessPanelModel(ChessPanelModel.initial(true));
        }
    }

    private void handleMovePieceRequest(MovePieceRequest request) {
        GameTable table = gameTables.get(request.getRoomID());

        if (Objects.isNull(table)) {
            return;
        }

        if (Objects.isNull(table.getRedPlayer()) || Objects.isNull(table.getBlackPlayer())) {
            return;
        }

        MovePieceResponse response = new MovePieceResponse();

        response.setResult(Result.SUCCESS);
        response.setFrom(request.getFrom());
        response.setTo(request.getTo());

        sendToAnother(table, request.getUserName(), response);

        Piece toPiece = table.getChessPanelModel().pieceAt(request.getTo());
        if (toPiece == Piece.BLACK_GENERAL || toPiece == Piece.RED_GENERAL) {
            String winner = request.getUserName();
            sendGameOverResponseToAll(table, request.getUserName(), "将死");
            setGameStatus(table, "胜方: " + winner);
            return;
        }

        table.getChessPanelModel().move(request.getFrom(), request.getTo());
    }

    private void handleAdmitDefeatRequest(AdmitDefeatRequest request) {
        GameTable table = gameTables.get(request.getRoomID());

        if (Objects.isNull(table)) {
            return;
        }

        if (Objects.isNull(table.getRedPlayer()) || Objects.isNull(table.getBlackPlayer())) {
            return;
        }

        String winner;
        if (Objects.equals(request.getUserName(), table.getRedPlayer())) {
            winner = table.getBlackPlayer();
        } else {
            winner = table.getRedPlayer();
        }

        sendGameOverResponseToAll(table, winner, request.getUserName() + " 已认输");
        setGameStatus(table, "胜方: " + winner);
    }

    public void sendGameOverResponseToAll(GameTable table, String winner, String message) {
        GameOverResponse gameOverResponse = new GameOverResponse();
        gameOverResponse.setWinner(winner);
        gameOverResponse.setMessage(message);
        table.getRedPlayerMessageSink().add(gameOverResponse);
        table.getBlackPlayerMessageSink().add(gameOverResponse);
    }

    private void handleStopButtonClicked(ActionEvent e) {
        view.getStartButton().setEnabled(true);
        view.getStopButton().setEnabled(false);
        view.getUseNIOCheckBox().setEnabled(true);

        try {
            if (messageSocketManager != null) {
                messageSocketManager.close();
            }
        } catch (Exception ignored) {
        }
    }

    private String generateRandomRoomID() {
        StringBuilder sb = new StringBuilder();

        while (sb.length() < 5) {
            int index = random.nextInt(CHARACTERS.length());
            char c = CHARACTERS.charAt(index);
            if (sb.indexOf(String.valueOf(c)) == -1) {
                sb.append(c);
            }
        }

        String result = sb.toString();
        if (usedRoomID.contains(result)) {
            return generateRandomRoomID();
        } else {
            usedRoomID.add(result);
            return result;
        }
    }

    public void setGameStatus(GameTable table, String status) {
        view.getTableModel().setValueAt(status, table.getRow(), 3);
    }

    private void sendToAnother(GameTable gameTable, String currentUserName, Message message) {
        if (Objects.equals(currentUserName, gameTable.getRedPlayer())) {
            gameTable.getBlackPlayerMessageSink().add(message);
        } else if (Objects.equals(currentUserName, gameTable.getBlackPlayer())) {
            gameTable.getRedPlayerMessageSink().add(message);
        }
    }
}
