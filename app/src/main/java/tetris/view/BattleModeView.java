package tetris.view;

import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.CompoundBorder;

public class BattleModeView extends JPanel {

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
    private JTextPane playerTwoGameBoardPane;
    private JTextPane playerTwoNextBlockPane;
    private JTextPane playerTwoAttackLinePane;
    private JTextPane playerTwoScorePane;
    private JTextPane playerTwoTimePane;

    private JPanel selectBattleModePanel;
    private JPanel battleModeDisplayPanel;

    private JButton generalModeBtn;
    private JButton itemModeBtn;
    private JButton timeAttackBtn;

    //private PlayerOneView playerOneView;
    //private PlayerTwoView playerTwoView;

    private BattleModeView() {
        initBattleGameDisplayPane();
        initSelectBattleModePane();
        initView();
    }

    private static class LazyHolder {
        private static BattleModeView INSTANCE = new BattleModeView();
    }

    public static BattleModeView getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void initView() {
        setFocusable(true);

        super.setLayout(new GridLayout(1, 2, 0, 0));
        super.add(selectBattleModePanel);
    }

    private void initBattleGameDisplayPane() {
        battleModeDisplayPanel = new JPanel();
        battleModeDisplayPanel.setLayout(new GridLayout(1, 4, 0, 0));

        /* Player One */
        playerOneGameBoardPane = new JTextPane();
        playerOneGameBoardPane.setBackground(Color.BLACK);
        playerOneGameBoardPane.setEditable(false);
        CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
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

        /* Player Two */
        playerTwoGameBoardPane = new JTextPane();
        playerTwoGameBoardPane.setBackground(Color.BLACK);
        playerTwoGameBoardPane.setEditable(false);
         /*CompoundBorder border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GRAY, 10),
            BorderFactory.createLineBorder(Color.DARK_GRAY, 5)); */
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
        infoTwoPane.add(playerOneNextBlockPane);
        infoTwoPane.add(playerOneScorePane);
        infoTwoPane.add(playerOneTimePane);
        infoTwoPane.add(playerOneAttackLinePane);

        battleModeDisplayPanel.add(playerOneGameBoardPane);
        battleModeDisplayPanel.add(infoOnePane);
        battleModeDisplayPanel.add(playerTwoGameBoardPane);
        battleModeDisplayPanel.add(infoTwoPane);

        /* 이 부분을 PlayerOneView, PlayerTwoView 에서 끌어 온다.
        playerOneView = PlayerOneView.getInstance();
        playerTwoView = PlayerTwoView.getInstance();
        BattleModeDisplayPanel.add(playerOneView);
        BattleModeDisplayPanel.add(playerTwoView); */
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

    private void initSelectBattleModePane() {
        selectBattleModePanel = new JPanel();
        selectBattleModePanel.setLayout(new GridLayout(0, 3, 0, 0));

        generalModeBtn = initAndSetName("generalModeBtn", new JButton("일반모드"));
        itemModeBtn = initAndSetName("itemModeBtn", new JButton("아이템모드"));
        timeAttackBtn = initAndSetName("timeAttackBtn", new JButton("시간제한모드"));

        selectBattleModePanel.add(generalModeBtn);
        selectBattleModePanel.add(itemModeBtn);
        selectBattleModePanel.add(timeAttackBtn);
    }



    private <T extends JComponent> T initAndSetName(String name, T comp) {
        comp.setName(name);
        return comp;
    }

    public JTextPane getPlayerOneGameBoardPane() {
        return playerOneGameBoardPane;
    }

    public JTextPane getPlayerOneNextBlockPane() {
        return playerOneNextBlockPane;
    }

    public JTextPane getPlayerOneAttackLinePane() {
        return playerOneAttackLinePane;
    }

    public JTextPane getPlayerOneScorePane() {
        return playerOneScorePane;
    }

    public JTextPane getPlayerOneTimePane() {
        return playerOneTimePane;
    }

    public JTextPane getPlayerTwoGameBoardPane() {
        return playerTwoGameBoardPane;
    }

    public JTextPane getPlayerTwoNextBlockPane() {
        return playerTwoNextBlockPane;
    }

    public JTextPane getPlayerTwoAttackLinePane() {
        return playerTwoAttackLinePane;
    }

    public JTextPane getPlayerTwoScorePane() {
        return playerTwoScorePane;
    }

    public JTextPane getPlayerTwoTimePane() {
        return playerTwoTimePane;
    }

    public JPanel getBattleModeDisplayPane() {
        return this.battleModeDisplayPanel;
    }

    public JPanel getSelectBattleModePane() {
        return this.selectBattleModePanel;
    }

    public JButton getGeneralModeBtn() {
        return this.generalModeBtn;
    }

    public JButton getItemModeBtn() {
        return this.itemModeBtn;
    }

    public JButton getTimeAttackBtn() {
        return this.timeAttackBtn;
    }

}
