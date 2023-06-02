package org.henu.chess.server.controller;

import org.henu.chess.common.MessageListener;
import org.henu.chess.common.SocketMessageReceiver;
import org.henu.chess.common.messages.Result;
import org.henu.chess.common.messages.request.CreateRoomRequest;
import org.henu.chess.common.messages.request.JoinRoomRequest;
import org.henu.chess.common.messages.request.MovePieceRequest;
import org.henu.chess.common.messages.response.CreateRoomResponse;
import org.henu.chess.common.messages.response.JoinRoomResponse;
import org.henu.chess.common.messages.response.StartGameResponse;
import org.henu.chess.server.model.GameTable;
import org.henu.chess.server.view.ServerWindow;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class ServerViewController {
    Random random = new Random();
    HashMap<String, GameTable> gameTables = new HashMap<>();
    ServerWindow view;
    ServerSocket serverSocket;

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
                            .on(JoinRoomRequest.class, (request) -> handleJoinRoomRequest(receiver, request));
                    receiver.setListener(listener);
                    receiver.listen();
                } catch (IOException ex) {
                    view.showErrorMessageBox(ex.getMessage(), "错误");
                }
            }
        }).start();
    }

    private void handleCreateRoomRequest(SocketMessageReceiver receiver, CreateRoomRequest request) {
        String roomNumber = Integer.toString(random.nextInt());
        CreateRoomResponse response = new CreateRoomResponse();
        response.setRoomID(roomNumber);
        response.setResult(Result.SUCCESS);
        response.setMessage("");
        GameTable table = new GameTable();
        table.setRow(view.getTableModel().getRowCount());
        gameTables.put(roomNumber, table);
        view.getTableModel().addRow(new Object[]{roomNumber, "", "", "等待中"});
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
        }
    }

    private void handleMovePieceRequest(MovePieceRequest request) {
    }

    private void handleStopButtonClicked(ActionEvent e) {
        view.getStartButton().setEnabled(true);
        view.getStopButton().setEnabled(false);
    }
}
