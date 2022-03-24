package Tetris.src.controller;

import javax.swing.JFrame;
import Tetris.src.view.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewController extends JFrame{

    public ViewController() {
        super.setTitle("Team 19 Tetris");
        super.setSize(400, 800);    // 화면크기(이후 Setting파일에 연결할 필요 있음)

        super.setResizable(false);          // 창의 크기 조정 가능 여부
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // 창을 닫으면 더 이상 실행(run)되지 않는다
        super.setLocationRelativeTo(null); // 어떤 위치에 창이 나타날 것인가를 결정. 현재는 화면 정 중앙
        super.getContentPane().setLayout(null); // Frame의 레이아웃 방식을 설정
    
        super.setVisible(true);
    
        MainView mainView = new MainView();
        GameView gameView = new GameView();
        ScoreView scoreView = new ScoreView();
        SettingView settingView = new SettingView();

        /*각 View의 크기설정구간(mainView는 생성자에서 정의)*/
        gameView.setBounds(0,0,400, 800);
        scoreView.setBounds(0,0,400, 800);
        settingView.setBounds(0,0,400, 800);
        /***********************/

        /*mainView를 제외한 나머지 view를 보이지 않게끔 설정*/
        gameView.setVisible(false);
        scoreView.setVisible(false);
        settingView.setVisible(false);
        /***********************/

        super.getContentPane().add(mainView);
        super.getContentPane().add(gameView);
        super.getContentPane().add(scoreView);
        super.getContentPane().add(settingView);

        mainView.StartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameView.setVisible(true);
                mainView.setVisible(false);

            }
        });

        mainView.SettingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingView.setVisible(true);
                mainView.setVisible(false);
            }
        });

        mainView.ScoreBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoreView.setVisible(true);
                mainView.setVisible(false);

            }
        });
    }
}
