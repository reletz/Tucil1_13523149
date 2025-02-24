import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.Scanner;
import javax.xml.namespace.QName;

public class Main extends Application {
  private ListView<String> testCaseList;
  private TextArea fileContentArea;
  private Label resultLabel;
  private Canvas canvas;
  private static final int CELL_SIZE = 50;

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("IQ Puzzler Pro Solver (JavaFX)");

    // Panel kiri (Input)
    VBox leftPanel = new VBox(10);
    leftPanel.setPadding(new Insets(10));

    Label titleLabel = new Label("IQ Puzzler Pro Solver");
    Button uploadButton = new Button("Upload Test Case");
    testCaseList = new ListView<>();
    Button findButton = new Button("Find It!");
    Button saveTxtButton = new Button("Save .txt");
    Button savePngButton = new Button("Save .png");
    fileContentArea = new TextArea();
    fileContentArea.setEditable(false);

    leftPanel.getChildren().addAll(
      titleLabel, uploadButton, testCaseList, fileContentArea, findButton, saveTxtButton, savePngButton
    );

    // Panel kanan (Hasil dan File Content)
    VBox rightPanel = new VBox(10);
    rightPanel.setPadding(new Insets(10));
    Label resultTitle = new Label("Results");
    resultLabel = new Label("Searching Time: 0 ms | Cases: 0");
    canvas = new Canvas(600, 600);
    rightPanel.getChildren().addAll(resultTitle, resultLabel, canvas);

    // Tombol-tombol (adh ada kurang ga yak)
    uploadButton.setOnAction(e -> handleUpload(primaryStage));
    testCaseList.setOnMouseClicked(e -> displaySelectedFile());
    findButton.setOnAction(e -> runSolver());

    HBox root = new HBox(leftPanel, rightPanel);
    Scene scene = new Scene(root, 1000, 600);
    primaryStage.setScene(scene);
    primaryStage.show();

    saveTxtButton.setOnAction(e -> {
      Board board = Handler.getBoard();
      if (board != null) {
        String[] parsedData = Handler.parseOutput(resultLabel.getText(), board);
        Handler.saveSolutionToTxtGUI(parsedData[0], parsedData[1], parsedData[2], parsedData[3], primaryStage);
      } else {
        showAlert("Error", "Solusi gagal disimpan!", Alert.AlertType.ERROR);
      }
    });
      
    savePngButton.setOnAction(e -> {
      Board board = Handler.getBoard();
      if (board == null) {
        showAlert("Error", "Solusi gagal disimpan!", Alert.AlertType.ERROR);
        return;
      }
      updateCanvas(board);
      Handler.saveSolutionToImageGUI(canvas, primaryStage);
    });
    
    loadTestCases();
  }

  private void loadTestCases() {
    List<String> files = Handler.getTestCases();
    testCaseList.getItems().setAll(files);
    if (!files.isEmpty()) {
      testCaseList.getSelectionModel().selectFirst();
      displaySelectedFile();
    }
  }

  private void displaySelectedFile() {
    String selectedFile = testCaseList.getSelectionModel().getSelectedItem();
    if (selectedFile != null) {
      fileContentArea.setText(Handler.readTestCase(selectedFile));
    }
  }

  private void runSolver() {
    String selectedFile = testCaseList.getSelectionModel().getSelectedItem();
    if (selectedFile != null) {
      String result = Handler.runSolver(selectedFile);
      fileContentArea.setText(Handler.readTestCase(selectedFile));
      resultLabel.setText(result);
      updateCanvas(Handler.getBoard());
    } else {
      showAlert("Error", "No test case selected woi!!", Alert.AlertType.ERROR);
    }
  }


  private void updateCanvas(Board board) {
    if (board == null) return;

    GraphicsContext gc = canvas.getGraphicsContext2D();
    Color[][] colorGrid = board.getColorGrid();
    char[][] grid = board.grid;
    int rows = board.rows;
    int cols = board.cols;

    double cellWidth = canvas.getWidth() / cols;
    double cellHeight = canvas.getHeight() / rows;

    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        gc.setFill(colorGrid[i][j]);
        gc.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);

        gc.setFill(Color.BLACK);
        gc.fillText(String.valueOf(grid[i][j]), j * cellWidth + cellWidth / 3, i * cellHeight + cellHeight / 2);
      }
    }
  }


  private void handleUpload(Stage stage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
    File selectedFile = fileChooser.showOpenDialog(stage);

    if (selectedFile != null) {
      boolean success = Handler.handleUpload(selectedFile);
      if (success) {
        loadTestCases();
      } else {
        showAlert("Upload Failed", "Gagal upload :(", Alert.AlertType.ERROR);
      }
    }
  }

  private void showAlert(String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    while (true) {
      System.out.println("Selamat datang! Pilih opsi menjalankan program:");
      System.out.println("1. GUI");
      System.out.println("2. CLI");
      System.out.println("3. Keluar");
      System.out.print("Pilihan: ");
        
      String choiceS = input.next();
      while (!choiceS.equals("1") && !choiceS.equals("2") && !choiceS.equals("3")) { 
        System.out.println("Pilihan tidak valid!");
        System.out.print("Pilihan: ");
        choiceS = input.next();
      }
        
      int choice = Integer.parseInt(choiceS);
      if (choice == 1) {
        input.close();
        launch(args);
        return;
      } else if (choice == 2) {
        Handler handler = new Handler();
        Data data;

        input.nextLine();
        System.out.print("Masukkan nama file: ");
        String fileName = input.nextLine().trim();
        data = handler.handleFile(fileName);
        if (data == null) {
          continue;
        }

        Board board = new Board(data.N, data.M);
        Solver solver = new Solver(board, data.blocks);

        String result = solver.solve();
        System.out.println(result);
        board.printBoard();
        System.out.println("Ingin menyimpan data?");
        System.out.println("1. Simpan sebagai .txt");
        System.out.println("2. Simpan sebagai .png");
        System.out.println("3. Lewati.");
        choiceS = input.next();
        while (!choiceS.equals("1") && !choiceS.equals("2") && !choiceS.equals("3")) { 
          System.out.println("Pilihan tidak valid!");
          System.out.print("Pilihan: ");
          choiceS = input.next();
        }
        choice = Integer.parseInt(choiceS);
        if (choice == 1) {
          String[] parsedData = Handler.parseOutput(result, board);
          Handler.saveSolutionToTxtCLI(parsedData[0], parsedData[1], parsedData[2], parsedData[3]);
        } else if (choice == 2) {
          System.out.println("Maaf, fitur ini hanya bisa digunakan dalam GUI karena keterbatasan JavaFX.");
        }
      } else if (choice == 3) {
        input.close();
        System.exit(0);
        return;
      } else {
        System.out.println("Pilihan tidak valid!");
      }
    }
  }
}