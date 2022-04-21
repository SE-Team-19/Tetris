package tetris;

import javax.swing.*;
import javax.swing.AbstractButton;
import tetris.view.GameView;
import tetris.view.MainView;
import tetris.view.ScoreView;
import tetris.view.SettingView;
import java.awt.*;
import java.awt.event.*;

public class TestAllView {

    private MainView mainView;
    private GameView gameView;
    private ScoreView scoreView;
    private SettingView settingView;

    public TestAllView() {
        mainView = MainView.getInstance();
        gameView = GameView.getInstance();
        scoreView = ScoreView.getInstance();
        settingView = SettingView.getInstance();
    }

    public void removeAllEventListeners() {
        removeKeyListner(mainView, gameView, scoreView, settingView);
        removeActionListner(mainView, gameView, scoreView, settingView);
    }

    public void removeKeyListner(JPanel... viewList) {
        for (JPanel view : viewList) {
            for (Component viewComp : view.getComponents()) {
                if (viewComp instanceof JPanel) {
                    for (Component panelComp : ((JPanel) viewComp).getComponents()) {
                        for (KeyListener kl : panelComp.getKeyListeners())
                            panelComp.removeKeyListener(kl);
                    }
                }
                if (viewComp.getClass().equals(JLabel.class))
                    continue;
                for (KeyListener kl : viewComp.getKeyListeners())
                    viewComp.removeKeyListener(kl);
            }
        }
    }

    public void removeActionListner(JPanel... viewList) {
        for (JPanel view : viewList) {
            for (Component viewComp : view.getComponents()) {
                if (viewComp instanceof JPanel) {
                    for (Component panelComp : ((JPanel) viewComp).getComponents()) {
                        try {
                            for (ActionListener al : ((AbstractButton) panelComp)
                                    .getActionListeners()) {
                                ((AbstractButton) panelComp).removeActionListener(al);
                            }
                        } catch (Exception e) {
                            continue;
                        }

                    }
                }
                if (viewComp.getClass().equals(JLabel.class))
                    continue;
                try {
                    for (ActionListener al : ((AbstractButton) viewComp).getActionListeners()) {
                        ((AbstractButton) viewComp).removeActionListener(al);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }

    public MainView getMainView() {
        return this.mainView;
    }

    public GameView getGameView() {
        return this.gameView;
    }

    public ScoreView getScoreView() {
        return this.scoreView;
    }

    public SettingView getSettingView() {
        return this.settingView;
    }
}
