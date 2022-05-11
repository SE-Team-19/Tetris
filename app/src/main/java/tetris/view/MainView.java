package tetris.view;

import java.awt.Container;
import javax.swing.*;
import static javax.swing.SwingConstants.CENTER;

import java.awt.GridLayout;
import java.awt.Component;

import java.util.*;

public class MainView extends JPanel {

    private JPanel buttonPanel;
    private JButton startBtn;
    private JButton battleModeBtn;
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
        buttonPanel.setLayout(new GridLayout(5, 0, 0, 0));

        startBtn = initButton("startBtn", new JButton("Game Start"));
        battleModeBtn = initButton("battleModeBtn", new JButton("BattleMode Start"));
        settingBtn = initButton("settingBtn", new JButton("Settings"));
        scoreBoardBtn = initButton("scoreBoardBtn", new JButton("Scoreboard"));
        exitBtn = initButton("exitBtn", new JButton("Exit"));
        deleteKeyBinding(startBtn, battleModeBtn, settingBtn, scoreBoardBtn, exitBtn);

        buttonList = new ArrayList<>(Arrays.asList(
                startBtn, battleModeBtn, settingBtn, scoreBoardBtn, exitBtn));

        for (JButton button : buttonList) {
            buttonPanel.add(button);
        }

        addAButton(buttonList, buttonPanel);

        JLabel titleLabel = new JLabel("Tetris");
        titleLabel.setHorizontalAlignment(CENTER);

        super.setLayout(new GridLayout(2, 0, 0, 0));
        super.add(titleLabel);
        super.add(buttonPanel);
    }

    private void addAButton(ArrayList<JButton> buttons, Container container) {
        buttonPanel.setLayout(new GridLayout(buttons.size(), 0, 0, 0));
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(button);
        }
    }

    private JButton initButton(String name, JButton comp) {
        comp.setName(name);
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        comp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");
        return comp;
    }

    private void deleteKeyBinding(JComponent... comps) {
        for (JComponent comp : comps)
            comp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");
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

    public JButton getBattleModeBtn() { return this.battleModeBtn; }

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
