package app;

import csv.CSVReader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sorting.MergeSort;
import sorting.InsertionSort;
import sorting.QuickSort;
import sorting.HeapSort;
import sorting.ShellSort;
import performance.SortTimer;
import java.io.File;

public class Main extends Application {

    private File selectedFile;
    private double[] loadedData;

    @Override
    public void start(Stage stage) {

        Label titleLabel = new Label("Sorting Algorithm Analyzer");
        titleLabel.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #2c3e50;"
        );
        HBox titleBox = new HBox(titleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 20, 5, 20));

        Button uploadButton = new Button("Upload CSV File");
        uploadButton.setStyle(
                "-fx-background-color: #3498db; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 12 30; " +
                        "-fx-background-radius: 8;"
        );
        HBox uploadBox = new HBox(uploadButton);
        uploadBox.setAlignment(Pos.CENTER);
        uploadBox.setPadding(new Insets(5));

        ComboBox<String> columnSelector = new ComboBox<>();
        columnSelector.setPromptText("Choose a numeric column");
        columnSelector.setPrefWidth(300);
        columnSelector.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-padding: 7 10; " +
                        "-fx-background-radius: 5;"
        );

        Button loadDataButton = new Button("Load Column Data");
        loadDataButton.setStyle(
                "-fx-background-color: #27ae60; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-radius: 8;"
        );

        HBox secondRow = new HBox(10, columnSelector, loadDataButton);
        secondRow.setAlignment(Pos.CENTER);
        secondRow.setPadding(new Insets(10));

        Button runAllSortsButton = new Button("Run All Sorts");
        runAllSortsButton.setStyle(
                "-fx-background-color: #e74c3c; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-radius: 8;"
        );
        HBox thirdRow = new HBox(runAllSortsButton);
        thirdRow.setAlignment(Pos.CENTER);
        thirdRow.setPadding(new Insets(10));

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(300);
        VBox.setMargin(outputArea, new Insets(10));

        // Upload Button Action
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
                    outputArea.setText("CSV Error: " + ex.getMessage() + "\n");
                }
            }
        });

        // Load Data Button Action
        loadDataButton.setOnAction(e -> {
            String selectedColumn = columnSelector.getValue();

            if (selectedFile == null) {
                outputArea.setText("Error: No file selected. Please upload a CSV file first.\n");
                return;
            }

            if (selectedColumn == null) {
                outputArea.setText("Error: No column selected. Please select a column.\n");
                return;
            }

            try {
                loadedData = CSVReader.readNumericColumn(selectedFile, selectedColumn);
                outputArea.setText("Loaded " + loadedData.length + " values from column: " + selectedColumn + "\n");
            } catch (Exception ex) {
                outputArea.setText("Data Error: " + ex.getMessage() + "\n");
            }
        });

        // Run All Sorts Button Action
        runAllSortsButton.setOnAction(e -> {
            if (loadedData == null) {
                outputArea.setText("Error: No data loaded. Please load a column first.\n");
                return;
            }

            try {
                SortTimer timer = new SortTimer();

                outputArea.setText("Running sorting algorithms...\n\n");

                timer.measureSort("Insertion Sort", loadedData, InsertionSort::sort);
                timer.measureSort("Shell Sort", loadedData, ShellSort::sort);
                timer.measureSort("Merge Sort", loadedData, MergeSort::sort);
                timer.measureSort("Quick Sort", loadedData, QuickSort::sort);
                timer.measureSort("Heap Sort", loadedData, HeapSort::sort);

                outputArea.setText(timer.generateReport());

            } catch (Exception ex) {
                outputArea.setText("Sorting Error: " + ex.getMessage() + "\n");
            }
        });

        // Layout
        VBox root = new VBox(12,
                titleBox,
                uploadBox,
                secondRow,
                thirdRow,
                outputArea
        );

        Scene scene = new Scene(root, 500, 500);

        stage.setTitle("Sorting Algorithm Performance Analyzer");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}