package io.github.jlzhjp.chess.common.socketManager;

import io.github.jlzhjp.chess.common.socketManager.sinks.PrintWriterSink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class SingleSocketManagerBIO extends SocketManager {
    private final ExecutorService executorService;
    private final InetAddress ipAddress;
    private final int port;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriterSink sink;

    public SingleSocketManagerBIO(InetAddress ipAddress, int port, ExecutorService executorService) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.executorService = executorService;
    }

    @Override
    public InetAddress getIPAddress() {
        return ipAddress;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public void close() throws Exception {
        if (Objects.nonNull(reader)) {
            reader.close();
        }
        if (Objects.nonNull(socket)) {
            socket.close();
        }
        if (Objects.nonNull(sink)) {
            sink.getPrintWriter().close();
        }
    }

    @Override
    public void start() throws Exception {
        socket = new Socket(ipAddress, port);
        sink = new PrintWriterSink(new PrintWriter(socket.getOutputStream()));
        executeOnConnected(sink);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        executorService.execute(() -> {
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    executeOnMessage(line, sink);
                }
            } catch (IOException ex) {
                catchError(ex);
            }
        });
    }

    public Sink getSink() {
        return sink;
    }
}
