import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
      try {
        Scanner scanner = new Scanner(new File("../test/testdata.txt"));
        TxtHandler txtHandler = new TxtHandler();
        
        Data data = txtHandler.handleInput(scanner);

        System.out.println("Board Size: " + data.N + "x" + data.M);
        System.out.println("Puzzle Type: " + data.S);
        System.out.println("Total Blocks: " + data.P);
        
        for (Block block : data.blocks) {
            block.printBlock();
            System.out.println();
        }
        
        scanner.close();
      } catch (FileNotFoundException e) {
        System.out.println("File not found!");
      }
    }
}