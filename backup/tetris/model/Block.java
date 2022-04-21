package tetris.model;

import java.awt.Color;

abstract class Block {

    protected int[][] shape;
    protected Color color;

    protected Block() {
        shape = new int[][] {{0}};
        color = Color.YELLOW;
    }

    public int getShape(int x, int y) {
        return shape[y][x];
    }

    public Color getColor() {
        return color;
    }
}
