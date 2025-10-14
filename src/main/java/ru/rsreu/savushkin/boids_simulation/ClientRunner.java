package ru.rsreu.savushkin.boids_simulation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.rsreu.savushkin.boids_simulation.state.GameField;

public class ClientRunner extends Application {
    @Override
    public void start(Stage primaryStage) {
        GameField field = new GameField();
        // Инициализация (добавьте рыб и хищника)
        Canvas canvas = new Canvas(800, 600);
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        // Здесь добавить Controller для запуска потоков
    }

    public static void main(String[] args) {
        launch(args);
    }
}
