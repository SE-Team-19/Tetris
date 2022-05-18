package tetris.controller;

import java.awt.Component;
import java.util.Objects;

// Key 쌍을 위한 클래스
public class KeyPair {

    private final int keyCode;
    private final Component component;

    public KeyPair(int keyCode, Component component) {
        this.keyCode = keyCode;
        this.component = component;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof KeyPair))
            return false;
        KeyPair key = (KeyPair) o;
        return keyCode == key.keyCode && component == key.component;
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyCode, component);
    }
}
