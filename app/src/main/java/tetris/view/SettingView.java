package tetris.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.*;
import javax.swing.plaf.basic.*;

public class SettingView extends MasterView {

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
    private JLabel isColorBlindLabel;
    private JToggleButton setUpKeyBtn;
    private JToggleButton setDownKeyBtn;
    private JToggleButton setLeftKeyBtn;
    private JToggleButton setRightKeyBtn;
    private JToggleButton setStackKeyBtn;
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
        setUpKeyBtn = initAndSetName("setUpKeyBtn", new JToggleButton("UP"));
        setDownKeyBtn = initAndSetName("setDownKeyBtn", new JToggleButton("DOWN"));
        setLeftKeyBtn = initAndSetName("setLeftKeyBtn", new JToggleButton("LEFT"));
        setRightKeyBtn = initAndSetName("setRightKeyBtn", new JToggleButton("RIGHT"));
        setStackKeyBtn = initAndSetName("setStackKeyBtn", new JToggleButton("STACK"));
        initKeyGridReturnBtn = initAndSetName("initKeyGridReturnBtn", new JButton("Return"));


        upKeyLabel = initAndSetName("upKeyLabel", new JLabel("방향키:위"));
        downKeyLabel = initAndSetName("downKeyLabel", new JLabel("방향키:아래"));
        leftKeyLabel = initAndSetName("leftKeyLabel", new JLabel("방향키:왼쪽"));
        rightKeyLabel = initAndSetName("rightKeyLabel", new JLabel("방향키:오른쪽"));
        stackKeyLabel = initAndSetName("stackKeyLabel", new JLabel("스페이스"));

        setKeyMap = new HashMap<>();
        setKeyMap.put(setUpKeyBtn, upKeyLabel);
        setKeyMap.put(setDownKeyBtn, downKeyLabel);
        setKeyMap.put(setLeftKeyBtn, leftKeyLabel);
        setKeyMap.put(setRightKeyBtn, rightKeyLabel);
        setKeyMap.put(setStackKeyBtn, stackKeyLabel);

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

    private void initSetKeyGrid() {
        initKeyGridPane = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        gbl.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0 };
        gbl.rowHeights = new int[] { 0, 0, 0 };
        gbl.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 0.1, Double.MIN_VALUE };
        gbl.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
        initKeyGridPane.setLayout(gbl);
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
                setStackKeyBtn, initKeyGridReturnBtn);
    }

    public void setSettingBtnsFocusable(boolean bool) {
        setFocusableComponents(bool, setDisplayBtn, initKeyBtn, initMenuBtn, isColorBlindBtn,
                returnSettingToMainBtn);
    }

    public void setInitSettingBtnsFocusable(boolean bool) {
        setFocusableComponents(bool, initScoreBtn, initReturnBtn, initSettingBtn);
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

}
