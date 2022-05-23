package tetris.model;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.CompoundBorder;

// 현재 미사용
public class PlayerOneBoard extends JPanel {

    public static final int BORDER_HEIGHT = 20;
    public static final int BORDER_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';
    public static final char BLOCK_CHAR = 'O';
    public static final char NULL_CHAR = ' ';
    public static final char BOMB_CHAR = 'T';
    public static final char ONELINE_CHAR = 'L';
    public static final char GHOST_CHAR = 'G';

    private JTextPane playerOneGameBoardPane;
    private JTextPane playerOneNextBlockPane;
    private JTextPane playerOneAttackLinePane;
    private JTextPane playerOneScorePane;
    private JTextPane playerOneTimePane;

    private PlayerOneBoard() {
        /* Player One */
        playerOneGameBoardPane = new JTextPane();
        playerOneGameBoardPane.setBackground(Color.BLACK);
        playerOneGameBoardPane.setEditable(false);
        CompoundBorder border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 10),
            BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        playerOneGameBoardPane.setBorder(border);

        playerOneNextBlockPane = new JTextPane();
        playerOneNextBlockPane.setEditable(false);
        playerOneNextBlockPane.setBackground(Color.BLACK);
        playerOneNextBlockPane.setBorder(border);
        playerOneNextBlockPane.setFocusable(false);

        playerOneAttackLinePane = new JTextPane();
        playerOneAttackLinePane.setEditable(false);
        playerOneAttackLinePane.setBackground(Color.BLACK);
        playerOneAttackLinePane.setBorder(border);
        playerOneAttackLinePane.setFocusable(false);

        JPanel infoOnePane = new JPanel();
        //JTextPane infoOnePane = new JTextPane();
        // infoOnePane = new JPanel();
        infoOnePane.setFocusable(false);
        playerOneScorePane = new JTextPane();
        playerOneScorePane.setEditable(false);
        playerOneScorePane.setFocusable(false);

        playerOneTimePane = new JTextPane();
        playerOneTimePane.setEditable(false);
        playerOneTimePane.setFocusable(false);

        infoOnePane.setLayout(new GridLayout(4, 0, 0, 0));
        infoOnePane.add(playerOneNextBlockPane);
        infoOnePane.add(playerOneScorePane);
        infoOnePane.add(playerOneTimePane);
        infoOnePane.add(playerOneAttackLinePane);

    }

    private static class LazyHolder {
        private static PlayerOneBoard INSTANCE = new PlayerOneBoard();
    }

    public static PlayerOneBoard getInstance() {
        return PlayerOneBoard.LazyHolder.INSTANCE;
    }
}
