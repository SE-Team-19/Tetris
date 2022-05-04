package tetris.controller;

import static tetris.view.GameView.BORDER_HEIGHT;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.*;
import java.util.*;
import java.util.stream.*;

import tetris.model.*;
import tetris.view.*;

public class GameController {

    private static final int NEXT_BOARD_HEIGHT = 5;
    private static final int NEXT_BOARD_WIDTH = 4;
    private static final int ANIMATION_INTERVAL = 50;

    private Timer gameDelayTimer;
    private Timer gameTimer;
    private int delay;
    private int gameTime;
    private int diffMode; // 난이도 설정
    private int gameMode; // 게임모드 설정
    private Block currentBlock;
    private Block nextBlock;
    private Block blockBuffer;

    private int[][] visualBoard;
    private int[][] board; // gamePane 의 'X' size를 결정하기 위한 변수
    private int[][] boardBuffer;

    private int x = 3;
    private int y = 0;
    private int score; // game 점수와 관련된 변수

    private String userName;

    private GameView gameView = GameView.getInstance();
    private ScoreView scoreView = ScoreView.getInstance();
    private JTextPane gamePane;
    private JTextPane nextBlockPane;
    private JLabel gameOverText; // 게임 종료를 나타내주는 문구
    private SimpleAttributeSet boardAttributeSet;
    private SimpleAttributeSet nextBoardAttributeSet;
    private Container contentPane;
    KeyListener gameKeyListener;

    private Map<KeyPair, Runnable> gameKeyMap;
    private Map<Integer, Color> colorMap;
    private Map<Integer, Runnable> rotateMap;
    private Map<Integer, Character> blockCharMap;
    private List<List<WallKick>> wallKickList;

    private Setting setting;
    private boolean isColorBlindMode;
    private PlayerController playerController;

    public GameController(Setting setting, PlayerController playerController, Container contentPane) {
        this.setting = setting;
        this.contentPane = contentPane;
        this.playerController = playerController;
        initGameController();
    }

    private void initGameController() {
        isColorBlindMode = setting.isColorBlindMode();

        board = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        boardBuffer = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        visualBoard = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        currentBlock = getRandomBlock(diffMode, 0);
        blockBuffer = getRandomBlock(diffMode, 0);
        nextBlock = getRandomBlock(diffMode, 10);
        gameOverText = new JLabel("Game Over");

        gamePane = gameView.getGameBoardPane();
        nextBlockPane = gameView.getNextBlockPane();

        // getSelectModeDialog().setVisible(true);

        initBlockCharMap();
        initColorMap();
        initRotateMap();
        initWallKickList();
        new InitGameKeyMap(setting);
        addGameKeyListener();

        boardAttributeSet = new SimpleAttributeSet();
        nextBoardAttributeSet = new SimpleAttributeSet();

        setAttributeSet(boardAttributeSet);
        setAttributeSet(nextBoardAttributeSet);

        placeBlock(board, visualBoard, currentBlock, x, y);
        drawGameBoard();
        drawNextBlock();

        // placeAccumulatedBlock(); // collision add

        nextBlockPane.repaint();
        nextBlockPane.revalidate();

        score = 0;
        delay = 1000;
        gameTime = 0;

        showScore();
    }

    private void initBlockCharMap() {
        blockCharMap = new HashMap<>();
        blockCharMap.put(Block.IBLOCK_IDENTIFY_NUMBER, GameView.BLOCK_CHAR);
        blockCharMap.put(Block.JBLOCK_IDENTIFY_NUMBER, GameView.BLOCK_CHAR);
        blockCharMap.put(Block.LBLOCK_IDENTIFY_NUMBER, GameView.BLOCK_CHAR);
        blockCharMap.put(Block.OBLOCK_IDENTIFY_NUMBER, GameView.BLOCK_CHAR);
        blockCharMap.put(Block.SBLOCK_IDENTIFY_NUMBER, GameView.BLOCK_CHAR);
        blockCharMap.put(Block.TBLOCK_IDENTIFY_NUMBER, GameView.BLOCK_CHAR);
        blockCharMap.put(Block.ZBLOCK_IDENTIFY_NUMBER, GameView.BLOCK_CHAR);
        blockCharMap.put(Block.WEIGHTBLOCK_IDENTIFY_NUMBER, GameView.BLOCK_CHAR);
        blockCharMap.put(Block.ONELINEBLOCK_IDENTIFY_NUMBER, GameView.ONELINE_CHAR);
        blockCharMap.put(Block.NULL_IDENTIFY_NUMBER, GameView.NULL_CHAR);
    }

