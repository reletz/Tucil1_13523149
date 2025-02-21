public class Board {
  final public int rows, cols;
  final public char[][] grid;

  public Board(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    this.grid = new char[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        grid[i][j] = ' ';
      }
    }
  }

  public void printBoard() {
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        System.out.print(grid[i][j]);
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