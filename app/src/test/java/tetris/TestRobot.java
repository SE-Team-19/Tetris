package tetris;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.*;
import java.awt.event.*;

import tetris.controller.RobotController;
import tetris.TestAllView;

public class TestRobot extends Robot {

    public int delay;

    public TestRobot() throws AWTException {
        super();
        delay = 100;
    }

    public void pressAndReleaseKeys(int... keyCode) {
        for (int key : keyCode) {
            super.keyPress(key);
            super.delay(delay);
            super.keyRelease(key);
            super.delay(delay);
        }
    }

    public void Click(Component comp) {
        Point p = comp.getLocationOnScreen();
        super.mouseMove((int) p.getX() + 1, (int) p.getY() + 1);
        super.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        super.delay(delay);
        super.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        super.delay(delay);
    }

    public void Click(Component comp, int count) {
        for (int i = 0; i < count; i++)
            Click(comp);
    }

}
