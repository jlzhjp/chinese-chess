package io.github.jlzhjp.chess.common;

import io.github.jlzhjp.chess.common.socketManager.SocketManager;
import io.github.jlzhjp.chess.common.view.AppWindow;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class DefaultErrorHandler implements SocketManager.ErrorHandler {
    private final AppWindow view;
    private final PrintWriter pw;
    private final StringWriter sw = new StringWriter();

    public DefaultErrorHandler(AppWindow view) {
        this.view = view;
        pw = new PrintWriter(sw);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace(pw);
        pw.println("========================================");
        pw.println(ex.getMessage());

        SwingUtilities.invokeLater(() -> view.showErrorMessageBox(sw.toString(), ex.getClass().getTypeName()));
    }
}
