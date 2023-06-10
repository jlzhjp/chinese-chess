package edu.henu.chineseChess.client.controller;

import edu.henu.chineseChess.client.model.GameInfo;
import edu.henu.chineseChess.client.view.ChessWindow;
import edu.henu.chineseChess.client.view.LoginWindow;
import edu.henu.chineseChess.client.view.WaitingWindow;
import edu.henu.chineseChess.common.DefaultErrorHandler;
import edu.henu.chineseChess.common.MessageListener;
import edu.henu.chineseChess.common.MessageSink;
import edu.henu.chineseChess.common.MessageSocketManager;
import edu.henu.chineseChess.common.messages.request.CreateRoomRequest;
import edu.henu.chineseChess.common.messages.request.JoinRoomRequest;
import edu.henu.chineseChess.common.messages.response.CreateRoomResponse;
import edu.henu.chineseChess.common.messages.response.JoinRoomResponse;
import edu.henu.chineseChess.common.messages.response.StartGameResponse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class LoginViewController {
    private final LoginWindow view;
    private final MessageSocketManager socketManager;
    private final MessageSink sink;
    private String roomID;
    private boolean isRoomCreator;

    public LoginViewController(LoginWindow view, MessageSocketManager socketManager) {
        this.view = view;
        this.socketManager = socketManager;
        this.sink = socketManager.getSinkFromConnected();

        view.getIPAddressTextField().setText(socketManager.getIPAddress().getHostAddress());
        view.getPortTextField().setText(Integer.toString(socketManager.getPort()));

        view.getCreateRoomButton().addActionListener(this::handleCreateRoomButtonClicked);
        view.getJoinRoomButton().addActionListener(this::handleJoinRoomButtonClicked);
        registerListeners();
    }

    private void registerListeners() {
        socketManager.setErrorHandler((e) -> {
            view.showErrorMessageBox(e.getMessage(), "连接失败");
        });

        socketManager.setMessageListener(new MessageListener()
                .on(CreateRoomResponse.class, this::handleCreateRoomResponse)
                .on(JoinRoomResponse.class, this::handleJoinRoomResponse)
                .on(StartGameResponse.class, this::handleStartGameResponse));
        socketManager.setErrorHandler(new DefaultErrorHandler(view));
    }

    private void handleJoinRoomButtonClicked(ActionEvent e) {
        isRoomCreator = false;
        String roomID = view.getRoomIDTextField().getText();
        String userName = view.getUserNameTextField().getText();

        if (Objects.equals(userName, "")) {
            view.showErrorMessageBox("请输入用户名", "创建房间失败");
            return;
        }

        if (roomID.isEmpty()) {
            view.showErrorMessageBox("请输入房间号", "加入房间失败");
            return;
        }

        JoinRoomRequest request = new JoinRoomRequest();
        this.roomID = roomID;
        request.setRoomID(roomID);
        request.setUserName(userName);
        sink.add(request);
    }

    private void handleCreateRoomButtonClicked(ActionEvent e) {
        isRoomCreator = true;

        String userName = view.getUserNameTextField().getText();
        if (Objects.equals(userName, "")) {
            view.showErrorMessageBox("请输入用户名", "创建房间失败");
            return;
        }

        CreateRoomRequest request = new CreateRoomRequest();
        sink.add(request);
    }

    private void handleCreateRoomResponse(CreateRoomResponse response, MessageSink sink) {
        roomID = response.getRoomID();
        String userName = view.getUserNameTextField().getText();

        switch (response.getResult()) {
            case SUCCESS: {
                JoinRoomRequest request = new JoinRoomRequest();
                request.setRoomID(response.getRoomID());
                request.setUserName(userName);
                sink.add(request);
                break;
            }
            case ERROR: {
                view.showErrorMessageBox(response.getMessage(), "创建房间失败");
                break;
            }
        }
    }

    private void handleStartGameResponse(StartGameResponse response, MessageSink sink) {
        String userName = view.getUserNameTextField().getText();
        SwingUtilities.invokeLater(() -> {
            view.close();

            ChessWindow chessWindow = new ChessWindow();

            GameInfo info = new GameInfo();

            info.setRoomID(roomID);
            info.setUserName(userName);
            info.setBlackPlayer(response.getBlackPlayerName());
            info.setRedPlayer(response.getRedPlayerName());

            ChessViewController controller = new ChessViewController(chessWindow, info, socketManager, () -> {
                SwingUtilities.invokeLater(view::show);
            });
            chessWindow.show();
        });
    }

    private void handleJoinRoomResponse(JoinRoomResponse response, MessageSink sink) {
        switch (response.getResult()) {
            case SUCCESS: {
                if (isRoomCreator) {
                    SwingUtilities.invokeLater(() -> {
                        view.close();

                        String userName = view.getUserNameTextField().getText();
                        // show waiting window
                        WaitingWindow waitingWindow = new WaitingWindow();
                        WaitingViewController waitingViewController = new WaitingViewControllerBuilder()
                                .setView(waitingWindow)
                                .setUserName(userName)
                                .setRoomID(roomID)
                                .setSocketManager(socketManager)
                                .setOnBack(() -> {
                                    view.show();
                                    registerListeners();
                                })
                                .build();
                        waitingWindow.show();
                    });
                }
                break;
            }
            case ERROR: {
                SwingUtilities.invokeLater(() -> view.showErrorMessageBox(response.getMessage(), "加入房间失败"));
                break;
            }
        }
    }
}
