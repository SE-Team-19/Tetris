package tetris.src.model;

public class Setting {
  private int displayMode;
  private boolean colorBlindMode;
  private String moveLeftKey;
  private String moveRightKey;
  private String rotateKey;
  private String stackKey;

  public Setting(int dispalyMode, boolean colorBlindMode, String moveLeftKey, String moveRightKey,
      String rotateKey, String stackKey) {
    this.displayMode = dispalyMode;
    this.moveLeftKey = moveLeftKey;
    this.moveRightKey = moveRightKey;
    this.rotateKey = rotateKey;
    this.stackKey = stackKey;
  }
}
