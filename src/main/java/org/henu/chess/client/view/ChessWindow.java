package org.henu.chess.client.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.henu.chess.common.model.ChessLogicImpl;
import org.henu.chess.common.model.ChessPanelModel;
import org.henu.chess.common.view.AppWindow;
import org.henu.chess.common.view.ChessPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ChessWindow extends AppWindow {
    private ChessPanel chessPanel;
    private JScrollPane logScrollWrapper;
    private JTextArea txtLog;
    private JPanel logWrapper;
    private JPanel userInfoWrapper;
    private JLabel lblRed;
    private JLabel lblBlack;
    private JPanel remainingTimeWrapper;
    private JLabel lblRemainingTime;

    /**
     * Create the application.
     */
    public ChessWindow() {
        super();
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
                window.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        getFrame().setTitle("中国象棋");
        getFrame().setResizable(false);
        getFrame().setBounds(100, 100, 923, 651);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().getContentPane().setLayout(null);

        chessPanel = new ChessPanel();
        chessPanel.setBounds(10, 11, 600, 600);
        chessPanel.setModel(new ChessPanelModel(new ChessLogicImpl()));
        getFrame().getContentPane().add(chessPanel);

        logWrapper = new JPanel();
        logWrapper.setBorder(new TitledBorder(null, "对局记录", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        logWrapper.setBounds(620, 215, 281, 390);
        getFrame().getContentPane().add(logWrapper);
        logWrapper.setLayout(new BorderLayout(0, 0));

        logScrollWrapper = new JScrollPane();
        logWrapper.add(logScrollWrapper);

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        logScrollWrapper.setViewportView(txtLog);

        userInfoWrapper = new JPanel();
        userInfoWrapper.setBorder(new TitledBorder(null, "棋手信息", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        userInfoWrapper.setBounds(620, 11, 281, 91);
        getFrame().getContentPane().add(userInfoWrapper);
        userInfoWrapper.setLayout(null);

        lblRed = new JLabel("红方：");
        lblRed.setBounds(10, 31, 261, 14);
        userInfoWrapper.add(lblRed);

        lblBlack = new JLabel("黑方：");
        lblBlack.setBounds(10, 56, 261, 14);
        userInfoWrapper.add(lblBlack);

        remainingTimeWrapper = new JPanel();
        remainingTimeWrapper.setBorder(new TitledBorder(null, "剩余时间", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        remainingTimeWrapper.setBounds(620, 113, 281, 91);
        getFrame().getContentPane().add(remainingTimeWrapper);
        remainingTimeWrapper.setLayout(null);

        lblRemainingTime = new JLabel("--");
        lblRemainingTime.setBounds(126, 37, 58, 25);
        lblRemainingTime.setFont(new Font("Tahoma", Font.BOLD, 32));
        remainingTimeWrapper.add(lblRemainingTime);
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

    public void appendToLog(String line) {
        txtLog.append(line + "\n");
    }

    public JLabel getRemainingTimeLabel() {
        return lblRemainingTime;
    }
}
