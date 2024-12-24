package io.github.jlzhjp.chess.client.view;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.jlzhjp.chess.common.model.ChessLogicImpl;
import io.github.jlzhjp.chess.common.model.ChessPanelModel;
import io.github.jlzhjp.chess.common.model.ChessRecord;
import io.github.jlzhjp.chess.common.view.AppWindow;
import io.github.jlzhjp.chess.common.view.ChessPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ChessWindow extends AppWindow {
    private final DefaultListModel<ChessRecord> logListModel = new DefaultListModel<>();
    private ChessPanel chessPanel;
    private JScrollPane logScrollWrapper;
    private JList<ChessRecord> logList;
    private JPanel logWrapper;
    private JPanel userInfoWrapper;
    private JLabel lblRed;
    private JLabel lblBlack;
    private JPanel remainingTimeWrapper;
    private JLabel lblRemainingTime;
    private JPanel operationWrapper;
    private JButton btnRetract;
    private JButton btnAdmitDefeat;

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
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().getContentPane().setLayout(null);

        chessPanel = new ChessPanel();
        chessPanel.setBounds(20, 20, ChessPanel.CHESS_BOARD_WIDTH, ChessPanel.CHESS_BOARD_HEIGHT);
        chessPanel.setModel(new ChessPanelModel(new ChessLogicImpl()));
        getFrame().getContentPane().add(chessPanel);

        logWrapper = new JPanel();
        logWrapper.setBorder(new TitledBorder(null, "对局记录", TitledBorder.LEADING, TitledBorder.TOP, getDefaultFont(), null));
        logWrapper.setBounds(620, 285, 281, 320);
        getFrame().getContentPane().add(logWrapper);
        logWrapper.setLayout(new BorderLayout(0, 0));

        logScrollWrapper = new JScrollPane();
        logWrapper.add(logScrollWrapper);

        logList = new JList<>();
        logList.setFont(getDefaultFont());
        logList.setModel(logListModel);
        logScrollWrapper.setViewportView(logList);

        userInfoWrapper = new JPanel();
        userInfoWrapper.setBorder(new TitledBorder(null, "棋手信息", TitledBorder.LEADING, TitledBorder.TOP, getDefaultFont(), null));
        userInfoWrapper.setBounds(620, 11, 281, 91);
        getFrame().getContentPane().add(userInfoWrapper);
        userInfoWrapper.setLayout(null);

        lblRed = new JLabel("红方：");
        lblRed.setFont(getDefaultFont());
        lblRed.setBounds(10, 31, 261, 14);
        userInfoWrapper.add(lblRed);

        lblBlack = new JLabel("黑方：");
        lblBlack.setFont(getDefaultFont());
        lblBlack.setBounds(10, 56, 261, 14);
        userInfoWrapper.add(lblBlack);

        remainingTimeWrapper = new JPanel();
        remainingTimeWrapper.setBorder(new TitledBorder(null, "剩余时间", TitledBorder.LEADING, TitledBorder.TOP, getDefaultFont(), null));
        remainingTimeWrapper.setBounds(620, 113, 281, 91);
        getFrame().getContentPane().add(remainingTimeWrapper);
        remainingTimeWrapper.setLayout(null);

        lblRemainingTime = new JLabel("--");
        lblRemainingTime.setFont(new Font("Arial", Font.BOLD, 30));
        lblRemainingTime.setBounds(126, 37, 58, 25);
        remainingTimeWrapper.add(lblRemainingTime);

        operationWrapper = new JPanel();
        operationWrapper.setBorder(new TitledBorder(null, "操作", TitledBorder.LEADING, TitledBorder.TOP, getDefaultFont(), null));
        operationWrapper.setBounds(620, 215, 281, 59);
        getFrame().getContentPane().add(operationWrapper);
        operationWrapper.setLayout(null);

        btnRetract = new JButton("悔棋");
        btnRetract.setFont(getDefaultFont());
        btnRetract.setBounds(42, 25, 89, 23);
        operationWrapper.add(btnRetract);

        btnAdmitDefeat = new JButton("认输");
        btnAdmitDefeat.setFont(getDefaultFont());
        btnAdmitDefeat.setBounds(153, 25, 89, 23);
        operationWrapper.add(btnAdmitDefeat);
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

    public JList<ChessRecord> getLogList() {
        return logList;
    }

    public DefaultListModel<ChessRecord> getLogListModel() {
        return logListModel;
    }

    public JLabel getRemainingTimeLabel() {
        return lblRemainingTime;
    }

    public JButton getRetractButton() {
        return btnRetract;
    }

    public JButton getAdmitDefeatButton() {
        return btnAdmitDefeat;
    }

    public JFrame getWindow() {
        return getFrame();
    }
}
