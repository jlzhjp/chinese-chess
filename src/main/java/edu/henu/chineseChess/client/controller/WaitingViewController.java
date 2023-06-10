package edu.henu.chineseChess.client.controller;

import edu.henu.chineseChess.client.model.GameInfo;
import edu.henu.chineseChess.client.view.ChessWindow;
import edu.henu.chineseChess.client.view.WaitingWindow;
import edu.henu.chineseChess.common.DefaultErrorHandler;
import edu.henu.chineseChess.common.MessageListener;
import edu.henu.chineseChess.common.MessageSink;
import edu.henu.chineseChess.common.MessageSocketManager;
import edu.henu.chineseChess.common.messages.response.StartGameResponse;

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
