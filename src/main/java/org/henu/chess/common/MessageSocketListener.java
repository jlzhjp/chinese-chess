package org.henu.chess.common;

import org.henu.chess.common.messages.Message;

import java.io.IOException;

public class MessageSocketListener implements AutoCloseable {
    private final MessageListener listener;
    SocketListener socketListener;

    MessageSocketListener(String ip, int port, MessageListener listener) throws IOException {
        this.listener = listener;
        socketListener = new SocketListener(ip, port);
    }

    public void listen() {
        socketListener.listen(message -> listener.onMessage(Message.parse(message)));
    }

    public void send(Message message) {
        socketListener.send(Message.toJsonString(message));
    }

    @Override
    public void close() {
        socketListener.close();
    }
}
