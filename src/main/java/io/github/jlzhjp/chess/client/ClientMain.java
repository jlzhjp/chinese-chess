package io.github.jlzhjp.chess.client;

import io.github.jlzhjp.chess.client.controller.ServerInfoViewController;
import io.github.jlzhjp.chess.client.view.ServerInfoWindow;

public class ClientMain {
    public static void main(String[] args) {
        ServerInfoWindow serverInfoWindow = new ServerInfoWindow();
        ServerInfoViewController serverInfoViewController = new ServerInfoViewController(serverInfoWindow);
        serverInfoWindow.show();
    }
}
