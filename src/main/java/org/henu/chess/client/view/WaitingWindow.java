package org.henu.chess.client.view;

import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;

import javax.swing.*;

public class WaitingWindow {

    private JFrame frame;
    private JLabel lblUserName;
    private JLabel lblRoomID;
    private JLabel lblUserNameValue;

    private JTextField txtRoomID;
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                WaitingWindow window = new WaitingWindow();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the application.
     */
    public WaitingWindow() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("等待中...");
        frame.setBounds(100, 100, 450, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        lblUserName = new JLabel("用户名：");
        lblUserName.setFont(new Font("SimSun", Font.BOLD, 16));
        lblUserName.setBounds(74, 56, 73, 19);
        frame.getContentPane().add(lblUserName);

        lblRoomID = new JLabel("房间号：");
        lblRoomID.setFont(new Font("SimSun", Font.BOLD, 16));
        lblRoomID.setBounds(74, 86, 73, 19);
        frame.getContentPane().add(lblRoomID);

        lblUserNameValue = new JLabel("");
        lblUserNameValue.setFont(new Font("SimSun", Font.PLAIN, 16));
        lblUserNameValue.setBounds(157, 61, 271, 14);
        frame.getContentPane().add(lblUserNameValue);


        txtRoomID = new JTextField();
        txtRoomID.setEditable(false);
        txtRoomID.setBounds(141, 86, 287, 20);
        txtRoomID.setColumns(10);
        frame.getContentPane().add(txtRoomID);
    }

    public JLabel getUserNameValueLabel() {
        return lblUserNameValue;
    }

    public JTextField getRoomIDTextField() {
        return txtRoomID;
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
