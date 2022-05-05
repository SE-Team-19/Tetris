package tetris.model;

import java.awt.Color;

public class ZBlock extends Block {

    public ZBlock() {
        initZBlock();
    }

    public ZBlock(boolean isAttachItem) {
        initZBlock();
        if (isAttachItem)
            attachItem(BOMBBLOCK_IDENTIFY_NUMBER);
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
