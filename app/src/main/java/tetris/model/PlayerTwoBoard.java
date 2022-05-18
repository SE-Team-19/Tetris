package tetris.model;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.CompoundBorder;

// 현재 미사용
public class PlayerTwoBoard extends JPanel {

    public static final int BORDER_HEIGHT = 20;
    public static final int BORDER_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';
    public static final char BLOCK_CHAR = 'O';
    public static final char NULL_CHAR = ' ';
    public static final char BOMB_CHAR = 'T';
    public static final char ONELINE_CHAR = 'L';
    public static final char GHOST_CHAR = 'G';

    private JTextPane playerTwoGameBoardPane;
    private JTextPane playerTwoNextBlockPane;
    private JTextPane playerTwoAttackLinePane;
    private JTextPane playerTwoScorePane;
    private JTextPane playerTwoTimePane;

    private PlayerTwoBoard() {
        /* Player Two */
        playerTwoGameBoardPane = new JTextPane();
        playerTwoGameBoardPane.setBackground(Color.BLACK);
        playerTwoGameBoardPane.setEditable(false);
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
            BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        playerTwoGameBoardPane.setBorder(border);

        playerTwoNextBlockPane = new JTextPane();
        playerTwoNextBlockPane.setEditable(false);
        playerTwoNextBlockPane.setBackground(Color.BLACK);
        playerTwoNextBlockPane.setBorder(border);
        playerTwoNextBlockPane.setFocusable(false);

        playerTwoAttackLinePane = new JTextPane();
        playerTwoAttackLinePane.setEditable(false);
        playerTwoAttackLinePane.setBackground(Color.BLACK);
        playerTwoAttackLinePane.setBorder(border);
        playerTwoAttackLinePane.setFocusable(false);

        JPanel infoTwoPane = new JPanel();
        infoTwoPane.setFocusable(false);
        playerTwoScorePane = new JTextPane();
        playerTwoScorePane.setEditable(false);
        playerTwoScorePane.setFocusable(false);

        playerTwoTimePane = new JTextPane();
        playerTwoTimePane.setEditable(false);
        playerTwoTimePane.setFocusable(false);

        infoTwoPane.setLayout(new GridLayout(4, 0, 0, 0));
        infoTwoPane.add(playerTwoNextBlockPane);
        infoTwoPane.add(playerTwoScorePane);
        infoTwoPane.add(playerTwoTimePane);
        infoTwoPane.add(playerTwoAttackLinePane);
    }

    private static class LazyHolder {
        private static PlayerTwoBoard INSTANCE = new PlayerTwoBoard();
    }

    public static PlayerTwoBoard getInstance() {
        return PlayerTwoBoard.LazyHolder.INSTANCE;
    }
}
