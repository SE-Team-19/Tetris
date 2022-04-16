package tetris.view;

import javax.swing.*;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Component;

import java.util.*;

public class MainView extends JPanel {

    private static final MainView mainView = new MainView();

    private JPanel buttonPanel;
    private JLabel appName;
    private JButton startBtn;
    private JButton settingBtn;
    private JButton scoreBoardBtn;
    private JButton exitBtn;
    private ArrayList<JButton> buttonList;

    /* singleton Instance (LazyHolder) */
    private MainView() {
        initMainView();
    }

    private static class LazyHolder {
        private static final MainView INSTANCE = new MainView();
    }

    public static MainView getInstance() {
        return LazyHolder.INSTANCE;
    }

    /***************************************/

    private void initMainView() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // BoxLayout으로 세로정렬
        buttonList = new ArrayList<>(); // ArrayList<JButton> 생성
        startBtn = initAndSetName("startBtn", new JButton("Game Start"));
        settingBtn = initAndSetName("settingBtn", new JButton("Settings"));
        scoreBoardBtn = initAndSetName("scoreBoardBtn", new JButton("Scoreboard"));
        exitBtn = initAndSetName("exitBtn", new JButton("Exit"));

        buttonList.add(startBtn);
        buttonList.add(settingBtn);
        buttonList.add(scoreBoardBtn);
        buttonList.add(exitBtn);

        addAButton(buttonList, buttonPanel);

        appName = new JLabel("Tetris");
        appName.setHorizontalAlignment(JLabel.CENTER);

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

    // 언제든지 Button객체를 추가할 수 있는 method
    private void addAButton(ArrayList<JButton> buttons, Container container) {
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(button);
        }
    }

    private <T extends JComponent> T initAndSetName(String name, T comp) {
        comp.setName(name);
        return comp;
    }

}
