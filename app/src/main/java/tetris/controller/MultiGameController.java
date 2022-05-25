package tetris.controller;

import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.*;
import tetris.model.*;

public class MultiGameController extends SingleGameController {

    GameController gamePlayer1;
    GameController gamePlayer2;
    GameController gameRobot;
    RobotController robotController;

    JLabel multiGameTimeLabel;

    public MultiGameController(PlayerController playerController, ViewController viewController) {
        super(playerController, viewController);
        multiGameTimeLabel = gameView.getMultiGameTimeLabel();
        JTextPane gamepane1 = gameView.getPlayerOneGameBoardPane();
        JTextPane nextBlockPane1 = gameView.getPlayerOneNextBlockPane();
        JTextPane attackLinePane1 = gameView.getPlayerOneAttackLinePane();
        JLabel scoreLabel1 = gameView.getPlayerOneScoreLabel();
        JTextPane gamepane2 = gameView.getPlayerTwoGameBoardPane();
        JTextPane nextBlockPane2 = gameView.getPlayerTwoNextBlockPane();
        JTextPane attackLinePane2 = gameView.getPlayerTwoAttackLinePane();
        JLabel scoreLabel2 = gameView.getPlayerTwoScoreLabel();
        JTextArea gameOverTextArea = gameView.getGameOverTextArea();
        gamePlayer1 = new GameController(gamepane1, nextBlockPane1, attackLinePane1, scoreLabel1, gamepane2) {
            @Override
            public void doAfterGameOver() {
                gamePlayer1.endGame();
                gamePlayer2.endGame();
                gameRobot.endGame();
                gameTimer.stop();
                gameView.setPlayerTwoWin();
                gameView.getVictoryLabel().requestFocus();
            }

            @Override
            public void doBeforeTakeOutNextBlock() {
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
            public void doAfterGameOver() {
                gamePlayer1.endGame();
                gamePlayer2.endGame();
                gameRobot.endGame();
                gameTimer.stop();
                gameView.setPlayerOneWin();
                gameView.getVictoryLabel().requestFocus();
            }

            @Override
            public void doBeforeTakeOutNextBlock() {
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

        gameRobot = new GameController(gamepane2, nextBlockPane2, attackLinePane2, scoreLabel2, gamepane2) {
            @Override
            public void doWhenGameStart() {
                robotController.findMove(currentBlock);
                robotController.moveBlock();
            }

            @Override
            public void doAfterGameOver() {
                gamePlayer1.endGame();
                gamePlayer2.endGame();
                gameRobot.endGame();
                gameTimer.stop();
                gameView.setPlayerTwoWin();
                gameView.getVictoryLabel().requestFocus();
            }

            @Override
            public void doBeforeTakeOutNextBlock() {
                if (attackLines > 0) {
                    underAttack();
                }
                if (blockDeque.isEmpty()) {
                    generateBlockRandomizer(GameController.NORMAL_MODE);
                    blockDeque.addAll(randomBlockList);
                    opponentBlockDeque.addAll(randomBlockList);
                }
                robotController.findMove(nextBlock);
            }

            @Override
            public void doAfterTakeOutNextBlock() {

                robotController.moveBlock();
            }
        };

        robotController = new RobotController(this.gameRobot);

        gameView.getVictoryLabel().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                gameView.resetMultiGameDisplayPane();
                gameView.resetGameView();
                viewController.transitView(viewController.contentPane, mainView, gameView);
            }
        });
    }

    public void startLocalGame(Setting setting) {
        gamePlayer1.setOpponentPlayer(gamePlayer2);
        gamePlayer2.setOpponentPlayer(gamePlayer1);
        generateBlockRandomizer(GameController.NORMAL_MODE);
        gamePlayer1.setPlayerKeys(setting.getRotateKey(), setting.getMoveDownKey(), setting.getMoveLeftKey(),
                setting.getMoveRightKey(), setting.getStackKey());
        gamePlayer2.setPlayerKeys(setting.getRotate2Key(), setting.getMoveDown2Key(), setting.getMoveLeft2Key(),
                setting.getMoveRight2Key(), setting.getStack2Key());

        int currentResoultion = gameView.getWidth() * gameView.getHeight();

        gamePlayer1.startGame(GameController.NORMAL_MODE, gameMode, randomBlockList, currentResoultion);
        gamePlayer2.startGame(GameController.NORMAL_MODE, gameMode, randomBlockList, currentResoultion);
        gameTime = 0;
        showTime(multiGameTimeLabel);
        showMode();
        startTimer(gameView.getMultiGameDisplayTimeLabel(), multiGameTimeLabel);
    }

    public void startRobotGame(Setting setting) {

        gamePlayer1.setOpponentPlayer(gameRobot);
        gameRobot.setOpponentPlayer(gamePlayer1);
        generateBlockRandomizer(GameController.NORMAL_MODE);

        gamePlayer1.setPlayerKeys(setting.getRotateKey(), setting.getMoveDownKey(), setting.getMoveLeftKey(),
                setting.getMoveRightKey(), setting.getStackKey());

        int currentResoultion = gameView.getWidth() * gameView.getHeight();
        gamePlayer1.startGame(GameController.NORMAL_MODE, gameMode, randomBlockList, currentResoultion);
        gameRobot.startGame(GameController.NORMAL_MODE, gameMode, randomBlockList, currentResoultion);
        gameTime = 0;
        showTime(multiGameTimeLabel);
        showMode();
        startTimer(gameView.getMultiGameDisplayTimeLabel(), multiGameTimeLabel);
    }

}