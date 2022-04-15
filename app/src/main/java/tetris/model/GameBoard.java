package tetris.model;

import java.awt.Font;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;

import tetris.controller.*;
import tetris.model.*;
import tetris.view.*;

public class GameBoard extends JFrame {
    /*
    게임화면 구성. JPanel 안에 JPanel 삽입이 불가능하므로, GameView 가 아닌
    Controller 에서 관리하도록 해야 할듯
     */

    public static int count = 0;    // 생성된 블럭의 개수를 계산하는 변수

    public static int GAME_HEIGHT = 20;
    public static int GAME_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';

    private Block currentBlock;
    private Block nextBlock;

    private int x = 3;
    private int y = 0;

    private String font;
    private int fontSize;
    private String size;
    private Color color;
    private char a;
    private char b;
    private int width;
    private int height;


    private SimpleAttributeSet styleSet;
    private JTextPane tetrisGameArea;
    private JTextPane nextBlockArea;
    GameView gameView = GameView.getInstance();

    private SimpleAttributeSet styleSetGameBoard;

    private int [][] board;
    private int [][] nextBoard;

    public GameBoard() {
        super("Seoultech");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // gameBoard display
        tetrisGameArea = new JTextPane();
        tetrisGameArea.setEditable(false);
        tetrisGameArea.setBackground(Color.BLACK);
        CompoundBorder border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 10),
            BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        tetrisGameArea.setBorder(border);

        nextBlockArea = new JTextPane();
        nextBlockArea.setEditable(false);
        nextBlockArea.setBackground(Color.BLACK);
        nextBlockArea.setBorder(border);

        styleSetGameBoard = new SimpleAttributeSet();
        StyleConstants.setFontSize(styleSetGameBoard, 20);
        StyleConstants.setFontFamily(styleSetGameBoard, "Courier New"); // font에 따라 다름
        // Font.MONOSPACED, "Courier", "Courier New"
        StyleConstants.setBold(styleSetGameBoard, true);
        StyleConstants.setForeground(styleSetGameBoard, Color.WHITE);
        StyleConstants.setAlignment(styleSetGameBoard, StyleConstants.ALIGN_CENTER);


        JPanel leftPanel;
        JPanel rightPanel;
        leftPanel = new JPanel();
        leftPanel.add(tetrisGameArea);
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(nextBlockArea);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(Box.createVerticalStrut(20));

        JPanel panel = new JPanel();
        panel.add(leftPanel);
        panel.add(rightPanel);

        add(panel);

        //setStyleSet();



        board = new int[GAME_HEIGHT][GAME_WIDTH];    // 세로 x 가로: 순서 주의하기
        nextBoard = new int[5][3];

