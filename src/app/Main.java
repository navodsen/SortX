package app;

import csv.CSVReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sorting.MergeSort;
import sorting.InsertionSort;
import sorting.QuickSort;
import sorting.HeapSort;
import sorting.ShellSort;


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
        Button runAllSortsButton = new Button("Run All Sorts");


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

        // ================= All Sort Button =================
        runAllSortsButton.setOnAction(e -> {
            if (loadedData == null) {
                showError("No Data", "Please load a column first.");
                return;
            }

            try {
                StringBuilder sb = new StringBuilder();

                // Insertion Sort
                double[] arr1 = loadedData.clone();
                InsertionSort.sort(arr1);
                sb.append("✓ Insertion Sort completed\n");

                // Shell Sort
                double[] arr2 = loadedData.clone();
                ShellSort.sort(arr2);
                sb.append("✓ Shell Sort completed\n");

                // Merge Sort
                double[] arr3 = loadedData.clone();
                MergeSort.sort(arr3);
                sb.append("✓ Merge Sort completed\n");

                // Quick Sort
                double[] arr4 = loadedData.clone();
                QuickSort.sort(arr4);
                sb.append("✓ Quick Sort completed\n");

                // Heap Sort
                double[] arr5 = loadedData.clone();
                HeapSort.sort(arr5);
                sb.append("✓ Heap Sort completed\n");

                sb.append("\nAll 5 sorts completed successfully!");
                outputArea.setText(sb.toString());

            } catch (Exception ex) {
                showError("Sorting Error", ex.getMessage());
            }
        });



        // ================= Layout =================
        VBox root = new VBox(12,
                uploadButton,
                columnSelector,
                loadDataButton,
                runAllSortsButton,
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