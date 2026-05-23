import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Inter extends Application {

    private TextField brigadesField;
    private TextField vehiclesField;
    private TextField housesField;
    private TextArea outputArea;
    private Button runButton;

    @Override
    public void start(Stage primaryStage) {
        initializeFields();
        Scene scene = createScene();
        primaryStage.setTitle("Симуляция ЖКХ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeFields() {
        brigadesField = new TextField("5");
        brigadesField.getStyleClass().add("text-field");

        vehiclesField = new TextField("3");
        vehiclesField.getStyleClass().add("text-field");

        housesField = new TextField("10");
        housesField.getStyleClass().add("text-field");

        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.getStyleClass().add("output-area");

        runButton = new Button("Запустить симуляцию");
        runButton.getStyleClass().add("run-button");
        runButton.setOnAction(e -> runSimulation());
    }

    private Scene createScene() {
        GridPane inputGrid = createInputGrid();
        inputGrid.getStyleClass().add("input-grid");

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        Label titleLabel = new Label("🏠 Симуляция ЖКХ");
        titleLabel.getStyleClass().add("title-label");

        Label resultsLabel = new Label("📊 Результаты:");
        resultsLabel.getStyleClass().add("section-label");

        root.getChildren().addAll(titleLabel, inputGrid, runButton, resultsLabel, outputArea);

        Scene scene = new Scene(root, 700, 600);

        // Подключаем CSS
        String cssFile = getClass().getResource("/styles.css").toExternalForm();
        scene.getStylesheets().add(cssFile);

        return scene;
    }

    private GridPane createInputGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        grid.add(createLabel("Количество бригад:"), 0, 0);
        grid.add(brigadesField, 1, 0);

        grid.add(createLabel("Количество ТС:"), 0, 1);
        grid.add(vehiclesField, 1, 1);

        grid.add(createLabel("Количество домов:"), 0, 2);
        grid.add(housesField, 1, 2);

        return grid;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("normal-label");
        return label;
    }

    private void runSimulation() {
        runButton.setDisable(true);
        runButton.setText("Выполняется...");
        outputArea.setText("Запуск симуляции...\n");

        new Thread(() -> {
            try {
                int brigadesCount = parseInt(brigadesField.getText());
                int vehiclesCount = parseInt(vehiclesField.getText());
                int houseCount = parseInt(housesField.getText());

                validateInputs(brigadesCount, vehiclesCount, houseCount);

                String output = runSimulationWithOutput(brigadesCount, vehiclesCount, houseCount);

                Platform.runLater(() -> {
                    outputArea.setText(output);
                    runButton.setDisable(false);
                    runButton.setText("Запустить симуляцию");
                });

            } catch (ValidationException ex) {
                Platform.runLater(() -> {
                    showError(ex.getMessage());
                    resetButton();
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    showError("Ошибка: " + ex.getMessage());
                    ex.printStackTrace();
                    resetButton();
                });
            }
        }).start();
    }

    private int parseInt(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Пожалуйста, введите корректные числа");
        }
    }

    private void validateInputs(int brigades, int vehicles, int houses) {
        if (brigades <= 0) throw new ValidationException("Количество бригад должно быть больше 0");
        if (vehicles < 0) throw new ValidationException("Количество ТС не может быть отрицательным");
        if (houses <= 0) throw new ValidationException("Количество домов должно быть больше 0");
    }

    private String runSimulationWithOutput(int brigades, int vehicles, int houses) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream customOut = new PrintStream(baos, true, StandardCharsets.UTF_8);
        PrintStream originalOut = System.out;

        try {
            System.setOut(customOut);
            Simulation sim = new Simulation(brigades, vehicles, houses);
            sim.run();
            return baos.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(originalOut);
        }
    }

    private void resetButton() {
        runButton.setDisable(false);
        runButton.setText("Запустить симуляцию");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка ввода");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
}