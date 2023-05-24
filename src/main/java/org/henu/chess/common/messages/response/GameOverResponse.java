package org.henu.chess.common.messages.response;

import org.henu.chess.common.messages.request.Request;

public class GameOverResponse extends Request {
    private String winner;

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
