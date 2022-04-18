package tetris.controller;

<<<<<<< HEAD
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.*;
import tetris.model.Game;
import tetris.view.*;
=======
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.awt.GridLayout;
import java.awt.Component;

import tetris.model.Setting;
import tetris.view.*;
import java.awt.event.KeyAdapter;
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba

public class ViewController extends JFrame {

    private Container contentPane;
<<<<<<< HEAD
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
=======
    MainView mainView = MainView.getInstance();
    GameView gameView = GameView.getInstance();
    ScoreView scoreView = ScoreView.getInstance();
    SettingView settingView = SettingView.getInstance();
    private Setting setting;
    private HashMap<JButton, Container> viewMap;
    private Rectangle screenSize;
    private Timer refresh;
    private TimerTask task;

    public ViewController() {
        initView();
        addMainViewEvent();
        addGameViewEvent();
        addSettingViewEvent();
        addScoreViewEvent();
    }

    private void initView() {
        screenSize = new Rectangle(0, 0, 1000, 600); // 화면크기(이후 Setting파일에 연결할 필요 있음)
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
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

<<<<<<< HEAD
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
=======
    private void addMainViewEvent() {
        MainKeyEvent mainKeyListener = new MainKeyEvent();

        mainView.getStartBtn()
                .addActionListener(e -> viewTransion(contentPane, gameView, mainView));
        mainView.getSettingBtn()
                .addActionListener(e -> viewTransion(contentPane, settingView, mainView));
        mainView.getScoreBoardBtn()
                .addActionListener(e -> viewTransion(contentPane, scoreView, mainView));
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
        mainView.getExitBtn().addActionListener(e -> System.exit(0));

        mainView.getStartBtn().requestFocus();

<<<<<<< HEAD
        mainView.getStartBtn().addKeyListener(mainKeyEventListner);
        mainView.getSettingBtn().addKeyListener(mainKeyEventListner);
        mainView.getScoreBoardBtn().addKeyListener(mainKeyEventListner);
        mainView.getExitBtn().addKeyListener(mainKeyEventListner);
    }

    private void addGameViewEventListner() {
        gameView.getReturnGameToMainBtn()
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
=======
        mainView.getStartBtn().addKeyListener(mainKeyListener);
        mainView.getSettingBtn().addKeyListener(mainKeyListener);
        mainView.getScoreBoardBtn().addKeyListener(mainKeyListener);
        mainView.getExitBtn().addKeyListener(mainKeyListener);
    }

    private void addGameViewEvent() {
        gameView.getReturnGameToMainBtn()
                .addActionListener(e -> viewTransion(contentPane, mainView, gameView));
    }

    private void addSettingViewEvent() {
        SettingKeyEvent settingKeyEvent = new SettingKeyEvent();
        settingView.getReturnSettingToMainBtn()
                .addActionListener(e -> viewTransion(contentPane, mainView, settingView));
        settingView.getReturnSettingToMainBtn().addKeyListener(settingKeyEvent);
        settingView.getDisplayComboBox().addKeyListener(settingKeyEvent);
        settingView.getIsColorBlindBtn().addKeyListener(settingKeyEvent);
        settingView.getInitKeyBtn().addKeyListener(settingKeyEvent);
        settingView.getInitMenuBtn().addKeyListener(settingKeyEvent);
        settingView.getSetDisplayBtn().addKeyListener(settingKeyEvent);
        settingView.getSetUpKeyBtn().addKeyListener(settingKeyEvent);
        settingView.getSetDownKeyBtn().addKeyListener(settingKeyEvent);
        settingView.getSetLeftKeyBtn().addKeyListener(settingKeyEvent);
        settingView.getSetRightKeyBtn().addKeyListener(settingKeyEvent);
        settingView.getSetStaticKeyBtn().addKeyListener(settingKeyEvent);
        settingView.getInitKeyGridReturnBtn().addKeyListener(settingKeyEvent);
        settingView.getInitReturnBtn().addKeyListener(settingKeyEvent);
        settingView.getInitScoreBtn().addKeyListener(settingKeyEvent);
        settingView.getInitSettingBtn().addKeyListener(settingKeyEvent);
    }

    private void addScoreViewEvent() {
        scoreView.getReturnScoreToMainBtn()
                .addActionListener(e -> viewTransion(contentPane, mainView, scoreView));
    }

    // 전환함수
    private void viewTransion(Container pane, Container to, Container from) {
        pane.add(to);
        pane.remove(from);
        focusing(to);
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
        revalidate(); // component 변화 후 JFrame 새로고침(component 변화 적용) */
        repaint(); // component 변화 후 JFrame 새로고침(component 색 등의 성질 적용) */
    }

<<<<<<< HEAD
    private void focus(Container to) {
        if (to.equals(mainView)) {
            mainView.getStartBtn().requestFocus();
        }
        else if (to.equals(gameView)) {
            gameView.getReturnGameToMainBtn().requestFocus();
        }
        else if (to.equals(settingView)) {
=======
    private void focusing(Container to) {
        if (to.equals(mainView)) {
            mainView.getStartBtn().requestFocus();
        } else if (to.equals(gameView)) {
            gameView.getReturnGameToMainBtn().requestFocus();
        } else if (to.equals(settingView)) {
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
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
<<<<<<< HEAD
                    transitView(contentPane, viewMap.get(e.getComponent()), mainView);
=======
                    viewTransion(contentPane, viewMap.get(e.getComponent()), mainView);
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
                    break;
                default:
                    break;
            }
        }
    }

<<<<<<< HEAD
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
=======
    private class SettingKeyEvent extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (e.getComponent().equals(settingView.getDisplayComboBox())) {
                        settingView.getDisplayComboBox()
                                .setSelectedIndex((-1 == settingView
                                        .getDisplayComboBox().getSelectedIndex() - 1)
                                                ? settingView.getDisplayList().length - 1
                                                : settingView.getDisplayComboBox().getSelectedIndex() - 1);
                        settingView.getDisplayComboBox().hidePopup();
                    } else
                        e.getComponent().transferFocusBackward();
                    break;
                case KeyEvent.VK_DOWN:
                    if (e.getComponent().equals(settingView.getDisplayComboBox())) {
                        settingView.getDisplayComboBox()
                                .setSelectedIndex((settingView.getDisplayList().length == settingView
                                        .getDisplayComboBox().getSelectedIndex() + 1)
                                                ? 0
                                                : settingView.getDisplayComboBox().getSelectedIndex() + 1);
                        settingView.getDisplayComboBox().hidePopup();
                    } else
                        e.getComponent().transferFocus();
                    break;
                case KeyEvent.VK_SPACE:
                    if (e.getComponent().equals(settingView.getReturnSettingToMainBtn())) {
                        viewTransion(contentPane, mainView, settingView);
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
                        settingView.setInitKeyBtnsFocusable(false);
                        settingView.setSettingBtnsFocusable(true);
                        settingView.setInitKeyBtnsFocusable(false);
                    }

<<<<<<< HEAD
                    else if (comp.equals(settingView.getIsColorBlindBtn())) {
=======
                    if (e.getComponent().equals(settingView.getIsColorBlindBtn())) {

>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
                        // 이후 수정해야함
                        if (settingView.getIsColorBlindBtn().getText().equals("ON"))
                            settingView.getIsColorBlindBtn().setText("OFF");
                        else
                            settingView.getIsColorBlindBtn().setText("ON");

                    }
<<<<<<< HEAD

                    else if (comp.equals(settingView.getSetDisplayBtn())) {
                        displayComboBox.setFocusable(true);
                        displayComboBox.requestFocus();
                    }

                    else if (comp.equals(displayComboBox)) {
                        displayComboBox.setFocusable(false);
                        settingView.getSetDisplayBtn().requestFocus();
                    }

                    else if (comp.equals(settingView.getInitKeyBtn())) {
=======
                    if (e.getComponent().equals(settingView.getSetDisplayBtn())) {
                        settingView.getDisplayComboBox().setFocusable(true);
                        settingView.getDisplayComboBox().requestFocus();
                    }
                    if (e.getComponent().equals(settingView.getDisplayComboBox())) {
                        settingView.getDisplayComboBox().setFocusable(false);
                        settingView.getSetDisplayBtn().requestFocus();
                    }
                    if (e.getComponent().equals(settingView.getInitKeyBtn())) {
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
                        settingView.setInitKeyBtnsFocusable(true);
                        settingView.setSettingBtnsFocusable(false);
                        settingView.getSetUpKeyBtn().requestFocus();
                        settingView.getInitKeyBtn().doClick(100);
                    }
<<<<<<< HEAD

                    else if (comp.equals(settingView.getInitKeyGridReturnBtn())) {
=======
                    if (e.getComponent().equals(settingView.getInitKeyGridReturnBtn())) {
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
                        settingView.setInitKeyBtnsFocusable(false);
                        settingView.setSettingBtnsFocusable(true);
                        settingView.getInitKeyBtn().requestFocus();
                    }
<<<<<<< HEAD

                    else if (comp.equals(settingView.getInitMenuBtn())) {
=======
                    if (e.getComponent().equals(settingView.getInitMenuBtn())) {
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
                        settingView.setInitSettingBtnsFocusable(true);
                        settingView.setSettingBtnsFocusable(false);
                        settingView.getInitReturnBtn().requestFocus();
                    }
<<<<<<< HEAD

                    else if (comp.equals(settingView.getInitReturnBtn())) {
=======
                    if (e.getComponent().equals(settingView.getInitReturnBtn())) {
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
                        settingView.setInitSettingBtnsFocusable(false);
                        settingView.setSettingBtnsFocusable(true);
                        settingView.getInitMenuBtn().requestFocus();
                    }
                    break;
<<<<<<< HEAD

                case KeyEvent.VK_LEFT:
                    comp.transferFocusBackward();
                    break;

                case KeyEvent.VK_RIGHT:
                    comp.transferFocus();
                    break;

=======
                case KeyEvent.VK_LEFT:
                    e.getComponent().transferFocusBackward();
                    break;
                case KeyEvent.VK_RIGHT:
                    e.getComponent().transferFocus();
                    break;
>>>>>>> d1e8c364a27fa8d56ada43ea8358619767b3e6ba
                default:
                    break;
            }
        }
    }

}
