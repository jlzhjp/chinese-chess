package io.github.jlzhjp.chess.common.messages.response;

import java.util.Objects;

public class CreateRoomResponse extends Response {
    String roomID;

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    @Override
    public String toString() {
        return "CreateRoomResponse{" +
                "roomID='" + roomID + '\'' +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateRoomResponse that = (CreateRoomResponse) o;
        return Objects.equals(roomID, that.roomID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomID);
    }
}
