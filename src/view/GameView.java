package tetris.src.view;

import javax.swing.border.CompoundBorder;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;

import java.awt.Color;
import java.awt.GridLayout;



public class GameView extends JPanel {

    private JTextPane gamePane;
    private JTextPane nextBlockPane;
    private JButton returnButton;
    private JPanel gameInfoPane;
    private JLabel score;

    public JButton getReturnButton() {
        return this.returnButton;
    }

    public void setReturnButton(JButton returnButton) {
        this.returnButton = returnButton;
    }

    public GameView() {
        initGameView();
    }

    private void initGameView() {
        super.setLayout(new GridLayout(0, 2, 0, 0));

        gamePane = new JTextPane();
        gameInfoPane = new JPanel();
        nextBlockPane = new JTextPane();
        score = new JLabel("점수표기");
        returnButton = new JButton("Return");

        gamePane.setEditable(false);
        gamePane.setBackground(Color.BLACK);
        nextBlockPane.setEditable(false);
        nextBlockPane.setBackground(Color.BLACK);
        CompoundBorder border =
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
                        BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        gamePane.setBorder(border);
        nextBlockPane.setBorder(border);

        gameInfoPane.setLayout(new GridLayout(3, 0, 0, 0));
        gameInfoPane.add(nextBlockPane);
        gameInfoPane.add(this.score);
        gameInfoPane.add(returnButton);

        super.add(gamePane);
        super.add(gameInfoPane);
    }
}
