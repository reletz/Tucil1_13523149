public class Data {
  public final int N, M, P;
  public final String S;
  public final Block[] blocks;

  public Data(int N, int M, int P, String S, Block[] blocks) {
    this.N = N;
    this.M = M;
    this.P = P;
    this.S = S;
    this.blocks = blocks;
  }

  public void printData() {
    System.out.println(N + " " + M + " " + P);
    System.out.println(S);
    for (Block block : blocks) {
      block.printBlock();
    }
  }
}
