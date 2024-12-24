package io.github.jlzhjp.chess.client.view;

import com.formdev.flatlaf.FlatLightLaf;
import io.github.jlzhjp.chess.common.view.AppWindow;

import javax.swing.*;
import java.awt.*;

public class WaitingWindow extends AppWindow {
    private JLabel lblUserName;
    private JLabel lblRoomID;
    private JLabel lblUserNameValue;

    private JTextField txtRoomID;

    /**
     * Create the application.
     */
    public WaitingWindow() {
        initialize();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                WaitingWindow window = new WaitingWindow();
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
        getFrame().setResizable(false);
        getFrame().setTitle("等待中...");
        getFrame().setBounds(100, 100, 450, 200);
        getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getFrame().getContentPane().setLayout(null);

        lblUserName = new JLabel("用户名：");
        lblUserName.setFont(getDefaultFont());
        lblUserName.setBounds(74, 56, 73, 19);
        getFrame().getContentPane().add(lblUserName);

        lblRoomID = new JLabel("房间号：");
        lblRoomID.setFont(getDefaultFont());
        lblRoomID.setBounds(74, 86, 73, 19);
        getFrame().getContentPane().add(lblRoomID);

        lblUserNameValue = new JLabel("");
        lblUserNameValue.setFont(getDefaultFont());
        lblUserNameValue.setBounds(157, 61, 271, 14);
        getFrame().getContentPane().add(lblUserNameValue);


        txtRoomID = new JTextField();
        txtRoomID.setFont(getDefaultFont());
        txtRoomID.setEditable(false);
        txtRoomID.setBounds(157, 86, 271, 20);
        txtRoomID.setColumns(10);
        getFrame().getContentPane().add(txtRoomID);
    }

    public JLabel getUserNameValueLabel() {
        return lblUserNameValue;
    }

    public JTextField getRoomIDTextField() {
        return txtRoomID;
    }
}
