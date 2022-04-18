package tetris.view;

import javax.swing.*;
import java.util.ArrayList;

public class ScoreView extends JPanel {

    private JButton returnScoreToMainBtn;
    private ArrayList<JLabel> names;

    public JButton getReturnScoreToMainBtn() {
        return this.returnScoreToMainBtn;
    }

    private ScoreView() {
        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        returnScoreToMainBtn = initAndSetName("returnScoreToMainBtn", new JButton("Return"));
        names = new ArrayList<>();

        names.add(new JLabel("호호이1"));
        names.add(new JLabel("호호이2"));
        names.add(new JLabel("호호이3"));
        names.add(new JLabel("호호이4"));
        names.add(new JLabel("호호이5"));
        names.add(new JLabel("호호이6"));
        names.add(new JLabel("호호이7"));

        addLabel(names);
        super.add(returnScoreToMainBtn);
    }

    private static class LazyHolder {
        private static final ScoreView INSTANCE = new ScoreView();
    }

    public static ScoreView getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void addLabel(ArrayList<JLabel> players) {
        for (JLabel player : players)
            super.add(player);
    }

    public void addComponent(JComponent comp) {
        super.add(comp);
    }

    private <T extends JComponent> T initAndSetName(String name, T comp) {
        comp.setName(name);
        return comp;
    }

}
