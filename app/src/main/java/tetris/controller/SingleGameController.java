package tetris.controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import tetris.model.*;
import tetris.view.*;

public class SingleGameController {

    private GameView gameView;
    private ScoreView scoreView;
    private JTextPane gamePane;
    private Container contentPane;

    private Map<KeyPair, Runnable> gameKeyMap;
    GameController player1;
    private PlayerController playerController;

    private int diffMode;
    private int gameMode;
    private String userName;

    public SingleGameController(Setting setting, PlayerController playerController, Container contentPane) {
        this.playerController = playerController;
        this.contentPane = contentPane;
        scoreView = ScoreView.getInstance();
        gameView = GameView.getInstance();
        gamePane = gameView.getPlayerOneGameBoardPane();
        player1 = new GameController(setting, contentPane, gamePane,
                gameView.getPlayerOneNextBlockPane(), gameView.getPlayerOneAttackLinePane()) {
            @Override
            void doAfterGameOver() {
                player1.endGame();
                transitView(gameView, gameView.getGameOverPanel(), gameView.getSingleGameDisplayPane());
            }
        };
        new InitGameKeyMap(setting);
        addSingleGameKeyListener();
    }

    private void addSingleGameKeyListener() {
        SingleGameKeyListener gameKeyListener = new SingleGameKeyListener();
        for (Component comp : gameView.getSelectDiffPane().getComponents())
            comp.addKeyListener(gameKeyListener);
        for (Component comp : gameView.getSelectModePane().getComponents())
            comp.addKeyListener(gameKeyListener);
        for (Component comp : gameView.getGameOverPanel().getComponents())
            comp.addKeyListener(gameKeyListener);
    }

    public class SingleGameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            KeyPair key = new KeyPair(e.getKeyCode(), e.getComponent());
            if (gameKeyMap.containsKey(key))
                gameKeyMap.get(key).run();
        }
    }

    private class InitGameKeyMap {

        int leftKey;
        int rightKey;
        int stackKey;

        private InitGameKeyMap(Setting setting) {
            loadSetting(setting);
            resetMap();
            initAllKey();
        }

        private void loadSetting(Setting setting) {
            this.leftKey = setting.getMoveLeftKey();
            this.rightKey = setting.getMoveRightKey();
            this.stackKey = setting.getStackKey();
        }

        private void initAllKey() {
            initLeftKey();
            initRightKey();
            initStackKey();
            initOtherKeys();
        }

        private void resetMap() {
            gameKeyMap = new HashMap<>();
        }

        private void initLeftKey() {
            for (Component comp : gameView.getSelectDiffPane().getComponents())
                gameKeyMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            gameKeyMap.put(new KeyPair(leftKey, gameView.getEasyBtn()),
                    () -> gameView.getHardBtn().requestFocus(true));
            for (Component comp : gameView.getSelectModePane().getComponents())
                gameKeyMap.put(new KeyPair(leftKey, comp), comp::transferFocusBackward);
            gameKeyMap.put(new KeyPair(leftKey, gameView.getGeneralModeBtn()),
                    () -> gameView.getTimeAttackBtn().requestFocus(true));
        }

        private void initRightKey() {
            for (Component comp : gameView.getSelectDiffPane().getComponents())
                gameKeyMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            gameKeyMap.put(new KeyPair(rightKey, gameView.getHardBtn()),
                    () -> gameView.getEasyBtn().requestFocus(true));
            for (Component comp : gameView.getSelectModePane().getComponents())
                gameKeyMap.put(new KeyPair(rightKey, comp), comp::transferFocus);
            gameKeyMap.put(new KeyPair(rightKey, gameView.getTimeAttackBtn()),
                    () -> gameView.getGeneralModeBtn().requestFocus(true));
        }

        private void initStackKey() {
            gameKeyMap.put(new KeyPair(stackKey, gameView.getEasyBtn()), () -> {
                diffMode = GameController.EASY_MODE;
                transitView(gameView, gameView.getMulitiGameDisplayPane(), gameView.getSelectDiffPane());
                player1.startGame(diffMode, gameMode);
            });
            gameKeyMap.put(new KeyPair(stackKey, gameView.getNormalBtn()), () -> {
                diffMode = GameController.NORMAL_MODE;
                transitView(gameView, gameView.getMulitiGameDisplayPane(), gameView.getSelectDiffPane());
                player1.startGame(diffMode, gameMode);
            });
            gameKeyMap.put(new KeyPair(stackKey, gameView.getHardBtn()), () -> {
                diffMode = GameController.HARD_MODE;
                transitView(gameView, gameView.getMulitiGameDisplayPane(), gameView.getSelectDiffPane());
                player1.startGame(diffMode, gameMode);
            });
            gameKeyMap.put(new KeyPair(stackKey, gameView.getGeneralModeBtn()), () -> {
                gameMode = GameController.GENERAL_GAME_MODE;
                transitView(gameView, gameView.getSelectDiffPane(), gameView.getSelectModePane());
            });
            gameKeyMap.put(new KeyPair(stackKey, gameView.getItemModeBtn()), () -> {
                gameMode = GameController.ITEM_GAME_MODE;
                transitView(gameView, gameView.getSelectDiffPane(), gameView.getSelectModePane());
            });
            gameKeyMap.put(new KeyPair(stackKey, gameView.getTimeAttackBtn()), () -> {
                gameMode = GameController.TIME_ATTACK_MODE;
                transitView(gameView, gameView.getSelectDiffPane(), gameView.getSelectModePane());
            });
        }

        private void initOtherKeys() {
            gameKeyMap.put(new KeyPair(KeyEvent.VK_ENTER, gameView.getInputName()), () -> {
                saveUserName();
            });
        }
    }

    // 전환함수
    private void transitView(Container pane, Container to, Container from) {
        pane.add(to);
        pane.remove(from);
        focus(to);
    }

    private void focus(Container to) {
        if (to.equals(gameView.getSelectDiffPane()))
            gameView.getEasyBtn().requestFocus();
        else if (to.equals(gameView.getSelectModePane()))
            gameView.getGeneralModeBtn().requestFocus();
        else if (to.equals(gameView.getSingleGameDisplayPane()))
            gamePane.requestFocus();
        else if (to.equals(scoreView))
            scoreView.getReturnScoreToMainBtn().requestFocus();
    }

    // 유저 저장 메소드
    void saveUserName() {
        String difficulty = "normal";
        if (diffMode == GameController.EASY_MODE)
            difficulty = "easy";
        else if (diffMode == GameController.HARD_MODE)
            difficulty = "hard";
        userName = gameView.getInputName().getText();
        playerController.addPlayer(userName, player1.score, difficulty);
        System.out.println("선수의 점수: " + player1.score);
        playerController.savePlayerList();
        playerController.loadPlayerList();
        scoreView.resetRankingPane();
        scoreView.resetRankingList();
        playerController.getPlayerList()
                .forEach(player -> System.out.println(player.getName() + player.getScore() + player.getDifficulty()));

        playerController.getPlayerList()
                .forEach(player -> scoreView.addRankingList(new ArrayList<>(Arrays.asList(player.getName(),
                        Integer.toString(player.getScore()), player.getDifficulty()))));
        scoreView.fillScoreBoard(userName);
        gameView.resetGameView();
        transitView(contentPane, scoreView, gameView);
    }
}
