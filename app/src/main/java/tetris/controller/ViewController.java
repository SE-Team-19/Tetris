package tetris.controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.Timer;
import java.util.*;

import tetris.view.*;
import static tetris.view.SettingView.KEY_CHANGE_MESSAGE;

public class ViewController extends JFrame {

    private Container contentPane;
    private HashMap<JButton, Container> viewMap;
    private transient Map<KeyPair, PressedKeyEvent> mainViewKeyMap;
    private transient Map<KeyPair, PressedKeyEvent> settingMap;
    private transient SettingViewKeyMap initSettingMap;
    private transient Timer refresh;

    boolean settingFlag;

    private MainView mainView = MainView.getInstance();
    private BattleModeView battleModeView = BattleModeView.getInstance();
    private GameView gameView = GameView.getInstance();
    private ScoreView scoreView = ScoreView.getInstance();
    private SettingView settingView = SettingView.getInstance();

    private transient PlayerController playerController = new PlayerController();
    private transient SettingController settingController = new SettingController();
    private transient GameController gameController;
    private transient BattleModeController battleModeController;

    public ViewController() {
        initJFrame();
        initView();

        initMainViewKeyMap();
        addEventListener();
    }

    private void initJFrame() {
        super.setTitle("Team 19 Tetris");
        super.setResizable(false);
        super.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        resizeJFrame();
        contentPane = super.getContentPane();
    }

    private void resizeJFrame() {
        super.setBounds(settingController.getDisplaySize());
        super.setLocationRelativeTo(null);
    }

    private void initView() {
        viewMap = new HashMap<>();
        viewMap.put(mainView.getStartBtn(), gameView);
        viewMap.put(mainView.getBattleModeBtn(), battleModeView);
        viewMap.put(mainView.getScoreBoardBtn(), scoreView);
        viewMap.put(mainView.getSettingBtn(), settingView);

        contentPane.setLayout(new GridLayout(1, 0, 0, 0));
        contentPane.add(mainView);

        TimerTask task = new TimerTask() {
            public void run() {
                revalidate();
                repaint();
            }
        };
        refresh = new Timer();
        refresh.scheduleAtFixedRate(task, 0L, 1000L);

        initMainView();
        initSettingView();
        initScoreView();
    }

    private void initMainView() {
        mainViewKeyMap = new HashMap<>();
    }

    private void initSettingView() {
        int upKey = settingController.getRotateKey();
        int downKey = settingController.getDownKey();
        int leftKey = settingController.getLeftKey();
        int rightKey = settingController.getRightKey();
        int stackKey = settingController.getStackKey();

        settingView.setDisplayModeComboBox(settingController.getDisplayMode());
        settingView.setIsColorBlindBtn(settingController.isColorBlindMode());

        initSettingMap = new SettingViewKeyMap(upKey, downKey, leftKey, rightKey, stackKey);
        initSettingMap.initAllKey();
        settingView.initKetLabels(upKey, downKey, leftKey, rightKey, stackKey);
    }

    private void initScoreView() {
        playerController.loadPlayerList();
        scoreView.resetRankingList();
        playerController.getPlayerList().forEach(
                player -> scoreView.addRankingList(new ArrayList<>(Arrays.asList(player.getName(),
                        Integer.toString(player.getScore()), player.getDifficulty()))));
        scoreView.fillScoreBoard();
    }

    private void addEventListener() {
        addMainViewEventListener();
        addBattleModeViewEventListener();
        addSettingViewEventListener();
        addScoreViewEventListener();
    }

    private void addMainViewEventListener() {
        MainViewKeyListener mainViewKeyListener = new MainViewKeyListener();
        mainView.getStartBtn().requestFocus();
        for (JButton button : mainView.getButtonList()) {
            if (button == mainView.getExitBtn()) {
                button.addActionListener(e -> System.exit(0));
                button.addKeyListener(mainViewKeyListener);
                continue;
            }
            button.addActionListener(
                    e -> transitView(viewMap.get(e.getSource()), mainView));
            button.addKeyListener(mainViewKeyListener);
        }
    }

    private void addBattleModeViewEventListener() {
        MainViewKeyListener mainViewKeyListener = new MainViewKeyListener();
        mainView.getBattleModeBtn().requestFocus();
        for (JButton button : mainView.getButtonList()) {
            if (button == mainView.getExitBtn()) {
                button.addActionListener(e -> System.exit(0));
                button.addKeyListener(mainViewKeyListener);
                continue;
            }
            button.addActionListener(
                e -> transitView(viewMap.get(e.getSource()), mainView));
            button.addKeyListener(mainViewKeyListener);
        }
    }

