package tetris.controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.Timer;
import java.util.*;

import tetris.view.*;
import tetris.view.MasterView.JToggleButton;
import tetris.view.MasterView.JLabel;

public class ViewController extends JFrame {

    private Container contentPane;
    private HashMap<JButton, Container> viewMap;
    private transient Map<KeyPair, PressedKeyEvent> mainMap;
    private transient Map<KeyPair, PressedKeyEvent> settingMap;
    private transient InitMainMap initMainMap;
    private transient InitSettingMap initSettingMap;
    private transient Timer refresh;
    private transient TimerTask task;

    boolean settingFlag;

    private MainView mainView;
    private GameView gameView;
    private ScoreView scoreView;
    private SettingView settingView;

    private transient PlayerController playerController = new PlayerController();
    private transient SettingController settingController = new SettingController();
    private transient GameController gameController;

    public ViewController() {
        initJFrame();
        initViewAndController();
        initView();
        addEventListener();
    }

    private void initViewAndController() {
        mainView = MainView.getInstance();
        gameView = GameView.getInstance();
        scoreView = ScoreView.getInstance();
        settingView = SettingView.getInstance();
        settingController = new SettingController();
        playerController = new PlayerController();
    }

    private void initJFrame() {
        super.setTitle("Team 19 Tetris");
        super.setResizable(false); // 창의 크기 조정 가능 여부
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 창을 닫으면 더 이상 실행(run)되지 않는다
        resizeJFrame();
        super.setVisible(true);
        contentPane = super.getContentPane(); // contentPane 부르기
    }

    private void resizeJFrame() {
        super.setBounds(settingController.getScreenSize());
        super.setLocationRelativeTo(null);
    }

    private void initView() {
        // View Mapping
        viewMap = new HashMap<>();
        viewMap.put(mainView.getStartBtn(), gameView);
        viewMap.put(mainView.getScoreBoardBtn(), scoreView);
        viewMap.put(mainView.getSettingBtn(), settingView);

        contentPane.setLayout(new GridLayout(1, 0, 0, 0));
        contentPane.add(mainView);

        task = new TimerTask() {
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
        initMainMap = new InitMainMap();
    }

    private void initSettingView() {
        int upKey = settingController.getRotateKey();
        int downKey = settingController.getMoveDownKey();
        int leftKey = settingController.getMoveLeftKey();
        int rightKey = settingController.getMoveRightKey();
        int stackKey = settingController.getStackKey();

        settingView.setDisplayComboBox(settingController.getDisplayList(),
                settingController.getDisplayMode());
        settingView.setIsColorBlindBtn(settingController.isColorBlindMode());

        initSettingMap = new InitSettingMap(upKey, downKey, leftKey, rightKey, stackKey);

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
        addSettingViewEventListener();
        addScoreViewEventListener();
    }

    private void addMainViewEventListener() {
        MainKeyListener mainKeyEventListner = new MainKeyListener();
        mainView.getStartBtn().requestFocus();
        for (Component buttonComp : mainView.getButtonPanel().getComponents()) {
            AbstractButton button = (AbstractButton) buttonComp;
            if (button == mainView.getExitBtn()) {
                button.addActionListener(e -> System.exit(0));
                button.addKeyListener(mainKeyEventListner);
                continue;
            }
            button.addActionListener(
                    e -> transitView(contentPane, viewMap.get(e.getSource()), mainView));
            buttonComp.addKeyListener(mainKeyEventListner);
        }
    }

    private void addSettingViewEventListener() {
        SettingKeyListner settingEventListner = new SettingKeyListner();

        settingView.getReturnSettingToMainBtn()
                .addActionListener(e -> transitView(contentPane, mainView, settingView));
        for (Component viewComp : settingView.getComponents()) {
            if (viewComp instanceof JPanel) {
                for (Component panelComp : ((JPanel) viewComp).getComponents()) {
                    panelComp.addKeyListener(settingEventListner);
                }
            }
            if (viewComp.getClass().equals(JLabel.class))
                continue;
            viewComp.addKeyListener(settingEventListner);
        }
    }

    private void addScoreViewEventListener() {
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
        } else if (to.equals(gameView)) {
            // refresh.cancel();
            gameController = new GameController(settingController.getSetting(), playerController, contentPane);
            gameView.getGeneralModeBtn().requestFocus();
        } else if (to.equals(settingView)) {
            settingView.getReturnSettingToMainBtn().requestFocus();
        }
    }

