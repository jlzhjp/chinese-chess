package io.github.jlzhjp.chess.common;

import io.github.jlzhjp.chess.common.model.ChessBoardPoint;
import io.github.jlzhjp.chess.common.model.ChessRecord;
import io.github.jlzhjp.chess.common.model.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChessRecorder {
    private final ArrayList<ChessRecord> steps = new ArrayList<>();
    int width;
    int height;

    public ChessRecorder(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static String convertToChinese(int n) {
        switch (n) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "七";
            case 8:
                return "八";
            case 9:
                return "九";
            default:
                return "";
        }
    }

    public ChessRecord add(Piece piece, ChessBoardPoint from, ChessBoardPoint to) {
        StringBuilder result = new StringBuilder();
        result.append(piece.getSymbol());

        int startX, endX;

        if (piece.isRed()) {
            startX = width - from.getX();
            endX = width - to.getX();
        } else {
            startX = from.getX() + 1;
            endX = to.getX() + 1;
        }

        String startXChinese = piece.isRed() ? convertToChinese(startX) : Integer.toString(startX);
        String endXChinese = piece.isRed() ? convertToChinese(endX) : Integer.toString(endX);

        int dy = to.getY() - from.getY();
        if (piece.isRed()) {
            dy = -dy;
        }

        String dyChinese = piece.isRed() ? convertToChinese(Math.abs(dy)) : Integer.toString(Math.abs(dy));

        switch (piece) {
            case RED_GUARD:
            case BLACK_GUARD:
            case RED_HORSE:
            case BLACK_HORSE:
            case RED_ELEPHANT:
            case BLACK_ELEPHANT: {
                assert dy != 0;

                result.append(startXChinese);
                if (dy > 0) {
                    result.append("进").append(endXChinese);
                } else {
                    result.append("退").append(endXChinese);
                }
                break;
            }
            default: {
                result.append(startXChinese);

                if (dy > 0) {
                    result.append("进").append(dyChinese);
                } else if (dy < 0) {
                    result.append("退").append(dyChinese);
                } else {
                    result.append("平").append(endXChinese);
                }
                break;
            }
        }
        String stepString = result.toString();

        ChessRecord record = new ChessRecord();
        record.setPiece(piece);
        record.setStepString(stepString);
        record.setFrom(from);
        record.setTo(to);

        steps.add(record);
        return record;
    }

    public List<ChessRecord> getSteps() {
        return steps;
    }

    public ListCellRenderer<Object> getCellRenderer(Font defaultFont) {
        return new CustomListCellRenderer(defaultFont);
    }

    static class CustomListCellRenderer extends DefaultListCellRenderer {
        private final Font defaultFont;

        public CustomListCellRenderer(Font defaultFont) {
            this.defaultFont = defaultFont;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            ChessRecord record = (ChessRecord) value;
            label.setFont(defaultFont);
            if (record.getPiece().isRed()) {
                if (Singletons.isDarkMode) {
                    label.setForeground(new Color(240, 84, 84));
                } else {
                    label.setForeground(new Color(179, 19, 18));
                }
            } else {
                if (Singletons.isDarkMode) {
                    label.setForeground(new Color(245, 245, 245));
                } else {
                    label.setForeground(new Color(43, 42, 76));
                }
            }
            return label;
        }
    }
}

