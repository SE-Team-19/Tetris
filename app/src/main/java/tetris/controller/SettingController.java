package tetris.controller;

import java.awt.Rectangle;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.awt.event.KeyEvent;
import javax.swing.*;
import com.google.gson.*;

import tetris.model.Setting;

public class SettingController {

    private Setting setting;

    public SettingController() {
        initSetting();
        loadSetting();
    }

    private void initSetting() {
        setting = new Setting(
                0,
                false,
                KeyEvent.VK_LEFT,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_UP, KeyEvent.VK_SPACE);
    }

    public void resetSetting() {
        initSetting();
        loadSetting();
    }

    public void saveSetting() {
        try (Writer sw = new OutputStreamWriter(
                new FileOutputStream("src/main/java/tetris/data/Setting.json"),
                StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(setting, sw);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Setting.json 파일을 저장하는데 실패하였습니다.",
                    "Failed to save Setting.json", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public void loadSetting() {
        while (true) {
            try (Reader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream("src/main/java/tetris/data/Setting.json"),
                    StandardCharsets.UTF_8))) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                setting = gson.fromJson(br, Setting.class);
                break;

            } catch (IOException e) {
                saveSetting();
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(new JFrame(), "Setting.json 파일을 불러오는데 실패하였습니다.",
                        "Failed to load Setting.json", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    public Setting getSetting() {
        return setting;
    }

    public Rectangle getDisplaySize() {
        switch (getDisplayMode()) {
            case 0:
                //return new Rectangle(0, 0, 366, 342);
                return new Rectangle(0, 0, 1280, 960);
            case 1:
                //return new Rectangle(0, 0, 380, 350);
                return new Rectangle(0, 0, 1600, 900);
            case 2:
            default:
                //return new Rectangle(0, 0, 640, 960);
                return new Rectangle(0, 0, 800, 600);
        }
    }

    public int getDisplayMode() {
        return setting.getDisplayMode();
    }

    public void setDisplayMode(int displayMode) {
        setting.setDisplayMode(displayMode);
    }

    public boolean isColorBlindMode() {
        return setting.isColorBlindMode();
    }

    public void setColorBlindMode(boolean colorBlindMode) {
        setting.setColorBlindMode(colorBlindMode);
    }

    public int getLeftKey() {
        return setting.getLeftKey();
    }

    public void setLeftKey(int leftKey) {
        setting.setLeftKey(leftKey);
        saveSetting();
    }

    public int getRightKey() {
        return setting.getRightKey();
    }

    public void setRightKey(int downKey) {
        setting.setRightKey(downKey);
        saveSetting();
        loadSetting();
    }

    public int getDownKey() {
        return setting.getDownKey();
    }

    public void setDownKey(int downKey) {
        setting.setDownKey(downKey);
        saveSetting();
        loadSetting();
    }

    public int getRotateKey() {
        return setting.getRotateKey();
    }

    public void setRotateKey(int rotateKey) {
        setting.setRotateKey(rotateKey);
        saveSetting();
        loadSetting();
    }

    public int getStackKey() {
        return setting.getStackKey();
    }

    public void setStackKey(int stackKey) {
        setting.setStackKey(stackKey);
        saveSetting();
        loadSetting();
    }
}
