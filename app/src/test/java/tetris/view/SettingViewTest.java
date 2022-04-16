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
    void testGetDisplayComboBox() {
        assertThat(settingViewTest.getDisplayComboBox()).isInstanceOf(JComboBox.class);
    }

    @Test
    void testGetDisplayList() {
        assertThat(settingViewTest.getDisplayList()).isInstanceOf(String[].class);
    }

    @Test
    void testGetDownKeyLabel() {
        assertThat(settingViewTest.getDownKeyLabel()).isInstanceOf(JLabel.class);
    }

    @Test
    void testGetInitKeyBtn() {
        assertThat(settingViewTest.getInitKeyBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetInitKeyGridReturnBtn() {
        assertThat(settingViewTest.getInitKeyGridReturnBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetInitMenuBtn() {
        assertThat(settingViewTest.getInitMenuBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetInitReturnBtn() {
        assertThat(settingViewTest.getInitReturnBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetInitScoreBtn() {
        assertThat(settingViewTest.getInitScoreBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetInitSettingBtn() {
        assertThat(settingViewTest.getInitSettingBtn()).isInstanceOf(JButton.class);
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
    void testGetReturnSettingToMainBtn() {
        assertThat(settingViewTest.getReturnSettingToMainBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetRightKeyLabel() {
        assertThat(settingViewTest.getRightKeyLabel()).isInstanceOf(JLabel.class);
    }

    @Test
    void testGetSetDisplayBtn() {
        assertThat(settingViewTest.getSetDisplayBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetSetDownKeyBtn() {
        assertThat(settingViewTest.getSetDownKeyBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetSetLeftKeyBtn() {
        assertThat(settingViewTest.getSetLeftKeyBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetSetRightKeyBtn() {
        assertThat(settingViewTest.getSetRightKeyBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetSetStaticKeyBtn() {
        assertThat(settingViewTest.getSetStaticKeyBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetSetUpKeyBtn() {
        assertThat(settingViewTest.getSetUpKeyBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetStaticKeyLabel() {
        assertThat(settingViewTest.getStaticKeyLabel()).isInstanceOf(JLabel.class);
    }

    @Test
    void testGetUpKeyLabel() {
        assertThat(settingViewTest.getUpKeyLabel()).isInstanceOf(JLabel.class);
    }
}
