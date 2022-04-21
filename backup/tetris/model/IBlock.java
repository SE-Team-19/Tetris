package tetris.model;

import java.awt.Color;

class IBlock extends Block {

    public IBlock() {
        shape = new int[][] {{1, 1, 1, 1}};
        color = Color.RED;
    }
}
