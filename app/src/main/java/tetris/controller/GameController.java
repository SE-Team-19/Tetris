package tetris.controller;

import static tetris.view.GameView.BORDER_HEIGHT;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.text.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import tetris.model.*;
import tetris.view.*;

public class GameController {

    private static final int NEXT_BOARD_HEIGHT = 5;
    private static final int NEXT_BOARD_WIDTH = 4;
    private static final int ANIMATION_CODE = 20;

    private Timer timer;
    private int delay;
    private int mode;
    private Block currentBlock;
    private Block nextBlock;
    private Block blockBuffer;

    private int[][] colorBoard;
    private int[][] board; // gamePane 의 'X' size를 결정하기 위한 변수
    private int[][] boardBuffer;
    private int[][] nextBoard;

    private int x = 3;
    private int y = 0;
    private int nextBlockX = 0; // warning: 시작 위치 조절
    private int nextBlockY = 0;
    private int score; // game 점수와 관련된 변수

    private String userName;

    private GameView gameView = GameView.getInstance();
    private ScoreView scoreView = ScoreView.getInstance();
    private JTextPane gamePane;
    private JTextPane nextTetrisBlockPane;
    private JLabel gameOverText; // 게임 종료를 나타내주는 문구
    private SimpleAttributeSet boardAttributeSet;
    private SimpleAttributeSet nextBoardAttributeSet;
    private Container contentPane;

    private Map<Integer, Color> colorMap;
    private Map<Integer, Runnable> rotateMap;
    private List<List<WallKick>> wallKickList;

    private Setting setting;
    private boolean isColorBlindMode;
    private PlayerController playerController;

    public GameController(Setting setting, PlayerController playerController, Container contentPane) {
        this.setting = setting;
        this.contentPane = contentPane;
        this.playerController = playerController;
        isColorBlindMode = setting.isColorBlindMode();
        KeyListener gameKeyListener = new GameKeyListener();
        gameView.addKeyListener(gameKeyListener);

        board = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        boardBuffer = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        colorBoard = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        nextBoard = new int[NEXT_BOARD_HEIGHT][NEXT_BOARD_WIDTH];
        currentBlock = getRandomBlock(mode, 0);
        blockBuffer = getRandomBlock(mode, 0);
        nextBlock = getRandomBlock(mode, 10);
        gameOverText = new JLabel("Game Over");
        getSelectModeDialog().setVisible(true);

        colorMap = new HashMap<>();
        if (isColorBlindMode) {
            colorMap.put(new IBlock().getIndentifynumber(), new IBlock().getBlindColor());
            colorMap.put(new JBlock().getIndentifynumber(), new JBlock().getBlindColor());
            colorMap.put(new LBlock().getIndentifynumber(), new LBlock().getBlindColor());
            colorMap.put(new OBlock().getIndentifynumber(), new OBlock().getBlindColor());
            colorMap.put(new SBlock().getIndentifynumber(), new SBlock().getBlindColor());
            colorMap.put(new TBlock().getIndentifynumber(), new TBlock().getBlindColor());
            colorMap.put(new ZBlock().getIndentifynumber(), new ZBlock().getBlindColor());

        } else {
            colorMap.put(new IBlock().getIndentifynumber(), new IBlock().getColor());
            colorMap.put(new JBlock().getIndentifynumber(), new JBlock().getColor());
            colorMap.put(new LBlock().getIndentifynumber(), new LBlock().getColor());
            colorMap.put(new OBlock().getIndentifynumber(), new OBlock().getColor());
            colorMap.put(new SBlock().getIndentifynumber(), new SBlock().getColor());
            colorMap.put(new TBlock().getIndentifynumber(), new TBlock().getColor());
            colorMap.put(new ZBlock().getIndentifynumber(), new ZBlock().getColor());
        }

        rotateMap = new HashMap<>();
        rotateMap.put(3, () -> {
        });
        rotateMap.put(0, () -> x++);
        rotateMap.put(1, () -> {
            y++;
            x--;
        });
        rotateMap.put(2, () -> y--);
        rotateMap.put(7, () -> {
            x--;
            y++;
        });
        rotateMap.put(4, () -> {
            x += 2;
            y--;
        });
        rotateMap.put(5, () -> {
            x -= 2;
            y += 2;
        });
        rotateMap.put(6, () -> {
            x++;
            y -= 2;
        });

        initWallKickList();

        // gamePane 위치 조정
        gamePane = gameView.getGamePane();
        nextTetrisBlockPane = gameView.getNextBlockPane();

        boardAttributeSet = new SimpleAttributeSet();
        nextBoardAttributeSet = new SimpleAttributeSet();

        setAttributeSet(boardAttributeSet);
        setAttributeSet(nextBoardAttributeSet);

        placeBlock(board, currentBlock, x, y);
        placeBlock(nextBoard, nextBlock, nextBlockX, nextBlockY);
        drawGameBoard();
        drawNextBlock();

        // placeAccumulatedBlock(); // collision add

        nextTetrisBlockPane.repaint();
        nextTetrisBlockPane.revalidate();

        startGame();
    }

