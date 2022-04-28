package tetris.model;

import java.awt.Color;

public class IBlock extends Block {

    public IBlock() {
        shape = new int[][] { { 1, 1, 1, 1 } };
        color = Color.RED;
        blindColor = new Color(227, 66, 52);
        indentifynumber = 2;
        rotateCount = 4;
    }
}
