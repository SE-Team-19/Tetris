package tetris.view;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import tetris.model.Block;
import tetris.model.Block.BlockShape;


public class GameView extends JPanel {

    public static int GAME_HEIGHT = 20;
    public static int GAME_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';
    private final int SPEED = 300;

    private int [][] gameSize;      // tetrisGamePane 의 'X' size를 결정하기 위한 변수
    private int [][] nextBoard;

    private JTextPane tetrisGamePane;
    private JTextPane nextBlockPane;
    private JButton returnButton;
    private JPanel gameInfoPane;
    private JLabel score;

    private Block currentBlock;
    private BlockShape[] board;
    private Timer timer;
    private boolean isPaused = false;
    private boolean isFallingFinished = false;
    private JLabel statusBar;
    private int countLinesRemoved = 0;
    private int currentX = 0;
    private int currentY = 0;


    private SimpleAttributeSet styleSetGameBoard;

    public JButton getReturnButton() {
        return this.returnButton;
    }

    public void setReturnButton(JButton returnButton) {
        this.returnButton = returnButton;
    }

    /* singleton Instance (LazyHolder) */
    private GameView() {
        initGameView();
    }

    private static class LazyHolder {
        private static final GameView INSTANCE = new GameView();
    }

    public static GameView getInstance() {
        return LazyHolder.INSTANCE;
    }

    /***************************************/
    private void initGameView() {
        super.setLayout(new GridLayout(0, 2, 0, 0));

        tetrisGamePane = new JTextPane();
        tetrisGamePane.setEditable(false);
        tetrisGamePane.setBackground(Color.BLACK);
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
            BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        tetrisGamePane.setBorder(border);

        setStyleSetGameBoard();

        nextBlockPane = new JTextPane();
        nextBlockPane.setEditable(false);
        nextBlockPane.setBackground(Color.BLACK);
        nextBlockPane.setBorder(border);

        gameInfoPane = new JPanel();
        score = new JLabel("점수표기");
        returnButton = new JButton("Return");

        gameInfoPane.setLayout(new GridLayout(3, 0, 0, 0));
        gameInfoPane.add(nextBlockPane);
        gameInfoPane.add(this.score);
        gameInfoPane.add(returnButton);

        super.add(tetrisGamePane);
        super.add(gameInfoPane);

        gameSize = new int[GAME_HEIGHT][GAME_WIDTH];    // 세로 x 가로: 순서 주의하기

        drawGameBoard();

        start();
        //setFocusable(true);     // component 를 먼저 입력받을 수 있다.
        addKeyListener(new TAdapter());
    }

    public JTextPane getTetrisGamePane() {
        return this.tetrisGamePane;
    }

    public void setTetrisGamePane(JTextPane tetrisGamePane) {
        this.tetrisGamePane = tetrisGamePane;
    }

    public void setStyleSetGameBoard() {
        styleSetGameBoard = new SimpleAttributeSet();
        StyleConstants.setFontSize(styleSetGameBoard, 20);
        StyleConstants.setFontFamily(styleSetGameBoard, "Courier New");
        StyleConstants.setBold(styleSetGameBoard, true);
        StyleConstants.setForeground(styleSetGameBoard, Color.WHITE);
        StyleConstants.setAlignment(styleSetGameBoard, StyleConstants.ALIGN_CENTER);
        //StyleConstants.setAlignment(styleSetGameBoard, StyleConstants.ALIGN_JUSTIFIED);
    }

