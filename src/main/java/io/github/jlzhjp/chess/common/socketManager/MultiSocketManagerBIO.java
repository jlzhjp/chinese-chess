package io.github.jlzhjp.chess.common.socketManager;

import io.github.jlzhjp.chess.common.socketManager.sinks.PrintWriterSink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

public class MultiSocketManagerBIO extends SocketManager {
    private final int port;
    private final ExecutorService executorService;
    private ServerSocket serverSocket;

    public MultiSocketManagerBIO(int port, ExecutorService executorService) {
        this.port = port;
        this.executorService = executorService;
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
        if (Objects.nonNull(serverSocket)) {
            serverSocket.close();
        }
    }

    @Override
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        MultiSocketManagerBIO that = this;
        executorService.execute(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    Socket socket = serverSocket.accept();
                    PrintWriter writer = new PrintWriter(socket.getOutputStream());
                    executeOnConnected(new PrintWriterSink(writer));
                    executorService.execute(() -> {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                synchronized (that) {
                                    executeOnMessage(line, new PrintWriterSink(writer));
                                }
                            }
                        } catch (IOException ex) {
                            catchError(ex);
                        }
                    });
                }
            } catch (IOException ex) {
                catchError(ex);
            }
        });
    }
}
