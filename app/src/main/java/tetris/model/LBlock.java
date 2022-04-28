package tetris.model;

import java.awt.Color;

public class LBlock extends Block {

    public LBlock() {
        shape = new int[][] { { 0, 0, 1 }, { 1, 1, 1 } };
        color = Color.BLUE;
        blindColor = Color.BLUE;
        indentifynumber = 4;
        rotateCount = 0;
    }
}
