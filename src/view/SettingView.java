package Tetris.src.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

import java.awt.Color;
import java.awt.GridLayout;

public class SettingView extends JPanel {
    
    private JButton returnButton;

    public JButton getReturnButton() {
        return this.returnButton;
    }

    public void setReturnButton(JButton returnButton) {
        this.returnButton = returnButton;
    }

    public SettingView(){
        InitView();    
    }

    private void InitView() {

        String[] selections = {"360X230","700X800","900X1000"};
        JComboBox<String> comboBox = new JComboBox<>(selections);	
		
		JButton btnNewButton = new JButton("키보드 입력");
		
		JButton btnScoreInit = new JButton("점수초기화");
		
		JCheckBox checkBox = new JCheckBox("색맹모드");
		
		returnButton = new JButton("Return");

        super.setLayout(new GridLayout(5,0,0,0));
        super.add(comboBox);
        super.add(btnNewButton);
        super.add(btnScoreInit);
        super.add(checkBox);
		super.add(returnButton);
    }
}
