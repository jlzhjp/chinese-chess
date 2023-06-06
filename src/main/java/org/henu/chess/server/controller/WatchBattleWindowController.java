package org.henu.chess.server.controller;

import org.henu.chess.common.model.ChessPanelModel;
import org.henu.chess.server.view.WatchBattleWindow;

public class WatchBattleWindowController {
    WatchBattleWindow view;
    ChessPanelModel model;

    public WatchBattleWindowController(WatchBattleWindow view, ChessPanelModel model) {
        this.view = view;
        this.model = model;
        view.getChessPanel().setModel(model);
    }
}
