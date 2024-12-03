package ru.utin.game.domain;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board {
    private Cell[][] board = new Cell[3][3];
    private TypeCell typeCell;
    private GraphicsContext gc;
    private Canvas canvas;
    private final int size;

    public Board(int size) {
        this.size = size;
        init(size);
    }

    public void clear() {
        init(size);
    }

    private void init(int size) {
        canvas = new Canvas(size, size);
        gc = canvas.getGraphicsContext2D();
        for (int i = 0, x = 1; i < board.length; i++, x++) {
            for (int j = 0, y = 1; j < board[i].length; j++, y++) {
                Cell cell = new Cell((i + 1) * size / 9, (j + 1) * size / 9, size / 9);
                board[i][j] = cell;
                draw(board[i][j]);
            }
        }
    }

    private void draw(Cell cell) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2d);
        gc.setGlobalAlpha(0.5d);

        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(cell.getX(), cell.getY(), cell.getSize(), cell.getSize());
        gc.strokeRect(cell.getX(), cell.getY(), cell.getSize(), cell.getSize());
    }

    public void drawCross(Cell cell) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(cell.getX(), cell.getY(), cell.getX() + cell.getSize(), cell.getY() + cell.getSize());

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(cell.getX() + cell.getSize(), cell.getY(), cell.getX(), cell.getY() + cell.getSize());
    }

    public void drawCircle(Cell cell) {
        gc.setStroke(Color.BLACK);
        gc.strokeOval(cell.getX(), cell.getY(), cell.getSize(), cell.getSize());
    }


}
