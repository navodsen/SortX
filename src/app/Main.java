package app;

import csv.CSVReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private File selectedFile;

    @Override
    public void start(Stage stage) {

        Button uploadButton = new Button("Upload CSV");
        ComboBox<String> columnSelector = new ComboBox<>();
        columnSelector.setPromptText("Select Numeric Column");

        Button loadDataButton = new Button("Load Column Data");

        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select CSV File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );

            selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                try {
                    columnSelector.getItems().clear();
                    columnSelector.getItems().addAll(
                            CSVReader.getNumericColumns(selectedFile)
                    );
                } catch (Exception ex) {
                    showError("CSV Error", ex.getMessage());
                }
            }
        });

        loadDataButton.setOnAction(e -> {
            String selectedColumn = columnSelector.getValue();

            if (selectedFile == null) {
                showError("No File Selected", "Please upload a CSV file first.");
                return;
            }

            if (selectedColumn == null) {
                showError("No Column Selected", "Please select a column.");
                return;
            }

            try {
                double[] data = CSVReader.readNumericColumn(selectedFile, selectedColumn);
                System.out.println("Loaded " + data.length + " numeric values.");
            } catch (Exception ex) {
                showError("Data Error", ex.getMessage());
            }
        });

        VBox root = new VBox(12, uploadButton, columnSelector, loadDataButton);
        Scene scene = new Scene(root, 400, 250);

        stage.setTitle("Sorting Algorithm Performance");
        stage.setScene(scene);
        stage.show();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
