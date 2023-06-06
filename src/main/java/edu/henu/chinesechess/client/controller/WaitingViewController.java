package edu.henu.chinesechess.client.controller;

import edu.henu.chinesechess.client.model.GameInfo;
import edu.henu.chinesechess.common.messages.response.StartGameResponse;
import edu.henu.chinesechess.client.view.ChessWindow;
import edu.henu.chinesechess.client.view.WaitingWindow;
import edu.henu.chinesechess.common.MessageListener;
import edu.henu.chinesechess.common.SocketMessageReceiver;

import javax.swing.*;

public class WaitingViewController {
    private final WaitingWindow view;
    private final String selfUserName;
    private final String roomID;
    private final SocketMessageReceiver receiver;

    public WaitingViewController(SocketMessageReceiver receiver, WaitingWindow view, String userName, String roomID) {
        this.view = view;
        this.selfUserName = userName;
        this.roomID = roomID;
        this.receiver = receiver;

        SwingUtilities.invokeLater(() -> {
            view.getUserNameValueLabel().setText(userName);
            view.getRoomIDTextField().setText(roomID);
            view.show();
        });

        receiver.setListener(new MessageListener().on(StartGameResponse.class, this::handleStartGameResponse));
    }

    private void handleStartGameResponse(StartGameResponse response) {
        SwingUtilities.invokeLater(() -> {
            view.close();
            view.dispose();

            ChessWindow chessWindow = new ChessWindow();

            GameInfo info = new GameInfo();

            info.setRoomID(roomID);
            info.setUserName(selfUserName);
            info.setBlackPlayer(response.getBlackPlayerName());
            info.setRedPlayer(response.getRedPlayerName());

            ChessViewController controller = new ChessViewController(chessWindow, info, receiver);
            chessWindow.show();
        });
    }
}
