package tetris.view;

import javax.swing.*;

import java.awt.*;

public class SettingView extends JPanel {

    private String[] displayList;
    private JComboBox<String> displayComboBox;
    private JButton returnMenuBtn;
    private JButton initKeyButton;
    private JButton initMenuBtn;
    private JButton setDisplayBtn;
    private JButton initKeyGridReturnBtn;
    private JButton initScoreBtn;
    private JButton initSettingBtn;
    private JButton initReturnBtn;
    private JLabel upKeyLabel;
    private JLabel downKeyLabel;
    private JLabel leftKeyLabel;
    private JLabel rightKeyLabel;
    private JLabel staticKeyLabel;
    private JLabel isColorBlindLabel;
    private JToggleButton setUpkeyBtn;
    private JToggleButton setDownKeyBtn;
    private JToggleButton setLeftKeyBtn;
    private JToggleButton setRightKeyBtn;
    private JToggleButton setStaticKeyBtn;
    private JToggleButton isColorBlindBtn;

    public String[] getDisplayList() {
        return this.displayList;
    }

    public void setDisplayList(String[] displayList) {
        this.displayList = displayList;
    }

    public JComboBox<String> getDisplayComboBox() {
        return this.displayComboBox;
    }

    public void setDisplayComboBox(JComboBox<String> displayComboBox) {
        this.displayComboBox = displayComboBox;
    }

    public JButton getReturnMenuBtn() {
        return this.returnMenuBtn;
    }

    public void setReturnMenuBtn(JButton returnMenuBtn) {
        this.returnMenuBtn = returnMenuBtn;
    }

    public JButton getInitKeyButton() {
        return this.initKeyButton;
    }

    public void setInitKeyButton(JButton initKeyButton) {
        this.initKeyButton = initKeyButton;
    }

    public JButton getInitMenuBtn() {
        return this.initMenuBtn;
    }

    public void setInitMenuBtn(JButton initMenuBtn) {
        this.initMenuBtn = initMenuBtn;
    }

    public JButton getSetDisplayBtn() {
        return this.setDisplayBtn;
    }

    public void setSetDisplayBtn(JButton setDisplayBtn) {
        this.setDisplayBtn = setDisplayBtn;
    }

    public JButton getInitKeyGridReturnBtn() {
        return this.initKeyGridReturnBtn;
    }

    public void setInitKeyGridReturnBtn(JButton initKeyGridReturnBtn) {
        this.initKeyGridReturnBtn = initKeyGridReturnBtn;
    }

    public JButton getInitScoreBtn() {
        return this.initScoreBtn;
    }

    public void setInitScoreBtn(JButton initScoreBtn) {
        this.initScoreBtn = initScoreBtn;
    }

    public JButton getInitSettingBtn() {
        return this.initSettingBtn;
    }

    public void setInitSettingBtn(JButton initSettingBtn) {
        this.initSettingBtn = initSettingBtn;
    }

    public JButton getInitReturnBtn() {
        return this.initReturnBtn;
    }

    public void setInitReturnBtn(JButton initReturnBtn) {
        this.initReturnBtn = initReturnBtn;
    }

    public JLabel getUpKeyLabel() {
        return this.upKeyLabel;
    }

    public void setUpKeyLabel(JLabel upKeyLabel) {
        this.upKeyLabel = upKeyLabel;
    }

    public JLabel getDownKeyLabel() {
        return this.downKeyLabel;
    }

    public void setDownKeyLabel(JLabel downKeyLabel) {
        this.downKeyLabel = downKeyLabel;
    }

    public JLabel getLeftKeyLabel() {
        return this.leftKeyLabel;
    }

    public void setLeftKeyLabel(JLabel leftKeyLabel) {
        this.leftKeyLabel = leftKeyLabel;
    }

    public JLabel getRightKeyLabel() {
        return this.rightKeyLabel;
    }

    public void setRightKeyLabel(JLabel rightKeyLabel) {
        this.rightKeyLabel = rightKeyLabel;
    }

    public JLabel getStaticKeyLabel() {
        return this.staticKeyLabel;
    }

