package tetris.src.model;

import java.awt.Color;

public abstract class Block {
    protected int[][] block;
    protected Color color;

    public Block() {
        block = new int[][] {{0},};
        color = Color.YELLOW;
    }

    public int getShape(int x, int y) {
        return block[y][x];
    }

    public Color getColor() {
        return color;
    }
}
