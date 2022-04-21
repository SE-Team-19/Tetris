package tetris.controller;

import static tetris.view.GameView.BORDER_HEIGHT;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.text.*;
import java.util.Arrays;

import tetris.model.*;
import tetris.view.GameView;

public class GameController {

    private static final int NEXT_BOARD_HEIGHT = 5;
    private static final int NEXT_BOARD_WIDTH = 4;

    private Timer timer;
    private int delay;
    private int mode;
    private Block currentBlock;
    private Block nextBlock;
    private Block blockBuffer;

    private int[][] board; // gamePane 의 'X' size를 결정하기 위한 변수
    private int[][] boardBuffer;
    private int[][] nextBoard;
    private int[][] accumulatedBoard; // 충돌되는 부분 설정

    private int x = 3;
    private int y = 0;
    private int nextBlockX = 0; // warning: 시작 위치 조절
    private int nextBlockY = 0;
    private int score; // game 점수와 관련된 변수
    private int lineCount; // 제거된 라인의 개수를 담는 변수. 이를 통해 score를 주는 방식을 지정할 것임

    private GameView gameView = GameView.getInstance();
    private JTextPane gamePane;
    private JTextPane nextTetrisBlockPane;
    private JLabel gameOverText; // 게임 종료를 나타내주는 문구
    private SimpleAttributeSet boardAttributeSet;
    private SimpleAttributeSet nextBoardAttributeSet;

    private Setting setting;
    private boolean isColorBlindMode;

    public GameController(Setting setting) {
        this.setting = setting;
        isColorBlindMode = setting.isColorBlindMode();
        KeyListener gameKeyListener = new GameKeyListener();
        gameView.addKeyListener(gameKeyListener);

        board = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        boardBuffer = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        nextBoard = new int[NEXT_BOARD_HEIGHT][NEXT_BOARD_WIDTH];
        accumulatedBoard = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        currentBlock = getRandomBlock(mode);
        blockBuffer = getRandomBlock(mode);
        nextBlock = getRandomBlock(mode);

        // gamePane 위치 조정
        gamePane = gameView.getGamePane();

        setBoardAttributeSet();
        setNextBoardAttributeSet();

        drawGameBoard();
        drawNextBlock();

        placeCurrentBlock();
        // placeAccumulatedBlock(); // collision add
        placeNextBlock();

        startGame();
    }