    public void setStaticKeyLabel(JLabel staticKeyLabel) {
        this.staticKeyLabel = staticKeyLabel;
    }

    public JLabel getIsColorBlindLabel() {
        return this.isColorBlindLabel;
    }

    public void setIsColorBlindLabel(JLabel isColorBlindLabel) {
        this.isColorBlindLabel = isColorBlindLabel;
    }

    public JToggleButton getSetUpkeyBtn() {
        return this.setUpkeyBtn;
    }

    public void setSetUpkeyBtn(JToggleButton setUpkeyBtn) {
        this.setUpkeyBtn = setUpkeyBtn;
    }

    public JToggleButton getSetDownKeyBtn() {
        return this.setDownKeyBtn;
    }

    public void setSetDownKeyBtn(JToggleButton setDownKeyBtn) {
        this.setDownKeyBtn = setDownKeyBtn;
    }

    public JToggleButton getSetLeftKeyBtn() {
        return this.setLeftKeyBtn;
    }

    public void setSetLeftKeyBtn(JToggleButton setLeftKeyBtn) {
        this.setLeftKeyBtn = setLeftKeyBtn;
    }

    public JToggleButton getSetRightKeyBtn() {
        return this.setRightKeyBtn;
    }

    public void setSetRightKeyBtn(JToggleButton setRightKeyBtn) {
        this.setRightKeyBtn = setRightKeyBtn;
    }

    public JToggleButton getSetStaticKeyBtn() {
        return this.setStaticKeyBtn;
    }

    public void setSetStaticKeyBtn(JToggleButton setStaticKeyBtn) {
        this.setStaticKeyBtn = setStaticKeyBtn;
    }

    public JToggleButton getIsColorBlindBtn() {
        return this.isColorBlindBtn;
    }

    public void setIsColorBlindBtn(JToggleButton isColorBlindBtn) {
        this.isColorBlindBtn = isColorBlindBtn;
    }

    private JPanel initSettingPane;
    private JPanel initKeyGridPane;

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
        initComponents();
        initSetKeyGrid();
        initSettingGrid();

        GridBagLayout gridBag = new GridBagLayout();
        gridBag.columnWidths = new int[] { 0, 0, 0 };
        gridBag.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
        gridBag.columnWeights = new double[] { 1.0, 0.2, Double.MIN_VALUE };
        gridBag.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
        super.setLayout(gridBag);
        super.add(setDisplayBtn, addGridBagComponents(1, 0));
        super.add(initKeyButton, addGridBagComponents(1, 1));
        super.add(initMenuBtn, addGridBagComponents(1, 2));
        super.add(isColorBlindBtn, addGridBagComponents(1, 3));
        super.add(displayComboBox, addGridBagComponents(0, 0));
        super.add(initKeyGridPane, addGridBagComponents(0, 1));
        super.add(initSettingPane, addGridBagComponents(0, 2));
        super.add(isColorBlindLabel, addGridBagComponents(0, 3));

