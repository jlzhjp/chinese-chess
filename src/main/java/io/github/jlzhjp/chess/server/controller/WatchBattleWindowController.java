package io.github.jlzhjp.chess.server.controller;

import io.github.jlzhjp.chess.server.model.GameTable;
import io.github.jlzhjp.chess.server.view.WatchBattleWindow;

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
