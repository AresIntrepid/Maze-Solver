import java.io.*;
import java.util.Scanner;

class Maze {
    private char[][] maze;
    private int startRow = -1;
    private int startCol = -1;
    private int endRow = -1;
    private int endCol = -1;
    private int rows, cols;
    
    public Maze(String file) throws FileNotFoundException {
        loadMaze(file);
        validateMaze();
    }
    
    private void loadMaze(String file) throws FileNotFoundException {
        Scanner dimensionScanner = new Scanner(new FileReader(file));
        
        int totalRows = 0;
        int totalColumns = 0;
        
        while (dimensionScanner.hasNextLine()) {
            String currentLine = dimensionScanner.nextLine();
            if (totalColumns == 0) {
                totalColumns = currentLine.length();
            } else if (currentLine.length() != totalColumns) {
                dimensionScanner.close();
                throw new IllegalArgumentException("ERROR: Maze is not rectangular");
            }
            totalRows++;
        }
        dimensionScanner.close();
        
        rows = totalRows;
        cols = totalColumns;
        maze = new char[totalRows][totalColumns];
        
        Scanner mazeScanner = new Scanner(new FileReader(file));
        int rowIndex = 0;
        while (mazeScanner.hasNextLine()) {
            String currentLine = mazeScanner.nextLine();
            for (int columnIndex = 0; columnIndex < totalColumns; columnIndex++) {
                char c = currentLine.charAt(columnIndex);
                if (c != 'S' && c != 'E' && c != 'O' && c != '-') {
                    mazeScanner.close();
                    throw new IllegalArgumentException("ERROR: Invalid character in maze: " + c);
                }
                maze[rowIndex][columnIndex] = c;
                if (c == 'S') {
                    startRow = rowIndex;
                    startCol = columnIndex;
                } else if (c == 'E') {
                    endRow = rowIndex;
                    endCol = columnIndex;
                }
            }
            rowIndex++;
        }
        mazeScanner.close();
    }
    
    private void validateMaze() {
        boolean foundStart = false;
        boolean foundEnd = false;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 'S') {
                    if (foundStart) {
                        throw new IllegalArgumentException("ERROR: Multiple start positions found");
                    }
                    foundStart = true;
                } else if (maze[i][j] == 'E') {
                    if (foundEnd) {
                        throw new IllegalArgumentException("ERROR: Multiple end positions found");
                    }
                    foundEnd = true;
                } 
            }
        }
        if (!foundStart) {
            throw new IllegalArgumentException("ERROR: No start position found");
        }
        if (!foundEnd) {
            throw new IllegalArgumentException("ERROR: No end position found");
        }
    }

    public void solve() {
        // Initialize arrays for tracking the path
        int[] currentPath = new int[rows * cols * 2];  // Stores row,col pairs
        boolean[][] visited = new boolean[rows][cols];
        int pathLength = 0;
        
        // Directions: up, right, down, left
        int[] dirRow = {-1, 0, 1, 0};
        int[] dirCol = {0, 1, 0, -1};
        
        // Start position
        currentPath[pathLength * 2] = startRow;
        currentPath[pathLength * 2 + 1] = startCol;
        pathLength++;
        visited[startRow][startCol] = true;
        
        // Keep track of which direction we tried at each position
        int[] lastTriedDir = new int[rows * cols];
        int currentDir = 0;
        
        boolean solved = false;
        
        while (pathLength > 0) {
            // Get current position
            int currentRow = currentPath[(pathLength - 1) * 2];
            int currentCol = currentPath[(pathLength - 1) * 2 + 1];
            
            // Check if we reached the end
            if (currentRow == endRow && currentCol == endCol) {
                solved = true;
                break;
            }
            
            // Try to move in a direction we haven't tried yet
            boolean moved = false;
            while (currentDir < 4) {
                int newRow = currentRow + dirRow[currentDir];
                int newCol = currentCol + dirCol[currentDir];
                
                if (isValidMove(newRow, newCol) && !visited[newRow][newCol]) {
                    // Mark current position with X if it's not start or end
                    if (maze[currentRow][currentCol] != 'S' && 
                        maze[currentRow][currentCol] != 'E') {
                        maze[currentRow][currentCol] = 'X';
                    }
                    
                    // Add new position to path
                    currentPath[pathLength * 2] = newRow;
                    currentPath[pathLength * 2 + 1] = newCol;
                    lastTriedDir[pathLength - 1] = currentDir;
                    pathLength++;
                    
                    visited[newRow][newCol] = true;
                    currentDir = 0;  // Reset direction for new position
                    moved = true;
                    break;
                }
                currentDir++;
            }
            
            // If we couldn't move in any direction, backtrack
            if (!moved) {
                pathLength--;
                if (pathLength > 0) {
                    // Mark the backtracked position as O if it's not start or end
                    if (maze[currentRow][currentCol] != 'S' && 
                        maze[currentRow][currentCol] != 'E') {
                        maze[currentRow][currentCol] = 'O';
                    }
                    // Continue from the last tried direction at the previous position
                    currentDir = lastTriedDir[pathLength - 1] + 1;
                }
            }
        }
        
        if (!solved) {
            throw new IllegalStateException("ERROR: No valid path found from start to end");
        }
        
        // Clean up the maze - restore original O's while keeping the successful path marked with X
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == 'O' || maze[i][j] == '-') {
                    // Check if this position is in the final path
                    boolean inFinalPath = false;
                    for (int k = 0; k < pathLength * 2; k += 2) {
                        if (i == currentPath[k] && j == currentPath[k + 1]) {
                            inFinalPath = true;
                            if (maze[i][j] != 'S' && maze[i][j] != 'E') {
                                maze[i][j] = 'X';  // Mark successful path
                            }
                            break;
                        }
                    }
                    // If not in final path and was originally O, restore it
                    if (!inFinalPath && maze[i][j] == 'O') {
                        maze[i][j] = 'O';  // Keep original O's
                    }
                    // If not in final path and was originally -, restore it
                    if (!inFinalPath && maze[i][j] == '-') {
                        maze[i][j] = '-';
                    }
                }
            }
        }
    }
    
    private boolean isValidMove(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return false;
        }
        return maze[row][col] == 'O' || maze[row][col] == 'E';
    }
    
    public void print() {
        for (int rowIndex = 0; rowIndex < maze.length; rowIndex++) {
            for (int columnIndex = 0; columnIndex < maze[rowIndex].length; columnIndex++) {
                System.out.print(maze[rowIndex][columnIndex]);
            }
            System.out.println();
        }
    }
}

public class MazeSolver {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("ERROR: No input file provided");
            return;
        }
        
        try {
            Maze maze = new Maze(args[0]);
            System.out.println("Original maze:");
            maze.print();
            maze.solve();
            System.out.println("\nSolved maze:");
            maze.print();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found - " + args[0]);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}