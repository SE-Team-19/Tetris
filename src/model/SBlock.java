package Tetris.src.model;

import java.awt.Color;

class SBlock extends Block {
    public SBlock() {
        block = new int[][] {
                { 0, 1, 1 },
                { 1, 1, 0 },
        };
        color = Color.MAGENTA;
    }
}
