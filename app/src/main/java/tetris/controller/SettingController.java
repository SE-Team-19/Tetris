package tetris.controller;

import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.System.Logger;
import java.util.logging.*;

import tetris.model.Setting;

public class SettingController {

    public SettingController() {
        loadSetting();
    }

    private void saveSetting() {
        String fileName = "Settings.dat";
        try (FileOutputStream fos = new FileOutputStream(fileName);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                ObjectOutputStream out = new ObjectOutputStream(bos);) {

            Setting set1 = new Setting(0, false, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP,
                    KeyEvent.VK_SPACE);

            out.writeObject(set1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadSetting() {
        String fileName = "Settings.dat";
        try (FileInputStream fis = new FileInputStream(fileName);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ObjectInputStream in = new ObjectInputStream(bis);) {

            Setting set1 = (Setting) in.readObject();
            System.out.println(set1.getDisplayMode());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
