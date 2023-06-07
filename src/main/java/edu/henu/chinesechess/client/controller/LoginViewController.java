package edu.henu.chinesechess.client.controller;

import edu.henu.chinesechess.client.model.GameInfo;
import edu.henu.chinesechess.client.view.ChessWindow;
import edu.henu.chinesechess.client.view.LoginWindow;
import edu.henu.chinesechess.client.view.WaitingWindow;
import edu.henu.chinesechess.common.MessageListener;
import edu.henu.chinesechess.common.SocketMessageReceiver;
import edu.henu.chinesechess.common.messages.request.CreateRoomRequest;
import edu.henu.chinesechess.common.messages.request.JoinRoomRequest;
import edu.henu.chinesechess.common.messages.response.CreateRoomResponse;
import edu.henu.chinesechess.common.messages.response.JoinRoomResponse;
import edu.henu.chinesechess.common.messages.response.StartGameResponse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class LoginViewController {
    private final LoginWindow view;
    private final SocketMessageReceiver receiver;
    private String roomID;
    private boolean isRoomCreator;

    public LoginViewController(LoginWindow view, SocketMessageReceiver receiver) {
        this.view = view;
        this.receiver = receiver;

        view.getIPAddressTextField().setText(receiver.getIPAddress());
        view.getPortTextField().setText(Integer.toString(receiver.getPort()));

        view.getCreateRoomButton().addActionListener(this::handleCreateRoomButtonClicked);
        view.getJoinRoomButton().addActionListener(this::handleJoinRoomButtonClicked);

        receiver.setListener(new MessageListener()
                .on(CreateRoomResponse.class, this::handleCreateRoomResponse)
                .on(JoinRoomResponse.class, this::handleJoinRoomResponse)
                .on(StartGameResponse.class, this::handleStartGameResponse));
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
        receiver.send(request);
    }

    private void handleCreateRoomButtonClicked(ActionEvent e) {
        isRoomCreator = true;

        String userName = view.getUserNameTextField().getText();
        if (Objects.equals(userName, "")) {
            view.showErrorMessageBox("请输入用户名", "创建房间失败");
            return;
        }

        CreateRoomRequest request = new CreateRoomRequest();
        receiver.send(request);
    }

    private void handleCreateRoomResponse(CreateRoomResponse response) {
        roomID = response.getRoomID();
        String userName = view.getUserNameTextField().getText();

        switch (response.getResult()) {
            case SUCCESS: {
                JoinRoomRequest request = new JoinRoomRequest();
                request.setRoomID(response.getRoomID());
                request.setUserName(userName);
                receiver.send(request);
                break;
            }
            case ERROR: {
                view.showErrorMessageBox(response.getMessage(), "创建房间失败");
                break;
            }
        }
    }

    private void handleStartGameResponse(StartGameResponse response) {
        String userName = view.getUserNameTextField().getText();
        SwingUtilities.invokeLater(() -> {
            view.close();

            ChessWindow chessWindow = new ChessWindow();

            GameInfo info = new GameInfo();

            info.setRoomID(roomID);
            info.setUserName(userName);
            info.setBlackPlayer(response.getBlackPlayerName());
            info.setRedPlayer(response.getRedPlayerName());

            ChessViewController controller = new ChessViewController(chessWindow, info, receiver, () -> {
                SwingUtilities.invokeLater(view::show);
            });
            chessWindow.show();
        });
    }

    private void handleJoinRoomResponse(JoinRoomResponse response) {
        switch (response.getResult()) {
            case SUCCESS: {
                if (isRoomCreator) {
                    SwingUtilities.invokeLater(() -> {
                        view.close();

                        String userName = view.getUserNameTextField().getText();
                        // show waiting window
                        WaitingWindow waitingWindow = new WaitingWindow();
                        WaitingViewController waitingViewController = new WaitingViewController(receiver, waitingWindow, userName, roomID, view::show);
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
