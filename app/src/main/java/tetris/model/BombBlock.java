package tetris.model;

import java.awt.Color;

public class BombBlock extends Block {
    public BombBlock() {
        initBombBlock();
    }

    private void initBombBlock() {
        shape = new int[][] { { 1 } };
        color = Color.RED;
        blindColor = new Color(227, 66, 52);
        identifynumber = BOMBBLOCK_IDENTIFY_NUMBER;
        rotateCount = DO_NOT_ROTATE_STATE;
        initVisualShapeAndXYList(identifynumber);
    }
}
