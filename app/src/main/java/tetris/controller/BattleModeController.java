package tetris.controller;

import static java.lang.System.currentTimeMillis;

import java.awt.Container;
import javax.swing.*;
import tetris.model.*;
import tetris.view.*;

public class BattleModeController {

    private PlayerOneController playerOneController;
    private PlayerTwoController playerTwoController;
    // private PlayerController playerController;
    // private Container contentPane;

    private Timer timer; // 시간 제한 모드를 위한 총괄 Timer
    private Timer timeLimitTimer;
    private long startTime;
    private final long endTime = 60000;

    private boolean isTimeLimitMode = false;

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

        // initialMessage();
    }

    // PlayerOne, PlayerTwo 의 방향키를 처음에 알려주는 메시지 작성
    /*
     * private void initialMessage() {
     * String message = "Initial Key: \n" +
     * "PlayerOne Key: " + KeyEvent.VK_A + " " + KeyEvent.VK_D + " " + KeyEvent.VK_W
     * + " " +
     * KeyEvent.VK_S + " " + KeyEvent.VK_R +
     * "\nPlayerTwo Key: " + KeyEvent.VK_LEFT + " " + KeyEvent.VK_RIGHT + " " +
     * KeyEvent.VK_UP
     * + " " +
     * KeyEvent.VK_DOWN + " " + KeyEvent.VK_SHIFT;
     * JOptionPane.showMessageDialog(battleModeView, message);
     * }
     */

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
}