package edu.henu.chinesechess.client;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import edu.henu.chinesechess.client.controller.ServerInfoViewController;
import edu.henu.chinesechess.client.view.ServerInfoWindow;
import edu.henu.chinesechess.common.Singletons;

import java.util.Arrays;

public class ClientMain {
    public static void main(String[] args) {
        ServerInfoWindow serverInfoWindow = new ServerInfoWindow();
        ServerInfoViewController serverInfoViewController = new ServerInfoViewController(serverInfoWindow);
        serverInfoWindow.show();
    }
}
