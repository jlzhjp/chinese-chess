package edu.henu.chineseChess.client;

import edu.henu.chineseChess.client.controller.ServerInfoViewController;
import edu.henu.chineseChess.client.view.ServerInfoWindow;

public class ClientMain {
    public static void main(String[] args) {
        ServerInfoWindow serverInfoWindow = new ServerInfoWindow();
        ServerInfoViewController serverInfoViewController = new ServerInfoViewController(serverInfoWindow);
        serverInfoWindow.show();
    }
}
