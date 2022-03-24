package Tetris.src.model;

import java.awt.Color;

class OBlock extends Block {
    public OBlock() {
        block = new int[][] {
                { 1, 1 },
                { 1, 1 },
        };
        color = Color.YELLOW;
    }
}
