package tetris.controller;

import java.awt.Rectangle;
import java.io.*;
import java.nio.charset.StandardCharsets;
import com.google.gson.*;

import tetris.model.Setting;

public class SettingController {

    private Setting setting;

    public SettingController() {
        setting = new Setting(0, false, 0xE2, 0xE3, 0xE0, 0x20);
        loadSetting();
    }

    public Rectangle getScreenSize() {
        int displayMode = setting.getDisplayMode();
        switch (displayMode) {
            case 0:
                return new Rectangle(0, 0, 360, 230);

            case 1:
                return new Rectangle(0, 0, 700, 800);

            case 2:
                return new Rectangle(0, 0, 900, 1000);

            default:
                return new Rectangle(0, 0, 360, 230);
        }
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

    public int getMoveLeftKey() {
        return setting.getMoveLeftKey();
    }

    public void setMoveLeftKey(int moveLeftKey) {
        setting.setMoveLeftKey(moveLeftKey);
    }

    public int getMoveRightKey() {
        return setting.getMoveRightKey();
    }

    public void setMoveRightKey(int moveRightKey) {
        setting.setMoveRightKey(moveRightKey);
    }

    public int getRotateKey() {
        return setting.getRotateKey();
    }

    public void setRotateKey(int rotateKey) {
        setting.setRotateKey(rotateKey);
    }

    public int getStackKey() {
        return setting.getStackKey();
    }

    public void setStackKey(int stackKey) {
        setting.setStackKey(stackKey);
    }

    public void saveSetting() {
        try (Writer sw = new OutputStreamWriter(
                new FileOutputStream("src/main/java/tetris/data/Setting.json"),
                StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(setting, sw);

        } catch (IOException e) {

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

            }
        }
    }
}