    private void addSettingViewEventListener() {
        SettingViewKeyListener settingViewKeyListener = new SettingViewKeyListener();

        settingView.getExitBtn()
                .addActionListener(e -> transitView(mainView, settingView));
        for (Component comp : settingView.getComponentList()) {
            comp.addKeyListener(settingViewKeyListener);
        }
    }

    private void addScoreViewEventListener() {
        scoreView.getReturnScoreToMainBtn()
                .addActionListener(e -> transitView(mainView, scoreView));
    }

    private void transitView(Container to, Container from) {
        contentPane.add(to);
        contentPane.remove(from);
        focus(to);
        revalidate();
        repaint();
    }

    private void focus(Container to) {
        if (to.equals(mainView)) {
            mainView.getStartBtn().requestFocus();
        } else if (to.equals(gameView)) {
            refresh.cancel();
            gameController = new GameController(settingController.getSetting(), playerController, contentPane);
            gameView.getGeneralModeBtn().requestFocus();
        } else if (to.equals(settingView)) {
            settingView.getExitBtn().requestFocus();
        } else if (to.equals(battleModeView)) {
            refresh.cancel();
            battleModeController = new BattleModeController();
            battleModeView.getGeneralModeBtn().requestFocus();
        }
    }

    private class SettingViewKeyMap {
        JComboBox<String> displayModeComboBox = settingView.getDisplayModeComboBox();
        Map<JToggleButton, KeyMapping> btnmap;
        int keyBuffer;
        int upKey;
        int downKey;
        int leftKey;
        int rightKey;
        int stackKey;
        List<Integer> keyList;

        private SettingViewKeyMap(int upKey, int downKey, int leftKey, int rightKey, int stackKey) {
            resetMap();
            this.upKey = upKey;
            this.downKey = downKey;
            this.leftKey = leftKey;
            this.rightKey = rightKey;
            this.stackKey = stackKey;
            initKeyList();
            btnmap = new HashMap<>();
            btnmap.put(settingView.getUpKeyBtn(), this::setUpKey);
            btnmap.put(settingView.getDownKeyBtn(), this::setDownKey);
            btnmap.put(settingView.getLeftKeyBtn(), this::setLeftKey);
            btnmap.put(settingView.getRightKeyBtn(), this::setRightKey);
            btnmap.put(settingView.getStackKeyBtn(), this::setStackKey);
            initAllKey();
        }

        private void initAllKey() {
            initKeyList();
            initUpKey(upKey);
            initDownKey(downKey);
            initLeftKey(leftKey);
            initRightKey(rightKey);
            initStackKey(stackKey);
        }

        private void initKeyList() {
            keyList = new ArrayList<>();
            keyList.add(upKey);
            keyList.add(downKey);
            keyList.add(leftKey);
            keyList.add(rightKey);
            keyList.add(stackKey);
        }

        private void resetMap() {
            settingMap = new HashMap<>();
        }

        private void initUpKey(int upKey) {
            for (Component comp : settingView.getComponents()) {
                settingMap.put(new KeyPair(upKey, comp), comp::transferFocusBackward);
            }
            settingMap.put(new KeyPair(upKey, displayModeComboBox), () -> {
                int a = displayModeComboBox.getSelectedIndex();
                displayModeComboBox.setSelectedIndex((3 - (a % 4)) - ((1 + a) % 3));
                displayModeComboBox.hidePopup();
            });
        }

        private void initDownKey(int downKey) {
            for (Component comp : settingView.getComponents()) {
                settingMap.put(new KeyPair(downKey, comp), comp::transferFocus);
            }
            settingMap.put(new KeyPair(downKey, displayModeComboBox), () -> {
                displayModeComboBox.setSelectedIndex((displayModeComboBox.getSelectedIndex() + 1)
                        % 3);
                displayModeComboBox.hidePopup();
            });
        }

