package org.henu.chess.common;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;


public class SocketReceiver implements AutoCloseable {
    public interface SocketMessageHandler {
        void handle(String message);
    }

    private final Socket socket;
    private final PrintWriter writer;

    public SocketReceiver(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)));
    }

    /**
     * 监听 (非阻塞)
     *
     * @param handler Socket 消息处理器
     */
    public void listen(SocketMessageHandler handler) {
        var that = this;
        new Thread(() -> {
            try (var reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    synchronized (that) {
                        handler.handle(line);
                    }
                }
            } catch (SocketException ex) {
                // 当 Socket 被关闭时会抛出 SocketException
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 发送请求
     *
     * @param message 请求消息
     */
    public void send(String message) {
        writer.println(message);
        writer.flush();
    }

    @Override
    public void close() {
        writer.close();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
