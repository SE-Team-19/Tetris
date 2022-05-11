package tetris.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;

public class GameView extends JPanel {

    public static final int BORDER_HEIGHT = 20;
    public static final int BORDER_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';
    public static final char BLOCK_CHAR = 'O';
    public static final char NULL_CHAR = ' ';
    public static final char BOMB_CHAR = 'T';
    public static final char ONELINE_CHAR = 'L';
    public static final char GHOST_CHAR = 'G';

    private JTextPane gameBoardPane;
    private JTextPane nextBlockPane;
    private JTextPane attackLinePane;
    private JTextPane scorePane;
    private JTextPane timePane;
    private JPanel gameDisplayPanel;
    private JPanel selectModePanel;
    private JPanel selectDiffPanel;
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
        initSelectModePane();
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

        super.add(selectModePanel);
    }

    private void initGameDisplayPane() {
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

    private void initSelectModePane() {
        selectModePanel = new JPanel();
        selectModePanel.setLayout(new GridLayout(0, 3, 0, 0));

        generalModeBtn = initAndSetName("generalModeBtn", new JButton("일반모드"));
        itemModeBtn = initAndSetName("itemModeBtn", new JButton("아이템모드"));
        timeAttackBtn = initAndSetName("timeAttackBtn", new JButton("시간제한모드"));

        selectModePanel.add(generalModeBtn);
        selectModePanel.add(itemModeBtn);
        selectModePanel.add(timeAttackBtn);
    }

    private void initSelectDiffPane() {
        selectDiffPanel = new JPanel();
        selectDiffPanel.setLayout(new GridLayout(0, 3, 0, 0));

        easyBtn = initAndSetName("generalModeBtn", new JButton("Easy"));
        normalBtn = initAndSetName("itemModeBtn", new JButton("Normal"));
        hardBtn = initAndSetName("timeAttackBtn", new JButton("Hard"));

        selectDiffPanel.add(easyBtn);
        selectDiffPanel.add(normalBtn);
        selectDiffPanel.add(hardBtn);
    }

    private <T extends JComponent> T initAndSetName(String name, T comp) {
        comp.setName(name);
        return comp;
    }

    public JPanel getGameDisplayPane() {
        return this.gameDisplayPanel;
    }

    public JPanel getSelectDiffPane() {
        return this.selectDiffPanel;
    }

    public JPanel getSelectModePane() {
        return this.selectModePanel;
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
