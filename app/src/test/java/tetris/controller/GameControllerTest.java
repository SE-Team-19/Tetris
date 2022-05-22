package tetris.controller;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static java.awt.event.KeyEvent.*;

import javax.swing.*;
import java.awt.*;
import java.awt.AWTException;
import java.util.Deque;
import tetris.TestAllView;
import tetris.TestRobot;

import tetris.model.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameControllerTest {

    SingleGameController gameController = new SingleGameController(new PlayerController());
    ViewController frame;
    TestAllView testAllView;
    TestRobot testRobot;
    Deque<Integer> queue;

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
        for (int i = 0; i < 100000; i++) {
            gameController.generateBlockRandomizer(GameController.EASY_MODE);
            // System.out.println("현재크기" + gameController.randomBlockList.size());
            for (int j = 0; j < gameController.randomBlockList.size(); j++) {
                // System.out.println("현재값 randomBlockList.get(" + j + "): " +
                // gameController.randomBlockList.get(j));
                switch (gameController.randomBlockList.get(j)) {
                    case Block.IBLOCK_IDENTIFY_NUMBER:
                        IBlockCount++;
                        continue;
                    case Block.JBLOCK_IDENTIFY_NUMBER:
                        JBlockCount++;
                        continue;
                    case Block.LBLOCK_IDENTIFY_NUMBER:
                        LBlockCount++;
                        continue;
                    case Block.OBLOCK_IDENTIFY_NUMBER:
                        OBlockCount++;
                        continue;
                    case Block.SBLOCK_IDENTIFY_NUMBER:
                        SBlockCount++;
                        continue;
                    case Block.TBLOCK_IDENTIFY_NUMBER:
                        TBlockCount++;
                        continue;
                    case Block.ZBLOCK_IDENTIFY_NUMBER:
                        ZBlockCount++;
                        continue;
                    default:
                        continue;
                }
            }
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
        for (int i = 0; i < 100000; i++) {
            gameController.generateBlockRandomizer(GameController.HARD_MODE);
            System.out.println("현재크기" + gameController.randomBlockList.size());
            for (int j = 0; j < gameController.randomBlockList.size(); j++) {
                switch (gameController.randomBlockList.get(j)) {
                    case Block.IBLOCK_IDENTIFY_NUMBER:
                        IBlockCount++;
                        continue;
                    case Block.JBLOCK_IDENTIFY_NUMBER:
                        JBlockCount++;
                        continue;
                    case Block.LBLOCK_IDENTIFY_NUMBER:
                        LBlockCount++;
                        continue;
                    case Block.OBLOCK_IDENTIFY_NUMBER:
                        OBlockCount++;
                        continue;
                    case Block.SBLOCK_IDENTIFY_NUMBER:
                        SBlockCount++;
                        continue;
                    case Block.TBLOCK_IDENTIFY_NUMBER:
                        TBlockCount++;
                        continue;
                    case Block.ZBLOCK_IDENTIFY_NUMBER:
                        ZBlockCount++;
                        continue;
                    default:
                        continue;
                }
            }
        }

        System.out.println("IBlockCount:" + IBlockCount);
        System.out.println("JBlockCount:" + JBlockCount);
        System.out.println("LBlockCount:" + LBlockCount);
        System.out.println("OBlockCount:" + OBlockCount);
        System.out.println("SBlockCount:" + SBlockCount);
        System.out.println("TBlockCount:" + TBlockCount);

        assertThat(IBlockCount).isGreaterThan(380000).isLessThan(420000);
        assertThat(JBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(LBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(ZBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(SBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(TBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(OBlockCount).isGreaterThan(475000).isLessThan(525000);
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
        testAllView.getGameView().getPlayerOneGameBoardPane().setText("");
        frame.dispose();
    }
}
