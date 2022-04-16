package tetris.controller;

import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;

import tetris.model.*;
import tetris.view.*;

public class GameController {

    //GameView gameView = GameView.getInstance();    // instance를 한 번 호출한다.

    GameBoard gameBoard;
    public static int count = 1;

    private JTextPane gamePane;

    public GameController() {
        //initBoard();
        GameView gameView = GameView.getInstance();
    }

    /*
    public void initBoard() {
        // gameView.initGameView() 이 부분을 굳이 선언할 필요가 없다. 어차피 위에서 instance를 한 번 호출해옴.
        // 이렇게 되면 instance를 두번 호출, 즉 중복해서 호출한다.
    }
    */
}
