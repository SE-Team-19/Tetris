package tetris.model;

import java.awt.Color;

public class TBlock extends Block {

    public TBlock() {
        initTBlock();
    }

    public TBlock(boolean isAttachItem) {
        initTBlock();
        if (isAttachItem)
            attachItem(BOMBBLOCK_IDENTIFY_NUMBER);
    }

    private void initTBlock() {
        shape = new int[][] { { 0, 1, 0 }, { 1, 1, 1 } };
        color = Color.CYAN;
        blindColor = Color.CYAN;
        identifynumber = TBLOCK_IDENTIFY_NUMBER;
        rotateCount = FIRST_ROTATE_STATE;
        initVisualShapeAndXYList(identifynumber);
    }
}
