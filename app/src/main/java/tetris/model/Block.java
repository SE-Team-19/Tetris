package tetris.model;

import java.awt.Color;
import java.util.Random;

public class Block {

    Color colors[] = {Color.white, Color.BLUE, Color.CYAN,
        Color.darkGray, Color.GRAY,
        Color.YELLOW, Color.magenta, Color.YELLOW};

    public enum BlockShape {
        NoShape, ZBlock, SBlock, IBlock,
        TBlock, OBlock, LBlock, JBlock
    }

    public Color getBlockShape(BlockShape blockShape) {

        switch(blockShape){
            case NoShape:
                return Color.WHITE;
            case ZBlock:
                return Color.YELLOW;
            case SBlock:
                return Color.GRAY;
            case IBlock:
                return Color.BLUE;
            case TBlock:
                return Color.GREEN;
            case OBlock:
                return Color.magenta;
            case LBlock:
                return Color.CYAN;
            case JBlock:
                return Color.LIGHT_GRAY;
        }
        return Color.WHITE;
    }

    private BlockShape blockShape;
    private int[][] block;

    public Block() {
        block = new int[4][2];
        setShape(BlockShape.NoShape);
    }

    public void setShape(BlockShape shape) {
        int[][][] blockTable = new int[][][]{
            {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
            {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
            {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
            {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
            {{-1, 0}, {0, 0}, {1, 0}, {0, -1}},
            {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
            {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
            {{1, -1}, {0, -1}, {0, 0}, {0, 1}}
        };
        for (int i = 0; i < 4; i++) {
            System.arraycopy(blockTable[shape.ordinal()], 0, block, 0, 4);
        }
        blockShape = shape;

        getBlockShape(blockShape);
    }

    public void setX(int index, int x) {
        block[index][0] = x;
    }

    public void setY(int index, int y) {
        block[index][1] = y;
    }

    public int x(int index) {
        return block[index][0];
    }

    public int y(int index) {
        return block[index][1];
    }

    public BlockShape getShape() {
        return blockShape;
    }

    public void setRandomShape() {
        var r = new Random();
        int random = Math.abs(r.nextInt()) % 7 + 1;

        BlockShape[] values = BlockShape.values();    // 배열 형태로 리턴받는다.
        setShape(values[random]);
    }

    public int minX() {
        int m = block[0][0];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, block[i][0]);
        }
        return m;
    }

    public int minY() {
        int m = block[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, block[i][1]);
        }
        return m;
    }

    public Block rotateLeft() {
        if (blockShape == BlockShape.OBlock) {
            return this;
        }
        var result = new Block();
        result.blockShape = blockShape;

        for (int i = 0; i < 4; i++) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }
}