package tetris.controller;

import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.CompoundBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

import tetris.model.*;
import tetris.view.*;


public class GameController extends JPanel {

    GameView gameView = GameView.getInstance();
    BlockController blockController;

    GameBoard gameBoard;
    JPanel nextBlockBoard;  // 다음 블럭을 띄우는 것이므로 굳이 GameBoard로 선언할 필요가 없다.
    public static int count = 1;

    public GameController() {

        initBoard();
        //blockController = new BlockController();
        drawGameBoard(this.gameBoard);


    }

    public void initBoard() {
        gameBoard = new GameBoard();
    }


    public void drawGameBoard(GameBoard gameBoard1) {
        StringBuilder sb = new StringBuilder();
        for (int t = 0; t < GameBoard.GAME_WIDTH; t++) {
            sb.append(GameBoard.BORDER_CHAR);
        }
        sb.append("\n");
        for (int i = 0; i < gameBoard1.getHeight(); i++) {
            sb.append(GameBoard.BORDER_CHAR);
            for (int j = 0; j < gameBoard1.getWidth(); j++) {
                if (gameBoard1.getBoard()[i][j] == 1) {
                    sb.append(GameBoard.BORDER_CHAR);
                } else {
                    sb.append(" ");
                }
            }
            sb.append(GameBoard.BORDER_CHAR);
            sb.append("\n");
        }
        for (int t = 0; t < gameBoard1.getWidth() + 2; t++) {
            sb.append(GameBoard.BORDER_CHAR);
        }
        gameView.getGamePane().setText(sb.toString());
        StyledDocument doc = gameView.getGamePane().getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), gameBoard1.getStyleSet(), false);

        /*
        SimpleAttributeSet styleSet = new SimpleAttributeSet();
        StyleConstants.setFontFamily(styleSet, Font.SANS_SERIF);
        StyleConstants.setFontSize(styleSet, 20);
        StyleConstants.setBold(styleSet, true);
        StyleConstants.setForeground(styleSet, Color.WHITE);
        StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), styleSet, false);

        //setParagraphAttributes(int offset, int length, AttributeSet s, boolean replace); */

        gameView.getGamePane().setStyledDocument(doc);
    }

    public void drawNextBlockBoard(JPanel jPanel) {

    }



    // Block에 관한 모든 메소드들을 여기에 정의한다.
    private static class BlockController {
        private Block currentBlock;

        public BlockController() {
            //currentBlock = new Block();
            currentBlock = getRandomBlock();
        }

        /*
        블록 클래스 선언
         */
        static class IBlock extends Block {
            public IBlock() {
                shape = new int[][]{{1, 1, 1, 1}};
                color = Color.RED;
            }
        }

        static class JBlock extends Block {
            public JBlock() {
                shape = new int[][]{{0, 1}, {0, 1}, {1, 1},};
                color = Color.ORANGE;
            }
        }

        static class LBlock extends Block{
            public LBlock() {
                shape = new int[][]{{1, 0}, {1, 0}, {1, 1},};
                color = Color.BLUE;
            }
        }

        static class OBlock extends Block {
            public OBlock() {
                shape = new int[][]{{1, 1}, {1, 1}};
                color = Color.YELLOW;
            }
        }

        static class SBlock extends Block {
            public SBlock() {
                shape = new int[][]{{0, 1, 1}, {1, 1, 0}};
                color = Color.MAGENTA;
            }
        }

        static class TBlock extends Block {
            public TBlock() {
                shape = new int[][]{{0, 1, 0}, {1, 1, 1},};
                color = Color.CYAN;
            }
        }

        static class ZBlock extends Block {
            public ZBlock() {
                shape = new int[][]{{1, 1, 0}, {0, 1, 1}};
                color = Color.GREEN;
            }
        }

        /*
        랜덤으로 블록을 받아오는 메소드
        */
        public Block getRandomBlock() {
            long seed = System.currentTimeMillis();
            Random random = new Random(seed);

            int block = random.nextInt(7);        // 7개의 블럭을 동일한 확률로 불러온다: 0이상 6이하
            //count++;    // 개수를 하나 증가시킨다.


            // enhanced switch
            switch (block) {
                case 0 -> {
                    currentBlock = new IBlock();
                    return currentBlock;
                }
                case 1 -> {
                    currentBlock = new JBlock();
                    return currentBlock;
                }
                case 2 -> {
                    currentBlock = new LBlock();
                    return currentBlock;
                }
                case 3 -> {
                    currentBlock = new ZBlock();
                    return currentBlock;
                }
                case 4 -> {
                    currentBlock = new SBlock();
                    return currentBlock;
                }
                case 5 -> {
                    currentBlock = new TBlock();
                    return currentBlock;
                }
                case 6 -> {
                    currentBlock = new OBlock();
                    return currentBlock;
                }
            }
            currentBlock = new IBlock();
            return new IBlock();
        }

        /*
        블록의 위치와 관련된 기본 메소드
         */

        public void moveDown() {
            currentBlock.y += 1;
        }

        public void moveLeft() {
            currentBlock.x -= 1;
        }

        public void moveRight() {
            currentBlock.x += 1;
        }

        // 바닥에 닿았는지 확인할 수 있는 메소드
        public int getBottomLocation() {
            return currentBlock.getY() + currentBlock.getHeight();
        }

        // 오른족에 닿았는지 확인할 수 있는 메소드
        public int getRightLocation() {
            return currentBlock.getX() + currentBlock.getWidth();
        }

        // 왼쪽에 닿았는지 확인할 수 있는 메소드 : getX와 동일
        public int getLeftLocation() {
            return currentBlock.getX();
        }

        // 블록을 회전시키는 메소드: 90도, 시계반대방향
        // 정상작동 여부 test 해봐야함(확실하지 않음)
//        public int[][] rotate() {
//            int height = shape.length;
//            int width = shape[0].length;
//
//            int [][] current2 = new int[width][height];
//
//            int [][] rotateBlock = new int[width][height];
//            for (int i = 0; i < height; i++) {
//                for (int j = 0; j < width; j++) {
//                    rotateBlock[j][height - 1 - i ] = current2[i][j];
//                }
//            }
//            return rotateBlock;
//        }






    }







    /*
    private GameView gameView;
    private GameBoard gameBoard;

    private Timer timer;
    private static final int initInterval = 1000;   // 단위: milliseconds

    public GameController(GameView gameView, GameBoard gameboard) {
        this.gameView = gameView;
        this.gameBoard = gameboard;


        timer = new Timer(initInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //moveDown();
                //drawBoard();
            }
        });

        timer.start();
    }




    @Override
    public void run() {
        // 게임이 실행되는 메소드
    }


    // 아래는 KeyListener 에 대한 구현
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        // 게임 방향키 조절

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:    // 위 방향키 눌림
                gameBoard.blockRotate();
                break;

            case KeyEvent.VK_DOWN:  // 아래 방향키 눌림
                gameBoard.blockMoveDown();
                break;

            case KeyEvent.VK_RIGHT: // 오른쪽 방향키 눌림
                gameBoard.blockMoveRight();
                break;

            case KeyEvent.VK_LEFT:  // 왼쪽 방향키 눌림
                gameBoard.blockMoveLeft();
                break;

            case KeyEvent.VK_SPACE: // space 바 눌림 -> 한번에 끝까지 밑으로 내리는 방향키 구현을 위해
                gameBoard.blockDrop();
                break;

            default:
                break;
        }
    }

     */


}
