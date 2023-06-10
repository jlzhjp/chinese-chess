package edu.henu.chinesechess.server;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import edu.henu.chinesechess.common.Singletons;
import edu.henu.chinesechess.server.controller.ServerViewController;
import edu.henu.chinesechess.server.view.ServerWindow;

import java.util.Arrays;

public class ServerMain {
    public static void main(String[] args) {
        ServerWindow window = new ServerWindow();
        ServerViewController controller = new ServerViewController(window);
        window.show();
    }
}
