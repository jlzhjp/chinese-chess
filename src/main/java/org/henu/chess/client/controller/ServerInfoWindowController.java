package org.henu.chess.client.controller;

import org.henu.chess.client.view.LoginWindow;
import org.henu.chess.client.view.ServerInfoWindow;
import org.henu.chess.common.SocketMessageReceiver;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ServerInfoWindowController {
    ServerInfoWindow view;

    SocketMessageReceiver receiver;

    public ServerInfoWindowController(ServerInfoWindow view) {
        this.view = view;
        view.getConnectButton().addActionListener(this::handleConnectButtonClick);
    }

    public void handleConnectButtonClick(ActionEvent e) {
        String serverIPAddress = view.getServerIPAddressTextField().getText();
        String portString  = view.getPortTextField().getText();

        int port;

        try {
            port = Integer.parseInt(portString);
        } catch (NumberFormatException ex) {
            SwingUtilities.invokeLater(() -> view.showErrorMessageBox("端口号必须是一个整数", "连接失败"));
            return;
        }

        try {
             receiver = new SocketMessageReceiver(serverIPAddress, port);
             receiver.listen();
        } catch (IOException ex) {
            SwingUtilities.invokeLater(() -> view.showErrorMessageBox(ex.getMessage(), "连接失败"));
            return;
        }

        SwingUtilities.invokeLater(() -> {
            view.dispose();

            LoginWindow loginWindow = new LoginWindow();
            LoginViewController loginViewController =  new LoginViewController(loginWindow, receiver);

            loginWindow.show();
        });
    }
}