        private void initStackKey(int stackKey) {
            settingMap.put(new KeyPair(stackKey, settingView.getExitBtn()), () -> {
                transitView(mainView, settingView);
                settingView.setInitKeyBtnsFocusable(false);
                settingView.setSettingBtnsFocusable(true);
                settingView.setInitKeyBtnsFocusable(false);
            });
            settingMap.put(new KeyPair(stackKey, settingView.getIsColorBlindBtn()), () -> {
                boolean isColorBlind = settingController.isColorBlindMode();
                settingView.setIsColorBlindBtn(!isColorBlind);
                settingController.setColorBlindMode(!isColorBlind);
                settingController.saveSetting();
            });
            settingMap.put(new KeyPair(stackKey, settingView.getEnterDisplayMenuBtn()), () -> {
                displayModeComboBox.setFocusable(true);
                displayModeComboBox.requestFocus();
            });
            settingMap.put(new KeyPair(stackKey, displayModeComboBox), () -> {
                displayModeComboBox.setFocusable(false);
                settingController.setDisplayMode(displayModeComboBox.getSelectedIndex());
                settingController.saveSetting();
                resizeJFrame();
                settingView.getEnterDisplayMenuBtn().requestFocus();
            });
            settingMap.put(new KeyPair(stackKey, settingView.getEnterKeyMenuBtn()), () -> {
                settingView.setInitKeyBtnsFocusable(true);
                settingView.setSettingBtnsFocusable(false);
                settingView.getExitKeyMenuBtn().requestFocus();
                settingView.getEnterKeyMenuBtn().doClick(100);
            });
            settingMap.put(new KeyPair(stackKey, settingView.getExitKeyMenuBtn()), () -> {
                settingView.setInitKeyBtnsFocusable(false);
                settingView.setSettingBtnsFocusable(true);
                settingView.getEnterKeyMenuBtn().requestFocus();
                settingView.getExitKeyMenuBtn().doClick(100);
            });
            settingMap.put(new KeyPair(stackKey, settingView.getEnterResetMenuBtn()), () -> {
                settingView.setResetSettingBtnsFocusable(true);
                settingView.setSettingBtnsFocusable(false);
                settingView.getExitResetMenuBtn().requestFocus();
                settingView.getEnterResetMenuBtn().doClick(100);
            });
            settingMap.put(new KeyPair(stackKey, settingView.getExitResetMenuBtn()), () -> {
                settingView.setResetSettingBtnsFocusable(false);
                settingView.setSettingBtnsFocusable(true);
                settingView.getEnterResetMenuBtn().requestFocus();
                settingView.getExitResetMenuBtn().doClick(100);
            });
            settingMap.put(new KeyPair(stackKey, settingView.getResetSettingBtn()), () -> {
                settingController.resetSetting();
                initSettingView();
                initJFrame();
                settingView.getExitBtn().requestFocus();
                settingView.getResetSettingBtn().doClick(100);
            });
            settingMap.put(new KeyPair(stackKey, settingView.getUpKeyBtn()), () -> {
                settingView.getUpKeyLabel().setText(KEY_CHANGE_MESSAGE);
                settingView.getUpKeyLabel().setForeground(Color.RED);
                settingView.getUpKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = upKey;
            });
            settingMap.put(new KeyPair(stackKey, settingView.getDownKeyBtn()), () -> {
                settingView.getDownKeyLabel().setText(KEY_CHANGE_MESSAGE);
                settingView.getDownKeyLabel().setForeground(Color.RED);
                settingView.getDownKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = downKey;
            });
            settingMap.put(new KeyPair(stackKey, settingView.getLeftKeyBtn()), () -> {
                settingView.getLeftKeyLabel().setText(KEY_CHANGE_MESSAGE);
                settingView.getLeftKeyLabel().setForeground(Color.RED);
                settingView.getLeftKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = leftKey;
            });
            settingMap.put(new KeyPair(stackKey, settingView.getRightKeyBtn()), () -> {
                settingView.getRightKeyLabel().setText(KEY_CHANGE_MESSAGE);
                settingView.getRightKeyLabel().setForeground(Color.RED);
                settingView.getRightKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = rightKey;
            });
            settingMap.put(new KeyPair(stackKey, settingView.getStackKeyBtn()), () -> {
                if (!settingView.getStackKeyBtn().isSelected()) {
                    settingView.getStackKeyLabel().setText(KEY_CHANGE_MESSAGE);
                    settingView.getStackKeyLabel().setForeground(Color.RED);
                    settingView.getStackKeyBtn().setSelected(true);
                    settingFlag = true;
                    keyBuffer = stackKey;
                }
            });
        }

        private void initLeftKey(int leftKey) {
            for (Component comp : settingView.getKeyMenuPanel().getComponents()) {
                if (!(comp.getClass().equals(JLabel.class))) {
                    settingMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
                }
            }
            for (Component comp : settingView.getResetMenuPanel().getComponents()) {
                settingMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            }
        }

