package tetris.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import tetris.controller.ViewController;

public class GameView extends MasterView {

    public static final int BORDER_HEIGHT = 20;
    public static final int BORDER_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';
    public static final char BLOCK_CHAR = 'O';
    public static final char NULL_CHAR = ' ';
    public static final char BOMB_CHAR = 'T';
    public static final char ONELINE_CHAR = 'L';
    public static final char GHOST_CHAR = 'G';
    public static final String RETURN_STRING = "Return";

    private JTextPane playerOneGameBoardPane;
    private JTextPane playerOneNextBlockPane;
    private JTextPane playerOneAttackLinePane;
    private JLabel playerOneScoreLabel;
    private JLabel timeLabel;
    private JTextPane playerTwoGameBoardPane;
    private JTextPane playerTwoNextBlockPane;
    private JTextPane playerTwoAttackLinePane;
    private JLabel playerTwoScoreLabel;

    private JPanel gameDisplayPanel;
    private JPanel singleGameDisplayPanel;
    private JPanel playerTwoGameDisplayPanel;
    private JPanel multiGameDisplayPanel;
    private JPanel selectGamePanel;
    private JPanel selectMultiGamePanel;
    private JPanel selectModePanel;
    private JPanel selectDiffPanel;
    private JPanel gameOverPanel;
    private JTextField inputName;
    private JButton modeReturnBtn;
    private JButton gameReturnBtn;
    private JButton diffReturnBtn;
    private JButton multiGameReturnBtn;
    private JButton robotGameBtn;
    private JButton localGameBtn;
    private JButton onlineGameBtn;
    private JButton easyBtn;
    private JButton normalBtn;
    private JButton hardBtn;
    private JButton itemModeBtn;
    private JButton generalModeBtn;
    private JButton timeAttackBtn;
    private JButton singleGameBtn;
    private JButton mulitiGameBtn;

    private GameView() {
        initGameDisplayComponents();
        initSingleGameDisplayPanel();
        initMultiGameDisplayPane();
        initSelectGamePane();
        initSelectMultiGamePane();
        initSelectModePane();
        initSelectDiffPane();
        initGameOverPanel();
        initView();
    }

    public void resetGameView() {
        super.removeAll();
        multiGameDisplayPanel.removeAll();
        playerOneGameBoardPane.setText(null);
        playerTwoGameBoardPane.setText(null);
        multiGameDisplayPanel.add(singleGameDisplayPanel);
        multiGameDisplayPanel.add(playerTwoGameDisplayPanel);
        super.add(selectGamePanel);
    }

    private static class LazyHolder {
        private static GameView instance = new GameView();
    }

    public static GameView getInstance() {
        return LazyHolder.instance;
    }

    private void initView() {
        setFocusable(true);
        super.setLayout(new GridLayout(1, 1, 0, 0));
        super.add(selectGamePanel);
    }

    private void initGameDisplayComponents() {
        gameDisplayPanel = initAndSetName("gameDisplayPanel", new JPanel());
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.2, 1.0, Double.MIN_VALUE };
        gameDisplayPanel.setLayout(gridBagLayout);

        playerOneGameBoardPane = initAndSetName("playerOneGameBoardPane", new JTextPane());
        playerOneGameBoardPane.setBackground(Color.BLACK);
        playerOneGameBoardPane.setEditable(false);
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
                BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        playerOneGameBoardPane.setBorder(border);

        playerOneNextBlockPane = initAndSetName("playerOneNextBlockPane", new JTextPane());
        playerOneNextBlockPane.setEditable(false);
        playerOneNextBlockPane.setBackground(Color.BLACK);
        playerOneNextBlockPane.setBorder(border);
        playerOneNextBlockPane.setFocusable(false);

        playerOneAttackLinePane = initAndSetName("playerOneAttackLinePane", new JTextPane());
        playerOneAttackLinePane.setEditable(false);
        playerOneAttackLinePane.setBackground(Color.BLACK);
        playerOneAttackLinePane.setBorder(border);
        playerOneAttackLinePane.setFocusable(false);

        playerOneScoreLabel = new JLabel("0");
        playerOneScoreLabel.setFocusable(false);
        playerOneScoreLabel.setBackground(BASIC_BACKGROUND_COLOR);
        playerOneScoreLabel.setForeground(BASIC_FONT_COLOR);

        playerTwoGameBoardPane = new JTextPane();
        playerTwoGameBoardPane.setBackground(Color.BLACK);
        playerTwoGameBoardPane.setEditable(false);
        playerTwoGameBoardPane.setBorder(border);

        playerTwoScoreLabel = new JLabel("0");
        playerTwoScoreLabel.setFocusable(false);
        playerTwoScoreLabel.setBackground(BASIC_BACKGROUND_COLOR);
        playerTwoScoreLabel.setForeground(BASIC_FONT_COLOR);

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

