package tetris.controller;

interface GameMethod {

    // 게임오버시 발동 메소드
    public void doAfterGameOver();

    // 바닥 도달시 발동 메소드
    public void doBeforeTakeOutNextBlock();
}
