package tetris.view;

import javax.swing.*;
import static javax.swing.SwingConstants.CENTER;

import java.awt.Color;
import java.awt.GridLayout;

import java.util.*;

public class MainView extends MasterView {

    private JPanel buttonPanel;
    private JButton startBtn;
    private JButton settingBtn;
    private JButton scoreBoardBtn;
    private JButton exitBtn;
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

        JLabel appName = new JLabel("Tetris");
        appName.setHorizontalAlignment(CENTER);

        super.setLayout(new GridLayout(2, 0, 0, 0));
        super.add(appName);
        super.add(buttonPanel);
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
}
