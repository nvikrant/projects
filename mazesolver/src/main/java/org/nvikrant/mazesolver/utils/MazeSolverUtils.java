package org.nvikrant.mazesolver.utils;

/**
 * Created by nvikrant on 9/19/13.
 */
public class MazeSolverUtils {

    public static enum MAZEGEN_ALGO { DFS, RECURSIVE_DIVISION, GROWING_TREE};

    public static int getRandom(int range){
        return (int)Math.ceil(Math.random() * range);
    }
}
