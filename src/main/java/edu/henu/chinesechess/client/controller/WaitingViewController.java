package edu.henu.chinesechess.client.controller;

import edu.henu.chinesechess.client.model.GameInfo;
import edu.henu.chinesechess.client.view.ChessWindow;
import edu.henu.chinesechess.client.view.WaitingWindow;
import edu.henu.chinesechess.common.DefaultErrorHandler;
import edu.henu.chinesechess.common.MessageListener;
import edu.henu.chinesechess.common.MessageSink;
import edu.henu.chinesechess.common.MessageSocketManager;
import edu.henu.chinesechess.common.messages.response.StartGameResponse;

import javax.swing.*;

public class WaitingViewController {
    private final WaitingWindow view;
    private final String selfUserName;
    private final String roomID;
    private final MessageSocketManager socketManager;
    private final Runnable onBack;

    public WaitingViewController(MessageSocketManager socketManager, WaitingWindow view, String userName, String roomID, Runnable onBack) {
        this.view = view;
        this.selfUserName = userName;
        this.roomID = roomID;
        this.socketManager = socketManager;
        this.onBack = onBack;

        SwingUtilities.invokeLater(() -> {
            view.getUserNameValueLabel().setText(userName);
            view.getRoomIDTextField().setText(roomID);
            view.show();
        });

        socketManager.setMessageListener(new MessageListener().on(StartGameResponse.class, this::handleStartGameResponse));
        socketManager.setErrorHandler(new DefaultErrorHandler(view));
    }

    private void handleStartGameResponse(StartGameResponse response, MessageSink sink) {
        SwingUtilities.invokeLater(() -> {
            view.close();
            view.dispose();

            ChessWindow chessWindow = new ChessWindow();
            GameInfo info = new GameInfo();

            chessWindow.show();
            info.setRoomID(roomID);
            info.setUserName(selfUserName);
            info.setBlackPlayer(response.getBlackPlayerName());
            info.setRedPlayer(response.getRedPlayerName());

            ChessViewController controller = new ChessViewController(chessWindow, info, socketManager, onBack);
        });
    }
}
