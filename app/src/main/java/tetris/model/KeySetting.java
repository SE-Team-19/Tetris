package tetris.model;

import java.awt.event.KeyEvent;
public class KeySetting {
    // BattleMode Key Setting 을 위한 부분
    // Setting 이랑 합침 고려
    // 현재 사용x. GameController 불러올 때 직접 입력

    private int left1P;
    private int right1P;
    private int down1P;
    private int spaceDown1P;
    private int rotate1P;
    private int left2P;
    private int right2P;
    private int down2P;
    private int rotate2P;
    private int spaceDown2P;
    private int ESC;

    public KeySetting() {
        // PlayerOne(Left Position)
        this.left1P = 65; // 'a'
        this.right1P = 68;  // 'd'
        this.down1P = 83;   // 's'
        this.rotate1P = 87; // 'w'
        this.spaceDown1P = 82; // 'r'

        // PlayerTwo(Right Position)
        this.left2P = 37; // KeyEvent.VK_LEFT;
        this.right2P = 39;
        this.rotate2P = 38;
        this.down2P = 40;
        this.spaceDown2P = 32;

        this.ESC = 27; // escape
    }

    public void setKeySetting(int left1P, int right1P, int down1P, int rotate1P, int spaceDown1P,
                            int left2P, int right2P, int down2P, int rotate2P, int spaceDown2P) {
        this.left1P = left1P;
        this.right1P = right1P;
        this.down1P = down1P;
        this.rotate1P = rotate1P;
        this.spaceDown1P = spaceDown1P;

        this.left2P = left2P;
        this.right2P = right2P;
        this.rotate2P = rotate2P;
        this.down2P = down2P;
        this.spaceDown2P = spaceDown2P;
    }

    public int getLeft1P() {
        return left1P;
    }

    public int getRight1P() {
        return right1P;
    }

    public int getDown1P() {
        return down1P;
    }

    public int getSpaceDown1P() {
        return spaceDown1P;
    }

    public int getRotate1P() {
        return rotate1P;
    }

    public int getLeft2P() {
        return left2P;
    }

    public int getRight2P() {
        return right2P;
    }

    public int getDown2P() {
        return down2P;
    }

    public int getRotate2P() {
        return rotate2P;
    }

    public int getSpaceDown2P() {
        return spaceDown2P;
    }

    public int getESC() {
        return ESC;
    }
}
