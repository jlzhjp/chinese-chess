package edu.henu.chinesechess;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import edu.henu.chinesechess.client.ClientMain;
import edu.henu.chinesechess.common.Singletons;
import edu.henu.chinesechess.server.ServerMain;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> argList = Arrays.asList(args);
        if (argList.contains("dark")) {
            FlatDarkLaf.setup();
            Singletons.isDarkMode = true;
        } else {
            FlatLightLaf.setup();
            Singletons.isDarkMode = false;
        }

        if (argList.contains("server")) {
            ServerMain.main(args);
        } else if (argList.contains("client")) {
            ClientMain.main(args);
        }
    }
}
