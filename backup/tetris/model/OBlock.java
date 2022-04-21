package tetris.model;

import java.awt.Color;

class OBlock extends Block {

    public OBlock() {
        shape = new int[][] {{1, 1}, {1, 1}};
        color = Color.YELLOW;
    }
}
