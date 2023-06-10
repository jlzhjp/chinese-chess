package edu.henu.chineseChess.server.controller;

import edu.henu.chineseChess.server.model.GameTable;
import edu.henu.chineseChess.server.view.WatchBattleWindow;

public class WatchBattleWindowController {
    WatchBattleWindow view;
    GameTable gameTable;

    public WatchBattleWindowController(WatchBattleWindow view, GameTable gameTable) {
        this.view = view;
        view.setTitle("观战 - 红: " + gameTable.getRedPlayer() + " vs. " + "黑: " + gameTable.getBlackPlayer());
        this.gameTable = gameTable;
        view.getChessPanel().setModel(gameTable.getChessPanelModel());
    }
}
