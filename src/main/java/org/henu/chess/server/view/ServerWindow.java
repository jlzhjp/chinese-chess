package org.henu.chess.server.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.henu.chess.common.view.AppWindow;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ServerWindow extends AppWindow {
    private JPanel configWrapper;
    private JScrollPane scrollPane;
    private JTable tblGameTables;
    private JLabel lblPort;
    private JTextField txtPort;
    private JButton btnStart;
    private JButton btnStop;

    private final DefaultTableModel tableModel = new DefaultTableModel();

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
     * Create the application.
     */
    public ServerWindow() {
        super();
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        getFrame().setTitle("中国象棋服务器");
        getFrame().setBounds(100, 100, 392, 450);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().getContentPane().setLayout(null);
        getFrame().setResizable(false);

        configWrapper = new JPanel();
        configWrapper.setBorder(new TitledBorder(null, "配置", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        configWrapper.setBounds(10, 11, 360, 57);
        getFrame().getContentPane().add(configWrapper);
        configWrapper.setLayout(null);

        lblPort = new JLabel("端口号");
        lblPort.setBounds(9, 25, 48, 14);
        configWrapper.add(lblPort);

        txtPort = new JTextField();
        txtPort.setText("8888");
        txtPort.setBounds(67, 22, 96, 20);
        configWrapper.add(txtPort);
        txtPort.setColumns(10);

        btnStart = new JButton("启动");
        btnStart.setBounds(173, 21, 74, 23);
        configWrapper.add(btnStart);

        btnStop = new JButton("关闭");
        btnStop.setBounds(261, 21, 74, 23);
        configWrapper.add(btnStop);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 79, 360, 325);
        getFrame().getContentPane().add(scrollPane);

        tblGameTables = new JTable();
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
}
