package tetris;


public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        int a = 3;
        int b = 1;
        if (a == 3) {
            if (b == 1) {
                System.out.println("true");
                b = 5;
            }
            else {
                System.out.println("true");
            }
        }

        if (b == 5) {
            a = 4;
        }
        else {
            System.out.println("Test");
        }
    }
}
