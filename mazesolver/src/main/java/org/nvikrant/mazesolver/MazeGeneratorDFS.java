package org.nvikrant.mazesolver;

/**
 * Created by nvikrant on 9/19/13.
 * This Class extends the MazeGenerator class
 * and overrides the generate() method to implement
 * the algorithm specific wall carving in the maze.
 */
public class MazeGeneratorDFS extends MazeGenerator {
    
    private void collapseWall(int x, int y) {
        visitGrid[x][y] = true;

        // while there is an unvisited neighbor
        // start collapsing walls at random.
        while (!visitGrid[x][y+1] || !visitGrid[x+1][y] ||
                !visitGrid[x][y-1] || !visitGrid[x-1][y]) {

            while (true) {
                double r = Math.random();
                if (r < 0.25 && !visitGrid[x][y+1]) {
                    northGrid[x][y] = southGrid[x][y+1] = false;
                    collapseWall(x, y + 1);
                    break;
                }
                else if (r >= 0.25 && r < 0.50 && !visitGrid[x+1][y]) {
                    eastGrid[x][y] = westGrid[x+1][y] = false;
                    collapseWall(x+1, y);
                    break;
                }
                else if (r >= 0.5 && r < 0.75 && !visitGrid[x][y-1]) {
                    southGrid[x][y] = northGrid[x][y-1] = false;
                    collapseWall(x, y-1);
                    break;
                }
                else if (r >= 0.75 && r < 1.00 && !visitGrid[x-1][y]) {
                    westGrid[x][y] = eastGrid[x-1][y] = false;
                    collapseWall(x-1, y);
                    break;
                }
            }
        }
    }
    
    @Override
    public void generate(int x, int y) {
        collapseWall(x, y);
    }

    public MazeGenerator(final boolean[][][] wallGrids,
                         final boolean[][] visitGrid) {
        super(wallGrids, visitGrid);
    }
}
