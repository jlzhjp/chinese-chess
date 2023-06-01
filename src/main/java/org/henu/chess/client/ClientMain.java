package org.henu.chess.client;

import com.formdev.flatlaf.FlatLightLaf;
import org.henu.chess.client.controller.ServerInfoViewController;
import org.henu.chess.client.view.ServerInfoWindow;

public class ClientMain {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        ServerInfoWindow serverInfoWindow = new ServerInfoWindow();
        ServerInfoViewController serverInfoViewController = new ServerInfoViewController(serverInfoWindow);
        serverInfoWindow.show();
    }
}
