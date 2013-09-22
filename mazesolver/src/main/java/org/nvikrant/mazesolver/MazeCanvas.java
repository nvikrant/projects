package org.nvikrant.mazesolver;
import org.nvikrant.mazesolver.utils.MazeSolverUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * Created by nvikrant on 9/19/13.
 * This is the main UI Class for displaying,
 * maintaining and refreshing the maze UI
 * elements.
 */
public class MazeCanvas {
    private final double BORDER = 0.05;
    private final int WIDTH  = 512;
    private final int HEIGHT = 512;
    private final int DEFAULT_SIZE = 512;
    private JFrame mazeFrame;
    private boolean defer = false;
    private final Color DEFAULT_PEN_COLOR = Color.BLACK;

    protected static final Color BOOK_RED        = new Color(150, 35, 31);
    protected static final Color BOOK_LIGHT_BLUE = new Color(103, 196, 248);

    protected int width, height;
    protected boolean[][] visitMatrix;
    protected boolean[][][] wallGrids;

    private final Font DEFAULT_FONT = new Font("SansSerif", Font.BOLD, 16);
    private final double DEFAULT_PEN_RADIUS = 0.002;
    private BufferedImage bgImage, fgImage;
    private Graphics2D bgMaze, fgMaze;
    private double xmin, xmax, ymin, ymax;

    /* Scale the user x-coordinate to the device x-coordinate */
    public void setXscale(double min, double max) {
        double size = max - min;
        xmin = min - BORDER * size;
        xmax = max + BORDER * size;
    }

    /* Scale the user y-coordinate to the device y-coordinate */
    public void setYscale(double min, double max) {
        double size = max - min;
        ymin = min - BORDER * size;
        ymax = max + BORDER * size;
    }

    public void setPenColor(Color color) {
        bgMaze.setColor(color);
    }

    public void setPenRadius() {
        float scaledPenRadius = (float) (DEFAULT_PEN_RADIUS * DEFAULT_SIZE);
        BasicStroke stroke = new BasicStroke(scaledPenRadius, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND);
        bgMaze.setStroke(stroke);
    }

    private void initUIElements() {
        mazeFrame = new JFrame();
        bgImage  = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        fgImage  = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        bgMaze = bgImage.createGraphics();
        fgMaze = fgImage.createGraphics();

    }
    private void initMazeUI() {
        setXscale(0, width + 2);
        setYscale(0, height + 2);
        setPenColor(DEFAULT_PEN_COLOR);
        setPenRadius();
    }

    /* Init the Maze Canvas UI Elements with all
     * the helper functions.
     */
    private void initCanvas(){
        initUIElements();
        bgMaze.setColor(Color.white);
        bgMaze.fillRect(0, 0, WIDTH, HEIGHT);

        /** If we do not use anti-aliasing the geometric figures having
         *  a radius display like an inkblot :)
         */
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        bgMaze.addRenderingHints(hints);


        // Frame related config settings.
        ImageIcon icon = new ImageIcon(fgImage);
        JLabel draw = new JLabel(icon);

        mazeFrame.setContentPane(draw);
        mazeFrame.setResizable(false);
        mazeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mazeFrame.setTitle("Maze Solver - DFS");
        mazeFrame.pack();
        mazeFrame.requestFocusInWindow();
        mazeFrame.setVisible(true);
    }

    /** This is used to stop/start the animation|refresh process
     *  on the screen. Instead of drawing a figure line-by-line or
     *  dot-by-dot we wait until the entire figure is done and then
     *  display.
     *  @param t the number of milliseconds to wait before display.
     */
    public void pause(int t) {
        defer = false;
        refreshScreen();
        try { Thread.sleep(t); }
        catch (InterruptedException e) { System.out.println("Error sleeping");}
        defer = true;
    }

    public void refreshScreen() {
        if(defer) return;
        fgMaze.drawImage(bgImage,0,0,null);
        mazeFrame.repaint();
    }

    private void pixel(double x, double y) {
        bgMaze.fillRect((int) Math.round(scaleX(x)),
                (int) Math.round(scaleY(y)), 1, 1);
    }

    /** These are some of the utility functions which translate
     *  the user coordinates to the device/screen coordinates and back.
     */
    private double scaleX(double x) { return WIDTH  * (x - xmin) / (xmax - xmin); }
    private double scaleY(double y) { return HEIGHT * (ymax - y) / (ymax - ymin); }
    private double factorX(double w) { return w * WIDTH  / Math.abs(xmax - xmin);  }
    private double factorY(double h) { return h * HEIGHT / Math.abs(ymax - ymin);  }

    public void filledCircle(double x, double y, double r) {
        if (r < 0) throw new IllegalArgumentException("circle radius must be nonnegative");
        double xs = scaleX(x);
        double ys = scaleY(y);
        double ws = factorX(2*r);
        double hs = factorY(2*r);
        if (ws <= 1 && hs <= 1) pixel(x, y);
        else bgMaze.fill(new Ellipse2D.Double(xs - ws/2, ys - hs/2, ws, hs));
    }

    public void drawLine(double x0, double y0, double x1, double y1) {
        bgMaze.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
        refreshScreen();
    }

    /** This ctor sets the maze width and height.
     *  It inits the canvas/maze ui and draws the maze.
     *
     * @param width the user entered width
     * @param height the user entered height
     */
    public MazeCanvas(int width, int height) {
        wallGrids   = new boolean[4][width+2][height+2];
        visitMatrix = new boolean[width+2][height+2];
        this.width  = width;
        this.height = height;

        initCanvas();
        initMazeUI();
        drawMaze();
    }

    /** Draw the maze on the canvas with the user coordinates
     * translated to the device/screen coordinates.
     */
    public void draw() {
        pause(0);
        setPenColor(BOOK_LIGHT_BLUE);
        for (int x = 1; x <= width; x++) {
            for (int y = 1; y <= height; y++) {
                if (wallGrids[1][x][y]) drawLine(x, y, x + 1, y);
                if (wallGrids[0][x][y]) drawLine(x, y + 1, x + 1, y + 1);
                if (wallGrids[2][x][y]) drawLine(x, y, x, y + 1);
                if (wallGrids[3][x][y]) drawLine(x + 1, y, x + 1, y + 1);
            }
        }
        pause(1000);
    }

    public void drawMaze() {
        Maze maze = new Maze(width, height);
        wallGrids = maze.getWallGrids();
        visitMatrix = maze.getVisitMatrix();
        for (int x = 1; x <= width; x++)
            for (int y = 1; y <= height; y++)
                visitMatrix[x][y] = false;

        /* Get an instance of a maze generator, here we use DFS */
        MazeGenerator mazer = new MazeGeneratorDFS(wallGrids, visitMatrix);
        int x = MazeSolverUtils.getRandom(width);
        int y = MazeSolverUtils.getRandom(height);
        mazer.generate(x,y);
        draw();
    }
}
