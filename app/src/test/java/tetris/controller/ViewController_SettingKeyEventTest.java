package tetris.controller;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static java.awt.event.KeyEvent.*;

import javax.swing.*;
import java.awt.AWTException;
import tetris.TestAllView;
import tetris.TestRobot;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ViewController_SettingKeyEventTest {

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
    public void testSettingKeys() {
        int keyInput[] = {VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_DOWN, VK_DOWN, VK_SPACE,
                VK_DOWN, VK_SPACE, VK_RIGHT, VK_RIGHT, VK_RIGHT, VK_RIGHT, VK_RIGHT, VK_RIGHT,
                VK_LEFT, VK_LEFT, VK_LEFT, VK_LEFT, VK_LEFT, VK_LEFT, VK_SPACE, VK_DOWN, VK_SPACE,
                VK_RIGHT, VK_RIGHT, VK_RIGHT, VK_LEFT, VK_LEFT, VK_LEFT, VK_SPACE, VK_DOWN,
                VK_SPACE, VK_SPACE, VK_DOWN};
        testRobot.pressAndReleaseKeys(keyInput);
        assertThat(frame.getFocusOwner())
                .isEqualTo(testAllView.getSettingView().getReturnSettingToMainBtn());
        testRobot.pressAndReleaseKeys(VK_SPACE);
    }

    @Test
    @Order(2)
    public void testDisplayChange() {
        int keyInput[] = {VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_SPACE,
                VK_DOWN, VK_SPACE, VK_SPACE, VK_DOWN, VK_SPACE};
        testRobot.pressAndReleaseKeys(keyInput);

        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf((int) frame.getBounds().getWidth()));
        sb.append("X");
        sb.append(String.valueOf((int) frame.getBounds().getHeight()));
        assertThat(sb.toString()).hasToString(
                (String) testAllView.getSettingView().getDisplayComboBox().getSelectedItem());
    }

    @Test
    @Order(3)
    public void testKeymapping() {
        int keyInput[] = {VK_DOWN, VK_SPACE, VK_DOWN, VK_DOWN, VK_SPACE, VK_RIGHT, VK_SPACE,
                VK_SPACE, VK_W, VK_RIGHT, VK_SPACE, VK_S, VK_RIGHT, VK_SPACE, VK_A, VK_RIGHT,
                VK_SPACE, VK_D, VK_D, VK_SPACE, VK_R, VK_D, VK_R};
        testRobot.pressAndReleaseKeys(keyInput);
        assertThat(testAllView.getSettingView().getUpKeyLabel().getText()).isEqualTo("W");
    }

    @Test
    @Order(4)
    public void testResetSettings() {
        int keyInput[] = {VK_S, VK_R, VK_W, VK_W, VK_R, VK_D, VK_R};
        testRobot.pressAndReleaseKeys(keyInput);
        assertThat(testAllView.getSettingView().getUpKeyLabel().getText()).isEqualTo("Up");
    }

    @AfterEach
    public void tearDown() {
        testAllView.removeAllEventListeners();
        frame.dispose();
    }
}