        timeLabel = new JLabel("");
        timeLabel.setFocusable(false);
    }

    private void initGameDisplayPanel() {

    }

    private void initSingleGameDisplayPanel() {
        singleGameDisplayPanel = initAndSetName("singleGameDisplayPanel", new JPanel());
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 0.5, 0.5, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
        singleGameDisplayPanel.setLayout(gridBagLayout);

        JPanel scorePane = new JPanel();
        scorePane.setLayout(new GridLayout(2, 1, 0, 0));
        scorePane.setFocusable(true);
        scorePane.add(new JLabel("Score"));
        scorePane.add(playerOneScoreLabel);

        JPanel timePane = new JPanel();
        timePane.setLayout(new GridLayout(2, 1, 0, 0));
        timePane.setFocusable(false);
        timePane.add(new JLabel("Time"));
        timePane.add(timeLabel);

        singleGameDisplayPanel.add(playerOneGameBoardPane, addGridBagComponents(0, 0, 1, 3));
        singleGameDisplayPanel.add(playerOneNextBlockPane, addGridBagComponents(1, 0, 2, 1));
//        singleGameDisplayPanel.add(scorePane, addGridBagComponents(1, 1, 1, 1));
//        singleGameDisplayPanel.add(timePane, addGridBagComponents(1, 2, 1, 1));
//        singleGameDisplayPanel.add(playerOneAttackLinePane, addGridBagComponents(2, 1, 1, 2));
        singleGameDisplayPanel.add(scorePane, addGridBagComponents(1, 1, 1, 1));
        singleGameDisplayPanel.add(timePane, addGridBagComponents(2, 1, 1, 1));
        singleGameDisplayPanel.add(playerOneAttackLinePane, addGridBagComponents(1, 2, 2, 2));
        singleGameDisplayPanel.setFocusable(false);
    }

    private void initMultiGameDisplayPane() {
        multiGameDisplayPanel = new JPanel();
        multiGameDisplayPanel.setLayout(new GridLayout(1, 2, 0, 0));

        playerTwoGameDisplayPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 0.5, 0.5, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
        playerTwoGameDisplayPanel.setLayout(gridBagLayout);

        JPanel scorePane = new JPanel();
        scorePane.setLayout(new GridLayout(2, 1, 0, 0));
        scorePane.setFocusable(false);
        scorePane.add(new JLabel("Score"));
        scorePane.add(playerTwoScoreLabel);

        JPanel timePane = new JPanel();
        timePane.setLayout(new GridLayout(2, 1, 0, 0));
        timePane.setFocusable(false);
        timePane.add(new JLabel("Time"));
        timePane.add(timeLabel);

        playerTwoGameDisplayPanel.add(playerTwoGameBoardPane, addGridBagComponents(0, 0, 1, 3));
        playerTwoGameDisplayPanel.add(playerTwoNextBlockPane, addGridBagComponents(1, 0, 2, 1));
//        playerTwoGameDisplayPanel.add(scorePane, addGridBagComponents(1, 1, 1, 1));
//        playerTwoGameDisplayPanel.add(timePane, addGridBagComponents(1, 2, 1, 1));
//        playerTwoGameDisplayPanel.add(playerTwoAttackLinePane, addGridBagComponents(2, 1, 1, 2));
        playerTwoGameDisplayPanel.add(scorePane, addGridBagComponents(1, 1, 1, 1));
        playerTwoGameDisplayPanel.add(timePane, addGridBagComponents(2, 1, 1, 1));
        playerTwoGameDisplayPanel.add(playerTwoAttackLinePane, addGridBagComponents(1, 2, 2, 1));

        multiGameDisplayPanel.add(singleGameDisplayPanel);
        multiGameDisplayPanel.add(playerTwoGameDisplayPanel);
    }

    private void initGameOverPanel() {
        gameOverPanel = new JPanel();
        gameOverPanel.setLayout(new GridLayout(2, 1, 0, 0));
        gameOverPanel.add(new JLabel("이름을 입력해주세요!"));
        inputName = new JTextField();
        inputName.setBackground(BASIC_BACKGROUND_COLOR);
        inputName.setForeground(BASIC_FONT_COLOR);
        gameOverPanel.add(inputName);
    }

    private void initSelectGamePane() {
        selectGamePanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.2, Double.MIN_VALUE };
        selectGamePanel.setLayout(gridBagLayout);

        singleGameBtn = initAndSetName("singleGameBtn", new JButton("싱글게임"));
        mulitiGameBtn = initAndSetName("mulitiGameBtn", new JButton("멀티게임"));
        gameReturnBtn = initAndSetName("gameReturnBtn", new JButton(RETURN_STRING));

        selectGamePanel.add(singleGameBtn, addGridBagComponents(0, 0));
        selectGamePanel.add(mulitiGameBtn, addGridBagComponents(1, 0));
        selectGamePanel.add(gameReturnBtn, addGridBagComponents(0, 1, 2, 1));
    }

    private void initSelectMultiGamePane() {
        selectMultiGamePanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.2, Double.MIN_VALUE };
        selectMultiGamePanel.setLayout(gridBagLayout);

        robotGameBtn = initAndSetName("robotGameBtn", new JButton("인공지능"));
        localGameBtn = initAndSetName("localGameBtn", new JButton("로컬게임"));
        onlineGameBtn = initAndSetName("onlineGameBtn", new JButton("온라인게임"));
        multiGameReturnBtn = initAndSetName("multiGameReturnBtn", new JButton(RETURN_STRING));

        selectMultiGamePanel.add(robotGameBtn, addGridBagComponents(0, 0));
        selectMultiGamePanel.add(localGameBtn, addGridBagComponents(1, 0));
        selectMultiGamePanel.add(onlineGameBtn, addGridBagComponents(2, 0));
        selectMultiGamePanel.add(multiGameReturnBtn, addGridBagComponents(0, 1, 3, 1));
    }

    private void initSelectModePane() {
        selectModePanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.2, Double.MIN_VALUE };
        selectModePanel.setLayout(gridBagLayout);

        generalModeBtn = initAndSetName("generalModeBtn", new JButton("일반모드"));
        itemModeBtn = initAndSetName("itemModeBtn", new JButton("아이템모드"));
        timeAttackBtn = initAndSetName("timeAttackBtn", new JButton("시간제한모드"));
        modeReturnBtn = initAndSetName("modeReturnBtn", new JButton(RETURN_STRING));

        selectModePanel.add(generalModeBtn, addGridBagComponents(0, 0));
        selectModePanel.add(itemModeBtn, addGridBagComponents(1, 0));
        selectModePanel.add(timeAttackBtn, addGridBagComponents(2, 0));
        selectModePanel.add(modeReturnBtn, addGridBagComponents(0, 1, 3, 1));
    }

    private void initSelectDiffPane() {
        selectDiffPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 0.2, Double.MIN_VALUE };
        selectDiffPanel.setLayout(gridBagLayout);

        easyBtn = initAndSetName("generalModeBtn", new JButton("Easy"));
        normalBtn = initAndSetName("itemModeBtn", new JButton("Normal"));
        hardBtn = initAndSetName("timeAttackBtn", new JButton("Hard"));
        diffReturnBtn = initAndSetName("diffReturnBtn", new JButton(RETURN_STRING));

        selectDiffPanel.add(easyBtn, addGridBagComponents(0, 0));
        selectDiffPanel.add(normalBtn, addGridBagComponents(1, 0));
        selectDiffPanel.add(hardBtn, addGridBagComponents(2, 0));
        selectDiffPanel.add(diffReturnBtn, addGridBagComponents(0, 1, 3, 1));
    }

    public JPanel getSingleGameDisplayPane() {
        return this.singleGameDisplayPanel;
    }

    public JPanel getMulitiGameDisplayPane() {
        return this.multiGameDisplayPanel;
    }

    public JPanel getSelectDiffPane() {
        return this.selectDiffPanel;
    }

    public JPanel getSelectModePane() {
        return this.selectModePanel;
    }

    public JPanel getSelectMultiGamePanel() {
        return this.selectMultiGamePanel;
    }

    public JTextPane getPlayerOneGameBoardPane() {
        return this.playerOneGameBoardPane;
    }

    public JTextPane getPlayerOneNextBlockPane() {
        return this.playerOneNextBlockPane;
    }

    public JTextPane getPlayerOneAttackLinePane() {
        return this.playerOneAttackLinePane;
    }

    public JLabel getPlayerOneScoreLabel() {
        return this.playerOneScoreLabel;
    }

    public JTextPane getPlayerTwoGameBoardPane() {
        return this.playerTwoGameBoardPane;
    }

    public JTextPane getPlayerTwoNextBlockPane() {
        return this.playerTwoNextBlockPane;
    }

    public JTextPane getPlayerTwoAttackLinePane() {
        return this.playerTwoAttackLinePane;
    }

    public JLabel getPlayerTwoScoreLabel() {
        return this.playerTwoScoreLabel;
    }

    public JLabel getTimeLabel() {
        return this.timeLabel;
    }

    public JButton getSingleGameBtn() {
        return this.singleGameBtn;
    }

    public JButton getMulitiGameBtn() {
        return this.mulitiGameBtn;
    }

    public JButton getModeReturnBtn() {
        return this.modeReturnBtn;
    }

    public JButton getGameReturnBtn() {
        return this.gameReturnBtn;
    }

    public JButton getDiffReturnBtn() {
        return this.diffReturnBtn;
    }

    public JButton getMultiGameReturnBtn() {
        return this.multiGameReturnBtn;
    }

    public JButton getRobotGameBtn() {
        return this.robotGameBtn;
    }

    public JButton getLocalGameBtn() {
        return this.localGameBtn;
    }

    public JButton getOnlineGameBtn() {
        return this.onlineGameBtn;
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

    public JPanel getSelectGamePanel() {
        return this.selectGamePanel;
    }

    public JPanel getGameOverPanel() {
        return this.gameOverPanel;
    }

    public JTextField getInputName() {
        return this.inputName;
    }
}
