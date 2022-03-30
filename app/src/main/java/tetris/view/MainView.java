package tetris.view;

import javax.swing.*;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Component;

import java.util.*;

public class MainView extends JPanel {

    private JPanel buttonPanel;
    private JButton startButton;
    private JButton settingButton;
    private JButton scoreBoardButton;
    private JButton exitButton;
    private List<JButton> buttonList;

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
        startButton = new JButton("Game Start");
        settingButton = new JButton("Settings");
        scoreBoardButton = new JButton("Scoreboard");
        exitButton = new JButton("Exit");

        buttonList.add(startButton);
        buttonList.add(settingButton);
        buttonList.add(scoreBoardButton);
        buttonList.add(exitButton);

        addAButton(buttonList, buttonPanel);

        JLabel appName = new JLabel("Tetris");
        appName.setHorizontalAlignment(SwingConstants.CENTER);

        super.setLayout(new GridLayout(2, 0, 0, 0)); // row 2, colmn 0
        super.add(appName);
        super.add(buttonPanel);
    }

    public List<JButton> getButtonList() {
        return this.buttonList;
    }

    public void setButtonList(List<JButton> buttonList) {
        this.buttonList = buttonList;
    }

    public JPanel getButtonPanel() {
        return this.buttonPanel;
    }

    public void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }

    public JButton getStartButton() {
        return this.startButton;
    }

    public void setStartButton(JButton startButton) {
        this.startButton = startButton;
    }

    public JButton getSettingButton() {
        return this.settingButton;
    }

    public void setSettingButton(JButton settingButton) {
        this.settingButton = settingButton;
    }

    public JButton getScoreBoardButton() {
        return this.scoreBoardButton;
    }

    public void setScoreBoardButton(JButton scoreBoardButton) {
        this.scoreBoardButton = scoreBoardButton;
    }

    public JButton getExitButton() {
        return this.exitButton;
    }

    public void setExitButton(JButton exitButton) {
        this.exitButton = exitButton;
    }

    // 언제든지 Button객체를 추가할 수 있는 method
    private void addAButton(List<JButton> buttons, Container container) {
        for (JButton button : buttons) {
            button.setName(button.getText());
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(button);
        }
    }

}
