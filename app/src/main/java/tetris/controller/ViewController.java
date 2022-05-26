package tetris.controller;

import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import javax.swing.*;
import javax.swing.Timer;
import java.util.List;
import java.util.*;

import tetris.model.Setting;
import tetris.view.*;
import tetris.view.MasterView.JToggleButton;
import tetris.view.MasterView.JLabel;

public class ViewController extends JFrame {

    public static final int REFRESH_RATE = 17; // 60FPS
    static final String OVERLAP_KEY_MSG = "키 중복!";

    private int currentResoultion;

    Container contentPane;
    private Map<JButton, Container> viewMap;
    private transient Map<KeyPair, PressedKeyEvent> mainViewKeyMap;
    private transient Map<KeyPair, PressedKeyEvent> settingViewKeyMap;
    private transient Map<KeyPair, PressedKeyEvent> gameViewKeyMap;
    private transient Map<Container, Runnable> focusingMap;
    private transient InitMainViewMap initMainViewKeyMap;
    private transient InitGameViewKeyMap initGameViewKeyMap;
    private transient InitSettingViewMap initSettingViewMap;
    private transient List<Component> allComponents;
    transient Timer refreshTimer;

    boolean settingFlag;

    private MainView mainView;
    private GameView gameView;
    private ScoreView scoreView;
    private SettingView settingView;

    private transient PlayerController playerController = new PlayerController();
    private transient SettingController settingController = new SettingController();
    private transient MultiGameController multiGameController;

    public ViewController() {
        initJFrame();
        initViewAndController();
        initView();
        addEventListener();
        setMainKeyLabel();
        resizeViewFont();
    }

    private void initViewAndController() {
        mainView = MainView.getInstance();
        gameView = GameView.getInstance();
        scoreView = ScoreView.getInstance();
        settingView = SettingView.getInstance();
        settingController = new SettingController();
        playerController = new PlayerController();
        multiGameController = new MultiGameController(playerController, this);
        allComponents = new LinkedList<>();
    }

    private void initJFrame() {
        super.setTitle("Team 19 Tetris");
        super.setResizable(false); // 창의 크기 조정 가능 여부
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 창을 닫으면 더 이상 실행(run)되지 않는다
        resizeJFrame();
        super.setVisible(true);
        contentPane = super.getContentPane(); // contentPane 부르기
        super.rootPane.setFocusable(false);
    }

    private void resizeJFrame() {
        super.setBounds(settingController.getScreenSize());
        super.setLocationRelativeTo(null);
        currentResoultion = super.getWidth() * super.getHeight();
    }

    // ScreenSize 변경 이후, 각 View 들의 Font 크기를 조정한다.
    private void resizeViewFont() {
        int fontSize = currentResoultion / 23040;
        allComponents.forEach(e -> e.setFont(new Font(GameView.BASIC_FONT_FAMILY, Font.BOLD, fontSize)));
        settingView.getIsColorBlindLabel().setFont(new Font(GameView.BASIC_FONT_FAMILY, Font.BOLD, fontSize));
        mainView.getAppNameLabel().setFont(new Font(GameView.BASIC_FONT_FAMILY, Font.BOLD, fontSize * 3));
        mainView.getAllKeyLabel().setFont(new Font(GameView.BASIC_FONT_FAMILY, Font.BOLD, fontSize / 2));
    }

    private void initView() {
        viewMap = new HashMap<>();
        viewMap.put(mainView.getStartBtn(), gameView);
        viewMap.put(mainView.getScoreBoardBtn(), scoreView);
        viewMap.put(mainView.getSettingBtn(), settingView);

        focusingMap = new HashMap<>();
        focusingMap.put(mainView, () -> mainView.getStartBtn().grabFocus());
        focusingMap.put(gameView, () -> gameView.getSingleGameBtn().grabFocus());
        focusingMap.put(gameView.getSelectGamePanel(), () -> gameView.getSingleGameBtn().grabFocus());
        focusingMap.put(gameView.getSelectModePane(), () -> gameView.getGeneralModeBtn().grabFocus());
        focusingMap.put(gameView.getSelectDiffPane(), () -> gameView.getEasyBtn().grabFocus());
        focusingMap.put(gameView.getSelectMultiGamePanel(), () -> gameView.getRobotGameBtn().grabFocus());
        focusingMap.put(gameView.getSingleGameDisplayPane(),
                () -> gameView.getPlayerOneGameBoardPane().grabFocus());
        focusingMap.put(gameView.getMulitiGameDisplayPane(),
                () -> gameView.getPlayerTwoGameBoardPane().grabFocus());
        focusingMap.put(gameView.getGameOverPanel(), () -> gameView.getInputName().grabFocus());
        focusingMap.put(settingView, () -> settingView.getReturnSettingToMainBtn().grabFocus());
        focusingMap.put(scoreView, () -> scoreView.getReturnScoreToMainBtn().grabFocus());

        contentPane.setLayout(new GridLayout(1, 0, 0, 0)); // Frame의 레이아웃 방식을 설정, row 1
        contentPane.add(mainView);

        refreshTimer = new Timer(REFRESH_RATE, e -> {
            super.revalidate();
            super.repaint();
        });
        refreshTimer.start();
        initMainView();
        initGameView();
        initSettingView();
        initScoreView();

    }

