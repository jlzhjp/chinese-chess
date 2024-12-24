package io.github.jlzhjp.chess.common.messages.request;

import io.github.jlzhjp.chess.common.messages.Message;

import java.util.Objects;

public class Request extends Message {
    private String roomID;
    private String userName;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
