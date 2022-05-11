package tetris.view;

import javax.swing.*;
import javax.swing.SwingConstants;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class MasterView extends JPanel {

    public static final Color BASIC_FONT_COLOR = Color.WHITE;
    public static final Color FOCUSED_FONT_COLOR = Color.GREEN;
    public static final Color BASIC_BACKGROUND_COLOR = Color.BLACK;
    public static final Color WARNING_FONT_COLOR = Color.RED;
    public static final String BASIC_FONT_FAMILY = "맑은 고딕";
    public static final int BASIC_FONT_SIZE = 20;

    public MasterView() {
        super();
        super.setBackground(BASIC_BACKGROUND_COLOR);
    }

    protected <T extends JComponent> T initAndSetName(String name, T comp) {
        comp.setName(name);
        return comp;
    }

    protected void addAButton(ArrayList<JButton> buttons, Container container) {
        container.setLayout(new GridLayout(buttons.size(), 0, 0, 0));
        for (JButton button : buttons) {
            container.add(button);
        }
    }

    protected void deleteKeyBinding(JComponent... comps) {
        for (JComponent comp : comps)
            comp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");
    }

    /* GridBaglayout에 간편하게 넣기 위한 함수들 (overloading) */
    protected GridBagConstraints addGridBagComponents(int x, int y) {
        GridBagConstraints gridBag = new GridBagConstraints();
        gridBag.insets = new Insets(0, 0, 0, 0);
        gridBag.gridx = x;
        gridBag.gridy = y;
        gridBag.fill = GridBagConstraints.BOTH;
        return gridBag;
    }

    protected void addGridBagComponents(Container pane, JComponent comp, int x, int y) {
        GridBagConstraints gridBag = addGridBagComponents(x, y);
        pane.add(comp, gridBag);
    }

    public class JButton extends javax.swing.JButton {
        public JButton(String label) {
            super(label);
            super.setBackground(BASIC_BACKGROUND_COLOR);
            super.setForeground(BASIC_FONT_COLOR);
            super.setFont(new Font(BASIC_FONT_FAMILY, Font.BOLD, BASIC_FONT_SIZE));
            super.setBorder(BorderFactory.createEmptyBorder());
            super.setFocusPainted(false);
            super.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");
            super.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    e.getComponent().setForeground(FOCUSED_FONT_COLOR);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    e.getComponent().setForeground(BASIC_FONT_COLOR);
                }
            });
        }
    }

    public class JToggleButton extends javax.swing.JToggleButton {
        public JToggleButton(String label) {
            super(label);
            super.setBackground(BASIC_BACKGROUND_COLOR);
            super.setForeground(BASIC_FONT_COLOR);
            super.setFont(new Font(BASIC_FONT_FAMILY, Font.BOLD, BASIC_FONT_SIZE));
            super.setBorder(BorderFactory.createEmptyBorder());
            super.setFocusPainted(false);
            super.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    e.getComponent().setForeground(FOCUSED_FONT_COLOR);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    e.getComponent().setForeground(BASIC_FONT_COLOR);
                }
            });
        }
    }

    public class JLabel extends javax.swing.JLabel {
        public JLabel(String text) {
            super.setText(text);
            super.setHorizontalAlignment(CENTER);
            super.setVerticalAlignment(CENTER);
            super.setBackground(BASIC_BACKGROUND_COLOR);
            super.setForeground(BASIC_FONT_COLOR);
            super.setFont(new Font(BASIC_FONT_FAMILY, Font.BOLD, BASIC_FONT_SIZE));
        }
    }

    public class JPanel extends javax.swing.JPanel {
        public JPanel() {
            super();
            super.setBackground(BASIC_BACKGROUND_COLOR);
        }
    }

}
