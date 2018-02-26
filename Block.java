import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.util.HashMap;

public class Block {
    private int xPos = (Main.getCols() / 2 - 2);
    private int yPos = 0;
    private int orientation = 0;
    private int blockSize = Main.getBlockSize();
    private int type;
    public final static int BLOCK_TYPES = 6;
    private ArrayList<Point> allPoints = new ArrayList<Point>();
    private ArrayList<ArrayList<Point>> orientations = new ArrayList<ArrayList<Point>>();
    private ArrayList<Point> bottom;
    private ArrayList<Point> left;
    private ArrayList<Point> right;
    private Color color;
    private Color outline;

    public Block() {
        int random = (int)(Math.random() * BLOCK_TYPES);
        readData(random);
        outline = Color.darkGray;
        type = random;
        allPoints = orientations.get(0);
        getBoundries();
    }

    public Block(Block b, String s) {
        switch(s) {
            case "g":
                xPos = b.xPos;
                yPos = b.yPos;
                orientation = b.orientation;
                readData(b.getType());
                allPoints = orientations.get(orientation);
                color = Color.BLACK;
                outline = Color.GRAY;
                break;
            case "q":
                orientation = b.orientation;
                readData(b.getType());
                allPoints = orientations.get(orientation);
                outline = Color.GRAY;
            default:
                break;
        }
    }

    public Block(int x, int y, Color c) {
        xPos = x;
        yPos = y;
        color = c;
        outline = Color.darkGray;
        allPoints.add(new Point(0,0));
    }

    public void getBoundries() {
        bottom = new ArrayList<Point>();
        left = new ArrayList<Point>();
        right = new ArrayList<Point>();
        HashMap<Integer, ArrayList<Point>> bottomPoints = new HashMap<Integer, ArrayList<Point>>();
        HashMap<Integer, ArrayList<Point>> sidePoints = new HashMap<Integer, ArrayList<Point>>();

        for(Point p: allPoints) {
            ArrayList<Point> currentBottom = bottomPoints.get((int)p.getX());
            ArrayList<Point> currentSide = sidePoints.get((int)p.getY());
            if(currentBottom == null) {
                ArrayList<Point> add = new ArrayList<Point>();
                add.add(p);
                bottomPoints.put((int) p.getX(), add);
            }
            else
                currentBottom.add(p);
            if(currentSide == null) {
                ArrayList<Point> add = new ArrayList<Point>();
                add.add(p);
                sidePoints.put((int) p.getY(), add);
            }
            else
                currentSide.add(p);
        }
        for(ArrayList<Point> xPoints: bottomPoints.values()) {
            Point lowest = new Point(-1, -1);
            for (Point p : xPoints) {
                if (p.getY() > lowest.getY())
                    lowest = p;
            }
            bottom.add(lowest);
        }

        for(ArrayList<Point> yPoints: sidePoints.values()) {
            Point leftMost = new Point(100, 100);
            Point rightMost = new Point(-1,-1);
            for (Point p : yPoints) {
                if (p.getX() > rightMost.getX())
                    rightMost = p;
                if (p.getX() < leftMost.getX())
                    leftMost = p;
            }
            left.add(leftMost);
            right.add(rightMost);
        }
    }

    public void readData(int n) {
        for(int i = 0; i < 4; i++)
            orientations.add(new ArrayList<Point>());
        File test = new File("Blocks.txt");
        try (Scanner input = new Scanner(test))  {
            String read = "";
            for(int i = 0; i <= n; i++) {
                read = input.nextLine();
            }
            String[] parameters =  read.split(" ", 0);
            type = Integer.parseInt(parameters[0]);
            color = Color.decode(parameters[2]);
            for(int i = 0; i < 4; i++) {
                String[] readPoints = parameters[3 + i].split("(?<=\\G..)");
                for (String add : readPoints) {
                    orientations.get(i).add(new Point(Character.getNumericValue(add.charAt(0)), Character.getNumericValue(add.charAt(1))));
                }
            }
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file");
        }
    }

    public void setBlockSize(int size) {
        blockSize = size;
    }

    public int getX() {
        return xPos;
    }

    public int getType() {
        return type;
    }

    public void setX(int x) {
        xPos = x;
    }

    public void setY(int y) {
        yPos = y;
    }

    public int getY() {
        return yPos;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        color = c;
    }

    public void setOutline(Color c) {
        outline = c;
    }
    public ArrayList<Point> getOrientationPoints() {
        return orientations.get((orientation + 1) % 4);
    }

    public ArrayList<Point> getAllPoints() {
        return allPoints;
    }

    public ArrayList<Point> getBottom() {
        return bottom;
    }

    public ArrayList<Point> getLeft() {
        return left;
    }

    public ArrayList<Point> getRight() {
        return right;
    }

    public void rotateClockwise() {
        orientation = (orientation + 1) % 4;
        makePoints();
    }

    public void rotateCounterClockwise() {
        if(orientation == 0)
            orientation = 3;
        else
            orientation -= 1;
        makePoints();
    }

    public void moveLeft() {
        xPos -= 1;
    }

    public void moveRight() {
        xPos += 1;
    }

    public void moveDown() {
        yPos += 1;
    }

    public void reset() {
        orientation = 0;
        xPos = (Main.getCols() / 2 - 2);
        yPos = 0;
    }

    public void makePoints() {
        allPoints = orientations.get(orientation);
        getBoundries();
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(8));
        for(Point p: allPoints) {
            g.setColor(color);
            int drawX = (int)(p.getX() + xPos) * blockSize;
            int drawY = (int)(p.getY() + yPos) * blockSize;
            g.fillRect(drawX, drawY, blockSize, blockSize);
            g.setColor(outline);
            g.drawRect(drawX, drawY, blockSize, blockSize);
        }
    }

}