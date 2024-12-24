package io.github.jlzhjp.chess.common.view;

import javax.swing.*;
import java.awt.*;

public class AppWindow {
    private static final Font defaultFont = new Font("Microsoft YaHei UI", Font.PLAIN, 12);
    private static final Font defaultBoldFont = new Font("Microsoft YaHei UI", Font.BOLD, 12);
    private final JFrame frame;

    public AppWindow() {
        frame = new JFrame();
        frame.setFont(defaultFont);
    }

    public Font getDefaultFont() {
        return defaultFont;
    }

    public Font getDefaultBoldFont() {
        return defaultBoldFont;
    }

    public void makeDefaultFont(JComponent component) {
        component.setFont(defaultFont);
    }

    public void showInfoMessageBox(String content, String title) {
        JOptionPane.showMessageDialog(frame, content, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessageBox(String content, String title) {
        JOptionPane.showMessageDialog(frame, content, title, JOptionPane.ERROR_MESSAGE);
    }

    public int showConfirmDialog(String content, String title) {
        return JOptionPane.showConfirmDialog(frame, content, title, JOptionPane.YES_NO_OPTION);
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

    public void setTitle(String title) {
        frame.setTitle(title);
    }
}