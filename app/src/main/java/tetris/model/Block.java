package tetris.model;

import java.awt.Color;

public abstract class Block {

    protected int[][] shape;
    protected Color color;
    protected Color blindColor;
    protected int indentifynumber;
    protected int rotateCount;

    protected Block() {
        shape = new int[][] { { 1, 1 }, { 1, 1 } };
        color = Color.YELLOW;
        indentifynumber = 1;
        rotateCount = 0;
        // x = 3;
        // y = 0;
    }

    public void copyBlock(Block src) {
        this.shape = src.getShape();
        this.color = src.getColor();
        this.indentifynumber = src.getIndentifynumber();
        this.rotateCount = src.rotateCount;
        this.blindColor = src.blindColor;
    }

    public int[][] getShape() {
        return shape;
    }

    public int getShape(int x, int y) {
        return shape[y][x];
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getBlindColor() {
        return blindColor;
    }

    public void setBlindColor(Color blindColor) {
        this.blindColor = blindColor;
    }

    public int getHeight() {
        return shape.length;
    }

    public int getWidth() {
        if (shape.length > 0)
            return shape[0].length;
        return 0;
    }

    public int getIndentifynumber() {
        return indentifynumber;
    }

    public int getRotateCount() {
        return rotateCount;
    }

    public void rotate() {
        int length = shape.length;
        int width = shape[0].length;

        int[][] rotate = new int[width][length];
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < width; col++) {
                rotate[col][length - 1 - row] = shape[row][col];
            }
        }
        shape = rotate;
        plusRotateCount();
    }

    public void plusRotateCount() {
        int length = shape.length;
        int width = shape[0].length;

        // OBlock
        if (width == length) {
            rotateCount = 3;
            return;
        }

        rotateCount = (rotateCount + 1) % 4;

        // IBlock
        if (Math.abs(width - length) > 2)
            rotateCount += 4;
    }
}
