package org.henu.chess.server.model;

import org.henu.chess.common.GameStatus;
import org.henu.chess.common.SocketMessageReceiver;
import org.henu.chess.common.model.ChessPanelModel;

public class GameTable {
    private int row;
    private String redPlayer;

    private String blackPlayer;

    private SocketMessageReceiver redPlayerReceiver;

    private SocketMessageReceiver blackPlayerReceiver;

    private GameStatus status;

    private ChessPanelModel chessPanelModel;

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

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public ChessPanelModel getChessPanelModel() {
        return chessPanelModel;
    }

    public void setChessPanelModel(ChessPanelModel chessPanelModel) {
        this.chessPanelModel = chessPanelModel;
    }
}
