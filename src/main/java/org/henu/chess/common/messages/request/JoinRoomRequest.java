package org.henu.chess.common.messages.request;

public class JoinRoomRequest extends Request {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
