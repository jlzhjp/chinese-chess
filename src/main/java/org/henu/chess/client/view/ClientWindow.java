package org.henu.chess.client.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.henu.chess.common.model.ChessBoardPoint;
import org.henu.chess.common.model.Piece;
import org.henu.chess.common.view.ChessPanel;

import java.awt.*;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;

public class ClientWindow {
    private boolean isRed = true;

    private JFrame frame;
    private ChessPanel chessPanel;
    private JScrollPane scrollPane;
    private JTextArea txtLog;
    private JPanel logWrapper;
    private JPanel userInfoWrapper;
    private JLabel lblRed;
    private JLabel lblBlack;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                ClientWindow window = new ClientWindow();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public ClientWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setBounds(100, 100, 923, 651);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        chessPanel = new ChessPanel();
        chessPanel.setBounds(10, 11, 600, 600);
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

        initializeChessPanel();
    }

    public void initializeChessPanel() {
        var model = chessPanel.getModel();
        if (isRed) {
            model.put(new ChessBoardPoint(0, 9), Piece.RED_CHARIOT);
            model.put(new ChessBoardPoint(1, 9), Piece.RED_HORSE);
            model.put(new ChessBoardPoint(2, 9), Piece.RED_ELEPHANT);
            model.put(new ChessBoardPoint(3, 9), Piece.RED_GUARD);
            model.put(new ChessBoardPoint(4, 9), Piece.RED_GENERAL);
            model.put(new ChessBoardPoint(5, 9), Piece.RED_GUARD);
            model.put(new ChessBoardPoint(6, 9), Piece.RED_ELEPHANT);
            model.put(new ChessBoardPoint(7, 9), Piece.RED_HORSE);
            model.put(new ChessBoardPoint(8, 9), Piece.RED_CHARIOT);
            model.put(new ChessBoardPoint(1, 7), Piece.RED_CANNON);
            model.put(new ChessBoardPoint(7, 7), Piece.RED_CANNON);
            model.put(new ChessBoardPoint(0, 6), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(2, 6), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(4, 6), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(6, 6), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(8, 6), Piece.RED_SOLDIER);

            model.put(new ChessBoardPoint(0, 0), Piece.BLACK_CHARIOT);
            model.put(new ChessBoardPoint(1, 0), Piece.BLACK_HORSE);
            model.put(new ChessBoardPoint(2, 0), Piece.BLACK_ELEPHANT);
            model.put(new ChessBoardPoint(3, 0), Piece.BLACK_GUARD);
            model.put(new ChessBoardPoint(4, 0), Piece.BLACK_GENERAL);
            model.put(new ChessBoardPoint(5, 0), Piece.BLACK_GUARD);
            model.put(new ChessBoardPoint(6, 0), Piece.BLACK_ELEPHANT);
            model.put(new ChessBoardPoint(7, 0), Piece.BLACK_HORSE);
            model.put(new ChessBoardPoint(8, 0), Piece.BLACK_CHARIOT);
            model.put(new ChessBoardPoint(1, 2), Piece.BLACK_CANNON);
            model.put(new ChessBoardPoint(7, 2), Piece.BLACK_CANNON);
            model.put(new ChessBoardPoint(0, 3), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(2, 3), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(4, 3), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(6, 3), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(8, 3), Piece.BLACK_SOLDIER);
        } else {
            model.put(new ChessBoardPoint(0, 0), Piece.RED_CHARIOT);
            model.put(new ChessBoardPoint(1, 0), Piece.RED_HORSE);
            model.put(new ChessBoardPoint(2, 0), Piece.RED_ELEPHANT);
            model.put(new ChessBoardPoint(3, 0), Piece.RED_GUARD);
            model.put(new ChessBoardPoint(4, 0), Piece.RED_GENERAL);
            model.put(new ChessBoardPoint(5, 0), Piece.RED_GUARD);
            model.put(new ChessBoardPoint(6, 0), Piece.RED_ELEPHANT);
            model.put(new ChessBoardPoint(7, 0), Piece.RED_HORSE);
            model.put(new ChessBoardPoint(8, 0), Piece.RED_CHARIOT);
            model.put(new ChessBoardPoint(1, 2), Piece.RED_CANNON);
            model.put(new ChessBoardPoint(7, 2), Piece.RED_CANNON);
            model.put(new ChessBoardPoint(0, 3), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(2, 3), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(4, 3), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(6, 3), Piece.RED_SOLDIER);
            model.put(new ChessBoardPoint(8, 3), Piece.RED_SOLDIER);

            model.put(new ChessBoardPoint(0, 9), Piece.BLACK_CHARIOT);
            model.put(new ChessBoardPoint(1, 9), Piece.BLACK_HORSE);
            model.put(new ChessBoardPoint(2, 9), Piece.BLACK_ELEPHANT);
            model.put(new ChessBoardPoint(3, 9), Piece.BLACK_GUARD);
            model.put(new ChessBoardPoint(4, 9), Piece.BLACK_GENERAL);
            model.put(new ChessBoardPoint(5, 9), Piece.BLACK_GUARD);
            model.put(new ChessBoardPoint(6, 9), Piece.BLACK_ELEPHANT);
            model.put(new ChessBoardPoint(7, 9), Piece.BLACK_HORSE);
            model.put(new ChessBoardPoint(8, 9), Piece.BLACK_CHARIOT);
            model.put(new ChessBoardPoint(1, 7), Piece.BLACK_CANNON);
            model.put(new ChessBoardPoint(7, 7), Piece.BLACK_CANNON);
            model.put(new ChessBoardPoint(0, 6), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(2, 6), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(4, 6), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(6, 6), Piece.BLACK_SOLDIER);
            model.put(new ChessBoardPoint(8, 6), Piece.BLACK_SOLDIER);
        }

        chessPanel.setListener(point ->{
            Piece selectedPiece = model.pieceAt(point);
            if (Objects.nonNull(selectedPiece) && selectedPiece.isRed() == isRed) {
                model.select(point);
            }
        });
    }

}
