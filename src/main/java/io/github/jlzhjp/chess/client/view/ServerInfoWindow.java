package io.github.jlzhjp.chess.client.view;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.jlzhjp.chess.common.view.AppWindow;

import javax.swing.*;
import java.awt.*;

public class ServerInfoWindow extends AppWindow {
    private JLabel lblServerIPAddress;
    private JTextField txtServerIPAddress;
    private JLabel lblPort;
    private JTextField txtPort;
    private JButton btnConnect;

    /**
     * Create the application.
     */
    public ServerInfoWindow() {
        initialize();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                ServerInfoWindow window = new ServerInfoWindow();
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
        JFrame frame = getFrame();
        frame.setTitle("连接服务器");
        frame.setBounds(100, 100, 450, 163);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setResizable(false);

        lblServerIPAddress = new JLabel("服务器地址：");
        lblServerIPAddress.setFont(getDefaultFont());
        lblServerIPAddress.setBounds(10, 32, 75, 14);
        frame.getContentPane().add(lblServerIPAddress);

        txtServerIPAddress = new JTextField();
        lblServerIPAddress.setFont(getDefaultFont());
        txtServerIPAddress.setText("127.0.0.1");
        txtServerIPAddress.setBounds(95, 29, 333, 20);
        frame.getContentPane().add(txtServerIPAddress);
        txtServerIPAddress.setColumns(10);

        lblPort = new JLabel("端口号：");
        lblPort.setFont(getDefaultFont());
        lblPort.setBounds(10, 75, 48, 14);
        frame.getContentPane().add(lblPort);

        txtPort = new JTextField();
        txtPort.setFont(getDefaultFont());
        txtPort.setText("8888");
        txtPort.setBounds(95, 72, 96, 20);
        frame.getContentPane().add(txtPort);
        txtPort.setColumns(10);

        btnConnect = new JButton("连接");
        btnConnect.setFont(getDefaultFont());
        btnConnect.setBounds(339, 94, 89, 23);
        frame.getContentPane().add(btnConnect);
    }

    public JTextField getServerIPAddressTextField() {
        return txtServerIPAddress;
    }

    public JTextField getPortTextField() {
        return txtPort;
    }

    public JButton getConnectButton() {
        return btnConnect;
    }
}
