package tetris.model;

import java.awt.Color;

public class SBlock extends Block {

    public SBlock() {
        shape = new int[][] { { 0, 1, 1 }, { 1, 1, 0 } };
        color = Color.MAGENTA;
        blindColor = Color.MAGENTA;
    }
}
