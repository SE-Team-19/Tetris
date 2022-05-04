package tetris.model;

import java.awt.Color;

public class SBlock extends Block {

    public SBlock() {
        initSBlock();
    }

    public SBlock(boolean isAttachL) {
        initSBlock();
        if (isAttachL)
            attachL();
    }

    private void initSBlock() {
        shape = new int[][] { { 0, 1, 1 }, { 1, 1, 0 } };
        color = Color.MAGENTA;
        blindColor = Color.MAGENTA;
        identifynumber = SBLOCK_IDENTIFY_NUMBER;
        rotateCount = FIRST_ROTATE_STATE;
        initVisualShapeAndXYList(identifynumber);
    }
}
