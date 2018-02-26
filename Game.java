import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Canvas;
import java.awt.Color;
import javax.swing.Timer;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.FontMetrics;

public class Game extends JPanel implements ActionListener, KeyListener, MouseListener {
    private Blockboard player1;
    private Blockboard player2;
    private boolean control = false;
    public static final int SCALE = 1;
    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final int BLOCK_SIZE = 40;
    public static final int RIGHT_BAR_SIZE = 200;
    public static final int DELAY = 100;
    public static final double SPEED_SCALE = 2.0;
    public final int BUTTON_WIDTH = 250;
    public final int BUTTON_HEIGHT = 85;
    public static final int BORDER_SIZE = 10;
    private int speed;
    private int counter = speed;
    private int mode = 0;
    private static JFrame frame = new JFrame("Tetris");
    private BufferedImage logo;
    private final Rectangle SINGLEPLAYER;
    private final Rectangle TWOPLAYER;


    private Timer timer;

    public Game() {
        try {
            logo = ImageIO.read(new File("Tetris-logo.png"));
        } catch (IOException e) {
        }
        SINGLEPLAYER = new Rectangle((COLS * BLOCK_SIZE + RIGHT_BAR_SIZE) / 2 - BUTTON_WIDTH / 2, logo.getHeight(null) + BLOCK_SIZE * 3, BUTTON_WIDTH, BUTTON_HEIGHT);
        TWOPLAYER = new Rectangle((COLS * BLOCK_SIZE + RIGHT_BAR_SIZE) / 2 - BUTTON_WIDTH / 2, logo.getHeight(null) + BLOCK_SIZE * 6, BUTTON_WIDTH, BUTTON_HEIGHT);
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    public static void main(String[] args) {
        Game game = new Game();
        frame.setSize(COLS * BLOCK_SIZE + RIGHT_BAR_SIZE, ROWS * BLOCK_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(game);
        frame.setVisible(true);

    }

    public void actionPerformed(ActionEvent arg0){
        switch(mode) {
            case 1:
                int lines = player1.lineCheck();
                if (counter <= 0) {
                    player1.update();
                    counter = speed;
                } else {
                    counter -= DELAY / SPEED_SCALE;
                }
                if (lines > 0) {
                    speed -= (int) (lines * DELAY / SPEED_SCALE);
                }
                repaint();
                break;
            case 2:
                int lines1 = player1.lineCheck();
                int lines2 = player2.lineCheck();
                if (counter <= 0) {
                    player1.update();
                    player2.update();
                    counter = speed;
                } else {
                    counter -= DELAY / SPEED_SCALE;
                }
                repaint();
            default:
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(mode) {
            case(1):
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:

                        player1.moveLeft();

                        break;
                    case KeyEvent.VK_RIGHT:
                        player1.moveRight();
                        break;
                    case KeyEvent.VK_DOWN:
                        player1.moveDown();
                        break;
                    case KeyEvent.VK_UP:
                        if (control) {
                            player1.rotateCounterClockwise();
                        } else {
                            player1.rotateClockwise(0);
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        player1.moveToBottom("c");
                        break;
                    case KeyEvent.VK_CONTROL:
                        control = true;
                        break;
                    case KeyEvent.VK_SHIFT:
                        player1.keep();
                    default:
                        break;
                }
                repaint();
                break;
            case(2):
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        player2.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        player2.moveRight();
                        break;
                    case KeyEvent.VK_DOWN:
                        player2.moveDown();
                        break;
                    case KeyEvent.VK_UP:
                        player2.rotateClockwise(0);
                        break;
                    case KeyEvent.VK_SPACE:
                        player2.moveToBottom("c");
                        break;
                    case KeyEvent.VK_M:
                        player2.keep();
                        break;
                    case KeyEvent.VK_A:
                        player1.moveLeft();
                        break;
                    case KeyEvent.VK_D:
                        player1.moveRight();
                        break;
                    case KeyEvent.VK_S:
                        player1.moveDown();
                        break;
                    case KeyEvent.VK_W:
                        player1.rotateClockwise(0);
                        break;
                    case KeyEvent.VK_Z:
                        player1.moveToBottom("c");
                        break;
                    case KeyEvent.VK_SHIFT:
                        player1.keep();
                        break;
                    default:
                        break;
                }
                repaint();
                break;
            default:
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(mode) {
            case(1):
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        control = false;
                        break;
                    default:
                        break;
                }
                repaint();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseClicked(MouseEvent arg0) {

    }

    public void mouseExited(MouseEvent arg0) {

    }

    public void mouseEntered(MouseEvent arg0) {

    }

    public void mousePressed(MouseEvent arg0) {

    }

    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(mode == 0) {
            if(SINGLEPLAYER.contains(x, y)) {
                player1 = new Blockboard(1);
                mode = 1;
                speed = 2000;
                timer = new Timer(DELAY, this);
                timer.start();
            }
            else if(TWOPLAYER.contains(x,y)) {
                player1 = new Blockboard(1);
                player2 = new Blockboard(2);
                player2 = new Blockboard(2);
                frame.setSize((COLS * BLOCK_SIZE + RIGHT_BAR_SIZE) * 2, ROWS * BLOCK_SIZE);
                frame.setLocationRelativeTo(null);
                mode = 2;
                speed = 1000;
                timer = new Timer(DELAY, this);
                timer.start();
            }
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch(mode) {
            case 0:
                int frameWidth = COLS * BLOCK_SIZE + RIGHT_BAR_SIZE;
                int frameHeight = ROWS * BLOCK_SIZE;
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, frameWidth, frameHeight);
                g.drawImage(logo, frameWidth / 2 - logo.getWidth(null) / 2, BLOCK_SIZE, null);

                g.setColor(Color.BLUE);
                g.fillRect((int)SINGLEPLAYER.getX(), (int)SINGLEPLAYER.getY(), BUTTON_WIDTH, BUTTON_HEIGHT);
                g.setColor(Color.RED);
                g.fillRect((int)TWOPLAYER.getX(), (int)TWOPLAYER.getY(), BUTTON_WIDTH, BUTTON_HEIGHT);

                g.setColor(Color.WHITE);
                Font buttonLabels = new Font("serif", Font.BOLD, 36);
                g.setFont(buttonLabels);
                FontMetrics metrics = g.getFontMetrics(buttonLabels);
                g.drawString("1 Player", (int)SINGLEPLAYER.getX() + (int)SINGLEPLAYER.getWidth() / 2 - metrics.stringWidth("1 Player") / 2,
                                         (int)SINGLEPLAYER.getY() + (int) SINGLEPLAYER.getHeight() / 2 + metrics.getHeight() / 3);
                g.drawString("2 Player", (int)TWOPLAYER.getX() + (int)TWOPLAYER.getWidth() / 2 - metrics.stringWidth("2 Player") / 2,
                                         (int)TWOPLAYER.getY() + (int) TWOPLAYER.getHeight() / 2 + metrics.getHeight() / 3);
                break;
            case 1:
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, COLS * BLOCK_SIZE + RIGHT_BAR_SIZE, ROWS * BLOCK_SIZE);

                g.setColor(Color.GRAY);

                g.fillRect(COLS * BLOCK_SIZE, 0, BORDER_SIZE, ROWS * BLOCK_SIZE);
                player1.draw((Graphics2D) g);
                break;
            case 2:
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, (COLS * BLOCK_SIZE + RIGHT_BAR_SIZE) * 2, ROWS * BLOCK_SIZE);


                g.setColor(Color.GRAY);
                g.fillRect(COLS * BLOCK_SIZE, 0, BORDER_SIZE, ROWS * BLOCK_SIZE);
                g.fillRect(COLS * BLOCK_SIZE * 2 + BORDER_SIZE + RIGHT_BAR_SIZE, 0, BORDER_SIZE, ROWS * BLOCK_SIZE);
                g.fillRect(0, ROWS * BLOCK_SIZE - 4 * BORDER_SIZE, (COLS * BLOCK_SIZE + BORDER_SIZE + RIGHT_BAR_SIZE) * 2, BORDER_SIZE * 2);

                g.setColor(Color.RED);
                g.fillRect(COLS * BLOCK_SIZE + RIGHT_BAR_SIZE, 0, BORDER_SIZE, ROWS * BLOCK_SIZE);

                player1.draw((Graphics2D) g);
                player2.draw((Graphics2D) g);


        }
    }
}
