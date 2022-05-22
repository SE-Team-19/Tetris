package tetris.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;
import javax.swing.plaf.basic.*;

public class SettingView extends MasterView {

    private GridBagLayout gridBagLayout;
    private JComboBox<String> displayComboBox;
    private JButton returnSettingToMainBtn;
    private JButton initKeyBtn;
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
    private JLabel stackKeyLabel;
    private JLabel up2KeyLabel;
    private JLabel down2KeyLabel;
    private JLabel left2KeyLabel;
    private JLabel right2KeyLabel;
    private JLabel stack2KeyLabel;
    private JLabel isColorBlindLabel;
    private JToggleButton setUpKeyBtn;
    private JToggleButton setDownKeyBtn;
    private JToggleButton setLeftKeyBtn;
    private JToggleButton setRightKeyBtn;
    private JToggleButton setStackKeyBtn;
    private JToggleButton setUp2KeyBtn;
    private JToggleButton setDown2KeyBtn;
    private JToggleButton setLeft2KeyBtn;
    private JToggleButton setRight2KeyBtn;
    private JToggleButton setStack2KeyBtn;
    private JToggleButton isColorBlindBtn;
    private Map<JToggleButton, JLabel> setKeyMap;
    private JPanel initSettingPane;
    private JPanel initKeyGridPane;
    private String inputMessage;

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

        super.setBackground(Color.BLACK);

        GridBagLayout gridBag = new GridBagLayout();
        gridBag.columnWidths = new int[] { 0, 0, 0 };
        gridBag.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
        gridBag.columnWeights = new double[] { 1.0, 0.2, Double.MIN_VALUE };
        gridBag.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
        super.setLayout(gridBag);
        super.add(setDisplayBtn, addGridBagComponents(1, 0));
        super.add(initKeyBtn, addGridBagComponents(1, 1));
        super.add(initMenuBtn, addGridBagComponents(1, 2));
        super.add(isColorBlindBtn, addGridBagComponents(1, 3));
        super.add(displayComboBox, addGridBagComponents(0, 0));
        super.add(initKeyGridPane, addGridBagComponents(0, 1));
        super.add(initSettingPane, addGridBagComponents(0, 2));
        super.add(isColorBlindLabel, addGridBagComponents(0, 3));

        GridBagConstraints gblReturnMenuBtn = addGridBagComponents(0, 4);
        gblReturnMenuBtn.gridwidth = 2;

