package tetris.controller;


import java.awt.Container;

import javax.swing.*;
import tetris.model.*;
import tetris.view.*;

public class BattleModeController {

    private Container contentPane;
    private Setting setting;
    private PlayerController playerController;
    private BattleModeView battleModeView = BattleModeView.getInstance();
    private ScoreView scoreView = ScoreView.getInstance();

    private KeySetting keySetting;


    /*
    private static GameController gameInstance1 = new GameController(
        new Setting(0, false, 65, 68, 83, 87, 82),
        new PlayerController(), new Container() );

    private static GameController gameInstance2 = new GameController(
        new Setting(0, false, 37, 39, 38, 40, 32),
        new PlayerController(), new Container() );

     */

    public BattleModeController(PlayerController playerController,  Container contentPane ) {
        this.playerController = playerController;
        this.contentPane = contentPane;
        initBattleModeController();
        /*gamecontroller = new GameController(new Setting(0, false, 65, 68, 83, 87, 82),
            new PlayerController(), new Container()); */


    }

    private void initBattleModeController() {


    }

    // PlayerOne, PlayerTwo의 방향키를 초기에 알려주는 메시지 작성
    private void message() {
        String message = "PlayerOne Key is" + keySetting.getLeft1P();
        JOptionPane.showMessageDialog(battleModeView, message);
    }

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

    }

    private void transitView(Container pane, Container to, Container from) {
        pane.add(to);
        pane.remove(from);
        focus(to);
        contentPane.revalidate();
        contentPane.repaint();
    }

    private void focus(Container to) {
        if (to.equals(scoreView))
            scoreView.getReturnScoreToMainBtn().requestFocus();
        else if (to.equals(battleModeView.getGeneralModeBtn()))
            battleModeView.getGeneralModeBtn().requestFocus();
        }



}
