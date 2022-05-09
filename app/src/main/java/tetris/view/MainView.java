package tetris.view;

import javax.swing.*;
import static javax.swing.SwingConstants.CENTER;

import java.awt.GridLayout;
import java.awt.Component;

import java.util.*;

public class MainView extends JPanel {

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
        buttonPanel.setLayout(new GridLayout(4, 0, 0, 0));

        startBtn = initButton("startBtn", new JButton("Game Start"));
        settingBtn = initButton("settingBtn", new JButton("Settings"));
        scoreBoardBtn = initButton("scoreBoardBtn", new JButton("Scoreboard"));
        exitBtn = initButton("exitBtn", new JButton("Exit"));

        buttonList = new ArrayList<>(Arrays.asList(
                startBtn, settingBtn, scoreBoardBtn, exitBtn));

        for (JButton button : buttonList) {
            buttonPanel.add(button);
        }

        JLabel titleLabel = new JLabel("Tetris");
        titleLabel.setHorizontalAlignment(CENTER);

        super.setLayout(new GridLayout(2, 0, 0, 0));
        super.add(titleLabel);
        super.add(buttonPanel);
    }

    private JButton initButton(String name, JButton comp) {
        comp.setName(name);
        comp.setAlignmentX(Component.CENTER_ALIGNMENT);
        comp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");
        return comp;
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