        super.add(returnSettingToMainBtn, gblReturnMenuBtn);
    }

    private void initComponent() {
        /* 메뉴키 */
        setDisplayBtn = initAndSetName("setDisplayBtn", new JButton("해상도 설정"));
        initKeyBtn = initAndSetName("initKeyBtn", new JButton("키보드 입력"));
        initMenuBtn = initAndSetName("initMenuBtn", new JButton("점수초기화"));
        isColorBlindBtn = initAndSetName("isColorBlindBtn", new JToggleButton("OFF"));
        returnSettingToMainBtn = initAndSetName("returnSettingToMainBtn", new JButton("메인메뉴로"));

        /* 키 초기화 버튼과 라벨 */
        setUpKeyBtn = initAndSetName("setUpKeyBtn", new JToggleButton("1p UP"));
        setDownKeyBtn = initAndSetName("setDownKeyBtn", new JToggleButton("1p DOWN"));
        setLeftKeyBtn = initAndSetName("setLeftKeyBtn", new JToggleButton("1p LEFT"));
        setRightKeyBtn = initAndSetName("setRightKeyBtn", new JToggleButton("1p RIGHT"));
        setStackKeyBtn = initAndSetName("setStackKeyBtn", new JToggleButton("1p STACK"));

        setUp2KeyBtn = initAndSetName("setUp2KeyBtn", new JToggleButton("2p UP"));
        setDown2KeyBtn = initAndSetName("setDown2KeyBtn", new JToggleButton("2p DOWN"));
        setLeft2KeyBtn = initAndSetName("setLeft2KeyBtn", new JToggleButton("2p LEFT"));
        setRight2KeyBtn = initAndSetName("setRight2KeyBtn", new JToggleButton("2p RIGHT"));
        setStack2KeyBtn = initAndSetName("setStack2KeyBtn", new JToggleButton("2p STACK"));
        initKeyGridReturnBtn = initAndSetName("initKeyGridReturnBtn", new JButton("Return"));

        upKeyLabel = initAndSetName("upKeyLabel", new JLabel("1p 방향키:위"));
        downKeyLabel = initAndSetName("downKeyLabel", new JLabel("1p 방향키:아래"));
        leftKeyLabel = initAndSetName("leftKeyLabel", new JLabel("1p 방향키:왼쪽"));
        rightKeyLabel = initAndSetName("rightKeyLabel", new JLabel("1p 방향키:오른쪽"));
        stackKeyLabel = initAndSetName("stackKeyLabel", new JLabel("1p 스페이스"));

        up2KeyLabel = initAndSetName("up2KeyLabel", new JLabel("2p 방향키:위"));
        down2KeyLabel = initAndSetName("down2KeyLabel", new JLabel("2p 방향키:아래"));
        left2KeyLabel = initAndSetName("left2KeyLabel", new JLabel("2p 방향키:왼쪽"));
        right2KeyLabel = initAndSetName("right2KeyLabel", new JLabel("2p 방향키:오른쪽"));
        stack2KeyLabel = initAndSetName("stack2KeyLabel", new JLabel("2p 스페이스"));

        setKeyMap = new HashMap<>();
        setKeyMap.put(setUpKeyBtn, upKeyLabel);
        setKeyMap.put(setDownKeyBtn, downKeyLabel);
        setKeyMap.put(setLeftKeyBtn, leftKeyLabel);
        setKeyMap.put(setRightKeyBtn, rightKeyLabel);
        setKeyMap.put(setStackKeyBtn, stackKeyLabel);

        setKeyMap.put(setUp2KeyBtn, up2KeyLabel);
        setKeyMap.put(setDown2KeyBtn, down2KeyLabel);
        setKeyMap.put(setLeft2KeyBtn, left2KeyLabel);
        setKeyMap.put(setRight2KeyBtn, right2KeyLabel);
        setKeyMap.put(setStack2KeyBtn, stack2KeyLabel);

        inputMessage = "키를 입력하세요";

        /* 해상도 관련 Combobox */
        displayComboBox = initAndSetName("displayComboBox", new JComboBox<>());
        displayComboBox.setFocusable(false);
        displayComboBox.setPopupVisible(false);
        displayComboBox.setBackground(BASIC_BACKGROUND_COLOR);
        displayComboBox.setForeground(BASIC_FONT_COLOR);
        displayComboBox.setFont(new Font(BASIC_FONT_FAMILY, Font.BOLD, BASIC_FONT_SIZE));
        displayComboBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                return new JButton() {
                    @Override
                    public int getWidth() {
                        return 0;
                    }
                };
            }
        });

        /* JcomboBox의 기본 키 바인딩(아래키)을 삭제하는 메소드 */
        InputMap im = displayComboBox.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke("DOWN"), "none");

        /* 초기화버튼 관련 */
        initScoreBtn = initAndSetName("initScoreBtn", new JButton("점수 초기화"));
        initSettingBtn = initAndSetName("initSettingBtn", new JButton("설정 초기화"));
        initReturnBtn = initAndSetName("initReturnBtn", new JButton("Return"));

        /* 색맹모드 관련 */
        isColorBlindLabel = initAndSetName("isColorBlindLabel", new JLabel("색맹모드"));
    }

    private void setFocusableComponents(boolean bool, JComponent... comp) {
        for (JComponent c : comp) {
            c.setFocusable(bool);
        }
    }

    public GridBagLayout getGridBagLayout() {
        return this.gridBagLayout;
    }

    private void initSetKeyGrid() {
        initKeyGridPane = new JPanel();
        gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 0.1, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE };
        initKeyGridPane.setLayout(gridBagLayout);
        setInitKeyBtnsFocusable(false);

        addGridBagComponents(initKeyGridPane, setUpKeyBtn, 0, 0);
        addGridBagComponents(initKeyGridPane, setDownKeyBtn, 1, 0);
        addGridBagComponents(initKeyGridPane, setLeftKeyBtn, 2, 0);
        addGridBagComponents(initKeyGridPane, setRightKeyBtn, 3, 0);
        addGridBagComponents(initKeyGridPane, setStackKeyBtn, 4, 0);
        addGridBagComponents(initKeyGridPane, upKeyLabel, 0, 1);
        addGridBagComponents(initKeyGridPane, downKeyLabel, 1, 1);
        addGridBagComponents(initKeyGridPane, leftKeyLabel, 2, 1);
        addGridBagComponents(initKeyGridPane, rightKeyLabel, 3, 1);
        addGridBagComponents(initKeyGridPane, stackKeyLabel, 4, 1);

        addGridBagComponents(initKeyGridPane, setUp2KeyBtn, 0, 2);
        addGridBagComponents(initKeyGridPane, setDown2KeyBtn, 1, 2);
        addGridBagComponents(initKeyGridPane, setLeft2KeyBtn, 2, 2);
        addGridBagComponents(initKeyGridPane, setRight2KeyBtn, 3, 2);
        addGridBagComponents(initKeyGridPane, setStack2KeyBtn, 4, 2);
        addGridBagComponents(initKeyGridPane, up2KeyLabel, 0, 3);
        addGridBagComponents(initKeyGridPane, down2KeyLabel, 1, 3);
        addGridBagComponents(initKeyGridPane, left2KeyLabel, 2, 3);
        addGridBagComponents(initKeyGridPane, right2KeyLabel, 3, 3);
        addGridBagComponents(initKeyGridPane, stack2KeyLabel, 4, 3);

        GridBagConstraints gbcInitKeyGridReturnBtn = new GridBagConstraints();
        gbcInitKeyGridReturnBtn.insets = new Insets(0, 0, 0, 0);
        gbcInitKeyGridReturnBtn.gridheight = 4;
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

    public JComboBox<String> getDisplayComboBox() {
        return this.displayComboBox;
    }

    public void setDisplayComboBox(List<Rectangle> displayList, int index) {
        displayList.forEach(x -> displayComboBox.addItem(makeDisplayString(x)));
        displayComboBox.setSelectedIndex(index);
    }

    private String makeDisplayString(Rectangle r) {
        StringBuilder sb = new StringBuilder();
        sb.append((int) r.getWidth());
        sb.append("X");
        sb.append((int) r.getHeight());
        return sb.toString();
    }

    public JButton getReturnSettingToMainBtn() {
        return this.returnSettingToMainBtn;
    }

    public JButton getInitKeyBtn() {
        return this.initKeyBtn;
    }

    public JButton getInitMenuBtn() {
        return this.initMenuBtn;
    }

    public JButton getSetDisplayBtn() {
        return this.setDisplayBtn;
    }

    public JButton getInitKeyGridReturnBtn() {
        return this.initKeyGridReturnBtn;
    }

    public JButton getInitScoreBtn() {
        return this.initScoreBtn;
    }

    public JButton getInitSettingBtn() {
        return this.initSettingBtn;
    }

    public JButton getInitReturnBtn() {
        return this.initReturnBtn;
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

    public JLabel getUp2KeyLabel() {
        return this.up2KeyLabel;
    }

    public JLabel getDown2KeyLabel() {
        return this.down2KeyLabel;
    }

    public JLabel getLeft2KeyLabel() {
        return this.left2KeyLabel;
    }

    public JLabel getRight2KeyLabel() {
        return this.right2KeyLabel;
    }

    public JLabel getStack2KeyLabel() {
        return this.stack2KeyLabel;
    }

    public JLabel getIsColorBlindLabel() {
        return this.isColorBlindLabel;
    }

    public JToggleButton getSetUpKeyBtn() {
        return this.setUpKeyBtn;
    }

    public JToggleButton getSetDownKeyBtn() {
        return this.setDownKeyBtn;
    }

    public JToggleButton getSetLeftKeyBtn() {
        return this.setLeftKeyBtn;
    }

    public JToggleButton getSetRightKeyBtn() {
        return this.setRightKeyBtn;
    }

    public JToggleButton getSetStackKeyBtn() {
        return this.setStackKeyBtn;
    }

    public JToggleButton getSetUp2KeyBtn() {
        return this.setUp2KeyBtn;
    }

    public JToggleButton getSetDown2KeyBtn() {
        return this.setDown2KeyBtn;
    }

    public JToggleButton getSetLeft2KeyBtn() {
        return this.setLeft2KeyBtn;
    }

    public JToggleButton getSetRight2KeyBtn() {
        return this.setRight2KeyBtn;
    }

    public JToggleButton getSetStack2KeyBtn() {
        return this.setStack2KeyBtn;
    }

    public Map<JToggleButton, JLabel> getSetKeyMap() {
        return this.setKeyMap;
    }

    public JToggleButton getIsColorBlindBtn() {
        return this.isColorBlindBtn;
    }

    public JPanel getInitSettingPane() {
        return this.initSettingPane;
    }

    public JPanel getInitKeyGridPane() {
        return this.initKeyGridPane;
    }

    public String getInputMessage() {
        return this.inputMessage;
    }

    public void setInitKeyBtnsFocusable(boolean bool) {
        setFocusableComponents(bool, setUpKeyBtn, setDownKeyBtn, setLeftKeyBtn, setRightKeyBtn,
                setStackKeyBtn, initKeyGridReturnBtn, setUp2KeyBtn, setDown2KeyBtn, setLeft2KeyBtn, setRight2KeyBtn,
                setStack2KeyBtn);
    }

    public void setSettingBtnsFocusable(boolean bool) {
        setFocusableComponents(bool, setDisplayBtn, initKeyBtn, initMenuBtn, isColorBlindBtn,
                returnSettingToMainBtn);
    }

    public void setInitSettingBtnsFocusable(boolean bool) {
        setFocusableComponents(bool, initScoreBtn, initReturnBtn, initSettingBtn);
    }

    public void initKeyLabels(int upKey, int downKey, int leftKey, int rightKey, int stackKey) {
        upKeyLabel.setText(KeyEvent.getKeyText(upKey));
        downKeyLabel.setText(KeyEvent.getKeyText(downKey));
        leftKeyLabel.setText(KeyEvent.getKeyText(leftKey));
        rightKeyLabel.setText(KeyEvent.getKeyText(rightKey));
        stackKeyLabel.setText(KeyEvent.getKeyText(stackKey));
    }

    public void init2KeyLabels(int up2Key, int down2Key, int left2Key, int right2Key, int stack2Key) {
        up2KeyLabel.setText(KeyEvent.getKeyText(up2Key));
        down2KeyLabel.setText(KeyEvent.getKeyText(down2Key));
        left2KeyLabel.setText(KeyEvent.getKeyText(left2Key));
        right2KeyLabel.setText(KeyEvent.getKeyText(right2Key));
        stack2KeyLabel.setText(KeyEvent.getKeyText(stack2Key));
    }

    public void setIsColorBlindBtn(boolean bool) {
        this.isColorBlindBtn.setSelected(bool);
        if (bool)
            this.isColorBlindBtn.setText("ON");
        else
            this.isColorBlindBtn.setText("OFF");
    }

}
