package tetris.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.text.*;

import tetris.model.*;
import tetris.view.GameView;

public class GameController {

    private static final int INTERVAL = 1000;
    private static final int NEXT_BOARD_HEIGHT = 5;
    private static final int NEXT_BOARD_WIDTH = 4;

    private Timer timer;
    private int mode;
    private Block currentBlock;
    private Block nextBlock;

    private int[][] board; // gamePane 의 'X' size를 결정하기 위한 변수
    private int[][] nextBoard;
    private int[][] accumulatedBoard;     // 충돌되는 부분 설정

    private int x = 3;
    private int y = 0;
    private int nextBlockX = 0; // warning: 시작 위치 조절
    private int nextBlockY = 1;
    private int score = 0;      // game 점수와 관련된 변수
    private int lineCount = 0;  // 제거된 라인의 개수를 담는 변수. 이를 통해 score를 주는 방식을 지정할 것임

    private GameView gameView = GameView.getInstance();
    private JTextPane gamePane;
    private JTextPane nextTetrisBlockPane;
    private SimpleAttributeSet boardAttributeSet;
    private SimpleAttributeSet nextBoardAttributeSet;

    public GameController() {
        KeyListener playerKeyListener = new PlayerKeyListener();
        gameView.addKeyListener(playerKeyListener);

        board = new int[GameView.BORDER_HEIGHT][GameView.BORDER_WIDTH];
        nextBoard = new int[NEXT_BOARD_HEIGHT][NEXT_BOARD_WIDTH];
        accumulatedBoard = new int[GameView.BORDER_HEIGHT][GameView.BORDER_WIDTH];

        currentBlock = getRandomBlock(mode);
        nextBlock = getRandomBlock(mode);

        // gamePane 위치 조정
        gamePane = gameView.getGamePane();

        setBoardAttributeSet();
        setNextBoardAttributeSet();

        drawGameBoard();
        drawNextBlock();

        placeCurrentBlock();
        //placeAccumulatedBlock();      // collision add
        placeNextBlock();

        startTime();
    }

    public void startTime() {
        timer = new Timer(INTERVAL, e -> {
            moveDown();
            drawGameBoard();
        });

        timer.start();
    }

    public void setBoardAttributeSet() {
        boardAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(boardAttributeSet, 20);
        StyleConstants.setFontFamily(boardAttributeSet, "Courier New");
        StyleConstants.setBold(boardAttributeSet, true);
        StyleConstants.setForeground(boardAttributeSet, Color.WHITE);
        StyleConstants.setAlignment(boardAttributeSet, StyleConstants.ALIGN_CENTER);
    }

    public void setNextBoardAttributeSet() {
        nextBoardAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(nextBoardAttributeSet, 15);
        StyleConstants.setFontFamily(nextBoardAttributeSet, "Courier New");
        StyleConstants.setBold(nextBoardAttributeSet, true);
        StyleConstants.setForeground(nextBoardAttributeSet, Color.WHITE);
        StyleConstants.setAlignment(nextBoardAttributeSet, StyleConstants.ALIGN_CENTER);
    }

    /*
    private void selectMode() {
        String [] gameMode = {"Easy", "Normal", "Hard"};

        String strGameMode = (String)JOptionPane.showInputDialog(null, "Select",
            "Difficulty", JOptionPane.WARNING_MESSAGE, null, gameMode, gameMode[0]);

        if (strGameMode.equals(gameMode[0])) {
            mode = 0;
        }
        else if (strGameMode.equals(gameMode[1])) {
            mode = 1;
        }
        else {
            mode = 2;
        }
    } */

    // 블록이 7개
    // 확률이 1/7인데, 20% 더 등장이면,
    // 10, 10, 10, 10, 10, 10, 12 -> 5, 5, 5, 5, 5, 5, 6
    private Block getRandomBlock(int mode) {
        // normal mode
        if (mode == 0) {
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
                default:
                    return new IBlock();
            }
        }

