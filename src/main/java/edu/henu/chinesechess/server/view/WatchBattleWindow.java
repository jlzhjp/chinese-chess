package edu.henu.chinesechess.server.view;

import edu.henu.chinesechess.common.view.ChessPanel;
import edu.henu.chinesechess.common.view.AppWindow;

import javax.swing.*;
import java.awt.*;

public class WatchBattleWindow extends AppWindow {
    private ChessPanel chessPanel;

    /**
     * Create the application.
     */
    public WatchBattleWindow() {
        initialize();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                WatchBattleWindow window = new WatchBattleWindow();
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
        getFrame().setBounds(100, 100, ChessPanel.CHESS_BOARD_WIDTH + 35, ChessPanel.CHESS_BOARD_HEIGHT + 50);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setResizable(false);
        getFrame().getContentPane().setLayout(null);

        chessPanel = new ChessPanel();
        chessPanel.setBounds(10, 10, ChessPanel.CHESS_BOARD_WIDTH, ChessPanel.CHESS_BOARD_HEIGHT);
        getFrame().getContentPane().add(chessPanel);
    }

    public ChessPanel getChessPanel() {
        return chessPanel;
    }
}
