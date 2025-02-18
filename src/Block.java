public class Block {
  final private char label;
  final private boolean[][] shape;

  public Block(char label, boolean[][] shape) {
    this.label = label;
    this.shape = shape;
  }

  public char getLabel() {
    return label;
  }

  public boolean[][] getShape() {
    return shape;
  }

  public void printBlock() {
    for (boolean[] row: shape) {
      for (boolean cell: row) {
        System.out.print(cell ? label : ' ');
      }
      System.out.println();
    }
  }

  public boolean[][] rotate() {
    int rows = shape.length;
    int cols = shape[0].length;
    boolean[][] rotated = new boolean[cols][rows];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        rotated[j][rows - 1 - i] = shape[i][j];
      }
    }

    return rotated;
  }

  public boolean[][] mirror() {
    int rows = shape.length;
    int cols = shape[0].length;
    boolean[][] mirrored = new boolean[rows][cols];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        mirrored[i][cols - 1 - j] = shape[i][j];
      }
    }

    return mirrored;
  }
}
