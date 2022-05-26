package tetris.model;

public class Board {

    private int x;
    private int y;
    private int nextX;
    private int nextY;
    private int[][] gameBoard;
    private int[][] bufferedBoard;
    private int[][] visualBoard;
    private int[][] nextBoard;

    public Board(int height, int width, int nextHeight, int nextWidth) {
        gameBoard = new int[height][width];
        bufferedBoard = new int[height][width];
        visualBoard = new int[height][width];
        nextBoard = new int[nextHeight][nextWidth];
        x = 0;
        y = 3;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void addX(int add) {
        this.x += add;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void addY(int add) {
        this.y += add;
    }

    public int getNextX() {
        return this.nextX;
    }

    public void setNextX(int nextX) {
        this.nextX = nextX;
    }

    public int getNextY() {
        return this.nextY;
    }

    public void setNextY(int nextY) {
        this.nextY = nextY;
    }

    public int[][] getGameBoard() {
        return this.gameBoard;
    }

    public int[][] getBufferedBoard() {
        return this.bufferedBoard;
    }

    public int[][] getColorBoard() {
        return this.visualBoard;
    }

    public int[][] getNextBoard() {
        return this.nextBoard;
    }
}
