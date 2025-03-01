package com.example.best_local_area;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 384, 400);
        stage.setTitle("BEST Local Area");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.setMaximized(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}