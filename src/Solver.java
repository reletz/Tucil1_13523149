public class Solver {
  private final Board board;
  private final Block[] blocks;
  public int visited = 0;

  public Solver(Board board, Block[] blocks) {
    this.board = board;
    this.blocks = blocks;
  }

  public boolean solve(int blockIndex) {
    // base case
    if (blockIndex >= blocks.length) {
      return true;
    }

    // oks lanjut
    Block block = blocks[blockIndex];

    for (int flip = 0; flip < 2; flip++) {
      for (int rotation = 0; rotation < 4; rotation++) {
        for (int row = 0; row < board.rows; row++) {
          for (int col = 0; col < board.cols; col++) {
            visited++;

            if (board.canPlaceBlock(block, row, col)) {
              board.placeBlock(block, row, col);

              // lanjutin blok berikutnya
              if (solve(blockIndex + 1)) {
                return true;
              } 
              board.removeBlock(block, row, col); // Backtrack
            }
          }
        } 
        block = block.rotate();
      } 
      block = block.mirror();
    } return false;
  }

  public void solve(){
    long startTime = System.nanoTime();
    
    boolean isFound = solve(0);

    long endTime = System.nanoTime();
    double time = (endTime - startTime) / 1_000_000.0;

    if (isFound && board.isFull()) {
      System.out.println("Solution found:");
    } else if (!isFound) {
      System.out.println("No solution found (No combination of blocks can fit the board)");
    } else {
      System.out.println("No solution found (Board is not full)");
    }
    board.printBoard();

    System.out.println("Total possibilities visited: " + visited);
    System.out.printf("Time taken: %.3f ms\n", time);
  }
}  