package tetris.model;

import java.io.Serializable;

public class Setting implements Serializable {

  static final long serialVersionUID = 3518731767529258119L;
  private int displayMode;
  private boolean colorBlindMode;
  private int moveLeftKey;
  private int moveRightKey;
  private int rotateKey;
  private int stackKey;

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

  public Setting(int dispalyMode, boolean colorBlindMode, int moveLeftKey, int moveRightKey,
      int rotateKey, int stackKey) {
    this.displayMode = dispalyMode;
    this.colorBlindMode = colorBlindMode;
    this.moveLeftKey = moveLeftKey;
    this.moveRightKey = moveRightKey;
    this.rotateKey = rotateKey;
    this.stackKey = stackKey;
  }
}
