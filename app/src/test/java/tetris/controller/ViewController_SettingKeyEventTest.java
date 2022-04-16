package tetris.controller;

import org.junit.jupiter.api.*;
import org.assertj.swing.edt.*;
import org.assertj.swing.fixture.FrameFixture;
import static org.assertj.core.api.Assertions.*;
import static java.awt.event.KeyEvent.*;

import javax.swing.*;

public class ViewController_SettingKeyEventTest {

    private FrameFixture window;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setUp() {
        ViewController frame = GuiActionRunner.execute(() -> new ViewController());
        window = new FrameFixture(frame);
        assertThat(frame).isInstanceOf(JFrame.class);
    }

    @Test
    public void testSettingKeyEvent() {
        window.button("settingBtn").pressAndReleaseKeys(VK_ENTER);
        window.button("setDisplayBtn").pressAndReleaseKeys(VK_SPACE);
        window.comboBox("displayComboBox").pressAndReleaseKeys(VK_UP, VK_UP, VK_UP, VK_DOWN, VK_DOWN, VK_DOWN,
                VK_SPACE);
        window.button("initKeyBtn").pressAndReleaseKeys(VK_SPACE);
        window.toggleButton("setUpKeyBtn").pressAndReleaseKeys(VK_LEFT);
        window.toggleButton("setDownKeyBtn").pressAndReleaseKeys(VK_RIGHT);
        window.button("initKeyGridReturnBtn").pressAndReleaseKeys(VK_SPACE);
        window.button("initKeyBtn").pressAndReleaseKeys(VK_DOWN);
        window.button("initMenuBtn").pressAndReleaseKeys(VK_SPACE);
        window.button("initReturnBtn").pressAndReleaseKeys(VK_SPACE);
        window.toggleButton("isColorBlindBtn").pressAndReleaseKeys(VK_SPACE);
        assertThat(window.toggleButton("isColorBlindBtn").isEnabled()).isTrue();
        window.button("returnSettingToMainBtn").pressAndReleaseKeys(VK_SPACE);
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }
}
