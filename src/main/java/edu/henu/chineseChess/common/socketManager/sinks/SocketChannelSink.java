package edu.henu.chineseChess.common.socketManager.sinks;

import edu.henu.chineseChess.common.socketManager.Sink;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SocketChannelSink implements Sink {
    SocketChannel socketChannel;

    public SocketChannelSink(SocketChannel channel) {
        this.socketChannel = channel;
    }

    @Override
    public boolean isClosed() {
        return !socketChannel.isOpen();
    }

    @Override
    public void println(String message) {
        ByteBuffer buffer = ByteBuffer.wrap((message + "\n").getBytes());
        try {
            socketChannel.write(buffer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
