package tetris.controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.*;
import tetris.view.*;

public class ViewController extends JFrame {

    private Container contentPane;
    private HashMap<JButton, Container> viewMap;
    private Timer refresh;
    private TimerTask task;

    private MainView mainView = MainView.getInstance();
    private GameView gameView = GameView.getInstance();
    private ScoreView scoreView = ScoreView.getInstance();
    private SettingView settingView = SettingView.getInstance();

    public ViewController() {
        initView();
        addEventListner();
    }

    private void initView() {
        Rectangle screenSize = new Rectangle(0, 0, 1000, 600); // 화면크기(이후 Setting파일에 연결할 필요 있음)
        super.setTitle("Team 19 Tetris");
        super.setBounds(screenSize);

        super.setResizable(false); // 창의 크기 조정 가능 여부
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 창을 닫으면 더 이상 실행(run)되지 않는다
        super.setLocationRelativeTo(null); // 어떤 위치에 창이 나타날 것인가를 결정. 현재는 화면 정 중앙

        super.setVisible(true);

        contentPane = super.getContentPane(); // contentPane 부르기

        // View Mapping
        viewMap = new HashMap<>();
        viewMap.put(mainView.getStartBtn(), gameView);
        viewMap.put(mainView.getScoreBoardBtn(), scoreView);
        viewMap.put(mainView.getSettingBtn(), settingView);

        contentPane.setLayout(new GridLayout(1, 0, 0, 0)); // Frame의 레이아웃 방식을 설정, row 1
        contentPane.add(mainView);

        task = new TimerTask() {
            public void run() {
                revalidate();
                repaint();
            }
        };
        refresh = new Timer();
        refresh.scheduleAtFixedRate(task, 0L, 1000L);
    }

    private void addEventListner() {
        addMainViewEventListner();
        addGameViewEventListner();
        addSettingViewEventListner();
        addScoreViewEventListner();
    }

    private void addMainViewEventListner() {
        MainKeyEvent mainKeyEventListner = new MainKeyEvent();

        mainView.getStartBtn().addActionListener(e -> transitView(contentPane, gameView, mainView));
        mainView.getSettingBtn()
                .addActionListener(e -> transitView(contentPane, settingView, mainView));
        mainView.getScoreBoardBtn()
                .addActionListener(e -> transitView(contentPane, scoreView, mainView));
        mainView.getExitBtn().addActionListener(e -> System.exit(0));

        mainView.getStartBtn().requestFocus();

        mainView.getStartBtn().addKeyListener(mainKeyEventListner);
        mainView.getSettingBtn().addKeyListener(mainKeyEventListner);
        mainView.getScoreBoardBtn().addKeyListener(mainKeyEventListner);
        mainView.getExitBtn().addKeyListener(mainKeyEventListner);
    }

    private void addGameViewEventListner() {
        gameView.getReturnButton()
                .addActionListener(e -> transitView(contentPane, mainView, gameView));
    }

    private void addSettingViewEventListner() {
        SettingKeyEventListner settingKeyEventListner = new SettingKeyEventListner();

        settingView.getReturnSettingToMainBtn()
                .addActionListener(e -> transitView(contentPane, mainView, settingView));
        settingView.getReturnSettingToMainBtn().addKeyListener(settingKeyEventListner);
        settingView.getDisplayComboBox().addKeyListener(settingKeyEventListner);
        settingView.getIsColorBlindBtn().addKeyListener(settingKeyEventListner);
        settingView.getInitKeyBtn().addKeyListener(settingKeyEventListner);
        settingView.getInitMenuBtn().addKeyListener(settingKeyEventListner);
        settingView.getSetDisplayBtn().addKeyListener(settingKeyEventListner);
        settingView.getSetUpKeyBtn().addKeyListener(settingKeyEventListner);
        settingView.getSetDownKeyBtn().addKeyListener(settingKeyEventListner);
        settingView.getSetLeftKeyBtn().addKeyListener(settingKeyEventListner);
        settingView.getSetRightKeyBtn().addKeyListener(settingKeyEventListner);
        settingView.getSetStaticKeyBtn().addKeyListener(settingKeyEventListner);
        settingView.getInitKeyGridReturnBtn().addKeyListener(settingKeyEventListner);
        settingView.getInitReturnBtn().addKeyListener(settingKeyEventListner);
        settingView.getInitScoreBtn().addKeyListener(settingKeyEventListner);
        settingView.getInitSettingBtn().addKeyListener(settingKeyEventListner);
    }

