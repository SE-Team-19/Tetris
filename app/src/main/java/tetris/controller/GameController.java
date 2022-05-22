package tetris.controller;

import java.awt.*;
import java.awt.event.*;
import java.util.logging.Logger;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.*;
import java.util.*;

import tetris.model.*;
import tetris.view.*;

public abstract class GameController {

    private final Logger log = Logger.getGlobal();

    private static final int ANIMATION_INTERVAL = 50;
    private static final int LOCK_DELAY_TIME = 500;
    private static final int BOARD_START_HEIGHT = 5;
    private static final int BOARD_END_HEIGHT = GameView.BORDER_HEIGHT + BOARD_START_HEIGHT;
    private static final int BOARD_HEIGHT = BOARD_END_HEIGHT - BOARD_START_HEIGHT;
    private static final int BOARD_WIDTH = GameView.BORDER_WIDTH;
    private static final int START_X = 3;
    private static final int START_Y = BOARD_START_HEIGHT - 1;
    private static final int BOMB_RANGE = 5; // 홀수만 가능
    private static final int MAX_BLOCK_HEIGHT = 4;
    private static final int UNFIXED_BLOCK_NUMBER = 1;
    private static final int FIXED_BLOCK_NUMBER = 2;
    private static final int OVERLAP_BLOCK_NUMBER = UNFIXED_BLOCK_NUMBER + FIXED_BLOCK_NUMBER;
    private static final int FULL_LINE = FIXED_BLOCK_NUMBER * BOARD_WIDTH;
    public static final int EASY_MODE = 1;
    public static final int NORMAL_MODE = 0;
    public static final int HARD_MODE = 2;
    public static final int GENERAL_GAME_MODE = 0;
    public static final int ITEM_GAME_MODE = 1;
    public static final int TIME_ATTACK_MODE = 2;

    private Timer gameDelayTimer;
    private boolean isBottomFlag;
    private boolean isItemFlag;
    private int delay;
    private int diffMode; // 난이도 설정
    private int gameMode; // 게임모드 설정
    private Block currentBlock;
    private Block nextBlock;
    private Block blockBuffer;

    protected int[][] visualBoard;
    protected int[][] board; // gamePane 의 'X' size를 결정하기 위한 변수
    private int[][] boardBuffer;
    protected int[][] attackLineBoard;
    protected Deque<Integer> blockDeque;
    protected Deque<Integer> opponentBlockDeque;
    private Deque<int[]> attackLinesDeque;
    private Stack<int[]> attackLinesStack;

    private int x;
    private int y;
    private int ghostY;
    protected int score; // game 점수와 관련된 변수
    protected int attackLines;
    private int deleteLines;

    private GameView gameView = GameView.getInstance();
    private GameController opponent;
    private JTextPane gamePane;
    private JTextPane nextBlockPane;
    private JTextPane attackLinePane;
    private JLabel scoreLabel;
    private SimpleAttributeSet boardAttributeSet;
    private SimpleAttributeSet nextBoardAttributeSet;
    private SimpleAttributeSet attackBoardAttributeSet;
    private Component focusing;
    KeyListener gameKeyListener;

    private InitGameKeyMap initGameKeyMap;
    private Map<KeyPair, Runnable> gameKeyMap;
    private Map<Integer, Color> colorMap;
    private Map<Integer, Runnable> rotateMap;
    private Map<Integer, Character> blockCharMap;
    private List<List<WallKick>> wallKickList;

    private boolean isColorBlindMode;

    protected GameController(JTextPane gamePane, JTextPane nextBlockPane, JTextPane attackLinePane,
            JLabel scoreLabel,
            Component focusing) {
        this.gamePane = gamePane;
        this.nextBlockPane = nextBlockPane;
        this.attackLinePane = attackLinePane;
        this.scoreLabel = scoreLabel;
        this.focusing = focusing;
        initGameController();
        // setDisplayWidth();
    }

