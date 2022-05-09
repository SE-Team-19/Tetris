package tetris.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class SettingView extends JPanel {

    public static final String KEY_CHANGE_MESSAGE = "키를 입력하세요";

    private JComboBox<String> displayModeComboBox;

    private JButton enterDisplayMenuBtn;
    private JButton enterKeyMenuBtn;
    private JButton enterResetMenuBtn;
    private JButton exitKeyMenuBtn;
    private JButton exitResetMenuBtn;
    private JButton resetScoreBtn;
    private JButton resetSettingBtn;
    private JButton exitBtn;

    private JLabel upKeyLabel;
    private JLabel downKeyLabel;
    private JLabel leftKeyLabel;
    private JLabel rightKeyLabel;
    private JLabel stackKeyLabel;
    private JLabel isColorBlindLabel;

    private JToggleButton upKeyBtn;
    private JToggleButton downKeyBtn;
    private JToggleButton leftKeyBtn;
    private JToggleButton rightKeyBtn;
    private JToggleButton stackKeyBtn;
    private JToggleButton isColorBlindBtn;

    private Map<JToggleButton, JLabel> setKeyMap;

    private JPanel keyMenuPanel;
    private JPanel resetMenuPanel;

    private ArrayList<Component> componentList;

    private SettingView() {
        initView();
    }

    private static class LazyHolder {
        private static final SettingView INSTANCE = new SettingView();
    }

    public static SettingView getInstance() {
        return LazyHolder.INSTANCE;
    }

    private void initView() {
        initComponent();
        initSetKeyGrid();
        initSettingGrid();

        GridBagLayout gridBag = new GridBagLayout();
        gridBag.columnWidths = new int[] { 0, 0, 0 };
        gridBag.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
        gridBag.columnWeights = new double[] { 1.0, 0.2, Double.MIN_VALUE };
        gridBag.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
        super.setLayout(gridBag);
        super.add(enterDisplayMenuBtn, addGridBagComponents(1, 0));
        super.add(enterKeyMenuBtn, addGridBagComponents(1, 1));
        super.add(enterResetMenuBtn, addGridBagComponents(1, 2));
        super.add(isColorBlindBtn, addGridBagComponents(1, 3));
        super.add(displayModeComboBox, addGridBagComponents(0, 0));
        super.add(keyMenuPanel, addGridBagComponents(0, 1));
        super.add(resetMenuPanel, addGridBagComponents(0, 2));
        super.add(isColorBlindLabel, addGridBagComponents(0, 3));

        GridBagConstraints gblReturnMenuBtn = addGridBagComponents(0, 4);
        gblReturnMenuBtn.gridwidth = 2;
        super.add(exitBtn, gblReturnMenuBtn);

        componentList = new ArrayList<>(Arrays.asList(
                displayModeComboBox,
                enterDisplayMenuBtn,
                enterKeyMenuBtn,
                enterResetMenuBtn,
                exitKeyMenuBtn,
                exitResetMenuBtn,
                resetScoreBtn,
                resetSettingBtn,
                exitBtn,
                upKeyBtn,
                downKeyBtn,
                leftKeyBtn,
                rightKeyBtn,
                stackKeyBtn,
                isColorBlindBtn,
                keyMenuPanel,
                resetMenuPanel));
    }

    private void initComponent() {
        enterDisplayMenuBtn = initAndSetName("enterDisplayMenuBtn", new JButton("해상도 설정"));
        enterKeyMenuBtn = initAndSetName("enterKeyMenuBtn", new JButton("키 변경"));
        enterResetMenuBtn = initAndSetName("enterResetMenuBtn", new JButton("초기화"));
        isColorBlindBtn = initAndSetName("isColorBlindBtn", new JToggleButton("OFF"));
        exitBtn = initAndSetName("exitBtn", new JButton("나가기"));
        deleteKeyBinding(enterDisplayMenuBtn, enterKeyMenuBtn, enterResetMenuBtn, isColorBlindBtn,
                exitBtn);

        upKeyBtn = initAndSetName("upKeyBtn", new JToggleButton("UP"));
        downKeyBtn = initAndSetName("downKeyBtn", new JToggleButton("DOWN"));
        leftKeyBtn = initAndSetName("leftKeyBtn", new JToggleButton("LEFT"));
        rightKeyBtn = initAndSetName("rightKeyBtn", new JToggleButton("RIGHT"));
        stackKeyBtn = initAndSetName("stackKeyBtn", new JToggleButton("STACK"));
        deleteKeyBinding(upKeyBtn, downKeyBtn, leftKeyBtn, rightKeyBtn, stackKeyBtn);
        exitKeyMenuBtn = initAndSetName("exitKeyMenuBtn", new JButton("Return"));

        upKeyLabel = initAndSetName("upKeyLabel", new JLabel("방향키:위"));
        downKeyLabel = initAndSetName("downKeyLabel", new JLabel("방향키:아래"));
        leftKeyLabel = initAndSetName("leftKeyLabel", new JLabel("방향키:왼쪽"));
        rightKeyLabel = initAndSetName("rightKeyLabel", new JLabel("방향키:오른쪽"));
        stackKeyLabel = initAndSetName("stackKeyLabel", new JLabel("스페이스"));

        setKeyMap = new HashMap<>();
        setKeyMap.put(upKeyBtn, upKeyLabel);
        setKeyMap.put(downKeyBtn, downKeyLabel);
        setKeyMap.put(leftKeyBtn, leftKeyLabel);
        setKeyMap.put(rightKeyBtn, rightKeyLabel);
        setKeyMap.put(stackKeyBtn, stackKeyLabel);

        displayModeComboBox = initAndSetName("displayModeComboBox", new JComboBox<>());
        displayModeComboBox.setFocusable(false);
        displayModeComboBox.setPopupVisible(false);

        InputMap im = displayModeComboBox.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke("DOWN"), "none");

        resetScoreBtn = initAndSetName("resetScoreBtn", new JButton("점수 초기화"));
        resetSettingBtn = initAndSetName("resetSettingBtn", new JButton("설정 초기화"));
        exitResetMenuBtn = initAndSetName("exitResetMenuBtn", new JButton("Return"));

        isColorBlindLabel = initAndSetName("isColorBlindLabel", new JLabel("색맹모드"));
    }

    private void deleteKeyBinding(JComponent... comps) {
        for (JComponent comp : comps)
            comp.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");
    }

    private <T extends JComponent> T initAndSetName(String name, T comp) {
        comp.setName(name);
        return comp;
    }

    private void setFocusableComponents(boolean bool, JComponent... comp) {
        for (JComponent c : comp) {
            c.setFocusable(bool);
        }
    }

    private void addGridBagComponents(Container pane, JComponent comp, int x, int y) {
        GridBagConstraints gridBag = new GridBagConstraints();
        gridBag.insets = new Insets(0, 0, 0, 0);
        gridBag.gridx = x;
        gridBag.gridy = y;
        gridBag.fill = GridBagConstraints.BOTH;
        pane.add(comp, gridBag);
    }

    private GridBagConstraints addGridBagComponents(int x, int y) {
        GridBagConstraints gridBag = new GridBagConstraints();
        gridBag.insets = new Insets(0, 0, 0, 0);
        gridBag.gridx = x;
        gridBag.gridy = y;
        gridBag.fill = GridBagConstraints.BOTH;
        return gridBag;
    }

    private void initSetKeyGrid() {
        keyMenuPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        gbl.rowHeights = new int[] { 0, 0, 0 };
        gbl.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 0.1, Double.MIN_VALUE };
        gbl.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
        keyMenuPanel.setLayout(gbl);
        setInitKeyBtnsFocusable(false);

        addGridBagComponents(keyMenuPanel, upKeyBtn, 0, 0);
        addGridBagComponents(keyMenuPanel, downKeyBtn, 1, 0);
        addGridBagComponents(keyMenuPanel, leftKeyBtn, 2, 0);
        addGridBagComponents(keyMenuPanel, rightKeyBtn, 3, 0);
        addGridBagComponents(keyMenuPanel, stackKeyBtn, 4, 0);
        addGridBagComponents(keyMenuPanel, upKeyLabel, 0, 1);
        addGridBagComponents(keyMenuPanel, downKeyLabel, 1, 1);
        addGridBagComponents(keyMenuPanel, leftKeyLabel, 2, 1);
        addGridBagComponents(keyMenuPanel, rightKeyLabel, 3, 1);
        addGridBagComponents(keyMenuPanel, stackKeyLabel, 4, 1);

        GridBagConstraints gbcExitKeyMenuBtn = new GridBagConstraints();
        gbcExitKeyMenuBtn.insets = new Insets(0, 0, 0, 0);
        gbcExitKeyMenuBtn.gridheight = 2;
        gbcExitKeyMenuBtn.gridx = 5;
        gbcExitKeyMenuBtn.gridy = 0;
        gbcExitKeyMenuBtn.fill = GridBagConstraints.BOTH;
        keyMenuPanel.add(exitKeyMenuBtn, gbcExitKeyMenuBtn);
    }

    private void initSettingGrid() {
        resetMenuPanel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[] { 0, 0, 0, 0 };
        gbl.rowHeights = new int[] { 0, 0 };
        gbl.columnWeights = new double[] { 1.0, 0.5, 1.0, Double.MIN_VALUE };
        gbl.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        resetMenuPanel.setLayout(gbl);
        setResetSettingBtnsFocusable(false);

        addGridBagComponents(resetMenuPanel, resetScoreBtn, 0, 0);
        addGridBagComponents(resetMenuPanel, exitResetMenuBtn, 1, 0);
        addGridBagComponents(resetMenuPanel, resetSettingBtn, 2, 0);
    }

    public JComboBox<String> getDisplayModeComboBox() {
        return this.displayModeComboBox;
    }

    public void setDisplayModeComboBox(int index) {
        displayModeComboBox.addItem("366 X 342");
        displayModeComboBox.addItem("380 X 350");
        displayModeComboBox.addItem("640 X 960");
        displayModeComboBox.setSelectedIndex(index);
    }

    public JButton getExitBtn() {
        return this.exitBtn;
    }

    public JButton getEnterKeyMenuBtn() {
        return this.enterKeyMenuBtn;
    }

    public JButton getEnterResetMenuBtn() {
        return this.enterResetMenuBtn;
    }

    public JButton getEnterDisplayMenuBtn() {
        return this.enterDisplayMenuBtn;
    }

    public JButton getExitKeyMenuBtn() {
        return this.exitKeyMenuBtn;
    }

    public JButton getResetScoreBtn() {
        return this.resetScoreBtn;
    }

    public JButton getResetSettingBtn() {
        return this.resetSettingBtn;
    }

    public JButton getExitResetMenuBtn() {
        return this.exitResetMenuBtn;
    }

    public JLabel getUpKeyLabel() {
        return this.upKeyLabel;
    }

    public JLabel getDownKeyLabel() {
        return this.downKeyLabel;
    }

    public JLabel getLeftKeyLabel() {
        return this.leftKeyLabel;
    }

    public JLabel getRightKeyLabel() {
        return this.rightKeyLabel;
    }

    public JLabel getStackKeyLabel() {
        return this.stackKeyLabel;
    }

    public JLabel getIsColorBlindLabel() {
        return this.isColorBlindLabel;
    }

    public JToggleButton getUpKeyBtn() {
        return this.upKeyBtn;
    }

    public JToggleButton getDownKeyBtn() {
        return this.downKeyBtn;
    }

    public JToggleButton getLeftKeyBtn() {
        return this.leftKeyBtn;
    }

    public JToggleButton getRightKeyBtn() {
        return this.rightKeyBtn;
    }

    public JToggleButton getStackKeyBtn() {
        return this.stackKeyBtn;
    }

    public Map<JToggleButton, JLabel> getSetKeyMap() {
        return this.setKeyMap;
    }

    public JToggleButton getIsColorBlindBtn() {
        return this.isColorBlindBtn;
    }

    public JPanel getResetMenuPanel() {
        return this.resetMenuPanel;
    }

    public JPanel getKeyMenuPanel() {
        return this.keyMenuPanel;
    }

    public void setInitKeyBtnsFocusable(boolean bool) {
        setFocusableComponents(bool, upKeyBtn, downKeyBtn, leftKeyBtn, rightKeyBtn,
                stackKeyBtn, exitKeyMenuBtn);
    }

    public void setSettingBtnsFocusable(boolean bool) {
        setFocusableComponents(bool, enterDisplayMenuBtn, enterKeyMenuBtn, enterResetMenuBtn, isColorBlindBtn,
                exitBtn);
    }

    public void setResetSettingBtnsFocusable(boolean bool) {
        setFocusableComponents(bool, resetScoreBtn, exitResetMenuBtn, resetSettingBtn);
    }

    public void initKetLabels(int upKey, int downKey, int leftKey, int rightKey, int stackKey) {
        upKeyLabel.setText(KeyEvent.getKeyText(upKey));
        downKeyLabel.setText(KeyEvent.getKeyText(downKey));
        leftKeyLabel.setText(KeyEvent.getKeyText(leftKey));
        rightKeyLabel.setText(KeyEvent.getKeyText(rightKey));
        stackKeyLabel.setText(KeyEvent.getKeyText(stackKey));
    }

    public void setIsColorBlindBtn(boolean bool) {
        this.isColorBlindBtn.setSelected(bool);
        if (bool)
            this.isColorBlindBtn.setText("ON");
        else
            this.isColorBlindBtn.setText("OFF");
    }

    public List<Component> getComponentList() {
        return this.componentList;
    }
}
