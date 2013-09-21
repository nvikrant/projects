package org.nvikrant.mazesolver;
import org.nvikrant.mazesolver.utils.MazeSolverUtils;


/**
 * Created by nvikrant on 9/19/13.
 * This is the main driver for the program.
 * Its takes the args for width height of the maze.
 */
public class MazeSolver {
    public static void main(String[] args) {
        if(args.length != 2) {
            System.out.println("usage: mazesolver <width> <height>");
            System.exit(1);
        }

        try {
            int width  = Integer.parseInt(args[0]);
            int height = Integer.parseInt(args[1]);
            MazePathFinder canvas = new MazePathFinder(width, height);

            int x0 = MazeSolverUtils.getRandom(width);
            int x1 = MazeSolverUtils.getRandom(width);
            int y0 = MazeSolverUtils.getRandom(height);
            int y1 = MazeSolverUtils.getRandom(height);

            canvas.setMazeStartEnd(x0, y0, x1, y1);
            canvas.displayPath(x0, y0);
        }
        catch(Exception ex) {
            System.out.println("Error in execution " + ex.getMessage());
        }
    }
}
