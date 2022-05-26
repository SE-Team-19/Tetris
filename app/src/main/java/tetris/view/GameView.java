package tetris.view;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class GameView extends MasterView {

    public static final int BORDER_HEIGHT = 20;
    public static final int BORDER_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';
    public static final char BLOCK_CHAR = 'O';
    public static final char NULL_CHAR = ' ';
    public static final char BOMB_CHAR = 'T';
    public static final char STUFF_CHAR = 'F';
    public static final char ONELINE_CHAR = 'L';
    public static final char GHOST_CHAR = 'G';
    public static final String RETURN_STRING = "Return";
    public static final String TIME_ATTACK_STRING = "남은 시간";
    public static final String STOP_WATCH_STRING = "진행 시간";

    private JTextPane singlePlayerGameBoardPane;
    private JTextPane singlePlayerNextBlockPane;
    private JLabel singleScoreLabel;
    private JLabel singleLinesLabel;
    private JLabel singleGameDisplayTimeLabel;
    private JLabel singleGameTimeLabel;
    private JLabel scoreLabel1;
    private JLabel scoreLabel2;
    private JLabel gameOverLabel;
    private JLabel victoryLabel;
    private JLabel depeatLabel;

    private JTextPane playerOneGameBoardPane;
    private JTextPane playerOneNextBlockPane;
    private JTextPane playerOneAttackLinePane;
    private JLabel playerOneScoreLabel;
    private JLabel multiGameDisplayTimeLabel;
    private JLabel multiGameTimeLabel;
    private JTextPane playerTwoGameBoardPane;
    private JTextPane playerTwoNextBlockPane;
    private JTextPane playerTwoAttackLinePane;
    private JLabel playerTwoScoreLabel;
    private JLabel singleGameModeLabel;
    private JLabel mulitiGameModeLabel;
    private JLabel gameDiffLabel;

    private JPanel StopMenu;
    private JButton continueBtn;
    private JButton restartBtn;
    private JButton returnMainBtn;
    private JButton exitGameBtn;

    private JPanel singleGameDisplayPanel;
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
    private ArrayList<JButton> buttonArrayList;

    private GameView() {
        initGameDisplayComponents();
        initSingleGameDisplayPanel();
        initMultiGameDisplayPane();
        initSelectGamePane();
        initSelectMultiGamePane();
        initSelectModePane();
        initSelectDiffPane();
        initGameOverPanel();
        initStopMenu();
        initView();
    }

    public void resetGameView() {
        super.removeAll();
        singlePlayerGameBoardPane.setText(null);
        playerOneGameBoardPane.setText(null);
        playerTwoGameBoardPane.setText(null);

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

        singleGameDisplayPanel = new JPanel();

        singlePlayerGameBoardPane = initAndSetName("singlePlayerGameBoardPane", new JTextPane());
        singlePlayerGameBoardPane.setBackground(Color.BLACK);
        singlePlayerGameBoardPane.setEditable(false);

        singlePlayerNextBlockPane = initAndSetName("singlePlayerNextBlockPane", new JTextPane());
        singlePlayerNextBlockPane.setBackground(Color.BLACK);
        singlePlayerNextBlockPane.setEditable(false);

        singleScoreLabel = new JLabel("0");
        singleScoreLabel.setFocusable(false);
        singleScoreLabel.setBackground(BASIC_BACKGROUND_COLOR);
        singleScoreLabel.setForeground(BASIC_FONT_COLOR);

        singleLinesLabel = new JLabel("0");
        singleLinesLabel.setFocusable(false);
        singleLinesLabel.setBackground(BASIC_BACKGROUND_COLOR);
        singleLinesLabel.setForeground(BASIC_FONT_COLOR);

        singleGameDisplayTimeLabel = new JLabel("TIME");

        singleGameTimeLabel = new JLabel("0");
        singleGameTimeLabel.setFocusable(false);
        singleGameTimeLabel.setBackground(BASIC_BACKGROUND_COLOR);
        singleGameTimeLabel.setForeground(BASIC_FONT_COLOR);

        playerOneGameBoardPane = initAndSetName("playerOneGameBoardPane", new JTextPane());
        playerOneGameBoardPane.setBackground(Color.BLACK);
        playerOneGameBoardPane.setEditable(false);

        playerOneNextBlockPane = initAndSetName("playerOneNextBlockPane", new JTextPane());
        playerOneNextBlockPane.setEditable(false);
        playerOneNextBlockPane.setBackground(Color.BLACK);
        playerOneNextBlockPane.setFocusable(false);

        playerOneAttackLinePane = initAndSetName("playerOneAttackLinePane", new JTextPane());
        playerOneAttackLinePane.setEditable(false);
        playerOneAttackLinePane.setBackground(Color.BLACK);
        playerOneAttackLinePane.setFocusable(false);

        playerOneScoreLabel = new JLabel("0");
        playerOneScoreLabel.setFocusable(false);

        playerTwoGameBoardPane = new JTextPane();
        playerTwoGameBoardPane.setBackground(Color.BLACK);
        playerTwoGameBoardPane.setEditable(false);

        playerTwoScoreLabel = new JLabel("0");
        playerTwoScoreLabel.setFocusable(false);

        playerTwoNextBlockPane = new JTextPane();
        playerTwoNextBlockPane.setEditable(false);
        playerTwoNextBlockPane.setBackground(Color.BLACK);
        playerTwoNextBlockPane.setFocusable(false);

        playerTwoAttackLinePane = new JTextPane();
        playerTwoAttackLinePane.setEditable(false);
        playerTwoAttackLinePane.setBackground(Color.BLACK);
        playerTwoAttackLinePane.setFocusable(false);

        multiGameDisplayPanel = new JPanel();

        multiGameDisplayTimeLabel = new JLabel(STOP_WATCH_STRING);
        multiGameDisplayTimeLabel.setHorizontalAlignment(JLabel.RIGHT);

        multiGameTimeLabel = new JLabel("");
        multiGameTimeLabel.setHorizontalAlignment(JLabel.LEFT);
        multiGameTimeLabel.setFocusable(false);
        multiGameTimeLabel.setForeground(Color.orange);

        mulitiGameModeLabel = new JLabel("게임모드");
        singleGameModeLabel = new JLabel("게임모드");
        gameDiffLabel = new JLabel("난이도");

        gameOverLabel = new JLabel("Game Over");
        victoryLabel = new JLabel("WIN!");
        depeatLabel = new JLabel("Loose...");

        StopMenu = initAndSetName("StopMenu", new JPanel());
        continueBtn = initAndSetName("continueBtn", new JButton("이어서"));
        restartBtn = initAndSetName("restartBtn", new JButton("게임 재시작"));
        returnMainBtn = initAndSetName("returnMainBtn", new JButton("메인메뉴로"));
        exitGameBtn = initAndSetName("exitGameBtn", new JButton("게임종료"));

        scoreLabel1 = initAndSetName("scoreLabel1", new JLabel("SCORE"));
        scoreLabel2 = initAndSetName("scoreLabel2", new JLabel("SCORE"));

        buttonArrayList = new ArrayList<>();
    }

    private void initSingleGameDisplayPanel() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.1, 0.1, Double.MIN_VALUE, 0.5, 0.5, 0.5, 0.5, Double.MIN_VALUE };
        singleGameDisplayPanel.setLayout(gridBagLayout);

        singleGameDisplayPanel.add(singleGameModeLabel, addGridBagComponents(0, 0, 4, 1));
        singleGameDisplayPanel.add(gameDiffLabel, addGridBagComponents(0, 1, 4, 1));

        singleGameDisplayPanel.add(singlePlayerGameBoardPane, addGridBagComponents(0, 2, 2, 5));
        singleGameDisplayPanel.add(singlePlayerNextBlockPane, addGridBagComponents(2, 2, 2, 1));
        singleGameDisplayPanel.add(singleGameDisplayTimeLabel, addGridBagComponents(2, 3, 1, 1));
        singleGameDisplayPanel.add(singleGameTimeLabel, addGridBagComponents(2, 4, 1, 1));
        singleGameDisplayPanel.add(new JLabel("Score"), addGridBagComponents(3, 3, 1, 1));
        singleGameDisplayPanel.add(singleScoreLabel, addGridBagComponents(3, 4, 1, 1));
        singleGameDisplayPanel.add(new JLabel("deleteLines"), addGridBagComponents(2, 5, 1, 1));
        singleGameDisplayPanel.add(singleLinesLabel, addGridBagComponents(2, 6, 1, 1));
        singleGameDisplayPanel.setFocusable(false);
    }

    private void initMultiGameDisplayPane() {
        GridBagLayout gbl_multiGameDisplayPanel = new GridBagLayout();
        gbl_multiGameDisplayPanel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
        gbl_multiGameDisplayPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_multiGameDisplayPanel.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
        gbl_multiGameDisplayPanel.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.4,
                Double.MIN_VALUE };
        multiGameDisplayPanel.setLayout(gbl_multiGameDisplayPanel);

        multiGameDisplayPanel.add(mulitiGameModeLabel, addGridBagComponents(0, 0, 4, 1));
        multiGameDisplayPanel.add(multiGameDisplayTimeLabel, addGridBagComponents(1, 1, 1, 1));
        multiGameDisplayPanel.add(multiGameTimeLabel, addGridBagComponents(2, 1, 1, 1));
        multiGameDisplayPanel.add(new JLabel("Player 1"), addGridBagComponents(0, 2, 2, 1));
        multiGameDisplayPanel.add(new JLabel("Player 2"), addGridBagComponents(2, 2, 2, 1));

        multiGameDisplayPanel.add(playerOneGameBoardPane, addGridBagComponents(0, 3, 1, 4));
        multiGameDisplayPanel.add(playerOneNextBlockPane, addGridBagComponents(1, 3, 1, 1));
        multiGameDisplayPanel.add(scoreLabel1, addGridBagComponents(1, 4, 1, 1));
        multiGameDisplayPanel.add(playerOneScoreLabel, addGridBagComponents(1, 5, 1, 1));
        multiGameDisplayPanel.add(playerOneAttackLinePane, addGridBagComponents(1, 6, 1, 1));

        multiGameDisplayPanel.add(playerTwoGameBoardPane, addGridBagComponents(2, 3, 1, 4));
        multiGameDisplayPanel.add(playerTwoNextBlockPane, addGridBagComponents(3, 3, 1, 1));
        multiGameDisplayPanel.add(scoreLabel2, addGridBagComponents(3, 4, 1, 1));
        multiGameDisplayPanel.add(playerTwoScoreLabel, addGridBagComponents(3, 5, 1, 1));
        multiGameDisplayPanel.add(playerTwoAttackLinePane, addGridBagComponents(3, 6, 1, 1));
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

    private void initStopMenu() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.5, 1.0, 1.0, 1.0, Double.MIN_VALUE };
        StopMenu.setLayout(gridBagLayout);

        StopMenu.add(new JLabel("게임 중단중"), addGridBagComponents(0, 0));
        StopMenu.add(continueBtn, addGridBagComponents(0, 1));
        StopMenu.add(restartBtn, addGridBagComponents(0, 2));
        StopMenu.add(returnMainBtn, addGridBagComponents(0, 3));
        StopMenu.add(exitGameBtn, addGridBagComponents(0, 4));

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

        buttonArrayList.add(singleGameBtn);
        buttonArrayList.add(mulitiGameBtn);
        buttonArrayList.add(gameReturnBtn);
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

        buttonArrayList.add(robotGameBtn);
        buttonArrayList.add(localGameBtn);
        buttonArrayList.add(onlineGameBtn);
        buttonArrayList.add(multiGameReturnBtn);
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

        buttonArrayList.add(generalModeBtn);
        buttonArrayList.add(itemModeBtn);
        buttonArrayList.add(timeAttackBtn);
        buttonArrayList.add(modeReturnBtn);
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

        buttonArrayList.add(easyBtn);
        buttonArrayList.add(normalBtn);
        buttonArrayList.add(hardBtn);
        buttonArrayList.add(diffReturnBtn);
    }

    public void setGameOver() {
        singleGameDisplayPanel.remove(singlePlayerGameBoardPane);
        singleGameDisplayPanel.add(gameOverLabel, addGridBagComponents(0, 2, 2, 5));
    }

    public void setSingleStopPanel() {
        singleGameDisplayPanel.remove(singlePlayerGameBoardPane);
        singleGameDisplayPanel.add(StopMenu, addGridBagComponents(0, 2, 2, 5));
    }

    public void resetSingleStopPanel() {
        singleGameDisplayPanel.remove(StopMenu);
        singleGameDisplayPanel.add(singlePlayerGameBoardPane, addGridBagComponents(0, 2, 2, 5));
    }

    public void setMultiStopPanel() {
        multiGameDisplayPanel.remove(playerOneGameBoardPane);
        multiGameDisplayPanel.remove(playerOneNextBlockPane);
        multiGameDisplayPanel.remove(scoreLabel1);
        multiGameDisplayPanel.remove(playerOneScoreLabel);
        multiGameDisplayPanel.remove(playerOneAttackLinePane);

        multiGameDisplayPanel.remove(playerTwoGameBoardPane);
        multiGameDisplayPanel.remove(playerTwoNextBlockPane);
        multiGameDisplayPanel.remove(scoreLabel2);
        multiGameDisplayPanel.remove(playerTwoScoreLabel);
        multiGameDisplayPanel.remove(playerTwoAttackLinePane);
        multiGameDisplayPanel.add(StopMenu, addGridBagComponents(0, 3, 4, 4));
    }

    public void resetMultiStopPanel() {
        multiGameDisplayPanel.remove(StopMenu);

        multiGameDisplayPanel.add(playerOneGameBoardPane, addGridBagComponents(0, 3, 1, 4));
        multiGameDisplayPanel.add(playerOneNextBlockPane, addGridBagComponents(1, 3, 1, 1));
        multiGameDisplayPanel.add(scoreLabel1, addGridBagComponents(1, 4, 1, 1));
        multiGameDisplayPanel.add(playerOneScoreLabel, addGridBagComponents(1, 5, 1, 1));
        multiGameDisplayPanel.add(playerOneAttackLinePane, addGridBagComponents(1, 6, 1, 1));

        multiGameDisplayPanel.add(playerTwoGameBoardPane, addGridBagComponents(2, 3, 1, 4));
        multiGameDisplayPanel.add(playerTwoNextBlockPane, addGridBagComponents(3, 3, 1, 1));
        multiGameDisplayPanel.add(scoreLabel2, addGridBagComponents(3, 4, 1, 1));
        multiGameDisplayPanel.add(playerTwoScoreLabel, addGridBagComponents(3, 5, 1, 1));
        multiGameDisplayPanel.add(playerTwoAttackLinePane, addGridBagComponents(3, 6, 1, 1));
    }

    public void resetSingleGameDisplayPane() {
        singleGameDisplayPanel.remove(gameOverLabel);
        singleGameDisplayPanel.add(singlePlayerGameBoardPane, addGridBagComponents(0, 2, 2, 5));
    }

    public void setPlayerOneWin() {
        multiGameDisplayPanel.remove(playerOneGameBoardPane);
        multiGameDisplayPanel.remove(playerTwoGameBoardPane);
        multiGameDisplayPanel.add(victoryLabel, addGridBagComponents(0, 3, 1, 4));
        multiGameDisplayPanel.add(depeatLabel, addGridBagComponents(2, 3, 1, 4));
    }

    public void setPlayerTwoWin() {
        multiGameDisplayPanel.remove(playerOneGameBoardPane);
        multiGameDisplayPanel.remove(playerTwoGameBoardPane);
        multiGameDisplayPanel.add(depeatLabel, addGridBagComponents(0, 3, 1, 4));
        multiGameDisplayPanel.add(victoryLabel, addGridBagComponents(2, 3, 1, 4));
    }

    public void resetMultiGameDisplayPane() {
        multiGameDisplayPanel.remove(depeatLabel);
        multiGameDisplayPanel.remove(victoryLabel);
        victoryLabel.setText("WIN!");
        depeatLabel.setText("Loose...");
        multiGameDisplayPanel.add(playerOneGameBoardPane, addGridBagComponents(0, 3, 1, 4));
        multiGameDisplayPanel.add(playerTwoGameBoardPane, addGridBagComponents(2, 3, 1, 4));
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

    public JTextPane getSinglePlayerGameBoardPane() {
        return this.singlePlayerGameBoardPane;
    }

    public JTextPane getSinglePlayerOneNextBlockPane() {
        return this.singlePlayerNextBlockPane;
    }

    public JLabel getSingleScoreLabel() {
        return this.singleScoreLabel;
    }

    public JLabel getSingleGameDisplayTimeLabel() {
        return this.singleGameDisplayTimeLabel;
    }

    public JLabel getSingleGameTimeLabel() {
        return this.singleGameTimeLabel;
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

    public JLabel getMultiGameDisplayTimeLabel() {
        return this.multiGameDisplayTimeLabel;
    }

    public JLabel getMultiGameTimeLabel() {
        return this.multiGameTimeLabel;
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

    public ArrayList<JButton> getButtonArrayList() {
        return buttonArrayList;
    }

    public JLabel getMultiGameModeLabel() {
        return this.mulitiGameModeLabel;
    }

    public JLabel getGameDiffLabel() {
        return this.gameDiffLabel;
    }

    public JLabel getSingleGameModeLabel() {
        return this.singleGameModeLabel;
    }

    public JLabel getSingleLinesLabel() {
        return this.singleLinesLabel;
    }

    public JLabel getGameOverLabel() {
        return this.gameOverLabel;
    }

    public JLabel getVictoryLabel() {
        return this.victoryLabel;
    }

    public JLabel getDepeatLabel() {
        return this.depeatLabel;
    }

    public JPanel getStopMenu() {
        return this.StopMenu;
    }

    public JButton getRestartBtn() {
        return this.restartBtn;
    }

    public JButton getContinueBtn() {
        return this.continueBtn;
    }

    public JButton getReturnMainBtn() {
        return this.returnMainBtn;
    }

    public JButton getExitGameBtn() {
        return this.exitGameBtn;
    }
}
