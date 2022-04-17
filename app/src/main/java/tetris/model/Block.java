package tetris.model;

import java.awt.Color;

public abstract class Block {

    protected int[][] shape;
    protected Color color;
    //protected int x,y;

    public Block() {
        shape = new int[][]{
            {1, 1},
            {1, 1}
        };
        color = Color.YELLOW;

//        x = 3;
//        y = 0;
    }

    public int getShape(int x, int y) {
        return shape[y][x];
    }

    public Color getColor() {
        return color;
    }

    public int height() {
        return shape.length;
    }

    public int width() {
        if(shape.length > 0)
            return shape[0].length;
        return 0;
    }

    /*
    public int getX() {return x;}
    public int getY() {return y;}

    public void moveDown() {y++;}
    public void moveLeft() {x--;}
    public void moveRight() {x++;}

    public int getBottomEdge() { return y + height(); }
    public int getLeftEdge() {return x;}
    public int getRightEdge() {return x+ width();} */

    // 시계방향으로 90도 회전시킨다.
    public void rotate() {
        int length = shape.length;
        int width = shape[0].length;

        int[][] rotate = new int[width][length];
        for(int row = 0; row < length; row++){
            for(int col = 0; col < width; col++){
                rotate[col][length - 1 - row] = shape[row][col];
            }
        }
        shape = rotate;
    }
}