package edu.henu.chineseChess.common.socketManager;

public interface Sink {
    boolean isClosed();

    void println(String message);
}
