package tetris.controller;

import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.util.List;

import tetris.model.*;
import tetris.view.*;

public class SingleGameController {

    protected GameView gameView;
    protected ScoreView scoreView;

    JTextPane gamepane;
    protected Timer gameTimer;

    GameController gamePlayer;
    protected PlayerController playerController;

    protected int diffMode;
    protected int gameMode;
    protected int gameTime;
    protected List<Integer> randomBlockList;

    public SingleGameController(PlayerController playerController) {
        diffMode = 0;
        gameMode = 0;
        randomBlockList = new ArrayList<>();
        this.playerController = playerController;
        this.gameView = GameView.getInstance();
        scoreView = ScoreView.getInstance();
        gamepane = gameView.getPlayerOneGameBoardPane();
        JTextPane nextBlockPane = gameView.getPlayerOneNextBlockPane();
        JTextPane attackLinePane = gameView.getPlayerOneAttackLinePane();
        JLabel scoreLabel = gameView.getPlayerOneScoreLabel();
        gameTime = 0;
        gamePlayer = new GameController(gamepane, nextBlockPane, attackLinePane, scoreLabel,
                gamepane) {
            @Override
            public void doAfterGameOver() {
                gameView.add(gameView.getGameOverPanel());
                gameView.remove(gameView.getSingleGameDisplayPane());
                gameView.getInputName().requestFocus();
                gamePlayer.endGame();
                gameTimer.stop();
            }

            @Override
            public void doBeforeTakeOutNextBlock() {
                if (blockDeque.size() < 3) {
                    generateBlockRandomizer(diffMode);
                    blockDeque.addAll(randomBlockList);
                }
            }
        };
    }

    protected void startSingleGame(Setting setting) {

        gamePlayer.setPlayerKeys(setting.getRotateKey(), setting.getMoveDownKey(), setting.getMoveLeftKey(),
                setting.getMoveRightKey(), setting.getStackKey());
        generateBlockRandomizer(diffMode);
        gamePlayer.startGame(diffMode, gameMode, randomBlockList);
        gamepane.requestFocus(true);
        showTime();
        if (gameMode == GameController.TIME_ATTACK_MODE) {
            gameTime = 100;
            showTime();
            startTimeAttack();
        } else {
            showTime();
            startStopWatch();
        }
    }

    protected void setDiffMode(int diffMode) {
        this.diffMode = diffMode;
    }

    protected void setGameMode(int gameMode) {
        this.gameMode = gameMode;
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

    protected void startStopWatch() {
        gameTimer = new Timer(1000, e -> {
            gameTime++;
            showTime();
        });
        gameTimer.start();
    }

    protected void startTimeAttack() {
        gameTimer = new Timer(1000, e -> {
            gameTime--;
            showTime();
            if (gameTime == 0) {
                gamePlayer.doAfterGameOver();
            }
        });
        gameTimer.start();
    }

    protected void showTime() {
        gameView.getTimeLabel().setText(String.format("%d", gameTime));
    }

}
