package org.henu.chess.common.messages.response;

public class CreateRoomResponse extends Response {
    String roomID;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }
}