    private void addScoreViewEventListner() {
        scoreView.getReturnScoreToMainBtn()
                .addActionListener(e -> transitView(contentPane, mainView, scoreView));
    }

    // 전환함수
    private void transitView(Container pane, Container to, Container from) {
        pane.add(to);
        pane.remove(from);
        focus(to);
        revalidate(); // component 변화 후 JFrame 새로고침(component 변화 적용) */
        repaint(); // component 변화 후 JFrame 새로고침(component 색 등의 성질 적용) */
    }

    private void focus(Container to) {
        if (to.equals(mainView)) {
            mainView.getStartBtn().requestFocus();
        }
        else if (to.equals(gameView)) {
            gameView.getReturnButton().requestFocus();
        }
        else if (to.equals(settingView)) {
            settingView.getReturnSettingToMainBtn().requestFocus();
        }
    }

    private class MainKeyEvent extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    e.getComponent().transferFocusBackward();
                    break;
                case KeyEvent.VK_DOWN:
                    e.getComponent().transferFocus();
                    break;
                case KeyEvent.VK_ENTER:
                    transitView(contentPane, viewMap.get(e.getComponent()), mainView);
                    break;
                default:
                    break;
            }
        }
    }

    private class SettingKeyEventListner extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            Component comp = e.getComponent();
            JComboBox<String> displayComboBox = settingView.getDisplayComboBox();

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (comp.equals(displayComboBox)) {
                        displayComboBox
                                .setSelectedIndex((displayComboBox.getSelectedIndex() - 1 == -1) ? 2
                                        : displayComboBox.getSelectedIndex() - 1);
                        displayComboBox.hidePopup();
                    }
                    else {
                        comp.transferFocusBackward();
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (comp.equals(displayComboBox)) {
                        displayComboBox
                                .setSelectedIndex((3 == displayComboBox.getSelectedIndex() + 1) ? 0
                                        : displayComboBox.getSelectedIndex() + 1);
                        displayComboBox.hidePopup();
                    }
                    else {
                        comp.transferFocus();
                    }
                    break;

                case KeyEvent.VK_SPACE:
                    if (comp.equals(settingView.getReturnSettingToMainBtn())) {
                        transitView(contentPane, mainView, settingView);
                        settingView.setInitKeyBtnsFocusable(false);
                        settingView.setSettingBtnsFocusable(true);
                        settingView.setInitKeyBtnsFocusable(false);
                    }

                    else if (comp.equals(settingView.getIsColorBlindBtn())) {
                        // 이후 수정해야함
                        if (settingView.getIsColorBlindBtn().getText().equals("ON"))
                            settingView.getIsColorBlindBtn().setText("OFF");
                        else
                            settingView.getIsColorBlindBtn().setText("ON");

                    }

                    else if (comp.equals(settingView.getSetDisplayBtn())) {
                        displayComboBox.setFocusable(true);
                        displayComboBox.requestFocus();
                    }

                    else if (comp.equals(displayComboBox)) {
                        displayComboBox.setFocusable(false);
                        settingView.getSetDisplayBtn().requestFocus();
                    }

                    else if (comp.equals(settingView.getInitKeyBtn())) {
                        settingView.setInitKeyBtnsFocusable(true);
                        settingView.setSettingBtnsFocusable(false);
                        settingView.getSetUpKeyBtn().requestFocus();
                        settingView.getInitKeyBtn().doClick(100);
                    }

                    else if (comp.equals(settingView.getInitKeyGridReturnBtn())) {
                        settingView.setInitKeyBtnsFocusable(false);
                        settingView.setSettingBtnsFocusable(true);
                        settingView.getInitKeyBtn().requestFocus();
                    }

                    else if (comp.equals(settingView.getInitMenuBtn())) {
                        settingView.setInitSettingBtnsFocusable(true);
                        settingView.setSettingBtnsFocusable(false);
                        settingView.getInitReturnBtn().requestFocus();
                    }

                    else if (comp.equals(settingView.getInitReturnBtn())) {
                        settingView.setInitSettingBtnsFocusable(false);
                        settingView.setSettingBtnsFocusable(true);
                        settingView.getInitMenuBtn().requestFocus();
                    }
                    break;

                case KeyEvent.VK_LEFT:
                    comp.transferFocusBackward();
                    break;

                case KeyEvent.VK_RIGHT:
                    comp.transferFocus();
                    break;

                default:
                    break;
            }
        }
    }
}
