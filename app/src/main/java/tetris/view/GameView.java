package tetris.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.text.*;

public class GameView extends JPanel {

    public static final int BORDER_HEIGHT = 20;
    public static final int BORDER_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';
    public static final char BLOCK_CHAR = 'O';

    private JTextPane gamePane;
    private JTextPane nextBlockPane;
    private JButton returnButton;
    private SimpleAttributeSet boardAttributeSet;
    private SimpleAttributeSet nextBoardAttributeSet;

    private GameView() {
        initView();
    }

    private static class LazyHolder {
        private static final GameView INSTANCE = new GameView();
    }

    public static GameView getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void initView() {
        setFocusable(true);
        setBoardAttributeSet();
        setNextBoardAttributeSet();

        super.setLayout(new GridLayout(0, 2, 0, 0));

        gamePane = new JTextPane();
        gamePane.setBackground(Color.BLACK);
        CompoundBorder border =
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        gamePane.setBorder(border);

        nextBlockPane = new JTextPane();
        nextBlockPane.setEditable(false);
        nextBlockPane.setBackground(Color.BLACK);
        nextBlockPane.setBorder(border);

        JPanel infoPane = new JPanel();
        returnButton = new JButton("Return");

        infoPane.setLayout(new GridLayout(3, 0, 0, 0));
        infoPane.add(nextBlockPane);
        infoPane.add(new JLabel("점수표기"));
        infoPane.add(returnButton);

        super.add(gamePane);
        super.add(infoPane);
    }

    public JTextPane getGamePane() {
        return this.gamePane;
    }

    public JTextPane getnextBlockPane() {
        return this.nextBlockPane;
    }

    public JButton getReturnButton() {
        return this.returnButton;
    }

    public void setReturnButton(JButton returnButton) {
        this.returnButton = returnButton;
    }

    public SimpleAttributeSet getBoardAttributeSet() {
        return this.boardAttributeSet;
    }

    public void setBoardAttributeSet() {
        boardAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(boardAttributeSet, 20);
        StyleConstants.setFontFamily(boardAttributeSet, "Courier New");
        StyleConstants.setBold(boardAttributeSet, true);
        StyleConstants.setAlignment(boardAttributeSet, StyleConstants.ALIGN_CENTER);
    }

    public SimpleAttributeSet getNextBoardAttributeSet() {
        return this.nextBoardAttributeSet;
    }

    public void setNextBoardAttributeSet() {
        nextBoardAttributeSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(nextBoardAttributeSet, 15);
        StyleConstants.setFontFamily(nextBoardAttributeSet, "Courier New");
        StyleConstants.setBold(nextBoardAttributeSet, true);
        StyleConstants.setAlignment(nextBoardAttributeSet, StyleConstants.ALIGN_CENTER);
    }
}
