import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import java.awt.image.BufferedImage;

public class Handler {
  private static final String INPUT_DIR = "../test/input/";
  private static Board lastBoard;

  private static String trimEnd(String s) {
    return s.replaceAll("\\s+$", "");
  }

  private static String trimStart(String s) {
    return s.replaceAll("^\\s+", "");
  }

  private static String trimBoth(String s) {
    return trimStart(trimEnd(s));
  }

  private boolean validateInt(String s) {
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private boolean validateConsistency(String s) {
    String trimmed = trimBoth(s);
    char label = trimmed.charAt(0);
    for (int i = 0; i < trimmed.length(); i++) {
      if (trimmed.charAt(i) != label && trimmed.charAt(i) != ' ') {
        return false;
      }
    }
    return true;
  }

  public static List<String> getTestCases() {
    File folder = new File(INPUT_DIR);
    if (folder.exists() && folder.isDirectory()) {
      return Arrays.asList(folder.list((dir, name) -> name.endsWith(".txt")));
    }
    return List.of();
  }

  public static String readTestCase(String fileName) {
    try {
      return new String(Files.readAllBytes(Paths.get(INPUT_DIR + fileName)));
    } catch (IOException e) {
      return "Error: Unable to load file!";
    }
  }

  public static boolean handleUpload(File file) {
    if (file == null || !file.exists() || !file.getName().endsWith(".txt")) {
      return false;
    }
    try {
      Files.copy(file.toPath(), Paths.get(INPUT_DIR + file.getName()));
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  public Data handleFile(String fileName) {
    try (Scanner fileScanner = new Scanner(new File(INPUT_DIR + fileName))) {
      return handleInput(fileScanner);
    } catch (IOException e) {
      System.err.println("File tidak ditemukan!");
      return null;
    }
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
    String S = trimBoth(scanner.nextLine());
    if (!S.equals("DEFAULT") && !S.equals("CUSTOM") && !S.equals("PYRAMID")) {
      System.err.println("Invalid input (S must be DEFAULT, CUSTOM, or PYRAMID)");
      return null;
    }

    Block[] blocks = new Block[P];
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
        if (!validateConsistency(line)) {
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
    }
    return new Data(N, M, P, S, blocks);
  }

  public static String[] parseOutput(String output, Board board){
    String[] lines = output.split("\\n");
    String status = "";
    String time = "";
    String attempts = "";
    String boardS = (board != null) ? board.boardToString() : "No solution board available.";

    for (String line : lines) {
      if (line.startsWith("Time taken:")) {
        time = line;
      } else if (line.startsWith("Total possibilities visited:")) {
        attempts = line;
      } else if (line.startsWith("Solution found") || line.startsWith("No solution found")) {
        status = line;
      }
    }
    return new String[]{status, boardS, time, attempts};
  }

  public static void saveSolutionToTxtGUI(String result, String board, String attempts, String time, Stage stage) {
    TextInputDialog dialog = new TextInputDialog("solution");
    dialog.setTitle("Save Solution");
    dialog.setHeaderText("Masukkan nama file untuk solusi");
    dialog.setContentText("Filename:");
    
    dialog.showAndWait().ifPresent(filename -> {
      File directory = new File("../test/output/");
      if (!directory.exists()) {
        directory.mkdirs();
      }
      File file = new File(directory, filename + ".txt");

      if (file != null) {
        try (FileWriter writer = new FileWriter(file)) {
          writer.write(result + "\n");
          writer.write(board + "\n");
          writer.write(attempts + "\n");
          writer.write(time + "\n");
        } catch (IOException e) {
          showAlert("Error", "Gagal menyimpan solusi!", Alert.AlertType.ERROR);
        }
      }
    });
  }

  public static void saveSolutionToTxtCLI(String result, String board, String attempts, String time) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Masukkan nama file untuk solusi (without extension): ");
    String filename = scanner.nextLine().trim();
    File directory = new File("../test/output/");
    if (!directory.exists()) {
      directory.mkdirs();
    }
    File file = new File(directory, filename + ".txt");


    try (FileWriter writer = new FileWriter(file)) {
      writer.write(result + "\n");
      writer.write(board + "\n");
      writer.write(attempts + "\n");
      writer.write(time + "\n");
      System.out.println("Solusi disimpan ke: " + file.getAbsolutePath());
    } catch (IOException e) {
      System.out.println("Error: Gagal menyimpan solusi");
    }
  }

  public static void saveSolutionToImageGUI(Canvas canvas, Stage stage) {
    if (canvas == null || canvas.getWidth() == 0 || canvas.getHeight() == 0) {
      showAlert("Error", "Canvas kosong! Tidak ada yang bisa disimpan.", Alert.AlertType.ERROR);
      return;
    }

    TextInputDialog dialog = new TextInputDialog("solution");
    dialog.setTitle("Save Image");
    dialog.setHeaderText("Masukkan nama file untuk solusi");
    dialog.setContentText("Filename:");

    dialog.showAndWait().ifPresent(filename -> {
      File directory = new File("../test/output/");
      if (!directory.exists()) {
        directory.mkdirs();
      }
      File file = new File(directory, filename + ".png");

      try {
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage); // Pastikan snapshot berhasil
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        ImageIO.write(bufferedImage, "png", file);
        showAlert("Sukses", "Gambar disimpan ke: " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
      } catch (IOException e) {
        showAlert("Error", "Yah gagal :(!", Alert.AlertType.ERROR);
      }
    });
  }

  public static String runSolver(String fileName) {
    Data data = new Handler().handleFile(fileName);
    if (data == null) {
      return "Error: Failed to read file!";
    }

    Board board = new Board(data.N, data.M);
    Solver solver = new Solver(board, data.blocks);
    lastBoard = board;

    return solver.solve();
  }

  public static Board getBoard() {
    return lastBoard;
  }

  private static void showAlert(String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}