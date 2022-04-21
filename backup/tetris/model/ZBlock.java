package tetris.model;

import java.awt.Color;

class ZBlock extends Block {

    public ZBlock() {
        shape = new int[][] {{1, 1, 0}, {0, 1, 1}};
        color = Color.GREEN;
    }
}