    private void initGameController() {
        diffMode = 0;
        gameMode = 0;
        isColorBlindMode = false;
        board = new int[BOARD_END_HEIGHT][BOARD_WIDTH];
        boardBuffer = new int[BOARD_END_HEIGHT][BOARD_WIDTH];
        visualBoard = new int[BOARD_END_HEIGHT][BOARD_WIDTH];
        attackLineBoard = new int[BOARD_HEIGHT][BOARD_WIDTH];
        attackLinesDeque = new ArrayDeque<>();
        blockDeque = new ArrayDeque<>();
        x = START_X;
        y = START_Y;

        initBlockCharMap();
        initColorMap();
        initRotateMap();
        initWallKickList();

        gameKeyMap = new HashMap<>();
        initGameKeyMap = new InitGameKeyMap();
        addGameKeyListener();

        boardAttributeSet = new SimpleAttributeSet();
        nextBoardAttributeSet = new SimpleAttributeSet();
        attackBoardAttributeSet = new SimpleAttributeSet();

        setAttributeSet(boardAttributeSet);
        setAttributeSet(nextBoardAttributeSet);
        setAttributeSet(attackBoardAttributeSet);
    }

    public void setOpponentPlayer(GameController opponent) {
        this.opponent = opponent;
        this.attackLines = opponent.attackLines;
        this.opponentBlockDeque = opponent.blockDeque;
        this.attackLineBoard = opponent.attackLineBoard;
    }

    public void startGame(int diffMode, int gameMode, List<Integer> randomBlockList) {
        this.diffMode = diffMode;
        this.gameMode = gameMode;

        blockDeque.addAll(randomBlockList);
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
        deleteLines = 0;
        isItemFlag = false;

        showScore();
        startGameDelayTimer(delay);
    }

    public void setPlayerKeys(int upKey, int downKey, int leftKey, int rightKey, int stackKey) {
        initGameKeyMap.setAllKey(upKey, downKey, leftKey, rightKey, stackKey);
    }

    public void loadSetting(Setting setting) {
        isColorBlindMode = setting.isColorBlindMode();
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
        blockCharMap.put(Block.GHOST_IDENTIFIY_NUMBER, GameView.GHOST_CHAR);
        blockCharMap.put(Block.BOMBBLOCK_IDENTIFY_NUMBER, GameView.BOMB_CHAR);
        blockCharMap.put(Block.WEIGHTBLOCK_IDENTIFY_NUMBER, GameView.BLOCK_CHAR);
        blockCharMap.put(Block.ONELINEBLOCK_IDENTIFY_NUMBER, GameView.ONELINE_CHAR);
        blockCharMap.put(Block.NULL_IDENTIFY_NUMBER, GameView.NULL_CHAR);
        blockCharMap.put(Block.ATTACK_BLOCK_IDENTIFY_NUMBER, GameView.BLOCK_CHAR);
    }

