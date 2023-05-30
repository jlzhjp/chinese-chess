package org.henu.chess.common.messages.request;

import org.henu.chess.common.messages.Message;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Request{" +
                "roomID='" + roomID + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(roomID, request.roomID) && Objects.equals(userName, request.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomID, userName);
    }
}
