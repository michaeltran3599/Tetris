

import javax.swing.JFrame;

public class Main {
    public static int ROWS = 20;
    public static int COLS = 10;
    public static int BLOCK_SIZE = 40;
    public static void main(String [] args) {
        JFrame frame = new JFrame();
        Game game = new Game();
        frame.setTitle("Tetris");
        frame.setSize(COLS * BLOCK_SIZE + 300, ROWS * BLOCK_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.add(game);
    }

    public static int getCols() {
        return COLS;
    }

    public static int getRows() {
        return ROWS;
    }

    public static int getBlockSize() {
        return BLOCK_SIZE;
    }
}