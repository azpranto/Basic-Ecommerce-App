package com.example.ecommerce;

import com.example.ecommerce.services.DatabaseService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseService.init();

        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/MainApp.fxml"));
        javafx.scene.Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(MainApp.class.getResource("/css/app.css").toExternalForm());

        com.example.ecommerce.util.AnimationUtil.fadeIn(root, 100);

        stage.setTitle("ECommerce Desktop Client");
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

