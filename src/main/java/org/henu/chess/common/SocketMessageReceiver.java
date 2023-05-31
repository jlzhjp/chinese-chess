package org.henu.chess.common;

import org.henu.chess.common.messages.Message;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class SocketMessageReceiver implements AutoCloseable {
    private final SocketReceiver socketReceiver;
    private MessageListener listener;

    public SocketMessageReceiver(Socket socket) throws IOException {
        socketReceiver = new SocketReceiver(socket);
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    public int getPort() {
        return socketReceiver.getPort();
    }

    public String getIPAddress() {
        return socketReceiver.getIPAddress();
    }

    public void listen() {
        socketReceiver.listen(message -> {
            if (Objects.nonNull(listener)) {
                listener.onMessage(Message.parse(message));
            }
        });
    }

    public void send(Message message) {
        socketReceiver.send(Message.toJsonString(message));
    }

    @Override
    public void close() {
        socketReceiver.close();
    }
}