        GridBagConstraints gblReturnMenuBtn = addGridBagComponents(0, 4);
        gblReturnMenuBtn.gridwidth = 2;
        super.add(returnMenuBtn, gblReturnMenuBtn);
    }

    private void initComponents() {
        /* 메뉴키 */
        setDisplayBtn = new JButton("해상도 설정");
        initKeyButton = new JButton("키보드 입력");
        initMenuBtn = new JButton("점수초기화");
        isColorBlindBtn = new JToggleButton("OFF");
        returnMenuBtn = new JButton("메인메뉴로");

        /* 키 초기화 버튼과 라벨 */
        setUpkeyBtn = new JToggleButton("UP");
        setDownKeyBtn = new JToggleButton("DOWN");
        setLeftKeyBtn = new JToggleButton("LEFT");
        setRightKeyBtn = new JToggleButton("RIGHT");
        setStaticKeyBtn = new JToggleButton("STATIC");
        initKeyGridReturnBtn = new JButton("Return");
        upKeyLabel = new JLabel("방향키:위");
        downKeyLabel = new JLabel("방향키:아래");
        leftKeyLabel = new JLabel("방향키:왼쪽");
        rightKeyLabel = new JLabel("방향키:오른쪽");
        staticKeyLabel = new JLabel("스페이스");

        /* 해상도 관련 Combobox */
        displayList = new String[] { "360X230", "700X800", "900X1000" };
        displayComboBox = new JComboBox<>(displayList);
        displayComboBox.setFocusable(false);
        displayComboBox.setPopupVisible(false);
        /* JcomboBox의 기본 키 바인딩(아래키)을 삭제하는 메소드 */
        InputMap im = displayComboBox.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke("DOWN"), "none");

        /* 초기화버튼 관련 */
        initScoreBtn = new JButton("점수 초기화");
        initSettingBtn = new JButton("설정 초기화");
        initReturnBtn = new JButton("Return");

        /* 색맹모드 관련 */
        isColorBlindLabel = new JLabel("색맹모드");

    }

    private void setFocusableComponents(boolean bool, JComponent... comp) {
        for (JComponent c : comp) {
            c.setFocusable(bool);
        }
    }

    public void setInitKeyBtnsFocusable(boolean bool) {
        setFocusableComponents(bool, setUpkeyBtn, setDownKeyBtn, setLeftKeyBtn, setRightKeyBtn, setStaticKeyBtn,
                initKeyGridReturnBtn);
    }

    public void setSettingBtnsFocusable(boolean bool) {
        setFocusableComponents(bool, setDisplayBtn, initKeyButton, initMenuBtn, isColorBlindBtn, returnMenuBtn);
    }

    public void setInitSettingBtnsFocusable(boolean bool) {
        setFocusableComponents(bool, initScoreBtn, initReturnBtn, initSettingBtn);
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
        initKeyGridPane = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        gbl.rowHeights = new int[] { 0, 0, 0 };
        gbl.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 0.1, Double.MIN_VALUE };
        gbl.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
        initKeyGridPane.setLayout(gbl);
        setInitKeyBtnsFocusable(false);

        addGridBagComponents(initKeyGridPane, setUpkeyBtn, 0, 0);
        addGridBagComponents(initKeyGridPane, setDownKeyBtn, 1, 0);
        addGridBagComponents(initKeyGridPane, setLeftKeyBtn, 2, 0);
        addGridBagComponents(initKeyGridPane, setRightKeyBtn, 3, 0);
        addGridBagComponents(initKeyGridPane, setStaticKeyBtn, 4, 0);
        addGridBagComponents(initKeyGridPane, upKeyLabel, 0, 1);
        addGridBagComponents(initKeyGridPane, downKeyLabel, 1, 1);
        addGridBagComponents(initKeyGridPane, leftKeyLabel, 2, 1);
        addGridBagComponents(initKeyGridPane, rightKeyLabel, 3, 1);
        addGridBagComponents(initKeyGridPane, staticKeyLabel, 4, 1);

        GridBagConstraints gbcInitKeyGridReturnBtn = new GridBagConstraints();
        gbcInitKeyGridReturnBtn.insets = new Insets(0, 0, 0, 0);
        gbcInitKeyGridReturnBtn.gridheight = 2;
        gbcInitKeyGridReturnBtn.gridx = 5;
        gbcInitKeyGridReturnBtn.gridy = 0;
        gbcInitKeyGridReturnBtn.fill = GridBagConstraints.BOTH;
        initKeyGridPane.add(initKeyGridReturnBtn, gbcInitKeyGridReturnBtn);
    }

    private void initSettingGrid() {
        initSettingPane = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[] { 0, 0, 0, 0 };
        gbl.rowHeights = new int[] { 0, 0 };
        gbl.columnWeights = new double[] { 1.0, 0.5, 1.0, Double.MIN_VALUE };
        gbl.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
        initSettingPane.setLayout(gbl);
        setInitSettingBtnsFocusable(false);

        addGridBagComponents(initSettingPane, initScoreBtn, 0, 0);
        addGridBagComponents(initSettingPane, initReturnBtn, 1, 0);
        addGridBagComponents(initSettingPane, initSettingBtn, 2, 0);
    }

}
