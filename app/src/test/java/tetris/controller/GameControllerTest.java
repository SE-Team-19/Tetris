package tetris.controller;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static java.awt.event.KeyEvent.*;

import javax.swing.*;
import java.awt.*;
import java.awt.AWTException;

import tetris.TestAllView;
import tetris.TestRobot;

import tetris.model.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameControllerTest {

    GameController gameController = new GameController(
            new Setting(0, false, VK_LEFT, VK_RIGHT, VK_DOWN,
                    VK_UP, VK_SPACE),
            new PlayerController(), new Container());
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
    public void getIBlockMoreThan20Pct() {
        int IBlockCount = 0;
        int JBlockCount = 0;
        int LBlockCount = 0;
        int ZBlockCount = 0;
        int SBlockCount = 0;
        int TBlockCount = 0;
        int OBlockCount = 0;
        for (int i = 0; i < 3600000; i++) {
            Block randomBlock = gameController.getBlock(0);
            if (randomBlock instanceof IBlock)
                IBlockCount++;
            else if (randomBlock instanceof JBlock)
                JBlockCount++;
            else if (randomBlock instanceof LBlock)
                LBlockCount++;
            else if (randomBlock instanceof ZBlock)
                ZBlockCount++;
            else if (randomBlock instanceof SBlock)
                SBlockCount++;
            else if (randomBlock instanceof TBlock)
                TBlockCount++;
            else if (randomBlock instanceof OBlock)
                OBlockCount++;
        }

        assertThat(IBlockCount).isGreaterThan(570000).isLessThan(630000);
        assertThat(JBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(LBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(ZBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(SBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(TBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(OBlockCount).isGreaterThan(475000).isLessThan(525000);
    }

    @Test
    public void getIBlockLessThan20Pct() {

        int IBlockCount = 0;
        int JBlockCount = 0;
        int LBlockCount = 0;
        int ZBlockCount = 0;
        int SBlockCount = 0;
        int TBlockCount = 0;
        int OBlockCount = 0;

        for (int i = 0; i < 4100000; i++) {
            Block randomBlock = gameController.getBlock(0);

            if (randomBlock instanceof IBlock)
                IBlockCount++;
            else if (randomBlock instanceof JBlock)
                JBlockCount++;
            else if (randomBlock instanceof LBlock)
                LBlockCount++;
            else if (randomBlock instanceof ZBlock)
                ZBlockCount++;
            else if (randomBlock instanceof SBlock)
                SBlockCount++;
            else if (randomBlock instanceof TBlock)
                TBlockCount++;
            else if (randomBlock instanceof OBlock)
                OBlockCount++;
        }

        assertThat(IBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(JBlockCount).isGreaterThan(570000).isLessThan(630000);
        assertThat(LBlockCount).isGreaterThan(570000).isLessThan(630000);
        assertThat(ZBlockCount).isGreaterThan(570000).isLessThan(630000);
        assertThat(SBlockCount).isGreaterThan(570000).isLessThan(630000);
        assertThat(TBlockCount).isGreaterThan(570000).isLessThan(630000);
        assertThat(OBlockCount).isGreaterThan(570000).isLessThan(630000);
    }

    @Test
    @Order(1)
    public void testGameController() {
        int keyInput[] = { VK_SPACE, VK_DOWN, VK_LEFT, VK_RIGHT, VK_SPACE, VK_UP,
                VK_UP };
        testRobot.pressAndReleaseKeys(keyInput);
        testRobot.pressAndReleaseKeys(VK_SPACE);
    }

    @Test
    @Order(2)
    public void testGameController2() {
        int keyInput[] = { VK_SPACE, VK_DOWN, VK_LEFT, VK_RIGHT, VK_SPACE, VK_UP, VK_UP };
        testRobot.pressAndReleaseKeys(keyInput);
        testRobot.pressAndReleaseKeys(VK_SPACE);
    }

    @AfterEach
    public void tearDown() {
        testAllView.removeAllEventListeners();
        testAllView.getGameView().getGameBoardPane().setText("");
        frame.stopTimer();
        frame.dispose();
    }

}
