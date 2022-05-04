package tetris.model;

import java.awt.Color;
import java.util.*;

public abstract class Block {

    protected int[][] shape;
    protected int[][] visualShape;
    protected Color color;
    protected Color blindColor;
    protected int identifynumber;
    protected int rotateCount;
    protected List<int[]> xyList;
    protected Random rnd = new Random();
    public static final int FIRST_ROTATE_STATE = 0;
    public static final int SECOND_ROTATE_STATE = 1;
    public static final int THIRD_ROTATE_STATE = 2;
    public static final int FOURTH_ROTATE_STATE = 3;
    public static final int IBLOCK_FIRST_ROTATE_STATE = 4;
    public static final int IBLOCK_SECOND_ROTATE_STATE = 5;
    public static final int IBLOCK_THIRD_ROTATE_STATE = 6;
    public static final int IBLOCK_FOURTH_ROTATE_STATE = 7;
    public static final int OBLOCK_ROTATE_STATE = 8;
    public static final int DO_NOT_ROTATE_STATE = -1;
    public static final int NULL_IDENTIFY_NUMBER = 0;
    public static final int IBLOCK_IDENTIFY_NUMBER = 2;
    public static final int JBLOCK_IDENTIFY_NUMBER = 3;
    public static final int LBLOCK_IDENTIFY_NUMBER = 4;
    public static final int OBLOCK_IDENTIFY_NUMBER = 5;
    public static final int SBLOCK_IDENTIFY_NUMBER = 6;
    public static final int TBLOCK_IDENTIFY_NUMBER = 7;
    public static final int ZBLOCK_IDENTIFY_NUMBER = 8;
    public static final int WEIGHTBLOCK_IDENTIFY_NUMBER = 9;
    public static final int ONELINEBLOCK_IDENTIFY_NUMBER = 10;

    protected Block() {
        shape = new int[][] { { 1, 1 }, { 1, 1 } };
        color = Color.YELLOW;
        identifynumber = 1;
        rotateCount = FIRST_ROTATE_STATE;
        initVisualShapeAndXYList(identifynumber);
    }

    public void copyBlock(Block src) {
        this.shape = copyShape(src.getShape());
        this.visualShape = copyShape(src.getVisualShape());
        this.color = src.getColor();
        this.identifynumber = src.getIdentifynumber();
        this.rotateCount = src.rotateCount;
        this.blindColor = src.blindColor;
    }

    private int[][] copyShape(int[][] shape) {
        int length = shape.length;
        int width = shape[0].length;
        int[][] copyShape = new int[length][width];
        for (int i = 0; i < shape.length; i++) {
            copyShape[i] = Arrays.copyOf(shape[i], copyShape[i].length);
        }
        return copyShape;
    }

    protected void initVisualShapeAndXYList(int id) {
        int length = shape.length;
        int width = shape[0].length;
        xyList = new ArrayList<>();
        visualShape = new int[length][width];
        for (int j = 0; j < length; j++) {
            for (int i = 0; i < width; i++) {
                if (shape[j][i] > 0) {
                    visualShape[j][i] = id;
                    xyList.add(new int[] { i, j });
                } else
                    visualShape[j][i] = 0;
            }
        }
    }

    protected void attachL() {
        int[] index = xyList.get(rnd.nextInt(xyList.size()));
        visualShape[index[1]][index[0]] = ONELINEBLOCK_IDENTIFY_NUMBER;
    }

    public int[][] getShape() {
        return shape;
    }

    public int getShape(int x, int y) {
        return shape[y][x];
    }

    public int[][] getVisualShape() {
        return visualShape;
    }

    public int getVisualShape(int x, int y) {
        return visualShape[y][x];
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

    public int getIdentifynumber() {
        return identifynumber;
    }

    public int getRotateCount() {
        return rotateCount;
    }

    public void rotate() {
        int length = shape.length;
        int width = shape[0].length;

        int[][] rotate = new int[width][length];
        int[][] visualrotate = new int[width][length];
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < width; col++) {
                rotate[col][length - 1 - row] = shape[row][col];
                visualrotate[col][length - 1 - row] = visualShape[row][col];
            }
        }
        shape = rotate;
        visualShape = visualrotate;
        if (rotateCount != OBLOCK_ROTATE_STATE)
            plusRotateCount();
    }

    private void plusRotateCount() {
        int length = shape.length;
        int width = shape[0].length;

        rotateCount = (rotateCount + 1) % IBLOCK_FIRST_ROTATE_STATE;

        // IBlock
        if (Math.abs(width - length) > 2)
            rotateCount += IBLOCK_FIRST_ROTATE_STATE;
    }
}
