# Maze Solver

A Java program that solves rectangular mazes using a backtracking algorithm. The solver finds a path from the start position to the end position and marks the solution path in the output.

## Features

- **File-based maze input**: Reads maze layouts from text files
- **Robust validation**: Ensures maze integrity and proper format
- **Backtracking algorithm**: Efficiently finds solutions using depth-first search
- **Visual output**: Displays both original and solved mazes
- **Comprehensive error handling**: Provides clear error messages for invalid inputs

## Maze Format

The maze should be stored in a text file using the following characters:

- `S` - Start position (exactly one required)
- `E` - End position (exactly one required)  
- `O` - Open path (walkable spaces)
- `-` - Wall (impassable barriers)

### Requirements

- Maze must be rectangular (all rows same length)
- Must contain exactly one start (`S`) and one end (`E`) position
- Only valid characters are `S`, `E`, `O`, and `-`

### Example Maze File

```
S-O-O-E
O-O-O-O
O-O---O
O-OOOOO
```

## Usage

### Compilation

```bash
javac MazeSolver.java
```

### Running the Program

```bash
java MazeSolver <maze_file>
```

Replace `<maze_file>` with the path to your maze text file.

### Example

```bash
java MazeSolver maze1.txt
```

## Output

The program displays:

1. **Original maze**: The maze as read from the input file
2. **Solved maze**: The maze with the solution path marked with `X`

### Sample Output

```
Original maze:
S-O-O-E
O-O-O-O
O-O---O
O-OOOOO

Solved maze:
S-X-X-E
O-X-X-X
O-X---X
O-XXXXX
```

## Algorithm

The solver uses a **backtracking algorithm** (depth-first search) with the following approach:

1. **Initialization**: Start at the `S` position and mark it as visited
2. **Exploration**: Try moving in four directions (up, right, down, left)
3. **Path marking**: Mark successful moves with `X` 
4. **Backtracking**: When no valid moves are available, backtrack and try alternative paths
5. **Solution**: Continue until the `E` position is reached

### Algorithm Features

- **Memory efficient**: Uses arrays to track the current path
- **Direction tracking**: Remembers which directions have been tried at each position
- **State restoration**: Properly restores maze state during backtracking

## Error Handling

The program handles various error conditions:

| Error Type | Description |
|------------|-------------|
| **File not found** | Input file doesn't exist |
| **Non-rectangular maze** | Rows have different lengths |
| **Invalid characters** | Characters other than `S`, `E`, `O`, `-` |
| **Missing start/end** | No `S` or `E` position found |
| **Multiple start/end** | More than one `S` or `E` position |
| **No solution** | No valid path from start to end |

## Class Structure

### `Maze` Class

- **Constructor**: `Maze(String file)` - Loads and validates maze from file
- **`loadMaze()`**: Reads file and populates maze array
- **`validateMaze()`**: Ensures maze has exactly one start and end
- **`solve()`**: Implements backtracking algorithm to find solution
- **`isValidMove()`**: Checks if a move is within bounds and to an open space
- **`print()`**: Displays the current maze state

### `MazeSolver` Class

- **`main()`**: Entry point, handles command line arguments and orchestrates solving

## Requirements

- **Java 8 or higher**
- Input maze file in the specified format

## Limitations

- Only supports rectangular mazes
- Single solution path (doesn't find multiple solutions)
- No diagonal movement (only up, down, left, right)

## Contributing

To extend this maze solver:

1. **Multiple solutions**: Modify to find and display all possible paths
2. **Diagonal movement**: Add diagonal directions to the movement array
3. **Different algorithms**: Implement A* or Dijkstra's algorithm for shortest path
4. **GUI interface**: Add a graphical interface for maze input and visualization
5. **Maze generation**: Add functionality to generate random solvable mazes
