package tetris.controller;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.awt.GridLayout;

import tetris.model.Setting;
import tetris.view.*;
import java.awt.event.KeyAdapter;

public class ViewController extends JFrame {

    private Container contentPane;
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
        super.setTitle("Team 19 Tetris");
        super.setBounds(screenSize);

        super.setResizable(false); // 창의 크기 조정 가능 여부
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 창을 닫으면 더 이상 실행(run)되지 않는다
        super.setLocationRelativeTo(null); // 어떤 위치에 창이 나타날 것인가를 결정. 현재는 화면 정 중앙

        super.setVisible(true);

        contentPane = super.getContentPane(); // contentPane 부르기

        // View Mapping
        viewMap = new HashMap<>();
        viewMap.put(mainView.getStartButton(), gameView);
        viewMap.put(mainView.getScoreBoardButton(), scoreView);
        viewMap.put(mainView.getSettingButton(), settingView);

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

    private void addMainViewEvent() {
        MainKeyEvent mainKeyListener = new MainKeyEvent();

        mainView.getStartButton()
                .addActionListener(e -> viewTransion(contentPane, gameView, mainView));
        mainView.getSettingButton()
                .addActionListener(e -> viewTransion(contentPane, settingView, mainView));
        mainView.getScoreBoardButton()
                .addActionListener(e -> viewTransion(contentPane, scoreView, mainView));

        mainView.getStartButton().requestFocus();

        mainView.getStartButton().addKeyListener(mainKeyListener);
        mainView.getSettingButton().addKeyListener(mainKeyListener);
        mainView.getScoreBoardButton().addKeyListener(mainKeyListener);
        mainView.getExitButton().addKeyListener(mainKeyListener);
    }

    private void addGameViewEvent() {
        gameView.getReturnButton()
                .addActionListener(e -> viewTransion(contentPane, mainView, gameView));
    }

    private void addSettingViewEvent() {
        SettingKeyEvent settingKeyEvent = new SettingKeyEvent();
        settingView.getReturnMenuBtn()
                .addActionListener(e -> viewTransion(contentPane, mainView, settingView));
        settingView.getReturnMenuBtn().addKeyListener(settingKeyEvent);
        settingView.getDisplayComboBox().addKeyListener(settingKeyEvent);
        settingView.getIsColorBlindBtn().addKeyListener(settingKeyEvent);
        settingView.getInitKeyButton().addKeyListener(settingKeyEvent);
        settingView.getInitMenuBtn().addKeyListener(settingKeyEvent);
        settingView.getSetDisplayBtn().addKeyListener(settingKeyEvent);
        settingView.getSetUpkeyBtn().addKeyListener(settingKeyEvent);
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
        scoreView.getReturnButton()
                .addActionListener(e -> viewTransion(contentPane, mainView, scoreView));
    }

    // 전환함수
    private void viewTransion(Container pane, Container to, Container from) {
        pane.add(to);
        pane.remove(from);
        focusing(to);
        revalidate(); // component 변화 후 JFrame 새로고침(component 변화 적용) */
        repaint(); // component 변화 후 JFrame 새로고침(component 색 등의 성질 적용) */
    }

    private void focusing(Container to) {
        if (to.equals(mainView)) {
            mainView.getStartButton().requestFocus();
        } else if (to.equals(gameView)) {
            gameView.getReturnButton().requestFocus();
        } else if (to.equals(settingView)) {
            settingView.getReturnMenuBtn().requestFocus();
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
                    viewTransion(contentPane, viewMap.get(e.getComponent()), mainView);
                    break;
                default:
                    break;
            }
        }
    }

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
                    if (e.getComponent().equals(settingView.getReturnMenuBtn())) {
                        viewTransion(contentPane, mainView, settingView);
                        settingView.setInitKeyBtnsFocusable(false);
                        settingView.setSettingBtnsFocusable(true);
                        settingView.setInitKeyBtnsFocusable(false);
                    }

                    if (e.getComponent().equals(settingView.getIsColorBlindBtn())) {

                        // 이후 수정해야함
                        if (settingView.getIsColorBlindBtn().getText().equals("ON"))
                            settingView.getIsColorBlindBtn().setText("OFF");
                        else
                            settingView.getIsColorBlindBtn().setText("ON");

                    }
                    if (e.getComponent().equals(settingView.getSetDisplayBtn())) {
                        settingView.getDisplayComboBox().setFocusable(true);
                        settingView.getDisplayComboBox().requestFocus();
                    }
                    if (e.getComponent().equals(settingView.getDisplayComboBox())) {
                        settingView.getDisplayComboBox().setFocusable(false);
                        settingView.getSetDisplayBtn().requestFocus();
                    }
                    if (e.getComponent().equals(settingView.getInitKeyButton())) {
                        settingView.setInitKeyBtnsFocusable(true);
                        settingView.setSettingBtnsFocusable(false);
                        settingView.getSetUpkeyBtn().requestFocus();
                        settingView.getInitKeyButton().doClick(100);
                    }
                    if (e.getComponent().equals(settingView.getInitKeyGridReturnBtn())) {
                        settingView.setInitKeyBtnsFocusable(false);
                        settingView.setSettingBtnsFocusable(true);
                        settingView.getInitKeyButton().requestFocus();
                    }
                    if (e.getComponent().equals(settingView.getInitMenuBtn())) {
                        settingView.setInitSettingBtnsFocusable(true);
                        settingView.setSettingBtnsFocusable(false);
                        settingView.getInitReturnBtn().requestFocus();
                    }
                    if (e.getComponent().equals(settingView.getInitReturnBtn())) {
                        settingView.setInitSettingBtnsFocusable(false);
                        settingView.setSettingBtnsFocusable(true);
                        settingView.getInitMenuBtn().requestFocus();
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    e.getComponent().transferFocusBackward();
                    break;
                case KeyEvent.VK_RIGHT:
                    e.getComponent().transferFocus();
                    break;
                default:
                    break;
            }
        }
    }

}