    private void initColorMap() {
        colorMap = new HashMap<>();
        colorMap.put(Block.ONELINEBLOCK_IDENTIFY_NUMBER, Color.WHITE);
        colorMap.put(Block.NULL_IDENTIFY_NUMBER, Color.WHITE);
        colorMap.put(Block.GHOST_IDENTIFIY_NUMBER, new Color(200, 200, 200));
        colorMap.put(Block.BOMBBLOCK_IDENTIFY_NUMBER, Color.RED);
        colorMap.put(Block.ATTACK_BLOCK_IDENTIFY_NUMBER, Color.GRAY);
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
                Arrays.asList(new WallKick(-1, 0), new WallKick(-1, -1), new WallKick(0, 2),
                        new WallKick(-1, 2))));
        wallKickList.add(new ArrayList<>( // 1 >> 2
                Arrays.asList(new WallKick(1, 0), new WallKick(1, 1), new WallKick(0, -2),
                        new WallKick(1, -2))));
        wallKickList.add(new ArrayList<>( // 2 >> 3
                Arrays.asList(new WallKick(1, 0), new WallKick(1, -1), new WallKick(0, 2),
                        new WallKick(1, 2))));
        wallKickList.add(new ArrayList<>( // 3 >> 0
                Arrays.asList(new WallKick(-1, 0), new WallKick(-1, 1), new WallKick(0, -2),
                        new WallKick(-1, -2))));
        /* IBlock testcase */
        wallKickList.add(new ArrayList<>( // 0 >> 1
                Arrays.asList(new WallKick(-2, 0), new WallKick(1, 0), new WallKick(-2, 1),
                        new WallKick(1, -2))));
        wallKickList.add(new ArrayList<>( // 1 >> 2
                Arrays.asList(new WallKick(-1, 0), new WallKick(2, 0), new WallKick(-1, -2),
                        new WallKick(2, 1))));
        wallKickList.add(new ArrayList<>( // 2 >> 3
                Arrays.asList(new WallKick(2, 0), new WallKick(-1, 0), new WallKick(2, -1),
                        new WallKick(-1, 2))));
        wallKickList.add(new ArrayList<>( // 3 >> 0
                Arrays.asList(new WallKick(1, 0), new WallKick(-2, 0), new WallKick(1, 2),
                        new WallKick(-2, -1))));
    }

    public void startGameDelayTimer(int startDelay) {
        gameDelayTimer = new Timer(startDelay, e -> {
            moveDown();
            drawGameBoard();
            delay -= delay > 250 ? 1 : 0;
            gameDelayTimer.setDelay(delay);
        });
        gameDelayTimer.start();
    }

    private void setAttributeSet(SimpleAttributeSet attributeSet) {
        // 1366 * 768
        if (ViewController.screenWidthNum == 0) {
            StyleConstants.setFontSize(attributeSet, 53);
            StyleConstants.setFontFamily(attributeSet, "Courier New");
            StyleConstants.setBold(attributeSet, true);
            StyleConstants.setForeground(attributeSet, Color.WHITE);
            StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_CENTER);
            StyleConstants.setLeftIndent(attributeSet, -85);
            StyleConstants.setRightIndent(attributeSet, -85);
            StyleConstants.setLineSpacing(attributeSet, -0.45f);
            StyleConstants.setSpaceAbove(attributeSet, -3.5f);
            System.out.println(
                    "GameController is " + ViewController.screenWidthNum); // test용. 추후 지울 것
        }
        // 1400 * 1050
        else if (ViewController.screenWidthNum == 1) {
            StyleConstants.setFontSize(attributeSet, 53);
            StyleConstants.setFontFamily(attributeSet, "Courier New");
            StyleConstants.setBold(attributeSet, true);
            StyleConstants.setForeground(attributeSet, Color.WHITE);
            StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_CENTER);
            StyleConstants.setLeftIndent(attributeSet, -85);
            StyleConstants.setRightIndent(attributeSet, -85);
            StyleConstants.setLineSpacing(attributeSet, -0.23f);
            StyleConstants.setSpaceAbove(attributeSet, -3.5f);
            System.out.println(
                    "GameController is " + ViewController.screenWidthNum); // test용. 추후 지울 것
        }
        // 1600 * 900
        else {
            StyleConstants.setFontSize(attributeSet, 60);
            StyleConstants.setFontFamily(attributeSet, "Courier New");
            StyleConstants.setBold(attributeSet, true);
            StyleConstants.setForeground(attributeSet, Color.WHITE);
            StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_CENTER);
            StyleConstants.setLeftIndent(attributeSet, -105);
            StyleConstants.setRightIndent(attributeSet, -105);
            StyleConstants.setLineSpacing(attributeSet, -0.42f);
            StyleConstants.setSpaceAbove(attributeSet, -3.5f);
            System.out.println(
                    "GameController is " + ViewController.screenWidthNum); // test용. 추후 지울 것
        }

    }

    public void drawGameBoard() {
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < BOARD_WIDTH + 2; t++) {
            sb.append(GameView.BORDER_CHAR);
        }

        sb.append("\n");
        for (int i = BOARD_START_HEIGHT; i < BOARD_END_HEIGHT; i++) {
            sb.append(GameView.BORDER_CHAR);
            for (int j = 0; j < BOARD_WIDTH; j++) {
                sb.append(blockCharMap.get(visualBoard[i][j])); // currentBlock 의 모양을 그려준다.
            }
            sb.append(GameView.BORDER_CHAR);
            sb.append("\n");
        }

        for (int t = 0; t < BOARD_WIDTH + 2; t++) {
            sb.append(GameView.BORDER_CHAR);
        }
        gamePane.setText(sb.toString());

        StyledDocument doc = gamePane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), boardAttributeSet, true);
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
                StyleConstants.setForeground(blockAttributeSet,
                        colorMap.get(nextBlock.getVisualShape(i, j)));
                doc.setCharacterAttributes(i + j + j * nextWidth, 1, blockAttributeSet, false);
            }
        }

    }

    Block getBlock(int id) {
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

    private void paintBlock() {
        StyledDocument doc = gamePane.getStyledDocument();
        SimpleAttributeSet blockAttributeSet = new SimpleAttributeSet();
        for (int i = BOARD_START_HEIGHT; i < BOARD_END_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (visualBoard[i][j] > 0) {
                    StyleConstants.setForeground(blockAttributeSet,
                            colorMap.get(visualBoard[i][j]));
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
        block.getCoordiList()
                .forEach(e -> visualBoard[y + e[1]][x + e[0]] = block.getVisualShape(e[0], e[1]));
    }

    // 주어진 board에 Block을 놓아주는 메소드(오버로딩)
    private void placeBlock(int[][] board, Block block, int x, int y) {
        block.getCoordiList().forEach(e -> board[y + e[1]][x + e[0]] += 1);
    }

    // board에서 블록을 지워주는 method
    private void eraseBlock(int[][] board, Block block) {
        block.getCoordiList().forEach(e -> {
            board[y + e[1]][x + e[0]] = 0;
            visualBoard[y + e[1]][x + e[0]] = 0;
            visualBoard[ghostY + e[1]][x + e[0]] = 0;
        });
    }

    protected void moveDown() {

        if (isBottomFlag) {
            lockDelay();
            return;
        }
        eraseBlock(board, currentBlock);
        y++;
        placeBlock(board, visualBoard, currentBlock, x, y);
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
                if (boardBuffer[y + j][x + i] > FIXED_BLOCK_NUMBER) {
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

    // 게임오버시 발동 메소드
    abstract void doAfterGameOver();

    // 바닥 도달시 발동 메소드
    abstract void doBeforeTakeOutNextBlock();

    // 삭제줄 복사 메소드
    private void drawAttackLine(int lines) {

        if (opponent == null || opponent.attackLines > (BOARD_HEIGHT) / 2)
            return;

        // 만일 이번에 들어오는 줄로 공격할 줄이 10개를 넘어선다면
        if (opponent.attackLines + lines > (BOARD_HEIGHT) / 2)
            lines = (BOARD_HEIGHT) / 2 - attackLines;

        // 먼저 이미 공격 줄이 있다면 미리 스택에 넣어두고
        for (int i = BOARD_HEIGHT - opponent.attackLines; i < BOARD_HEIGHT; i++)
            attackLinesDeque.push(attackLineBoard[i].clone());

        // 공격할 줄을 만들고 (구멍을 만드는 과정)
        int[][] temp = new int[MAX_BLOCK_HEIGHT][BOARD_WIDTH];
        for (int j = 3; j > 3 - lines; j--) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                temp[j][i] = FIXED_BLOCK_NUMBER;
            }
        }
        placeBlock(temp, currentBlock, x, MAX_BLOCK_HEIGHT - currentBlock.getHeight());

        // 구멍난 공격줄을 stack에 넣어주고 4-> 테트리미노 블록개수
        for (int j = 0; j < MAX_BLOCK_HEIGHT; j++) {
            if (Arrays.stream(temp[j]).sum() > FULL_LINE) {
                attackLinesDeque.push(
                        Arrays.stream(temp[j]).map(e -> e % OVERLAP_BLOCK_NUMBER).toArray());
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
        opponent.attackLinePane.setText(sb.toString());
        StyleConstants.setFontSize(attackBoardAttributeSet, 20);

        StyledDocument doc = opponent.attackLinePane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), attackBoardAttributeSet, false);

        // 상대방 공격줄에 라인 추가
        opponent.attackLines += lines;

        log.info("공격줄 추가 후");
        showCurrent(attackLineBoard, currentBlock);
    }

    // 공격받는중
    void underAttack() {
        copyBoard(board, boardBuffer);
        copyBoard(attackLineBoard, board, BOARD_END_HEIGHT - 1);
        copyBoard(boardBuffer, board, BOARD_END_HEIGHT - attackLines - 1);
        for (int i = BOARD_HEIGHT - 1 - attackLines; i < BOARD_HEIGHT; i++) {
            attackLineBoard[i] = Arrays.stream(attackLineBoard[i])
                    .map(e -> e * 6)
                    .toArray();
        }
        copyBoard(visualBoard, boardBuffer);
        copyBoard(attackLineBoard, visualBoard, BOARD_END_HEIGHT - 1);
        copyBoard(boardBuffer, visualBoard, BOARD_END_HEIGHT - attackLines - 1);
        showCurrent(visualBoard, currentBlock);
        attackLines = 0;
        attackLinesDeque.clear();
        initZeroBoard(attackLineBoard);

        // 그리고 그걸 attackLinePane에 표현한다.
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < BOARD_HEIGHT; j++) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                sb.append(" ");
            }
            sb.append("\n");
        }
        attackLinePane.setText(sb.toString());
        StyleConstants.setFontSize(attackBoardAttributeSet, 20);

        StyledDocument doc = attackLinePane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), attackBoardAttributeSet, false);

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
            delay -= delay > 250 ? 5 - 2 * diffMode % 2 : 0;
            score += 25;
            showScore();
        }
        deleteLines += fullyLines;
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
                aniTimer = new Timer(count * aniDelay,
                        e -> paintLines(index, lines, Color.WHITE));
            else
                aniTimer = new Timer(count * aniDelay,
                        e -> paintLines(index, lines, Color.BLACK));
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
                    (BOARD_WIDTH + 4) + (index + i - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3),
                    BOARD_WIDTH,
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
                aniTimer = new Timer(count * aniDelay,
                        e -> paintSquare(xbuffer, ybuffer, Color.RED));
            else
                aniTimer = new Timer(count * aniDelay,
                        e -> paintSquare(xbuffer, ybuffer, Color.YELLOW));
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
                        doc.setCharacterAttributes(offset + i * (BOARD_WIDTH + 3) + j, 1,
                                blockAttributeSet, true);
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
        if (isItemFlag) {
            if (currentBlock.getIdentifynumber() == Block.WEIGHTBLOCK_IDENTIFY_NUMBER) {
                ifIsweightBlock(board, visualBoard, currentBlock);
                isItemFlag = false;
            } else if (currentBlock.getAttachItemID() == Block.BOMBBLOCK_IDENTIFY_NUMBER) {
                launchExplosionAnimation();
                isItemFlag = false;
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

    }

    // 다음 블록 놓기
    private void takeOutNextBlock() {
        if (isGameOver()) {
            doAfterGameOver();
            return;
        }
        doBeforeTakeOutNextBlock();
        currentBlock.copyBlock(nextBlock);
        blockBuffer.copyBlock(currentBlock);
        nextBlock = getBlock(blockDeque.removeFirst());
        if (gameMode == ITEM_GAME_MODE && deleteLines > 10) {
            nextBlock.makeItemBlock();
            deleteLines = 0;
            isItemFlag = true;
        }
        drawNextBlock();
        x = START_X;
        y = START_Y;
        getGhostY();
        // 시작위치에서 충돌날 시 위로 한 칸 올린다.
        if (checkBlockCollision(x, y)) {
            y--;
        }
        placeBlock(board, visualBoard, currentBlock, x, y);
        drawGameBoard();
        isBottomFlag = checkIsItBottom();
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
        blockBuffer = getBlock(blockDeque.getFirst());
        currentBlock = getBlock(blockDeque.removeFirst());
        nextBlock = getBlock(blockDeque.removeFirst());

        placeBlock(board, visualBoard, currentBlock, x, y);
        drawGameBoard();
        drawNextBlock();
    }

    private class InitGameKeyMap {

        int upKey;
        int downKey;
        int leftKey;
        int rightKey;
        int stackKey;
        int up2Key;
        int down2Key;
        int left2Key;
        int right2Key;
        int stack2Key;

        private void initAllKey() {
            initUpKey();
            initDownKey();
            initLeftKey();
            initRightKey();
            initStackKey();
        }

        private void setAllKey(int upKey, int downKey, int leftKey, int rightKey,
                int stackKey) {
            resetMap();
            this.upKey = upKey;
            this.downKey = downKey;
            this.leftKey = leftKey;
            this.rightKey = rightKey;
            this.stackKey = stackKey;
            initAllKey();
        }

        private void resetMap() {
            gameKeyMap.clear();
        }

        private void initUpKey() {
            gameKeyMap.put(new KeyPair(upKey, focusing), () -> {
                moveRotate();
                drawGameBoard();
            });
        }

        private void initDownKey() {
            gameKeyMap.put(new KeyPair(downKey, focusing), () -> {
                moveDown();
                drawGameBoard();
            });
        }

        private void initLeftKey() {
            gameKeyMap.put(new KeyPair(leftKey, focusing), () -> {
                moveLeft();
                drawGameBoard();
            });
        }

        private void initRightKey() {
            gameKeyMap.put(new KeyPair(rightKey, focusing), () -> {
                moveRight();
                drawGameBoard();
            });
        }

        private void initStackKey() {
            gameKeyMap.put(new KeyPair(stackKey, focusing), () -> {
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
        focusing.addKeyListener(gameKeyListener);
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

    void copyBoard(int[][] copy, int[][] paste) {
        for (int i = 0; i < paste.length; i++) {
            paste[i] = Arrays.copyOf(copy[i], paste[i].length);
        }
    }

    void copyBoard(int[][] copy, int[][] paste, int start) {
        int copyIndex = copy.length;
        copyIndex--;
        if (start > paste.length) {
            log.info("입력받은 시작인덱스가 실제 복사받을 인덱스를 넘어섭니다.");
            return;
        }
        for (int i = start; i > -1 && copyIndex > -1; i--, copyIndex--) {
            paste[i] = Arrays.copyOf(copy[copyIndex], paste[i].length);
        }

    }

    private void fixBoard() {
        for (int i = BOARD_START_HEIGHT; i < BOARD_END_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == 1) {
                    board[i][j] = FIXED_BLOCK_NUMBER;
                    if (visualBoard[i][j] == Block.ONELINEBLOCK_IDENTIFY_NUMBER)
                        board[i][j] = FULL_LINE;
                    visualBoard[i][j] = currentBlock.getIdentifynumber();
                }
            }
        }
    }

    public void stopGameDelayTimer() {
        if (gameDelayTimer != null) {
            gameDelayTimer.stop();
        }
    }

    public void endGame() {
        Timer endGameTimer = new Timer(2, e -> {
            stopGameDelayTimer();
            initZeroBoard(board);
            initZeroBoard(visualBoard);
            initZeroBoard(boardBuffer);
            isBottomFlag = false;
            blockDeque.clear();
        });
        endGameTimer.setRepeats(false);
        endGameTimer.start();
    }

    public void stopGame() {
        stopGameDelayTimer();
        initZeroBoard(board);
        initZeroBoard(visualBoard);
        initZeroBoard(boardBuffer);
        isBottomFlag = false;
        blockDeque.clear();
        showCurrent(board, currentBlock);
    }

    private void showCurrent(int[][] board, Block block) {
        String msg = "\n블록현황 x:" + x + " y:" + y + " width:" + block.getWidth() + " height:"
                + block.getHeight() + " rotateCount: " + block.getRotateCount();
        log.info(msg);
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(board[i][j] + " ");
            }
            sb.append('\n');
        }
        msg = sb.toString();
        log.info(msg);
    }

    private void showScore() {
        scoreLabel.setText(String.format("%d", score));
    }

    public void restartGameDelayTimer() {
        gameDelayTimer.restart();
    }
}
