package tetris.controller;

import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.Timer;

import tetris.model.*;

public class RobotController {

    private static final int DELAY = 100;

    GameController gameController;
    GameController caculator;
    Timer moveRotateTimer;
    Timer moveLeftTimer;
    Timer moveRightTimer;
    Timer testTimer;
    boolean timerFlag;

    Block block;
    int[][] board;
    int moveX = 0;
    int rotateCount = 0;
    int finalGameScore;

    List<Integer> randomBlockList;

    double weightAggHeights = -0.510066;
    double weightCompleteLines = 0.760666;
    double weightHoles = -0.35663;
    double weightBumpiness = -0.184483;
    int[] highestVertical;

    double holes;
    double completeLines;
    double aggHeight;
    double bumpiness;

    RobotController(GameController gameController) {
        caculator = new GameController(new JTextPane(), new JTextPane(), new JTextPane(), null, new JPanel()) {

        };
        this.gameController = gameController;
        this.board = new int[GameController.BOARD_END_HEIGHT][GameController.BOARD_WIDTH];
        this.block = new JBlock();
        caculator.currentBlock = caculator.getBlock(0);
        caculator.blockBuffer = new JBlock();
        this.timerFlag = true;
    }

    void findMove(Block block) {
        double currnetScore;
        double maxScore = Double.MIN_EXPONENT;
        caculator.copyBoard(gameController.board, caculator.board);
        this.block.copyBlock(block);
        caculator.currentBlock.copyBlock(block);
        caculator.blockBuffer.copyBlock(block);
        for (int j = 0; j < 4; j++) {
            caculator.currentBlock.rotate();
            caculator.blockBuffer.rotate();
            for (int i = 0; i < GameController.BOARD_WIDTH - caculator.currentBlock.getWidth() + 1; i++) {
                caculator.x = i;
                caculator.y = GameController.BOARD_START_HEIGHT;
                caculator.getGhostY();
                caculator.placeBlock(caculator.board, caculator.currentBlock, i, caculator.ghostY);
                currnetScore = calculateScore(caculator.board);

                if (currnetScore > maxScore) {
                    maxScore = currnetScore;
                    moveX = i;
                    rotateCount = caculator.currentBlock.getRotateCount();
                }
                caculator.eraseBlock(caculator.board, caculator.currentBlock, i, caculator.ghostY);
            }
        }
    }

    void moveBlock() {
        int currentRotateCount = gameController.currentBlock.getRotateCount();
        int currentX = gameController.x;
        int count = 0;
        for (int i = 0; i < rotateCount - currentRotateCount; i++) {
            startMoveRotateTimer(DELAY * count);
            count++;
        }
        currentX = calculateRotateX(currentX, rotateCount);
        for (int i = 0; i < currentX - moveX; i++) {
            startMoveLeftTimer(DELAY * count);
            count++;
        }
        for (int i = 0; i < moveX - currentX; i++) {
            startMoveRightTimer(DELAY * count);
            count++;
        }
        Timer stackTimer = new Timer(DELAY * count + DELAY, e -> {
            if (timerFlag)
                gameController.dropDown();
        });
        stackTimer.setRepeats(false);
        stackTimer.start();
    }

    private int calculateRotateX(int x, int rotateCount) {
        switch (rotateCount) {
            case Block.SECOND_ROTATE_STATE:
                x++;
                break;
            case Block.IBLOCK_SECOND_ROTATE_STATE:
                x += 2;
                break;
            case Block.IBLOCK_FOURTH_ROTATE_STATE:
                x++;
                break;
            default:
                break;
        }
        return x;
    }

    private void startMoveRotateTimer(int delay) {
        moveRotateTimer = new Timer(delay, e -> {
            if (timerFlag)
                gameController.moveRotate();
        });
        moveRotateTimer.setRepeats(false);
        moveRotateTimer.start();
    }

    private void startMoveLeftTimer(int delay) {
        moveLeftTimer = new Timer(delay, e -> {
            if (timerFlag)
                gameController.moveLeft();
        });
        moveLeftTimer.setRepeats(false);
        moveLeftTimer.start();
    }

    private void startMoveRightTimer(int delay) {
        moveRightTimer = new Timer(delay, e -> {
            if (timerFlag)
                gameController.moveRight();
        });
        moveRightTimer.setRepeats(false);
        moveRightTimer.start();
    }

    double calculateScore(int[][] board) {
        int highestBlock = 0;
        int blockNums = 0;
        holes = 0.0;
        completeLines = 0.0;
        aggHeight = 0.0;
        bumpiness = 0.0;
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

        getCompleteLines(highestBlock);

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

    private double getCompleteLines(int highestBlock) {
        int blockNums = 0;
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
        return completeLines;
    }

    public void stopRobot() {
        timerFlag = false;
    }

    public void startRobot() {
        timerFlag = true;
        findMove(gameController.currentBlock);
        moveBlock();
    }

    public void startTestRobot() {
        timerFlag = true;

        findMove(gameController.currentBlock);
        moveBlock();
    }

}