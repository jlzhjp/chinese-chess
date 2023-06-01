package org.henu.chess.common.view;

import org.henu.chess.common.ImageLoader;
import org.henu.chess.common.model.ChessBoardPoint;
import org.henu.chess.common.model.ChessPanelModel;
import org.henu.chess.common.model.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class ChessPanel extends JPanel implements PropertyChangeListener {
    public interface ChessPanelListener {
        void onChessBoardClicked(ChessBoardPoint point);
    }

    static final int LATTICE_SIZE = 57;
    static final int MARGIN = 32;
    static final int PIECE_SIZE = 57;
    private final Image boardImage = ImageLoader.load("/WOOD_BOARD.GIF");
    private ChessPanelListener listener;
    private ChessPanelModel model;

    public ChessPanel() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                ChessBoardPoint point = getChessBoardPoint(x, y);
                if (!model.isRed()) {
                    point = new ChessBoardPoint(point.getX(), model.getLogic().getBoardHeight() - point.getY() - 1);
                }
                if (Objects.nonNull(listener)) {
                    listener.onChessBoardClicked(point);
                }
            }
        });
    }

    public ChessPanelModel getModel() {
        return model;
    }

    public void setModel(ChessPanelModel model) {
        if (Objects.nonNull(this.model)) {
            this.model.removePropertyChangeListener(this);
        }
        model.addPropertyChangeListener(this);

        this.model = model;

        this.repaint();
    }

    public void setListener(ChessPanelListener listener) {
        this.listener = listener;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintChessBoard(g);
    }

    public void paintChessBoard(Graphics g) {
        g.drawImage(boardImage, 0, 0, boardImage.getWidth(this), boardImage.getHeight(this), this);

        var pieces = model.getPieces();
        for (var piece : pieces.entrySet()) {
            paintPiece(g, piece.getKey(), piece.getValue());
        }
        for (var move : model.getAvailableMoves()) {
            paintAvailableMove(g, move);
        }
    }

    private void paintPiece(Graphics g, ChessBoardPoint point, Piece piece) {
        if (!model.isRed()) {
            point = new ChessBoardPoint(point.getX(), model.getLogic().getBoardHeight() - point.getY() - 1);
        }
        Point2D screenPoint = getScreenPoint(point);
        int screenX = (int) screenPoint.getX() - PIECE_SIZE / 2;
        int screenY = (int) screenPoint.getY() - PIECE_SIZE / 2;
        if (point.equals(model.getSelectedPoint())) {
            g.drawImage(ImageLoader.load("/pieces/" + piece.getImageFileName() + "S.GIF"), screenX, screenY, PIECE_SIZE, PIECE_SIZE, this);
        } else {
            g.drawImage(ImageLoader.load("/pieces/" + piece.getImageFileName() + ".GIF"), screenX, screenY, PIECE_SIZE, PIECE_SIZE, this);
        }
    }

    private Point2D getScreenPoint(ChessBoardPoint point) {
        Point2D screenPoint = new Point2D.Double();
        screenPoint.setLocation(point.getX() * LATTICE_SIZE + MARGIN, point.getY() * LATTICE_SIZE + MARGIN);
        return screenPoint;
    }

    public ChessBoardPoint getChessBoardPoint(int x, int y) {
        int chessBoardX = (int) Math.round(((double) x - MARGIN) / LATTICE_SIZE);
        int chessBoardY = (int) Math.round(((double) y - MARGIN) / LATTICE_SIZE);
        return new ChessBoardPoint(chessBoardX, chessBoardY);
    }

    public void paintAvailableMove(Graphics g, ChessBoardPoint move) {
        Point2D screenPoint = getScreenPoint(move);
        int screenX = (int) screenPoint.getX() - PIECE_SIZE / 2;
        int screenY = (int) screenPoint.getY() - PIECE_SIZE / 2;
        g.drawImage(ImageLoader.load("/pieces/OOS.GIF"), screenX, screenY, PIECE_SIZE, PIECE_SIZE, this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }
}