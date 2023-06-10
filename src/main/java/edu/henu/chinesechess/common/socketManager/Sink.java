package edu.henu.chinesechess.common.socketManager;

public interface Sink {
    boolean isClosed();

    void println(String message);
}
