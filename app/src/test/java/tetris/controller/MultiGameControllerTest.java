package tetris.controller;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static java.awt.event.KeyEvent.*;

import javax.swing.*;
import java.awt.AWTException;
import java.util.Deque;
import tetris.TestAllView;
import tetris.TestRobot;

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
    @Order(2)
    public void testMultiGameControllerRobotMode() {
        testRobot.pressAndReleaseKeys(new int[] { VK_DOWN, VK_SPACE, VK_UP, VK_SPACE, VK_DOWN, VK_SPACE, VK_SPACE,
                VK_RIGHT, VK_SPACE, VK_SPACE, VK_SPACE });
        assertThat(testAllView.getGameView().getMultiGameModeLabel().getText()).isEqualTo("일반 모드");
        testRobot.delay(60000);
        testRobot.pressAndReleaseKeys(VK_DOWN, VK_DOWN);
        testRobot.pressAndReleaseKeys(new int[] { VK_ESCAPE, VK_DOWN, VK_DOWN, VK_SPACE });
        testRobot.delay(1000);
    }

    @Test
    @Order(3)
    public void testMultiGameControllerItemMode() {
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE, VK_RIGHT, VK_SPACE, VK_RIGHT, VK_SPACE, VK_SPACE });
        assertThat(testAllView.getGameView().getMultiGameModeLabel().getText()).isEqualTo("아이템모드");
        testRobot.delay(60000);
        testRobot.pressAndReleaseKeys(VK_DOWN, VK_DOWN);
        testRobot.pressAndReleaseKeys(new int[] { VK_ESCAPE, VK_DOWN, VK_DOWN, VK_SPACE });
        testRobot.delay(1000);
    }

    @Test
    @Order(1)
    public void testMultiGameControllerPlayerTwoWin() {
        testRobot.pressAndReleaseKeys(
                new int[] { VK_SPACE, VK_RIGHT, VK_SPACE, VK_RIGHT, VK_SPACE, VK_RIGHT, VK_SPACE });
        testRobot.pressAndReleaseKeys(
                VK_SPACE, VK_SPACE, VK_SPACE, VK_SPACE, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN,
                VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN,
                VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN);
        testRobot.delay(5000);
        assertThat(testAllView.getGameView().getVictoryLabel().getText()).isEqualTo("WIN!");
        testRobot.pressAndReleaseKeys(VK_SPACE);
        testRobot.delay(1000);
    }

    @Test
    @Order(4)
    public void testMultiGameControllerStopMenu() {
        testRobot.pressAndReleaseKeys(
                new int[] { VK_SPACE, VK_RIGHT, VK_SPACE, VK_SPACE, VK_RIGHT, VK_SPACE });
        testRobot.pressAndReleaseKeys(
                VK_ESCAPE, VK_SPACE, VK_A, VK_W, VK_D, VK_S, VK_R, VK_ESCAPE, VK_DOWN, VK_DOWN, VK_DOWN, VK_DOWN,
                VK_DOWN, VK_SPACE, VK_ESCAPE, VK_DOWN, VK_DOWN, VK_SPACE);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getMainView().getStartBtn());
        testRobot.delay(1000);
    }

    @Test
    @Order(5)
    public void testMultiGameControllerTimeAttckMode() {
        testRobot.pressAndReleaseKeys(
                new int[] { VK_SPACE, VK_RIGHT, VK_SPACE, VK_RIGHT, VK_RIGHT, VK_SPACE, VK_RIGHT, VK_SPACE });
        assertThat(testAllView.getGameView().getMultiGameModeLabel().getText()).isEqualTo("시간제한모드");
        testRobot.delay(60000);
        testRobot.delay(40000);
        testRobot.delay(5000);
        assertThat(testAllView.getGameView().getVictoryLabel().getText()).isEqualTo("Draw");
        testRobot.pressAndReleaseKeys(VK_SPACE);
    }

    @Test
    @Order(6)
    public void testBlockdequeuSingleGame() {
        testRobot.pressAndReleaseKeys(VK_SPACE, VK_RIGHT, VK_SPACE, VK_SPACE, VK_RIGHT, VK_SPACE);
        assertThat(testAllView.getGameView().getMultiGameModeLabel().getText()).isEqualTo("일반 모드");
        testRobot.delay(5000);
        gameController.gamePlayer1.blockDeque.clear();
        gameController.gamePlayer2.blockDeque.clear();
        testRobot.pressAndReleaseKeys(VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN);
        testRobot.pressAndReleaseKeys(VK_R, VK_S, VK_R, VK_S, VK_R, VK_S, VK_R, VK_S);
        testRobot.delay(1000);
        testRobot.pressAndReleaseKeys(VK_ESCAPE, VK_DOWN, VK_DOWN, VK_SPACE);
        testRobot.delay(1000);
    }

    @AfterEach
    public void tearDown() {
        testAllView.removeAllEventListeners();
        testAllView.getGameView().resetGameView();
        frame.dispose();
    }

}