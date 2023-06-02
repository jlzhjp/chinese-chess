package org.henu.chess.client.controller;

import org.henu.chess.client.model.GameInfo;
import org.henu.chess.client.view.ChessWindow;
import org.henu.chess.client.view.WaitingWindow;
import org.henu.chess.common.MessageListener;
import org.henu.chess.common.SocketMessageReceiver;
import org.henu.chess.common.messages.response.StartGameResponse;

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
