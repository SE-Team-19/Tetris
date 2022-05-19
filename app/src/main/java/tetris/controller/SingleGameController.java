package tetris.controller;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

import tetris.model.*;
import tetris.view.*;

public class SingleGameController {

    private GameView gameView;
    private ScoreView scoreView;

    JTextPane gamepane;

    GameController gamePlayer;
    private PlayerController playerController;

    private int diffMode;
    private int gameMode;
    private List<Integer> randomBlockList;

    public SingleGameController(PlayerController playerController) {
        this.playerController = playerController;
        this.gameView = GameView.getInstance();
        scoreView = ScoreView.getInstance();
        gamepane = gameView.getPlayerOneGameBoardPane();
        JTextPane nextBlockPane = gameView.getPlayerOneNextBlockPane();
        JTextPane attackLinePane = gameView.getPlayerOneAttackLinePane();
        JLabel scoreLabel = gameView.getPlayerOneScoreLabel();
        gamePlayer = new GameController(gamepane, nextBlockPane, attackLinePane, scoreLabel,
                gamepane) {
            @Override
            void doAfterGameOver() {
                gameView.add(gameView.getGameOverPanel());
                gameView.remove(gameView.getSingleGameDisplayPane());
                gameView.getInputName().requestFocus();
                gamePlayer.endGame();
            }

            @Override
            void doAfterArriveBottom() {
                if (blockDeque.size() < 3) {
                    generateBlockRandomizer(diffMode);
                    blockDeque.addAll(randomBlockList);
                }
            }
        };

    }

    void startSingleGame(Setting setting) {
        gamePlayer.setPlayerKeys(setting.getRotateKey(), setting.getMoveDownKey(), setting.getMoveLeftKey(),
                setting.getMoveRightKey(), setting.getStackKey());
        generateBlockRandomizer(diffMode);
        gamePlayer.startGame(diffMode, gameMode, randomBlockList);
        gamepane.requestFocus(true);
    }

    void setDiffMode(int diffMode) {
        this.diffMode = diffMode;
    }

    void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    // 유저 저장 메소드
    void saveUserName() {
        String difficulty = "normal";
        if (diffMode == GameController.EASY_MODE)
            difficulty = "easy";
        else if (diffMode == GameController.HARD_MODE)
            difficulty = "hard";
        String userName = gameView.getInputName().getText();
        playerController.addPlayer(userName, gamePlayer.score, difficulty);
        System.out.println("선수의 점수: " + gamePlayer.score);
        playerController.savePlayerList();
        playerController.loadPlayerList();
        scoreView.resetRankingPane();
        scoreView.resetRankingList();
        playerController.getPlayerList()
                .forEach(player -> System.out.println(player.getName() + player.getScore() + player.getDifficulty()));

        playerController.getPlayerList()
                .forEach(player -> scoreView.addRankingList(new ArrayList<>(Arrays.asList(player.getName(),
                        Integer.toString(player.getScore()), player.getDifficulty()))));
        scoreView.fillScoreBoard(userName);
        gameView.resetGameView();
    }

    void generateBlockRandomizer(int mode) {
        int jBlock = Block.JBLOCK_IDENTIFY_NUMBER;
        int lBlock = Block.LBLOCK_IDENTIFY_NUMBER;
        int zBlock = Block.ZBLOCK_IDENTIFY_NUMBER;
        int sBlock = Block.SBLOCK_IDENTIFY_NUMBER;
        int tBlock = Block.TBLOCK_IDENTIFY_NUMBER;
        int oBlock = Block.OBLOCK_IDENTIFY_NUMBER;
        int iBlock = Block.IBLOCK_IDENTIFY_NUMBER;

        randomBlockList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            List<Integer> blockList = Arrays.asList(jBlock, lBlock, zBlock, sBlock, tBlock, oBlock, iBlock);
            Collections.shuffle(blockList);
            randomBlockList.addAll(blockList);
        }

        // easy mode
        if (mode == 1) {
            List<Integer> blockList = Arrays.asList(jBlock, lBlock, zBlock, sBlock, tBlock, oBlock, iBlock);
            Collections.shuffle(blockList);
            randomBlockList.addAll(blockList);
            randomBlockList.add(iBlock);
        }

        // hard mode
        else if (mode == 2) {
            List<Integer> blockList = Arrays.asList(jBlock, lBlock, zBlock, sBlock, tBlock, oBlock);
            Collections.shuffle(blockList);
            randomBlockList.addAll(blockList);
        }
    }

}