        drawGameBoard();
    }

    public void setStyleSet() {
        styleSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(styleSet, Font.SANS_SERIF);
        StyleConstants.setFontSize(styleSet, 20);
        StyleConstants.setBold(styleSet, true);
        StyleConstants.setForeground(styleSet, Color.WHITE);
        StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_CENTER);
    }

    public void setStyleSetGameBoard() {
        styleSetGameBoard = new SimpleAttributeSet();
        StyleConstants.setFontSize(styleSetGameBoard, 20);
        StyleConstants.setFontFamily(styleSetGameBoard, "Courier");
        StyleConstants.setBold(styleSetGameBoard, true);
        StyleConstants.setForeground(styleSetGameBoard, Color.WHITE);
        StyleConstants.setAlignment(styleSetGameBoard, StyleConstants.ALIGN_CENTER);
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
        tetrisGameArea.setText(sb.toString());
        StyledDocument doc = tetrisGameArea.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), styleSetGameBoard, false);

        StyledDocument boardDoc = tetrisGameArea.getStyledDocument();


        gameView.getGamePane().setStyledDocument(doc);
        gameView.getGamePane().add(tetrisGameArea);

    }

    public AttributeSet getStyleSet() {
        return styleSet;
    }


    /*
    public GameBoard(jPanel placeholder) {
        //reset();
//        this.setBounds(placeholder.getBounds());
//        this.setBackground(placeholder.getBackground());
//        this.setBorder(placeholder.getBorder());

        board = new int[GAME_HEIGHT][GAME_WIDTH]; // 세로 x 가로: 순서 주의하기

        //currentBlock = getRandomBlock();
        //nextBlock = getRandomBlock();
        placeBlock();
        drawGameBoard();
        drawBlockBoard();
    } */
    
    public int [][] getBoard() {
        return board;
    }
    public int getHeight() {
        return board.length;
    }
    public int getWidth() {
        return board[0].length;
    }

    // 직사각형의 화면을 만든다
    /*
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 150, 300);
    }*/

    /*
    // 이 메소드는 나중에 GameView 클래스로 대체될 여지가 있다
    public void reset() {
        this.add(gamePanel);
        this.setSize(800, 400);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);   // 가운데에서 화면 만든다
    }
    */



    // 블록의 현재 색상을 받아오는 메소드
   public Color getColor() {
        return currentBlock.getColor();
    }




    /*
    // 블록의 움직임에 관한 메소드 정리
    public void moveDown() {
        eraseCurr();
        if (y < HEIGHT - currentBlock.getHeight()) {
            y++;
        }
        else {
            placeBlock();
            currentBlock = getRandomBlock();
            x = 3;
            y = 0;
        }
        placeBlock();
    }

    public void moveLeft() {
        if (x > 0) {
            eraseCurr();
            x--;
            placeBlock();
        }
    }

    public void moveRight() {
        if (x < GAME_WIDTH - currentBlock.getWidth()) {
            eraseCurr();
            x++;
            placeBlock();
        }
    }
    */

    // 아래로 움직이지 않도록 고정해준다.
    public void placeBlock() {
        for (int i = 0; i < currentBlock.getHeight(); i++) {
            for (int j = 0; j < currentBlock.getWidth(); j++) {
                board[y + j][x + i] = currentBlock.getShape(j, i);
            }
        }
    }

    // 바닥에 닿았을 때, 종료시키는 메소드. 함수 이름??
    public boolean ifBlockMeetBounds() {
        if (currentBlock.getY() < 0) {
            currentBlock = null;
            return true;
        }
        return false;
    }

    public void blockRotate() {

    }

    public void blockMoveDown() {
        //currentBlock.moveDown();
        placeBlock();
    }

    public void blockMoveLeft() {
        if(currentBlock == null) {
            return;
        }

        if (checkLeftLocation() == false) {
            return;
        }

        //currentBlock.moveLeft();
        placeBlock();
    }

    public void blockMoveRight() {

        //currentBlock.moveRight();
        placeBlock();
    }
    /*
    public Block blockDrop() {
        if (currentBlock == null) {
            return currentBlock;
        }
        while (checkBottomLocation() != false) {
            //currentBlock.moveDown();
        }

        return currentBlock;
    }
    */

    public boolean checkLeftLocation() {
       // if (currentBlock.getLeftLocation() <= 0) {
       //     return false;
       // }
        // 아래부분 수정해야함
//        else {
//            return true;
//        }
        return false;
    }


    public boolean checkBottomLocation() {
        return false;
    }

    /*
    public void drawGameBoard() {
        StringBuffer sb = new StringBuffer();
        for(int t = 0; t < WIDTH + 2; t++) {
            sb.append(BORDER_CHAR);
        }
        sb.append("\n");
        for(int i = 0; i < board.length; i++) {
            sb.append(BORDER_CHAR);

            for(int j = 0; j < board[i].length; j++) {
                if(board[i][j] == 1) {
                    sb.append("O");
                } else {
                    sb.append(" ");
                }
            }
            sb.append(BORDER_CHAR);
            sb.append("\n");
        }
        for(int t = 0; t < WIDTH + 2; t++) {
            sb.append(BORDER_CHAR);
        }
        gamePane.setText(sb.toString());
        StyledDocument doc = gamePane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), styleSet, false);
        gamePane.setStyledDocument(doc);
    } */

    // 다음 블록이 보여지는 화면
    public void drawBlockBoard() {

        StringBuffer sb = new StringBuffer();
        sb.append("\n");

        for (int i = 0; i < currentBlock.getWidth(); i++) {
            sb.append(" ");
            for (int j = 0; j < currentBlock.getHeight(); j++ ){
                // 이부분은 합친 뒤 다시 구현해보기
            }
        }
        nextBlockArea.setText(sb.toString());
        StyledDocument doc = nextBlockArea.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), styleSet, false);
        nextBlockArea.setStyledDocument(doc);
    }



}
