package tetris.view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import tetris.model.Block;
import tetris.model.Block.BlockShape;

public class GameBoard extends JPanel {

    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;
    private final int SPEED = 300;

    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isPaused = false;
    private int numLinesRemoved = 0;
    private int currentX = 0;
    private int currentY = 0;
    private JLabel statusbar;
    private Block curPiece;
    private BlockShape[] board;

    public GameBoard() {
        initBoard();
    }

    private void initBoard() {
        setFocusable(true);
        addKeyListener(new TAdapter());
    }

    private int squareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    private int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    private BlockShape shapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    void start() {
        curPiece = new Block();
        board = new BlockShape[BOARD_WIDTH * BOARD_HEIGHT];

        clearBoard();
        newPiece();

        timer = new Timer(SPEED, new GameCycle());
        timer.start();
    }

    private void pause() {

        isPaused = !isPaused;

        if (isPaused) {

            statusbar.setText("paused");
        } else {

            statusbar.setText(String.valueOf(numLinesRemoved));
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        var size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for (int i = 0; i < BOARD_HEIGHT; i++) {

            for (int j = 0; j < BOARD_WIDTH; j++) {
                BlockShape shape = shapeAt(j, BOARD_HEIGHT - i - 1);

                if (shape != BlockShape.NoShape) {
                    drawSquare(g, j * squareWidth(),
                        boardTop + i * squareHeight(), shape);
                }
            }
        }

        if (curPiece.getShape() != BlockShape.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = currentX + curPiece.x(i);
                int y = currentY - curPiece.y(i);
                drawSquare(g, x * squareWidth(),
                    boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                    curPiece.getShape());
            }
        }
    }

    private void dropDown() {
        int newY = currentY;
        while (newY > 0) {
            if (!tryMove(curPiece, currentX, newY - 1)) {
                break;
            }
            newY--;
        }
        pieceDropped();
    }

    private void oneLineDown() {
        if (!tryMove(curPiece, currentX, currentY - 1)) {
            pieceDropped();
        }
    }

    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = BlockShape.NoShape;
        }
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = currentX + curPiece.x(i);
            int y = currentY - curPiece.y(i);
            board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
        }
        removeFullLines();
        if (!isFallingFinished) {
            newPiece();
        }
    }

    private void newPiece() {
        curPiece.setRandomShape();
        currentX = BOARD_WIDTH / 2 + 1;
        currentY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, currentX, currentY)) {
            curPiece.setShape(BlockShape.NoShape);
            timer.stop();

            var msg = String.format("Game over. Score: %d", numLinesRemoved);
            statusbar.setText(msg);
        }
    }

    private boolean tryMove(Block newPiece, int newX, int newY) {

        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }

            if (shapeAt(x, y) != BlockShape.NoShape) {
                return false;
            }
        }

        curPiece = newPiece;
        currentX = newX;
        currentY = newY;

        repaint();

        return true;
    }

    private void removeFullLines() {
        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {

                if (shapeAt(j, i) == BlockShape.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                numFullLines++;
                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;

            statusbar.setText(String.valueOf(numLinesRemoved));
            isFallingFinished = true;
            curPiece.setShape(BlockShape.NoShape);
        }
    }


    private void drawSquare(Graphics g, int x, int y, BlockShape shape) {
        Color colors[] = {new Color(0, 0, 0), new Color(204, 102, 102),
            new Color(102, 204, 102), new Color(102, 102, 204),
            new Color(204, 204, 102), new Color(204, 102, 204),
            new Color(102, 204, 204), new Color(218, 170, 0)
        };

        var color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
            x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
            x + squareWidth() - 1, y + 1);
    }

    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private void doGameCycle() {
        update();
        repaint();
    }

    private void update() {
        if (isPaused) {
            return;
        }
        if (isFallingFinished) {

            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }

    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            if (curPiece.getShape() == BlockShape.NoShape) {
                return;
            }

            int keycode = e.getKeyCode();

            switch (keycode) {
                case KeyEvent.VK_P:
                    pause();
                    break;
                case KeyEvent.VK_LEFT:
                    tryMove(curPiece, currentX - 1, currentY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(curPiece, currentX + 1, currentY);
                    break;
                case KeyEvent.VK_DOWN:
                    oneLineDown();
                    break;
                case KeyEvent.VK_UP:
                    tryMove(curPiece.rotateLeft(), currentX, currentY);
                    break;
                case KeyEvent.VK_SPACE:
                    dropDown();
                    break;
            }
        }
    }
}