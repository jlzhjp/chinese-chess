package edu.henu.chineseChess.client.controller;

import edu.henu.chineseChess.client.view.WaitingWindow;
import edu.henu.chineseChess.common.MessageSocketManager;

public class WaitingViewControllerBuilder {
    private String userName;
    private String roomID;
    private WaitingWindow view;
    private MessageSocketManager socketManager;
    private Runnable onBack;

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

    public WaitingViewControllerBuilder setOnBack(Runnable onBack) {
        this.onBack = onBack;
        return this;
    }

    public WaitingViewController build() {
        return new WaitingViewController(socketManager, view, userName, roomID, onBack);
    }
}
