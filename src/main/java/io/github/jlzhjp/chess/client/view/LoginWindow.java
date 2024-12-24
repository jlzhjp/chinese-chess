package io.github.jlzhjp.chess.client.view;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.jlzhjp.chess.common.view.AppWindow;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class LoginWindow extends AppWindow {
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
     * Create the application.
     */
    public LoginWindow() {
        initialize();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                LoginWindow window = new LoginWindow();
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
        frame.setResizable(false);
        frame.setTitle("登陆游戏");
        frame.setBounds(100, 100, 508, 259);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        panelServerInfo = new JPanel();
        panelServerInfo.setBorder(new TitledBorder(null, "服务器信息", TitledBorder.LEADING, TitledBorder.TOP, getDefaultFont(), null));
        panelServerInfo.setBounds(10, 11, 476, 59);
        frame.getContentPane().add(panelServerInfo);
        panelServerInfo.setLayout(null);

        lblIPAddress = new JLabel("IP 地址");
        lblIPAddress.setFont(getDefaultFont());
        lblIPAddress.setBounds(10, 24, 40, 14);
        panelServerInfo.add(lblIPAddress);

        txtIPAddress = new JTextField();
        txtIPAddress.setFont(getDefaultFont());
        txtIPAddress.setEditable(false);
        txtIPAddress.setText("127.0.0.1");
        txtIPAddress.setBounds(56, 21, 112, 20);
        panelServerInfo.add(txtIPAddress);
        txtIPAddress.setColumns(10);

        lblPort = new JLabel("端口号");
        lblPort.setFont(getDefaultFont());
        lblPort.setBounds(189, 24, 39, 14);
        panelServerInfo.add(lblPort);

        txtPort = new JTextField();
        txtPort.setFont(getDefaultFont());
        txtPort.setEditable(false);
        txtPort.setBounds(229, 21, 83, 20);
        panelServerInfo.add(txtPort);
        txtPort.setColumns(10);

        lblUserName = new JLabel("用户名");
        lblUserName.setFont(getDefaultFont());
        lblUserName.setBounds(334, 24, 48, 14);
        panelServerInfo.add(lblUserName);

        txtUserName = new JTextField();
        txtUserName.setFont(getDefaultFont());
        txtUserName.setBounds(374, 21, 83, 20);
        panelServerInfo.add(txtUserName);
        txtUserName.setColumns(10);

        panelCreateRoom = new JPanel();
        panelCreateRoom.setBorder(new TitledBorder(null, "创建房间", TitledBorder.LEADING, TitledBorder.TOP, getDefaultFont(), null));
        panelCreateRoom.setBounds(10, 81, 224, 132);
        frame.getContentPane().add(panelCreateRoom);
        panelCreateRoom.setLayout(null);

        btnCreateRoom = new JButton("创建房间");
        btnCreateRoom.setFont(getDefaultFont());
        btnCreateRoom.setBounds(68, 60, 89, 23);
        panelCreateRoom.add(btnCreateRoom);

        panelJoinRoom = new JPanel();
        panelJoinRoom.setBorder(new TitledBorder(null, "加入房间", TitledBorder.LEADING, TitledBorder.TOP, getDefaultFont(), null));
        panelJoinRoom.setBounds(244, 81, 242, 132);
        frame.getContentPane().add(panelJoinRoom);
        panelJoinRoom.setLayout(null);

        txtRoomID = new JTextField();
        txtRoomID.setFont(getDefaultFont());
        txtRoomID.setBounds(102, 41, 96, 20);
        panelJoinRoom.add(txtRoomID);
        txtRoomID.setColumns(10);

        lblRoomNumber = new JLabel("房间号");
        lblRoomNumber.setFont(getDefaultFont());
        lblRoomNumber.setBounds(52, 44, 48, 14);
        panelJoinRoom.add(lblRoomNumber);

        btnJoinRoom = new JButton("加入房间");
        btnJoinRoom.setFont(getDefaultFont());
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
}