    private class InitMainMap {

        int upKey;
        int downKey;
        int stackKey;

        private InitMainMap() {
            resetMap();
            initAllKey();
        }

        private void loadSetting() {
            upKey = settingController.getRotateKey();
            downKey = settingController.getMoveDownKey();
            stackKey = settingController.getStackKey();
        }

        private void initAllKey() {
            loadSetting();
            initUpKey(upKey);
            initDownKey(downKey);
            initStackKey(stackKey);
        }

        private void resetMap() {
            mainMap = new HashMap<>();
        }

        private void initUpKey(int upKey) {
            for (Component comp : mainView.getButtonPanel().getComponents()) {
                mainMap.put(new KeyPair(upKey, comp), comp::transferFocusBackward);
            }
        }

        private void initDownKey(int downKey) {
            for (Component comp : mainView.getButtonPanel().getComponents())
                mainMap.put(new KeyPair(downKey, comp), comp::transferFocus);
        }

        private void initStackKey(int stackKey) {
            for (Component comp : mainView.getButtonPanel().getComponents())
                mainMap.put(new KeyPair(stackKey, comp),
                        () -> transitView(contentPane, viewMap.get(comp), mainView));
            mainMap.put(new KeyPair(stackKey, mainView.getExitBtn()), () -> System.exit(0));
        }

    }

    private class InitSettingMap {
        JComboBox<String> displayComboBox = settingView.getDisplayComboBox();
        Map<JToggleButton, KeyMapping> btnmap;
        int keyBuffer;
        int upKey;
        int downKey;
        int leftKey;
        int rightKey;
        int stackKey;
        List<Integer> keyList;

        private InitSettingMap(int upKey, int downKey, int leftKey, int rightKey, int stackKey) {
            resetMap();
            this.upKey = upKey;
            this.downKey = downKey;
            this.leftKey = leftKey;
            this.rightKey = rightKey;
            this.stackKey = stackKey;
            initKeyList();
            btnmap = new HashMap<>();
            btnmap.put(settingView.getSetUpKeyBtn(), this::setUpKey);
            btnmap.put(settingView.getSetDownKeyBtn(), this::setDownKey);
            btnmap.put(settingView.getSetLeftKeyBtn(), this::setLeftKey);
            btnmap.put(settingView.getSetRightKeyBtn(), this::setRightKey);
            btnmap.put(settingView.getSetStackKeyBtn(), this::setStackKey);
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
            settingMap.put(new KeyPair(upKey, displayComboBox), () -> {
                int a = displayComboBox.getSelectedIndex();
                int b = settingController.getDisplayList().size();
                displayComboBox.setSelectedIndex((b - (a % (b + 1))) - ((1 + a) % b));
                displayComboBox.hidePopup();
            });
        }

        private void initDownKey(int downKey) {
            for (Component comp : settingView.getComponents()) {
                settingMap.put(new KeyPair(downKey, comp), comp::transferFocus);
            }
            settingMap.put(new KeyPair(downKey, displayComboBox), () -> {
                displayComboBox.setSelectedIndex((displayComboBox.getSelectedIndex() + 1)
                        % settingController.getDisplayList().size());
                displayComboBox.hidePopup();
            });
        }

