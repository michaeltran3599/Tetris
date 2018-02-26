import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Blockboard {
    private Block currentBlock;
    private Block[][] set = new Block[Game.ROWS][Game.COLS];
    private final int QUEUE_SIZE = 3;
    private ArrayList<Block> queue = new ArrayList<Block>(QUEUE_SIZE);
    private Block keep;
    private int player;
    private double QUEUE_SCALE = 2.0;
    private Block guide = currentBlock;
    public static final int QUEUE_OFFSET = 80;
    public static final int HOLD_HEIGHT = 350;
    private int setTimer = 0;
    public static final int SET_DELAY = 20;

    public Blockboard(int p) {
        player = p;
        for(int i = 0; i < 3; i++) {
            queue.add(new Block());
        }
        queueToBoard();
    }

    public void queueToBoard() {
        currentBlock = queue.remove(0);
        queue.add(new Block());
    }

    public void addSetBlock() {
        ArrayList<Point> currentPoints = currentBlock.getAllPoints();
        for(Point p: currentPoints) {
            int x = (int)p.getX() + currentBlock.getX();
            int y = (int)p.getY() + currentBlock.getY();
            set[y][x] = new Block(x, y, currentBlock.getColor());
        }
    }

    public boolean canMoveDown(Block b) {
        for(Point p: b.getBottom()) {
            int x = (int)(p.getX() + currentBlock.getX());
            int y = (int)(p.getY() + currentBlock.getY());
            if(!(y + 2 < Game.ROWS && set[y + 1][x] == null))
                return false;
        }
        return true;
    }

    public void moveDown() {
        if(canMoveDown(currentBlock))
            currentBlock.moveDown();
    }

    public void moveToBottom(String s) {
        while(canMoveDown(currentBlock))
            currentBlock.moveDown();
        if(s.equals("c")) {
            addSetBlock();
            queueToBoard();
        }
    }

    public boolean moveLeft() {
        for(Point p: currentBlock.getLeft()) {
            int x = (int)(p.getX() + currentBlock.getX());
            int y = (int)(p.getY() + currentBlock.getY());
            if(!(x > 0 && set[y][x - 1] == null))
                return false;
        }
        currentBlock.moveLeft();
        return true;
    }

    public boolean moveRight() {
        for(Point p: currentBlock.getRight()) {
            int x = (int)(p.getX() + currentBlock.getX());
            int y = (int)(p.getY() + currentBlock.getY());
            if(!(x + 1 < Game.COLS && set[y][x + 1] == null))
                return false;
        }
        currentBlock.moveRight();
        return true;
    }

    public boolean rotateClockwise(int n) {
        int originalX = currentBlock.getX();
        boolean canRotate = true;
        ArrayList<Point> rotated = currentBlock.getOrientationPoints();
        for(Point i: rotated) {
            boolean same = false;
            for(Point j: currentBlock.getAllPoints()) {
                if(i.equals(j)) {
                    same = true;
                }
            }
            if(same == false) {
                int x = (int)i.getX() + currentBlock.getX();
                int y = (int)i.getY() + currentBlock.getY();
                if(y <= Game.ROWS - 2 && x < Game.COLS && x >= 0 && set[y][x] == null) {
                }
                else {
                    canRotate = false;
                    break;
                }
            }
        }
        if(canRotate) {
            currentBlock.rotateClockwise();
            return true;
        }
        else {
            moveRight();
            if(n < 3) {
                if(rotateClockwise(n + 1)) {
                    return true;
                }
            }
            if(n < 2) {
                moveLeft();
                if(rotateClockwise(100)) {
                    return true;
                }
            }
        }
        currentBlock.setX(originalX);
        return false;
    }


    public void rotateCounterClockwise() {
        rotateClockwise(0);
        rotateClockwise(0);
        rotateClockwise(0);
    }


    public void keep() {
        currentBlock.setY(0);
        currentBlock.setY((Game.COLS / 2 - 1));
        if(keep == null) {
            currentBlock.reset();
            keep = currentBlock;
            queueToBoard();
        }
        else {
            currentBlock.reset();
            Block swap = currentBlock;
            currentBlock = keep;
            keep = swap;
            keep.makePoints();
        }
    }

    public int lineCheck() {
        int fullRows = 0;
        for(int i = Game.ROWS - 1; i > 0; i--) {
            boolean full = true;
            for(int j = 0; j < Game.COLS; j++) {
                if(set[i][j] == null) {
                    full = false;
                    break;
                }
            }
            if(full) {
                fullRows++;
                for(int j = 0; j < Game.COLS; j++) {
                    set[i][j] = null;
                }
                for(int j = 0; j < Game.COLS; j++) {
                    int counter = 1;
                    while(i - counter >= 0) {
                        if(set[i - counter][j] != null) {
                            set[i - counter + 1][j] = set[i - counter][j];
                            set[i - counter + 1][j].moveDown();
                            set[i - counter][j] = null;
                        }
                        counter++;
                    }
                }
                i++;
            }
        }
        return fullRows;
    }

    public void update() {
        if(canMoveDown(currentBlock))
            moveDown();
        else {
            if(setTimer == SET_DELAY) {
                addSetBlock();
                queueToBoard();
                setTimer = 0;
            }
            else
                setTimer++;
        }
    }

    public void draw(Graphics2D g) {

        g.translate((Game.COLS * Game.BLOCK_SIZE + Game.RIGHT_BAR_SIZE + Game.BORDER_SIZE) * (player - 1), 0);

        for(Block[] blocks: set) {
            for(Block b: blocks) {
                if(b != null)
                    b.draw(g);
            }
        }
        int originalY = currentBlock.getY();
        moveToBottom("g");
        Block guide = new Block(currentBlock, "g");
        guide.draw(g);
        currentBlock.setY(originalY);
        currentBlock.draw(g);


        g.setColor(Color.white);
        Font labels = new Font("serif", Font.BOLD, 36);
        g.setFont(labels);
        FontMetrics metrics = g.getFontMetrics(labels);
        g.drawString("Next Up", ((Game.COLS * Game.BLOCK_SIZE + Game.RIGHT_BAR_SIZE)  - Game.RIGHT_BAR_SIZE / 2 - metrics.stringWidth("Next Up") / 2), 50);
        g.drawString("Hold",    ((Game.COLS * Game.BLOCK_SIZE + Game.RIGHT_BAR_SIZE)  - Game.RIGHT_BAR_SIZE / 2 - metrics.stringWidth("Hold") / 2), 350);

        for(int i = 0; i < QUEUE_SIZE; i++) {
            Block copy = new Block(queue.get(i), "q");
            copy.setBlockSize((int)(Game.BLOCK_SIZE / QUEUE_SCALE));
            copy.setX(((Game.COLS * Game.BLOCK_SIZE + Game.RIGHT_BAR_SIZE) - Game.BLOCK_SIZE - Game.RIGHT_BAR_SIZE / 2) / (int)(Game.BLOCK_SIZE / QUEUE_SCALE));
            copy.setY(QUEUE_OFFSET * (i + 1) / (int)(Game.BLOCK_SIZE / QUEUE_SCALE));
            copy.draw(g);
        }
        if(keep != null) {
            Block keepCopy = new Block(keep, "q");
            keepCopy.setBlockSize((int)(Game.BLOCK_SIZE / QUEUE_SCALE));
            keepCopy.setX(((Game.COLS * Game.BLOCK_SIZE + Game.RIGHT_BAR_SIZE) - Game.RIGHT_BAR_SIZE / 2 - Game.BLOCK_SIZE) / (int)(Game.BLOCK_SIZE / QUEUE_SCALE));
            keepCopy.setY(HOLD_HEIGHT / (int)(Game.BLOCK_SIZE / QUEUE_SCALE) + 2);
            keepCopy.draw(g);
        }

    }
}