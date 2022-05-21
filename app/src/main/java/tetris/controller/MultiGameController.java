package tetris.controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import java.util.List;

import tetris.model.*;
import tetris.view.*;

public class MultiGameController {

    GameController gamePlayer1;
    GameController gamePlayer2;
    GameView gameView = GameView.getInstance();

    private List<Integer> randomBlockList;

    protected int gameTime;
    protected Timer gameTimer;

    public MultiGameController() {
        JTextPane gamepane1 = gameView.getPlayerOneGameBoardPane();
        JTextPane nextBlockPane1 = gameView.getPlayerOneNextBlockPane();
        JTextPane attackLinePane1 = gameView.getPlayerOneAttackLinePane();
        JLabel scoreLabel1 = gameView.getPlayerOneScoreLabel();
        JTextPane gamepane2 = gameView.getPlayerTwoGameBoardPane();
        JTextPane nextBlockPane2 = gameView.getPlayerTwoNextBlockPane();
        JTextPane attackLinePane2 = gameView.getPlayerTwoAttackLinePane();
        JLabel scoreLabel2 = gameView.getPlayerTwoScoreLabel();
        gamePlayer1 = new GameController(gamepane1, nextBlockPane1, attackLinePane1, scoreLabel1, gamepane2) {
            @Override
            void doAfterGameOver() {
                gameView.add(gameView.getGameOverPanel());
                gameView.remove(gameView.getMulitiGameDisplayPane());
                gameView.getInputName().requestFocus();
                gamePlayer1.endGame();
            }

            @Override
            void doBeforeTakeOutNextBlock() {
                if (attackLines > 0) {
                    underAttack();
                }
                if (blockDeque.isEmpty()) {
                    generateBlockRandomizer(GameController.NORMAL_MODE);
                    blockDeque.addAll(randomBlockList);
                    opponentBlockDeque.addAll(randomBlockList);
                }
            }
        };

        gamePlayer2 = new GameController(gamepane2, nextBlockPane2, attackLinePane2, scoreLabel2, gamepane2) {
            @Override
            void doAfterGameOver() {
                gameView.add(gameView.getGameOverPanel());
                gameView.remove(gameView.getSingleGameDisplayPane());
                gameView.getInputName().requestFocus();
                gamePlayer2.endGame();
            }

            @Override
            void doBeforeTakeOutNextBlock() {
                if (attackLines > 0) {
                    underAttack();
                }
                if (blockDeque.isEmpty()) {
                    generateBlockRandomizer(GameController.NORMAL_MODE);
                    blockDeque.addAll(randomBlockList);
                    opponentBlockDeque.addAll(randomBlockList);
                }
            }
        };

        gamePlayer1.setOpponentPlayer(gamePlayer2);
        gamePlayer2.setOpponentPlayer(gamePlayer1);
    }

    public void startLocalGame(Setting setting) {
        generateBlockRandomizer(GameController.NORMAL_MODE);

        gamePlayer1.setPlayerKeys(setting.getRotateKey(), setting.getMoveDownKey(), setting.getMoveLeftKey(),
                setting.getMoveRightKey(), setting.getStackKey());
        gamePlayer2.setPlayerKeys(setting.getRotate2Key(), setting.getMoveDown2Key(), setting.getMoveLeft2Key(),
                setting.getMoveRight2Key(), setting.getStack2Key());

        gamePlayer1.startGame(GameController.NORMAL_MODE, GameController.GENERAL_GAME_MODE, randomBlockList);
        gamePlayer2.startGame(GameController.NORMAL_MODE, GameController.GENERAL_GAME_MODE, randomBlockList);
        gameTime = 0;
        showTime();
        startStopWatch();
    }

    void generateBlockRandomizer(int mode) {
        int jBlock = Block.JBLOCK_IDENTIFY_NUMBER;
        int lBlock = Block.LBLOCK_IDENTIFY_NUMBER;
        int zBlock = Block.ZBLOCK_IDENTIFY_NUMBER;
        int sBlock = Block.SBLOCK_IDENTIFY_NUMBER;
        int tBlock = Block.TBLOCK_IDENTIFY_NUMBER;
        int oBlock = Block.OBLOCK_IDENTIFY_NUMBER;
        int iBlock = Block.IBLOCK_IDENTIFY_NUMBER;

        randomBlockList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<Integer> blockList = Arrays.asList(jBlock, lBlock, zBlock, sBlock, tBlock, oBlock, iBlock);
            Collections.shuffle(blockList);
            randomBlockList.addAll(blockList);
        }

        // easy mode
        if (mode == 1) {
            List<Integer> blockList = Arrays.asList(jBlock, lBlock, zBlock, sBlock, tBlock, oBlock, iBlock);
            Collections.shuffle(blockList);
            randomBlockList.addAll(blockList);
            randomBlockList.add(iBlock);
        }

        // hard mode
        else if (mode == 2) {
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
                // TimeAttack 이후 할 일
                gamePlayer1.doAfterGameOver();
            }
        });
        gameTimer.start();
    }

    protected void showTime() {
        gameView.getTimeLabel().setText(String.format("%d", gameTime));
    }
}
