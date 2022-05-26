package tetris.controller;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static java.awt.event.KeyEvent.*;

import javax.swing.*;
import java.awt.AWTException;
import java.util.Deque;
import tetris.TestAllView;
import tetris.TestRobot;

import tetris.model.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameControllerTest_ItemMode {

    JFrame frame;
    ViewController viewController;
    TestAllView testAllView;
    MultiGameController gameController = new MultiGameController(new PlayerController(), viewController);
    SettingController settingController = new SettingController();
    TestRobot testRobot;

    @BeforeEach
    public void setUp() {
        frame = new JFrame();
        testAllView = new TestAllView();
        assertThat(frame).isInstanceOf(JFrame.class);
        try {
            testRobot = new TestRobot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        frame.setBounds(0, 0, 1200, 600);
        frame.add(testAllView.getGameView().getSingleGameDisplayPane());
        frame.setVisible(true);
    }

    @Test
    @Order(1)
    public void testSingleGameItemMode() {
        gameController.setItemFreqency(0);
        gameController.setGameMode(GameController.ITEM_GAME_MODE);
        gameController.startSingleGame(settingController.getSetting());
        assertThat(testAllView.getGameView().getSingleGameModeLabel().getText()).isEqualTo("아이템모드");
        testRobot.pressAndReleaseKeys(
                VK_SPACE, VK_SPACE, VK_SPACE, VK_SPACE, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN,
                VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN,
                VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN);
    }

    @AfterEach
    public void tearDown() {
        testAllView.removeAllEventListeners();
        testAllView.getGameView().resetGameView();
        frame.dispose();
    }
}