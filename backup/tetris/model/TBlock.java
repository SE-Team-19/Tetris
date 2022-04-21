package tetris.model;

import java.awt.Color;

class TBlock extends Block {

    public TBlock() {
        shape = new int[][] {{0, 1, 0}, {1, 1, 1}};
        color = Color.CYAN;
    }
}
