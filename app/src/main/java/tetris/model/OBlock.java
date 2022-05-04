package tetris.model;

import java.awt.Color;

public class OBlock extends Block {

    public OBlock() {
        initOBlock();
    }

    public OBlock(boolean isAttachL) {
        initOBlock();
        if (isAttachL)
            attachL();
    }

    private void initOBlock() {
        shape = new int[][] { { 1, 1 }, { 1, 1 } };
        color = Color.YELLOW;
        blindColor = Color.YELLOW;
        identifynumber = OBLOCK_IDENTIFY_NUMBER;
        rotateCount = OBLOCK_ROTATE_STATE;
        initVisualShapeAndXYList(identifynumber);
    }
}
