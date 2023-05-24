package org.henu.chess.common.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ChessLogicImpl implements ChessLogic {
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

        return switch (piece) {
            case RED_GENERAL, BLACK_GENERAL -> getAvailablePointForGeneral(pieces, point);
            case RED_GUARD, BLACK_GUARD -> getAvailablePointForGuard(pieces, point);
            case RED_ELEPHANT, BLACK_ELEPHANT -> getAvailablePointForElephant(pieces, point);
            case RED_HORSE, BLACK_HORSE -> getAvailablePointForHorse(pieces, point);
            case RED_CHARIOT, BLACK_CHARIOT -> getAvailablePointForChariot(pieces, point);
            case RED_CANNON, BLACK_CANNON -> getAvailablePointForCannon(pieces, point);
            case RED_SOLDIER, BLACK_SOLDIER -> getAvailablePointForSoldier(pieces, point);
        };
    }

    private List<ChessBoardPoint> getAvailablePointForGeneral(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        List<ChessBoardPoint> moves = new ArrayList<>();
        Piece piece = pieces.get(point);

        int x = point.getX();
        int y = point.getY();

        if (piece.isRed()) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (i >= 3 && i <= 5 && j >= 0 && j <= 2) {
                        moves.add(new ChessBoardPoint(i, j));
                    }
                }
            }
        } else {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (i >= 3 && i <= 5 && j >= 7 && j <= 9) {
                        moves.add(new ChessBoardPoint(i, j));
                    }
                }
            }
        }

        return moves;
    }

    private List<ChessBoardPoint> getAvailablePointForGuard(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        ArrayList<ChessBoardPoint> moves = new ArrayList<>();
        Piece piece = pieces.get(point);

        int x = point.getX();
        int y = point.getY();

        if (piece.isRed()) {
            for (int i = x - 1; i <= x + 1; i += 2) {
                for (int j = y - 1; j <= y + 1; j += 2) {
                    if (i >= 3 && i <= 5 && j >= 0 && j <= 2) {
                        moves.add(new ChessBoardPoint(i, j));
                    }
                }
            }
        } else {
            for (int i = x - 1; i <= x + 1; i += 2) {
                for (int j = y - 1; j <= y + 1; j += 2) {
                    if (i >= 3 && i <= 5 && j >= 7 && j <= 9) {
                        moves.add(new ChessBoardPoint(i, j));
                    }
                }
            }
        }

        return moves;
    }

    private List<ChessBoardPoint> getAvailablePointForElephant(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        int x = point.getX();
        int y = point.getY();

        Piece piece = pieces.get(point);
        boolean isRed = piece.isRed();

        ArrayList<ChessBoardPoint> moves = new ArrayList<>();

        int[][] elephantMoves = {{-2, -2}, {-2, 2}, {2, -2}, {2, 2}};
        int[][] checkPoints = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int i = 0; i < elephantMoves.length; ++i) {
            int[] move = elephantMoves[i];
            int dx = move[0];
            int dy = move[1];
            int newX = x + dx;
            int newY = y + dy;

            if (isRed && newY < 5 || !isRed && newY >= 5) {
                int checkX = checkPoints[i][0];
                int checkY = checkPoints[i][1];

                if (!pieces.containsKey(new ChessBoardPoint(checkX, checkY))) {
                    addToListIfValid(moves, new ChessBoardPoint(newX, newY), pieces, isRed);
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

            if (!pieces.containsKey(new ChessBoardPoint(checkX, checkY))) {
                assert checkY == 0 || checkX == 0;

                if (checkX == 0) {
                    addToListIfValid(moves, new ChessBoardPoint(x - 1, y + checkY * 2), pieces, isRed);
                    addToListIfValid(moves, new ChessBoardPoint(x + 1, y + checkY * 2), pieces, isRed);
                } else {
                    addToListIfValid(moves, new ChessBoardPoint(x + checkX * 2, y - 1), pieces, isRed);
                    addToListIfValid(moves, new ChessBoardPoint(x + checkX * 2, y + 1), pieces, isRed);
                }
            }
        }

        return moves;
    }

    private List<ChessBoardPoint> getAvailablePointForChariot(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        return new ArrayList<>();
    }

    private List<ChessBoardPoint> getAvailablePointForCannon(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        return new ArrayList<>();
    }

    private List<ChessBoardPoint> getAvailablePointForSoldier(Map<ChessBoardPoint, Piece> pieces, ChessBoardPoint point) {
        return new ArrayList<>();
    }

    private void addToListIfValid(ArrayList<ChessBoardPoint> points, ChessBoardPoint p, Map<ChessBoardPoint, Piece> pieces, boolean isRed) {
        if (p.getX() < 0 || p.getX() >= BOARD_WIDTH || p.getY() < 0 || p.getY() >= BOARD_HEIGHT) {
            return;
        }

        Piece pieceAtTargetPoint = pieces.get(p);

        if (pieceAtTargetPoint == null) {
            points.add(p);
        } else {
            if (pieceAtTargetPoint.isRed() != isRed) {
                points.add(p);
            }
        }
    }

}
