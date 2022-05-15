package tetris.model;

import java.awt.Color;

public class LBlock extends Block {

    public LBlock() {
        initLBlock();
    }

    private void initLBlock() {
        shape = new int[][] { { 0, 0, 1 }, { 1, 1, 1 } };
        color = Color.BLUE;
        blindColor = Color.BLUE;
        identifynumber = LBLOCK_IDENTIFY_NUMBER;
        rotateCount = FIRST_ROTATE_STATE;
        initVisualShapeAndXYList(identifynumber);
    }
}
