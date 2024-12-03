package ru.utin.game.domain;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import ru.utin.game.packets.Packet7MakeMotion;
import ru.utin.game.packets.Packet9GameOver;

@Getter
@Setter
public class Game {
    private boolean isStarted = false;
    private boolean canMove = false;
    private  Board board;
    private int[][] boardMatrix;
    private String lobbyUUID;
    private Channel channel;

    public Game() {
        boardMatrix = new int[3][3];
        board = new Board(600);
        board.getCanvas().setOnMouseClicked(event -> {
            if (isStarted && isCanMove()) {
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();
                clickOnCanvas(mouseX, mouseY, board.getBoard());
            }
        });
    }
    public void clear() {
        boardMatrix = new int[3][3];
        isStarted = false;
        canMove = false;
        lobbyUUID = null;
        board.clear();

    }

    public void makeMotion(Packet7MakeMotion packet) {
        canMove = true;
        if (packet.getBoard().getTypeCell() == TypeCell.CROSS) {
            getBoard().drawCross(board.getBoard()[packet.getBoard().getX()][packet.getBoard().getY()]);
        } else {
            getBoard().drawCircle(board.getBoard()[packet.getBoard().getX()][packet.getBoard().getY()]);
        }
        addElementBoardMatrix(packet.getBoard().getX(), packet.getBoard().getY(), packet.getBoard().getTypeCell());
        analysisBoardMatrix();
    }

    private void clickOnCanvas(double x, double y, Cell[][] board) {
        int size = board[0][0].getSize();
        looper:
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                double provCellX = board[i][j].getX() + size;
                double provCellY = board[i][j].getY() + size;
                if (x > board[i][j].getX() && x < provCellX && y < provCellY && y > board[i][j].getY()) {
                    if (getBoard().getTypeCell() == TypeCell.CROSS) {
                        getBoard().drawCross(board[i][j]);
                    } else if (getBoard().getTypeCell() == TypeCell.CIRCLE) {
                        getBoard().drawCircle(board[i][j]);

                    }
                    addElementBoardMatrix(i, j, getBoard().getTypeCell());
                    canMove = false;
                    channel.writeAndFlush(new Packet7MakeMotion(new CellPosition(i, j, getBoard().getTypeCell())));
                    analysisBoardMatrix();
                    break looper;
                }
            }
        }
    }

    private void addElementBoardMatrix(int i, int j, TypeCell typeCell) {
        int value = 0;
        if (typeCell == TypeCell.CROSS) {
            value = 1;
        } else if (typeCell == TypeCell.CIRCLE) {
            value = 2;
        }
        boardMatrix[i][j] = value;
    }

    private void analysisBoardMatrix() {
        int result;
        for (int i = 0; i < boardMatrix.length; i++) {
            int sumHorisontal = 0;
            int sumVertical = 0;
            for (int j = 0; j < boardMatrix[i].length; j++) {
                if (boardMatrix[i][j] == 0) {
                    sumHorisontal = 100;
                }
                if (boardMatrix[j][i] == 0) {
                    sumVertical = 100;
                }
                sumHorisontal += boardMatrix[i][j];
                sumVertical += boardMatrix[j][i];
            }
            if (sumHorisontal == 3 || sumHorisontal == 6 || sumVertical == 3 || sumVertical == 6) {
                if (sumHorisontal == 3 || sumVertical == 3) {
                    result = 3;
                } else {
                    result = 6;
                }
                resultBoardMatrix(result);
                break;
            }
        }
        int sumLeft = 0;
        int sumRight = 0;
        for (int i = 0, vertLeft = -1, vertRight = 3; i < 3; i++, vertLeft++, vertRight--) {
            if (boardMatrix[vertLeft + 1][vertLeft + 1] == 0) {
                sumLeft = 100;
            }
            if (boardMatrix[vertRight - 1][vertRight - 1] == 0) {
                sumRight = 100;
            }
            sumLeft += boardMatrix[vertLeft + 1][vertLeft + 1];
            sumRight += boardMatrix[vertRight - 1][vertRight - 1];
        }
        if (sumLeft == 3 || sumRight == 3 || sumLeft == 6 || sumRight == 6) {
            if (sumLeft == 3 || sumRight == 3) {
                result = 3;
            } else {
                result = 6;
            }
            resultBoardMatrix(result);
        }
    }

    private void resultBoardMatrix(int result) {
        if (result == 3) {
            channel.writeAndFlush(new Packet9GameOver(TypeCell.CROSS));
        } else {
            channel.writeAndFlush(new Packet9GameOver(TypeCell.CIRCLE));
        }
    }
}