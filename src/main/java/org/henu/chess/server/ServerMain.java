package org.henu.chess.server;

import com.formdev.flatlaf.FlatLightLaf;
import org.henu.chess.server.controller.ServerViewController;
import org.henu.chess.server.view.ServerWindow;

public class ServerMain {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        ServerWindow window = new ServerWindow();
        ServerViewController controller = new ServerViewController(window);
        window.show();
    }
}
