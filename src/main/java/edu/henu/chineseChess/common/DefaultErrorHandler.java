package edu.henu.chineseChess.common;

import edu.henu.chineseChess.common.socketManager.SocketManager;
import edu.henu.chineseChess.common.view.AppWindow;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class DefaultErrorHandler implements SocketManager.ErrorHandler {
    private final AppWindow view;
    private final PrintWriter pw;

    public DefaultErrorHandler(AppWindow view) {
       this.view = view;
        StringWriter writer = new StringWriter();
        pw = new PrintWriter(writer);
    }
    @Override
    public void onError(Exception ex) {
        ex.printStackTrace(pw);
        pw.println();
        pw.println(ex.getMessage());
        SwingUtilities.invokeLater(() -> view.showErrorMessageBox(pw.toString(), ex.getClass().getTypeName()));
    }
}
