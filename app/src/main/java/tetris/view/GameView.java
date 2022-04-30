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

    private JTextPane gameBoardPane;
    private JTextPane nextBlockPane;
    private JTextPane scorePane;
    private JPanel gameDisplayPane;
    private JPanel selectModePane;
    private JPanel selectDiffPane;
    private JButton easyBtn;
    private JButton normalBtn;
    private JButton hardBtn;
    private JButton itemModeBtn;
    private JButton generalModeBtn;
    private JButton timeAttackBtn;

    // private SimpleAttributeSet boardAttributeSet;
    // private SimpleAttributeSet nextBoardAttributeSet;

    private GameView() {
        initGameDisplayPane();
        initSelcetModePane();
        initSelectDiffPane();
        initView();
    }

    private static class LazyHolder {
        private static GameView INSTANCE = new GameView();
    }

    public static GameView getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void initView() {
        setFocusable(true);
        // setBoardAttributeSet();
        // setNextBoardAttributeSet();

        super.setLayout(new GridLayout(1, 1, 0, 0));

        super.add(selectModePane);
    }

    private void initGameDisplayPane() {
        gameDisplayPane = new JPanel();
        gameDisplayPane.setLayout(new GridLayout(1, 2, 0, 0));

        gameBoardPane = new JTextPane();
        gameBoardPane.setBackground(Color.BLACK);
        gameBoardPane.setEditable(false);
        // gamePane.setEnabled(false);
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
                BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        gameBoardPane.setBorder(border);

        nextBlockPane = new JTextPane();
        nextBlockPane.setEditable(false);
        nextBlockPane.setBackground(Color.BLACK);
        nextBlockPane.setBorder(border);
        // nextBlockPane.setEnabled(false);

        JPanel infoPane = new JPanel();
        scorePane = new JTextPane();
        scorePane.setEditable(false);

        infoPane.setLayout(new GridLayout(3, 0, 0, 0));
        infoPane.add(nextBlockPane);
        infoPane.add(scorePane);

        gameDisplayPane.add(gameBoardPane);
        gameDisplayPane.add(infoPane);
    }

    public JDialog getGameOverDialog() {
        JFrame frame = new JFrame();
        JDialog gameOverDialog = new JDialog(frame, "이름을 입력하세요", true);
        gameOverDialog.setBounds(0, 0, 300, 200);
        gameOverDialog.setLocationRelativeTo(null);
        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1, 1, 0, 0));
        JTextField inputName = new JTextField();
        inputName.addActionListener(e -> {
            // userName = inputName.getText();
            gameOverDialog.dispose();
        });
        pane.add(inputName);
        gameOverDialog.setContentPane(pane);
        return gameOverDialog;
    }

    private void initSelcetModePane() {
        selectModePane = new JPanel();
        selectModePane.setLayout(new GridLayout(0, 3, 0, 0));

        generalModeBtn = initAndSetName("generalModeBtn", new JButton("일반모드"));
        itemModeBtn = initAndSetName("itemModeBtn", new JButton("아이템모드"));
        timeAttackBtn = initAndSetName("timeAttackBtn", new JButton("시간제한모드"));

        selectModePane.add(generalModeBtn);
        selectModePane.add(itemModeBtn);
        selectModePane.add(timeAttackBtn);
    }

    private void initSelectDiffPane() {
        selectDiffPane = new JPanel();
        selectDiffPane.setLayout(new GridLayout(0, 3, 0, 0));

        easyBtn = initAndSetName("generalModeBtn", new JButton("Easy"));
        normalBtn = initAndSetName("itemModeBtn", new JButton("Normal"));
        hardBtn = initAndSetName("timeAttackBtn", new JButton("Hard"));

        selectDiffPane.add(easyBtn);
        selectDiffPane.add(normalBtn);
        selectDiffPane.add(hardBtn);
    }

    private <T extends JComponent> T initAndSetName(String name, T comp) {
        comp.setName(name);
        return comp;
    }

    public JPanel getGameDisplayPane() {
        return this.gameDisplayPane;
    }

    public JPanel getSelectDiffPane() {
        return this.selectDiffPane;
    }

    public JPanel getSelectModePane() {
        return this.selectModePane;
    }

    public JTextPane getGameBoardPane() {
        return this.gameBoardPane;
    }

    public JTextPane getNextBlockPane() {
        return this.nextBlockPane;
    }

    public JTextPane getScorePane() {
        return this.scorePane;
    }

    public JButton getEasyBtn() {
        return this.easyBtn;
    }

    public JButton getNormalBtn() {
        return this.normalBtn;
    }

    public JButton getHardBtn() {
        return this.hardBtn;
    }

    public JButton getItemModeBtn() {
        return this.itemModeBtn;
    }

    public JButton getGeneralModeBtn() {
        return this.generalModeBtn;
    }

    public JButton getTimeAttackBtn() {
        return this.timeAttackBtn;
    }
}
