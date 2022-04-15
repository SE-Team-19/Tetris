package tetris.controller;

import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;

import tetris.model.*;
import tetris.view.*;

public class GameController extends JPanel {

    GameView gameView = GameView.getInstance();    // instance를 한 번 호출한다.
    BlockController blockController;

    GameBoard gameBoard;
    public static int count = 1;

    private JTextPane gamePane;

    public GameController() {
        initBoard();
        blockController = new BlockController();
    }

    public void initBoard() {
        // gameView.initGameView() 이 부분을 굳이 선언할 필요가 없다. 어차피 위에서 instance를 한 번 호출해옴.
        // 이렇게 되면 instance를 두번 호출, 즉 중복해서 호출한다.
    }

    public void drawNextBlockBoard(JPanel jPanel) {

    }

    // Block에 관한 모든 메소드들을 여기에 정의한다.
    private static class BlockController {
        Block currentBlock;
        Block nextBlock;

        public BlockController() {
            //currentBlock = new Block();
            currentBlock = getRandomBlock();
            nextBlock = getRandomBlock();
        }

        /***************************************/
        /* 블록들 클래스 선언 */
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

        /***************************************/

        /*
        랜덤으로 블록을 받아오는 메소드
        */
        public Block getRandomBlock() {
            long seed = System.currentTimeMillis();
            Random random = new Random(seed);

            int block = random.nextInt(7);        // 7개의 블럭을 동일한 확률로 불러온다: 0이상 6이하
            //count++;    // 개수를 하나 증가시킨다.

            switch (block) {
                case 0: {
                    currentBlock = new IBlock();
                    return currentBlock;
                }
                case 1: {
                    currentBlock = new JBlock();
                    return currentBlock;
                }
                case 2: {
                    currentBlock = new LBlock();
                    return currentBlock;
                }
                case 3: {
                    currentBlock = new ZBlock();
                    return currentBlock;
                }
                case 4: {
                    currentBlock = new SBlock();
                    return currentBlock;
                }
                case 5: {
                    currentBlock = new TBlock();
                    return currentBlock;
                }
                case 6: {
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

        // Block class 에서 rotate() 메소드 실행 해보고 이상 없으면 여기로 옮기기

    }

}
