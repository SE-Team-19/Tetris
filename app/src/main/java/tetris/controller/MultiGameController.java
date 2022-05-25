package tetris.controller;

import java.awt.Dimension;
import javax.swing.*;
import tetris.model.*;

public class MultiGameController extends SingleGameController {

    GameController gamePlayer1;
    GameController gamePlayer2;
    AIController aiController;

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
        JTextArea gameOverTextArea = gameView.getGameOverTextArea();
        aiController = new AIController(this);
        gamePlayer1 = new GameController(gamepane1, nextBlockPane1, attackLinePane1, scoreLabel1, gamepane2) {
            @Override
            public void doAfterGameOver() {
                gamePlayer1.stopGame();
                gamePlayer2.stopGame();
                gameView.remove(gameView.getMulitiGameDisplayPane());
                gameOverTextArea.setText("Player 2 WINS!\nPress Any Key To Exit");
                gameView.add(gameOverTextArea);
                gameOverTextArea.requestFocus();
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
                gamePlayer1.stopGame();
                gamePlayer2.stopGame();
                gameView.remove(gameView.getMulitiGameDisplayPane());
                gameOverTextArea.setText("Player 1 WINS!\nPress Any Key To Exit");
                gameView.add(gameOverTextArea);
                gameOverTextArea.requestFocus();
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
                // aiController.findMove();
            }

            @Override
            public void doAfterTakeOutNextBlock() {

                // aiController.moveBlock();
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
}
