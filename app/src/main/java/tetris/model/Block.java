package tetris.model;

import java.awt.Color;

public class Block {

    protected int[][] shape;
    protected Color color;

    private int x;  // block의 x, y 위치를 받는 변수를 따로 선언
    private int y;

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

    public int getHeight() {
        return shape.length;
    }

    public int getWidth() {
        if(shape.length > 0)
            return shape[0].length;
        return 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveDown() {
        y += 1;
    }

    public void moveLeft() {
        x -= 1;
    }

    public void moveRight() {
        x += 1;
    }

    // 바닥에 닿았는지 확인할 수 있는 메소드
    public int getBottomLocation() {    
        return y + getHeight();
    }
    
    // 오른족에 닿았는지 확인할 수 있는 메소드
    public int getRightLocation() {
        return x + getWidth();
    }

    // 왼쪽에 닿았는지 확인할 수 있는 메소드 : getX와 동일
    public int getLeftLocation() {
        return x;
    }
    

    // 블록을 회전시키는 메소드: 90도, 시계반대방향
    // 정상작동 여부 test 해봐야함(확실하지 않음)
    public int[][] rotate() {
        int height = shape.length;
        int width = shape[0].length;

        int [][] current2 = new int[width][height];

        int [][] rotateBlock = new int[width][height];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                rotateBlock[j][height - 1 - i ] = current2[i][j];
            }
        }
        return rotateBlock;
    }


}
