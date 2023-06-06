package edu.henu.chinesechess.client.controller;

import edu.henu.chinesechess.client.view.LoginWindow;
import edu.henu.chinesechess.client.view.ServerInfoWindow;
import edu.henu.chinesechess.common.SocketMessageReceiver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.Socket;

public class ServerInfoViewController {
    ServerInfoWindow view;

    SocketMessageReceiver receiver;

    public ServerInfoViewController(ServerInfoWindow view) {
        this.view = view;
        view.getConnectButton().addActionListener(this::handleConnectButtonClick);
    }

    public void handleConnectButtonClick(ActionEvent e) {
        String serverIPAddress = view.getServerIPAddressTextField().getText();
        String portString = view.getPortTextField().getText();

        int port;

        try {
            port = Integer.parseInt(portString);
        } catch (NumberFormatException ex) {
            SwingUtilities.invokeLater(() -> view.showErrorMessageBox("端口号必须是一个整数", "连接失败"));
            return;
        }

        try {
            Socket socket = new Socket(serverIPAddress, port);
            receiver = new SocketMessageReceiver(socket);
            receiver.listen();
        } catch (IOException ex) {
            SwingUtilities.invokeLater(() -> view.showErrorMessageBox(ex.getMessage(), "连接失败"));
            return;
        }

        SwingUtilities.invokeLater(() -> {
            view.close();
            view.dispose();

            LoginWindow loginWindow = new LoginWindow();
            LoginViewController loginViewController = new LoginViewController(loginWindow, receiver);

            loginWindow.show();
        });
    }
}
