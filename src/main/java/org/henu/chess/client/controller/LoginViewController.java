package org.henu.chess.client.controller;

import org.henu.chess.client.view.LoginWindow;
import org.henu.chess.client.view.WaitingWindow;
import org.henu.chess.common.MessageListener;
import org.henu.chess.common.SocketMessageReceiver;
import org.henu.chess.common.messages.request.CreateRoomRequest;
import org.henu.chess.common.messages.request.JoinRoomRequest;
import org.henu.chess.common.messages.response.CreateRoomResponse;
import org.henu.chess.common.messages.response.JoinRoomResponse;
import org.henu.chess.common.messages.response.Response;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class LoginViewController {
    private final LoginWindow view;
    private final SocketMessageReceiver receiver;

    private String roomID;

    public LoginViewController(LoginWindow view, SocketMessageReceiver receiver) {
        this.view = view;
        this.receiver = receiver;

        view.getIPAddressTextField().setText(receiver.getIPAddress());
        view.getPortTextField().setText(Integer.toString(receiver.getPort()));

        view.getCreateRoomButton().addActionListener(this::handleCreateRoomButtonClick);
        view.getJoinRoomButton().addActionListener(this::handleJoinRoomButtonClick);

        receiver.setListener(new MessageListener() {
            @Override
            public void onResponse(Response response) {
                if (response instanceof CreateRoomResponse createRoomResponse) {
                    handleCreateRoomResponse(createRoomResponse);
                } else if (response instanceof JoinRoomResponse joinRoomResponse) {
                    handleJoinRoomResponse(joinRoomResponse);
                }
            }
        });
    }

    private void handleJoinRoomButtonClick(ActionEvent e) {
        String roomID = view.getRoomIDTextField().getText();

        if (roomID.isEmpty()) {
            view.showErrorMessageBox("请输入房间号", "加入房间失败");
            return;
        }
        JoinRoomRequest request = new JoinRoomRequest();
        request.setRoomID(roomID);
        request.setUserName(view.getUserNameTextField().getText());
        receiver.send(request);
    }

    private void handleCreateRoomButtonClick(ActionEvent e) {
        CreateRoomRequest request = new CreateRoomRequest();
        receiver.send(request);
    }

    private void handleCreateRoomResponse(CreateRoomResponse response) {
        roomID = response.getRoomID();
        String userName = view.getUserNameTextField().getText();

        switch (response.getResult()) {
            case SUCCESS -> {
                JoinRoomRequest request = new JoinRoomRequest();
                request.setRoomID(response.getRoomID());
                request.setUserName(userName);
                receiver.send(request);
            }
            case ERROR -> {
                view.showErrorMessageBox(response.getMessage(), "创建房间失败");
            }
        }
    }

    private void handleJoinRoomResponse(JoinRoomResponse response) {
        switch (response.getResult()) {
            case SUCCESS -> {
                SwingUtilities.invokeLater(() -> {
                    String userName = view.getUserNameTextField().getText();
                    // show waiting window
                    WaitingWindow waitingWindow = new WaitingWindow();
                    WaitingViewController waitingViewController = new WaitingViewController(receiver, waitingWindow, userName, roomID);
                    waitingWindow.show();
                });
            }
            case ERROR -> {
                SwingUtilities.invokeLater(() -> view.showErrorMessageBox(response.getMessage(), "加入房间失败"));
            }
        }
    }
}
