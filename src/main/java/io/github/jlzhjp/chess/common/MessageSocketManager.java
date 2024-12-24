package io.github.jlzhjp.chess.common;

import io.github.jlzhjp.chess.common.messages.Message;
import io.github.jlzhjp.chess.common.socketManager.Sink;
import io.github.jlzhjp.chess.common.socketManager.SocketManager;

import java.net.InetAddress;
import java.util.Objects;

public class MessageSocketManager {
    private final SocketManager socketManager;
    private MessageListener messageListener;

    public MessageSocketManager(SocketManager socketManager) {
        this.socketManager = socketManager;
        this.socketManager.setMessageListener((line, sink) -> {
            if (messageListener != null) {
                Message message = Message.parse(line);
                messageListener.onMessage(message, new MessageSink(sink));
            }
        });
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public void setErrorHandler(SocketManager.ErrorHandler errorHandler) {
        socketManager.setErrorHandler(errorHandler);
    }

    public SocketManager getSocketManager() {
        return socketManager;
    }

    public MessageSink getSinkFromConnected() {
        Sink sink = SocketManager.getSinkFromConnected(socketManager);
        if (Objects.nonNull(sink)) {
            return new MessageSink(sink);
        }
        return null;
    }

    public InetAddress getIPAddress() {
        return socketManager.getIPAddress();
    }

    public int getPort() {
        return socketManager.getPort();
    }

    public void start() throws Exception {
        socketManager.start();
    }

    public void close() throws Exception {
        socketManager.close();
    }
}
