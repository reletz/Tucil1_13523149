import java.util.HashMap;

public class Board {
  final public int rows, cols;
  final public char[][] grid;
  final private HashMap<Character, String> colorMap;
  private static final String RESET = "\u001B[0m";
  private static final String[] COLORS = {
    "\u001B[30m",  // Black
    "\u001B[31m",  // Red
    "\u001B[32m",  // Green
    "\u001B[33m",  // Yellow
    "\u001B[34m",  // Blue
    "\u001B[35m",  // Magenta
    "\u001B[36m",  // Cyan
    "\u001B[37m",  // White
    "\u001B[90m",  // Bright Black (Gray)
    "\u001B[91m",  // Bright Red
    "\u001B[92m",  // Bright Green
    "\u001B[93m",  // Bright Yellow
    "\u001B[94m",  // Bright Blue
    "\u001B[95m",  // Bright Magenta
    "\u001B[96m",  // Bright Cyan
    "\u001B[97m",  // Bright White
    "\u001B[100m", // Background Gray
    "\u001B[101m", // Background Red
    "\u001B[102m", // Background Green
    "\u001B[103m", // Background Yellow
    "\u001B[104m", // Background Blue
    "\u001B[105m", // Background Magenta
    "\u001B[106m", // Background Cyan
    "\u001B[107m", // Background White
    "\u001B[41m",  // Background Dark Red
    "\u001B[42m"   // Background Dark Green
  };  

  public Board(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    this.grid = new char[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        grid[i][j] = ' ';
      }
    }

    char label = 'A';
    colorMap = new HashMap<>();
    for (int i = 0; i < 'Z'; i++, label++) {
      colorMap.put(label, COLORS[i % COLORS.length]);
    }
  }

  public void printBoard() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        char cell = grid[i][j];
        System.out.print((cell == ' ') ? cell : colorMap.get(cell) + cell + RESET);
      }
      System.out.println();
    }
  }

  public boolean isFull() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (grid[i][j] == ' ') {
          return false;
        }
      }
    }
    return true;
  }

  public void removeBlock(Block block, int x, int y) {
    char[][] shape = block.getShape();
    for (int i = 0; i < shape.length; i++) {
      for (int j = 0; j < shape[i].length; j++) {
        if (shape[i][j] != ' ') {
          grid[x + i][y + j] = ' ';
        }
      }
    }
  }
  
  public boolean canPlaceBlock(Block block, int x, int y) {
    char[][] shape = block.getShape();
    int blockRows = shape.length;
    int blockCols = shape[0].length;

    if (x + blockRows > rows || y + blockCols > cols) return false;

    for (int i = 0; i < blockRows; i++) {
      for (int j = 0; j < blockCols; j++) {
        if (shape[i][j] != ' ' && grid[x + i][y + j] != ' ') {
          return false;
        }
      }
    }
    return true;
  }

  public void placeBlock(Block block, int x, int y) {
    char[][] shape = block.getShape();
    for (int i = 0; i < shape.length; i++) {
      for (int j = 0; j < shape[i].length; j++) {
        if (shape[i][j] != ' ') {
          grid[x + i][y + j] = shape[i][j];
        }
      }
    }
  }
}