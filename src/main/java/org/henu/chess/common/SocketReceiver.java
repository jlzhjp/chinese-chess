package org.henu.chess.common;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;


public class SocketReceiver implements AutoCloseable {
    private final Socket socket;
    private final PrintWriter writer;
    private final int port;
    private final String ipAddress;

    public SocketReceiver(Socket socket) throws IOException {
        this.socket = socket;
        this.port = socket.getPort();
        this.ipAddress = socket.getInetAddress().getHostAddress();
        this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8)));
    }

    public int getPort() {
        return port;
    }

    public String getIPAddress() {
        return ipAddress;
    }

    /**
     * 监听 (非阻塞)
     *
     * @param handler Socket 消息处理器
     */
    public void listen(SocketMessageHandler handler) {
        SocketReceiver that = this;
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
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

    public interface SocketMessageHandler {
        void handle(String message);
    }

}
