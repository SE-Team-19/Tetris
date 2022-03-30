package tetris.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JFrame {

    // private JTextArea textArea;
    private JPanel buttonPanel;

    private JButton StartButton;
    private JButton SettingButton;
    private JButton ScoreBoardButton;
    private JButton ExitButton;

    public MainView() {

        super.setTitle("Team 19 Tetris");
        super.setSize(400, 800);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창을 닫으면 더 이상 실행(run)되지 않는다
        super.setLocationRelativeTo(null); // 어떤 위치에 창이 나타날 것인가를 결정. 현재는 화면 정 중앙

        super.setVisible(true);

        GameView gameView = new GameView();
        ScoreView scoreView = new ScoreView();
        SettingView settingView = new SettingView();
        // ExitView exitView = new ExitView();

        buttonPanel = new JPanel();
        StartButton = new JButton("Game Start");
        SettingButton = new JButton("Settings");
        ScoreBoardButton = new JButton("Scoreboard");
        ExitButton = new JButton("Exit");

        buttonPanel.add(StartButton);
        buttonPanel.add(SettingButton);
        buttonPanel.add(ScoreBoardButton);
        buttonPanel.add(ExitButton);

        super.add(buttonPanel);



        StartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameView.setVisible(true);

            }
        });

        SettingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingView.setVisible(true);

            }
        });

        ScoreBoardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scoreView.setVisible(true);

            }
        });

        // ExitButton.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // exitView.setVisible(true);
        //
        // }
        // });


    }


}
