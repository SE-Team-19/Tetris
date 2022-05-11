package tetris.view;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import javax.swing.*;

public class SettingViewTest {

    SettingView settingViewTest;

    @BeforeEach
    void setUp() {
        settingViewTest = SettingView.getInstance();
    }

    @Test
    void testGetDisplayModeComboBox() {
        assertThat(settingViewTest.getDisplayModeComboBox()).isInstanceOf(JComboBox.class);
    }

    @Test
    void testGetDownKeyLabel() {
        assertThat(settingViewTest.getDownKeyLabel()).isInstanceOf(JLabel.class);
    }

    @Test
    void testGetEnterKeyMenuBtn() {
        assertThat(settingViewTest.getEnterKeyMenuBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetExitKeyMenuBtn() {
        assertThat(settingViewTest.getExitKeyMenuBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetEnterResetMenuBtn() {
        assertThat(settingViewTest.getEnterResetMenuBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetExitResetMenuBtn() {
        assertThat(settingViewTest.getExitResetMenuBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetResetScoreBtn() {
        assertThat(settingViewTest.getResetScoreBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetResetSettingBtn() {
        assertThat(settingViewTest.getResetSettingBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetIsColorBlindBtn() {
        assertThat(settingViewTest.getIsColorBlindBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetIsColorBlindLabel() {
        assertThat(settingViewTest.getIsColorBlindLabel()).isInstanceOf(JLabel.class);
    }

    @Test
    void testGetLeftKeyLabel() {
        assertThat(settingViewTest.getLeftKeyLabel()).isInstanceOf(JLabel.class);
    }

    @Test
    void testGetExitBtn() {
        assertThat(settingViewTest.getExitBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetRightKeyLabel() {
        assertThat(settingViewTest.getRightKeyLabel()).isInstanceOf(JLabel.class);
    }

    @Test
    void testGetEnterDisplayMenuBtn() {
        assertThat(settingViewTest.getEnterDisplayMenuBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetDownKeyBtn() {
        assertThat(settingViewTest.getDownKeyBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetLeftKeyBtn() {
        assertThat(settingViewTest.getLeftKeyBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetRightKeyBtn() {
        assertThat(settingViewTest.getRightKeyBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetStackKeyBtn() {
        assertThat(settingViewTest.getStackKeyBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetUpKeyBtn() {
        assertThat(settingViewTest.getUpKeyBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetStackKeyLabel() {
        assertThat(settingViewTest.getStackKeyLabel()).isInstanceOf(JLabel.class);
    }

    @Test
    void testGetUpKeyLabel() {
        assertThat(settingViewTest.getUpKeyLabel()).isInstanceOf(JLabel.class);
    }
}
