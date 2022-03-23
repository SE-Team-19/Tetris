package Tetris.src.model;

import java.awt.Color;

public abstract class Block {

    public int[][] block;
    public Color color;

    public Block() {
        block = new int[][] {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        };
        color = Color.YELLOW;
    }

    public int getShape(int x, int y) {
        return block[y][x];
    }

    public Color getColor() {
        return color;
    }

    public void rotate() {
        //Rotate the block 90 deg. clockwise.
    }

    public int height() {
        return block.length;
    }

    public int width() {
        if(block.length > 0)
            return block[0].length;
        return 0;
    }

}

class LBlock extends Block{
    // LEFT BLOCK

    public LBlock() {
        block = new int[][] {
                {1, 0, 0},
                {1, 0, 0},
                {1, 1, 1}
        };
        color = Color.RED;
    }
}

class RBlock extends Block{
    // RIGHT BLOCK

    public RBlock() {
        block = new int[][] {
                {0, 0, 1},
                {0, 0, 1},
                {1, 1, 1}
        };
        color = Color.BLUE;
    }
}

class TBlock extends Block{
    // SHAPE IS 'T'

    public TBlock() {
        block = new int[][] {
                {1, 1, 1},
                {0, 1, 1},
                {0, 1, 0}
        };
        color = Color.CYAN;
    }
}

class UBlock extends Block{
    // SHAPE IS 'U'

    public UBlock() {
        block = new int[][] {
                {1, 0, 1},
                {1, 0, 1},
                {1, 1, 1}
        };
        color = Color.MAGENTA;
    }
}

class OBlock extends Block{
    // SHAPE IS '2*2'

    public OBlock() {
        block = new int[][] {
                {0, 0, 0},
                {1, 1, 0},
                {1, 1, 0}
        };
        color = Color.YELLOW;
    }
}

class IBlock extends Block{
    // SHAPE IS 'I'

    public IBlock() {
        block = new int[][] {
                {0, 1, 0},
                {0, 1, 0},
                {0, 1, 0}
        };
        color = Color.PINK;
    }
}

class MBlock extends Block{
    // SHAPE IS '2 * 3'

    public MBlock() {
        block = new int[][] {
                {0, 0, 0},
                {1, 1, 1},
                {1, 1, 1}
        };
        color = Color.DARK_GRAY;
    }
}
