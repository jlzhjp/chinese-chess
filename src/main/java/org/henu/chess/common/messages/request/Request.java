package org.henu.chess.common.messages.request;

import org.henu.chess.common.messages.Message;

public class Request extends Message {
    private String roomID;
    private String userName;

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomID(String roomID) {
        return roomID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName(String userName) {
        return userName;
    }
}
