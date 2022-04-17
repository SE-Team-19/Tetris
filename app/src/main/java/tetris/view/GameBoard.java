package tetris.view;

import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

public class GameBoard extends JPanel {

    public static int GAME_HEIGHT = 20;
    public static int GAME_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';
    private final int SPEED = 300;
    private Timer timer;

    private int [][] currentBoard;      // tetrisGamePane 의 'X' size를 결정하기 위한 변수
    private int [][] nextBoard;

    private KeyListener PlayerKeyListener;
    private SimpleAttributeSet styleSetGameBoard;

    private Block currentBlock;
    private Block nextBlock;

    private JTextPane gamePane;

    int x = 3;
    int y = 0;

    private static final int initInterval = 1000;

    public GameBoard() {
        initBoard();
    }

    private void initBoard() {
        currentBoard = new int[GAME_HEIGHT][GAME_WIDTH];    // 세로 x 가로: 순서 주의하기

        setBounds(0, 0, 600, 400);
        setBackground(Color.BLACK);


        timer = new Timer(initInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                moveDown();
//                drawGameBoard();
            }
        });

        gamePane = new JTextPane();


       // PlayerKeyListener = new PlayerKeyListener();
        addKeyListener(PlayerKeyListener);
        setFocusable(true);

//        currentBlock = getRandomBlock_NormalMode();
//        nextBlock = getRandomBlock_NormalMode();
//
//        setStyleSetGameBoard();
//
//        placeBlock();
//        drawGameBoard();

        timer.start();
    }



}


