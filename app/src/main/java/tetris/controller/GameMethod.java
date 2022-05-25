package tetris.controller;

interface GameMethod {

    // 게임시작시 발동 메소드
    public void doWhenGameStart();

    // 게임오버시 발동 메소드
    public void doAfterGameOver();

    // 다음 블록 생성 전 발동 메소드
    public void doBeforeTakeOutNextBlock();

    // 다음 블록 생성 후 발동 메소드
    public void doAfterTakeOutNextBlock();
}