    public void drawGameBoard() {
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < GAME_WIDTH + 2; t++) {
            sb.append(BORDER_CHAR);
        }
        sb.append("\n");
        for (int i = 0; i < gameSize.length; i++) {
            sb.append(BORDER_CHAR);
            for (int j = 0; j < gameSize[i].length; j++) {
                if (gameSize[i][j] == 1) {
                    sb.append(BORDER_CHAR);
                } else {
                    sb.append(" ");
                }
            }
            sb.append(BORDER_CHAR);
            sb.append("\n");
        }
        for (int t = 0; t < GAME_WIDTH + 2; t++) {
            sb.append(BORDER_CHAR);
        }
        tetrisGamePane.setText(sb.toString());
        StyledDocument doc = tetrisGamePane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), styleSetGameBoard, false);

        StyledDocument boardDoc = tetrisGamePane.getStyledDocument();
    }

    private int squareWidth() {
        return (int) getSize().getWidth() / GAME_WIDTH;
    }

    private int squareHeight() {
        return (int) getSize().getHeight() / GAME_HEIGHT;
    }

    private BlockShape shapeAt(int x, int y) {
        return board[(y * GAME_WIDTH) + x];
    }

    public void start() {
        currentBlock = new Block();
        board = new BlockShape[GAME_WIDTH * GAME_HEIGHT];

        clearBoard();
        newPiece();

        timer = new Timer(SPEED, new GameCycle());
        timer.start();
    }

    public void pause() {
        isPaused = !isPaused;
        if (isPaused) {
            statusBar.setText("paused");
        } else {
            statusBar.setText(String.valueOf(countLinesRemoved));
        }
        repaint();
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    // Component들을 직접 그려준다.
    private void doDrawing(Graphics g) {
        var size = getSize();
        int boardTop = (int) size.getHeight() - GAME_HEIGHT * squareHeight();

        for (int i = 0; i < GAME_HEIGHT; i++) {

            for (int j = 0; j < GAME_WIDTH; j++) {
                BlockShape shape = shapeAt(j, GAME_HEIGHT - i - 1);
                if (shape != BlockShape.NoShape) {
                    drawSquare(g, j * squareWidth(),
                        boardTop + i * squareHeight(), shape);
                }
            }
        }
        if (currentBlock.getShape() != BlockShape.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = currentX + currentBlock.x(i);
                int y = currentY - currentBlock.y(i);

                drawSquare(g, x * squareWidth(),
                    boardTop + (GAME_HEIGHT - y - 1) * squareHeight(),
                    currentBlock.getShape());
            }
        }
    }

    private void dropDown() {
        int newY = currentY;
        while (newY > 0) {
            if (!tryMove(currentBlock, currentX, newY - 1)) {
                break;
            }
            newY--;
        }
        pieceDropped();
    }

    private void oneLineDown() {
        if (!tryMove(currentBlock, currentX, currentY - 1)) {
            pieceDropped();
        }
    }

    public void clearBoard() {
        for (int i = 0; i < GAME_HEIGHT * GAME_WIDTH; i++) {
            board[i] = BlockShape.NoShape;
        }
    }

    public void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = currentX + currentBlock.x(i);
            int y = currentY - currentBlock.y(i);
            board[(y * GAME_WIDTH) + x] = currentBlock.getShape();
        }
        removeFullLines();
        if (!isFallingFinished) {
            newPiece();
        }
    }

    public void newPiece() {
        currentBlock.setRandomShape();
        currentX = GAME_WIDTH / 2 + 1;
        currentY = GAME_HEIGHT - 1 + currentBlock.minY();

        if (!tryMove(currentBlock, currentX, currentY)) {
            currentBlock.setShape(BlockShape.NoShape);
            timer.stop();
            var msg = String.format("Game over. Your Score is: %d", countLinesRemoved);
            statusBar.setText(msg);
        }
    }

    public boolean tryMove(Block newPiece, int newX, int newY) {

        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            // 양 옆에 닿는 것 체크
            if (x < 0 || x >= GAME_WIDTH || y < 0 || y >= GAME_HEIGHT) {
                return false;
            }

            if (shapeAt(x, y) != BlockShape.NoShape) {
                return false;
            }
        }

        currentBlock = newPiece;
        currentX = newX;
        currentY = newY;

        repaint();

        return true;
    }

    private void removeFullLines() {
        int numFullLines = 0;
        for (int i = GAME_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;

            for (int j = 0; j < GAME_WIDTH; j++) {

                if (shapeAt(j, i) == BlockShape.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                numFullLines++;
                for (int k = i; k < GAME_HEIGHT - 1; k++) {
                    for (int j = 0; j < GAME_WIDTH; j++) {
                        board[(k * GAME_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0) {
            countLinesRemoved += numFullLines;
            statusBar.setText(String.valueOf(countLinesRemoved));
            isFallingFinished = true;
            currentBlock.setShape(BlockShape.NoShape);
        }
    }


    private void drawSquare(Graphics g, int x, int y, BlockShape shape) {

        Color colors[] = {Color.white, Color.BLUE, Color.CYAN, Color.darkGray, Color.GRAY,
            Color.YELLOW, Color.magenta, Color.YELLOW};

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

        }
        else {
            oneLineDown();
        }
    }

    class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            if (currentBlock.getShape() == BlockShape.NoShape) {
                return;
            }
            int keycode = e.getKeyCode();

            switch (keycode) {
                case KeyEvent.VK_P:
                    pause();
                    break;
                case KeyEvent.VK_LEFT:
                    tryMove(currentBlock, currentX - 1, currentY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(currentBlock, currentX + 1, currentY);
                    break;
                case KeyEvent.VK_DOWN:
                    oneLineDown();
                    break;
                case KeyEvent.VK_UP:
                    tryMove(currentBlock.rotateLeft(), currentX, currentY);
                    break;
                case KeyEvent.VK_SPACE:
                    dropDown();
                    break;
            }
        }
    }
}
