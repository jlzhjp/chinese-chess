package io.github.jlzhjp.chess.server;

import io.github.jlzhjp.chess.server.controller.ServerViewController;
import io.github.jlzhjp.chess.server.view.ServerWindow;

public class ServerMain {
    public static void main(String[] args) {
        ServerWindow window = new ServerWindow();
        ServerViewController controller = new ServerViewController(window);
        window.show();
    }
}
