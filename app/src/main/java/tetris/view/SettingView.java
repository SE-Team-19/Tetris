package tetris.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

import java.awt.GridLayout;

public class SettingView extends JPanel {

    private JButton returnButton;

    public JButton getReturnButton() {
        return this.returnButton;
    }

    public void setReturnButton(JButton returnButton) {
        this.returnButton = returnButton;
    }

    /* singleton Instance (LazyHolder) */
    private SettingView() {
        initView();
    }

    private static class LazyHolder {
        private static final SettingView INSTANCE = new SettingView();
    }

    public static SettingView getInstance() {
        return LazyHolder.INSTANCE;
    }

    /***************************************/

    private void initView() {
        String[] selections = { "360X230", "700X800", "900X1000" };
        JComboBox<String> comboBox = new JComboBox<>(selections);
        JButton btnNewButton = new JButton("키보드 입력");
        JButton btnScoreInit = new JButton("점수초기화");
        JCheckBox checkBox = new JCheckBox("색맹모드");
        returnButton = new JButton("Return");

        super.setLayout(new GridLayout(5, 0, 0, 0));
        super.add(comboBox);
        super.add(btnNewButton);
        super.add(btnScoreInit);
        super.add(checkBox);
        super.add(returnButton);
    }
}
