package io.github.jlzhjp.chess.client.controller;

import io.github.jlzhjp.chess.client.view.WaitingWindow;
import io.github.jlzhjp.chess.common.MessageSocketManager;

public class WaitingViewControllerBuilder {
    private String userName;
    private String roomID;
    private WaitingWindow view;
    private MessageSocketManager socketManager;

    public WaitingViewControllerBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public WaitingViewControllerBuilder setRoomID(String roomID) {
        this.roomID = roomID;
        return this;
    }

    public WaitingViewControllerBuilder setView(WaitingWindow view) {
        this.view = view;
        return this;
    }

    public WaitingViewControllerBuilder setSocketManager(MessageSocketManager socketManager) {
        this.socketManager = socketManager;
        return this;
    }

    public WaitingViewController build() {
        return new WaitingViewController(socketManager, view, userName, roomID);
    }
}
