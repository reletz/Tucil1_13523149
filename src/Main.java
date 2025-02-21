import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Masukkan nama file: ");
    String fileName = "../test/testdata.txt"; //scanner.nextLine();

    TxtHandler txtHandler = new TxtHandler();
    Data data;

    try (Scanner fileScanner = new Scanner(new java.io.File(fileName))) {
      data = txtHandler.handleInput(fileScanner);
      if (data == null) return;
    } catch (java.io.FileNotFoundException e) {
      System.err.println("File tidak ditemukan: " + e.getMessage());
      return;
    }

    Board board = new Board(data.N, data.M);
    Solver solver = new Solver(board, data.blocks);

    solver.solve();
  }
}