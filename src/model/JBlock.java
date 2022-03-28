package tetris.src.model;

import java.awt.Color;

class JBlock extends Block {
    public JBlock() {
        block = new int[][] {
                { 0, 1 },
                { 0, 1 },
                { 1, 1 },
        };
        color = Color.ORANGE;
    }
}
