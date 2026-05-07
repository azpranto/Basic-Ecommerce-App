package com.example.ecommerce.fxml;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class OrdersController {
    @FXML
    private VBox itemsWrapper;

    @FXML
    private GridPane titleGrid;

    @FXML
    private Parent root;
    private Scene scene;

    @FXML
    public void initialize() {
        Rectangle clip = new Rectangle();

        // Bind the clip size to the VBox size
        clip.widthProperty().bind(itemsWrapper.widthProperty());
        clip.heightProperty().bind(itemsWrapper.heightProperty());

        // Apply the clip - this is the "overflow: hidden"
        itemsWrapper.setClip(clip);

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

}
