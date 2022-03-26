package Tetris.src.model;

import java.awt.Color;

class Block {
    protected int[][] shape;
    protected Color color;

    public Block() {
        shape = new int[][] {
                { 0 },
        };
        color = Color.YELLOW;
    }

    public int getShape(int x, int y) {
        return shape[y][x];
    }

    public Color getColor() {
        return color;
    }
}
