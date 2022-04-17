package tetris.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JOptionPane;
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

import tetris.model.IBlock;
import tetris.model.JBlock;
import tetris.model.LBlock;
import tetris.model.OBlock;
import tetris.model.SBlock;
import tetris.model.TBlock;
import tetris.model.ZBlock;


public class GameView extends JPanel {


    public static int GAME_HEIGHT = 20;
    public static int GAME_WIDTH = 10;
    public static int NEXT_BOARD_HEIGHT = 5;
    public static int NEXT_BOARD_WIDTH = 4;
    public static final char BORDER_CHAR = 'X';
    private final int SPEED = 300;
    private Timer timer;

    private int [][] board;      // tetrisGamePane 의 'X' size를 결정하기 위한 변수
    private int [][] nextBoard;

    private JTextPane tetrisGamePane;
    private JTextPane nextTetrisBlockPane;
    private JButton returnButton;
    private JPanel gameInfoPane;
    private JLabel score;
    private SimpleAttributeSet styleSetGameBoard;
    private SimpleAttributeSet styleSetNextBlockBoard;
    private KeyListener PlayerKeyListener;

    private Block currentBlock;
    private Block nextBlock;

    private GameBoard gameBoard;

    int x = 3;
    int y = 0;

    int nextBlockX = 0; // warning: 시작 위치 조절
    int nextBlockY = 1;

    private static final int initInterval = 1000;



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
        //tetrisGamePane.setEditable(false);
        tetrisGamePane.setBackground(Color.BLACK);
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
            BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        tetrisGamePane.setBorder(border);


        nextTetrisBlockPane = new JTextPane();
        nextTetrisBlockPane.setEditable(false);
        nextTetrisBlockPane.setBackground(Color.BLACK);
        nextTetrisBlockPane.setBorder(border);

        gameInfoPane = new JPanel();
        score = new JLabel("점수표기");
        returnButton = new JButton("Return");

        gameInfoPane.setLayout(new GridLayout(3, 0, 0, 0));
        gameInfoPane.add(nextTetrisBlockPane);
        gameInfoPane.add(this.score);
        gameInfoPane.add(returnButton);

        super.add(tetrisGamePane);

        // gameInfoPane = nextBlockPane(JTextPane) + score(Label) + returnButton(JButton)
        super.add(gameInfoPane);

        //setBounds(0, 0, 600, 400);
        //setBackground(Color.BLACK);


        timer = new Timer(initInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveDown();
                drawGameBoard();
            }
        });

        board = new int[GAME_HEIGHT][GAME_WIDTH];    // 세로 x 가로: 순서 주의하기
        nextBoard = new int[5][4];

        PlayerKeyListener = new PlayerKeyListener();
        addKeyListener(PlayerKeyListener);
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


    private Block getRandomBlock_NormalMode() {
        Random random = new Random(System.currentTimeMillis());
        int block = random.nextInt(7);
        switch(block) {
            case 0:
//                currentBlock = new IBlock();
//                return currentBlock;
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

    private Block getRandomBlock_EasyMode() {
        return new IBlock();
    }
    private Block getRandomBlock_HardMode() {return new IBlock();}




    private void placeBlock() {
        for(int j = 0; j < currentBlock.height(); j++) {
            for(int i = 0; i < currentBlock.width(); i++) {
                board[y + j][x + i] = currentBlock.getShape(i, j);
                // getShape -> return j, i
            }
        }
    }

    private void placeNextBlock() {
        for(int j = 0; j < nextBlock.height(); j++) {
            for(int i = 0; i < nextBlock.width(); i++) {
                nextBoard[nextBlockY + j][nextBlockX + i] = nextBlock.getShape(i, j);
                // getShape -> return j, i
            }
        }
    }

    private void eraseCurr() {
        for(int i = x; i < x + currentBlock.width(); i++) {
            for(int j = y; j < y + currentBlock.height(); j++) {
                board[j][i] = 0;
            }
        }
    }

    private void eraseNext() {
        for(int i = nextBlockX; i < nextBlockX + nextBlock.width(); i++) {
            for(int j = nextBlockY; j < nextBlockY + nextBlock.height(); j++) {
                nextBoard[j][i] = 0;
            }
        }
    }

    public void moveDown() {
        eraseCurr();
        if(y < HEIGHT - currentBlock.height()) {
            y++;
        }
        else {
            placeBlock();
            currentBlock = nextBlock;
            eraseNext();    // 생략하면 안됨

            nextBlock = getRandomBlock_NormalMode();
            placeNextBlock();
            drawNextBlockBoard();
            x = 3;
            y = 0;
        }
        placeBlock();
    }

    public void moveRight() {
        eraseCurr();
        if(x < WIDTH - currentBlock.width()){
            x++;
        }
        placeBlock();
    }

    public void moveLeft() {
        eraseCurr();
        if(x > 0) {
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

    public void setStyleGameBoard() {
        styleSetGameBoard = new SimpleAttributeSet();
        StyleConstants.setFontSize(styleSetGameBoard, 20);
        StyleConstants.setFontFamily(styleSetGameBoard, "Courier New");
        StyleConstants.setBold(styleSetGameBoard, true);
        StyleConstants.setForeground(styleSetGameBoard, Color.WHITE);
        StyleConstants.setAlignment(styleSetGameBoard, StyleConstants.ALIGN_CENTER);
        //StyleConstants.setAlignment(styleSetGameBoard, StyleConstants.ALIGN_JUSTIFIED);
    }

    public void setStyleNextBlockBoard() {
        styleSetNextBlockBoard = new SimpleAttributeSet();
        StyleConstants.setFontSize(styleSetNextBlockBoard, 15);
        StyleConstants.setFontFamily(styleSetNextBlockBoard, "Courier New");
        StyleConstants.setBold(styleSetNextBlockBoard, true);
        StyleConstants.setForeground(styleSetNextBlockBoard, Color.WHITE);
        StyleConstants.setAlignment(styleSetNextBlockBoard, StyleConstants.ALIGN_CENTER);
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

        //StyledDocument boardDoc = tetrisGamePane.getStyledDocument();
    }

    public void drawNextBlockBoard() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < nextBoard.length; i++) {
            for (int j = 0; j < nextBoard[i].length; j++) {
                if(nextBoard[i][j] == 1) {
                    sb.append(BORDER_CHAR);
                } else {
                    sb.append(" ");
                }
            }
            sb.append("\n");
        }
        nextTetrisBlockPane.setText(sb.toString());
        StyledDocument doc = nextTetrisBlockPane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), styleSetNextBlockBoard, false);

    }




    // 키보드 입력을 받는다.
    public class PlayerKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
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
        public void keyReleased(KeyEvent e) {

        }
    }

}
