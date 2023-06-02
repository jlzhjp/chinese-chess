package org.henu.chess.common.view;

import javax.swing.*;

public class AppWindow {
    private final JFrame frame;

    public AppWindow() {
        frame = new JFrame();
    }

    public void showInfoMessageBox(String content, String title) {
        JOptionPane.showMessageDialog(frame, content, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessageBox(String content, String title) {
        JOptionPane.showMessageDialog(frame, content, title, JOptionPane.ERROR_MESSAGE);
    }

    public void show() {
        frame.setVisible(true);
    }

    public void close() {
        frame.setVisible(false);
    }

    public void dispose() {
        frame.dispose();
    }

    protected JFrame getFrame() {
        return frame;
    }
}