    private void initMainView() {
        initMainViewKeyMap = new InitMainViewMap();
    }

    private void initGameView() {
        initGameViewKeyMap = new InitGameViewKeyMap();
    }

    private void initSettingView() {
        int upKey = settingController.getRotateKey();
        int downKey = settingController.getMoveDownKey();
        int leftKey = settingController.getMoveLeftKey();
        int rightKey = settingController.getMoveRightKey();
        int stackKey = settingController.getStackKey();
        int up2Key = settingController.getRotate2Key();
        int down2Key = settingController.getMoveDown2Key();
        int left2Key = settingController.getMoveLeft2Key();
        int right2Key = settingController.getMoveRight2Key();
        int stack2Key = settingController.getStack2Key();

        settingView.setDisplayComboBox(settingController.getDisplayList(),
                settingController.getDisplayMode());
        settingView.setIsColorBlindBtn(settingController.isColorBlindMode());

        initSettingViewMap = new InitSettingViewMap(settingController.getSetting());
        initSettingViewMap.initAllKey();
        settingView.initKeyLabels(upKey, downKey, leftKey, rightKey, stackKey);
        settingView.init2KeyLabels(up2Key, down2Key, left2Key, right2Key, stack2Key);
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
        addGameViewEventListener();
        addSettingViewEventListener();
        addScoreViewEventListener();
    }

    private void addMainViewEventListener() {
        MainKeyListener mainKeyEventListener = new MainKeyListener();
        mainView.getStartBtn().requestFocus();
        for (Component buttonComp : mainView.getButtonPanel().getComponents()) {
            allComponents.add(buttonComp);
            AbstractButton button = (AbstractButton) buttonComp;
            if (button == mainView.getExitBtn()) {
                button.addActionListener(e -> System.exit(0));
                button.addKeyListener(mainKeyEventListener);
                continue;
            }
            button.addActionListener(
                    e -> transitView(contentPane, viewMap.get(e.getSource()), mainView));
            buttonComp.addKeyListener(mainKeyEventListener);
        }
    }

    private void addGameViewEventListener() {
        GameKeyListener gameKeyListener = new GameKeyListener();
        for (Component comp : gameView.getSelectGamePanel().getComponents()) {
            comp.addKeyListener(gameKeyListener);
            allComponents.add(comp);
        }

        for (Component comp : gameView.getSelectDiffPane().getComponents()) {
            comp.addKeyListener(gameKeyListener);
            allComponents.add(comp);
        }
        for (Component comp : gameView.getSelectModePane().getComponents()) {
            comp.addKeyListener(gameKeyListener);
            allComponents.add(comp);
        }
        for (Component comp : gameView.getSelectMultiGamePanel().getComponents()) {
            comp.addKeyListener(gameKeyListener);
            allComponents.add(comp);
        }
        for (Component comp : gameView.getGameOverPanel().getComponents()) {
            comp.addKeyListener(gameKeyListener);
            allComponents.add(comp);
        }
        for (Component comp : gameView.getStopMenu().getComponents()) {
            comp.addKeyListener(gameKeyListener);
            allComponents.add(comp);
        }
        multiGameController.singleGameFocusing.addKeyListener(gameKeyListener);
        multiGameController.multiGameFocusing.addKeyListener(gameKeyListener);
    }

    private void addSettingViewEventListener() {
        SettingKeyListener settingEventListener = new SettingKeyListener();

        settingView.getReturnSettingToMainBtn()
                .addActionListener(e -> transitView(contentPane, mainView, settingView));
        for (Component viewComp : settingView.getComponents()) {
            if (viewComp instanceof JPanel) {
                for (Component panelComp : ((JPanel) viewComp).getComponents()) {
                    panelComp.addKeyListener(settingEventListener);
                    allComponents.add(panelComp);
                }
            }
            if (viewComp.getClass().equals(JLabel.class))
                continue;
            viewComp.addKeyListener(settingEventListener);
            allComponents.add(viewComp);
        }
    }

