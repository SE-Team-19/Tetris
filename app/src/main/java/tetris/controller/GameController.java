package tetris.controller;

import tetris.model.*;
import tetris.controller.*;
import tetris.view.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class GameController extends Thread implements KeyListener{

    private GameView gameView;
    private GameBoard gameBoard;

    private Timer timer;
    private static final int initInterval = 1000;   // 단위: milliseconds

    public GameController(GameView gameView, GameBoard gameboard) {
        this.gameView = gameView;
        this.gameBoard = gameboard;


        timer = new Timer(initInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //moveDown();
                //drawBoard();
            }
        });

        timer.start();
    }




    @Override
    public void run() {
        // 게임이 실행되는 메소드
    }


    // 아래는 KeyListener 에 대한 구현
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        // 게임 방향키 조절

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    // 위 방향키 눌림
                gameBoard.blockRotate();
                break;

            case KeyEvent.VK_DOWN:  // 아래 방향키 눌림
                gameBoard.blockMoveDown();
                break;

            case KeyEvent.VK_RIGHT: // 오른쪽 방향키 눌림
                gameBoard.blockMoveRight();
                break;

            case KeyEvent.VK_LEFT:  // 왼쪽 방향키 눌림
                gameBoard.blockMoveLeft();
                break;

            case KeyEvent.VK_SPACE: // space 바 눌림 -> 한번에 끝까지 밑으로 내리는 방향키 구현을 위해
                gameBoard.blockDrop();
                break;

            default:
                break;
        }
    }

}
