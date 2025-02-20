public class Block {
  private final char[][] shape;

  public Block(char[][] shape) {
    this.shape = shape;
  }

  public char[][] getShape() {
    return shape;
  }

  public void printBlock() {
    for (char[] row : shape) {
      for (char cell : row) {
        System.out.print(cell);
      }
      System.out.println();
    }
  }

  public Block rotate() {
    int rows = shape.length;
    int cols = shape[0].length;
    char[][] rotated = new char[cols][rows];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
         rotated[j][rows - 1 - i] = shape[i][j];
      }
    }

    return new Block(rotated);
  }

  public Block mirror() {
    int rows = shape.length;
    int cols = shape[0].length;
    char[][] mirrored = new char[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        mirrored[i][cols - 1 - j] = shape[i][j];
      }
    }

    return new Block(mirrored);
  }
}