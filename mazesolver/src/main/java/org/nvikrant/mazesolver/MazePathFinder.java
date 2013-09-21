package org.nvikrant.mazesolver;

import java.awt.*;

/**
 * Created by nvikrant on 9/19/13.
 */
public class MazePathFinder extends MazeCanvas {
    private int pointEndX;
    private int pointEndY;
    private boolean complete = false;

    public MazePathFinder(int width, int height) {
        super(width, height);
        setVisits();
    }

    public void setVisits() {
        for (int x = 1; x <= width; x++)
            for (int y = 1; y <= height; y++)
                visitMatrix[x][y] = false;

    }

    public void setMazeStartEnd(int x, int y, int x1, int y1) {
        pointEndX = x1;
        pointEndY = y1;

        setPenColor(BOOK_RED);
        filledCircle(x + 0.5, y + 0.5, 0.375);
        filledCircle(pointEndX + 0.5, pointEndY + 0.5, 0.375);
        refreshScreen();
    }

    public void displayPath(int x ,int y) {
        // solve the maze using depth-first search
        if (x == 0 || y == 0 || x == width+1 || y == height+1) return;
        if (complete || visitMatrix[x][y]) return;
            visitMatrix[x][y] = true;

            setPenColor(BOOK_RED);
            pause(100);

            //Reached destination
            if (x == pointEndX && y == pointEndY) complete = true;

            if (!wallGrids[0][x][y]) {
                drawLine(x + 0.50, y + 0.50, x + 0.50, y + 1.5);
                displayPath(x, y + 1);
            }
            if (!wallGrids[3][x][y]) {
                drawLine(x + 0.5, y + 0.5, x + 1.5, y + 0.5);
                displayPath(x + 1, y);
            }
            if (!wallGrids[1][x][y]) {
                drawLine(x + 0.5, y + 0.5, x + 0.5, y - 0.5);
                displayPath(x, y - 1);
            }
            if (!wallGrids[2][x][y]) {
                drawLine(x + 0.50, y + 0.5, x - 0.5, y + 0.5);
                displayPath(x - 1, y);
            }
            if (complete) return;

            setPenColor(Color.GRAY);
            filledCircle(x + 0.5, y + 0.5, 0.250);
            pause(50);
    }
}
