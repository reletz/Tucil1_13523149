public class Board {
  final private int rows, cols;
  final private char[][] grid;

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
}