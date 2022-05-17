package tetris.controller;

import static java.lang.System.currentTimeMillis;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import tetris.model.*;
import tetris.view.*;

public class BattleModeController {

    //private KeyListener playerKeyListener;
    private PlayerOneController playerOneController;
    private PlayerTwoController playerTwoController;
    private BattleModeView battleModeView = BattleModeView.getInstance();
    //private PlayerController playerController;
    //private Container contentPane;

    private Timer timer;  // 시간 제한 모드를 위한 총괄 Timer
    private Timer timeLimitTimer;
    private long startTime;
    private final long endTime = 60000;

    private boolean isTimeLimitMode = false;


    /*
    private static GameController gameInstance1 = new GameController(
        new Setting(0, false, 65, 68, 83, 87, 82),
        new PlayerController(), new Container() );

    private static GameController gameInstance2 = new GameController(
        new Setting(0, false, 37, 39, 38, 40, 32),
        new PlayerController(), new Container() );

     */

    public BattleModeController() {
        initBattleModeController();
    }

    private void initBattleModeController() {
        playerOneController = new PlayerOneController(
            new Setting(),
            new PlayerController(), new Container());
        playerTwoController = new PlayerTwoController(
            new Setting(),
            new PlayerController(), new Container());

        // timeLimitMode 관련 추후 수정 필요. 다른 부분과 연관짓기
        if (!isTimeLimitMode) {
            startTime = currentTimeMillis();
            startTimeLimitMode(1000);
        }

        startTimer(1000);

        /*
        playerKeyListener = new PlayerKeyListener();
        addKeyListener(playerKeyListener);
        */

        initialMessage();
    }

    // PlayerOne, PlayerTwo 의 방향키를 처음에 알려주는 메시지 작성
    private void initialMessage() {
        String message = "Initial Key: \n" +
            "PlayerOne Key: " + KeyEvent.VK_A + " " + KeyEvent.VK_D + " " + KeyEvent.VK_W + " " +
            KeyEvent.VK_S + " " + KeyEvent.VK_R +
            "\nPlayerTwo Key: " + KeyEvent.VK_LEFT + " " + KeyEvent.VK_RIGHT + " " + KeyEvent.VK_UP
            + " " +
            KeyEvent.VK_DOWN + " " + KeyEvent.VK_SHIFT;
        JOptionPane.showMessageDialog(battleModeView, message);
    }


    private void startTimer(int initInterval) {
        timer = new Timer(initInterval, e -> {
            if (playerOneController.getStartFlag() && playerTwoController.getStartFlag()) {
                timer.start();
            }
        });
    }

    private void startTimeLimitMode(int initInterval) {
        timeLimitTimer = new Timer(initInterval, e -> {
            if (isTimeLimitMode && (currentTimeMillis() - startTime) > endTime) {
                playerOneController.stopGameDelayTimer();
                playerTwoController.stopGameDelayTimer();
            }
        });
        timeLimitTimer.start();
    }




    /*
    private void initKeySetting() {
        //JRootPane rootPane = this.getRootPane();
        JPanel jPanel = new JPanel();
        InputMap inputMap = jPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionmap = jPanel.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(keySetting.getLeft1P()), "left1p");
        inputMap.put(KeyStroke.getKeyStroke(keySetting.getRight1P()), "right1p");
        inputMap.put(KeyStroke.getKeyStroke(keySetting.getDown1P()), "down1p");
        inputMap.put(KeyStroke.getKeyStroke(keySetting.getRotate1P()), "rotate1p");
        inputMap.put(KeyStroke.getKeyStroke(keySetting.getSpaceDown1P()), "stack1p");
        inputMap.put(KeyStroke.getKeyStroke(keySetting.getLeft2P()), "left2p");
        inputMap.put(KeyStroke.getKeyStroke(keySetting.getRight2P()), "right2p");
        inputMap.put(KeyStroke.getKeyStroke(keySetting.getDown2P()), "down2p");
        inputMap.put(KeyStroke.getKeyStroke(keySetting.getRotate2P()), "rotate2p");
        inputMap.put(KeyStroke.getKeyStroke(keySetting.getSpaceDown1P()), "stack2p");
        inputMap.put(KeyStroke.getKeyStroke(keySetting.getESC()), "ESC");
    } */

    /*
    // 우선 대략적인 구현만. 이 부분은 Controller 에서 할 수 없다.
    public class PlayerKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_A:
                    playerOneController.moveLeft();
                    playerOneController.drawGameBoard();
                    break;
                case KeyEvent.VK_D:
                    playerOneController.moveRight();
                    playerOneController.drawGameBoard();
                    break;
                case KeyEvent.VK_W:
                    playerOneController.moveRotate();
                    playerOneController.drawGameBoard();
                    break;
                case KeyEvent.VK_S:
                    playerOneController.moveDown();
                    playerOneController.drawGameBoard();
                    break;
                case KeyEvent.VK_R:
                    // PlayerOne stack 부분 추가를 어떻게?
                    break;

                case KeyEvent.VK_LEFT:
                    playerTwoController.moveLeft();
                    playerTwoController.drawGameBoard();
                    break;
                case KeyEvent.VK_RIGHT:
                    playerTwoController.moveRight();
                    playerTwoController.drawGameBoard();
                    break;
                case KeyEvent.VK_UP:
                    playerTwoController.moveRotate();
                    playerTwoController.drawGameBoard();
                    break;
                case KeyEvent.VK_DOWN:
                    playerTwoController.moveDown();
                    playerTwoController.drawGameBoard();
                    break;

                case KeyEvent.VK_SHIFT:
                    // PlayerTwo stack 부분 추가를 어떻게?
                    break;

                case KeyEvent.VK_ESCAPE:

            }
        }
        @Override
        public void keyReleased(KeyEvent e) {

        }
    } */
}
