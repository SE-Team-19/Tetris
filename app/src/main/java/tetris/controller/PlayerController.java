package tetris.controller;

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import tetris.model.Player;

public class PlayerController {

    private List<Player> playerList;

    public PlayerController() {
        playerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            playerList.add(new Player("없음", 0, "없음"));
        }
    }

    public void addPlayer(String name, int score, String difficulty) {
        playerList.add(new Player(name, score, difficulty));
        Collections.sort(playerList, (p1, p2) -> Integer.compare(p1.getScore(), p2.getScore()));

        while (playerList.size() > 10)
            playerList.remove(0);

        while (playerList.size() < 10) {
            playerList.add(new Player("없음", 0, "없음"));
        }
    }

    public void savePlayerList() {
        try (Writer sw = new OutputStreamWriter(new FileOutputStream("src/main/java/tetris/data/Score.json"),
                StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(playerList, sw);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Score.json파일 저장하는데 실패하였습니다.",
                    "Failed to save score.json", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public void loadPlayerList() {
        while (true) {
            try (Reader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream("src/main/java/tetris/data/Score.json"),
                    StandardCharsets.UTF_8))) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                playerList = gson.fromJson(br,
                        TypeToken.getParameterized(ArrayList.class, Player.class).getType());

                while (playerList.size() < 10) {
                    playerList.add(new Player("없음", 0, "없음"));
                }
                break;

            } catch (IOException e) {
                savePlayerList();
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(new JFrame(), "Score.json 파일을 불러오는데 실패하였습니다.",
                        "Failed to load setting.json", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        }
    }

    public List<Player> getPlayerList() {
        return this.playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }
}
