package tetris.model;

import java.awt.Rectangle;
import java.util.List;


public class Setting {

    private int displayMode;
    private List<Rectangle> displayList;
    private boolean colorBlindMode;
    private int moveLeftKey;
    private int moveRightKey;
    private int moveDownKey;
    private int rotateKey;
    private int stackKey;

    public Setting(int displayMode, boolean colorBlindMode, int moveLeftKey, int moveRightKey,
            int moveDownKey, int rotateKey, int stackKey) {
        this.displayMode = displayMode;
        this.colorBlindMode = colorBlindMode;
        this.moveLeftKey = moveLeftKey;
        this.moveRightKey = moveRightKey;
        this.moveDownKey = moveDownKey;
        this.rotateKey = rotateKey;
        this.stackKey = stackKey;
    }

    public int getDisplayMode() {
        return this.displayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public List<Rectangle> getDisplayList() {
        return this.displayList;
    }

    public void setDisplayList(List<Rectangle> displayList) {
        this.displayList = displayList;
    }

    public boolean isColorBlindMode() {
        return this.colorBlindMode;
    }

    public void setColorBlindMode(boolean colorBlindMode) {
        this.colorBlindMode = colorBlindMode;
    }

    public int getMoveLeftKey() {
        return this.moveLeftKey;
    }

    public void setMoveLeftKey(int moveLeftKey) {
        this.moveLeftKey = moveLeftKey;
    }

    public int getMoveRightKey() {
        return this.moveRightKey;
    }

    public void setMoveRightKey(int moveRightKey) {
        this.moveRightKey = moveRightKey;
    }

    public int getMoveDownKey() {
        return this.moveDownKey;
    }

    public void setMoveDownKey(int moveDownKey) {
        this.moveDownKey = moveDownKey;
    }

    public int getRotateKey() {
        return this.rotateKey;
    }

    public void setRotateKey(int rotateKey) {
        this.rotateKey = rotateKey;
    }

    public int getStackKey() {
        return this.stackKey;
    }

    public void setStackKey(int stackKey) {
        this.stackKey = stackKey;
    }
}
