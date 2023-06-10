package edu.henu.chinesechess.common.socketManager;

import edu.henu.chinesechess.common.socketManager.sinks.SocketChannelSink;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class MultiSocketManagerNIO extends SocketManager {
    private final int port;
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(8 * 1024);
    private final ExecutorService executorService;
    ServerSocketChannel serverSocketChannel;
    Selector selector;

    public MultiSocketManagerNIO(int port, ExecutorService executorService) {
        this.port = port;
        this.executorService = executorService;
    }

    public void start() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        selector = Selector.open();

        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        executorService.execute(() -> {
            while (selector.isOpen() && serverSocketChannel.isOpen()) {
                try {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            executeOnConnected(new SocketChannelSink(socketChannel));
                        } else if (key.isReadable()) {
                            LineBuffer lineBuffer;
                            if (Objects.isNull(key.attachment())) {
                                lineBuffer = new LineBuffer();
                                key.attach(lineBuffer);
                            } else {
                                lineBuffer = (LineBuffer) key.attachment();
                            }

                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            byteBuffer.clear();
                            int read = socketChannel.read(byteBuffer);

                            if (read > 0) {
                                byteBuffer.flip();
                                lineBuffer.put(byteBuffer, read);
                                while (lineBuffer.hasNext()) {
                                    String message = lineBuffer.next();
                                    executeOnMessage(message, new SocketChannelSink(socketChannel));
                                }
                            }
                        }
                        iterator.remove();
                    }
                } catch (ClosedSelectorException | SocketException ignored) {
                } catch (Exception ex) {
                    catchError(ex);
                }
            }
        });
    }

    @Override
    public InetAddress getIPAddress() {
        return InetAddress.getLoopbackAddress();
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void close() throws Exception {
        if (Objects.nonNull(selector)) {
            selector.close();
        }
        if (Objects.nonNull(serverSocketChannel)) {
            serverSocketChannel.close();
        }
    }
}
