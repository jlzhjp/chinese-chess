package io.github.jlzhjp.chess.client.model;

public class GameInfo {
    private String roomID;
    private String userName;
    private String redPlayer;
    private String blackPlayer;

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

    public String getRedPlayer() {
        return redPlayer;
    }

    public void setRedPlayer(String redPlayer) {
        this.redPlayer = redPlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public boolean isRed() {
        return redPlayer.equals(userName);
    }
}
