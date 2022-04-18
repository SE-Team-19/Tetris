package tetris.view;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

import javax.swing.border.CompoundBorder;
import javax.swing.text.*;

import tetris.model.*;

public class GameView extends JPanel {

    private static final int GAME_HEIGHT = 20;
    private static final int GAME_WIDTH = 10;
    private static final int NEXT_BOARD_HEIGHT = 5;
    private static final int NEXT_BOARD_WIDTH = 4;
    private static final char BORDER_CHAR = 'X';
    private static final char BLOCK_CHAR = 'O';
    private static final int INTERVAL = 1000;

    private Timer timer;

    private int[][] board; // tetrisGamePane 의 'X' size를 결정하기 위한 변수
    private int[][] nextBoard;

    private JTextPane tetrisGamePane;
    private JTextPane nextTetrisBlockPane;
    private JButton returnButton;
    private SimpleAttributeSet styleSetGameBoard;
    private SimpleAttributeSet styleSetNextBlockBoard;
    private SimpleAttributeSet styleSetBlockChar;
    private SimpleAttributeSet styleSetNextBlockChar;
    private KeyListener playerKeyListener;

    private Block currentBlock;
    private Block nextBlock;

    private int x = 3;
    private int y = 0;

    private int nextBlockX = 0; // warning: 시작 위치 조절
    private int nextBlockY = 1;

    private GameView() {
        initView();
        timer = new Timer(INTERVAL, e -> {
            moveDown();
            drawGameBoard();
        });

        board = new int[GAME_HEIGHT][GAME_WIDTH]; // 세로 x 가로: 순서 주의하기
        nextBoard = new int[5][4];

        playerKeyListener = new PlayerKeyListener();
        addKeyListener(playerKeyListener);
        setFocusable(true);

        currentBlock = getRandomBlock_NormalMode();
        nextBlock = getRandomBlock_NormalMode();

        setStyleGameBoard();
        setStyleNextBlockBoard();

        placeBlock();
        drawGameBoard();

        placeNextBlock();
        drawNextBlockBoard();

        timer.start();
    }

    private static class LazyHolder {
        private static final GameView INSTANCE = new GameView();
    }

    public static GameView getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void initView() {
        super.setLayout(new GridLayout(0, 2, 0, 0));

        tetrisGamePane = new JTextPane();
        tetrisGamePane.setBackground(Color.BLACK);
        CompoundBorder border =
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        tetrisGamePane.setBorder(border);


        nextTetrisBlockPane = new JTextPane();
        nextTetrisBlockPane.setEditable(false);
        nextTetrisBlockPane.setBackground(Color.BLACK);
        nextTetrisBlockPane.setBorder(border);

        JPanel gameInfoPane = new JPanel();
        returnButton = new JButton("Return");

        gameInfoPane.setLayout(new GridLayout(3, 0, 0, 0));
        gameInfoPane.add(nextTetrisBlockPane);
        gameInfoPane.add(new JLabel("점수표기"));
        gameInfoPane.add(returnButton);

        super.add(tetrisGamePane);
        super.add(gameInfoPane);
    }

    // 블록이 7개
    // 확률이 1/7인데, 20% 더 등장이면,
    // 10, 10, 10, 10, 10, 10, 12 -> 5, 5, 5, 5, 5, 5, 6
    private Block getRandomBlock(int n) {
        // normal mode
        if (n == 0) {
            Random random = new Random(System.currentTimeMillis());
            int block = random.nextInt(7);
            switch (block) {
                case 0:
                    return new IBlock();
                case 1:
                    return new JBlock();
                case 2:
                    return new LBlock();
                case 3:
                    return new ZBlock();
                case 4:
                    return new SBlock();
                case 5:
                    return new TBlock();
                case 6:
                    return new OBlock();
            }
        }

        // easy mode
        if (n == 1) {
            Random random = new Random(System.currentTimeMillis());
            int block = random.nextInt(73);
            switch (block / 10) {
                case 0:
                    return new OBlock();
                case 1:
                    return new JBlock();
                case 2:
                    return new LBlock();
                case 3:
                    return new ZBlock();
                case 4:
                    return new SBlock();
                case 5:
                    return new TBlock();
                case 6:
                case 7:
                    return new IBlock();
            }
        }

        // hard mode
        if (n == 2) {
            Random random = new Random(System.currentTimeMillis());
            int block = random.nextInt(68);
            switch (block / 10) {
                case 0:
                    return new OBlock();
                case 1:
                    return new JBlock();
                case 2:
                    return new LBlock();
                case 3:
                    return new ZBlock();
                case 4:
                    return new SBlock();
                case 5:
                    return new TBlock();
                case 6:
                    return new IBlock();
            }
        }
        return new IBlock();
    }


