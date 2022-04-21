package tetris.controller;

import java.awt.event.KeyEvent;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import tetris.model.*;

public class GameControllerTest {
    private GameController gameController = new GameController(
            new Setting(0, false, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN,
                    KeyEvent.VK_UP, KeyEvent.VK_SPACE));

    @Test
    public void generateIBlockMoreThan20Pct() {
        int IblockCount = 0;
        int JblockCount = 0;
        int LblockCount = 0;
        int ZblockCount = 0;
        int SblockCount = 0;
        int TblockCount = 0;
        int OblockCount = 0;
        for (int i = 0; i < 144000; i++) {
            Block randomBlock = gameController.getRandomBlock(1);
            if (randomBlock instanceof IBlock)
                IblockCount++;
            else if (randomBlock instanceof JBlock)
                JblockCount++;
            else if (randomBlock instanceof LBlock)
                LblockCount++;
            else if (randomBlock instanceof ZBlock)
                ZblockCount++;
            else if (randomBlock instanceof SBlock)
                SblockCount++;
            else if (randomBlock instanceof TBlock)
                TblockCount++;
            else if (randomBlock instanceof OBlock)
                OblockCount++;
        }

        assertThat(IblockCount).isGreaterThan(22800).isLessThan(25200);
        assertThat(JblockCount).isGreaterThan(19000).isLessThan(21000);
        assertThat(LblockCount).isGreaterThan(19000).isLessThan(21000);
        assertThat(ZblockCount).isGreaterThan(19000).isLessThan(21000);
        assertThat(SblockCount).isGreaterThan(19000).isLessThan(21000);
        assertThat(TblockCount).isGreaterThan(19000).isLessThan(21000);
        assertThat(OblockCount).isGreaterThan(19000).isLessThan(21000);
    }

    @Test
    public void generateIBlockLessThan20Pct() {
        int IblockCount = 0;
        int JblockCount = 0;
        int LblockCount = 0;
        int ZblockCount = 0;
        int SblockCount = 0;
        int TblockCount = 0;
        int OblockCount = 0;
        for (int i = 0; i < 164000; i++) {
            Block randomBlock = gameController.getRandomBlock(2);
            if (randomBlock instanceof IBlock)
                IblockCount++;
            else if (randomBlock instanceof JBlock)
                JblockCount++;
            else if (randomBlock instanceof LBlock)
                LblockCount++;
            else if (randomBlock instanceof ZBlock)
                ZblockCount++;
            else if (randomBlock instanceof SBlock)
                SblockCount++;
            else if (randomBlock instanceof TBlock)
                TblockCount++;
            else if (randomBlock instanceof OBlock)
                OblockCount++;
        }

        assertThat(IblockCount).isGreaterThan(19000).isLessThan(21000);
        assertThat(JblockCount).isGreaterThan(22800).isLessThan(25200);
        assertThat(LblockCount).isGreaterThan(22800).isLessThan(25200);
        assertThat(ZblockCount).isGreaterThan(22800).isLessThan(25200);
        assertThat(SblockCount).isGreaterThan(22800).isLessThan(25200);
        assertThat(TblockCount).isGreaterThan(22800).isLessThan(25200);
        assertThat(OblockCount).isGreaterThan(22800).isLessThan(25200);
    }
}
