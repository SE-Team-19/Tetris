package tetris.model;

import java.awt.Color;

public class ZBlock extends Block {

    public ZBlock() {
        shape = new int[][] { { 1, 1, 0 }, { 0, 1, 1 } };
        color = Color.GREEN;
        blindColor = new Color(0, 146, 115);
        indentifynumber = 8;
    }

}
