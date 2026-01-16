package app;

import csv.CSVReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sorting.MergeSort;

import java.io.File;

public class Main extends Application {

    private File selectedFile;
    private double[] loadedData;   // store loaded column

    @Override
    public void start(Stage stage) {

        Button uploadButton = new Button("Upload CSV");
        ComboBox<String> columnSelector = new ComboBox<>();
        columnSelector.setPromptText("Select Numeric Column");

        Button loadDataButton = new Button("Load Column Data");
        Button mergeSortButton = new Button("Merge Sort");

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(200);

        // ================= Upload Button =================
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
                    outputArea.setText("File loaded: " + selectedFile.getName() + "\n");
                } catch (Exception ex) {
                    showError("CSV Error", ex.getMessage());
                }
            }
        });

        // ================= Load Data Button =================
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
                loadedData = CSVReader.readNumericColumn(selectedFile, selectedColumn);
                outputArea.setText("Loaded " + loadedData.length + " values from column: " + selectedColumn + "\n");
            } catch (Exception ex) {
                showError("Data Error", ex.getMessage());
            }
        });

        // ================= Merge Sort Button =================
        mergeSortButton.setOnAction(e -> {
            if (loadedData == null) {
                showError("No Data", "Please load a column first.");
                return;
            }

            double[] dataCopy = loadedData.clone(); // protect original

            long start = System.nanoTime();
            MergeSort.sort(dataCopy);
            long end = System.nanoTime();

            StringBuilder sb = new StringBuilder();
            sb.append("Merge Sort Completed!\n");
            sb.append("Time: ").append(end - start).append(" ns\n\n");
            sb.append("Sorted Values:\n");

            for (int i = 0; i < Math.min(100, dataCopy.length); i++) {
                sb.append(dataCopy[i]).append("\n");
            }

            outputArea.setText(sb.toString());
        });

        // ================= Layout =================
        VBox root = new VBox(12,
                uploadButton,
                columnSelector,
                loadDataButton,
                mergeSortButton,
                outputArea
        );

        Scene scene = new Scene(root, 450, 400);

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
