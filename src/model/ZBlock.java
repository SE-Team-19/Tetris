package tetris.src.model;

import java.awt.Color;

class ZBlock extends Block {
    public ZBlock() {
        block = new int[][] {{1, 1, 0}, {0, 1, 1}};
        color = Color.GREEN;
    }
}