    private void initWallKickList() {
        wallKickList = new ArrayList<>();
        /*
         * J,L,T,S,Z Block testcase (주의 사항으로 Tetris Fan Wiki에서의 회전에서 y좌표는 +가 위로 올라간다 따라서
         * 음수로 치환해야 한다.)
         */
        wallKickList.add(new ArrayList<>( // 0 >> 1
                Arrays.asList(new WallKick(-1, 0), new WallKick(-1, -1), new WallKick(0, 2), new WallKick(-1, 2))));
        wallKickList.add(new ArrayList<>( // 1 >> 2
                Arrays.asList(new WallKick(1, 0), new WallKick(1, 1), new WallKick(0, -2), new WallKick(1, -2))));
        wallKickList.add(new ArrayList<>( // 2 >> 3
                Arrays.asList(new WallKick(1, 0), new WallKick(1, -1), new WallKick(0, 2), new WallKick(1, 2))));
        wallKickList.add(new ArrayList<>( // 3 >> 0
                Arrays.asList(new WallKick(-1, 0), new WallKick(-1, 1), new WallKick(0, -2), new WallKick(-1, -2))));
        /* IBlock testcase */
        wallKickList.add(new ArrayList<>( // 0 >> 1
                Arrays.asList(new WallKick(-2, 0), new WallKick(1, 0), new WallKick(-2, 1), new WallKick(1, -2))));
        wallKickList.add(new ArrayList<>( // 1 >> 2
                Arrays.asList(new WallKick(-1, 0), new WallKick(2, 0), new WallKick(-1, -2), new WallKick(2, 1))));
        wallKickList.add(new ArrayList<>( // 2 >> 3
                Arrays.asList(new WallKick(2, 0), new WallKick(-1, 0), new WallKick(2, -1), new WallKick(-1, 2))));
        wallKickList.add(new ArrayList<>( // 3 >> 0
                Arrays.asList(new WallKick(1, 0), new WallKick(-2, 0), new WallKick(1, 2), new WallKick(-2, -1))));
    }

    private void transitView(Container pane, Container to, Container from) {
        pane.add(to);
        pane.remove(from);
        focus(to);
        contentPane.revalidate(); // component 변화 후 JFrame 새로고침(component 변화 적용) */
        contentPane.repaint(); // component 변화 후 JFrame 새로고침(component 색 등의 성질 적용) */
    }

    private void focus(Container to) {
        if (to.equals(scoreView)) {
            scoreView.getReturnScoreToMainBtn().requestFocus();
        }
    }

    public void startGame() {
        score = 0;
        delay = 1000;
        showScore();

        timer = new Timer(delay, e -> {
            // moveDown();
            drawGameBoard();
            delay -= delay > 250 ? 5 : 0;
            timer.setDelay(delay);
            // showCurrnent();
        });
        timer.start();
    }

