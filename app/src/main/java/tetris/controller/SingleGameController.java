package tetris.controller;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.awt.*;

import tetris.model.*;
import tetris.view.*;

public class SingleGameController {

    protected static final int GAME_TIME = 100;

    protected GameView gameView;
    protected ScoreView scoreView;
    protected MainView mainView;

    protected int resoultion;
    protected Setting setting;

    JTextPane singleGameFocusing;
    JTextPane gamePane;
    protected Timer gameTimer;
    protected JLabel singleTimeLabel;

    GameController gamePlayer;
    protected PlayerController playerController;
    protected ViewController viewController;

    protected boolean isSingleGameModeFlag;
    protected int diffMode;
    protected int gameMode;
    protected int gameTime;
    protected List<Integer> randomBlockList;

    public SingleGameController(PlayerController playerController, ViewController viewController) {
        diffMode = 0;
        gameMode = 0;
        randomBlockList = new ArrayList<>();
        this.playerController = playerController;
        this.viewController = viewController;
        this.gameView = GameView.getInstance();
        scoreView = ScoreView.getInstance();
        mainView = MainView.getInstance();
        gamePane = gameView.getSinglePlayerGameBoardPane();
        singleGameFocusing = gamePane;
        singleTimeLabel = gameView.getSingleGameTimeLabel();
        JTextPane nextBlockPane = gameView.getSinglePlayerOneNextBlockPane();
        JLabel scoreLabel = gameView.getSingleScoreLabel();
        gameTime = 0;
        gamePlayer = new GameController(gamePane, nextBlockPane, new JTextPane(), scoreLabel,
                singleGameFocusing) {
            @Override
            public void doAfterGameOver() {
                gamePlayer.endGame();
                gameTimer.stop();
                gameView.setGameOver();
                Timer timer = new Timer(5000, e -> {
                    gameView.getGameOverLabel().requestFocus();
                });
                timer.setRepeats(false);
                timer.start();

            }

            @Override
            public void doBeforeTakeOutNextBlock() {
                if (blockDeque.size() < 3) {
                    generateBlockRandomizer(diffMode);
                    blockDeque.addAll(randomBlockList);
                }
            }
        };

        gameView.getGameOverLabel().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                transitGameOver();
            }
        });

    }

    protected void startSingleGame(Setting setting) {
        this.setting = setting;
        isSingleGameModeFlag = true;
        gamePlayer.loadSetting(setting);
        gamePlayer.setPlayerKeys(setting.getRotateKey(), setting.getMoveDownKey(), setting.getMoveLeftKey(),
                setting.getMoveRightKey(), setting.getStackKey());

        generateBlockRandomizer(diffMode);
        resoultion = gameView.getWidth() * gameView.getHeight();
        gameView.getGameOverLabel().setPreferredSize(new Dimension(gameView.getWidth(), gameView.getHeight()));
        System.out.println(resoultion);
        gamePlayer.startGame(diffMode, gameMode, randomBlockList, resoultion);
        gamePane.requestFocus(true);
        gamePlayer.setDeleteLines(gameView.getSingleLinesLabel());
        gamePlayer.showDeleteLines();
        showMode();
        showTime(singleTimeLabel);
        startTimer(gameView.getSingleGameDisplayTimeLabel(), singleTimeLabel);
    }

    protected void setDiffMode(int diffMode) {
        this.diffMode = diffMode;
    }

    protected void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    protected void transitGameOver() {
        gameView.resetSingleGameDisplayPane();
        gameView.add(gameView.getGameOverPanel());
        gameView.remove(gameView.getSingleGameDisplayPane());
        gameView.getInputName().requestFocus();

    }

    // 유저 저장 메소드
    protected void saveUserName() {
        String difficulty = "normal";
        if (diffMode == GameController.EASY_MODE)
            difficulty = "easy";
        else if (diffMode == GameController.HARD_MODE)
            difficulty = "hard";
        String userName = gameView.getInputName().getText();
        playerController.addPlayer(userName, gamePlayer.score, difficulty);
        playerController.savePlayerList();
        playerController.loadPlayerList();
        scoreView.resetRankingPane();
        scoreView.resetRankingList();

        playerController.getPlayerList()
                .forEach(player -> scoreView.addRankingList(new ArrayList<>(Arrays.asList(player.getName(),
                        Integer.toString(player.getScore()), player.getDifficulty()))));
        scoreView.fillScoreBoard(userName);
        gameView.resetGameView();
    }

    protected void generateBlockRandomizer(int mode) {
        int jBlock = Block.JBLOCK_IDENTIFY_NUMBER;
        int lBlock = Block.LBLOCK_IDENTIFY_NUMBER;
        int zBlock = Block.ZBLOCK_IDENTIFY_NUMBER;
        int sBlock = Block.SBLOCK_IDENTIFY_NUMBER;
        int tBlock = Block.TBLOCK_IDENTIFY_NUMBER;
        int oBlock = Block.OBLOCK_IDENTIFY_NUMBER;
        int iBlock = Block.IBLOCK_IDENTIFY_NUMBER;

        randomBlockList.clear();
        for (int i = 0; i < 4; i++) {
            List<Integer> blockList = Arrays.asList(jBlock, lBlock, zBlock, sBlock, tBlock, oBlock, iBlock);
            Collections.shuffle(blockList);
            randomBlockList.addAll(blockList);
        }

        // easy mode
        if (mode == GameController.EASY_MODE) {
            List<Integer> blockList = Arrays.asList(jBlock, lBlock, zBlock, sBlock, tBlock, oBlock, iBlock);
            Collections.shuffle(blockList);
            randomBlockList.addAll(blockList);
            randomBlockList.add(iBlock);
        }

        // hard mode
        else if (mode == GameController.HARD_MODE) {
            List<Integer> blockList = Arrays.asList(jBlock, lBlock, zBlock, sBlock, tBlock, oBlock);
            Collections.shuffle(blockList);
            randomBlockList.addAll(blockList);
        }
    }

    protected void startTimer(JLabel timeDescribtion, JLabel timeLabel) {
        if (gameMode == GameController.TIME_ATTACK_MODE) {
            gameTime = GAME_TIME;
            showTime(timeLabel);
            timeDescribtion.setText(GameView.TIME_ATTACK_STRING);
            startTimeAttack(timeLabel);
        } else {
            showTime(timeLabel);
            timeDescribtion.setText(GameView.STOP_WATCH_STRING);
            startStopWatch(timeLabel);
        }
    }

    public void stopTimer() {
        gameTimer.setRepeats(false);
        gameTimer.stop();
    }

    protected void startStopWatch(JLabel timeLabel) {
        gameTimer = new Timer(1000, e -> {
            gameTime++;
            showTime(timeLabel);
        });
        gameTimer.start();
    }

    protected void startTimeAttack(JLabel timeLabel) {
        gameTimer = new Timer(1000, e -> {
            gameTime--;
            showTime(timeLabel);
            if (gameTime == 0) {
                doAfterTimeAttack();
            }
        });
        gameTimer.start();
    }

    protected void doAfterTimeAttack() {
        gamePlayer.doAfterGameOver();
    }

    protected void showTime(JLabel timeLabel) {
        timeLabel.setText(String.format("%d초", gameTime));
    }

    protected void showMode() {
        String difficulty = "normal";
        if (diffMode == GameController.EASY_MODE)
            difficulty = "easy";
        else if (diffMode == GameController.HARD_MODE)
            difficulty = "hard";
        String mode = "일반 모드";
        if (gameMode == GameController.ITEM_GAME_MODE)
            mode = "아이템모드";
        else if (gameMode == GameController.TIME_ATTACK_MODE)
            mode = "시간제한모드";
        gameView.getGameDiffLabel().setText(difficulty);
        gameView.getMultiGameModeLabel().setText(mode);
        gameView.getSingleGameModeLabel().setText(mode);
    }

    protected void continueSingleGame() {
        gameView.resetSingleStopPanel();
        gamePlayer.continuGame();
        gameTimer.restart();
        singleGameFocusing.requestFocus();
    }

    protected void restartSingleGame() {
        gamePlayer.resetGame();
        gameView.resetSingleStopPanel();
        startSingleGame(setting);
        singleGameFocusing.requestFocus();
    }

}
