package org.henu.chess.client.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.henu.chess.common.model.ChessLogicRedImpl;
import org.henu.chess.common.model.ChessPanelModel;
import org.henu.chess.common.view.ChessPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ChessWindow {
    private JFrame frame;
    private ChessPanel chessPanel;
    private JScrollPane scrollPane;
    private JTextArea txtLog;
    private JPanel logWrapper;
    private JPanel userInfoWrapper;
    private JLabel lblRed;
    private JLabel lblBlack;

    /**
     * Create the application.
     */
    public ChessWindow() {
        initialize();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                ChessWindow window = new ChessWindow();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("中国象棋");
        frame.setResizable(false);
        frame.setBounds(100, 100, 923, 651);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        chessPanel = new ChessPanel();
        chessPanel.setBounds(10, 11, 600, 600);
        chessPanel.setModel(new ChessPanelModel(new ChessLogicRedImpl()));
        frame.getContentPane().add(chessPanel);

        logWrapper = new JPanel();
        logWrapper.setBorder(new TitledBorder(null, "对局记录", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        logWrapper.setBounds(620, 113, 281, 492);
        frame.getContentPane().add(logWrapper);
        logWrapper.setLayout(new BorderLayout(0, 0));

        scrollPane = new JScrollPane();
        logWrapper.add(scrollPane);

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        scrollPane.setViewportView(txtLog);

        userInfoWrapper = new JPanel();
        userInfoWrapper.setBorder(new TitledBorder(null, "棋手信息", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        userInfoWrapper.setBounds(620, 11, 281, 91);
        frame.getContentPane().add(userInfoWrapper);
        userInfoWrapper.setLayout(null);

        lblRed = new JLabel("红方：");
        lblRed.setBounds(10, 31, 261, 14);
        userInfoWrapper.add(lblRed);

        lblBlack = new JLabel("黑方：");
        lblBlack.setBounds(10, 56, 261, 14);
        userInfoWrapper.add(lblBlack);
    }

    public JLabel getRedPlayerLabel() {
        return lblRed;
    }

    public JLabel getBlackPlayerLabel() {
        return lblBlack;
    }

    public ChessPanel getChessPanel() {
        return chessPanel;
    }

    public void showInfoMessageBox(String content, String title) {
        JOptionPane.showMessageDialog(frame, content, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessageBox(String content, String title) {
        JOptionPane.showMessageDialog(frame, content, title, JOptionPane.ERROR_MESSAGE);
    }

    public void appendToLog(String line) {
        txtLog.append(line + "\n");
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
}
