package io.github.jlzhjp.chess.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChessLogicImpl implements ChessLogic {
    private final int BOARD_HEIGHT = 10;
    private final int BOARD_WIDTH = 9;

    @Override
    public int getBoardWidth() {
        return BOARD_WIDTH;
    }

    @Override
    public int getBoardHeight() {
        return BOARD_HEIGHT;
    }

    @Override
    public List<ChessBoardPoint> getAvailablePointForPieceAt(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        Piece piece = pieces.get(point);

        switch (piece) {
            case RED_GENERAL:
            case BLACK_GENERAL:
                return getAvailablePointForGeneral(pieces, point);
            case RED_GUARD:
            case BLACK_GUARD:
                return getAvailablePointForGuard(pieces, point);
            case RED_ELEPHANT:
            case BLACK_ELEPHANT:
                return getAvailablePointForElephant(pieces, point);
            case RED_HORSE:
            case BLACK_HORSE:
                return getAvailablePointForHorse(pieces, point);
            case RED_CHARIOT:
            case BLACK_CHARIOT:
                return getAvailablePointForChariot(pieces, point);
            case RED_CANNON:
            case BLACK_CANNON:
                return getAvailablePointForCannon(pieces, point);
            case RED_SOLDIER:
            case BLACK_SOLDIER:
                return getAvailablePointForSoldier(pieces, point);
            default:
                throw new IllegalArgumentException("Unknown piece: " + piece);
        }
    }

    private List<ChessBoardPoint> getAvailablePointForGeneral(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        ArrayList<ChessBoardPoint> moves = new ArrayList<>();
        Piece piece = pieces.get(point);

        int x = point.getX();
        int y = point.getY();

        int upper_limit, lower_limit;

        if (piece.isRed()) {
            upper_limit = 9;
            lower_limit = 7;
        } else {
            upper_limit = 2;
            lower_limit = 0;
        }

        if (x - 1 >= 3) {
            addPoint(moves, x - 1, y, pieces, piece.isRed());
        }
        if (x + 1 <= 5) {
            addPoint(moves, x + 1, y, pieces, piece.isRed());
        }

        if (y - 1 >= lower_limit) {
            addPoint(moves, x, y - 1, pieces, piece.isRed());
        }
        if (y + 1 <= upper_limit) {
            addPoint(moves, x, y + 1, pieces, piece.isRed());
        }

        return moves;
    }

    private List<ChessBoardPoint> getAvailablePointForGuard(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        ArrayList<ChessBoardPoint> moves = new ArrayList<>();
        Piece piece = pieces.get(point);

        int x = point.getX();
        int y = point.getY();

        int upper_limit, lower_limit;

        if (piece.isRed()) {
            upper_limit = 9;
            lower_limit = 7;
        } else {
            upper_limit = 2;
            lower_limit = 0;
        }

        for (int i = x - 1; i <= x + 1; i += 2) {
            for (int j = y - 1; j <= y + 1; j += 2) {
                if (i >= 3 && i <= 5 && j >= lower_limit && j <= upper_limit) {
                    addPoint(moves, i, j, pieces, piece.isRed());
                }
            }
        }

        return moves;
    }

    private List<ChessBoardPoint> getAvailablePointForElephant(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        int x = point.getX();
        int y = point.getY();

        Piece piece = pieces.get(point);

        int upper_limit, lower_limit;

        if (piece.isRed()) {
            upper_limit = 9;
            lower_limit = 5;
        } else {
            upper_limit = 4;
            lower_limit = 0;
        }

        ArrayList<ChessBoardPoint> moves = new ArrayList<>();

        int[][] checkPoints = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int[] checkPoint : checkPoints) {
            if (noPiece(pieces, x + checkPoint[0], y + checkPoint[1])) {
                int newX = point.getX() + checkPoint[0] * 2;
                int newY = point.getY() + checkPoint[1] * 2;
                if (newY >= lower_limit && newY <= upper_limit) {
                    addPoint(moves, newX, newY, pieces, piece.isRed());
                }
            }
        }

        return moves;
    }

    // (2, 1) -> (1, 0)
    // (2, -1) -> (1, 0)

    // (1, -2) -> (0, -1)
    // (-1, -2) -> (0, -1)

    // (-2, -1) -> (-1, 0)
    // (-2, 1) -> (-1, 0)

    // (-1, 2) -> (0, 1)
    // (1, 2) -> (0, 1)
    private List<ChessBoardPoint> getAvailablePointForHorse(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        ArrayList<ChessBoardPoint> moves = new ArrayList<>();
        Piece piece = pieces.get(point);
        boolean isRed = piece.isRed();

        int x = point.getX();
        int y = point.getY();

        int[][] checkPoints = {{1, 0}, {0, -1}, {-1, 0}, {0, 1}};

        for (int[] checkPoint : checkPoints) {
            int checkX = checkPoint[0];
            int checkY = checkPoint[1];

            if (noPiece(pieces, x + checkX, y + checkY)) {
                assert checkY == 0 || checkX == 0;

                if (checkX == 0) {
                    addPoint(moves, x - 1, y + checkY * 2, pieces, isRed);
                    addPoint(moves, x + 1, y + checkY * 2, pieces, isRed);
                } else {
                    addPoint(moves, x + checkX * 2, y - 1, pieces, isRed);
                    addPoint(moves, x + checkX * 2, y + 1, pieces, isRed);
                }
            }
        }

        return moves;
    }

    // 获取车的可走位置
    private List<ChessBoardPoint> getAvailablePointForChariot(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        ArrayList<ChessBoardPoint> moves = new ArrayList<>();
        Piece piece = pieces.get(point);
        boolean isRed = piece.isRed();

        int x = point.getX();
        int y = point.getY();

        addDirectionForChariot(moves, x, y, -1, 0, pieces, isRed);
        addDirectionForChariot(moves, x, y, 1, 0, pieces, isRed);
        addDirectionForChariot(moves, x, y, 0, -1, pieces, isRed);
        addDirectionForChariot(moves, x, y, 0, 1, pieces, isRed);

        return moves;
    }

    // 获取炮的可走位置
    private List<ChessBoardPoint> getAvailablePointForCannon(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        ArrayList<ChessBoardPoint> moves = new ArrayList<>();
        Piece piece = pieces.get(point);
        boolean isRed = piece.isRed();

        int x = point.getX();
        int y = point.getY();

        addDirectionForCannon(moves, x, y, -1, 0, pieces, isRed, true);
        addDirectionForCannon(moves, x, y, 1, 0, pieces, isRed, true);
        addDirectionForCannon(moves, x, y, 0, -1, pieces, isRed, true);
        addDirectionForCannon(moves, x, y, 0, 1, pieces, isRed, true);

        return moves;
    }

    // 获取兵可以到达的点
    private List<ChessBoardPoint> getAvailablePointForSoldier(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        ArrayList<ChessBoardPoint> moves = new ArrayList<>();
        Piece piece = pieces.get(point);
        boolean isRed = piece.isRed();

        int upper_limit, lower_limit;
        int dy;

        int x = point.getX();
        int y = point.getY();

        if (isRed) {
            upper_limit = 9;
            lower_limit = 5;
            dy = -1;
        } else {
            upper_limit = 4;
            lower_limit = 0;
            dy = 1;
        }

        if (lower_limit <= y && y <= upper_limit) {
            addPoint(moves, x, y + dy, pieces, isRed);
        } else {
            addPoint(moves, x, y + dy, pieces, isRed);
            addPoint(moves, x - 1, y, pieces, isRed);
            addPoint(moves, x + 1, y, pieces, isRed);
        }

        return moves;
    }

    private Piece pieceAt(Map<ChessBoardPoint, Piece> pieces, int x, int y) {
        return pieces.get(new ChessBoardPoint(x, y));
    }

    private boolean noPiece(Map<ChessBoardPoint, Piece> pieces, int x, int y) {
        return pieceAt(pieces, x, y) == null;
    }


    private void addDirectionForChariot(ArrayList<ChessBoardPoint> moves, int startX, int startY, int dx, int dy, Map<ChessBoardPoint, Piece> pieces, boolean isRed) {
        int x = startX;
        int y = startY;

        while (true) {
            x += dx;
            y += dy;

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                break;
            }

            Piece piece = pieceAt(pieces, x, y);

            addPoint(moves, x, y, pieces, isRed);

            if (piece != null) {
                break;
            }
        }
    }

    private void addDirectionForCannon(ArrayList<ChessBoardPoint> moves, int startX, int startY, int dx, int dy, Map<ChessBoardPoint, Piece> pieces, boolean isRed, boolean includePath) {
        int x = startX;
        int y = startY;

        while (true) {
            x += dx;
            y += dy;

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                break;
            }

            Piece piece = pieceAt(pieces, x, y);

            if (includePath) {
                if (piece == null) {
                    moves.add(new ChessBoardPoint(x, y));
                } else {
                    addDirectionForCannon(moves, x, y, dx, dy, pieces, isRed, false);
                    break;
                }
            } else {
                if (piece != null) {
                    addPoint(moves, x, y, pieces, isRed);
                    break;
                }
            }
        }
    }

    private void addPoint(ArrayList<ChessBoardPoint> moves, int x, int y, Map<ChessBoardPoint, Piece> pieces, boolean isRed) {
        if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
            return;
        }

        ChessBoardPoint p = new ChessBoardPoint(x, y);
        Piece pieceAtTargetPoint = pieces.get(p);

        if (pieceAtTargetPoint == null) {
            moves.add(p);
        } else {
            if (pieceAtTargetPoint.isRed() != isRed) {
                moves.add(p);
            }
        }
    }
}