        // easy mode
        if (mode == 1) {
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
                default:
                    return new OBlock();
            }
        }

        // hard mode
        if (mode == 2) {
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
                default:
                    return new OBlock();
            }
        }
        return new IBlock();
    }

    public void drawGameBoard() {
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < GameView.BORDER_WIDTH + 2; t++) {
            sb.append(GameView.BORDER_CHAR);
        }
        sb.append("\n");
        for (int i = 0; i < board.length; i++) {
            sb.append(GameView.BORDER_CHAR);
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    sb.append(GameView.BLOCK_CHAR); // currentBlock 의 모양을 그려준다.
                }
                else {
                    sb.append(" ");
                }
            }
            sb.append(GameView.BORDER_CHAR);
            sb.append("\n");
        }
        for (int t = 0; t < GameView.BORDER_WIDTH + 2; t++) {
            sb.append(GameView.BORDER_CHAR);
        }
        gamePane.setText(sb.toString());

        StyledDocument doc = gamePane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), boardAttributeSet, false);
        paintBlock(currentBlock.getColor());

        // collision 부분 추가
        /*
        for (int i = 0; i < accumulatedBoard.length; i++) {
            for (int j = 0; j < accumulatedBoard[i].length; j++) {
                if (accumulatedBoard[i][j] == 2) {
                    sb.append(GameView.BORDER_WIDTH);
                }
                else {
                    sb.append(" ");
                }
            }
        }*/
    }

    public void drawNextBlock() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nextBoard.length; i++) {
            for (int j = 0; j < nextBoard[i].length; j++) {
                if (nextBoard[i][j] == 1) {
                    sb.append(GameView.BLOCK_CHAR); // nextBlock 의 모양을 그려준다.
                }
                else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        JTextPane nextBlockPane = gameView.getNextBlockPane();
        nextBlockPane.setText(sb.toString());

        StyledDocument doc = nextBlockPane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), nextBoardAttributeSet, false);
        paintBlock(nextBlock.getColor());
    }

    private void paintBlock(Color color) {
        StyledDocument doc = gamePane.getStyledDocument();
        SimpleAttributeSet blockAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(blockAttributeSet, color);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    doc.setCharacterAttributes(
                            (board[i].length + 4) + i * (board[i].length + 3) + j, 1,
                            blockAttributeSet, false);
                }
            }
        }
    }

    private void paintNextBlock(Color color) {
        // nextBlock 부분에 중복해서 그려지는 것을 방지
        int [][] board1 = nextBoard;
        for (int i = 0; i < nextBoard.length; i++) {
            for (int j = 0; j < nextBoard[i].length; j++) {
                nextBoard[i][j] = 0;
            }
        }
        nextBoard = board1;

        StyledDocument doc = nextTetrisBlockPane.getStyledDocument();
        SimpleAttributeSet nextBlockAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(nextBlockAttributeSet, color);
        for (int i = 0; i < nextBoard.length; i++) {
            for (int j = 0; j < nextBoard[i].length; j++) {
                if (nextBoard[i][j] == 1) {
                    doc.setCharacterAttributes((nextBoard[i].length * i) + j, 1,
                            nextBlockAttributeSet, false);
                }
            }
        }
    }

    private void placeCurrentBlock() {
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

    private void placeAccumulatedBlock() {
        for(int j = 0; j < currentBlock.getHeight(); j++) {
            for(int i = 0; i < currentBlock.getWidth(); i++) {
                if (currentBlock.getShape(i, j) == 1) {
                    accumulatedBoard[y + j][x + i] =  currentBlock.getShape(i, j);  // setting board 2
                }
                else {
                    accumulatedBoard[y + j][x + i] = 0;
                }
            }
        }
    }

    private void eraseCurrentBlock() {
        for (int i = x; i < x + currentBlock.getWidth(); i++) {
            for (int j = y; j < y + currentBlock.getHeight(); j++) {
                board[j][i] = 0;
            }
        }
    }

    private void eraseNextBlock() {
        for (int i = nextBlockX; i < nextBlockX + nextBlock.getWidth(); i++) {
            for (int j = nextBlockY; j < nextBlockY + nextBlock.getHeight(); j++) {
                nextBoard[j][i] = 0;
            }
        }
    }

    protected void moveDown() {
        eraseCurrentBlock();

        if (currentBlock == null) {
            return;
        }

        if (!checkCollision()) {
            saveBlock();
            currentBlock = nextBlock;
            nextBlock = getRandomBlock(mode);
            eraseNextBlock();
            placeNextBlock();
            drawNextBlock();
            x = 3;
            y = 0;

        }

        if (y < GameView.BORDER_HEIGHT - currentBlock.getHeight()) {
            y++;
        }
        else {
            placeCurrentBlock();
            currentBlock = nextBlock;
            nextBlock = getRandomBlock(mode);
            eraseNextBlock();
            placeNextBlock();
            drawNextBlock();
            x = 3;
            y = 0;
        }
        placeCurrentBlock();
    }

    public void moveRight() {
        eraseCurrentBlock();
        if (currentBlock == null) {
            return;
        }
        if (x < GameView.BORDER_WIDTH - currentBlock.getWidth()) {
            x++;
        }
        placeCurrentBlock();
    }

    public void moveLeft() {
        eraseCurrentBlock();
        if (currentBlock == null) {
            return;
        }
        if (x > 0) {
            x--;
        }
        placeCurrentBlock();
    }

    public void moveRotate() {
        eraseCurrentBlock();
        currentBlock.rotate();
        placeCurrentBlock();
    }


    // 한 번에 블록이 떨어지는 메소드 구현(SPACE BAR)
    private void dropDown() {
        eraseCurrentBlock();
        if (currentBlock == null) {
            return;
        }
        while (checkBottom() && checkCollision()) {
            moveDown();
        }
        placeCurrentBlock();
    }

    private boolean checkBottom() {
        if (y == GameView.BORDER_HEIGHT - currentBlock.getHeight()) {
            return false;
        }
        return true;
    }

    private boolean checkCollision() {
        for (int i = 0; i < currentBlock.getHeight(); i++) {
            for (int j = 0; j < currentBlock.getWidth(); j++) {
                if (currentBlock.getShape(j, i) == 1 && i + y < 19) {
                    if (accumulatedBoard[i][j] == 2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Block이 바닥에 쌓인다.
    private void saveBlock() {
        for (int i = 0; i < currentBlock.getHeight(); i++) {
            for (int j = 0; j < currentBlock.getWidth(); j++) {
                if (currentBlock.getShape(j, i) == 1) {
                    accumulatedBoard[y + i][j + x] = 2;
                }
            }
        }
    }

    // 블럭이 한 줄 쌓였는지 Check
    private boolean oneLineCheck() {
        int lineCount = 0;

        for (int i = 0; i < GameView.BORDER_WIDTH; i++) {
            for (int j = 0; j < GameView.BORDER_WIDTH; j++) {
                if (accumulatedBoard[i][j] == 2) {
                    lineCount += 1;
                }
            }
        }

        if (lineCount == GameView.BORDER_WIDTH) {
            return true;
        }

        return false;
    }




    // 게임 중단 상태에서 다시 실행하는 경우
    public void restart() {
        board = new int[GameView.BORDER_HEIGHT][GameView.BORDER_WIDTH];
        nextBoard = new int[NEXT_BOARD_HEIGHT][NEXT_BOARD_WIDTH];
        x = 3;
        y = 0;
        currentBlock = getRandomBlock(mode);
        nextBlock = getRandomBlock(mode);

        placeCurrentBlock();
        drawGameBoard();
        placeNextBlock();
        drawNextBlock();
    }

    // ESC 키를 누를 경우 게임 메세지를 출력
    public void showESCMessage() {
        int inputValue = JOptionPane.showConfirmDialog(gameView, "Do you want to end the game?",
                "Option", JOptionPane.YES_NO_OPTION);

        if (inputValue == JOptionPane.YES_OPTION) {
            // 이 부분을 게임이 종료되는 것으로 할지, 혹은 메인 화면으로 돌아가게 할지 정할 필요가 있음
            System.exit(0);
        }
        else if (inputValue == -1) {
            // 팝업을 종료하는 경우(X키 누르는 경우, 게임을 처음부터 재시작)
            timer.restart();
            restart();
        }
        // 그 외에는 중단된 상태에서 재시작
    }

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
                    gameView.repaint();
                    showESCMessage();
                    timer.start();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {}

    }


}
