package io.github.jlzhjp.chess.server.view;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.jlzhjp.chess.common.view.AppWindow;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServerWindow extends AppWindow {
    private final DefaultTableModel tableModel = new DefaultTableModel();
    private JPanel configWrapper;
    private JScrollPane scrollPane;
    private JTable tblGameTables;
    private JLabel lblPort;
    private JTextField txtPort;
    private JButton btnStart;
    private JButton btnStop;
    private JCheckBox chkUseNIO;

    /**
     * Create the application.
     */
    public ServerWindow() {
        super();
        initialize();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        FlatLightLaf.setup();
        EventQueue.invokeLater(() -> {
            try {
                ServerWindow window = new ServerWindow();
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
        getFrame().setTitle("中国象棋服务器");
        getFrame().setBounds(100, 100, 442, 450);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().getContentPane().setLayout(null);
        getFrame().setResizable(false);

        configWrapper = new JPanel();
        configWrapper.setBorder(new TitledBorder(null, "配置", TitledBorder.LEADING, TitledBorder.TOP, getDefaultFont(), null));
        configWrapper.setBounds(10, 11, 410, 57);
        getFrame().getContentPane().add(configWrapper);
        configWrapper.setLayout(null);

        lblPort = new JLabel("端口号");
        makeDefaultFont(lblPort);
        lblPort.setBounds(9, 25, 48, 14);
        configWrapper.add(lblPort);

        txtPort = new JTextField();
        makeDefaultFont(txtPort);
        txtPort.setText("8888");
        txtPort.setBounds(67, 22, 96, 20);
        configWrapper.add(txtPort);
        txtPort.setColumns(10);

        chkUseNIO = new JCheckBox("NIO");
        chkUseNIO.setBounds(169, 21, 67, 23);
        makeDefaultFont(chkUseNIO);
        configWrapper.add(chkUseNIO);

        btnStart = new JButton("启动");
        makeDefaultFont(btnStart);
        btnStart.setBounds(242, 21, 74, 23);
        configWrapper.add(btnStart);

        btnStop = new JButton("关闭");
        makeDefaultFont(btnStop);
        btnStop.setBounds(326, 21, 74, 23);
        configWrapper.add(btnStop);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 79, 410, 325);
        getFrame().getContentPane().add(scrollPane);

        tblGameTables = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        makeDefaultFont(tblGameTables);
        tableModel.addColumn("房间号");
        tableModel.addColumn("红方");
        tableModel.addColumn("黑方");
        tableModel.addColumn("状态");
        tblGameTables.setModel(tableModel);
        scrollPane.setViewportView(tblGameTables);
    }

    public JButton getStartButton() {
        return btnStart;
    }

    public JButton getStopButton() {
        return btnStop;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JTextField getPortTextField() {
        return txtPort;
    }

    public JCheckBox getUseNIOCheckBox() {
        return chkUseNIO;
    }

    public JTable getTable() {
        return tblGameTables;
    }
}
