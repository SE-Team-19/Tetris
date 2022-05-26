package tetris.controller;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static java.awt.event.KeyEvent.*;

import javax.swing.*;
import java.awt.*;
import java.awt.AWTException;
import java.util.Deque;
import tetris.TestAllView;
import tetris.TestRobot;

import tetris.model.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MultiGameControllerTest {
    ViewController frame;
    TestAllView testAllView;
    TestRobot testRobot;
    MultiGameController gameController = new MultiGameController(new PlayerController(), frame);
    Deque<Integer> queue;

    @BeforeAll
    public static void setUpOnce() {
        // FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setUp() {
        frame = new ViewController();
        testAllView = new TestAllView();
        assertThat(frame).isInstanceOf(JFrame.class);
        try {
            testRobot = new TestRobot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void testMultiGameControllerRobotMode() {
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE, VK_RIGHT, VK_SPACE, VK_SPACE, VK_SPACE });
        assertThat(testAllView.getGameView().getMultiGameModeLabel().getText()).isEqualTo("일반 모드");
        testRobot.delay(60000);
        testRobot.pressAndReleaseKeys(new int[] { VK_ESCAPE, VK_DOWN, VK_DOWN, VK_SPACE });
    }

    @Test
    @Order(1)
    public void testMultiGameControllerItemMode() {
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE, VK_RIGHT, VK_SPACE, VK_RIGHT, VK_SPACE, VK_SPACE });
        assertThat(testAllView.getGameView().getMultiGameModeLabel().getText()).isEqualTo("아이템 모드");
        testRobot.delay(60000);
        testRobot.pressAndReleaseKeys(new int[] { VK_ESCAPE, VK_DOWN, VK_DOWN, VK_SPACE });
    }

    @AfterEach
    public void tearDown() {
        testAllView.removeAllEventListeners();
        frame.dispose();
    }

}