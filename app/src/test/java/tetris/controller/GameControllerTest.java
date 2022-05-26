package tetris.controller;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;
import static java.awt.event.KeyEvent.*;

import javax.swing.*;
import java.awt.AWTException;
import java.util.Deque;
import tetris.TestAllView;
import tetris.TestRobot;

import tetris.model.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameControllerTest {
    ViewController frame;
    TestAllView testAllView;
    TestRobot testRobot;
    MultiGameController gameController = new MultiGameController(new PlayerController(), frame);
    Deque<Integer> queue;

    @BeforeAll
    public static void setUpOnce() {
        // FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setUp() {
        frame = new ViewController();
        testAllView = new TestAllView();
        assertThat(frame).isInstanceOf(JFrame.class);
        try {
            testRobot = new TestRobot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
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
        assertThat(IBlockCount).isGreaterThan(380000).isLessThan(420000);
        assertThat(JBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(LBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(ZBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(SBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(TBlockCount).isGreaterThan(475000).isLessThan(525000);
        assertThat(OBlockCount).isGreaterThan(475000).isLessThan(525000);
    }

    @Test
    @Order(2)
    public void testGameController() {
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE, VK_SPACE, VK_SPACE, VK_SPACE });
        assertThat(testAllView.getGameView().getGameDiffLabel().getText()).isEqualTo("easy");
        testRobot.pressAndReleaseKeys(new int[] { VK_UP, VK_DOWN, VK_LEFT, VK_RIGHT, VK_SPACE });
        testRobot.pressAndReleaseKeys(new int[] { VK_ESCAPE, VK_SPACE });
        testRobot.pressAndReleaseKeys(new int[] { VK_ESCAPE, VK_DOWN, VK_SPACE });
        testRobot.pressAndReleaseKeys(new int[] { VK_ESCAPE, VK_DOWN, VK_DOWN, VK_SPACE });
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getMainView().getStartBtn());
    }

    @Test
    @Order(1)
    public void testGameDiffMode() {
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE, VK_SPACE, VK_SPACE, VK_SPACE });
        assertThat(testAllView.getGameView().getGameDiffLabel().getText()).isEqualTo("easy");
        testRobot.pressAndReleaseKeys(new int[] { VK_ESCAPE, VK_DOWN, VK_DOWN, VK_SPACE });
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getMainView().getStartBtn());
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE, VK_SPACE, VK_SPACE, VK_RIGHT, VK_SPACE });
        assertThat(testAllView.getGameView().getGameDiffLabel().getText()).isEqualTo("normal");
        testRobot.pressAndReleaseKeys(new int[] { VK_ESCAPE, VK_DOWN, VK_DOWN, VK_SPACE });
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getMainView().getStartBtn());
        testRobot.pressAndReleaseKeys(new int[] { VK_SPACE, VK_SPACE, VK_SPACE, VK_RIGHT, VK_RIGHT, VK_SPACE });
        assertThat(testAllView.getGameView().getGameDiffLabel().getText()).isEqualTo("hard");
        testRobot.pressAndReleaseKeys(new int[] { VK_ESCAPE, VK_DOWN, VK_DOWN, VK_SPACE });
        assertThat(frame.getFocusOwner()).isEqualTo(testAllView.getMainView().getStartBtn());
    }

    @Test
    @Order(3)
    public void testAfterSingleGame() {
        testRobot.pressAndReleaseKeys(VK_SPACE, VK_SPACE, VK_SPACE, VK_SPACE);
        assertThat(testAllView.getGameView().getGameDiffLabel().getText()).isEqualTo("easy");
        testRobot.pressAndReleaseKeys(
                VK_SPACE, VK_SPACE, VK_SPACE, VK_SPACE, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN,
                VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN,
                VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN, VK_SPACE, VK_DOWN);
        testRobot.delay(5000);
        testRobot.pressAndReleaseKeys(VK_SPACE);
        testRobot.pressAndReleaseKeys(VK_T, VK_E, VK_S, VK_T, VK_ENTER);
        testRobot.pressAndReleaseKeys(VK_SPACE);
    }

    @Test
    @Order(4)
    public void testTimeAttack() {
        testRobot.pressAndReleaseKeys(VK_SPACE, VK_SPACE, VK_SPACE, VK_SPACE);
        assertThat(testAllView.getGameView().getSingleGameDisplayTimeLabel().getText()).isEqualTo("남은 시간");
        testRobot.delay(100000);
        testRobot.delay(6000);
        testRobot.pressAndReleaseKeys(VK_SPACE);
        testRobot.pressAndReleaseKeys(VK_T, VK_E, VK_S, VK_T, VK_ENTER);
        testRobot.pressAndReleaseKeys(VK_SPACE);
    }

    @AfterEach
    public void tearDown() {
        testAllView.removeAllEventListeners();
        testAllView.getGameView().resetGameView();
        frame.dispose();
    }

}