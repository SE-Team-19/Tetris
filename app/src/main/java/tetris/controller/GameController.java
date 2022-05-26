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

public abstract class GameController implements GameMethod {

    static final Logger log = Logger.getGlobal();

    private static final int ANIMATION_INTERVAL = 50;
    private static final int LOCK_DELAY_TIME = 500;
    static final int BOARD_START_HEIGHT = 5;
    static final int BOARD_END_HEIGHT = GameView.BORDER_HEIGHT + BOARD_START_HEIGHT;
    static final int BOARD_HEIGHT = BOARD_END_HEIGHT - BOARD_START_HEIGHT;
    static final int BOARD_WIDTH = GameView.BORDER_WIDTH;
    static final int START_X = 3;
    static final int START_Y = BOARD_START_HEIGHT - 1;
    private static final int BOMB_RANGE = 5; // 홀수만 가능
    private static final int STUFF_RANGE = 3; // 홀수만 가능
    private static final int MAX_BLOCK_HEIGHT = 4;
    private static final int NEXT_BLOCK_WIDTH = 6;
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
    private boolean delayFlag;
    private boolean isBottomFlag;
    private boolean isItemFlag;
    private int delay;
    private int diffMode; // 난이도 설정
    private int gameMode; // 게임모드 설정
    protected Block currentBlock;
    protected Block nextBlock;
    protected Block blockBuffer;

    protected int[][] visualBoard;
    protected int[][] board; // gamePane 의 'X' size를 결정하기 위한 변수
    private int[][] boardBuffer;
    protected int[][] attackLineBoard;
    protected Deque<Integer> blockDeque;
    protected Deque<Integer> opponentBlockDeque;
    private Deque<int[]> attackLinesDeque;

    private StringBuilder boardStringBuilder;
    private StringBuilder nextBlockStringBuilder;
    private StringBuilder attackLinesStringBuilder;
    private StringBuilder attackLineSB;
    int x;
    int y;
    int ghostY;

    protected int score; // game 점수와 관련된 변수
    protected int attackLines;
    private int itemLines;
    int deleteLines;

    private GameController opponent;
    private JTextPane gamePane;
    private JTextPane nextBlockPane;
    private JTextPane attackLinePane;
    private JLabel scoreLabel;
    private JLabel linesLabel;

    private StyledDocument gamePaneDoc;
    private StyledDocument nextBlockPaneDoc;
    private StyledDocument attackLinePaneDoc;
    private StyledDocument opponentAttackLinePaneDoc;

    private SimpleAttributeSet boardAttributeSet;
    private SimpleAttributeSet nextBoardAttributeSet;
    private SimpleAttributeSet attackBoardAttributeSet;
    private SimpleAttributeSet blockAttributeSet;
    private Component focusing;
    KeyListener gameKeyListener;

    private InitGameKeyMap initGameKeyMap;
    private Map<KeyPair, Runnable> gameKeyMap;
    private Map<Integer, Color> colorMap;
    private Map<Integer, Runnable> rotateMap;
    private Map<Integer, Character> blockCharMap;
    private List<List<WallKick>> wallKickList;

    private boolean isColorBlindMode;
    private boolean isNotDropDownState;

    protected GameController(JTextPane gamePane, JTextPane nextBlockPane, JTextPane attackLinePane,
            JLabel scoreLabel,
            Component focusing) {
        this.gamePane = gamePane;
        this.nextBlockPane = nextBlockPane;
        this.scoreLabel = scoreLabel;
        this.focusing = focusing;
        this.attackLinePane = attackLinePane;
        initGameController();
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

        delayFlag = true;

        initBlockCharMap();
        initColorMap();
        initRotateMap();
        initWallKickList();

        gameKeyMap = new HashMap<>();
        initGameKeyMap = new InitGameKeyMap();
        addGameKeyListener();

        gamePaneDoc = gamePane.getStyledDocument();
        nextBlockPaneDoc = nextBlockPane.getStyledDocument();
        attackLinePaneDoc = attackLinePane.getStyledDocument();

        boardStringBuilder = new StringBuilder();
        nextBlockStringBuilder = new StringBuilder();
        attackLinesStringBuilder = new StringBuilder();
        attackLineSB = new StringBuilder();

        boardAttributeSet = new SimpleAttributeSet();
        blockAttributeSet = new SimpleAttributeSet();
        nextBoardAttributeSet = new SimpleAttributeSet();
        attackBoardAttributeSet = new SimpleAttributeSet();

    }

