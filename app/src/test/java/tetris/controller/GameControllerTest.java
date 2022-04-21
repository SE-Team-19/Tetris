package tetris.controller;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static java.awt.event.KeyEvent.*;

import javax.swing.*;
import java.awt.AWTException;
import tetris.TestAllView;
import tetris.TestRobot;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameControllerTest {

    ViewController frame;
    TestAllView testAllView;
    TestRobot testRobot;

    @BeforeAll
    public static void setUpOnce() {
        // FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setUp() {
        frame = new ViewController();
        testAllView = new TestAllView();
        try {
            testRobot = new TestRobot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        assertThat(frame).isInstanceOf(JFrame.class);
    }

    @Test
    @Order(1)
    public void testGameController() {
        int keyInput[] = { VK_SPACE, VK_DOWN, VK_LEFT, VK_RIGHT, VK_SPACE, VK_UP, VK_UP };
        testRobot.pressAndReleaseKeys(keyInput);
        testRobot.pressAndReleaseKeys(VK_SPACE);
    }

    @Test
    @Order(2)
    public void testGameController2() {
        int keyInput[] = { VK_SPACE, VK_DOWN, VK_LEFT, VK_RIGHT, VK_SPACE, VK_UP, VK_UP };
        testRobot.pressAndReleaseKeys(keyInput);
        testRobot.pressAndReleaseKeys(VK_SPACE);
        testRobot.delay(10000);
}

@AfterEach
    public void tearDown() {
        testAllView.removeAllEventListeners();
        testAllView.getGameView().getGamePane().setText("");
        frame.dispose();
    }
}
    