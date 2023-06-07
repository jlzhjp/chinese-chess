package edu.henu.chinesechess.server.controller;

import edu.henu.chinesechess.common.GameStatus;
import edu.henu.chinesechess.common.MessageListener;
import edu.henu.chinesechess.common.SocketMessageReceiver;
import edu.henu.chinesechess.common.messages.Message;
import edu.henu.chinesechess.common.messages.Result;
import edu.henu.chinesechess.common.messages.request.*;
import edu.henu.chinesechess.common.messages.response.*;
import edu.henu.chinesechess.common.model.ChessPanelModel;
import edu.henu.chinesechess.common.model.Piece;
import edu.henu.chinesechess.server.model.GameTable;
import edu.henu.chinesechess.server.view.ServerWindow;
import edu.henu.chinesechess.server.view.WatchBattleWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
    ServerSocket serverSocket;

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
        int port = Integer.parseInt(view.getPortTextField().getText());
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            view.showErrorMessageBox("端口被占用", "错误");
            return;
        }

        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    SocketMessageReceiver receiver = new SocketMessageReceiver(socket);
                    MessageListener listener = new MessageListener()
                            .on(CreateRoomRequest.class, (request) -> handleCreateRoomRequest(receiver, request))
                            .on(JoinRoomRequest.class, (request) -> handleJoinRoomRequest(receiver, request))
                            .on(MovePieceRequest.class, this::handleMovePieceRequest)
                            .on(AdmitDefeatRequest.class, this::handleAdmitDefeatRequest)
                            .on(RetractRequest.class, this::handleRetractRequest)
                            .on(RetractReplyRequest.class, this::handleRetractReplyRequest);

                    receiver.setListener(listener);
                    receiver.listen();
                } catch (IOException ex) {
                    view.showErrorMessageBox(ex.getMessage(), "错误");
                    break;
                }
            }
        }).start();
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
            response.setResult(Result.SUCCESS);
            response.setMessage("对方已拒绝");
        }

        sendToAnother(gameTable, request.getUserName(), response);
    }

    private void handleCreateRoomRequest(SocketMessageReceiver receiver, CreateRoomRequest request) {
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
        receiver.send(response);
    }

    private void handleJoinRoomRequest(SocketMessageReceiver receiver, JoinRoomRequest request) {
        GameTable table = gameTables.get(request.getRoomID());
        if (Objects.isNull(table)) {
            JoinRoomResponse response = new JoinRoomResponse();
            response.setResult(Result.ERROR);
            response.setMessage("房间不存在");
            receiver.send(response);
            return;
        }

        if (Objects.isNull(table.getRedPlayer())) {
            table.setRedPlayer(request.getUserName());
            table.setRedPlayerReceiver(receiver);
            JoinRoomResponse response = new JoinRoomResponse();
            response.setResult(Result.SUCCESS);
            view.getTableModel().setValueAt(request.getUserName(), table.getRow(), 1);
            receiver.send(response);
        } else if (Objects.isNull(table.getBlackPlayer())) {
            table.setBlackPlayer(request.getUserName());
            table.setBlackPlayerReceiver(receiver);
            JoinRoomResponse response = new JoinRoomResponse();
            response.setResult(Result.SUCCESS);
            view.getTableModel().setValueAt(request.getUserName(), table.getRow(), 2);
            receiver.send(response);
        } else {
            JoinRoomResponse response = new JoinRoomResponse();
            response.setResult(Result.ERROR);
            response.setMessage("房间已满");
            receiver.send(response);
        }

        if (Objects.nonNull(table.getRedPlayer()) && Objects.nonNull(table.getBlackPlayer())) {
            StartGameResponse response = new StartGameResponse();
            response.setRedPlayerName(table.getRedPlayer());
            response.setBlackPlayerName(table.getBlackPlayer());
            response.setResult(Result.SUCCESS);

            table.getRedPlayerReceiver().send(response);
            table.getBlackPlayerReceiver().send(response);

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
        table.getRedPlayerReceiver().send(gameOverResponse);
        table.getBlackPlayerReceiver().send(gameOverResponse);
    }

    private void handleStopButtonClicked(ActionEvent e) {
        view.getStartButton().setEnabled(true);
        view.getStopButton().setEnabled(false);
        try {
            serverSocket.close();
        } catch (IOException ignored) {
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
            gameTable.getBlackPlayerReceiver().send(message);
        } else if (Objects.equals(currentUserName, gameTable.getBlackPlayer())) {
            gameTable.getRedPlayerReceiver().send(message);
        }
    }
}
