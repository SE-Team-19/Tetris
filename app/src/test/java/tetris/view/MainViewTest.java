package tetris.view;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import javax.swing.*;
import java.util.*;

public class MainViewTest {

    MainView mainView;

    @BeforeEach
    void setUp() {
        mainView = MainView.getInstance();
    }

    @Test
    void testGetButtonList() {
        assertThat(mainView.getButtonList()).isInstanceOf(ArrayList.class);
    }

    @Test
    void testGetButtonPanel() {
        assertThat(mainView.getButtonPanel()).isInstanceOf(JPanel.class);
    }

    @Test
    void testGetExitButton() {
        assertThat(mainView.getExitBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetScoreBoardButton() {
        assertThat(mainView.getScoreBoardBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetSettingButton() {
        assertThat(mainView.getSettingBtn()).isInstanceOf(JButton.class);
    }

    @Test
    void testGetStartButton() {
        assertThat(mainView.getStartBtn()).isInstanceOf(JButton.class);
    }
}