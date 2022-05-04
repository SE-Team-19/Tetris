package tetris.model;

import java.awt.Color;

public class JBlock extends Block {

    public JBlock() {
        shape = new int[][] { { 1, 0, 0 }, { 1, 1, 1 } };
        color = Color.ORANGE;
        blindColor = Color.ORANGE;
        identifynumber = JBLOCK_IDENTIFY_NUMBER;
        rotateCount = FIRST_ROTATE_STATE;
        initVisualShapeAndXYList(identifynumber);
    }

    public JBlock(boolean isAttachL) {
        initJBlock();
        if (isAttachL)
            attachL();
    }

    private void initJBlock() {
        shape = new int[][] { { 1, 0, 0 }, { 1, 1, 1 } };
        color = Color.ORANGE;
        blindColor = Color.ORANGE;
        identifynumber = JBLOCK_IDENTIFY_NUMBER;
        rotateCount = FIRST_ROTATE_STATE;
        initVisualShapeAndXYList(identifynumber);
    }
}