        private void initStackKey(int stackKey) {
            String inputMessage = settingView.getInputMessage();

            /* StackKey */
            settingMap.put(new KeyPair(stackKey, settingView.getReturnSettingToMainBtn()), () -> {
                transitView(contentPane, mainView, settingView);
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
            settingMap.put(new KeyPair(stackKey, settingView.getSetDisplayBtn()), () -> {
                displayComboBox.setFocusable(true);
                displayComboBox.requestFocus();
            });
            settingMap.put(new KeyPair(stackKey, displayComboBox), () -> {
                displayComboBox.setFocusable(false);
                settingController.setDisplayMode(displayComboBox.getSelectedIndex());
                settingController.saveSetting();
                resizeJFrame();
                settingView.getSetDisplayBtn().requestFocus();
            });
            settingMap.put(new KeyPair(stackKey, settingView.getInitKeyBtn()), () -> {
                settingView.setInitKeyBtnsFocusable(true);
                settingView.setSettingBtnsFocusable(false);
                settingView.getInitKeyGridReturnBtn().requestFocus();
            });
            settingMap.put(new KeyPair(stackKey, settingView.getInitKeyGridReturnBtn()), () -> {
                settingView.setInitKeyBtnsFocusable(false);
                settingView.setSettingBtnsFocusable(true);
                settingView.getInitKeyBtn().requestFocus();
            });
            settingMap.put(new KeyPair(stackKey, settingView.getInitMenuBtn()), () -> {
                settingView.setInitSettingBtnsFocusable(true);
                settingView.setSettingBtnsFocusable(false);
                settingView.getInitReturnBtn().requestFocus();
            });
            settingMap.put(new KeyPair(stackKey, settingView.getInitReturnBtn()), () -> {
                settingView.setInitSettingBtnsFocusable(false);
                settingView.setSettingBtnsFocusable(true);
                settingView.getInitMenuBtn().requestFocus();
            });
            settingMap.put(new KeyPair(stackKey, settingView.getInitSettingBtn()), () -> {
                settingController.resetSetting();
                initSettingView();
                initJFrame();
                settingView.getReturnSettingToMainBtn().requestFocus();
            });
            settingMap.put(new KeyPair(stackKey, settingView.getSetUpKeyBtn()), () -> {
                settingView.getUpKeyLabel().setText(inputMessage);
                settingView.getUpKeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetUpKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = upKey;
            });
            settingMap.put(new KeyPair(stackKey, settingView.getSetDownKeyBtn()), () -> {
                settingView.getDownKeyLabel().setText(inputMessage);
                settingView.getDownKeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetDownKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = downKey;
            });
            settingMap.put(new KeyPair(stackKey, settingView.getSetLeftKeyBtn()), () -> {
                settingView.getLeftKeyLabel().setText(inputMessage);
                settingView.getLeftKeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetLeftKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = leftKey;
            });
            settingMap.put(new KeyPair(stackKey, settingView.getSetRightKeyBtn()), () -> {
                settingView.getRightKeyLabel().setText(inputMessage);
                settingView.getRightKeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetRightKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = rightKey;
            });
            settingMap.put(new KeyPair(stackKey, settingView.getSetStackKeyBtn()), () -> {
                if (!settingView.getSetStackKeyBtn().isSelected()) {
                    settingView.getStackKeyLabel().setText(inputMessage);
                    settingView.getStackKeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                    settingView.getSetStackKeyBtn().setSelected(true);

                    settingFlag = true;
                    keyBuffer = stackKey;
                }
            });
        }

        private void initLeftKey(int leftKey) {
            for (Component comp : settingView.getInitKeyGridPane().getComponents()) {
                if (!(comp.getClass().equals(JLabel.class))) {
                    settingMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
                }
            }
            for (Component comp : settingView.getInitSettingPane().getComponents()) {
                settingMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            }
        }

        private void initRightKey(int rightKey) {
            for (Component comp : settingView.getInitKeyGridPane().getComponents()) {
                if (!(comp.getClass().equals(JLabel.class))) {
                    settingMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
                }
            }

            for (Component comp : settingView.getInitSettingPane().getComponents()) {
                settingMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            }
        }

        private void setKeyByToggleButton(JToggleButton btn, int key) {
            btnmap.get(btn).domapping(key);
        }

        private void setUpKey(int upKey) {
            this.upKey = upKey;
            settingController.setRotateKey(upKey);
            resetMap();
            initAllKey();
            initMainMap.initAllKey();
        }

        private void setDownKey(int downKey) {
            this.downKey = downKey;
            settingController.setMoveDownKey(downKey);
            resetMap();
            initAllKey();
        }

        private void setLeftKey(int leftKey) {
            this.leftKey = leftKey;
            settingController.setMoveLeftKey(leftKey);
            resetMap();
            initAllKey();
            initMainMap.initAllKey();
        }

        private void setRightKey(int rightKey) {
            this.rightKey = rightKey;
            settingController.setMoveRightKey(rightKey);
            resetMap();
            initAllKey();
            initMainMap.initAllKey();
        }

        private void setStackKey(int stackKey) {
            this.stackKey = stackKey;
            settingController.setStackKey(stackKey);
            resetMap();
            initAllKey();
            initMainMap.initAllKey();
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
        void domapping(int key);
    }

    private class MainKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            KeyPair key = new KeyPair(e.getKeyCode(), e.getComponent());
            if (mainMap.containsKey(key))
                mainMap.get(key).isKeyPressed();
        }
    }

    private class SettingKeyListner extends KeyAdapter {

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
                        setKeymap.get(x).setForeground(MasterView.BASIC_FONT_COLOR);
                        x.setSelected(false);
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
