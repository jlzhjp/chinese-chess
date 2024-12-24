package io.github.jlzhjp.chess.common.model;

public enum Piece {
    RED_GENERAL("帥", true, "RK"),
    RED_GUARD("仕", true, "RA"),
    RED_ELEPHANT("相", true, "RB"),
    RED_HORSE("傌", true, "RN"),
    RED_CHARIOT("俥", true, "RR"),
    RED_CANNON("炮", true, "RC"),
    RED_SOLDIER("兵", true, "RP"),
    BLACK_GENERAL("將", false, "BK"),
    BLACK_GUARD("士", false, "BA"),
    BLACK_ELEPHANT("象", false, "BB"),
    BLACK_HORSE("馬", false, "BN"),
    BLACK_CHARIOT("車", false, "BR"),
    BLACK_CANNON("砲", false, "BC"),
    BLACK_SOLDIER("卒", false, "BP");

    private final String symbol;
    private final boolean isRed;
    private final String imageFileName;

    Piece(String symbol, boolean isRed, String imageFileName) {
        this.symbol = symbol;
        this.isRed = isRed;
        this.imageFileName = imageFileName;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isRed() {
        return isRed;
    }

    public String getImageFileName() {
        return imageFileName;
    }
}