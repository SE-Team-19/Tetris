package tetris.controller;

import org.junit.jupiter.api.*;
import org.assertj.swing.edt.*;
import org.assertj.swing.fixture.*;

import static org.assertj.core.api.Assertions.*;
import static java.awt.event.KeyEvent.*;

import javax.swing.*;

public class ViewControllerTest {

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
    public void testMainKeyEvent() {
        assertThat(window.button("startBtn").getClass()).isEqualTo(JButtonFixture.class);
        window.button("startBtn").pressAndReleaseKeys(VK_DOWN);
        window.button("exitBtn").pressAndReleaseKeys(VK_UP);
    }

    @Test
    public void testMainToGameEvent() {
        window.button("startBtn").pressAndReleaseKeys(VK_ENTER);
        assertThat(window.button("returnGameToMainBtn").getClass()).isEqualTo(JButtonFixture.class);
        window.button("returnGameToMainBtn").pressAndReleaseKeys(VK_SPACE);
    }

    @Test
    public void testMainToSettingEvent() {
        window.button("settingBtn").pressAndReleaseKeys(VK_ENTER);
        assertThat(window.button("returnSettingToMainBtn").getClass()).isEqualTo(JButtonFixture.class);
        window.button("returnSettingToMainBtn").pressAndReleaseKeys(VK_SPACE);
    }

    @Test
    public void testMainToScoreEvent() {
        window.button("scoreBoardBtn").pressAndReleaseKeys(VK_ENTER);
        assertThat(window.button("returnScoreToMainBtn").getClass()).isEqualTo(JButtonFixture.class);
        window.button("returnScoreToMainBtn").pressAndReleaseKeys(VK_ENTER);
    }

    @AfterEach
    public void tearDown() {
        window.cleanUp();
    }

}
