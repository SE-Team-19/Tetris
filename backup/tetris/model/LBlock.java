package tetris.model;

import java.awt.Color;

class LBlock extends Block {

    public LBlock() {
        shape = new int[][] {{1, 0}, {1, 0}, {1, 1}};
        color = Color.BLUE;
    }
}
