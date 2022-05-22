package tetris.controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

import tetris.model.*;
import tetris.view.*;

public class MultiGameController {

    GameController gamePlayer1;
    GameController gamePlayer2;
    GameView gameView = GameView.getInstance();

    private List<Integer> randomBlockList;

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
                gameView.remove(gameView.getSingleGameDisplayPane());
                gameView.getInputName().requestFocus();
                gamePlayer1.endGame();
            }

            @Override
            void doAfterArriveBottom() {
                if (blockDeque.size() < 3) {
                    generateBlockRandomizer(GameController.NORMAL_MODE);
                    blockDeque.addAll(randomBlockList);
                }
            }
        };

        gamePlayer2 = new GameController(gamepane2, nextBlockPane2,
                attackLinePane2, scoreLabel2,
                gamepane2) {
            @Override
            void doAfterGameOver() {
                gameView.add(gameView.getGameOverPanel());
                gameView.remove(gameView.getSingleGameDisplayPane());
                gameView.getInputName().requestFocus();
                gamePlayer2.endGame();
            }

            @Override
            void doAfterArriveBottom() {
                if (blockDeque.size() < 3) {
                    generateBlockRandomizer(GameController.NORMAL_MODE);
                    blockDeque.addAll(randomBlockList);
                }
            }
        };

        // 게임 오른쪽 부분 배치 고정; 블럭이 쌓일 때 흔들림 방지
        if (ViewController.screenWidthNum == 0) {
            nextBlockPane1.setPreferredSize(new Dimension(85, 128));
            attackLinePane1.setPreferredSize(new Dimension(57, 84));
            nextBlockPane2.setPreferredSize(new Dimension(85, 128));
            attackLinePane2.setPreferredSize(new Dimension(85, 84));
            System.out.println("GameView is " + ViewController.screenWidthNum);
        }
        else if (ViewController.screenWidthNum == 1) {
            nextBlockPane1.setPreferredSize(new Dimension(87, 175));
            attackLinePane1.setPreferredSize(new Dimension(87, 116));
            nextBlockPane2.setPreferredSize(new Dimension(87, 175));
            attackLinePane2.setPreferredSize(new Dimension(87, 116));
            System.out.println("GameView is " + ViewController.screenWidthNum);
        }
        else {
            nextBlockPane1.setPreferredSize(new Dimension(100, 150));
            attackLinePane1.setPreferredSize(new Dimension(100, 100));
            nextBlockPane2.setPreferredSize(new Dimension(100, 150));
            attackLinePane2.setPreferredSize(new Dimension(100, 100));
            System.out.println("GameView is " + ViewController.screenWidthNum);
        }
    }

    public void startLocalGame(Setting setting) {
        generateBlockRandomizer(GameController.NORMAL_MODE);

        gamePlayer1.setPlayerKeys(setting.getRotateKey(), setting.getMoveDownKey(), setting.getMoveLeftKey(),
                setting.getMoveRightKey(), setting.getStackKey());
        gamePlayer2.setPlayerKeys(setting.getRotate2Key(), setting.getMoveDown2Key(), setting.getMoveLeft2Key(),
                setting.getMoveRight2Key(), setting.getStack2Key());

        gamePlayer1.startGame(GameController.NORMAL_MODE, GameController.GENERAL_GAME_MODE, randomBlockList);
        gamePlayer2.startGame(GameController.NORMAL_MODE, GameController.GENERAL_GAME_MODE, randomBlockList);
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
}
