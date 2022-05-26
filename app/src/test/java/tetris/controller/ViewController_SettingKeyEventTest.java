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

        int keyInput[] = { VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_DOWN, VK_DOWN, VK_SPACE,
                VK_DOWN, VK_SPACE, VK_RIGHT, VK_UP, VK_DOWN, VK_RIGHT, VK_UP, VK_DOWN, VK_RIGHT, VK_UP, VK_DOWN,
                VK_RIGHT, VK_UP, VK_DOWN, VK_RIGHT, VK_UP, VK_DOWN, VK_RIGHT,
                VK_LEFT, VK_LEFT, VK_LEFT, VK_LEFT, VK_LEFT, VK_LEFT, VK_SPACE, VK_DOWN, VK_SPACE,

                VK_RIGHT, VK_RIGHT, VK_RIGHT, VK_LEFT, VK_LEFT, VK_LEFT, VK_SPACE, VK_DOWN,
                VK_SPACE, VK_SPACE, VK_DOWN };
        testRobot.pressAndReleaseKeys(keyInput);
        assertThat(frame.getFocusOwner())
                .isEqualTo(testAllView.getSettingView().getReturnSettingToMainBtn());

        testRobot.pressAndReleaseKeys(VK_SPACE);
    }

    @Test
    @Order(2)
    public void testDisplayChange() {
        int keyInput[] = { VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_SPACE,
                VK_DOWN, VK_SPACE, VK_SPACE, VK_DOWN, VK_SPACE };
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
        testRobot.pressAndReleaseKeys(new int[] { VK_DOWN, VK_SPACE, VK_DOWN, VK_DOWN, VK_SPACE });
        testRobot.pressAndReleaseKeys(new int[] { VK_LEFT, VK_LEFT, VK_LEFT, VK_LEFT, VK_LEFT });
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE, VK_W });
        assertThat(testAllView.getSettingView().getUpKeyLabel().getText()).isEqualTo(ViewController.OVERLAP_KEY_MSG);
        testRobot.pressAndReleaseKeys(new int[] { VK_Z, VK_RIGHT, VK_SPACE, VK_X });
        assertThat(testAllView.getSettingView().getUpKeyLabel().getText()).isEqualTo("Z");
        assertThat(testAllView.getSettingView().getDownKeyLabel().getText()).isEqualTo("X");
        testRobot.pressAndReleaseKeys(new int[] { VK_RIGHT, VK_SPACE, VK_C });
        assertThat(testAllView.getSettingView().getLeftKeyLabel().getText()).isEqualTo("C");
        testRobot.pressAndReleaseKeys(new int[] { VK_RIGHT, VK_SPACE, VK_V });
        assertThat(testAllView.getSettingView().getRightKeyLabel().getText()).isEqualTo("V");
        testRobot.pressAndReleaseKeys(new int[] { VK_V, VK_SPACE, VK_1 });
        assertThat(testAllView.getSettingView().getStackKeyLabel().getText()).isEqualTo("1");
        testRobot.pressAndReleaseKeys(new int[] { VK_V, VK_V, VK_1, VK_X });
        assertThat(testAllView.getSettingView().getUp2KeyLabel().getText()).isEqualTo(ViewController.OVERLAP_KEY_MSG);
        testRobot.pressAndReleaseKeys(new int[] { VK_UP, VK_V, VK_1, VK_DOWN });
        assertThat(testAllView.getSettingView().getUp2KeyLabel().getText()).isEqualTo("Up");
        assertThat(testAllView.getSettingView().getDown2KeyLabel().getText()).isEqualTo("Down");
        testRobot.pressAndReleaseKeys(new int[] { VK_V, VK_1, VK_LEFT });
        assertThat(testAllView.getSettingView().getLeft2KeyLabel().getText()).isEqualTo("Left");
        testRobot.pressAndReleaseKeys(new int[] { VK_V, VK_1, VK_RIGHT });
        assertThat(testAllView.getSettingView().getRight2KeyLabel().getText()).isEqualTo("Right");
        testRobot.pressAndReleaseKeys(new int[] { VK_V, VK_1, VK_SPACE });
        assertThat(testAllView.getSettingView().getStack2KeyLabel().getText()).isEqualTo("Space");
        testRobot.pressAndReleaseKeys(new int[] { VK_V, VK_1 });
        assertThat(frame.getFocusOwner())
                .isEqualTo(testAllView.getSettingView().getInitKeyBtn());
    }

    @Test
    @Order(4)
    public void testResetSettings() {
        int keyInput[] = { VK_X, VK_1, VK_Z, VK_Z, VK_1, VK_V, VK_1 };
        testRobot.pressAndReleaseKeys(keyInput);
        assertThat(testAllView.getSettingView().getUpKeyLabel().getText()).isEqualTo("Up");
    }

    @AfterEach
    public void tearDown() {
        testAllView.removeAllEventListeners();
        frame.dispose();
    }
}