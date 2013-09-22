package org.nvikrant.mazesolver;
import org.nvikrant.mazesolver.utils.MazeSolverUtils;

/**
 * Created by nvikrant on 9/19/13.
 * This is the main driver for the program.
 * Its takes the cmd line args for width height of the maze.
 */
public class MazeSolver {
    public static void main(String[] args) {

        /** Now we accept only 2 args for width and height.
         *  Once we have different algorithms for generating
         *  the maze we can take a 3rd arg for the algorithm.
         */

        if(args.length != 2) {
            System.out.println("usage: java -jar mazesolver.jar <width> <height>");
            System.exit(1);
        }

        try {
            int width  = Integer.parseInt(args[0]);
            int height = Integer.parseInt(args[1]);
            MazePathFinder canvas = new MazePathFinder(width, height);

            /**
             * Randomize the start and end position dots
             * in the maze.
             */
            int x0 = MazeSolverUtils.getRandom(width);
            int x1 = MazeSolverUtils.getRandom(width);
            int y0 = MazeSolverUtils.getRandom(height);
            int y1 = MazeSolverUtils.getRandom(height);

            canvas.setMazeStartEnd(x0, y0, x1, y1);
            canvas.displayPath(x0, y0);
        }
        catch(Exception ex) {
            System.out.println("Error in Execution!!");
            System.exit(1);
        }
    }
}
