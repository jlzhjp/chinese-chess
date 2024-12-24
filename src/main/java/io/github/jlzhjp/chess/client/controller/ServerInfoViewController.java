package io.github.jlzhjp.chess.client.controller;

import io.github.jlzhjp.chess.client.view.LoginWindow;
import io.github.jlzhjp.chess.client.view.ServerInfoWindow;
import io.github.jlzhjp.chess.common.MessageSocketManager;
import io.github.jlzhjp.chess.common.socketManager.SocketManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerInfoViewController {
    ServerInfoWindow view;

    MessageSocketManager socketManager;

    public ServerInfoViewController(ServerInfoWindow view) {
        this.view = view;
        view.getConnectButton().addActionListener(this::handleConnectButtonClick);
    }

    public void handleConnectButtonClick(ActionEvent e) {
        String serverIPAddress = view.getServerIPAddressTextField().getText();
        String portString = view.getPortTextField().getText();

        InetAddress serverInetAddress;
        int port;

        try {
            port = Integer.parseInt(portString);
            serverInetAddress = InetAddress.getByName(serverIPAddress);
        } catch (NumberFormatException ex) {
            SwingUtilities.invokeLater(() -> view.showErrorMessageBox("端口号必须是一个整数", "连接失败"));
            return;
        } catch (UnknownHostException ex) {
            SwingUtilities.invokeLater(() -> view.showErrorMessageBox("无法解析服务器地址", "连接失败"));
            return;
        }

        socketManager = new MessageSocketManager(SocketManager.connect(serverInetAddress, port));
        socketManager.setErrorHandler((ex) -> SwingUtilities.invokeLater(() -> view.showErrorMessageBox(ex.getMessage(), "错误")));
        try {
            socketManager.start();

            SwingUtilities.invokeLater(() -> {
                view.close();
                view.dispose();

                LoginWindow loginWindow = new LoginWindow();
                LoginViewController loginViewController = new LoginViewController(loginWindow, socketManager);

                loginWindow.show();
            });
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> view.showErrorMessageBox(ex.getMessage(), "连接失败"));
        }
    }
}
