package tetris.model;

public class Setting {

    private int displayMode;
    private boolean colorBlindMode;
    private int leftKey;
    private int rightKey;
    private int downKey;
    private int rotateKey;
    private int stackKey;

    public Setting(int displayMode, boolean colorBlindMode, int leftKey, int rightKey,
            int downKey, int rotateKey, int stackKey) {
        this.displayMode = displayMode;
        this.colorBlindMode = colorBlindMode;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.downKey = downKey;
        this.rotateKey = rotateKey;
        this.stackKey = stackKey;
    }

    public int getDisplayMode() {
        return this.displayMode;
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
    }

    public boolean isColorBlindMode() {
        return this.colorBlindMode;
    }

    public void setColorBlindMode(boolean colorBlindMode) {
        this.colorBlindMode = colorBlindMode;
    }

    public int getLeftKey() {
        return this.leftKey;
    }

    public void setLeftKey(int leftKey) {
        this.leftKey = leftKey;
    }

    public int getRightKey() {
        return this.rightKey;
    }

    public void setRightKey(int rightKey) {
        this.rightKey = rightKey;
    }

    public int getDownKey() {
        return this.downKey;
    }

    public void setdownKey(int downKey) {
        this.downKey = downKey;
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
