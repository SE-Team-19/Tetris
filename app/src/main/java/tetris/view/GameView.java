package tetris.view;

import java.awt.Font;
import java.awt.Graphics;
import javax.swing.border.CompoundBorder;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GameView extends JPanel {

    public static int GAME_HEIGHT = 20;
    public static int GAME_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';

    private int [][] board;
    private int [][] nextBoard;

    private JTextPane tetrisGameArea;
    private JTextPane nextBlockArea;

    private JButton returnButton;
    private JPanel gameInfoPane;
    private JLabel scoreLabel;
    private JFrame randomJFrame;

    Font font = new Font("Serial", Font.BOLD, 15);

    private SimpleAttributeSet styleSetGameBoard;

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

    public void initGameView() {
        super.setLayout(new GridLayout(0, 2, 0, 0));

        // 아래는 JFrame을 위해 임의로 집어넣은 부분
//        randomJFrame = new JFrame();
//        randomJFrame.setSize(600, 800);
//        randomJFrame.setVisible(true);

        tetrisGameArea = new JTextPane();
        tetrisGameArea.setEditable(false);
        tetrisGameArea.setBackground(Color.BLACK);
        CompoundBorder border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 10),
            BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        tetrisGameArea.setBorder(border);
        //tetrisGameArea.setPreferredSize(new java.awt.Dimension(300, 150));

        nextBlockArea = new JTextPane();
        nextBlockArea.setEditable(false);
        nextBlockArea.setBackground(Color.BLACK);
        nextBlockArea.setBorder(border);

        setStyleSetGameBoard();

        gameInfoPane = new JPanel();
        scoreLabel = new JLabel("점수표기");
        returnButton = new JButton("Return");

        gameInfoPane.setLayout(new GridLayout(3, 0, 0, 0));
        gameInfoPane.add(nextBlockArea);
        gameInfoPane.add(this.scoreLabel);
        gameInfoPane.add(returnButton);

        super.add(tetrisGameArea);
        super.add(gameInfoPane);

//        randomJFrame.add(tetrisGameArea);
//        randomJFrame.add(gameInfoPane);

        board = new int[GAME_HEIGHT][GAME_WIDTH];    // 세로 x 가로: 순서 주의하기
        nextBoard = new int[5][3];

        drawGameBoard();
    }

    public JTextPane getGamePane() {
        return this.tetrisGameArea;
    }

    public void setGamePane(JTextPane gamePane) {
        this.tetrisGameArea = gamePane;
    }

    public void setStyleSetGameBoard() {
        styleSetGameBoard = new SimpleAttributeSet();
        StyleConstants.setFontSize(styleSetGameBoard, 20);
        StyleConstants.setFontFamily(styleSetGameBoard, "Courier New");
        StyleConstants.setBold(styleSetGameBoard, true);
        StyleConstants.setForeground(styleSetGameBoard, Color.WHITE);
        //StyleConstants.setAlignment(styleSetGameBoard, StyleConstants.ALIGN_CENTER);
        StyleConstants.setAlignment(styleSetGameBoard, StyleConstants.ALIGN_JUSTIFIED);
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
//        gameView.getGamePane().setStyledDocument(doc);
//        gameView.getGamePane().add(tetrisGameArea);

    }
}
