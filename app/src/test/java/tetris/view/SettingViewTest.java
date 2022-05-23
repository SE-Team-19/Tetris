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
<<<<<<< HEAD
    void testGetDisplayComboBox() {
        assertThat(settingViewTest.getDisplayComboBox()).isInstanceOf(JComboBox.class);
=======
    void testGetDisplayModeComboBox() {
        assertThat(settingViewTest.getDisplayModeComboBox()).isInstanceOf(JComboBox.class);
>>>>>>> feat/BattleMode
    }

    @Test
    void testGetDownKeyLabel() {
        assertThat(settingViewTest.getDownKeyLabel()).isInstanceOf(JLabel.class);
    }

    @Test
<<<<<<< HEAD
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
=======
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
>>>>>>> feat/BattleMode
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
<<<<<<< HEAD
    void testGetReturnSettingToMainBtn() {
        assertThat(settingViewTest.getReturnSettingToMainBtn()).isInstanceOf(JButton.class);
=======
    void testGetExitBtn() {
        assertThat(settingViewTest.getExitBtn()).isInstanceOf(JButton.class);
>>>>>>> feat/BattleMode
    }

    @Test
    void testGetRightKeyLabel() {
        assertThat(settingViewTest.getRightKeyLabel()).isInstanceOf(JLabel.class);
    }

    @Test
<<<<<<< HEAD
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
    void testGetSetStackKeyBtn() {
        assertThat(settingViewTest.getSetStackKeyBtn()).isInstanceOf(JToggleButton.class);
    }

    @Test
    void testGetSetUpKeyBtn() {
        assertThat(settingViewTest.getSetUpKeyBtn()).isInstanceOf(JToggleButton.class);
=======
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
>>>>>>> feat/BattleMode
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