    public void startGame() {
        score = 0;
        delay = 1000;
        showScore();

        timer = new Timer(delay, e -> {
            moveDown();
            drawGameBoard();
            delay -= delay > 250 ? 5 : 0;
            timer.setDelay(delay);
            showCurrnent();
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
        StyleConstants.setLineSpacing(boardAttributeSet, -0.5f);
    }

    public void setNextBoardAttributeSet() {
        nextBoardAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(nextBoardAttributeSet, 20);
        StyleConstants.setFontFamily(nextBoardAttributeSet, "Courier New");
        StyleConstants.setBold(nextBoardAttributeSet, true);
        StyleConstants.setForeground(nextBoardAttributeSet, Color.WHITE);
        StyleConstants.setAlignment(nextBoardAttributeSet, StyleConstants.ALIGN_CENTER);
        StyleConstants.setLineSpacing(boardAttributeSet, -0.5f);
    }

    private class Pair<K, V> {

        K block;
        V weight;

        public Pair(K block, V weight) {
            this.block = block;
            this.weight = weight;
        }
    }

    public Block getRandomBlock(int mode) {
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
            List<Pair<? extends Block, Double>> candidates = Arrays.asList(
                    new Pair<>(new JBlock(), 5 / 36d),
                    new Pair<>(new LBlock(), 5 / 36d),
                    new Pair<>(new ZBlock(), 5 / 36d),
                    new Pair<>(new SBlock(), 5 / 36d),
                    new Pair<>(new TBlock(), 5 / 36d),
                    new Pair<>(new OBlock(), 5 / 36d),
                    new Pair<>(new IBlock(), 6 / 36d));

            double pivot = Math.random();
            double acc = 0;
            for (Pair<? extends Block, Double> pair : candidates) {
                acc += pair.weight;

                if (pivot <= acc) {
                    return pair.block;
                }
            }
        }

        // hard mode
        if (mode == 2) {
            List<Pair<? extends Block, Double>> candidates = Arrays.asList(
                    new Pair<>(new JBlock(), 6 / 41d),
                    new Pair<>(new LBlock(), 6 / 41d),
                    new Pair<>(new ZBlock(), 6 / 41d),
                    new Pair<>(new SBlock(), 6 / 41d),
                    new Pair<>(new TBlock(), 6 / 41d),
                    new Pair<>(new OBlock(), 6 / 41d),
                    new Pair<>(new IBlock(), 5 / 41d));

            double pivot = Math.random();
            double acc = 0;
            for (Pair<? extends Block, Double> pair : candidates) {
                acc += pair.weight;

                if (pivot <= acc) {
                    return pair.block;
                }
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
                if (board[i][j] > 0) {
                    sb.append(GameView.BLOCK_CHAR); // currentBlock 의 모양을 그려준다.
                } else {
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
        if (isColorBlindMode) {
            paintBlock(currentBlock.getBlindColor());
            return;
        }
        paintBlock(currentBlock.getColor());

        // collision 부분 추가
        /*
         * for (int i = 0; i < accumulatedBoard.length; i++) { for (int j = 0; j <
         * accumulatedBoard[i].length; j++) { if (accumulatedBoard[i][j] == 2) {
         * sb.append(GameView.BORDER_WIDTH); } else { sb.append(" "); } } }
         */
    }

    public void drawNextBlock() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nextBoard.length; i++) {
            for (int j = 0; j < nextBoard[i].length; j++) {
                if (nextBoard[i][j] == 1) {
                    sb.append(GameView.BLOCK_CHAR); // nextBlock 의 모양을 그려준다.
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        JTextPane nextBlockPane = gameView.getNextBlockPane();
        nextBlockPane.setText(sb.toString());

        StyledDocument doc = nextBlockPane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), nextBoardAttributeSet, false);
        if (isColorBlindMode) {
            paintBlock(nextBlock.getBlindColor());
            return;
        }
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
                board[y + j][x + i] += currentBlock.getShape(i, j);
                // getShape -> return j, i
            }
        }
    }

    private void placeBufferBlock() {
        for (int j = 0; j < blockBuffer.getHeight(); j++) {
            for (int i = 0; i < blockBuffer.getWidth(); i++) {
                boardBuffer[y + j][x + i] += blockBuffer.getShape(i, j);
                // getShape -> return j, i
            }
        }
    }

    private void placeNextBlock() {
        initZeroBoard(nextBoard);
        for (int j = 0; j < nextBlock.getHeight(); j++) {
            for (int i = 0; i < nextBlock.getWidth(); i++) {
                nextBoard[nextBlockY + j][nextBlockX + i] = nextBlock.getShape(i, j);
                // getShape -> return j, i
            }
        }
    }

    private void eraseCurrentBlock() {
        for (int i = x; i < x + currentBlock.getWidth(); i++) {
            for (int j = y; j < y + currentBlock.getHeight(); j++) {
                if (board[j][i] == 1)
                    board[j][i] = 0;
            }
        }
    }

    private void eraseNextBlock() {
        initZeroBoard(nextBoard);
        for (int i = nextBlockX; i < nextBlockX + nextBlock.getWidth(); i++) {
            for (int j = nextBlockY; j < nextBlockY + nextBlock.getHeight(); j++) {
                nextBoard[j][i] = 0;
            }
        }
    }

    protected void moveDown() {
        if (!checkBottom()) {
            fixBoard();
            eraseCurrentBlock();
            currentBlock = nextBlock;
            blockBuffer.copyBlock(currentBlock);
            nextBlock = getRandomBlock(mode);
            eraseNextBlock();
            placeNextBlock();
            drawNextBlock();
            x = 3;
            y = 0;
            clearLine();
            placeCurrentBlock();
            gamePane.revalidate();
            gamePane.repaint();
            return;
        }
        eraseCurrentBlock();
        y++;
        placeCurrentBlock();

        gamePane.revalidate();
        gamePane.repaint();
        score += (201 - delay / 5);
        showScore();
    }

    public void moveRight() {
        eraseCurrentBlock();
        if (currentBlock == null) {
            return;
        }
        if (x < GameView.BORDER_WIDTH - currentBlock.getWidth()) {
            x++;
            if (checkBlockCollision())
                x--;
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
            if (checkBlockCollision())
                x++;
        }
        placeCurrentBlock();
    }

    public void moveRotate() {
        eraseCurrentBlock();
        safetyRotate();
        currentBlock.rotate();
        blockBuffer.copyBlock(currentBlock);
        placeCurrentBlock();
    }

    private void safetyRotate() {
        int count = 0;
        int height = currentBlock.getHeight();
        int width = currentBlock.getWidth();

        if (height > 2) {
            blockBuffer.rotate();
            while (count < 2) {
                if (ifBlockOutOfBounds() || checkBlockCollision())
                    x--;
                else
                    break;
                count++;
            }
        }
        if (width > 2) {
            blockBuffer.rotate();
            while (count < 2) {
                if (ifBlockOutOfBounds() || checkBlockCollision())
                    y--;
                else
                    break;
                count++;
            }
        }
    }

    // 한 번에 블록이 떨어지는 메소드 구현(SPACE BAR)
    private void dropDown() {
        if (currentBlock == null) {
            return;
        }
        eraseCurrentBlock();
        int width = currentBlock.getWidth();
        int height = currentBlock.getHeight();
        y += height;

        Outter: while (y < GameView.BORDER_HEIGHT - height) {
            for (int i = 0; i < width; i++) {
                if (board[y][x + i] > 1)
                    break Outter;
                System.out.println("board[" + y + "][" + (x + i) + "]=" + board[y][x + i]);
            }
            y++;
        }
        y -= height;

        while (true) {
            y++;
            if (ifBlockOutOfBounds() || checkBlockCollision()) {
                y--;
                break;
            }
        }
        placeCurrentBlock();
    }

    // Block이 바닥에 닿는지 확인
    public boolean checkBottom() {
        if (y == GameView.BORDER_HEIGHT - currentBlock.getHeight()) {
            return false;
        }

        int[][] shape = currentBlock.getShape();
        int width = currentBlock.getWidth();
        int height = currentBlock.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[y + j + 1][x + i] > 1 && board[y + j][x + i] == 1) {
                    return false;
                }
            }
        }
        return true;
    }

    // Block끼리 충돌하는지 확인
    private boolean checkBlockCollision() {
        saveBoardBuffer();
        placeBufferBlock();
        for (int j = 0; j < blockBuffer.getHeight(); j++) {
            for (int i = 0; i < blockBuffer.getWidth(); i++) {
                if (boardBuffer[y + j][x + i] > 2) {
                    return true;
                }
            }
        }
        return false;
    }

    // Block이 경계를 넘는지 확인
    private boolean ifBlockOutOfBounds() {
        if (x + blockBuffer.getWidth() > GameView.BORDER_WIDTH
                || y + blockBuffer.getHeight() > GameView.BORDER_HEIGHT)
            return true;
        else
            return false;
    }

    // 블럭 줄삭제
    private void clearLine() {
        for (int i = 0; i < GameView.BORDER_HEIGHT; i++) {
            int sum = Arrays.stream(board[i]).reduce(0, (a, b) -> a + b);
            if (sum > 19) {
                copyLines(i);
            }
        }

        score += 25;
        showScore();
    }

    // 줄을 지우는 메소드 (줄 애니메이션으로 바꿀 예정)
    private void eraseLine(int n) {
        for (int i = 0; i < GameView.BORDER_WIDTH; i++) {
            board[n][i] = 0;
        }
    }

    // 게임 중단 상태에서 다시 실행하는 경우
    public void restart() {
        board = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        nextBoard = new int[NEXT_BOARD_HEIGHT][NEXT_BOARD_WIDTH];
        x = 3;
        y = 0;
        currentBlock = getRandomBlock(mode);
        blockBuffer = getRandomBlock(mode);
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
        } else if (inputValue == -1) {
            // 팝업을 종료하는 경우(X키 누르는 경우, 게임을 처음부터 재시작)
            timer.restart();
            restart();
        }
        // 그 외에는 중단된 상태에서 재시작
    }

    public class GameKeyListener extends KeyAdapter {
        // int moveDownKey = setting.getMoveDownKey();
        int moveRightKey = setting.getMoveRightKey();
        int moveLeftKey = setting.getMoveLeftKey();
        int rotateKey = setting.getRotateKey();
        int stackKey = setting.getStackKey();

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_DOWN) {
                moveDown();
                drawGameBoard();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == moveRightKey) {
                moveRight();
                drawGameBoard();
            } else if (keyCode == moveLeftKey) {
                moveLeft();
                drawGameBoard();
            } else if (keyCode == rotateKey) {
                moveRotate();
                drawGameBoard();
            } else if (keyCode == stackKey) {
                dropDown();
                moveDown();
                drawGameBoard();
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                timer.stop();
                gameView.repaint();
                showESCMessage();
                timer.start();
            }
        }
    }

    private void initZeroBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = 0;
            }
        }
    }

    private void pasteLines(int[][] copy, int[][] paste) {
        for (int i = 0; i < copy.length; i++) {
            System.out.println("paste.length = " + copy.length);
            System.out.println("paste[" + i + "].length = " + paste[i].length);
            paste[i + 1] = Arrays.copyOf(copy[i], paste[i].length);
        }
    }

    private void copyLines(int index) {
        int[][] copy = new int[index][GameView.BORDER_WIDTH];
        copyBoard(board, copy);
        pasteLines(copy, board);
    }

    private void copyBoard(int[][] copy, int[][] paste) {
        for (int i = 0; i < paste.length; i++) {
            paste[i] = Arrays.copyOf(copy[i], paste[i].length);
        }
    }

    private void saveBoardBuffer() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardBuffer[i][j] = board[i][j];
            }
        }
    }

    private void fixBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    System.out.println("doit!");
                    board[i][j] = 2;
                }
            }
        }
    }

    private void showCurrnent() {
        System.out.println("블록현황 x:" + x + " y:" + y + " width" + currentBlock.getWidth() + "height"
                + currentBlock.getHeight());
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    private void showBuffer() {
        System.out.println("버퍼블록현황 x:" + x + " y:" + y + " width" + blockBuffer.getWidth()
                + "height" + blockBuffer.getHeight());
        for (int i = 0; i < boardBuffer.length; i++) {
            for (int j = 0; j < boardBuffer[i].length; j++) {
                System.out.print(boardBuffer[i][j]);
            }
            System.out.println();
        }
    }

    private void showScore() {
        gameView.getScorePane().setText(String.format("%d", score));
    }
}