    public void setOpponentPlayer(GameController opponent) {
        this.opponent = opponent;
        this.opponentBlockDeque = opponent.blockDeque;
        this.opponentAttackLinePaneDoc = opponent.attackLinePane.getStyledDocument();
    }

    public void startGame(int diffMode, int gameMode, List<Integer> randomBlockList, int resoultion) {
        this.diffMode = diffMode;
        this.gameMode = gameMode;

        blockDeque.addAll(randomBlockList);
        blockBuffer = getBlock(blockDeque.getFirst());
        currentBlock = getBlock(blockDeque.removeFirst());
        nextBlock = getBlock(blockDeque.removeFirst());

        setAttributeSet(boardAttributeSet);
        setAttributeSet(nextBoardAttributeSet);
        setAttributeSet(attackBoardAttributeSet);
        if (resoultion > 860783) {
            StyleConstants.setFontSize(boardAttributeSet, 45);
            StyleConstants.setFontSize(nextBoardAttributeSet, 35);
            StyleConstants.setFontSize(attackBoardAttributeSet, 21);
        } else if (resoultion > 652463) {
            StyleConstants.setFontSize(boardAttributeSet, 39);
            StyleConstants.setFontSize(nextBoardAttributeSet, 33);
            StyleConstants.setFontSize(attackBoardAttributeSet, 17);
        } else {
            StyleConstants.setFontSize(boardAttributeSet, 32);
            StyleConstants.setFontSize(nextBoardAttributeSet, 30);
            StyleConstants.setFontSize(attackBoardAttributeSet, 14);
        }

        initGameBoard();
        initAttackLines();
        resetNextBlockPane();
        placeBlock(board, visualBoard, currentBlock, x, y);
        drawNextBlock();

        nextBlockPane.repaint();
        nextBlockPane.revalidate();

        score = 0;
        delay = 1000;
        itemLines = 0;
        deleteLines = 0;
        attackLines = 0;
        isItemFlag = false;
        isNotDropDownState = true;
        delayFlag = true;

        showScore();
        startGameDelayTimer(delay);
        doWhenGameStart();
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
        blockCharMap.put(Block.STUFF_BLOCK_IDENTIFY_NUMBER, GameView.STUFF_CHAR);
    }

