package io.github.jlzhjp.chess.client.controller;

import io.github.jlzhjp.chess.client.model.GameInfo;
import io.github.jlzhjp.chess.client.view.ChessWindow;
import io.github.jlzhjp.chess.client.view.WaitingWindow;
import io.github.jlzhjp.chess.common.DefaultErrorHandler;
import io.github.jlzhjp.chess.common.MessageListener;
import io.github.jlzhjp.chess.common.MessageSink;
import io.github.jlzhjp.chess.common.MessageSocketManager;
import io.github.jlzhjp.chess.common.messages.response.StartGameResponse;

import javax.swing.*;

public class WaitingViewController {
    private final WaitingWindow view;
    private final String selfUserName;
    private final String roomID;
    private final MessageSocketManager socketManager;

    public WaitingViewController(MessageSocketManager socketManager, WaitingWindow view, String userName, String roomID) {
        this.view = view;
        this.selfUserName = userName;
        this.roomID = roomID;
        this.socketManager = socketManager;

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

            ChessViewController controller = new ChessViewController(chessWindow, info, socketManager);
        });
    }
}
