package io.github.jlzhjp.chess.common.socketManager;

public interface Sink {
    boolean isClosed();

    void println(String message);
}