        private void initRightKey(int rightKey) {
            for (Component comp : settingView.getKeyMenuPanel().getComponents()) {
                if (!(comp.getClass().equals(JLabel.class))) {
                    settingMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
                }
            }

            for (Component comp : settingView.getResetMenuPanel().getComponents()) {
                settingMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            }
        }

        private void setKeyByToggleButton(JToggleButton btn, int key) {
            btnmap.get(btn).mapping(key);
        }

        private void setUpKey(int upKey) {
            this.upKey = upKey;
            settingController.setRotateKey(upKey);
            resetMap();
            initAllKey();
            initMainViewKeyMap();
        }

        private void setDownKey(int downKey) {
            this.downKey = downKey;
            settingController.setDownKey(downKey);
            resetMap();
            initAllKey();
        }

        private void setLeftKey(int leftKey) {
            this.leftKey = leftKey;
            settingController.setLeftKey(leftKey);
            resetMap();
            initAllKey();
            initMainViewKeyMap();
        }

        private void setRightKey(int rightKey) {
            this.rightKey = rightKey;
            settingController.setRightKey(rightKey);
            resetMap();
            initAllKey();
            initMainViewKeyMap();
        }

        private void setStackKey(int stackKey) {
            this.stackKey = stackKey;
            settingController.setStackKey(stackKey);
            resetMap();
            initAllKey();
            initMainViewKeyMap();
        }

        private boolean checkKeyOverlap(int key) {
            if (key == keyBuffer)
                return false;
            return keyList.contains(key);
        }
    }

    // Key 쌍을 위한 클래스
    public class KeyPair {

        private final int keyCode;
        private final Component component;

        public KeyPair(int keyCode, Component component) {
            this.keyCode = keyCode;
            this.component = component;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (!(o instanceof KeyPair))
                return false;
            KeyPair key = (KeyPair) o;
            return keyCode == key.keyCode && component == key.component;
        }

        @Override
        public int hashCode() {
            return Objects.hash(keyCode, component);
        }
    }

    @FunctionalInterface
    private interface PressedKeyEvent {
        void isKeyPressed();
    }

    @FunctionalInterface
    private interface KeyMapping {
        void mapping(int key);
    }

    private void initMainViewKeyMap() {
        mainViewKeyMap = new HashMap<>();
        int upKey = settingController.getRotateKey();
        int downKey = settingController.getDownKey();
        int stackKey = settingController.getStackKey();
        for (Component comp : mainView.getButtonPanel().getComponents()) {
            mainViewKeyMap.put(new KeyPair(upKey, comp), comp::transferFocusBackward);
        }
        for (Component comp : mainView.getButtonPanel().getComponents()) {
            mainViewKeyMap.put(new KeyPair(downKey, comp), comp::transferFocus);
        }
        for (Component comp : mainView.getButtonPanel().getComponents()) {
            mainViewKeyMap.put(new KeyPair(stackKey, comp),
                    () -> transitView(viewMap.get(comp), mainView));
        }
        mainViewKeyMap.put(new KeyPair(stackKey, mainView.getExitBtn()), () -> System.exit(0));
    }

    private class MainViewKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            KeyPair key = new KeyPair(e.getKeyCode(), e.getComponent());
            if (mainViewKeyMap.containsKey(key)) {
                mainViewKeyMap.get(key).isKeyPressed();
            }
        }
    }

    private class SettingViewKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            Component comp = e.getComponent();
            int pressedKey = e.getKeyCode();
            KeyPair key = new KeyPair(e.getKeyCode(), comp);
            Map<JToggleButton, JLabel> setKeymap = settingView.getSetKeyMap();
            if (settingFlag) {
                setKeymap.keySet().stream().filter(AbstractButton::isSelected).forEach(x -> {
                    if (initSettingMap.checkKeyOverlap(pressedKey)) {
                        setKeymap.get(x).setText("키 중복!.");
                    } else {
                        initSettingMap.setKeyByToggleButton(x, pressedKey);
                        setKeymap.get(x).setText(KeyEvent.getKeyText(pressedKey));
                        setKeymap.get(x).setForeground(Color.BLACK);
                        x.doClick();
                        settingFlag = false;
                    }
                });
            } else if (settingMap.containsKey(key))
                settingMap.get(key).isKeyPressed();
        }
    }

    public void stopTimer() {
        gameController.stopGameDelayTimer();
    }
}
