package tetris.model;

import java.awt.Color;

public class WeightBlock extends Block {

    public WeightBlock() {
        shape = new int[][] { { 0, 1, 1, 0 }, { 1, 1, 1, 1 } };
        color = Color.GRAY;
        blindColor = Color.GRAY;
        identifynumber = WEIGHTBLOCK_IDENTIFY_NUMBER;
        rotateCount = DO_NOT_ROTATE_STATE;
        initVisualShapeAndXYList(identifynumber);
        attachItemID = WEIGHTBLOCK_IDENTIFY_NUMBER;
    }

}
