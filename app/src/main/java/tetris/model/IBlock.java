package Tetris.src.model;

import java.awt.Color;

public class IBlock extends Block {
    public IBlock() {
        shape = new int[][] {
                { 1, 1, 1, 1 },
        };
        color = Color.RED;
    }
}