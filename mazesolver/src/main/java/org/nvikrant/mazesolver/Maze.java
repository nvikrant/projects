package org.nvikrant.mazesolver;

/**
 * Created by nvikrant on 9/19/13.
 * The main maze data structure.
 * This abstracts the maze cells and
 * initializes the walls and the visit matrix.
 */
public class Maze {
    private boolean north[][];
    private boolean south[][];
    private boolean west[][];
    private boolean east[][];
    private boolean visited[][];
    private int width, height;

    /* Initialize the maze with the correct
     * width and height.
     */
    public Maze(int width, int height) {
        try {
            initGrid(width, height);
            this.width  = width;
            this.height = height;
        }
        catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Set the visits for the border cells, so that
     * all of the algorithms work fine with any corner
     * cases like going past the borders while generating
     * the maze or while traversing the maze.
     */
    private void setVisits() {
        visited = new boolean[width+2][height+2];
        for (int x = 0; x < width+2; x++)
            visited[x][0] = visited[x][height+1] = true;
        for (int y = 0; y < height+2; y++)
            visited[0][y] = visited[width+1][y] = true;

    }

    private void initGrid(int width, int height) {
        if(width <2 || height < 2) {
            throw new IllegalArgumentException("Illegal width|height, height and width > 2");
        }

        visited = new boolean[width+2][height+2];
        for (int x = 0; x < width+2; x++)
            visited[x][0] = visited[x][height+1] = true;
        for (int y = 0; y < height+2; y++)
            visited[0][y] = visited[width+1][y] = true;

        north = new boolean[width+2][height+2];
        south = new boolean[width+2][height+2];
        east  = new boolean[width+2][height+2];
        west  = new boolean[width+2][height+2];
        for (int x = 0; x < width+2; x++)
            for (int y = 0; y < height+2; y++)
                north[x][y] = east[x][y] = south[x][y] = west[x][y] = true;
    }

    public boolean[][][] getWallGrids() {
        boolean[][][] gridMatrix = new boolean[4][width+2][height+2];
        gridMatrix[0] = north;
        gridMatrix[1] = south;
        gridMatrix[2] = west;
        gridMatrix[3] = east;
        return gridMatrix;
    }

    public boolean[][] getVisitMatrix() {
        boolean[][] visitMatrix = new boolean[width+2][height+2];
        visitMatrix = visited;
        return visitMatrix;
    }
}
