package io.github.jlzhjp.chess.common.socketManager;

import io.github.jlzhjp.chess.common.Singletons;

import java.net.InetAddress;

public abstract class SocketManager implements AutoCloseable {
    private SocketConnectedListener socketConnectedListener;
    private SocketMessageListener socketMessageListener;
    private ErrorHandler errorHandler;

    public static SocketManager connect(InetAddress address, int port) {
        return new SingleSocketManagerBIO(address, port, Singletons.executorService);
    }

    public static Sink getSinkFromConnected(SocketManager manager) {
        if (manager instanceof SingleSocketManagerBIO) {
            return ((SingleSocketManagerBIO) manager).getSink();
        }
        return null;
    }

    public static SocketManager listen(int port, boolean useNIO) {
        if (useNIO) {
            return new MultiSocketManagerNIO(port, Singletons.executorService);
        } else {
            return new MultiSocketManagerBIO(port, Singletons.executorService);
        }
    }

    public abstract InetAddress getIPAddress();

    public abstract int getPort();

    public void setConnectedListener(SocketConnectedListener listener) {
        this.socketConnectedListener = listener;
    }

    public void setMessageListener(SocketMessageListener listener) {
        this.socketMessageListener = listener;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    protected void executeOnConnected(Sink sink) {
        if (socketConnectedListener != null) {
            socketConnectedListener.onConnected(sink);
        }
    }

    protected void executeOnMessage(String message, Sink sink) {
        if (socketMessageListener != null) {
            socketMessageListener.onMessage(message, sink);
        }
    }

    public void catchError(Exception ex) {
        if (errorHandler != null) {
            errorHandler.onError(ex);
        }
    }

    public abstract void start() throws Exception;

    public interface SocketConnectedListener {
        void onConnected(Sink sink);
    }

    public interface SocketMessageListener {
        void onMessage(String message, Sink sink);
    }

    public interface ErrorHandler {
        void onError(Exception ex);
    }
}
