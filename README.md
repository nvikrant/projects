projects
========

This will house all my projects. It does not have any language specific repo.
This is the code that I would like to share publicly.

(1) URLNormalizer: This is a common implementation of a URL Normalizer used by
                   web servers like Apache to normalize a URL submitted by the 
                   HTTP client before taking any action on a resource.
                   This follows the guidelines for RFC 3986 but with certain 
                   modifications. This has the following files:

    1. URL.cpp - This file implements all the normalizing methods on a given
                 url.

    2. URLTest.h - This file has the cppUnit bootstrapping for testing the 
                   normalization process on a set of urls.
   
    3. URLTestsFile.txt - A set of urls used for testing the normalization.
    
    Compilation: g++ URL.cpp -o URLTool -lCppUnit

    Execution: ./URLTool
                   


(2) MazeSolver: Maze solving is one of the cool projects to do in Computer Science.
                This is a simple maze solver application using awt and swing.
                The generation/solution algorithm used is DFS. The start and 
                end points in the maze are randomly choosen at program start.
                The main Classes in the application are as follows.
    
    1. Maze.java - This is the maze abstraction class which maintains the walls 
                   and all the visited Matrix. This is the Class used by the 
                   generation and path finder methods.

    2. MazeCanvas.java - This is the main Canvas UI abstraction, this maintains
                         all the UI generation code, frame re-painting for 
                         displaying the maze and the solution path of the maze.

    3. MazeGenerator.java - This class is an abstract class with the template
                            method called generate(). Different algorithms for 
                            generating the maze like DFS, RECURSIVE_DIVISION, etc..
                            have to subclass this and override generate().

    4. MazeGeneratorDFS.java - A concrete implementation of the DFS Algorithm to
                               generate a maze. Its the subclass of MazeGenerator.

    5. MazePathFinder.java - Its a subclass of MazeCanvas, since this application is 
                             a UI app, this class subclasses the MazeCanvas and get the
                             generated maze matrix from the Generator classes.
    
    6. MazeSolver.java - This is the driver class that begins the app, it takes two args
                         for width and height. The width and height should be > 1.
  
    Compilation: checkout the mazesolver folder and do a maven build.
                 'mvn clean compile assembly:single'

    Execution:   The target maven folder contains a jar 'MazeSolverDemo*-jar-with-dependencies.jar'
                 'java -jar MazeSolverDemo*-jar-with-dependencies.jar <width> <height>'

    TODO: Implement different maze generation algorithms like recursive divsion, hunt-and-kill etc..,
            
