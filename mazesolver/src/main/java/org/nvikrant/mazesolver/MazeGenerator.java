package org.nvikrant.mazesolver;

/**
 * Created by nvikrant on 9/22/13.
 * This is the abstract class which defines the
 * template method, to override in the subclasses.
 */
public abstract class MazeGenerator {
    protected boolean northGrid[][];
    protected boolean southGrid[][];
    protected boolean westGrid[][];
    protected boolean eastGrid[][];
    protected boolean visitGrid[][];


    /** This is the generation method that all the
     *  generation classes should override. x and y
     *  start from (1,1) till the max(width, height).
     *
     *  @param x the x-coordinate of the start.
     *  @param y the y-coordinate of the start.
     */
     public void generate(int x, int y) {}

     public MazeGenerator(final boolean[][][] wallGrids,
                          final boolean[][] visitGrid) {
         this.northGrid = wallGrids[0];
         this.southGrid = wallGrids[1];
         this.westGrid  = wallGrids[2];
         this.eastGrid  = wallGrids[3];
         this.visitGrid = visitGrid;

     }

}