    private void initColorMap() {
        colorMap = new HashMap<>();
        colorMap.put(Block.ONELINEBLOCK_IDENTIFY_NUMBER, Color.WHITE);
        colorMap.put(Block.NULL_IDENTIFY_NUMBER, Color.WHITE);
        colorMap.put(Block.GHOST_IDENTIFIY_NUMBER, new Color(200, 200, 200));
        colorMap.put(Block.BOMBBLOCK_IDENTIFY_NUMBER, Color.RED);
        colorMap.put(Block.ATTACK_BLOCK_IDENTIFY_NUMBER, Color.GRAY);
        colorMap.put(Block.STUFF_BLOCK_IDENTIFY_NUMBER, Color.PINK);
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
            delay -= delay > 250 ? 1 : 0;
            if (delayFlag)
                gameDelayTimer.setDelay(delay);
            else {
                // gameDelayTimer.setRepeats(false);
                gameDelayTimer.stop();
            }

        });
        /// gameDelayTimer.setRepeats(true);
        gameDelayTimer.start();
    }

    private void setAttributeSet(SimpleAttributeSet attributeSet) {

        // 1366 * 768

        StyleConstants.setFontFamily(attributeSet, "Courier New");
        StyleConstants.setBold(attributeSet, true);
        StyleConstants.setForeground(attributeSet, Color.WHITE);
        StyleConstants.setAlignment(attributeSet, StyleConstants.ALIGN_CENTER);
        StyleConstants.setLeftIndent(attributeSet, -85);
        StyleConstants.setRightIndent(attributeSet, -85);
        StyleConstants.setLineSpacing(attributeSet, -0.45f);
        StyleConstants.setSpaceAbove(attributeSet, -3.5f);
    }

    public void initGameBoard() {
        boardStringBuilder.setLength(0);
        for (int t = 0; t < BOARD_WIDTH + 2; t++) {
            boardStringBuilder.append(GameView.BORDER_CHAR);
        }

        boardStringBuilder.append("\n");
        for (int i = BOARD_START_HEIGHT; i < BOARD_END_HEIGHT; i++) {
            boardStringBuilder.append(GameView.BORDER_CHAR);
            for (int j = 0; j < BOARD_WIDTH; j++) {
                boardStringBuilder.append(blockCharMap.get(visualBoard[i][j])); // currentBlock 의 모양을 그려준다.
            }
            boardStringBuilder.append(GameView.BORDER_CHAR);
            boardStringBuilder.append("\n");
        }

        for (int t = 0; t < BOARD_WIDTH + 2; t++) {
            boardStringBuilder.append(GameView.BORDER_CHAR);
        }
        gamePane.setText(boardStringBuilder.toString());
        StyleConstants.setForeground(blockAttributeSet, Color.WHITE);
        gamePaneDoc.setParagraphAttributes(0, gamePaneDoc.getLength(), boardAttributeSet, true);
        gamePaneDoc.setCharacterAttributes(0, gamePaneDoc.getLength(), blockAttributeSet, true);
        paintBlock();
    }

    public void initAttackLines() {
        attackLinesStringBuilder.setLength(0);
        for (int t = 0; t < BOARD_WIDTH + 2; t++) {
            attackLinesStringBuilder.append(GameView.BORDER_CHAR);
        }

        attackLinesStringBuilder.append("\n");
        for (int i = BOARD_START_HEIGHT; i < BOARD_END_HEIGHT; i++) {
            attackLinesStringBuilder.append(GameView.BORDER_CHAR);
            for (int j = 0; j < BOARD_WIDTH; j++) {
                attackLinesStringBuilder.append(' ');
            }
            attackLinesStringBuilder.append(GameView.BORDER_CHAR);
            attackLinesStringBuilder.append("\n");
        }
        for (int t = 0; t < BOARD_WIDTH + 2; t++) {
            attackLinesStringBuilder.append(GameView.BORDER_CHAR);
        }
        attackLinePane.setText(attackLinesStringBuilder.toString());

        attackLinePaneDoc.setParagraphAttributes(0, attackLinePaneDoc.getLength(), attackBoardAttributeSet, true);

    }

    public void resetNextBlockPane() {

        nextBlockStringBuilder.setLength(0);
        for (int t = 0; t < NEXT_BLOCK_WIDTH + 2; t++) {
            nextBlockStringBuilder.append(GameView.BORDER_CHAR);
        }
        nextBlockStringBuilder.append("\n");
        for (int i = 0; i < NEXT_BLOCK_WIDTH; i++) {
            nextBlockStringBuilder.append(GameView.BORDER_CHAR);
            for (int j = 0; j < NEXT_BLOCK_WIDTH; j++) {
                nextBlockStringBuilder.append(' ');
            }
            nextBlockStringBuilder.append(GameView.BORDER_CHAR);
            nextBlockStringBuilder.append("\n");
        }
        for (int t = 0; t < NEXT_BLOCK_WIDTH + 2; t++) {
            nextBlockStringBuilder.append(GameView.BORDER_CHAR);
        }
        nextBlockPane.setText(nextBlockStringBuilder.toString());
        nextBlockPaneDoc.setParagraphAttributes(0, nextBlockPaneDoc.getLength(), nextBoardAttributeSet, false);

    }

    public void drawNextBlock() {
        currentBlock.getCoordiList().forEach(e -> {
            int offset = (NEXT_BLOCK_WIDTH + 4) + ((e[1] + 2) * (NEXT_BLOCK_WIDTH + 3) + e[0] + 1);
            nextBlockStringBuilder.setCharAt(
                    offset,
                    ' ');
        });
        nextBlock.getCoordiList().forEach(e -> {
            int offset = (NEXT_BLOCK_WIDTH + 4) + ((e[1] + 2) * (NEXT_BLOCK_WIDTH + 3) + e[0] + 1);
            nextBlockStringBuilder.setCharAt(
                    offset,
                    GameView.BLOCK_CHAR);
        });
        if (isItemFlag) {
            int[] itemCoordinate = nextBlock.getItemCoordinate();
            int offset = (NEXT_BLOCK_WIDTH + 4) + ((itemCoordinate[1] + 2) * (NEXT_BLOCK_WIDTH + 3))
                    + itemCoordinate[0] + 1;
            nextBlockStringBuilder.setCharAt(
                    offset,
                    blockCharMap.get(nextBlock.getAttachItemID()));
        }

        nextBlockPane.setText(nextBlockStringBuilder.toString());
        nextBlockPaneDoc.setParagraphAttributes(0, nextBlockPaneDoc.getLength(), nextBoardAttributeSet, false);
        StyleConstants.setForeground(blockAttributeSet, colorMap.get(nextBlock.getIdentifynumber()));
        for (int i = 0; i < NEXT_BLOCK_WIDTH; i++) {
            nextBlockPaneDoc.setCharacterAttributes(
                    (NEXT_BLOCK_WIDTH + 4) + (i * (NEXT_BLOCK_WIDTH + 3)),
                    NEXT_BLOCK_WIDTH,
                    blockAttributeSet,
                    false);
        }
        if (isItemFlag) {
            int[] itemCoordinate = nextBlock.getItemCoordinate();
            StyleConstants.setForeground(blockAttributeSet, colorMap.get(nextBlock.getAttachItemID()));
            int offset = (NEXT_BLOCK_WIDTH + 4) + ((itemCoordinate[1] + 2) * (NEXT_BLOCK_WIDTH + 3))
                    + itemCoordinate[0] + 1;
            nextBlockPaneDoc.setCharacterAttributes(offset, 1, blockAttributeSet, true);
        }

    }

    public void initNextBlockPane() {
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
        for (int i = BOARD_START_HEIGHT; i < BOARD_END_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (visualBoard[i][j] > 0) {
                    StyleConstants.setForeground(blockAttributeSet, colorMap.get(visualBoard[i][j]));
                    gamePaneDoc.setCharacterAttributes(
                            (BOARD_WIDTH + 4) + (i - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + j, 1,
                            blockAttributeSet, true);
                }
            }
        }
        StyleConstants.setForeground(blockAttributeSet, Color.WHITE);
        gamePane.setCharacterAttributes(blockAttributeSet, false);
    }

    // 주어진 board에 Block을 놓아주는 메소드
    void placeBlock(int[][] board, int[][] visualBoard, Block block, int x, int y) {
        getGhostY();
        block.getCoordiList().forEach(e -> {
            int offset = (BOARD_WIDTH + 4) + (ghostY + e[1] - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + x + e[0];
            board[y + e[1]][x + e[0]] += 1;
            visualBoard[ghostY + e[1]][x + e[0]] = Block.GHOST_IDENTIFIY_NUMBER;
            if (offset >= (BOARD_WIDTH + 4))
                boardStringBuilder.setCharAt(
                        offset,
                        blockCharMap.get(Block.GHOST_IDENTIFIY_NUMBER));

        });
        block.getCoordiList()
                .forEach(e -> {
                    int offset = (BOARD_WIDTH + 4) + (y + e[1] - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + x
                            + e[0];
                    visualBoard[y + e[1]][x + e[0]] = block.getVisualShape(e[0], e[1]);
                    if (offset >= (BOARD_WIDTH + 4))
                        boardStringBuilder.setCharAt(
                                (BOARD_WIDTH + 4) + (y + e[1] - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + x + e[0],
                                blockCharMap.get(block.getIdentifynumber()));
                });
        if (currentBlock.getAttachItemID() > 0) {
            int[] itemCoordinate = currentBlock.getItemCoordinate();
            int offset = (BOARD_WIDTH + 4) + (y + itemCoordinate[1] - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + x
                    + itemCoordinate[0];
            if (offset >= (BOARD_WIDTH + 4))
                boardStringBuilder.setCharAt(
                        offset,
                        blockCharMap.get(currentBlock.getAttachItemID()));
        }
        boardStringBuilder.replace(0, 12, "XXXXXXXXXXXX");
        gamePane.setText(boardStringBuilder.toString());
        paintBlock();
    }

    // 주어진 board에 Block을 놓아주는 메소드(오버로딩)
    void placeBlock(int[][] board, Block block, int x, int y) {
        block.getCoordiList().forEach(e -> board[y + e[1]][x + e[0]] += 1);
    }

    // board에서 블록을 지워주는 method
    void eraseBlock(int[][] board, Block block, int x, int y) {
        block.getCoordiList().forEach(e -> {
            board[y + e[1]][x + e[0]] = 0;
            visualBoard[y + e[1]][x + e[0]] = 0;
            visualBoard[ghostY + e[1]][x + e[0]] = 0;
        });
    }

    // board에서 블록을 지워주는 method
    void eraseBlock(int[][] board, Block block, int x, int y, StringBuilder boardStringBuilder) {
        block.getCoordiList().forEach(e -> {
            int offset = (BOARD_WIDTH + 4) + (y + e[1] - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + x + e[0];
            board[y + e[1]][x + e[0]] = 0;
            visualBoard[y + e[1]][x + e[0]] = 0;
            visualBoard[ghostY + e[1]][x + e[0]] = 0;
            if (offset >= (BOARD_WIDTH + 4)) {
                boardStringBuilder.setCharAt(
                        (BOARD_WIDTH + 4) + (y + e[1] - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + x + e[0],
                        ' ');
            }
        });
        block.getCoordiList().forEach(e -> boardStringBuilder.setCharAt(
                (BOARD_WIDTH + 4) + (ghostY + e[1] - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + x + e[0],
                ' '));
        gamePane.setText(boardStringBuilder.toString());
    }

    protected void moveDown() {

        if (isBottomFlag) {
            lockDelay();
            return;
        }
        eraseBlock(board, currentBlock, x, y, boardStringBuilder);
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

    void dropDown() {
        if (isNotDropDownState) {
            eraseBlock(board, currentBlock, x, y, boardStringBuilder);
            score += (201 - delay / 5) * ghostY - y;
            showScore();
            y = ghostY;
            placeBlock(board, visualBoard, currentBlock, x, y);
            if (!isBottomFlag) {
                isBottomFlag = true;
                stopGameDelayTimer();
                startGameDelayTimer(LOCK_DELAY_TIME);
            }
            isNotDropDownState = false;
            // moveDown(); hard drop 적용
        }
    }

    // Ghost piece의 Y좌표 구하는 메소드
    void getGhostY() {
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
        eraseBlock(board, currentBlock, x, y, boardStringBuilder);
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
        eraseBlock(board, currentBlock, x, y, boardStringBuilder);
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
        eraseBlock(board, currentBlock, x, y, boardStringBuilder);
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
            if (board[BOARD_START_HEIGHT][i] > 0)
                return true;
        }
        return false;
    }

    // 게임시작시 발동 메소드
    public void doWhenGameStart() {
    }

    // 게임오버시 발동 메소드
    public void doAfterGameOver() {
    }

    // 바닥 도달시 발동 메소드
    public void doBeforeTakeOutNextBlock() {
    }

    // 다음블록 놓은 후 메소드
    public void doAfterTakeOutNextBlock() {
    }

    // 삭제줄 복사 메소드
    private void drawAttackLines(int lines) {
        attackLinesDeque.clear();

        if (opponent == null || opponent.attackLines > (BOARD_HEIGHT) / 2)
            return;

        // 만일 이번에 들어오는 줄로 공격할 줄이 10개를 넘어선다면
        if (opponent.attackLines + lines > (BOARD_HEIGHT) / 2)
            lines = (BOARD_HEIGHT) / 2 - opponent.attackLines;

        // 공격할 줄을 만들고 (구멍을 만드는 과정)
        int[][] temp = new int[MAX_BLOCK_HEIGHT][BOARD_WIDTH];
        for (int j = 3; j > 3 - lines; j--) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                temp[j][i] = FIXED_BLOCK_NUMBER;
            }
        }
        placeBlock(temp, currentBlock, x, MAX_BLOCK_HEIGHT - currentBlock.getHeight());

        // 구멍난 공격줄을 queue 넣어주고
        for (int j = 3; j > 3 - lines; j--) {
            if (Arrays.stream(temp[j]).sum() > FULL_LINE) {
                attackLinesDeque.offer(
                        Arrays.stream(temp[j]).map(e -> e % OVERLAP_BLOCK_NUMBER).toArray());
            }
        }

        System.out.println("현재 공격 줄 수 :" + opponent.attackLines);

        // 그 다음 이미 있던 공격줄을 넣어주고
        for (int i = BOARD_HEIGHT - 1; i > BOARD_HEIGHT - opponent.attackLines - 1; i--)
            attackLinesDeque.offer(Arrays.copyOf(opponent.attackLineBoard[i], BOARD_WIDTH));

        // 상대방 공격줄에 라인 추가
        opponent.attackLines += lines;

        // stack에서 공격할 줄을 board에 넣어준다.
        int size = attackLinesDeque.size();
        System.out.println("이제 나올 공격 줄 수 :" + opponent.attackLines);
        System.out.println("현재 큐 크기:" + size);
        for (int i = BOARD_HEIGHT - 1; i > BOARD_HEIGHT - 1
                - size; i--) {
            opponent.attackLineBoard[i] = Arrays.copyOf(attackLinesDeque.poll(), BOARD_WIDTH);
        }

        // 그리고 그걸 attackLinePane에 표현한다.
        attackLineSB.setLength(0);
        for (int j = 0; j < BOARD_HEIGHT; j++) {
            for (int i = 0; i < BOARD_WIDTH; i++) {
                attackLineSB.append(blockCharMap.get(opponent.attackLineBoard[j][i]));
            }
            attackLinesStringBuilder.replace((BOARD_WIDTH + 4) + (j * (BOARD_WIDTH + 3)),
                    (BOARD_WIDTH + 4) + (j * (BOARD_WIDTH + 3)) + BOARD_WIDTH, attackLineSB.toString());
            attackLineSB.setLength(0);
        }

        opponent.attackLinePane.setText(attackLinesStringBuilder.toString());

        opponentAttackLinePaneDoc.setParagraphAttributes(0, opponentAttackLinePaneDoc.getLength(),
                attackBoardAttributeSet, false);
        StyleConstants.setForeground(blockAttributeSet, colorMap.get(Block.ATTACK_BLOCK_IDENTIFY_NUMBER));
        for (int i = BOARD_HEIGHT - 1; i > BOARD_HEIGHT - 1 - opponent.attackLines; i--) {
            opponent.attackLinePaneDoc.setCharacterAttributes(
                    (BOARD_WIDTH + 4) + (i * (BOARD_WIDTH + 3)),
                    BOARD_WIDTH,
                    blockAttributeSet,
                    true);
        }

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
        attackLines = 0;
        attackLinesDeque.clear();
        initZeroBoard(attackLineBoard);

        // 나의 attacakLinePane 비워주기
        initAttackLines();
        initGameBoard();

    }

    // 블럭 줄삭제
    private boolean clearLine() {
        boolean existFullyLine = false;
        int fullyLines = 0;
        int startindex = -1;
        int blockNums = 0;
        for (int i = BOARD_START_HEIGHT; i < BOARD_END_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++)
                if (board[i][j] > 1)
                    blockNums += board[i][j];
            if (blockNums >= 20) {
                startindex = i;
                fullyLines++;
                existFullyLine = true;
            }
            blockNums = 0;
        }
        if (fullyLines > 0) {
            startindex = startindex - fullyLines + 1;
            if (fullyLines > 1)
                drawAttackLines(fullyLines);
            launchDeleteLineAnimation(startindex, fullyLines);
            delay -= delay > 250 ? 5 - 2 * diffMode % 2 : 0;
            score += 20 * fullyLines;
            showScore();
        }
        itemLines += fullyLines;
        deleteLines += fullyLines;
        return existFullyLine;
    }

    // 줄삭제 애니메이션
    private void launchDeleteLineAnimation(int index, int lines) {
        stopGameDelayTimer();
        System.out.println("삭제전");
        showCurrent(board, currentBlock, x, y);
        focusing.removeKeyListener(gameKeyListener);
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
            showDeleteLines();
            System.out.println("삭제후");
            showCurrent(board, currentBlock, x, y);
            takeOutNextBlock();
            focusing.addKeyListener(gameKeyListener);
        });
        aniTimer.setRepeats(false);
        aniTimer.start();
        startGameDelayTimer(totaldelay + delay);

    }

    // 줄색칠 메소드
    private void paintLines(int index, int lines, Color color) {
        StyleConstants.setForeground(blockAttributeSet, color);
        for (int i = 0; i < lines; i++) {
            gamePaneDoc.setCharacterAttributes(
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
        placeBombSquare(xbuffer, ybuffer);
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
            takeOutNextBlock();
            isBottomFlag = checkBlockCollision(x, y);
            focusing.addKeyListener(gameKeyListener);
        });
        aniTimer.setRepeats(false);
        aniTimer.start();
        startGameDelayTimer(totaldelay);
    }

    // 사각형 색칠 메소드
    private void paintSquare(int x, int y, Color color) {
        StyleConstants.setForeground(blockAttributeSet, color);
        int bombLength = ((BOMB_RANGE - 1) / 2);
        int xbuffer = x - bombLength;
        int ybuffer = y - bombLength;
        int offset = (BOARD_WIDTH + 4) + ((ybuffer - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3)) + xbuffer;
        for (int i = 0; i < BOMB_RANGE; i++) {
            if (ybuffer + i < BOARD_END_HEIGHT) {
                for (int j = 0; j < BOMB_RANGE; j++) {
                    if (xbuffer + j > -1 && xbuffer + j < BOARD_WIDTH) {
                        gamePaneDoc.setCharacterAttributes(offset + i * (BOARD_WIDTH + 3) + j, 1,
                                blockAttributeSet, true);
                    }
                }
            }
        }
    }

    private void placeBombSquare(int x, int y) {
        x -= ((BOMB_RANGE - 1) / 2);
        y -= ((BOMB_RANGE - 1) / 2);
        for (int i = 0; i < BOMB_RANGE; i++) {
            if (y + i < BOARD_END_HEIGHT)
                for (int j = 0; j < BOMB_RANGE; j++) {
                    if (x + j > -1 && x + j < BOARD_WIDTH) {
                        visualBoard[y + i][x + j] = Block.BOMBBLOCK_IDENTIFY_NUMBER;
                        int offset = (BOARD_WIDTH + 4) + (y + i - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + x + j;
                        boardStringBuilder.setCharAt(offset, blockCharMap.get(Block.BOMBBLOCK_IDENTIFY_NUMBER));
                        gamePane.setText(boardStringBuilder.toString());
                        paintBlock();
                    }
                }
        }
    }

    private void placeSquare(int x, int y) {
        x -= ((STUFF_RANGE - 1) / 2);
        y -= ((STUFF_RANGE - 1) / 2);
        for (int i = 0; i < STUFF_RANGE; i++) {
            if (y + i < BOARD_END_HEIGHT)
                for (int j = 0; j < STUFF_RANGE; j++) {
                    if (x + j > -1 && x + j < BOARD_WIDTH) {
                        visualBoard[y + i][x + j] = Block.STUFF_BLOCK_IDENTIFY_NUMBER;
                        board[y + i][x + j] = FIXED_BLOCK_NUMBER;
                        int offset = (BOARD_WIDTH + 4) + (y + i - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + x + j;
                        boardStringBuilder.setCharAt(offset, blockCharMap.get(Block.STUFF_BLOCK_IDENTIFY_NUMBER));
                        gamePane.setText(boardStringBuilder.toString());
                        paintBlock();
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
                        int offset = (BOARD_WIDTH + 4) + (y + i - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + x + j;
                        boardStringBuilder.setCharAt(offset, ' ');
                        gamePane.setText(boardStringBuilder.toString());
                        paintBlock();
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
            } else if (currentBlock.getAttachItemID() == Block.STUFF_BLOCK_IDENTIFY_NUMBER) {
                ifIsStuffBlock(board, visualBoard, currentBlock);
                isItemFlag = false;
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
        if (gameMode == ITEM_GAME_MODE && itemLines >= 10) {
            nextBlock.makeItemBlock();
            itemLines = 0;
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
        isBottomFlag = checkIsItBottom();
        isNotDropDownState = true;
        doAfterTakeOutNextBlock();
    }

    /* 아이템블록 구현 메소드 */
    // 무게추
    private void ifIsweightBlock(int[][] board, int[][] visualBoard, Block block) {
        int yTemp = BOARD_END_HEIGHT - block.getHeight();
        for (int j = 0; j < BOARD_END_HEIGHT; j++) {
            for (int i = 0; i < block.getWidth(); i++) {
                board[j][x + i] = 0;
                visualBoard[j][x + i] = 0;
                int offset = (BOARD_WIDTH + 4) + (j - BOARD_START_HEIGHT) * (BOARD_WIDTH + 3) + x + i;
                if (offset >= (BOARD_WIDTH + 4))
                    boardStringBuilder.setCharAt(offset, ' ');
            }
        }
        placeBlock(board, visualBoard, block, x, yTemp);
    }

    // 채우기 블록
    private void ifIsStuffBlock(int[][] board, int[][] visualBoard, Block block) {
        int xbuffer = currentBlock.getItemCoordinate()[0] + x;
        int ybuffer = currentBlock.getItemCoordinate()[1] + y;
        placeSquare(xbuffer, ybuffer);

    }

    // 게임 중단 상태에서 다시 실행하는 경우
    public void restart() {
        initZeroBoard(board);
        x = START_X;
        y = START_Y;
        blockDeque.clear();
        blockBuffer = getBlock(blockDeque.getFirst());
        currentBlock = getBlock(blockDeque.removeFirst());
        nextBlock = getBlock(blockDeque.removeFirst());

        placeBlock(board, visualBoard, currentBlock, x, y);
        drawNextBlock();
    }

    private class InitGameKeyMap {

        int upKey;
        int downKey;
        int leftKey;
        int rightKey;
        int stackKey;

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
            });
        }

        private void initDownKey() {
            gameKeyMap.put(new KeyPair(downKey, focusing), () -> {
                moveDown();
            });
        }

        private void initLeftKey() {
            gameKeyMap.put(new KeyPair(leftKey, focusing), () -> {
                moveLeft();
            });
        }

        private void initRightKey() {
            gameKeyMap.put(new KeyPair(rightKey, focusing), () -> {
                moveRight();
            });
        }

        private void initStackKey() {
            gameKeyMap.put(new KeyPair(stackKey, focusing), GameController.this::dropDown);
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
        initGameBoard();
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
        Timer endGameTimer = new Timer(50, e -> {
            resetGame();
        });
        endGameTimer.setRepeats(false);
        endGameTimer.start();
    }

    public void resetGame() {
        delayFlag = false;
        stopGameDelayTimer();
        initZeroBoard(board);
        initZeroBoard(visualBoard);
        initZeroBoard(boardBuffer);
        isBottomFlag = false;
        blockDeque.clear();
        x = START_X;
        y = START_Y;
        boardStringBuilder.setLength(0);
    }

    public void stopGame() {
        delayFlag = false;
        stopGameDelayTimer();
    }

    public void continuGame() {
        delayFlag = true;
        startGameDelayTimer(delay);
    }

    public static void showCurrent(int[][] board, Block block, int x, int y) {
        String msg = "\n블록현황 x:" + x + " y:" + y + " width:" + block.getWidth() + " height:"
                + block.getHeight() + " rotateCount: " + block.getRotateCount();
        System.out.println(msg);
        StringBuilder sb = new StringBuilder();
        sb.append('\n');
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(board[i][j] + " ");
            }
            sb.append('\n');
        }
        msg = sb.toString();
        System.out.println(msg);
    }

    private void showScore() {
        scoreLabel.setText(String.format("%d", score));
    }

    protected void showDeleteLines() {
        if (linesLabel != null)
            linesLabel.setText(String.format("%d라인", deleteLines));
    }

    protected void setDeleteLines(JLabel linesLabel) {
        this.linesLabel = linesLabel;
    }

    public void restartGameDelayTimer() {
        gameDelayTimer.restart();
    }
}