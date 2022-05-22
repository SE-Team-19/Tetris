package tetris.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.*;
import java.util.*;

import tetris.model.*;
import tetris.view.*;

public class PlayerTwoController {

    private static final int ANIMATION_INTERVAL = 50;
    private static final int LOCK_DELAY_TIME = 500;
    private static final int BOARD_START_HEIGHT = 5;
    private static final int BOARD_END_HEIGHT = BattleModeView.BORDER_HEIGHT + BOARD_START_HEIGHT;
    private static final int BOARD_HEIGHT = BOARD_END_HEIGHT - BOARD_START_HEIGHT;
    private static final int BOARD_WIDTH = BattleModeView.BORDER_WIDTH;
    private static final int START_X = 3;
    private static final int START_Y = BOARD_START_HEIGHT - 1;
    private static final int BOMB_RANGE = 5; // 홀수만 가능
    private static final int MAX_BLOCK_HEIGHT = 4;

    private Timer gameDelayTimer;
    private Timer gameTimer;
    private boolean isBottomFlag;
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
    private int[][] attackLineBoard;
    private Deque<Integer> blockDeque;
    private Deque<int[]> attackLinesDeque;

    private int x;
    private int y;
    private int ghostY;
    private int score; // game 점수와 관련된 변수
    private int attackLines;
    private int scoreLines;

    private String userName;

    private BattleModeView battleModeView = BattleModeView.getInstance();
    private ScoreView scoreView = ScoreView.getInstance();
    private JTextPane gamePane;
    private JTextPane nextBlockPane;
    private JTextPane attackLinePane;
    private JLabel gameOverText; // 게임 종료를 나타내주는 문구
    private SimpleAttributeSet boardAttributeSet;
    private SimpleAttributeSet nextBoardAttributeSet;
    private SimpleAttributeSet attackBoardAttributeSet;
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
    private Boolean startFlag = false;   // BattleModeController 에서 사용하기 위해 추가

    public PlayerTwoController(Setting setting, PlayerController playerController, Container contentPane) {
        this.setting = setting;
        this.contentPane = contentPane;
        this.playerController = playerController;
        initGameController();
    }

    private void initGameController() {
        isColorBlindMode = setting.isColorBlindMode();

        board = new int[BOARD_END_HEIGHT][BOARD_WIDTH];
        boardBuffer = new int[BOARD_END_HEIGHT][BOARD_WIDTH];
        visualBoard = new int[BOARD_END_HEIGHT][BOARD_WIDTH];
        attackLineBoard = new int[BOARD_HEIGHT][BOARD_WIDTH];
        attackLinesDeque = new ArrayDeque<>();
        blockDeque = new ArrayDeque<>();
        gameOverText = new JLabel("Game Over");
        x = START_X;
        y = START_Y;

        gamePane = battleModeView.getPlayerTwoGameBoardPane();
        nextBlockPane = battleModeView.getPlayerTwoNextBlockPane();
        attackLinePane = battleModeView.getPlayerTwoAttackLinePane();

        initBlockCharMap();
        initColorMap();
        initRotateMap();
        initWallKickList();
        new InitGameKeyMap(setting);
        addGameKeyListener();

        boardAttributeSet = new SimpleAttributeSet();
        nextBoardAttributeSet = new SimpleAttributeSet();
        attackBoardAttributeSet = new SimpleAttributeSet();

        setAttributeSet(boardAttributeSet);
        setAttributeSet(nextBoardAttributeSet);
        setAttributeSet(attackBoardAttributeSet);

    }

    private void startGame() {
        generateBlockRandomizer(0);
        blockBuffer = getBlock(blockDeque.getFirst());
        currentBlock = getBlock(blockDeque.removeFirst());
        nextBlock = getBlock(blockDeque.removeFirst());

        placeBlock(board, visualBoard, currentBlock, x, y);
        drawGameBoard();
        drawNextBlock();

        nextBlockPane.repaint();
        nextBlockPane.revalidate();

        score = 0;
        delay = 1000;
        gameTime = 0;

        showScore();
        startGameDelayTimer(delay);
    }

