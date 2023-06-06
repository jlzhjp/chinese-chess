package edu.henu.chinesechess.client;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import edu.henu.chinesechess.client.controller.ServerInfoViewController;
import edu.henu.chinesechess.client.view.ServerInfoWindow;

import java.util.Arrays;

public class ClientMain {
    public static void main(String[] args) {
        if (Arrays.asList(args).contains("dark")) {
            FlatDarkLaf.setup();
        } else {
            FlatLightLaf.setup();
        }
        ServerInfoWindow serverInfoWindow = new ServerInfoWindow();
        ServerInfoViewController serverInfoViewController = new ServerInfoViewController(serverInfoWindow);
        serverInfoWindow.show();
    }
}
