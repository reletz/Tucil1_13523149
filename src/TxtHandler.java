import java.util.ArrayList;
import java.util.Scanner;

public class TxtHandler {
    private static String trimEnd(String s) {
      return s.replaceAll("\\s+$", "");
    }

    private static String trimStart(String s) {
      return s.replaceAll("^\\s+", "");
    }

    private static String trimBoth(String s) {
      return trimStart(trimEnd(s));
    }

    public Data handleInput(Scanner scanner) {
      int N = scanner.nextInt();
      int M = scanner.nextInt();
      int P = scanner.nextInt();
      scanner.nextLine();

      // Baca tipe kasus (DEFAULT / CUSTOM / PYRAMID)
      String S = trimBoth(scanner.nextLine());

      Block[] blocks = new Block[P];

      // Baca P blok
      String currentLine = "";
      for (int i = 0; i < P; i++) {
        ArrayList<String> blockLines = new ArrayList<>();
        int maxLength = 0;

        // ambil line pertama buat tiap blok
        if (currentLine.isEmpty()) {
          if (!scanner.hasNextLine()) break;
          currentLine = trimEnd(scanner.nextLine());
        }
        char label = trimBoth(currentLine).charAt(0);
        blockLines.add(currentLine);
        maxLength = currentLine.length();

        // identifikasi blok selanjutnya
        while (scanner.hasNextLine()) {
          String nextLine = trimEnd(scanner.nextLine());

          if (nextLine.isEmpty()) continue;

          char nextLabel = trimStart(nextLine).charAt(0);
          if (nextLabel != label) {
            currentLine = nextLine;
            break;
          }

          blockLines.add(nextLine);
          maxLength = Math.max(maxLength, nextLine.length());
          currentLine = "";
        }

        int rows = blockLines.size();
        int cols = maxLength;

        char[][] shape = new char[rows][cols];
        for (int j = 0; j < rows; j++) {
          String line = blockLines.get(j);
          for (int k = 0; k < cols; k++) {
              shape[j][k] = (k < line.length()) ? line.charAt(k) : ' ';
          }
        }
        blocks[i] = new Block(shape);
      }

    return new Data(N, M, P, S, blocks);
  } 
}