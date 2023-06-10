package edu.henu.chinesechess.server.model;

import edu.henu.chinesechess.common.GameStatus;
import edu.henu.chinesechess.common.MessageSink;
import edu.henu.chinesechess.common.model.ChessPanelModel;

public class GameTable {
    private int row;
    private String redPlayer;

    private String blackPlayer;

    private MessageSink redPlayerMessageSink;

    private MessageSink blackPlayerMessageSink;

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

    public MessageSink getRedPlayerMessageSink() {
        return redPlayerMessageSink;
    }

    public void setRedPlayerMessageSink(MessageSink redPlayerMessageSink) {
        this.redPlayerMessageSink = redPlayerMessageSink;
    }

    public MessageSink getBlackPlayerMessageSink() {
        return blackPlayerMessageSink;
    }

    public void setBlackPlayerMessageSink(MessageSink blackPlayerMessageSink) {
        this.blackPlayerMessageSink = blackPlayerMessageSink;
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
