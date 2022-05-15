package tetris.controller;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.awt.Rectangle;
import javax.swing.*;
import java.util.*;

import com.google.gson.*;

import tetris.model.Setting;

public class SettingController {

    private Setting setting;

    public SettingController() {
        initSetting();
        loadSetting();
    }

    private void initSetting() {
        List<Rectangle> displayList = new ArrayList<>();
        displayList.add(new Rectangle(0, 0, 1366, 768));
        displayList.add(new Rectangle(0, 0, 380, 350));
        displayList.add(new Rectangle(0, 0, 640, 960));
        setting = new Setting();
        setting.setDisplayList(displayList);

    }

    public void resetSetting() {
        initSetting();
        saveSetting();
    }

    public Rectangle getScreenSize() {
        int displayMode = setting.getDisplayMode();
        List<Rectangle> displayList = setting.getDisplayList();
        if (!displayList.get(displayMode).isEmpty()) {
            return displayList.get(displayMode);
        } else if (!displayList.get(0).isEmpty()) {
            return displayList.get(0);
        } else {
            return new Rectangle(0, 0, 360, 240);
        }
    }

    public void saveSetting() {
        try (Writer sw = new OutputStreamWriter(
                new FileOutputStream("src/main/java/tetris/data/Setting.json"),
                StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(setting, sw);

        } catch (IOException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(new JFrame(), "setting.json파일을 저장하는데 실패하였습니다.",
                    "File cannot save error", JOptionPane.ERROR_MESSAGE);
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

            } catch (FileNotFoundException | NullPointerException e) {
                saveSetting();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(new JFrame(), "setting.json파일을 불러오는데 실패하였습니다.",
                        "File Not Found error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    public Setting getSetting() {
        return setting;
    }

    public void setDisplayMode(int displayMode) {
        setting.setDisplayMode(displayMode);
    }

    public int getDisplayMode() {
        return setting.getDisplayMode();
    }

    public List<Rectangle> getDisplayList() {
        return setting.getDisplayList();
    }

    public boolean isColorBlindMode() {
        return setting.isColorBlindMode();
    }

    public void setColorBlindMode(boolean colorBlindMode) {
        setting.setColorBlindMode(colorBlindMode);
    }

    public int getMoveLeftKey() {
        return setting.getMoveLeftKey();
    }

    public void setMoveLeftKey(int moveLeftKey) {
        setting.setMoveLeftKey(moveLeftKey);
        saveSetting();
        loadSetting();
    }

    public int getMoveRightKey() {
        return setting.getMoveRightKey();
    }

    public void setMoveRightKey(int moveDownKey) {
        setting.setMoveRightKey(moveDownKey);
        saveSetting();
        loadSetting();
    }

    public int getMoveDownKey() {
        return setting.getMoveDownKey();
    }

    public void setMoveDownKey(int moveDownKey) {
        setting.setMoveDownKey(moveDownKey);
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

    public int getMoveLeft2Key() {
        return setting.getMoveLeft2Key();
    }

    public void setMoveLeft2Key(int moveLeft2Key) {
        setting.setMoveLeft2Key(moveLeft2Key);
        saveSetting();
        loadSetting();
    }

    public int getMoveRight2Key() {
        return setting.getMoveRight2Key();
    }

    public void setMoveRight2Key(int moveDown2Key) {
        setting.setMoveRight2Key(moveDown2Key);
        saveSetting();
        loadSetting();
    }

    public int getMoveDown2Key() {
        return setting.getMoveDown2Key();
    }

    public void setMoveDown2Key(int moveDown2Key) {
        setting.setMoveDown2Key(moveDown2Key);
        saveSetting();
        loadSetting();
    }

    public int getRotate2Key() {
        return setting.getRotate2Key();
    }

    public void setRotate2Key(int rotate2Key) {
        setting.setRotate2Key(rotate2Key);
        saveSetting();
        loadSetting();
    }

    public int getStack2Key() {
        return setting.getStack2Key();
    }

    public void setStack2Key(int stack2Key) {
        setting.setStack2Key(stack2Key);
        saveSetting();
        loadSetting();
    }
}
