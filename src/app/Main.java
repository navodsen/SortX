package app;

import csv.CSVReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Button uploadButton = new Button("Upload CSV");
        ComboBox<String> columnSelector = new ComboBox<>();
        columnSelector.setPromptText("Select Numeric Column");

        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select CSV File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );

            File file = fileChooser.showOpenDialog(stage);

            if (file != null) {
                try {
                    String[] headers = CSVReader.readHeaders(file);
                    columnSelector.getItems().clear();
                    columnSelector.getItems().addAll(headers);
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Failed to read CSV file");
                    alert.setContentText("Please select a valid CSV file.");
                    alert.showAndWait();
                }
            }
        });

        VBox root = new VBox(15);
        root.getChildren().addAll(uploadButton, columnSelector);

        Scene scene = new Scene(root, 400, 200);
        stage.setTitle("Sorting Algorithm Performance");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}