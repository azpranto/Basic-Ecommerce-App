package com.example.ecommerce;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MainApp extends Application{

    @FXML
    private Label titleLeft;

    @FXML
    private Label LProducts;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/ecommerce/MainApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("ECommerce Desktop Client");

        String css = this.getClass().getResource("/com/example/ecommerce/css/Main.css").toExternalForm();
        scene.getStylesheets().add(css);


        stage.setScene(scene);
        // Set the minimum window size
        stage.setMinWidth(800);  // Example: Sidebar (250) + Content (550)
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }


    @FXML
    public void products(MouseEvent e) throws Exception {
        // 1. Get the Label/Node that was actually clicked
        Node sourceNode = (Node) e.getSource();

        // 2. Reach up and grab the Stage (Window) it belongs to
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ecommerce/fxml/products.fxml"));
        root = loader.load();
        scene = new Scene(root);

        String css = this.getClass().getResource("/com/example/ecommerce/css/Products.css").toExternalForm();
        scene.getStylesheets().add(css);
        currentStage.setScene(scene);
        currentStage.show();
    }

    @FXML
    public void cart(MouseEvent e) throws Exception  {
        // 1. Get the Label/Node that was actually clicked
        Node sourceNode = (Node) e.getSource();

        // 2. Reach up and grab the Stage (Window) it belongs to
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ecommerce/fxml/cart.fxml"));
        root = loader.load();
        scene = new Scene(root);

        String css = this.getClass().getResource("/com/example/ecommerce/css/cart.css").toExternalForm();
        scene.getStylesheets().add(css);
        currentStage.setScene(scene);
        currentStage.show();
    }

    @FXML
    public void orders(MouseEvent e) throws Exception {
        // 1. Get the Label/Node that was actually clicked
        Node sourceNode = (Node) e.getSource();

        // 2. Reach up and grab the Stage (Window) it belongs to
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ecommerce/fxml/orders.fxml"));
        root = loader.load();
        scene = new Scene(root);

        String css = this.getClass().getResource("/com/example/ecommerce/css/orders.css").toExternalForm();
        scene.getStylesheets().add(css);
        currentStage.setScene(scene);
        currentStage.show();
    }

    @FXML
    public void customers(MouseEvent e) throws Exception {
        // 1. Get the Label/Node that was actually clicked
        Node sourceNode = (Node) e.getSource();

        // 2. Reach up and grab the Stage (Window) it belongs to
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ecommerce/fxml/customers.fxml"));
        root = loader.load();
        scene = new Scene(root);

        String css = this.getClass().getResource("/com/example/ecommerce/css/customers.css").toExternalForm();
        scene.getStylesheets().add(css);
        currentStage.setScene(scene);
        currentStage.show();
    }

    @FXML
    public void dashboard(MouseEvent e) throws Exception {
        Node sourceNode = (Node) e.getSource();

        // 2. Reach up and grab the Stage (Window) it belongs to
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ecommerce/MainApp.fxml"));
        root = loader.load();
        scene = new Scene(root);

        String css = this.getClass().getResource("/com/example/ecommerce/css/Main.css").toExternalForm();
        scene.getStylesheets().add(css);
        currentStage.setScene(scene);
        currentStage.show();
    }


}
