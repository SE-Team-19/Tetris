package tetris.model;

import java.awt.Color;

public class TBlock extends Block {
    public TBlock() {
        shape = new int[][]{ { 1, 1, 1 }, { 0, 1, 0 } };
        color = Color.CYAN;
    }
}
