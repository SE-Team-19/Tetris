package tetris.src.controller;

import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.WindowConstants;

import java.awt.Container;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.ContainerEvent;
import java.awt.Rectangle;
import java.awt.GridLayout;

import tetris.src.view.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;

public class ViewController extends JFrame {

    private Container contentPane;
    private MainView mainView;
    private GameView gameView;
    private ScoreView scoreView;
    private SettingView settingView;
    private HashMap<JButton,Container> viewMap;
    private Rectangle screenSize;

    public ViewController() {

        initView();
        revalidate();
        repaint();
        MainKeyEvent mainKeyListener = new MainKeyEvent();

        /* Key Event와 혼재되어있음 정리 필요*/
        mainView.getStartButton().addActionListener(e -> viewTransion(contentPane, gameView, mainView));
        mainView.getSettingButton().addActionListener(e -> viewTransion(contentPane, settingView, mainView));
        mainView.getScoreBoardButton().addActionListener(e -> viewTransion(contentPane, scoreView, mainView));

        mainView.getStartButton().requestFocus();
        mainView.getStartButton().addKeyListener(mainKeyListener);
        mainView.getSettingButton().addKeyListener(mainKeyListener);
        mainView.getScoreBoardButton().addKeyListener(mainKeyListener);
        mainView.getExitButton().addKeyListener(mainKeyListener);

        gameView.getReturnButton().addActionListener(e -> viewTransion(contentPane, mainView, gameView));
        scoreView.getReturnButton().addActionListener(e -> viewTransion(contentPane, mainView, scoreView));
        settingView.getReturnButton().addActionListener(e -> viewTransion(contentPane, mainView, settingView));
    }

    private void initView() {
        
        screenSize = new Rectangle(0,0,1000,600); // 화면크기(이후 Setting파일에 연결할 필요 있음)
        super.setTitle("Team 19 Tetris");
        super.setBounds(screenSize); 

        super.setResizable(false); // 창의 크기 조정 가능 여부
        setDefaultCloseOperation(EXIT_ON_CLOSE); // 창을 닫으면 더 이상 실행(run)되지 않는다
        super.setLocationRelativeTo(null); // 어떤 위치에 창이 나타날 것인가를 결정. 현재는 화면 정 중앙

        super.setVisible(true);

        contentPane = super.getContentPane(); // contentPane 부르기

        mainView = new MainView();
        gameView = new GameView();
        scoreView = new ScoreView();
        settingView = new SettingView();

        // View Mapping
        viewMap = new HashMap<>();
        viewMap.put(mainView.getStartButton(),gameView);
        viewMap.put(mainView.getScoreBoardButton(),scoreView);
        viewMap.put(mainView.getSettingButton(),settingView);

        contentPane.setLayout(new GridLayout(1,0,0,0)); // Frame의 레이아웃 방식을 설정, row 1
        contentPane.add(mainView);
    }

    // 전환함수
    private void viewTransion(Container pane, Container to, Container from) {
        pane.add(to);
        pane.remove(from);
        revalidate(); // component 변화 후 JFrame 새로고침(component 변화 적용) */
        repaint(); // component 변화 후 JFrame 새로고침(component 색 등의 성질 적용) */
    }

    private class MainKeyEvent extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP: e.getComponent().transferFocusBackward();
                break;
                case KeyEvent.VK_DOWN: e.getComponent().transferFocus();
                break;
                case KeyEvent.VK_ENTER: viewTransion(contentPane,viewMap.get(e.getComponent()),mainView);
                break;
                default:
                break;
            }
        }
    }

    private class gameKeyEvent extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP: e.getComponent().transferFocusBackward();
                break;
                case KeyEvent.VK_DOWN: e.getComponent().transferFocus();
                break;
                case KeyEvent.VK_ENTER: viewTransion(contentPane,viewMap.get(e.getComponent()),mainView);
                break;
                default:
                break;
            }
        }
    }

}
