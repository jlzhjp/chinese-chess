package org.henu.chess.common.messages.response;

public class StartGameResponse {
    private String redSideUserName;

    private String blackSideUserName;

    public String getRedSideUserName() {
        return redSideUserName;
    }
    public void setRedSideUserName(String redSideUserName) {
        this.redSideUserName = redSideUserName;
    }

    public String getBlackSideUserName() {
        return blackSideUserName;
    }

    public void setBlackSideUserName(String blackSideUserName) {
        this.blackSideUserName = blackSideUserName;
    }
}
