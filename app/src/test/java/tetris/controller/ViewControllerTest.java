package tetris.controller;

import org.junit.Ignore;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static java.awt.event.KeyEvent.*;

import javax.swing.*;
import java.awt.AWTException;

import tetris.TestRobot;
import tetris.TestAllView;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ViewControllerTest {
    ViewController frame;
    int[] keyList;
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
        assertThat(frame).isInstanceOf(JFrame.class);
        try {
            testRobot = new TestRobot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(1)
    public void testMainKeyEvent() {
        int keyInput[] = { VK_DOWN, VK_DOWN, VK_DOWN, VK_DOWN, VK_UP, VK_UP, VK_UP,
                VK_UP };
        testRobot.pressAndReleaseKeys(keyInput);
        testRobot.Click(frame.getFocusOwner());
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView());
    }

    @Ignore
    @Order(2)
    public void testMainToGameEvent() {
        int keyInput[] = { VK_SPACE };
        testRobot.pressAndReleaseKeys(keyInput);
        testRobot.Click(frame.getFocusOwner());
        testRobot.Click(frame.getFocusOwner());
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getMainView().getStartBtn());
    }

    @Test
    @Order(3)
    public void testMainToSettingEvent() {
        int keyInput[] = { VK_DOWN, VK_SPACE, VK_SPACE };
        testRobot.pressAndReleaseKeys(keyInput);
        testRobot.pressAndReleaseKeys(VK_DOWN);
        testRobot.Click(frame.getFocusOwner());
        testRobot.Click(frame.getFocusOwner());
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getMainView().getStartBtn());
    }

    @Test
    @Order(4)
    public void testMainToScoreEvent() {
        int keyInput[] = { VK_DOWN, VK_DOWN, VK_SPACE, VK_SPACE };
        testRobot.pressAndReleaseKeys(keyInput);
        testRobot.pressAndReleaseKeys(VK_DOWN, VK_DOWN);
        testRobot.Click(frame.getFocusOwner());
        testRobot.Click(frame.getFocusOwner());
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getMainView().getStartBtn());
    }

    @AfterEach
    public void tearDown() {
        testAllView.removeAllEventListeners();
        frame.dispose();
    }

}
