package tetris.model;

import java.awt.Color;

public class JBlock extends Block {

    public JBlock() {
        shape = new int[][] { { 1, 0, 0 }, { 1, 1, 1 } };
        color = Color.ORANGE;
        blindColor = Color.ORANGE;
        indentifynumber = 3;
        rotateCount = 0;
    }
}
