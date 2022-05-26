package tetris.model;

import java.awt.Color;

public class ZBlock extends Block {

    public ZBlock() {
        initZBlock();
    }

    private void initZBlock() {
        shape = new int[][] { { 1, 1, 0 }, { 0, 1, 1 } };
        color = Color.GREEN;
        blindColor = new Color(0, 146, 115);
        identifynumber = ZBLOCK_IDENTIFY_NUMBER;
        rotateCount = FIRST_ROTATE_STATE;
        initVisualShapeAndXYList(identifynumber);
    }

}
