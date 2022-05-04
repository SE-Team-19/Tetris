package tetris.model;

import java.awt.Color;

public class IBlock extends Block {

    public IBlock() {
        initIBlock();
    }

    public IBlock(boolean isAttachL) {
        initIBlock();
        if (isAttachL)
            attachL();
    }

    private void initIBlock() {
        shape = new int[][] { { 1, 1, 1, 1 } };
        color = Color.RED;
        blindColor = new Color(227, 66, 52);
        identifynumber = IBLOCK_IDENTIFY_NUMBER;
        rotateCount = IBLOCK_FIRST_ROTATE_STATE;
        initVisualShapeAndXYList(identifynumber);
    }
}
