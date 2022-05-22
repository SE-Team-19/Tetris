package tetris.model;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.*;

public class Setting {

    private int displayMode;
    private List<Rectangle> displayList;
    private boolean colorBlindMode;
    private int moveLeftKey;
    private int moveRightKey;
    private int moveDownKey;
    private int rotateKey;
    private int stackKey;
    private int moveLeft2Key;
    private int moveRight2Key;
    private int moveDown2Key;
    private int rotate2Key;
    private int stack2Key;

    public Setting() {
        displayMode = 0;
        displayList = Arrays.asList(new Rectangle(0, 0, 1366, 768),
                new Rectangle(0, 0, 380, 350),
                new Rectangle(0, 0, 640, 960));
        colorBlindMode = false;
        moveLeftKey = KeyEvent.VK_LEFT;
        moveRightKey = KeyEvent.VK_RIGHT;
        moveDownKey = KeyEvent.VK_DOWN;
        rotateKey = KeyEvent.VK_UP;
        stackKey = KeyEvent.VK_SPACE;
        moveLeft2Key = KeyEvent.VK_A;
        moveRight2Key = KeyEvent.VK_D;
        moveDown2Key = KeyEvent.VK_S;
        rotate2Key = KeyEvent.VK_W;
        stack2Key = KeyEvent.VK_R;
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

    public int getMoveLeft2Key() {
        return this.moveLeft2Key;
    }

    public void setMoveLeft2Key(int moveLeft2Key) {
        this.moveLeft2Key = moveLeft2Key;
    }

    public int getMoveRight2Key() {
        return this.moveRight2Key;
    }

    public void setMoveRight2Key(int moveRight2Key) {
        this.moveRight2Key = moveRight2Key;
    }

    public int getMoveDown2Key() {
        return this.moveDown2Key;
    }

    public void setMoveDown2Key(int moveDown2Key) {
        this.moveDown2Key = moveDown2Key;
    }

    public int getRotate2Key() {
        return this.rotate2Key;
    }

    public void setRotate2Key(int rotate2Key) {
        this.rotate2Key = rotate2Key;
    }

    public int getStack2Key() {
        return this.stack2Key;
    }

    public void setStack2Key(int stack2Key) {
        this.stack2Key = stack2Key;
    }
}
