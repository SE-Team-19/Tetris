package tetris.controller;

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import tetris.model.Player;
import tetris.view.ScoreView;

public class PlayerController {

    private List<Player> playerList;

    public PlayerController() {
        playerList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            playerList.add(new Player("없음", 0));
        }
        initView();
    }

    private void initView() {
        loadPlayerList();
        ScoreView scoreView = ScoreView.getInstance();

        playerList.forEach(
                player -> scoreView.addComponent(new JLabel(player.getName() + player.getScore())));
    }

    public void addPlayer(String name, int score) {
        playerList.add(new Player(name, score));
        Collections.sort(playerList, (p1, p2) -> p1.getScore().compareTo(p2.getScore()));

        while (playerList.size() > 10)
            playerList.remove(0);

        while (playerList.size() < 10) {
            playerList.add(new Player("없음", 0));
        }
    }

    public void savePlayerList() {
        try (Writer sw =
                new OutputStreamWriter(new FileOutputStream("src/main/java/tetris/data/Score.json"),
                        StandardCharsets.UTF_8)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(playerList, sw);

        } catch (IOException e) {

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
                    playerList.add(new Player("없음", 0));
                }
                break;

            } catch (FileNotFoundException | NullPointerException e) {
                savePlayerList();
            } catch (IOException e) {

            }
        }
    }
}
