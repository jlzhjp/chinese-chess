package org.henu.chess.server.controller;

import org.henu.chess.common.MessageListener;
import org.henu.chess.common.SocketMessageReceiver;
import org.henu.chess.common.messages.Result;
import org.henu.chess.common.messages.request.AdmitDefeatRequest;
import org.henu.chess.common.messages.request.CreateRoomRequest;
import org.henu.chess.common.messages.request.JoinRoomRequest;
import org.henu.chess.common.messages.request.MovePieceRequest;
import org.henu.chess.common.messages.response.*;
import org.henu.chess.server.model.GameTable;
import org.henu.chess.server.view.ServerWindow;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class ServerViewController {
    Random random = new Random();
    HashMap<String, GameTable> gameTables = new HashMap<>();
    ServerWindow view;
    ServerSocket serverSocket;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private final HashSet<String> usedRoomID = new HashSet<>();

    public ServerViewController(ServerWindow view) {
        this.view = view;
        view.getStartButton().addActionListener(this::handleStartButtonClicked);
        view.getStopButton().addActionListener(this::handleStopButtonClicked);
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
                    var listener = new MessageListener()
                            .on(CreateRoomRequest.class, (request) -> handleCreateRoomRequest(receiver, request))
                            .on(JoinRoomRequest.class, (request) -> handleJoinRoomRequest(receiver, request))
                            .on(MovePieceRequest.class, this::handleMovePieceRequest)
                            .on(AdmitDefeatRequest.class, this::handleAdmitDefeatRequest);
                    receiver.setListener(listener);
                    receiver.listen();
                } catch (IOException ex) {
                    view.showErrorMessageBox(ex.getMessage(), "错误");
                    break;
                }
            }
        }).start();
    }

    private void handleCreateRoomRequest(SocketMessageReceiver receiver, CreateRoomRequest request) {
        String roomID = generateRandomRoomID();
        CreateRoomResponse response = new CreateRoomResponse();
        response.setRoomID(roomID);
        response.setResult(Result.SUCCESS);
        response.setMessage("");
        GameTable table = new GameTable();
        table.setRow(view.getTableModel().getRowCount());
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

            view.getTableModel().setValueAt("游戏中", table.getRow(), 3);

            table.getRedPlayerReceiver().send(response);
            table.getBlackPlayerReceiver().send(response);
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

        if (Objects.equals(request.getUserName(), table.getRedPlayer())) {
            table.getBlackPlayerReceiver().send(response);
        } else {
            table.getRedPlayerReceiver().send(response);
        }
    }

    private void handleAdmitDefeatRequest(AdmitDefeatRequest request) {
        GameTable table = gameTables.get(request.getRoomID());

        if (Objects.isNull(table)) {
            return;
        }

        if (Objects.isNull(table.getRedPlayer()) || Objects.isNull(table.getBlackPlayer())) {
            return;
        }

        GameOverResponse response = new GameOverResponse();

        if (Objects.equals(request.getUserName(), table.getRedPlayer())) {
            response.setWinner(table.getBlackPlayer());
        } else {
            response.setWinner(table.getRedPlayer());
        }

        table.getBlackPlayerReceiver().send(response);
        table.getRedPlayerReceiver().send(response);
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
}
