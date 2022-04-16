package tetris.view;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.Color;
import java.awt.GridLayout;

public class GameView extends JPanel {

    private JTextPane gamePane;
    private JTextPane nextBlockPane;
    private JButton returnGameToMainBtn;
    private JPanel gameInfoPane;
    private JLabel score;

    public JButton getReturnGameToMainBtn() {
        return this.returnGameToMainBtn;
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

        gamePane = new JTextPane();
        gameInfoPane = new JPanel();
        nextBlockPane = new JTextPane();
        score = new JLabel("점수표기");
        returnGameToMainBtn = initAndSetName("returnGameToMainBtn", new JButton("Return"));

        returnGameToMainBtn.setFocusable(true);
        gamePane.setEditable(false);
        gamePane.setBackground(Color.BLACK);
        nextBlockPane.setEditable(false);
        nextBlockPane.setBackground(Color.BLACK);
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
                BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        gamePane.setBorder(border);
        nextBlockPane.setBorder(border);

        gameInfoPane.setLayout(new GridLayout(3, 0, 0, 0));
        gameInfoPane.add(nextBlockPane);
        gameInfoPane.add(this.score);
        gameInfoPane.add(returnGameToMainBtn);

        super.add(gamePane);
        super.add(gameInfoPane);
    }

    public JTextPane getGamePane() {
        return this.gamePane;
    }

    private <T extends JComponent> T initAndSetName(String name, T comp) {
        comp.setName(name);
        return comp;
    }
}