    private Block getRandomBlock_NormalMode() {
        Random random = new Random(System.currentTimeMillis());
        int block = random.nextInt(7);
        switch (block) {
            case 0:
                return new IBlock();
            case 1:
                return new JBlock();
            case 2:
                return new LBlock();
            case 3:
                return new ZBlock();
            case 4:
                return new SBlock();
            case 5:
                return new TBlock();
            case 6:
                return new OBlock();
        }
        return new LBlock();
    }

    // Block 색칠 method -> 추가적 구현 필요
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        for (int i = 0; i < GAME_HEIGHT; i++) {
            for (int j = 0; j < GAME_WIDTH; j++) {

                if (currentBlock != null) {
                    drawSquare(g, j * currentBlock.getWidth(), i * currentBlock.getHeight());
                }
            }
        }
    }

    private void drawSquare(Graphics g, int x, int y) {
        // I, J, L, Z, S, T, O
        Color[] colors = {Color.RED, Color.ORANGE, Color.BLUE, Color.GREEN, Color.MAGENTA,
                Color.CYAN, Color.YELLOW};

        int index = 0;

        if (new IBlock().equals(currentBlock)) {
            index = 0;
        }
        else if (new JBlock().equals(currentBlock)) {
            index = 1;
        }
        else if (new LBlock().equals(currentBlock)) {
            index = 2;
        }
        else if (new ZBlock().equals(currentBlock)) {
            index = 3;
        }
        else if (new SBlock().equals(currentBlock)) {
            index = 4;
        }
        else if (new TBlock().equals(currentBlock)) {
            index = 5;
        }
        else if (new OBlock().equals(currentBlock)) {
            index = 6;
        }

        var color = colors[index];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, currentBlock.getWidth() - 2, currentBlock.getHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + currentBlock.getHeight() - 1, x, y);
        g.drawLine(x, y, x + currentBlock.getWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + currentBlock.getHeight() - 1, x + currentBlock.getWidth() - 1,
                y + currentBlock.getHeight() - 1);
        g.drawLine(x + currentBlock.getWidth() - 1, y + currentBlock.getHeight() - 1,
                x + currentBlock.getWidth() - 1, y + 1);
    }


    private void placeBlock() {
        for (int j = 0; j < currentBlock.getHeight(); j++) {
            for (int i = 0; i < currentBlock.getWidth(); i++) {
                board[y + j][x + i] = currentBlock.getShape(i, j);
                // getShape -> return j, i
            }
        }
    }

    private void placeNextBlock() {
        for (int j = 0; j < nextBlock.getHeight(); j++) {
            for (int i = 0; i < nextBlock.getWidth(); i++) {
                nextBoard[nextBlockY + j][nextBlockX + i] = nextBlock.getShape(i, j);
                // getShape -> return j, i
            }
        }
    }

    private void eraseCurr() {
        for (int i = x; i < x + currentBlock.getWidth(); i++) {
            for (int j = y; j < y + currentBlock.getHeight(); j++) {
                board[j][i] = 0;
            }
        }
    }

    private void eraseNext() {
        for (int i = nextBlockX; i < nextBlockX + nextBlock.getWidth(); i++) {
            for (int j = nextBlockY; j < nextBlockY + nextBlock.getHeight(); j++) {
                nextBoard[j][i] = 0;
            }
        }
    }

    protected void moveDown() {
        eraseCurr();
        if (y < GAME_HEIGHT - currentBlock.getHeight()) {
            y++;
        }
        else {
            placeBlock();
            currentBlock = nextBlock;
            nextBlock = getRandomBlock_NormalMode();
            eraseNext();
            placeNextBlock();
            drawNextBlockBoard();
            x = 3;
            y = 0;
        }
        placeBlock();
    }

    public void moveRight() {
        eraseCurr();
        if (currentBlock == null) {
            return;
        }
        if (x < GAME_WIDTH - currentBlock.getWidth()) {
            x++;
        }
        placeBlock();
    }

    public void moveLeft() {
        eraseCurr();
        if (currentBlock == null) {
            return;
        }
        if (x > 0) {
            x--;
        }
        placeBlock();
    }

    public void moveRotate() {
        eraseCurr();
        currentBlock.rotate();
        placeBlock();
    }


    // 한 번에 블록이 떨어지는 메소드 구현(SPACE BAR)
    public void dropDown() {
        eraseCurr();
        if (currentBlock == null) {
            return;
        }
        while (checkBottom() && checkCollision()) {
            moveDown();
        }
        placeBlock();
    }

    public boolean checkBottom() {
        if (y == GAME_HEIGHT - currentBlock.getHeight()) {
            return false;
        }
        return true;
    }

    public boolean checkCollision() {
        for (int i = 0; i < currentBlock.getHeight(); i++) {
            for (int j = 0; j < currentBlock.getWidth(); j++) {
                if (currentBlock.getShape(j, i) == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    // 게임 중단 상태에서 다시 실행하는 경우
    public void restart() {
        board = new int[GAME_HEIGHT][GAME_WIDTH];
        nextBoard = new int[NEXT_BOARD_HEIGHT][NEXT_BOARD_WIDTH];
        x = 3;
        y = 0;
        currentBlock = getRandomBlock_NormalMode();
        nextBlock = getRandomBlock_NormalMode();

        placeBlock();
        drawGameBoard();
        placeNextBlock();
        drawNextBlockBoard();
    }

    // ESC 키를 누를 경우 게임 메세지를 출력
    public void showEscMessage() {
        int inputValue = JOptionPane.showConfirmDialog(this, "Do you want to end the game?",
                "Option", JOptionPane.YES_NO_OPTION);

        if (inputValue == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
        else {
            timer.restart();
            restart();
        }
    }



    public void drawGameBoard() {
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < GAME_WIDTH + 2; t++) {
            sb.append(BORDER_CHAR);
        }
        sb.append("\n");
        for (int i = 0; i < board.length; i++) {
            sb.append(BORDER_CHAR);
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    sb.append(BLOCK_CHAR); // currentBlock 의 모양을 그려준다.
                }
                else {
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
        drawBlockColor(currentBlock.getColor());
    }

    public void drawNextBlockBoard() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nextBoard.length; i++) {
            for (int j = 0; j < nextBoard[i].length; j++) {
                if (nextBoard[i][j] == 1) {
                    sb.append(BLOCK_CHAR); // nextBlock 의 모양을 그려준다.
                }
                else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        nextTetrisBlockPane.setText(sb.toString());

        StyledDocument doc = nextTetrisBlockPane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), styleSetNextBlockBoard, false);
        drawNextBlockColor(nextBlock.getColor());
    }

    private void drawBlockColor(Color color) {
        StyledDocument doc = tetrisGamePane.getStyledDocument();
        setStyleBlockChar(color);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    doc.setCharacterAttributes(
                            (board[i].length + 4) + i * (board[i].length + 3) + j, 1,
                            styleSetBlockChar, false);
                }
            }
        }
    }

    private void drawNextBlockColor(Color color) {
        StyledDocument doc = nextTetrisBlockPane.getStyledDocument();
        setStyleNextBlockChar(color);
        for (int i = 0; i < nextBoard.length; i++) {
            for (int j = 0; j < nextBoard[i].length; j++) {
                if (nextBoard[i][j] == 1) {
                    doc.setCharacterAttributes((nextBoard[i].length * i) + j, 1,
                            styleSetNextBlockChar, false);
                }
            }
        }
    }

    // 키보드 입력을 받는다.
    public class PlayerKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    moveDown();
                    drawGameBoard();
                    break;
                case KeyEvent.VK_RIGHT:
                    moveRight();
                    drawGameBoard();
                    break;
                case KeyEvent.VK_LEFT:
                    moveLeft();
                    drawGameBoard();
                    break;
                case KeyEvent.VK_UP:
                    moveRotate();
                    drawGameBoard();
                    break;
                case KeyEvent.VK_SPACE:
                    dropDown();
                    drawGameBoard();
                    break;
                case KeyEvent.VK_ESCAPE:
                    timer.stop();
                    repaint();
                    showEscMessage();
                    timer.start();
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}

    }

    public JButton getReturnButton() {
        return this.returnButton;
    }

    public void setReturnButton(JButton returnButton) {
        this.returnButton = returnButton;
    }

    public void setStyleGameBoard() {
        styleSetGameBoard = new SimpleAttributeSet();
        StyleConstants.setFontSize(styleSetGameBoard, 20);
        StyleConstants.setFontFamily(styleSetGameBoard, "Courier New");
        StyleConstants.setBold(styleSetGameBoard, true);
        StyleConstants.setAlignment(styleSetGameBoard, StyleConstants.ALIGN_CENTER);
    }

    public void setStyleNextBlockBoard() {
        styleSetNextBlockBoard = new SimpleAttributeSet();
        StyleConstants.setFontSize(styleSetNextBlockBoard, 15);
        StyleConstants.setFontFamily(styleSetNextBlockBoard, "Courier New");
        StyleConstants.setBold(styleSetNextBlockBoard, true);
        StyleConstants.setAlignment(styleSetNextBlockBoard, StyleConstants.ALIGN_CENTER);
    }

    public void setStyleBlockChar(Color color) {
        styleSetBlockChar = new SimpleAttributeSet();
        StyleConstants.setForeground(styleSetBlockChar, color);
    }

    public void setStyleNextBlockChar(Color color) {
        styleSetNextBlockChar = new SimpleAttributeSet();
        StyleConstants.setForeground(styleSetNextBlockChar, color);
    }
}


