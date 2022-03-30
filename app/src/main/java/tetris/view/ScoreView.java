package tetris.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

import java.util.ArrayList;

public class ScoreView extends JPanel {

    private JButton returnButton;
    private ArrayList<JLabel> names;

    public JButton getReturnButton() {
        return this.returnButton;
    }

    public void setReturnButton(JButton returnButton) {
        this.returnButton = returnButton;
    }

    private ScoreView() {
        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        returnButton = new JButton("Return");
        names = new ArrayList<>();

        names.add(new JLabel("호호이1"));
        names.add(new JLabel("호호이2"));
        names.add(new JLabel("호호이3"));
        names.add(new JLabel("호호이4"));
        names.add(new JLabel("호호이5"));
        names.add(new JLabel("호호이6"));
        names.add(new JLabel("호호이7"));

        addLabel(names);
    }

    private static class LazyHolder {
        private static final ScoreView INSTANCE = new ScoreView();
    }

    public static ScoreView getInstance() {
        return LazyHolder.INSTANCE;
    }

    /***************************************/

    private void addLabel(ArrayList<JLabel> players) {
        for (JLabel player : players)
            super.add(player);
    }
}
