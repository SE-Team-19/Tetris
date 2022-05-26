package tetris.view;

import static javax.swing.SwingConstants.CENTER;
import java.awt.*;

import java.util.*;
import java.util.List;

public class MainView extends MasterView {

    private JPanel buttonPanel;
    private JButton startBtn;
    private JButton settingBtn;
    private JButton scoreBoardBtn;
    private JButton exitBtn;
    private JLabel appNameLabel;
    private JLabel allKeyLabel;

    private ArrayList<JButton> buttonList;

    private MainView() {
        initView();
    }

    private static class LazyHolder {
        private static final MainView INSTANCE = new MainView();
    }

    public static MainView getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void initView() {
        buttonPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1, 0.2, 1, Double.MIN_VALUE };

        startBtn = initAndSetName("startBtn", new JButton("Game Start"));
        settingBtn = initAndSetName("settingBtn", new JButton("Settings"));
        scoreBoardBtn = initAndSetName("scoreBoardBtn", new JButton("Scoreboard"));
        exitBtn = initAndSetName("exitBtn", new JButton("Exit"));
        deleteKeyBinding(startBtn, settingBtn, scoreBoardBtn, exitBtn);

        buttonList = new ArrayList<>();
        buttonList.add(startBtn);
        buttonList.add(settingBtn);
        buttonList.add(scoreBoardBtn);
        buttonList.add(exitBtn);

        addAButton(buttonList, buttonPanel);

        appNameLabel = new JLabel("Tetris");
        appNameLabel.setHorizontalAlignment(CENTER);
        allKeyLabel = new JLabel("Player1 키: Up,Down,Left,Right,SPACE Player2 키: W,S,A,D,R");

        super.setLayout(gridBagLayout);
        super.add(appNameLabel, addGridBagComponents(0, 0));
        super.add(allKeyLabel, addGridBagComponents(0, 1));
        super.add(buttonPanel, addGridBagComponents(0, 2));
    }

    public List<JButton> getButtonList() {
        return this.buttonList;
    }

    public JPanel getButtonPanel() {
        return this.buttonPanel;
    }

    public JButton getStartBtn() {
        return this.startBtn;
    }

    public JButton getSettingBtn() {
        return this.settingBtn;
    }

    public JButton getScoreBoardBtn() {
        return this.scoreBoardBtn;
    }

    public JButton getExitBtn() {
        return this.exitBtn;
    }

    public JLabel getAppNameLabel() {
        return this.appNameLabel;
    }

    public JLabel getAllKeyLabel() {
        return this.allKeyLabel;
    }
}
