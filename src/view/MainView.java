package Tetris.src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainView extends JPanel {

    //private JTextArea textArea;
    private JPanel buttonPanel;

    /* 기능 구현을 위해 private->public 변경 수정 필요*/
    public JButton StartButton;
    public JButton SettingButton;
    public JButton ScoreBoardButton;
    public JButton ExitButton;

    public MainView() {
        //ExitView exitView = new ExitView();

        super.setBounds(0,0,400, 400);   // mainView panel의 크기 설정
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

//        ExitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                exitView.setVisible(true);
//
//            }
//        });
    }
}
