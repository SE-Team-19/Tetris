package tetris.view;

import javax.swing.*;
import java.awt.*;

public class BattleModeView extends JPanel {

    public static final int BORDER_HEIGHT = 20;
    public static final int BORDER_WIDTH = 10;
    public static final char BORDER_CHAR = 'X';
    public static final char BLOCK_CHAR = 'O';
    public static final char NULL_CHAR = ' ';
    public static final char BOMB_CHAR = 'T';
    public static final char ONELINE_CHAR = 'L';
    public static final char GHOST_CHAR = 'G';

    private JPanel selectBattleModePanel;
    private JPanel BattleModeDisplayPanel;
    private JButton itemModeBtn;
    private JButton generalModeBtn;
    private JButton timeAttackBtn;

    private PlayerOneView playerOneView;
    private PlayerTwoView playerTwoView;

    private BattleModeView() {
        initView();
    }

    private static class LazyHolder {
        private static BattleModeView INSTANCE = new BattleModeView();
    }

    public static BattleModeView getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void initView() {
        initBattleGameDisplayPane();
        initSelectBattleModePane();
    }

    private void initBattleGameDisplayPane() {
        // 이 부분을 PlayerOneView, PlayerTwoView 에서 끌어 온다.
        BattleModeDisplayPanel = new JPanel();
        BattleModeDisplayPanel.setLayout(new GridLayout(1, 2, 0, 0));

        playerOneView = PlayerOneView.getInstance();
        playerTwoView = PlayerTwoView.getInstance();

        BattleModeDisplayPanel.add(playerOneView);
        BattleModeDisplayPanel.add(playerTwoView);
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

    public JPanel getSelectBattleModePane() {
        return this.selectBattleModePanel;
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
