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
    private JTextField txtRoomID;
    private JLabel lblRoomNumber;
    private JButton btnJoinRoom;
    private JLabel lblUserName;
    private JTextField txtUserName;

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
        lblIPAddress.setBounds(10, 24, 40, 14);
        panelServerInfo.add(lblIPAddress);

        txtIPAddress = new JTextField();
        txtIPAddress.setEditable(false);
        txtIPAddress.setText("127.0.0.1");
        txtIPAddress.setBounds(58, 21, 112, 20);
        panelServerInfo.add(txtIPAddress);
        txtIPAddress.setColumns(10);

        lblPort = new JLabel("端口号");
        lblPort.setBounds(189, 24, 39, 14);
        panelServerInfo.add(lblPort);

        txtPort = new JTextField();
        txtPort.setEditable(false);
        txtPort.setBounds(229, 21, 83, 20);
        panelServerInfo.add(txtPort);
        txtPort.setColumns(10);

        lblUserName = new JLabel("用户名");
        lblUserName.setBounds(334, 24, 48, 14);
        panelServerInfo.add(lblUserName);

        txtUserName = new JTextField();
        txtUserName.setBounds(370, 21, 83, 20);
        panelServerInfo.add(txtUserName);
        txtUserName.setColumns(10);

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

        txtRoomID = new JTextField();
        txtRoomID.setBounds(102, 41, 96, 20);
        panelJoinRoom.add(txtRoomID);
        txtRoomID.setColumns(10);

        lblRoomNumber = new JLabel("房间号");
        lblRoomNumber.setBounds(52, 44, 48, 14);
        panelJoinRoom.add(lblRoomNumber);

        btnJoinRoom = new JButton("加入房间");
        btnJoinRoom.setBounds(75, 82, 89, 23);
        panelJoinRoom.add(btnJoinRoom);
    }

    public JTextField getIPAddressTextField() {
        return txtIPAddress;
    }

    public JTextField getPortTextField() {
        return txtPort;
    }

    public JTextField getRoomIDTextField() {
       return txtRoomID;
    }

    public JTextField getUserNameTextField() {
        return txtUserName;
    }

    public JButton getCreateRoomButton() {
        return btnCreateRoom;
    }

    public JButton getJoinRoomButton() {
        return btnJoinRoom;
    }

    public void showErrorMessageBox(String content, String title) {
        JOptionPane.showMessageDialog(frame, content, title, JOptionPane.ERROR_MESSAGE);
    }

    public void showInfoMessageBox(String content, String title) {
        JOptionPane.showMessageDialog(frame, content, title, JOptionPane.INFORMATION_MESSAGE);
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
