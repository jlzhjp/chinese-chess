package edu.henu.chinesechess.server;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import edu.henu.chinesechess.server.controller.ServerViewController;
import edu.henu.chinesechess.server.view.ServerWindow;

import java.util.Arrays;

public class ServerMain {
    public static void main(String[] args) {
        if (Arrays.asList(args).contains("dark")) {
            FlatDarkLaf.setup();
        } else {
            FlatLightLaf.setup();
        }
        ServerWindow window = new ServerWindow();
        ServerViewController controller = new ServerViewController(window);
        window.show();
    }
}