    private void initColorMap() {
        colorMap = new HashMap<>();
        colorMap.put(Block.ONELINEBLOCK_IDENTIFY_NUMBER, Color.WHITE);
        colorMap.put(Block.NULL_IDENTIFY_NUMBER, Color.WHITE);
        if (isColorBlindMode) {
            colorMap.put(new IBlock().getIdentifynumber(), new IBlock().getBlindColor());
            colorMap.put(new JBlock().getIdentifynumber(), new JBlock().getBlindColor());
            colorMap.put(new LBlock().getIdentifynumber(), new LBlock().getBlindColor());
            colorMap.put(new OBlock().getIdentifynumber(), new OBlock().getBlindColor());
            colorMap.put(new SBlock().getIdentifynumber(), new SBlock().getBlindColor());
            colorMap.put(new TBlock().getIdentifynumber(), new TBlock().getBlindColor());
            colorMap.put(new ZBlock().getIdentifynumber(), new ZBlock().getBlindColor());
            colorMap.put(new WeightBlock().getIdentifynumber(), new WeightBlock().getBlindColor());

        } else {
            colorMap.put(new IBlock().getIdentifynumber(), new IBlock().getColor());
            colorMap.put(new JBlock().getIdentifynumber(), new JBlock().getColor());
            colorMap.put(new LBlock().getIdentifynumber(), new LBlock().getColor());
            colorMap.put(new OBlock().getIdentifynumber(), new OBlock().getColor());
            colorMap.put(new SBlock().getIdentifynumber(), new SBlock().getColor());
            colorMap.put(new TBlock().getIdentifynumber(), new TBlock().getColor());
            colorMap.put(new ZBlock().getIdentifynumber(), new ZBlock().getColor());
            colorMap.put(new WeightBlock().getIdentifynumber(), new WeightBlock().getColor());
        }
    }

    private void initRotateMap() {
        rotateMap = new HashMap<>();
        rotateMap.put(Block.FOURTH_ROTATE_STATE, () -> {
        });
        rotateMap.put(Block.FIRST_ROTATE_STATE, () -> x++);
        rotateMap.put(Block.SECOND_ROTATE_STATE, () -> {
            y++;
            x--;
        });
        rotateMap.put(Block.THIRD_ROTATE_STATE, () -> y--);
        rotateMap.put(Block.IBLOCK_FOURTH_ROTATE_STATE, () -> {
            x--;
            y++;
        });
        rotateMap.put(Block.IBLOCK_FIRST_ROTATE_STATE, () -> {
            x += 2;
            y--;
        });
        rotateMap.put(Block.IBLOCK_SECOND_ROTATE_STATE, () -> {
            x -= 2;
            y += 2;
        });
        rotateMap.put(Block.IBLOCK_THIRD_ROTATE_STATE, () -> {
            x++;
            y -= 2;
        });
        rotateMap.put(Block.DO_NOT_ROTATE_STATE, () -> {
        });
        rotateMap.put(Block.OBLOCK_ROTATE_STATE, () -> {
        });
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
        if (to.equals(scoreView))
            scoreView.getReturnScoreToMainBtn().requestFocus();
        else if (to.equals(gameView.getSelectDiffPane()))
            gameView.getEasyBtn().requestFocus();
        else if (to.equals(gameView.getSelectModePane()))
            gameView.getGeneralModeBtn().requestFocus();
        else if (to.equals(gameView.getGameDisplayPane()))
            gamePane.requestFocus();
    }

