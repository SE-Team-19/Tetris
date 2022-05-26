package tetris.controller;

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
        testRobot.pressAndReleaseKeys(new int[] { VK_DOWN, VK_DOWN, VK_DOWN, VK_DOWN, VK_UP, VK_UP, VK_UP, VK_UP });
        testRobot.Click(frame.getFocusOwner());
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getSingleGameBtn());
        testRobot.pressAndReleaseKeys(new int[] { VK_DOWN, VK_SPACE });
    }

    @Test
    @Order(2)
    public void testMainToGameEvent() {
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE, VK_DOWN, VK_SPACE, VK_SPACE });
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getSingleGameBtn());
        testRobot.pressAndReleaseKeys(VK_RIGHT);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getMulitiGameBtn());
        testRobot.pressAndReleaseKeys(VK_DOWN);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getGameReturnBtn());
        testRobot.pressAndReleaseKeys(new int[] { VK_UP, VK_SPACE });
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getGeneralModeBtn());
        testRobot.pressAndReleaseKeys(VK_RIGHT);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getItemModeBtn());
        testRobot.pressAndReleaseKeys(VK_RIGHT);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getTimeAttackBtn());
        testRobot.pressAndReleaseKeys(VK_DOWN);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getModeReturnBtn());
        testRobot.pressAndReleaseKeys(new int[] { VK_UP, VK_SPACE });
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getEasyBtn());
        testRobot.pressAndReleaseKeys(VK_RIGHT);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getNormalBtn());
        testRobot.pressAndReleaseKeys(VK_RIGHT);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getHardBtn());
        testRobot.pressAndReleaseKeys(VK_DOWN);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getDiffReturnBtn());
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE, VK_DOWN, VK_SPACE, VK_LEFT, VK_SPACE });
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getGeneralModeBtn());
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE });
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getRobotGameBtn());
        testRobot.pressAndReleaseKeys(VK_RIGHT);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getLocalGameBtn());
        testRobot.pressAndReleaseKeys(VK_RIGHT);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getOnlineGameBtn());
        testRobot.pressAndReleaseKeys(VK_DOWN);
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getGameView().getMultiGameReturnBtn());
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE });
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
        testAllView.getGameView().resetGameView();
        frame.dispose();
    }

}