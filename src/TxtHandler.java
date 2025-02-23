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

  private boolean validateInt(String s){
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean validateConsistency(String s){
    String trimmed = trimBoth(s);
    char label = trimmed.charAt(0);
    for (int i = 0; i < trimmed.length(); i++){
      if (trimmed.charAt(i) != label && trimmed.charAt(i) != ' '){
        return false;
      }
    } return true;
  }

  public Data handleInput(Scanner scanner) {
    String n = scanner.next();
    int N = validateInt(n) ? Integer.parseInt(n) : 0;
    String m = scanner.next();
    int M = validateInt(m) ? Integer.parseInt(m) : 0;
    String p = scanner.next();
    int P = validateInt(p) ? Integer.parseInt(p) : 0;

    if (N == 0 || M == 0 || P == 0) {
      System.err.println("Invalid input (N, M, P must be integers)");
      return null;
    }

    scanner.nextLine();

    // Baca tipe kasus (DEFAULT / CUSTOM / PYRAMID)
    String S = trimBoth(scanner.nextLine());
    if (!S.equals("DEFAULT") && !S.equals("CUSTOM") && !S.equals("PYRAMID")) {
      System.err.println("Invalid input (S must be DEFAULT, CUSTOM, or PYRAMID)");
      return null;
    }

    Block[] blocks = new Block[P];

    // Baca P blok
    String currentLine = "";
    int blockCtr = 1;
    for (int i = 0; i < P; i++) {
      if (!scanner.hasNextLine() && i < P - 1) {
        System.err.println("Invalid input (Not enough blocks to render, P is higher)");
        return null;
      }

      ArrayList<String> blockLines = new ArrayList<>();
      int maxLength;

      if (currentLine.isEmpty()) {
        if (!scanner.hasNextLine()) break;
        currentLine = trimEnd(scanner.nextLine());
      }
      char label = trimBoth(currentLine).charAt(0);
      blockLines.add(currentLine);
      maxLength = currentLine.length();

      // yang ini buat mastiin bloknya jadi partnya apa engga
      while (scanner.hasNextLine()) {
        String nextLine = trimEnd(scanner.nextLine());

        if (nextLine.isEmpty()) continue;

        char nextLabel = trimStart(nextLine).charAt(0);
        if (nextLabel != label) {
          currentLine = nextLine;
          blockCtr++;
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
        if (!validateConsistency(line)){
          System.err.println("Invalid input (Block consistency mismatch)");
          return null;
        }
        for (int k = 0; k < cols; k++) {
          shape[j][k] = (k < line.length()) ? line.charAt(k) : ' ';
        }
      }
      blocks[i] = new Block(shape);
    }
    if (blockCtr != P) {
      System.err.println("Invalid input (Block count mismatch, P is lower)");
      return null;
    } return new Data(N, M, P, S, blocks);
  } 
}