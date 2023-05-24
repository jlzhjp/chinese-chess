package org.henu.chess.client.view;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class LoginWindow {

    private JFrame frame;
    private JTextField txtIPAddress;
    private JLabel lblIPAddress;
    private JLabel lblPort;
    private JTextField txtPort;
    private JPanel panelServerInfo;
    private JPanel panelCreateRoom;
    private JPanel panelJoinRoom;
    private JButton btnCreateRoom;
    private JTextField textField;
    private JLabel lblRoomNumber;
    private JButton btnJoinRoom;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                LoginWindow window = new LoginWindow();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public LoginWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setTitle("登陆游戏");
        frame.setBounds(100, 100, 508, 259);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        panelServerInfo = new JPanel();
        panelServerInfo.setBorder(new TitledBorder(null, "服务器信息", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelServerInfo.setBounds(10, 11, 476, 59);
        frame.getContentPane().add(panelServerInfo);
        panelServerInfo.setLayout(null);

        lblIPAddress = new JLabel("IP 地址");
        lblIPAddress.setBounds(10, 24, 38, 14);
        panelServerInfo.add(lblIPAddress);

        txtIPAddress = new JTextField();
        txtIPAddress.setBounds(56, 21, 245, 20);
        panelServerInfo.add(txtIPAddress);
        txtIPAddress.setColumns(10);

        lblPort = new JLabel("端口号");
        lblPort.setBounds(311, 24, 39, 14);
        panelServerInfo.add(lblPort);

        txtPort = new JTextField();
        txtPort.setBounds(360, 21, 83, 20);
        panelServerInfo.add(txtPort);
        txtPort.setColumns(10);

        panelCreateRoom = new JPanel();
        panelCreateRoom.setBorder(new TitledBorder(null, "创建房间", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelCreateRoom.setBounds(10, 81, 224, 132);
        frame.getContentPane().add(panelCreateRoom);
        panelCreateRoom.setLayout(null);

        btnCreateRoom = new JButton("创建房间");
        btnCreateRoom.setBounds(68, 60, 89, 23);
        panelCreateRoom.add(btnCreateRoom);

        panelJoinRoom = new JPanel();
        panelJoinRoom.setBorder(new TitledBorder(null, "加入房间", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelJoinRoom.setBounds(244, 81, 242, 132);
        frame.getContentPane().add(panelJoinRoom);
        panelJoinRoom.setLayout(null);

        textField = new JTextField();
        textField.setBounds(102, 41, 96, 20);
        panelJoinRoom.add(textField);
        textField.setColumns(10);

        lblRoomNumber = new JLabel("房间号");
        lblRoomNumber.setBounds(52, 44, 48, 14);
        panelJoinRoom.add(lblRoomNumber);

        btnJoinRoom = new JButton("加入房间");
        btnJoinRoom.setBounds(75, 82, 89, 23);
        panelJoinRoom.add(btnJoinRoom);
    }
}
