package tetris.model;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Random;

import tetris.controller.GameController;
import tetris.controller.ViewController;
import tetris.model.*;


import tetris.model.*;


public class GameBoard extends JPanel {
    /*
    게임화면 구성. JPanel 안에 JPanel 삽입이 불가능하므로, GameView 가 아닌
    Controller 에서 관리하도록 해야 할듯
     */

    public static int count = 0;    // 생성된 블럭의 개수를 계산하는 변수

    public static int GAME_HEIGHT = 20;
    public static int GAME_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';

    private SimpleAttributeSet styleSet;
    private JTextPane gamePane;
    private JTextPane nextBlockPane;
    private Block currentBlock;
    private Block nextBlock;
    private int [][] board;

    private int x = 3;
    private int y = 0;

    public GameBoard(JPanel placeholder) {
        //reset();
        this.setBounds(placeholder.getBounds());
        this.setBackground(placeholder.getBackground());
        this.setBorder(placeholder.getBorder());




        board = new int[GAME_HEIGHT][GAME_WIDTH]; // 세로 x 가로: 순서 주의하기

        currentBlock = getRandomBlock();
        nextBlock = getRandomBlock();
        placeBlock();
        drawGameBoard();
        drawBlockBoard();
    }

    // 직사각형의 화면을 만든다
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 150, 300);
    }

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

    // 블록의 시작 위치를 초기화하는 메소드
    public void eraseCurr() {
        for(int i = x; i < x + currentBlock.getWidth(); i++){
            for(int j = y; j < y + currentBlock.getHeight(); j++){
                board[j][i] = 0;
            }
        }
    }

    // 블록의 현재 색상을 받아오는 메소드
   public Color getColor() {
        return currentBlock.getColor();
    }


    // 랜덤으로 블록을 받아오는 메소드
    public Block getRandomBlock() {
        long seed = System.currentTimeMillis();
        Random random = new Random(seed);

        int block = random.nextInt(7);        // 7개의 블럭을 동일한 확률로 불러온다: 0이상 6이하
        //count++;    // 개수를 하나 증가시킨다.

        switch(block) {
            case 0:
                currentBlock = new IBlock();
                return currentBlock;
            case 1:
                currentBlock = new JBlock();
                return currentBlock;
            case 2:
                currentBlock = new LBlock();
                return currentBlock;
            case 3:
                currentBlock = new ZBlock();
                return currentBlock;
            case 4:
                currentBlock = new SBlock();
                return currentBlock;
            case 5:
                currentBlock = new TBlock();
                return currentBlock;
            case 6:
                currentBlock = new OBlock();
                return currentBlock;
            //default:   아래에서 return을 해줘야 하므로 굳이 필요가 없다
            // return new IBlock;
        }
        return new IBlock();
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
        currentBlock.moveDown();
        placeBlock();
    }

    public void blockMoveLeft() {
        if(currentBlock == null) {
            return;
        }

        if (checkLeftLocation() == false) {
            return;
        }

        currentBlock.moveLeft();
        placeBlock();
    }

    public void blockMoveRight() {

        currentBlock.moveRight();
        placeBlock();
    }

    public Block blockDrop() {
        if (currentBlock == null) {
            return currentBlock;
        }
        while (checkBottomLocation() != false) {
            currentBlock.moveDown();
        }

        return currentBlock;
    }

    public boolean checkLeftLocation() {
        if (currentBlock.getLeftLocation() <= 0) {
            return false;
        }
        // 아래부분 수정해야함
        else {
            return true;
        }
    }


    public boolean checkBottomLocation() {
        return false;
    }

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
    }

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
        nextBlockPane.setText(sb.toString());
        StyledDocument doc = nextBlockPane.getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), styleSet, false);
        nextBlockPane.setStyledDocument(doc);
    }


}
