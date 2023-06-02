package org.henu.chess.server.model;

import org.henu.chess.common.SocketMessageReceiver;

public class GameTable {
    private int row;
    private String redPlayer;

    private String blackPlayer;

    private SocketMessageReceiver redPlayerReceiver;

    private SocketMessageReceiver blackPlayerReceiver;

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

    public SocketMessageReceiver getRedPlayerReceiver() {
        return redPlayerReceiver;
    }

    public void setRedPlayerReceiver(SocketMessageReceiver redPlayerReceiver) {
        this.redPlayerReceiver = redPlayerReceiver;
    }

    public SocketMessageReceiver getBlackPlayerReceiver() {
        return blackPlayerReceiver;
    }

    public void setBlackPlayerReceiver(SocketMessageReceiver blackPlayerReceiver) {
        this.blackPlayerReceiver = blackPlayerReceiver;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