    public void startGameDelayTimer(int startDelay) {
        gameDelayTimer = new Timer(startDelay, e -> {
            moveDown();
            drawGameBoard();
            delay -= delay > 250 ? 5 : 0;
            gameDelayTimer.setDelay(delay);
            System.out.println(delay);
        });
        gameDelayTimer.start();
        startStopWatch();
    }

    private void startStopWatch() {
        gameTimer = new Timer(1000, e -> {
            gameTime++;
            showTime();
        });
        gameTimer.start();
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
            int block = random.nextInt(8);
            switch (block) {
                case 0:
                    return new IBlock(true);
                case 1:
                    return new JBlock(true);
                case 2:
                    return new LBlock(true);
                case 3:
                    return new ZBlock(true);
                case 4:
                    return new SBlock(true);
                case 5:
                    return new TBlock(true);
                case 6:
                    return new OBlock(true);
                case 7:
                    return new WeightBlock();
                default:
                    return new IBlock(true);
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
                sb.append(blockCharMap.get(visualBoard[i][j])); // currentBlock 의 모양을 그려준다.
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
        paintBlock();
    }

    public void drawNextBlock() {
        int nextHeight = nextBlock.getHeight();
        int nextWidth = nextBlock.getWidth();

        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < nextHeight; j++) {
            for (int i = 0; i < nextWidth; i++) {
                sb.append(blockCharMap.get(nextBlock.getVisualShape(i, j)));
            }
            sb.append("\n");
        }
        nextBlockPane.setText(sb.toString());

        StyledDocument doc = nextBlockPane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), nextBoardAttributeSet, false);
        SimpleAttributeSet blockAttributeSet = new SimpleAttributeSet();
        for (int j = 0; j < nextHeight; j++) {
            for (int i = 0; i < nextWidth; i++) {
                StyleConstants.setForeground(blockAttributeSet, colorMap.get(nextBlock.getVisualShape(i, j)));
                doc.setCharacterAttributes(i + j + j * nextWidth, 1, blockAttributeSet, false);
                System.out.println("offset: " + (i + j + j * nextWidth));
            }
        }

    }

    private void paintBlock() {
        StyledDocument doc = gamePane.getStyledDocument();
        SimpleAttributeSet blockAttributeSet = new SimpleAttributeSet();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (visualBoard[i][j] > 1) {
                    StyleConstants.setForeground(blockAttributeSet, colorMap.get(visualBoard[i][j]));
                    doc.setCharacterAttributes(
                            (visualBoard[i].length + 4) + i * (visualBoard[i].length + 3) + j, 1,
                            blockAttributeSet, true);
                }
            }
        }
    }

    // 주어진 board에 Block을 놓아주는 메소드
    private void placeBlock(int[][] board, int[][] visualBoard, Block block, int x, int y) {
        for (int j = 0; j < block.getHeight(); j++) {
            for (int i = 0; i < block.getWidth(); i++) {
                board[y + j][x + i] += block.getShape(i, j);
                visualBoard[y + j][x + i] += block.getVisualShape(i, j);
            }
        }
    }

    // 주어진 board에 Block을 놓아주는 메소드(오버로딩)
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
                if (board[j][i] == 1) {
                    board[j][i] = 0;
                    visualBoard[j][i] = 0;
                }
            }
        }
    }

    protected void moveDown() {

        if (!checkBottom()) {
            lockDelay();
            return;
        }
        eraseBlock(board, currentBlock);
        y++;
        placeBlock(board, visualBoard, currentBlock, x, y);
        showCurrent(visualBoard, currentBlock);

        gamePane.revalidate();
        gamePane.repaint();
        score += (201 - delay / 5);
        showScore();
    }

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
        placeBlock(board, visualBoard, currentBlock, x, y);
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
        placeBlock(board, visualBoard, currentBlock, x, y);
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
        placeBlock(board, visualBoard, currentBlock, x, y);
    }

    public void moveRotate() {
        eraseBlock(board, currentBlock);
        testRotation();
        showCurrent(boardBuffer, blockBuffer);
        placeBlock(board, visualBoard, currentBlock, x, y);
    }

    /* SRS기반 회전 점검 */
    private void testRotation() {
        // 아예 돌리기 전 x,y 좌표
        int rotateState = blockBuffer.getRotateCount();
        if (rotateState == Block.DO_NOT_ROTATE_STATE)
            return;
        else if (rotateState == Block.OBLOCK_ROTATE_STATE) {
            currentBlock.rotate();
            blockBuffer.copyBlock(currentBlock);
            return;
        }
        int xBeforeRotate = x;
        int yBeforeRotate = y;
        blockBuffer.rotate();
        rotateMap.get(rotateState).run(); // 순서 유의 (rotate전의 rotateCount를 보고 Anchor를 옮겼음)
        int xBuffer = x;
        int yBuffer = y;
        List<WallKick> wallKicks = wallKickList.get(rotateState);

        /* 충돌 확인 및 체크 */
        for (WallKick wallKick : wallKicks) {
            if (ifBlockOutOfBounds() || checkBlockCollision()) {
                x = xBuffer;
                y = yBuffer;
                x += wallKick.xKick;
                y += wallKick.yKick;
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
    private boolean clearLine() {
        boolean existFullyLine = false;
        int fullyLines = 0;
        int startindex = -1;
        for (int i = 0; i < GameView.BORDER_HEIGHT; i++) {
            int sum = Arrays.stream(board[i]).sum();
            if (sum > 19) {
                startindex = i;
                fullyLines++;
                existFullyLine = true;
            }
        }
        if (fullyLines > 0) {
            startindex = startindex - fullyLines + 1;
            launchDeleteAnimation(startindex, fullyLines);
            delay -= delay > 250 ? 12 - 2 * diffMode % 2 : 0;
            score += 25;
            showScore();
        }
        return existFullyLine;
    }

    // 줄삭제 애니메이션
    private void launchDeleteAnimation(int index, int lines) {
        stopGameDelayTimer();
        gamePane.removeKeyListener(gameKeyListener);
        Timer aniTimer;
        int count = 0;
        int aniDelay = ANIMATION_INTERVAL;
        for (count = 0; count < 10; count++) {
            if (count % 2 == 0)
                aniTimer = new Timer(count * aniDelay, e -> paintLines(index, lines, Color.WHITE));
            else
                aniTimer = new Timer(count * aniDelay, e -> paintLines(index, lines, Color.BLACK));
            aniTimer.setRepeats(false);
            aniTimer.start();
        }
        int totaldelay = count * aniDelay;
        aniTimer = new Timer(totaldelay, e -> {
            overWriteLines(index, lines);
            takeOutNextBlock();
            gamePane.addKeyListener(gameKeyListener);
        });
        aniTimer.setRepeats(false);
        aniTimer.start();
        startGameDelayTimer(totaldelay + delay);

    }

    // 줄색칠 메소드
    private void paintLines(int index, int lines, Color color) {
        StyledDocument doc = gamePane.getStyledDocument();
        SimpleAttributeSet blockAttributeSet = new SimpleAttributeSet();
        int width = board[index].length;
        StyleConstants.setForeground(blockAttributeSet, color);
        for (int i = 0; i < lines; i++) {
            doc.setCharacterAttributes(
                    (width + 4) + (index + i) * (width + 3), width,
                    blockAttributeSet, true);
        }
    }

    // 바닥 도달시
    private void lockDelay() {
        if (gameMode == 1) {
            if (currentBlock.getIdentifynumber() == Block.WEIGHTBLOCK_IDENTIFY_NUMBER)
                ifIsweightBlock(board, visualBoard, currentBlock);
        }
        copyBoard(board, boardBuffer);
        fixBoard();
        if (!clearLine())
            takeOutNextBlock();
    }

    // 다음 블록 놓기
    private void takeOutNextBlock() {
        currentBlock.copyBlock(nextBlock);
        blockBuffer.copyBlock(currentBlock);
        nextBlock = getRandomBlock(diffMode, 0);
        drawNextBlock();
        x = 3;
        y = 0;
        placeBlock(board, visualBoard, currentBlock, x, y);
        drawGameBoard();
        if (checkBlockCollision()) {
            // gameView.add(gameOverText); // 이 부분 정상적으로 잘 뜨는지 확인해야 함
            gameOverText.setVisible(true); // Game Over 글자를 나타냄
            // getGameOverDialog().setVisible(true);
            String difficulty = "normal";
            if (diffMode == 1)
                difficulty = "easy";
            else if (diffMode == 2)
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
    }

    /* 아이템블록 구현 메소드 */
    // 무게추
    private void ifIsweightBlock(int[][] board, int[][] visualBoard, Block block) {
        int yTemp = GameView.BORDER_HEIGHT - block.getHeight();
        for (int j = 0; j < GameView.BORDER_HEIGHT; j++) {
            for (int i = 0; i < block.getWidth(); i++) {
                board[j][x + i] = 0;
                visualBoard[j][x + i] = 0;
            }
        }
        placeBlock(board, visualBoard, block, x, yTemp);
    }

    // 게임 중단 상태에서 다시 실행하는 경우
    public void restart() {
        board = new int[BORDER_HEIGHT][GameView.BORDER_WIDTH];
        x = 3;
        y = 0;
        currentBlock = getRandomBlock(diffMode, 0);
        blockBuffer = getRandomBlock(diffMode, 0);
        nextBlock = getRandomBlock(diffMode, 1);

        placeBlock(board, visualBoard, currentBlock, x, y);
        drawGameBoard();
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
            gameDelayTimer.restart();
            restart();
        }
        // 그 외에는 중단된 상태에서 재시작
    }

    // Key 쌍을 위한 클래스
    public class KeyPair {

        private final int keyCode;
        private final Component component;

        public KeyPair(int keyCode, Component component) {
            this.keyCode = keyCode;
            this.component = component;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof KeyPair))
                return false;
            KeyPair key = (KeyPair) o;
            return keyCode == key.keyCode && component == key.component;
        }

        @Override
        public int hashCode() {
            return Objects.hash(keyCode, component);
        }
    }

    private class InitGameKeyMap {

        int upKey;
        int downKey;
        int leftKey;
        int rightKey;
        int stackKey;

        private InitGameKeyMap(Setting setting) {
            loadSetting(setting);
            resetMap();
            initAllKey();
        }

        private void loadSetting(Setting setting) {
            this.upKey = setting.getRotateKey();
            this.downKey = setting.getMoveDownKey();
            this.leftKey = setting.getMoveLeftKey();
            this.rightKey = setting.getMoveRightKey();
            this.stackKey = setting.getStackKey();
        }

        private void initAllKey() {
            initUpKey();
            initDownKey();
            initLeftKey();
            initRightKey();
            initStackKey();
        }

        private void resetMap() {
            gameKeyMap = new HashMap<>();
        }

        private void initUpKey() {
            gameKeyMap.put(new KeyPair(upKey, gamePane), () -> {
                moveRotate();
                drawGameBoard();
            });
        }

        private void initDownKey() {
            gameKeyMap.put(new KeyPair(downKey, gamePane), () -> {
                moveDown();
                drawGameBoard();
            });
        }

        private void initLeftKey() {
            for (Component comp : gameView.getSelectDiffPane().getComponents())
                gameKeyMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            gameKeyMap.put(new KeyPair(leftKey, gameView.getEasyBtn()),
                    () -> gameView.getHardBtn().requestFocus(true));
            for (Component comp : gameView.getSelectModePane().getComponents())
                gameKeyMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            gameKeyMap.put(new KeyPair(leftKey, gameView.getGeneralModeBtn()),
                    () -> gameView.getTimeAttackBtn().requestFocus(true));
            gameKeyMap.put(new KeyPair(leftKey, gamePane), () -> {
                moveLeft();
                drawGameBoard();
            });
        }

        private void initRightKey() {
            for (Component comp : gameView.getSelectDiffPane().getComponents())
                gameKeyMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            gameKeyMap.put(new KeyPair(rightKey, gameView.getHardBtn()),
                    () -> gameView.getEasyBtn().requestFocus(true));
            for (Component comp : gameView.getSelectModePane().getComponents())
                gameKeyMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            gameKeyMap.put(new KeyPair(rightKey, gameView.getTimeAttackBtn()),
                    () -> gameView.getGeneralModeBtn().requestFocus(true));
            gameKeyMap.put(new KeyPair(rightKey, gamePane), () -> {
                moveRight();
                drawGameBoard();
            });
        }

        private void initStackKey() {
            gameKeyMap.put(new KeyPair(stackKey, gamePane), () -> {
                dropDown();
                // moveDown(); hard drop 적용
                drawGameBoard();
            });
            gameKeyMap.put(new KeyPair(stackKey, gameView.getEasyBtn()), () -> {
                diffMode = 1;
                transitView(gameView, gameView.getGameDisplayPane(), gameView.getSelectDiffPane());
                startGameDelayTimer(delay);
            });
            gameKeyMap.put(new KeyPair(stackKey, gameView.getNormalBtn()), () -> {
                diffMode = 0;
                transitView(gameView, gameView.getGameDisplayPane(), gameView.getSelectDiffPane());
                startGameDelayTimer(delay);
            });
            gameKeyMap.put(new KeyPair(stackKey, gameView.getHardBtn()), () -> {
                diffMode = 2;
                transitView(gameView, gameView.getGameDisplayPane(), gameView.getSelectDiffPane());
                startGameDelayTimer(delay);
            });
            gameKeyMap.put(new KeyPair(stackKey, gameView.getGeneralModeBtn()), () -> {
                gameMode = 0;
                transitView(gameView, gameView.getSelectDiffPane(), gameView.getSelectModePane());
            });
            gameKeyMap.put(new KeyPair(stackKey, gameView.getItemModeBtn()), () -> {
                gameMode = 1;
                transitView(gameView, gameView.getSelectDiffPane(), gameView.getSelectModePane());
            });
            gameKeyMap.put(new KeyPair(stackKey, gameView.getTimeAttackBtn()), () -> {
                gameMode = 2;
                transitView(gameView, gameView.getSelectDiffPane(), gameView.getSelectModePane());
            });
        }
    }

    public class GameKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            KeyPair key = new KeyPair(e.getKeyCode(), e.getComponent());
            if (gameKeyMap.containsKey(key))
                gameKeyMap.get(key).run();
        }
    }

    private void addGameKeyListener() {
        gameKeyListener = new GameKeyListener();
        for (Component comp : gameView.getSelectDiffPane().getComponents())
            comp.addKeyListener(gameKeyListener);
        for (Component comp : gameView.getSelectModePane().getComponents())
            comp.addKeyListener(gameKeyListener);
        gamePane.addKeyListener(gameKeyListener);
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

    private void overWriteLines(int startIndex, int lines) {
        int endIndex = startIndex + lines;
        for (int i = startIndex; i < endIndex; i++)
            overWriteLine(i);
    }

    private void overWriteLine(int index) {
        int[][] copy = new int[index][GameView.BORDER_WIDTH];
        copyBoard(board, copy);
        pasteLines(copy, board);
        copyBoard(visualBoard, copy);
        pasteLines(copy, visualBoard);
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
                    board[i][j] = 2;
                    if (visualBoard[i][j] == Block.ONELINEBLOCK_IDENTIFY_NUMBER)
                        board[i][j] = 20;
                    visualBoard[i][j] = currentBlock.getIdentifynumber();
                }
                // else if (board[i][j] == 3) {
                // board[i][j] = 2;
                // } 잠시 보관 (이후 현재 블록을 제외한 줄 수를 알기 위해서)
            }
        }
    }

    public void stopGameDelayTimer() {
        gameDelayTimer.stop();
        gameTimer.stop();
    }

    private void showCurrent(int[][] board, Block block) {
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

    private void showTime() {
        gameView.getTimePane().setText(String.format("%d", gameTime));
    }
}