    private void setAttributeSet(SimpleAttributeSet attributeSet) {
        StyleConstants.setFontSize(attributeSet, 20);
        StyleConstants.setFontFamily(attributeSet, "Courier New");
        StyleConstants.setBold(attributeSet, true);
        StyleConstants.setForeground(attributeSet, Color.WHITE);
        StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_CENTER);
        StyleConstants.setLineSpacing(attributeSet, -0.5f);
    }

    private class Pair<K, V> {

        K block;
        V weight;

        public Pair(K block, V weight) {
            this.block = block;
            this.weight = weight;
        }
    }

    public Block getRandomBlock(int mode, int seed) {
        // normal mode
        if (mode == 0) {
            Random random = new Random(System.currentTimeMillis() + seed);
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
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    StyleConstants.setForeground(blockAttributeSet, color);
                    doc.setCharacterAttributes(
                            (board[i].length + 4) + i * (board[i].length + 3) + j, 1,
                            blockAttributeSet, true);
                }
                if (colorBoard[i][j] > 1) {
                    StyleConstants.setForeground(blockAttributeSet, colorMap.get(colorBoard[i][j]));
                    doc.setCharacterAttributes(
                            (colorBoard[i].length + 4) + i * (colorBoard[i].length + 3) + j, 1,
                            blockAttributeSet, true);
                }
                if (board[i][j] == ANIMATION_CODE) {
                    StyleConstants.setForeground(blockAttributeSet, color);
                    doc.setCharacterAttributes(
                            (board[i].length + 4) + i * (board[i].length + 3) + j, 1,
                            blockAttributeSet, true);
                }
            }
        }
    }

    // 주어진 board에 Block을 놓아주는 메소드
    private void placeBlock(int[][] board, Block block, int x, int y) {
        for (int j = 0; j < block.getHeight(); j++) {
            for (int i = 0; i < block.getWidth(); i++) {
                board[y + j][x + i] += block.getShape(i, j);
            }
        }
    }

    // board에서 블록을 지워주는 method
    private void eraseBlock(int[][] board, Block block) {
        for (int i = x; i < x + block.getWidth(); i++) {
            for (int j = y; j < y + block.getHeight(); j++) {
                if (board[j][i] == 1)
                    board[j][i] = 0;
            }
        }
    }

    protected void moveDown() {

        if (!checkBottom()) {
            fixBoard();
            eraseBlock(board, currentBlock);
            clearLine();
            currentBlock.copyBlock(nextBlock);
            blockBuffer.copyBlock(currentBlock);
            nextBlock = getRandomBlock(mode, 0);
            initZeroBoard(nextBoard); // nextBoard 초기화
            placeBlock(nextBoard, nextBlock, nextBlockX, nextBlockY);
            drawNextBlock();
            x = 3;
            y = 0;
            placeBlock(board, currentBlock, x, y);
            if (checkBlockCollision()) {
                timer.stop();
                // gameView.add(gameOverText); // 이 부분 정상적으로 잘 뜨는지 확인해야 함
                gameOverText.setVisible(true); // Game Over 글자를 나타냄
                getGameOverDialog().setVisible(true);
                String difficulty = "normal";
                if (mode == 1)
                    difficulty = "easy";
                else if (mode == 2)
                    difficulty = "hard";
                playerController.addPlayer(userName, score, difficulty);
                playerController.savePlayerList();
                playerController.loadPlayerList();
                scoreView.initRankingPane();
                scoreView.resetRankingList();
                playerController.getPlayerList()
                        .forEach(player -> scoreView.addRankingList(new ArrayList<>(Arrays.asList(player.getName(),
                                Integer.toString(player.getScore()), player.getDifficulty()))));
                scoreView.fillScoreBoard(userName);
                transitView(contentPane, scoreView, gameView);
            }
            gamePane.revalidate();
            gamePane.repaint();
            return;
        }
        eraseBlock(board, currentBlock);
        y++;
        placeBlock(board, currentBlock, x, y);
        showCurrnent(board, currentBlock);

        gamePane.revalidate();
        gamePane.repaint();
        score += (201 - delay / 5);
        showScore();
    }

    public void moveRight() {
        eraseBlock(board, currentBlock);
        if (currentBlock == null) {
            return;
        }
        if (x < GameView.BORDER_WIDTH - currentBlock.getWidth()) {
            x++;
            if (checkBlockCollision())
                x--;
        }
        placeBlock(board, currentBlock, x, y);
    }

    public void moveLeft() {
        eraseBlock(board, currentBlock);
        if (currentBlock == null) {
            return;
        }
        if (x > 0) {
            x--;
            if (checkBlockCollision())
                x++;
        }
        placeBlock(board, currentBlock, x, y);
    }

    public void moveRotate() {
        eraseBlock(board, currentBlock);
        testRotation();
        showCurrnent(boardBuffer, blockBuffer);
        placeBlock(board, currentBlock, x, y);
    }

    /* SRS기반 회전 점검 */
    private void testRotation() {
        // 아예 돌리기 전 x,y 좌표
        int xBeforeRotate = x;
        int yBeforeRotate = y;
        int rotateState = blockBuffer.getRotateCount();
        blockBuffer.rotate();
        rotateMap.get(rotateState).run(); // 순서 유의 (rotate전의 rotateCount를 보고 Anchor를 옮겼음)
        int xBuffer = x;
        int yBuffer = y;
        List<WallKick> wallKicks = wallKickList.get(rotateState);

        /* 충돌 확인 및 체크 */
        for (WallKick wallKick : wallKicks) {
            if (ifBlockOutOfBounds() || checkBlockCollision()) {
                System.out.println("Rotate Test!");
                showCurrnent(boardBuffer, blockBuffer);
                x = xBuffer;
                y = yBuffer;
                System.out.println("wallKick.xKick: " + wallKick.xKick);
                System.out.println("wallKick.yKick: " + wallKick.yKick);
                System.out.print("이전 x:" + x);
                x += wallKick.xKick;
                System.out.println(" 더한 x: " + x);
                System.out.print("이전 y:" + y);
                y += wallKick.yKick;
                System.out.println("더한 y: " + y);
            } else {
                currentBlock.rotate();
                blockBuffer.copyBlock(currentBlock);
                return;
            }
        }

        /* 끝까지 충돌 발생시 rotate 안함 */
        if (ifBlockOutOfBounds() || checkBlockCollision()) {
            x = xBeforeRotate;
            y = yBeforeRotate;
            blockBuffer.copyBlock(currentBlock);
        } else {
            currentBlock.rotate();
            blockBuffer.copyBlock(currentBlock);
        }

    }

    // Block이 바닥에 닿는지 확인
    public boolean checkBottom() {
        if (y == GameView.BORDER_HEIGHT - currentBlock.getHeight())
            return false;

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

    private class WallKick {
        int xKick;
        int yKick;

        WallKick(int xKick, int yKick) {
            this.xKick = xKick;
            this.yKick = yKick;
        }

    }

    // Block끼리 충돌하는지 확인
    private boolean checkBlockCollision() {
        copyBoard(board, boardBuffer);
        placeBlock(boardBuffer, blockBuffer, x, y);
        for (int j = 0; j < blockBuffer.getHeight(); j++) {
            for (int i = 0; i < blockBuffer.getWidth(); i++) {
                if (boardBuffer[y + j][x + i] > 2) {
                    System.out.println("충돌발생!");
                    return true;
                }
            }
        }
        return false;
    }

    // Block이 경계를 넘는지 확인
    private boolean ifBlockOutOfBounds() {
        boolean flag = false;
        if (x < 0 || y < 0) { // 왼쪽 위 아래 경게 확인 (다만 saftyRotate 함수에서 보니까 의미 없을 수도 )
            flag = true;
            return flag;
        } else if (x + blockBuffer.getWidth() > GameView.BORDER_WIDTH
                || y + blockBuffer.getHeight() > GameView.BORDER_HEIGHT)
            flag = true;
        return flag;
    }

    // 블럭 줄삭제
    private void clearLine() {
        for (int i = 0; i < GameView.BORDER_HEIGHT; i++) {
            int sum = Arrays.stream(board[i]).reduce(0, (a, b) -> a + b);
            if (sum > 19) {
                copyLines(i);
            }
        }

        if (mode == 1)
            delay -= 8;
        else if (mode == 2)
            delay -= 12;
        else
            delay -= 10;
        score += 25;
        showScore();
    }

    // 게임 중단 상태에서 다시 실행하는 경우
    public void restart() {
        board = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        nextBoard = new int[NEXT_BOARD_HEIGHT][NEXT_BOARD_WIDTH];
        x = 3;
        y = 0;
        currentBlock = getRandomBlock(mode, 0);
        blockBuffer = getRandomBlock(mode, 0);
        nextBlock = getRandomBlock(mode, 1);

        placeBlock(board, currentBlock, x, y);
        drawGameBoard();
        placeBlock(nextBoard, nextBlock, nextBlockX, nextBlockY);
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
        int moveRightKey = setting.getMoveRightKey();
        int moveLeftKey = setting.getMoveLeftKey();
        int rotateKey = setting.getRotateKey();
        int stackKey = setting.getStackKey();

        // 한 번에 블록이 떨어지는 메소드 구현(SPACE BAR)
        private void dropDown() {
            boolean flag = false;
            if (currentBlock == null) {
                return;
            }
            eraseBlock(board, currentBlock);
            int width = currentBlock.getWidth();
            int height = currentBlock.getHeight();
            y += height;

            while (y < GameView.BORDER_HEIGHT - height) {
                for (int i = 0; i < width; i++) {
                    if (board[y][x + i] > 1) {
                        flag = true;
                        break;
                    }

                }
                if (flag)
                    break;
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
            placeBlock(board, currentBlock, x, y);
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
                // moveDown(); hard drop 적용
                drawGameBoard();
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                timer.stop();
                gameView.repaint();
                showESCMessage();
                timer.start();
            } else if (keyCode == KeyEvent.VK_DOWN) {
                moveDown();
                drawGameBoard();
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
            paste[i + 1] = Arrays.copyOf(copy[i], paste[i].length);
        }
    }

    private void copyLines(int index) {
        int[][] copy = new int[index][GameView.BORDER_WIDTH];
        copyBoard(board, copy);
        pasteLines(copy, board);
        copyBoard(colorBoard, copy);
        pasteLines(copy, colorBoard);
    }

    private void copyBoard(int[][] copy, int[][] paste) {
        for (int i = 0; i < paste.length; i++) {
            paste[i] = Arrays.copyOf(copy[i], paste[i].length);
        }
    }

    private void fixBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    colorBoard[i][j] = currentBlock.getIndentifynumber();
                    board[i][j] = 2;
                }
                // else if (board[i][j] == 3) {
                // board[i][j] = 2;
                // } 잠시 보관 (이후 현재 블록을 제외한 줄 수를 알기 위해서)
            }
        }
    }

    public JDialog getGameOverDialog() {
        JFrame frame = new JFrame();
        JDialog gameOverDialog = new JDialog(frame, "이름을 입력하세요", true);
        gameOverDialog.setBounds(0, 0, 300, 200);
        gameOverDialog.setLocationRelativeTo(null);
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1, 1, 0, 0));
        JTextField inputName = new JTextField();
        inputName.addActionListener(e -> {
            userName = inputName.getText();
            gameOverDialog.dispose();
        });
        pane.add(inputName);
        gameOverDialog.setContentPane(pane);
        return gameOverDialog;
    }

    public JDialog getSelectModeDialog() {
        JFrame frame = new JFrame();
        JDialog selectModeDialog = new JDialog(frame, "이름을 입력하세요", true);
        selectModeDialog.setBounds(0, 0, 300, 200);
        selectModeDialog.setLocationRelativeTo(null);
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1, 3, 0, 0));
        JButton easyBtn = new JButton("Easy");
        JButton normalBtn = new JButton("Normal");
        JButton hardBtn = new JButton("Hard");
        easyBtn.addActionListener(e -> {
            mode = 1;
            selectModeDialog.dispose();
        });
        normalBtn.addActionListener(e -> {
            mode = 0;
            selectModeDialog.dispose();
        });
        hardBtn.addActionListener(e -> {
            mode = 2;
            selectModeDialog.dispose();
        });
        easyBtn.addKeyListener(new DialogKeyEventListener());
        normalBtn.addKeyListener(new DialogKeyEventListener());
        hardBtn.addKeyListener(new DialogKeyEventListener());
        pane.add(easyBtn);
        pane.add(normalBtn);
        pane.add(hardBtn);
        selectModeDialog.setContentPane(pane);
        return selectModeDialog;
    }

    private class DialogKeyEventListener extends KeyAdapter {
        int moveRightKey = setting.getMoveRightKey();
        int moveLeftKey = setting.getMoveLeftKey();
        int stackKey = setting.getStackKey();

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == moveLeftKey) // 여기에 setting 키를 대입해 주세요
            {
                e.getComponent().transferFocusBackward();
            } else if (e.getKeyCode() == moveRightKey) {
                e.getComponent().transferFocus();
            } else if (e.getKeyCode() == stackKey) {
                ((JButton) e.getComponent()).doClick();
            }
        }
    }

    public void stopTimer() {
        timer.stop();
    }

    private void showCurrnent(int[][] board, Block block) {
        System.out.println("블록현황 x:" + x + " y:" + y + " width:" + block.getWidth() + " height:"
                + block.getHeight() + " rotateCount: " + block.getRotateCount());
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    private void showScore() {
        gameView.getScorePane().setText(String.format("%d", score));
    }
}