    private void addScoreViewEventListener() {
        scoreView.getReturnScoreToMainBtn()
                .addActionListener(e -> transitView(contentPane, mainView, scoreView));
        scoreView.getReturnScoreToMainBtn()
                .addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == settingController.getStackKey())
                            transitView(contentPane, mainView, scoreView);
                        scoreView.revalidate();
                        scoreView.repaint();
                    }
                });
        allComponents.add(scoreView.getReturnScoreToMainBtn());
    }

    // 전환함수
    void transitView(Container pane, Container to, Container from) {
        pane.add(to);
        pane.remove(from);
        focus(to);
        super.revalidate(); // component 변화 후 JFrame 새로고침(component 변화 적용) */
        super.repaint(); // component 변화 후 JFrame 새로고침(component 색 등의 성질 적용) */
    }

    private void focus(Container to) {
        focusingMap.get(to).run();
    }

    private class InitMainViewMap {

        int upKey;
        int downKey;
        int stackKey;

        private InitMainViewMap() {
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
            mainViewKeyMap = new HashMap<>();
        }

        private void initUpKey(int upKey) {
            for (Component comp : mainView.getButtonPanel().getComponents()) {
                mainViewKeyMap.put(new KeyPair(upKey, comp), comp::transferFocusBackward);
            }
        }

        private void initDownKey(int downKey) {
            for (Component comp : mainView.getButtonPanel().getComponents())
                mainViewKeyMap.put(new KeyPair(downKey, comp), comp::transferFocus);
        }

        private void initStackKey(int stackKey) {
            for (Component comp : mainView.getButtonPanel().getComponents())
                mainViewKeyMap.put(new KeyPair(stackKey, comp),
                        () -> transitView(contentPane, viewMap.get(comp), mainView));
            mainViewKeyMap.put(new KeyPair(stackKey, mainView.getExitBtn()), () -> System.exit(0));
        }

    }

    private class InitGameViewKeyMap {

        int upKey;
        int downKey;
        int leftKey;
        int rightKey;
        int stackKey;
        boolean isSingleGameModeFlag;

        private InitGameViewKeyMap() {
            loadSetting();
            resetMap();
            initAllKey();
        }

        private void loadSetting() {
            this.upKey = settingController.getRotateKey();
            this.downKey = settingController.getMoveDownKey();
            this.leftKey = settingController.getMoveLeftKey();
            this.rightKey = settingController.getMoveRightKey();
            this.stackKey = settingController.getStackKey();
        }

        private void initAllKey() {
            loadSetting();
            resetMap();
            initUpKey();
            initDownKey();
            initLeftKey();
            initRightKey();
            initStackKey();
            initOtherKeys();
        }

        private void resetMap() {
            gameViewKeyMap = new HashMap<>();
        }

        private void initUpKey() {
            gameViewKeyMap.put(new KeyPair(upKey, gameView.getDiffReturnBtn()),
                    () -> gameView.getNormalBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(upKey, gameView.getGameReturnBtn()),
                    () -> gameView.getSingleGameBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(upKey, gameView.getModeReturnBtn()),
                    () -> gameView.getItemModeBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(upKey, gameView.getMultiGameReturnBtn()),
                    () -> gameView.getLocalGameBtn().requestFocus(true));
            for (Component comp : gameView.getStopMenu().getComponents())
                gameViewKeyMap.put(new KeyPair(upKey, comp), comp::transferFocusBackward);
            gameViewKeyMap.put(new KeyPair(upKey, gameView.getContinueBtn()),
                    () -> gameView.getExitGameBtn().requestFocus(true));
        }

        private void initDownKey() {
            gameViewKeyMap.put(new KeyPair(downKey, gameView.getEasyBtn()),
                    () -> gameView.getDiffReturnBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(downKey, gameView.getNormalBtn()),
                    () -> gameView.getDiffReturnBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(downKey, gameView.getHardBtn()),
                    () -> gameView.getDiffReturnBtn().requestFocus(true));

            gameViewKeyMap.put(new KeyPair(downKey, gameView.getGeneralModeBtn()),
                    () -> gameView.getModeReturnBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(downKey, gameView.getItemModeBtn()),
                    () -> gameView.getModeReturnBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(downKey, gameView.getTimeAttackBtn()),
                    () -> gameView.getModeReturnBtn().requestFocus(true));

            gameViewKeyMap.put(new KeyPair(downKey, gameView.getRobotGameBtn()),
                    () -> gameView.getMultiGameReturnBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(downKey, gameView.getLocalGameBtn()),
                    () -> gameView.getMultiGameReturnBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(downKey, gameView.getOnlineGameBtn()),
                    () -> gameView.getMultiGameReturnBtn().requestFocus(true));

            gameViewKeyMap.put(new KeyPair(downKey, gameView.getSingleGameBtn()),
                    () -> gameView.getGameReturnBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(downKey, gameView.getMulitiGameBtn()),
                    () -> gameView.getGameReturnBtn().requestFocus(true));
            for (Component comp : gameView.getStopMenu().getComponents())
                gameViewKeyMap.put(new KeyPair(downKey, comp), comp::transferFocus);
            gameViewKeyMap.put(new KeyPair(downKey, gameView.getExitGameBtn()),
                    () -> gameView.getContinueBtn().requestFocus(true));

        }

        private void initLeftKey() {
            for (Component comp : gameView.getSelectDiffPane().getComponents())
                gameViewKeyMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            gameViewKeyMap.put(new KeyPair(leftKey, gameView.getEasyBtn()),
                    () -> gameView.getHardBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(leftKey, gameView.getDiffReturnBtn()),
                    () -> gameView.getEasyBtn().requestFocus(true));

            for (Component comp : gameView.getSelectModePane().getComponents())
                gameViewKeyMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            gameViewKeyMap.put(new KeyPair(leftKey, gameView.getGeneralModeBtn()),
                    () -> gameView.getTimeAttackBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(leftKey, gameView.getModeReturnBtn()),
                    () -> gameView.getGeneralModeBtn().requestFocus(true));

            for (Component comp : gameView.getSelectMultiGamePanel().getComponents())
                gameViewKeyMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            gameViewKeyMap.put(new KeyPair(leftKey, gameView.getRobotGameBtn()),
                    () -> gameView.getOnlineGameBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(leftKey, gameView.getMultiGameReturnBtn()),
                    () -> gameView.getRobotGameBtn().requestFocus(true));

            gameViewKeyMap.put(new KeyPair(leftKey, gameView.getSingleGameBtn()),
                    () -> gameView.getMulitiGameBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(leftKey, gameView.getMulitiGameBtn()),
                    () -> gameView.getSingleGameBtn().requestFocus(true));
        }

        private void initRightKey() {
            for (Component comp : gameView.getSelectDiffPane().getComponents())
                gameViewKeyMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            gameViewKeyMap.put(new KeyPair(rightKey, gameView.getHardBtn()),
                    () -> gameView.getEasyBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(rightKey, gameView.getDiffReturnBtn()),
                    () -> gameView.getHardBtn().requestFocus(true));

            for (Component comp : gameView.getSelectModePane().getComponents())
                gameViewKeyMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            gameViewKeyMap.put(new KeyPair(rightKey, gameView.getModeReturnBtn()),
                    () -> gameView.getTimeAttackBtn().requestFocus(true));

            for (Component comp : gameView.getSelectMultiGamePanel().getComponents())
                gameViewKeyMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            gameViewKeyMap.put(new KeyPair(rightKey, gameView.getMultiGameReturnBtn()),
                    () -> gameView.getOnlineGameBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(rightKey, gameView.getOnlineGameBtn()),
                    () -> gameView.getRobotGameBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(rightKey, gameView.getTimeAttackBtn()),
                    () -> gameView.getGeneralModeBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(rightKey, gameView.getSingleGameBtn()),
                    () -> gameView.getMulitiGameBtn().requestFocus(true));
            gameViewKeyMap.put(new KeyPair(rightKey, gameView.getMulitiGameBtn()),
                    () -> gameView.getSingleGameBtn().requestFocus(true));
        }

        private void initStackKey() {
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getEasyBtn()), () -> {
                multiGameController.setDiffMode(GameController.EASY_MODE);
                transitView(gameView, gameView.getSingleGameDisplayPane(), gameView.getSelectDiffPane());
                multiGameController.startSingleGame(settingController.getSetting());
            });
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getNormalBtn()), () -> {
                multiGameController.setDiffMode(GameController.NORMAL_MODE);
                transitView(gameView, gameView.getSingleGameDisplayPane(), gameView.getSelectDiffPane());
                multiGameController.startSingleGame(settingController.getSetting());
            });
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getHardBtn()), () -> {
                multiGameController.setDiffMode(GameController.HARD_MODE);
                transitView(gameView, gameView.getSingleGameDisplayPane(), gameView.getSelectDiffPane());
                multiGameController.startSingleGame(settingController.getSetting());
            });

            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getLocalGameBtn()), () -> {
                transitView(gameView, gameView.getMulitiGameDisplayPane(), gameView.getSelectMultiGamePanel());
                multiGameController.startLocalGame(settingController.getSetting());
            });
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getRobotGameBtn()), () -> {
                transitView(gameView, gameView.getMulitiGameDisplayPane(), gameView.getSelectMultiGamePanel());
                multiGameController.startRobotGame(settingController.getSetting());
            });

            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getGeneralModeBtn()), () -> {
                multiGameController.setGameMode(GameController.GENERAL_GAME_MODE);
                if (isSingleGameModeFlag)
                    transitView(gameView, gameView.getSelectDiffPane(), gameView.getSelectModePane());
                else
                    transitView(gameView, gameView.getSelectMultiGamePanel(), gameView.getSelectModePane());
            });
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getItemModeBtn()), () -> {
                multiGameController.setGameMode(GameController.ITEM_GAME_MODE);
                if (isSingleGameModeFlag)
                    transitView(gameView, gameView.getSelectDiffPane(), gameView.getSelectModePane());
                else
                    transitView(gameView, gameView.getSelectMultiGamePanel(), gameView.getSelectModePane());
            });
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getTimeAttackBtn()), () -> {
                multiGameController.setGameMode(GameController.TIME_ATTACK_MODE);
                if (isSingleGameModeFlag)
                    transitView(gameView, gameView.getSelectDiffPane(), gameView.getSelectModePane());
                else
                    transitView(gameView, gameView.getSelectMultiGamePanel(), gameView.getSelectModePane());
            });

            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getSingleGameBtn()),
                    () -> {
                        isSingleGameModeFlag = true;
                        transitView(gameView, gameView.getSelectModePane(), gameView.getSelectGamePanel());
                    });
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getMulitiGameBtn()),
                    () -> {
                        isSingleGameModeFlag = false;
                        transitView(gameView, gameView.getSelectModePane(), gameView.getSelectGamePanel());
                    });

            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getDiffReturnBtn()),
                    () -> transitView(gameView, gameView.getSelectModePane(), gameView.getSelectDiffPane()));
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getModeReturnBtn()),
                    () -> transitView(gameView, gameView.getSelectGamePanel(), gameView.getSelectModePane()));
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getGameReturnBtn()),
                    () -> transitView(contentPane, mainView, gameView));
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getMultiGameReturnBtn()),
                    () -> transitView(gameView, gameView.getSelectModePane(), gameView.getSelectMultiGamePanel()));

            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getContinueBtn()),
                    () -> {
                        if (isSingleGameModeFlag)
                            multiGameController.continueSingleGame();
                        else
                            multiGameController.continueMultiGame();
                    });
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getRestartBtn()),
                    () -> {
                        if (isSingleGameModeFlag)
                            multiGameController.restartSingleGame();
                        else
                            multiGameController.restartMultiGame();
                    });
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getReturnMainBtn()),
                    () -> {
                        if (isSingleGameModeFlag)
                            gameView.resetSingleStopPanel();
                        else
                            gameView.resetMultiStopPanel();
                        multiGameController.gamePlayer.endGame();
                        multiGameController.gamePlayer1.endGame();
                        multiGameController.gamePlayer2.endGame();
                        multiGameController.gameRobot.endGame();
                        multiGameController.stopTimer();

                        gameView.resetGameView();
                        transitView(contentPane, mainView, gameView);
                    });
            gameViewKeyMap.put(new KeyPair(stackKey, gameView.getExitGameBtn()),
                    () -> System.exit(0));
        }

        private void initOtherKeys() {
            gameViewKeyMap.put(new KeyPair(KeyEvent.VK_ENTER, gameView.getInputName()),
                    () -> {
                        multiGameController.saveUserName();
                        transitView(contentPane, scoreView, gameView);
                        gameView.getInputName().setText("");
                        gameView.resetGameView();
                    });
            gameViewKeyMap.put(new KeyPair(KeyEvent.VK_ESCAPE, multiGameController.singleGameFocusing),
                    () -> {
                        multiGameController.gamePlayer.stopGame();
                        multiGameController.gameTimer.stop();
                        gameView.setSingleStopPanel();
                        gameView.getContinueBtn().requestFocus();

                    });
            gameViewKeyMap.put(new KeyPair(KeyEvent.VK_ESCAPE, multiGameController.multiGameFocusing),
                    () -> {
                        multiGameController.gamePlayer1.stopGame();
                        multiGameController.gamePlayer2.stopGame();
                        multiGameController.gameRobot.stopGame();
                        multiGameController.robotController.stopRobot();
                        multiGameController.gameTimer.stop();
                        gameView.setMultiStopPanel();
                        gameView.getContinueBtn().requestFocus();
                    });
        }
    }

    private class InitSettingViewMap {
        JComboBox<String> displayComboBox = settingView.getDisplayComboBox();
        Map<JToggleButton, KeyMapping> btnmap;
        int keyBuffer;
        int upKey;
        int downKey;
        int leftKey;
        int rightKey;
        int stackKey;
        int up2Key;
        int down2Key;
        int left2Key;
        int right2Key;
        int stack2Key;
        List<Integer> keyList;

        private InitSettingViewMap(Setting setting) {
            resetMap();

            this.upKey = setting.getRotateKey();
            this.downKey = setting.getMoveDownKey();
            this.leftKey = setting.getMoveLeftKey();
            this.rightKey = setting.getMoveRightKey();
            this.stackKey = setting.getStackKey();
            this.up2Key = setting.getRotate2Key();
            this.down2Key = setting.getMoveDown2Key();
            this.left2Key = setting.getMoveLeft2Key();
            this.right2Key = setting.getMoveRight2Key();
            this.stack2Key = setting.getStack2Key();

            initKeyList();
            btnmap = new HashMap<>();
            btnmap.put(settingView.getSetUpKeyBtn(), this::setUpKey);
            btnmap.put(settingView.getSetDownKeyBtn(), this::setDownKey);
            btnmap.put(settingView.getSetLeftKeyBtn(), this::setLeftKey);
            btnmap.put(settingView.getSetRightKeyBtn(), this::setRightKey);
            btnmap.put(settingView.getSetStackKeyBtn(), this::setStackKey);
            btnmap.put(settingView.getSetUp2KeyBtn(), this::setUp2Key);
            btnmap.put(settingView.getSetDown2KeyBtn(), this::setDown2Key);
            btnmap.put(settingView.getSetLeft2KeyBtn(), this::setLeft2Key);
            btnmap.put(settingView.getSetRight2KeyBtn(), this::setRight2Key);
            btnmap.put(settingView.getSetStack2KeyBtn(), this::setStack2Key);
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
            keyList.add(up2Key);
            keyList.add(down2Key);
            keyList.add(left2Key);
            keyList.add(right2Key);
            keyList.add(stack2Key);
        }

        private void resetMap() {
            settingViewKeyMap = new HashMap<>();
        }

        private void initUpKey(int upKey) {
            for (Component comp : settingView.getComponents()) {
                settingViewKeyMap.put(new KeyPair(upKey, comp), comp::transferFocusBackward);
            }

            Component[] keyComponents = settingView.getInitKeyGridPane().getComponents();

            settingViewKeyMap.put(new KeyPair(upKey, keyComponents[10]), keyComponents[19]::transferFocus);
            settingViewKeyMap.put(new KeyPair(upKey, keyComponents[11]), keyComponents[0]::transferFocus);
            settingViewKeyMap.put(new KeyPair(upKey, keyComponents[12]), keyComponents[1]::transferFocus);
            settingViewKeyMap.put(new KeyPair(upKey, keyComponents[13]), keyComponents[2]::transferFocus);
            settingViewKeyMap.put(new KeyPair(upKey, keyComponents[14]), keyComponents[3]::transferFocus);

            settingViewKeyMap.put(new KeyPair(upKey, displayComboBox), () -> {
                int a = displayComboBox.getSelectedIndex();
                int b = settingController.getDisplayList().size();
                displayComboBox.setSelectedIndex((b - (a % (b + 1))) - ((1 + a) % b));
                displayComboBox.hidePopup();

            });
        }

        private void initDownKey(int downKey) {
            for (Component comp : settingView.getComponents()) {
                settingViewKeyMap.put(new KeyPair(downKey, comp), comp::transferFocus);
            }

            Component[] keyComponents = settingView.getInitKeyGridPane().getComponents();

            settingViewKeyMap.put(new KeyPair(downKey, keyComponents[0]), keyComponents[9]::transferFocus);
            settingViewKeyMap.put(new KeyPair(downKey, keyComponents[1]), keyComponents[10]::transferFocus);
            settingViewKeyMap.put(new KeyPair(downKey, keyComponents[2]), keyComponents[11]::transferFocus);
            settingViewKeyMap.put(new KeyPair(downKey, keyComponents[3]), keyComponents[12]::transferFocus);
            settingViewKeyMap.put(new KeyPair(downKey, keyComponents[4]), keyComponents[13]::transferFocus);

            settingViewKeyMap.put(new KeyPair(downKey, displayComboBox), () -> {
                displayComboBox.setSelectedIndex((displayComboBox.getSelectedIndex() + 1)
                        % settingController.getDisplayList().size());
                displayComboBox.hidePopup();
            });
        }

        private void initStackKey(int stackKey) {
            String inputMessage = settingView.getInputMessage();

            /* StackKey */
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getReturnSettingToMainBtn()), () -> {
                transitView(contentPane, mainView, settingView);
                settingView.setInitKeyBtnsFocusable(false);
                settingView.setSettingBtnsFocusable(true);
                settingView.setInitKeyBtnsFocusable(false);
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getIsColorBlindBtn()), () -> {
                boolean isColorBlind = settingController.isColorBlindMode();
                settingView.setIsColorBlindBtn(!isColorBlind);
                settingController.setColorBlindMode(!isColorBlind);
                settingController.saveSetting();
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getSetDisplayBtn()), () -> {
                displayComboBox.setFocusable(true);
                displayComboBox.requestFocus();
            });
            settingViewKeyMap.put(new KeyPair(stackKey, displayComboBox), () -> {
                displayComboBox.setFocusable(false);
                settingController.setDisplayMode(displayComboBox.getSelectedIndex());
                settingController.saveSetting();
                resizeJFrame();
                resizeViewFont();

                settingView.getSetDisplayBtn().requestFocus();
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getInitKeyBtn()), () -> {
                settingView.setInitKeyBtnsFocusable(true);
                settingView.setSettingBtnsFocusable(false);
                settingView.getInitKeyGridReturnBtn().requestFocus();
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getInitKeyGridReturnBtn()), () -> {
                settingView.setInitKeyBtnsFocusable(false);
                settingView.setSettingBtnsFocusable(true);
                settingView.getInitKeyBtn().requestFocus();
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getInitMenuBtn()), () -> {
                settingView.setInitSettingBtnsFocusable(true);
                settingView.setSettingBtnsFocusable(false);
                settingView.getInitReturnBtn().requestFocus();
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getInitReturnBtn()), () -> {
                settingView.setInitSettingBtnsFocusable(false);
                settingView.setSettingBtnsFocusable(true);
                settingView.getInitMenuBtn().requestFocus();
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getInitSettingBtn()), () -> {
                settingController.resetSetting();
                initSettingView();
                initGameView();
                initJFrame();
                settingView.getReturnSettingToMainBtn().requestFocus();
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getSetUpKeyBtn()), () -> {
                settingView.getUpKeyLabel().setText(inputMessage);
                settingView.getUpKeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetUpKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = upKey;
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getSetDownKeyBtn()), () -> {
                settingView.getDownKeyLabel().setText(inputMessage);
                settingView.getDownKeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetDownKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = downKey;
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getSetLeftKeyBtn()), () -> {
                settingView.getLeftKeyLabel().setText(inputMessage);
                settingView.getLeftKeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetLeftKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = leftKey;
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getSetRightKeyBtn()), () -> {
                settingView.getRightKeyLabel().setText(inputMessage);
                settingView.getRightKeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetRightKeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = rightKey;
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getSetStackKeyBtn()), () -> {
                if (!settingView.getSetStackKeyBtn().isSelected()) {
                    settingView.getStackKeyLabel().setText(inputMessage);
                    settingView.getStackKeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                    settingView.getSetStackKeyBtn().setSelected(true);

                    settingFlag = true;
                    keyBuffer = stackKey;
                }
            });

            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getSetUp2KeyBtn()), () -> {
                settingView.getUp2KeyLabel().setText(inputMessage);
                settingView.getUp2KeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetUp2KeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = up2Key;
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getSetDown2KeyBtn()), () -> {
                settingView.getDown2KeyLabel().setText(inputMessage);
                settingView.getDown2KeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetDown2KeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = down2Key;
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getSetLeft2KeyBtn()), () -> {
                settingView.getLeft2KeyLabel().setText(inputMessage);
                settingView.getLeft2KeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetLeft2KeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = left2Key;
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getSetRight2KeyBtn()), () -> {
                settingView.getRight2KeyLabel().setText(inputMessage);
                settingView.getRight2KeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                settingView.getSetRight2KeyBtn().setSelected(true);
                settingFlag = true;
                keyBuffer = right2Key;
            });
            settingViewKeyMap.put(new KeyPair(stackKey, settingView.getSetStack2KeyBtn()), () -> {
                if (!settingView.getSetStack2KeyBtn().isSelected()) {
                    settingView.getStack2KeyLabel().setText(inputMessage);
                    settingView.getStack2KeyLabel().setForeground(MasterView.WARNING_FONT_COLOR);
                    settingView.getSetStack2KeyBtn().setSelected(true);

                    settingFlag = true;
                    keyBuffer = stack2Key;
                }
            });
        }

        private void initLeftKey(int leftKey) {
            for (Component comp : settingView.getInitKeyGridPane().getComponents()) {
                if (!(comp.getClass().equals(JLabel.class))) {
                    settingViewKeyMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
                }
            }

            Component[] keyComponents = settingView.getInitKeyGridPane().getComponents();
            settingViewKeyMap.put(new KeyPair(leftKey, keyComponents[0]), keyComponents[4]::transferFocus);

            for (Component comp : settingView.getInitSettingPane().getComponents()) {
                settingViewKeyMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            }
        }

        private void initRightKey(int rightKey) {
            for (Component comp : settingView.getInitKeyGridPane().getComponents()) {
                if (!(comp.getClass().equals(JLabel.class))) {
                    settingViewKeyMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
                }
            }

            Component[] keyComponents = settingView.getInitKeyGridPane().getComponents();
            settingViewKeyMap.put(new KeyPair(rightKey, keyComponents[14]), keyComponents[4]::transferFocus);

            for (Component comp : settingView.getInitSettingPane().getComponents()) {
                settingViewKeyMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            }
        }

        private void setKeyByToggleButton(JToggleButton btn, int key) {
            btnmap.get(btn).doMapping(key);
        }

        private void setUpKey(int upKey) {
            this.upKey = upKey;
            settingController.setRotateKey(upKey);
            resetMap();
            initAllKey();
            initMainViewKeyMap.initAllKey();
            initGameViewKeyMap.initAllKey();
        }

        private void setDownKey(int downKey) {
            this.downKey = downKey;
            settingController.setMoveDownKey(downKey);
            resetMap();
            initAllKey();
            initGameViewKeyMap.initAllKey();
        }

        private void setLeftKey(int leftKey) {
            this.leftKey = leftKey;
            settingController.setMoveLeftKey(leftKey);
            resetMap();
            initAllKey();
            initMainViewKeyMap.initAllKey();
            initGameViewKeyMap.initAllKey();
        }

        private void setRightKey(int rightKey) {
            this.rightKey = rightKey;
            settingController.setMoveRightKey(rightKey);
            resetMap();
            initAllKey();
            initMainViewKeyMap.initAllKey();
            initGameViewKeyMap.initAllKey();
        }

        private void setStackKey(int stackKey) {
            this.stackKey = stackKey;
            settingController.setStackKey(stackKey);
            resetMap();
            initAllKey();
            initMainViewKeyMap.initAllKey();
            initGameViewKeyMap.initAllKey();
        }

        private void setUp2Key(int up2Key) {
            this.up2Key = up2Key;
            settingController.setRotate2Key(up2Key);
            resetMap();
            initAllKey();
            initMainViewKeyMap.initAllKey();
        }

        private void setDown2Key(int down2Key) {
            this.down2Key = down2Key;
            settingController.setMoveDown2Key(down2Key);
            resetMap();
            initAllKey();
        }

        private void setLeft2Key(int left2Key) {
            this.left2Key = left2Key;
            settingController.setMoveLeft2Key(left2Key);
            resetMap();
            initAllKey();
            initMainViewKeyMap.initAllKey();
        }

        private void setRight2Key(int right2Key) {
            this.right2Key = right2Key;
            settingController.setMoveRight2Key(right2Key);
            resetMap();
            initAllKey();
            initMainViewKeyMap.initAllKey();
        }

        private void setStack2Key(int stack2Key) {
            this.stack2Key = stack2Key;
            settingController.setStack2Key(stack2Key);
            resetMap();
            initAllKey();
            initMainViewKeyMap.initAllKey();
        }

        private boolean checkKeyOverlap(int key) {
            if (key == keyBuffer)
                return false;
            return keyList.contains(key);
        }
    }

    @FunctionalInterface
    private interface PressedKeyEvent {
        void isKeyPressed();
    }

    @FunctionalInterface
    private interface KeyMapping {
        void doMapping(int key);
    }

    private class MainKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            KeyPair key = new KeyPair(e.getKeyCode(), e.getComponent());
            if (mainViewKeyMap.containsKey(key))
                mainViewKeyMap.get(key).isKeyPressed();
        }
    }

    private class GameKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            KeyPair key = new KeyPair(e.getKeyCode(), e.getComponent());
            if (gameViewKeyMap.containsKey(key))
                gameViewKeyMap.get(key).isKeyPressed();
        }
    }

    private class SettingKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            Component comp = e.getComponent();
            int pressedKey = e.getKeyCode();
            KeyPair key = new KeyPair(e.getKeyCode(), comp);
            Map<JToggleButton, JLabel> setKeymap = settingView.getSetKeyMap();
            if (settingFlag) {
                setKeymap.keySet().stream().filter(AbstractButton::isSelected).forEach(x -> {
                    if (initSettingViewMap.checkKeyOverlap(pressedKey)) {
                        setKeymap.get(x).setText(OVERLAP_KEY_MSG);
                    } else {
                        initSettingViewMap.setKeyByToggleButton(x, pressedKey);
                        setKeymap.get(x).setText(KeyEvent.getKeyText(pressedKey));
                        setKeymap.get(x).setForeground(MasterView.BASIC_FONT_COLOR);
                        x.setSelected(false);
                        setMainKeyLabel();
                        settingFlag = false;
                    }
                });
            } else if (settingViewKeyMap.containsKey(key))
                settingViewKeyMap.get(key).isKeyPressed();
        }
    }

    private void setMainKeyLabel() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player1: ");
        sb.append(KeyEvent.getKeyText(settingController.getRotateKey()));
        sb.append(", ");
        sb.append(KeyEvent.getKeyText(settingController.getMoveDownKey()));
        sb.append(", ");
        sb.append(KeyEvent.getKeyText(settingController.getMoveLeftKey()));
        sb.append(", ");
        sb.append(KeyEvent.getKeyText(settingController.getMoveRightKey()));
        sb.append(", ");
        sb.append(KeyEvent.getKeyText(settingController.getStackKey()));
        sb.append(" Player2: ");
        sb.append(KeyEvent.getKeyText(settingController.getRotate2Key()));
        sb.append(", ");
        sb.append(KeyEvent.getKeyText(settingController.getMoveDown2Key()));
        sb.append(", ");
        sb.append(KeyEvent.getKeyText(settingController.getMoveLeft2Key()));
        sb.append(", ");
        sb.append(KeyEvent.getKeyText(settingController.getMoveRight2Key()));
        sb.append(", ");
        sb.append(KeyEvent.getKeyText(settingController.getStack2Key()));
        mainView.getAllKeyLabel().setText(sb.toString());
    }
}