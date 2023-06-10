package edu.henu.chineseChess.server;

import edu.henu.chineseChess.server.controller.ServerViewController;
import edu.henu.chineseChess.server.view.ServerWindow;

public class ServerMain {
    public static void main(String[] args) {
        ServerWindow window = new ServerWindow();
        ServerViewController controller = new ServerViewController(window);
        window.show();
    }
}
