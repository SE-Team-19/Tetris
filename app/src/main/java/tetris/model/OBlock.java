package tetris.model;

import java.awt.Color;

public class OBlock extends Block {

    public OBlock() {
        shape = new int[][] { { 1, 1 }, { 1, 1 } };
        color = Color.YELLOW;
        blindColor = Color.YELLOW;
        indentifynumber = 5;
        rotateCount = 0;
    }
}