    private void initBlockCharMap() {
        blockCharMap = new HashMap<>();
        blockCharMap.put(Block.IBLOCK_IDENTIFY_NUMBER, BattleModeView.BLOCK_CHAR);
        blockCharMap.put(Block.JBLOCK_IDENTIFY_NUMBER, BattleModeView.BLOCK_CHAR);
        blockCharMap.put(Block.LBLOCK_IDENTIFY_NUMBER, BattleModeView.BLOCK_CHAR);
        blockCharMap.put(Block.OBLOCK_IDENTIFY_NUMBER, BattleModeView.BLOCK_CHAR);
        blockCharMap.put(Block.SBLOCK_IDENTIFY_NUMBER, BattleModeView.BLOCK_CHAR);
        blockCharMap.put(Block.TBLOCK_IDENTIFY_NUMBER, BattleModeView.BLOCK_CHAR);
        blockCharMap.put(Block.ZBLOCK_IDENTIFY_NUMBER, BattleModeView.BLOCK_CHAR);
        blockCharMap.put(Block.GHOST_IDENTIFIY_NUMBER, BattleModeView.GHOST_CHAR);
        blockCharMap.put(Block.BOMBBLOCK_IDENTIFY_NUMBER, BattleModeView.BOMB_CHAR);
        blockCharMap.put(Block.WEIGHTBLOCK_IDENTIFY_NUMBER, BattleModeView.BLOCK_CHAR);
        blockCharMap.put(Block.ONELINEBLOCK_IDENTIFY_NUMBER, BattleModeView.ONELINE_CHAR);
        blockCharMap.put(Block.NULL_IDENTIFY_NUMBER, BattleModeView.NULL_CHAR);
    }

    private void initColorMap() {
        colorMap = new HashMap<>();
        colorMap.put(Block.ONELINEBLOCK_IDENTIFY_NUMBER, Color.WHITE);
        colorMap.put(Block.NULL_IDENTIFY_NUMBER, Color.WHITE);
        colorMap.put(Block.GHOST_IDENTIFIY_NUMBER, new Color(200, 200, 200));
        colorMap.put(Block.BOMBBLOCK_IDENTIFY_NUMBER, Color.RED);
        if (isColorBlindMode) {
            colorMap.put(Block.IBLOCK_IDENTIFY_NUMBER, new IBlock().getBlindColor());
            colorMap.put(Block.JBLOCK_IDENTIFY_NUMBER, new JBlock().getBlindColor());
            colorMap.put(Block.LBLOCK_IDENTIFY_NUMBER, new LBlock().getBlindColor());
            colorMap.put(Block.OBLOCK_IDENTIFY_NUMBER, new OBlock().getBlindColor());
            colorMap.put(Block.SBLOCK_IDENTIFY_NUMBER, new SBlock().getBlindColor());
            colorMap.put(Block.TBLOCK_IDENTIFY_NUMBER, new TBlock().getBlindColor());
            colorMap.put(Block.ZBLOCK_IDENTIFY_NUMBER, new ZBlock().getBlindColor());
            colorMap.put(Block.WEIGHTBLOCK_IDENTIFY_NUMBER, new WeightBlock().getBlindColor());

        } else {
            colorMap.put(Block.IBLOCK_IDENTIFY_NUMBER, new IBlock().getColor());
            colorMap.put(Block.JBLOCK_IDENTIFY_NUMBER, new JBlock().getColor());
            colorMap.put(Block.LBLOCK_IDENTIFY_NUMBER, new LBlock().getColor());
            colorMap.put(Block.OBLOCK_IDENTIFY_NUMBER, new OBlock().getColor());
            colorMap.put(Block.SBLOCK_IDENTIFY_NUMBER, new SBlock().getColor());
            colorMap.put(Block.TBLOCK_IDENTIFY_NUMBER, new TBlock().getColor());
            colorMap.put(Block.ZBLOCK_IDENTIFY_NUMBER, new ZBlock().getColor());
            colorMap.put(Block.WEIGHTBLOCK_IDENTIFY_NUMBER, new WeightBlock().getColor());
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
        else if (to.equals(battleModeView.getSelectBattleModePane()))
            battleModeView.getGeneralModeBtn().requestFocus();
        else if (to.equals(battleModeView.getBattleModeDisplayPane()))
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
        startFlag = true;     // BattleModeController 에서 사용하기 위해 추가
    }

    public boolean getStartFlag() {
        return this.startFlag;
    }


    private void setAttributeSet(SimpleAttributeSet attributeSet) {
        StyleConstants.setFontSize(attributeSet, 20);
        StyleConstants.setFontFamily(attributeSet, "Courier New");
        StyleConstants.setBold(attributeSet, true);
        StyleConstants.setForeground(attributeSet, Color.WHITE);
        StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_CENTER);
        StyleConstants.setLineSpacing(attributeSet, -0.5f);
    }

