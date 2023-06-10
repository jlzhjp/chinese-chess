package edu.henu.chinesechess.common.socketManager;

import edu.henu.chinesechess.common.Singletons;

import java.net.InetAddress;

public abstract class SocketManager implements AutoCloseable {
    private OnSocketConnectedListener onSocketConnectedListener;
    private OnSocketMessageListener onSocketMessageListener;
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

    public void setOnConnectedListener(OnSocketConnectedListener listener) {
        this.onSocketConnectedListener = listener;
    }

    public void setOnMessageListener(OnSocketMessageListener listener) {
        this.onSocketMessageListener = listener;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    protected void executeOnConnected(Sink sink) {
        if (onSocketConnectedListener != null) {
            onSocketConnectedListener.onConnected(sink);
        }
    }

    protected void executeOnMessage(String message, Sink sink) {
        if (onSocketMessageListener != null) {
            onSocketMessageListener.onMessage(message, sink);
        }
    }

    public void catchError(Exception ex) {
        if (errorHandler != null) {
            errorHandler.onError(ex);
        }
    }

    public abstract void start() throws Exception;

    public interface OnSocketConnectedListener {
        void onConnected(Sink sink);
    }

    public interface OnSocketMessageListener {
        void onMessage(String message, Sink sink);
    }

    public interface ErrorHandler {
        void onError(Exception ex);
    }
}
