package tetris.controller;

import java.util.*;
import java.util.List;
import java.math.BigDecimal;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;

import tetris.model.*;
import tetris.view.MasterView;

public class AIController {

    MultiGameController gameController;
    GameController gamer;
    GameController Calculater;

    Block block;
    int[][] board;
    int moveX = 0;
    int rotateCount = 0;
    int finalGameScore;

    List<Integer> randomBlockList;

    public double weightAggHeights = -0.510066;
    public double weightCompleteLines = 0.760666;
    public double weightHoles = -0.35663;
    public double weightBumpiness = -0.184483;
    public int[] highestVertical;

    AIController(MultiGameController gameController) {
        gamer = new GameController(new JTextPane(), new JTextPane(), null, null, new JPanel()) {

        };
        this.gameController = gameController;
        this.board = new int[GameController.BOARD_END_HEIGHT][GameController.BOARD_WIDTH];
        this.block = new JBlock();
    }

    void findMove() {
        double currnetScore;
        double maxScore = Double.MIN_EXPONENT;
        gamer.copyBoard(gameController.gamePlayer2.board, gamer.board);
        gamer.currentBlock = gamer.getBlock(0);
        gamer.blockBuffer = new JBlock();
        gamer.currentBlock.copyBlock(gameController.gamePlayer2.nextBlock);
        gamer.blockBuffer.copyBlock(gameController.gamePlayer2.nextBlock);
        for (int j = 0; j < 4; j++) {
            gamer.currentBlock.rotate();
            gamer.blockBuffer.rotate();
            for (int i = 0; i < GameController.BOARD_WIDTH - gamer.currentBlock.getWidth() + 1; i++) {
                gamer.x = i;
                gamer.y = GameController.BOARD_START_HEIGHT;
                gamer.getGhostY();
                gamer.placeBlock(gamer.board, gamer.currentBlock, i, gamer.ghostY);
                currnetScore = calculateScore(gamer.board);

                if (currnetScore > maxScore) {

                    maxScore = currnetScore;
                    moveX = i;
                    rotateCount = gamer.currentBlock.getRotateCount();

                }
                // finalScoreList.add(calculateScore(gamer.board));
                gamer.eraseBlock(gamer.board, gamer.currentBlock, i, gamer.ghostY);
            }
        }
        System.out.println("이동해야할 X: " + moveX);
        System.out.println("맞춰야할 rotateCount: " + rotateCount);

    }

    void moveBlock() {
        Timer moveTimer = new Timer(1000, e -> {
            while (gameController.gamePlayer2.currentBlock.getRotateCount() != rotateCount) {
                gameController.gamePlayer2.moveRotate();
                gameController.gamePlayer2.drawGameBoard();
            }
            if (moveX > GameController.START_X) {
                while (moveX != gameController.gamePlayer2.x) {
                    gameController.gamePlayer2.moveRight();
                    gameController.gamePlayer2.drawGameBoard();
                }

            } else if (moveX < GameController.START_X) {
                while (moveX != gameController.gamePlayer2.x) {
                    gameController.gamePlayer2.moveLeft();
                    gameController.gamePlayer2.drawGameBoard();
                }
            }
            System.out.println("실제 이동한 X: " + gameController.gamePlayer2.x);
            System.out.println("실제 rotateCount: " + gameController.gamePlayer2.currentBlock.getRotateCount());
            gameController.gamePlayer2.dropDown();
        });
        moveTimer.setRepeats(false);
        moveTimer.start();

        // drop down

    }

    double calculateScore(int[][] board) {
        int highestBlock = 0;
        int blockNums = 0;
        double holes = 0.0;
        double completeLines = 0.0;
        double aggHeight = 0.0;
        double bumpiness = 0.0;
        highestVertical = new int[GameController.BOARD_WIDTH];

        for (int j = 0; j < GameController.BOARD_WIDTH; j++) {
            for (int i = GameController.BOARD_START_HEIGHT; i < GameController.BOARD_END_HEIGHT; i++) {
                if (board[i][j] > 0) {
                    if (highestBlock < GameController.BOARD_END_HEIGHT - i)
                        highestBlock = GameController.BOARD_END_HEIGHT - i;
                    blockNums++;
                }
            }
            highestVertical[j] = highestBlock;
            holes += highestBlock - blockNums;
            blockNums = 0;
            highestBlock = 0;
        }
        aggHeight = Arrays.stream(highestVertical).sum();
        bumpiness = getRoughness(highestVertical, GameController.BOARD_WIDTH);
        highestBlock = Arrays.stream(highestVertical).max().getAsInt();

        for (int i = GameController.BOARD_END_HEIGHT - highestBlock; i < GameController.BOARD_END_HEIGHT; i++) {
            for (int j = 0; j < GameController.BOARD_WIDTH; j++) {
                if (board[i][j] > 0) {
                    blockNums++;
                }
            }
            if (blockNums > 9)
                completeLines++;
            blockNums = 0;
        }

        // System.out.println(
        // "점수들:\n" + "높이들의합 " + aggHeight + "\n구멍수: " + holes + "\n꽉찬줄수: " +
        // completeLines + "\n높이 차:"
        // + bumpiness);

        return (weightAggHeights * aggHeight) + (weightHoles * holes)
                + (weightCompleteLines + completeLines)
                + (weightBumpiness * bumpiness);
    }

    public void setWeight(double weightAggHeights,
            double weightBumpiness, double weightHoles, double weightCompleteLines) {
        this.weightAggHeights = weightAggHeights;
        this.weightBumpiness = weightBumpiness;
        this.weightHoles = weightHoles;
        this.weightCompleteLines = weightCompleteLines;
    }

    private double getRoughness(int[] a, int length) {
        int roughness = 0;
        for (int x = 0; x < length - 1; x++) {
            roughness += Math.abs(a[x] - a[x + 1]);
        }
        return roughness;
    }

}
