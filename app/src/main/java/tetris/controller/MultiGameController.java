package tetris.controller;

import java.awt.Dimension;
import javax.swing.*;
import tetris.model.*;

public class MultiGameController extends SingleGameController {

    GameController gamePlayer1;
    GameController gamePlayer2;

    public MultiGameController(PlayerController playerController) {
        super(playerController);
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

        // 게임 오른쪽 부분 배치 고정; 블럭이 쌓일 때 흔들림 방지
        if (ViewController.screenWidthNum == 0) {
            nextBlockPane1.setPreferredSize(new Dimension(85, 128));
            attackLinePane1.setPreferredSize(new Dimension(57, 84));
            nextBlockPane2.setPreferredSize(new Dimension(85, 128));
            attackLinePane2.setPreferredSize(new Dimension(85, 84));
            System.out.println("GameView is " + ViewController.screenWidthNum);
        } else if (ViewController.screenWidthNum == 1) {
            nextBlockPane1.setPreferredSize(new Dimension(87, 175));
            attackLinePane1.setPreferredSize(new Dimension(87, 116));
            nextBlockPane2.setPreferredSize(new Dimension(87, 175));
            attackLinePane2.setPreferredSize(new Dimension(87, 116));
            System.out.println("GameView is " + ViewController.screenWidthNum);
        } else {
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
        gameTime = 0;
        showTime();
        startStopWatch();
    }
}