    public void generateBlockRandomizer(int mode) {
        int jBlock = Block.JBLOCK_IDENTIFY_NUMBER;
        int lBlock = Block.LBLOCK_IDENTIFY_NUMBER;
        int zBlock = Block.ZBLOCK_IDENTIFY_NUMBER;
        int sBlock = Block.SBLOCK_IDENTIFY_NUMBER;
        int tBlock = Block.TBLOCK_IDENTIFY_NUMBER;
        int oBlock = Block.OBLOCK_IDENTIFY_NUMBER;
        int iBlock = Block.IBLOCK_IDENTIFY_NUMBER;

        List<Integer> randomBlockList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<Integer> blockList = Arrays.asList(jBlock, lBlock, zBlock, sBlock, tBlock, oBlock, iBlock);
            Collections.shuffle(blockList);
            randomBlockList.addAll(blockList);
        }

        // easy mode
        if (mode == 1) {
            randomBlockList.add(iBlock);
        }

        // hard mode
        else if (mode == 2) {
            randomBlockList.addAll(Arrays.asList(jBlock, lBlock, zBlock, sBlock, tBlock, oBlock));
        }
        blockDeque.addAll(randomBlockList);
    }

    private Block getBlock(int id) {
        switch (id) {
            case Block.IBLOCK_IDENTIFY_NUMBER:
                return new IBlock();
            case Block.JBLOCK_IDENTIFY_NUMBER:
                return new JBlock();
            case Block.LBLOCK_IDENTIFY_NUMBER:
                return new LBlock();
            case Block.OBLOCK_IDENTIFY_NUMBER:
                return new OBlock();
            case Block.SBLOCK_IDENTIFY_NUMBER:
                return new SBlock();
            case Block.TBLOCK_IDENTIFY_NUMBER:
                return new TBlock();
            case Block.ZBLOCK_IDENTIFY_NUMBER:
                return new ZBlock();
            default:
                return new IBlock();
        }
    }

    public void drawGameBoard() {
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < BOARD_WIDTH + 2; t++) {
            sb.append(BattleModeView.BORDER_CHAR);
        }
        sb.append("\n");
        for (int i = BOARD_START_HEIGHT; i < BOARD_END_HEIGHT; i++) {
            sb.append(GameView.BORDER_CHAR);
            for (int j = 0; j < BOARD_WIDTH; j++) {
                sb.append(blockCharMap.get(visualBoard[i][j])); // currentBlock 의 모양을 그려준다.
            }
            sb.append(BattleModeView.BORDER_CHAR);
            sb.append("\n");
        }
        for (int t = 0; t < BOARD_WIDTH + 2; t++) {
            sb.append(BattleModeView.BORDER_CHAR);
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
            }
        }

    }

    private void paintBlock() {
        StyledDocument doc = gamePane.getStyledDocument();
        SimpleAttributeSet blockAttributeSet = new SimpleAttributeSet();
        for (int i = BOARD_START_HEIGHT; i < BOARD_END_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (visualBoard[i][j] > 0) {
                    StyleConstants.setForeground(blockAttributeSet, colorMap.get(visualBoard[i][j]));
                    doc.setCharacterAttributes(
                        (BOARD_WIDTH + 4) + (i - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + j, 1,
                        blockAttributeSet, false);
                }
            }
        }
    }

    // 주어진 board에 Block을 놓아주는 메소드
    private void placeBlock(int[][] board, int[][] visualBoard, Block block, int x, int y) {
        getGhostY();
        block.getCoordiList().forEach(e -> {
            board[y + e[1]][x + e[0]] += 1;
            visualBoard[ghostY + e[1]][x + e[0]] = Block.GHOST_IDENTIFIY_NUMBER;
        });
        // foreach는 병렬적으로 연산하므로 순서대로 하기 위해서 이리함
        block.getCoordiList().forEach(e -> visualBoard[y + e[1]][x + e[0]] = block.getVisualShape(e[0], e[1]));
    }

    // 주어진 board에 Block을 놓아주는 메소드(오버로딩)
    private void placeBlock(int[][] board, Block block, int x, int y) {
        block.getCoordiList().forEach(e -> board[y + e[1]][x + e[0]] += 1);
    }

    // board에서 블록을 지워주는 method
    private void eraseBlock(int[][] board, Block block) {
        try {
            block.getCoordiList().forEach(e -> {
                board[y + e[1]][x + e[0]] = 0;
                visualBoard[y + e[1]][x + e[0]] = 0;
                visualBoard[ghostY + e[1]][x + e[0]] = 0;
            });
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected void moveDown() {

        if (isBottomFlag) {
            lockDelay();

            return;
        }
        eraseBlock(board, currentBlock);
        y++;
        placeBlock(board, visualBoard, currentBlock, x, y);
        showCurrent(board, currentBlock);

        gamePane.revalidate();
        gamePane.repaint();
        score += (201 - delay / 5);
        showScore();
        isBottomFlag = checkIsItBottom();
        if (isBottomFlag) {
            stopGameDelayTimer();
            startGameDelayTimer(LOCK_DELAY_TIME);
        }
    }

    // Ghost piece의 Y좌표 구하는 메소드
    private void getGhostY() {
        if (currentBlock == null)
            return;
        int limit = BOARD_END_HEIGHT - currentBlock.getHeight();
        for (ghostY = y; ghostY < limit; ghostY++) {
            if (checkBlockCollision(x, ghostY)) {
                ghostY--;
                return;
            }
        }
        if (checkBlockCollision(x, ghostY))
            ghostY--;

    }

    public void moveRight() {
        eraseBlock(board, currentBlock);
        if (currentBlock == null) {
            return;
        }
        if (x < BOARD_WIDTH - currentBlock.getWidth()) {
            x++;
            if (checkBlockCollision(x, y))
                x--;
        }
        placeBlock(board, visualBoard, currentBlock, x, y);
        isBottomFlag = checkIsItBottom();
    }

    public void moveLeft() {
        eraseBlock(board, currentBlock);
        if (currentBlock == null) {
            return;
        }
        if (x > 0) {
            x--;
            if (checkBlockCollision(x, y))
                x++;
        }
        placeBlock(board, visualBoard, currentBlock, x, y);
        isBottomFlag = checkIsItBottom();
    }

    public void moveRotate() {
        eraseBlock(board, currentBlock);
        testRotation();
        placeBlock(board, visualBoard, currentBlock, x, y);
        boolean flag = checkIsItBottom();
        if (isBottomFlag || flag) {
            stopGameDelayTimer();
            startGameDelayTimer(delay);
            isBottomFlag = flag;
        }
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
            if (ifBlockOutOfBounds(x, y) || checkBlockCollision(x, y)) {
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
        if (ifBlockOutOfBounds(x, y) || checkBlockCollision(x, y)) {
            x = xBeforeRotate;
            y = yBeforeRotate;
            blockBuffer.copyBlock(currentBlock);
        } else {
            currentBlock.rotate();
            blockBuffer.copyBlock(currentBlock);
        }

    }

    // Block이 바닥에 닿는지 확인
    public boolean checkIsItBottom() {
        if (y == BOARD_END_HEIGHT - currentBlock.getHeight())
            return true;

        int width = currentBlock.getWidth();
        int height = currentBlock.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[y + j + 1][x + i] > 1 && board[y + j][x + i] == 1) {
                    return true;
                }
            }
        }
        return false;
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
    private boolean checkBlockCollision(int x, int y) {
        copyBoard(board, boardBuffer);
        placeBlock(boardBuffer, blockBuffer, x, y);
        for (int j = 0; j < blockBuffer.getHeight(); j++) {
            for (int i = 0; i < blockBuffer.getWidth(); i++) {
                if (boardBuffer[y + j][x + i] > 2) {
                    System.out.println("충돌발생");
                    return true;
                }
            }
        }
        return false;
    }

    // Block이 경계를 넘는지 확인
    private boolean ifBlockOutOfBounds(int x, int y) {
        boolean flag = false;
        if (x < 0 || y < 0) { // 왼쪽 위 아래 경게 확인
            flag = true;
            return flag;
        } else if (x + blockBuffer.getWidth() > BOARD_WIDTH
            || y + blockBuffer.getHeight() > BOARD_END_HEIGHT)
            flag = true;
        return flag;
    }

    // 게임오버플래그 설정
    private boolean isGameOver() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            if (board[BOARD_START_HEIGHT - 1][i] > 0)
                return true;
        }
        return false;
    }

    // 삭제줄 복사 메소드
    private void drawAttackLine(int lines) {

        if (attackLines > (BOARD_HEIGHT) / 2)
            return;

        // 만일 이번에 들어오는 줄로 공격할 줄이 10개를 넘어선다면
        if (attackLines + lines > 10)
            lines = 10 - attackLines;

        System.out.println("현재스택 사이즈: " + attackLinesDeque.size());

        // 먼저 이미 공격 줄이 있다면 미리 스택에 넣어두고
        if (attackLines > 0) {
            for (int i = BOARD_HEIGHT - attackLines; i < BOARD_HEIGHT; i++)
                attackLinesDeque.push(attackLineBoard[i]);
        }

        // 공격할 줄을 만들고 (구멍을 만드는 과정)
        int[][] temp = new int[MAX_BLOCK_HEIGHT][BOARD_WIDTH];
        for (int j = 3; j > 3 - lines; j--) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                temp[j][i] = 2;
            }
        }
        placeBlock(temp, currentBlock, x, MAX_BLOCK_HEIGHT - currentBlock.getHeight());
        showCurrent(temp, currentBlock);

        // 구멍난 공격줄을 stack에 넣어주고 4-> 테트리미노 블록개수
        for (int j = 0; j < MAX_BLOCK_HEIGHT; j++) {
            if (Arrays.stream(temp[j]).sum() > 20) {
                attackLinesDeque.push(Arrays.stream(temp[j]).map(e -> e % 3).toArray());
            }
        }

        // stack에서 공격할 줄을 board에 넣어준다.
        int size = attackLinesDeque.size();
        for (int i = BOARD_HEIGHT - 1; i > BOARD_HEIGHT - 1
            - size; i--) {
            attackLineBoard[i] = Arrays.copyOf(attackLinesDeque.pop(), BOARD_WIDTH);
        }

        // 그리고 그걸 attackLinePane에 표현한다.
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < BOARD_HEIGHT; j++) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                sb.append(blockCharMap.get(attackLineBoard[j][i]));
            }
            sb.append("\n");
        }
        attackLinePane.setText(sb.toString());
        StyleConstants.setFontSize(attackBoardAttributeSet, 20);

        StyledDocument doc = attackLinePane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), attackBoardAttributeSet, false);
        attackLines += lines;
    }

    // 블럭 줄삭제
    private boolean clearLine() {
        boolean existFullyLine = false;
        int fullyLines = 0;
        int startindex = -1;
        for (int i = 0; i < BOARD_END_HEIGHT; i++) {
            int sum = Arrays.stream(board[i]).sum();
            if (sum > 19) {
                startindex = i;
                fullyLines++;
                existFullyLine = true;
            }
        }
        if (fullyLines > 0) {
            startindex = startindex - fullyLines + 1;
            drawAttackLine(fullyLines);
            launchDeleteLineAnimation(startindex, fullyLines);
            delay -= delay > 250 ? 12 - 2 * diffMode % 2 : 0;
            score += 25;
            showScore();
        }
        return existFullyLine;
    }

    // 줄삭제 애니메이션
    private void launchDeleteLineAnimation(int index, int lines) {
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
        StyleConstants.setForeground(blockAttributeSet, color);
        for (int i = 0; i < lines; i++) {
            doc.setCharacterAttributes(
                (BOARD_WIDTH + 4) + (index + i - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3), BOARD_WIDTH,
                blockAttributeSet, true);
        }
    }

    // 폭발 애니메이션
    private void launchExplosionAnimation() {
        stopGameDelayTimer();
        gamePane.removeKeyListener(gameKeyListener);
        Timer aniTimer;
        int count = 0;
        int aniDelay = ANIMATION_INTERVAL;
        int xbuffer = currentBlock.getItemCoordinate()[0] + x;
        int ybuffer = currentBlock.getItemCoordinate()[1] + y;
        placeSquare(xbuffer, ybuffer);
        drawGameBoard();
        for (count = 0; count < 10; count++) {
            if (count % 2 == 0)
                aniTimer = new Timer(count * aniDelay, e -> paintSquare(xbuffer, ybuffer, Color.RED));
            else
                aniTimer = new Timer(count * aniDelay, e -> paintSquare(xbuffer, ybuffer, Color.YELLOW));
            aniTimer.setRepeats(false);
            aniTimer.start();
        }
        int totaldelay = count * aniDelay;
        aniTimer = new Timer(totaldelay, e -> {
            deleteSquare(xbuffer, ybuffer);
            fixBoard();
            drawGameBoard();
            takeOutNextBlock();
            isBottomFlag = checkBlockCollision(x, y);
            gamePane.addKeyListener(gameKeyListener);
        });
        aniTimer.setRepeats(false);
        aniTimer.start();
        startGameDelayTimer(totaldelay);
    }

    // 사각형 색칠 메소드
    private void paintSquare(int x, int y, Color color) {
        StyledDocument doc = gamePane.getStyledDocument();
        SimpleAttributeSet blockAttributeSet = new SimpleAttributeSet();
        StyleConstants.setForeground(blockAttributeSet, color);
        int bombLength = ((BOMB_RANGE - 1) / 2);
        int xbuffer = x - bombLength;
        int ybuffer = y - bombLength;
        int offset = (BOARD_WIDTH + 4) + ((ybuffer - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3)) + xbuffer;
        for (int i = 0; i < BOMB_RANGE; i++) {
            if (ybuffer + i < BOARD_END_HEIGHT) {
                for (int j = 0; j < BOMB_RANGE; j++) {
                    if (xbuffer + j > -1 && xbuffer + j < BOARD_WIDTH) {
                        doc.setCharacterAttributes(offset + i * (BOARD_WIDTH + 3) + j, 1, blockAttributeSet, true);
                    }
                }
            }
        }

    }

    private void placeSquare(int x, int y) {
        x -= ((BOMB_RANGE - 1) / 2);
        y -= ((BOMB_RANGE - 1) / 2);
        for (int i = 0; i < BOMB_RANGE; i++) {
            if (y + i < BOARD_END_HEIGHT)
                for (int j = 0; j < BOMB_RANGE; j++) {
                    if (x + j > -1 && x + j < BOARD_WIDTH) {
                        visualBoard[y + i][x + j] = Block.BOMBBLOCK_IDENTIFY_NUMBER;
                    }
                }
        }
    }

    private void deleteSquare(int x, int y) {
        x -= ((BOMB_RANGE - 1) / 2);
        y -= ((BOMB_RANGE - 1) / 2);
        for (int i = 0; i < BOMB_RANGE; i++) {
            if (y + i < BOARD_END_HEIGHT)
                for (int j = 0; j < BOMB_RANGE; j++) {
                    if (x + j > -1 && x + j < BOARD_WIDTH) {
                        board[y + i][x + j] = 0;
                        visualBoard[y + i][x + j] = 0;
                    }
                }
        }
    }

    // 바닥 도달시
    private void lockDelay() {
        if (gameMode == 1) {
            if (currentBlock.getIdentifynumber() == Block.WEIGHTBLOCK_IDENTIFY_NUMBER)
                ifIsweightBlock(board, visualBoard, currentBlock);
            if (currentBlock.getAttachItemID() == Block.BOMBBLOCK_IDENTIFY_NUMBER) {
                launchExplosionAnimation();
                return;
            }
        }
        copyBoard(board, boardBuffer);
        fixBoard();
        if (!clearLine()) {
            takeOutNextBlock();
            stopGameDelayTimer();
            startGameDelayTimer(delay);
        }
        isBottomFlag = false;
    }

    // 다음 블록 놓기
    private void takeOutNextBlock() {
        if (isGameOver()) {
            showESCMessage();
            gameOverText.setVisible(true); // Game Over 글자를 나타냄
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
            transitView(contentPane, scoreView, battleModeView);
        }
        currentBlock.copyBlock(nextBlock);
        blockBuffer.copyBlock(currentBlock);
        if (blockDeque.isEmpty()) {
            generateBlockRandomizer(diffMode);
        }
        nextBlock = getBlock(blockDeque.removeFirst());
        drawNextBlock();
        x = START_X;
        y = START_Y;
        getGhostY();
        // 시작위치에서 충돌날 시 위로 한 칸 올린다.
        if (checkBlockCollision(x, y))
            y--;
        placeBlock(board, visualBoard, currentBlock, x, y);
        drawGameBoard();

    }

    /* 아이템블록 구현 메소드 */
    // 무게추
    private void ifIsweightBlock(int[][] board, int[][] visualBoard, Block block) {
        int yTemp = BOARD_END_HEIGHT - block.getHeight();
        for (int j = 0; j < BOARD_END_HEIGHT; j++) {
            for (int i = 0; i < block.getWidth(); i++) {
                board[j][x + i] = 0;
                visualBoard[j][x + i] = 0;
            }
        }
        placeBlock(board, visualBoard, block, x, yTemp);
    }

    // 게임 중단 상태에서 다시 실행하는 경우
    public void restart() {
        board = new int[BOARD_END_HEIGHT][BOARD_WIDTH];
        x = START_X;
        y = START_Y;
        blockDeque = new ArrayDeque<>();
        generateBlockRandomizer(diffMode);
        blockBuffer = getBlock(blockDeque.getFirst());
        currentBlock = getBlock(blockDeque.removeFirst());
        nextBlock = getBlock(blockDeque.removeFirst());

        placeBlock(board, visualBoard, currentBlock, x, y);
        drawGameBoard();
        drawNextBlock();
    }

    // ESC 키를 누를 경우 게임 메세지를 출력
    public void showESCMessage() {
        int inputValue = JOptionPane.showConfirmDialog(battleModeView, "Do you want to end the game?",
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
            this.upKey = setting.getRotate2Key();
            this.downKey = setting.getMoveDown2Key();
            this.leftKey = setting.getMoveLeft2Key();
            this.rightKey = setting.getMoveRight2Key();
            this.stackKey = setting.getStack2Key();
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
            /*for (Component comp : battleModeView.getSelectDiffPane().getComponents())
                gameKeyMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            gameKeyMap.put(new KeyPair(leftKey, battleModeView.getEasyBtn()),
                () -> battleModeView.getHardBtn().requestFocus(true));*/
            for (Component comp : battleModeView.getSelectBattleModePane().getComponents())
                gameKeyMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            gameKeyMap.put(new KeyPair(leftKey, battleModeView.getGeneralModeBtn()),
                () -> battleModeView.getTimeAttackBtn().requestFocus(true));
            gameKeyMap.put(new KeyPair(leftKey, gamePane), () -> {
                moveLeft();
                drawGameBoard();
            });
        }

        private void initRightKey() {
            /*for (Component comp : gameView.getSelectDiffPane().getComponents())
                gameKeyMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            gameKeyMap.put(new KeyPair(rightKey, gameView.getHardBtn()),
                () -> gameView.getEasyBtn().requestFocus(true));*/
            for (Component comp : battleModeView.getSelectBattleModePane().getComponents())
                gameKeyMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            gameKeyMap.put(new KeyPair(rightKey, battleModeView.getTimeAttackBtn()),
                () -> battleModeView.getGeneralModeBtn().requestFocus(true));
            gameKeyMap.put(new KeyPair(rightKey, gamePane), () -> {
                moveRight();
                drawGameBoard();
            });
        }

        private void initStackKey() {
            gameKeyMap.put(new KeyPair(stackKey, gamePane), () -> {
                eraseBlock(board, currentBlock);
                y = ghostY;
                placeBlock(board, visualBoard, currentBlock, x, y);
                drawGameBoard();
                if (!isBottomFlag) {
                    isBottomFlag = true;
                    stopGameDelayTimer();
                    startGameDelayTimer(LOCK_DELAY_TIME);
                }
                // moveDown(); hard drop 적용
            });
           /* gameKeyMap.put(new KeyPair(stackKey, battleModeView.getEasyBtn()), () -> {
                diffMode = 1;
                transitView(battleModeView, battleModeView.getGameDisplayPane(), battleModeView.getSelectDiffPane());
                startGame();
            });
            gameKeyMap.put(new KeyPair(stackKey, battleModeView.getNormalBtn()), () -> {
                diffMode = 0;
                transitView(battleModeView, battleModeView.getGameDisplayPane(), battleModeView.getSelectDiffPane());
                startGame();
            });
            gameKeyMap.put(new KeyPair(stackKey, battleModeView.getHardBtn()), () -> {
                diffMode = 2;
                transitView(battleModeView, battleModeView.getGameDisplayPane(), battleModeView.getSelectDiffPane());
                startGame();
            });*/

            // 이 부분 검토 필요
            gameKeyMap.put(new KeyPair(stackKey, battleModeView.getGeneralModeBtn()), () -> {
                gameMode = 0;
                transitView(battleModeView, battleModeView.getBattleModeDisplayPane(), battleModeView.getSelectBattleModePane());
                startGame();
            });
            gameKeyMap.put(new KeyPair(stackKey, battleModeView.getItemModeBtn()), () -> {
                gameMode = 1;
                transitView(battleModeView, battleModeView.getBattleModeDisplayPane(), battleModeView.getSelectBattleModePane());
                startGame();
            });
            gameKeyMap.put(new KeyPair(stackKey, battleModeView.getTimeAttackBtn()), () -> {
                gameMode = 2;
                transitView(battleModeView, battleModeView.getBattleModeDisplayPane(), battleModeView.getSelectBattleModePane());
                startGame();
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
        /*for (Component comp : battleModeView.getSelectDiffPane().getComponents())
            comp.addKeyListener(gameKeyListener);*/
        for (Component comp : battleModeView.getSelectBattleModePane().getComponents())
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
        int[][] copy = new int[index][BOARD_WIDTH];
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
        for (int i = BOARD_START_HEIGHT; i < BOARD_END_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
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
        battleModeView.getPlayerTwoScorePane().setText(String.format("%d", score));
    }

    private void showTime() {
        battleModeView.getPlayerTwoTimePane().setText(String.format("%d", gameTime));
    }
}