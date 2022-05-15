package tetris.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import tetris.controller.*;
import tetris.model.*;
import tetris.view.*;

public class PlayerTwoView extends JPanel{

 /*   public static final int BORDER_HEIGHT = 20;
    public static final int BORDER_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';
    public static final char BLOCK_CHAR = 'O';
    public static final char NULL_CHAR = ' ';
    public static final char BOMB_CHAR = 'T';
    public static final char ONELINE_CHAR = 'L';
    public static final char GHOST_CHAR = 'G';*/

    private JTextPane gameBoardPane;
    private JTextPane nextBlockPane;
    private JTextPane attackLinePane;
    private JTextPane scorePane;
    private JTextPane timePane;
    private JPanel gameDisplayPanel;

    //private GameController gameController2;


    private static class LazyHolder {
        private static final PlayerTwoView INSTANCE = new PlayerTwoView();
    }

    public static PlayerTwoView getInstance() {
        return PlayerTwoView.LazyHolder.INSTANCE;
    }

    private PlayerTwoView() {
        gameDisplayPanel = new JPanel();
        gameDisplayPanel.setLayout(new GridLayout(1, 2, 0, 0));

        gameBoardPane = new JTextPane();
        gameBoardPane.setBackground(Color.BLACK);
        gameBoardPane.setEditable(false);
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
            BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        gameBoardPane.setBorder(border);

        nextBlockPane = new JTextPane();
        nextBlockPane.setEditable(false);
        nextBlockPane.setBackground(Color.BLACK);
        nextBlockPane.setBorder(border);
        nextBlockPane.setFocusable(false);

        attackLinePane = new JTextPane();
        attackLinePane.setEditable(false);
        attackLinePane.setBackground(Color.BLACK);
        attackLinePane.setBorder(border);
        attackLinePane.setFocusable(false);

        JPanel infoPane = new JPanel();
        infoPane.setFocusable(false);
        scorePane = new JTextPane();
        scorePane.setEditable(false);
        scorePane.setFocusable(false);

        timePane = new JTextPane();
        timePane.setEditable(false);
        timePane.setFocusable(false);

        infoPane.setLayout(new GridLayout(1, 0, 0, 0));
        // infoPane.add(nextBlockPane);
        // infoPane.add(scorePane);
        // infoPane.add(timePane);
        infoPane.add(attackLinePane);

        gameDisplayPanel.add(gameBoardPane);
        gameDisplayPanel.add(infoPane);

        /*
        gameController2 = new GameController(
            new Setting(0, false, 37, 39, 38, 40, 32),
            new PlayerController(), new Container() ); */
    }

    public JPanel getGameDisplayPane() {
        return this.gameDisplayPanel;
    }

    public JTextPane getGameBoardPane() {
        return this.gameBoardPane;
    }

    public JTextPane getNextBlockPane() {
        return this.nextBlockPane;
    }

    public JTextPane getAttackLinePane() {
        return this.attackLinePane;
    }

    public JTextPane getScorePane() {
        return this.scorePane;
    }

    public JTextPane getTimePane() {
        return this.timePane;
    }
}