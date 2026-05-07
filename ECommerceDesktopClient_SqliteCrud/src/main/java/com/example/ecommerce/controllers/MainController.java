package com.example.ecommerce.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {
    @FXML private StackPane contentPane;
    @FXML private Label clockLabel;

    @FXML private Label navDashboard;
    @FXML private Label navProducts;
    @FXML private Label navCustomers;
    @FXML private Label navCart;
    @FXML private Label navOrders;

    private Timer clockTimer;

    @FXML
    public void initialize() {
        installNavAnimations(navDashboard);
        installNavAnimations(navProducts);
        installNavAnimations(navCustomers);
        installNavAnimations(navCart);
        installNavAnimations(navOrders);
        
        // Initialize clock
        startClock();
        
        loadView("/fxml/dashboard.fxml");
    }

    private void startClock() {
        clockTimer = new Timer(true);
        clockTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LocalDateTime now = LocalDateTime.now();
                String timeStr = now.format(DateTimeFormatter.ofPattern("📅 MMM dd, yyyy | 🕐 HH:mm:ss"));
                javafx.application.Platform.runLater(() -> clockLabel.setText(timeStr));
            }
        }, 0, 1000);
    }

    @FXML
    private void showDashboard() {
        loadView("/fxml/dashboard.fxml");
    }

    @FXML
    private void showProducts() {
        loadView("/fxml/products.fxml");
    }

    @FXML
    private void showCustomers() {
        loadView("/fxml/customers.fxml");
    }

    @FXML
    private void showCart() {
        loadView("/fxml/cart.fxml");
    }

    @FXML
    private void showOrders() {
        loadView("/fxml/orders.fxml");
    }

    private void loadView(String resource) {
        try {
            Node next = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource)));
            next.setOpacity(0);
            next.setScaleX(0.98);
            next.setScaleY(0.98);

            contentPane.getChildren().setAll(next);

            FadeTransition fade = new FadeTransition(Duration.millis(300), next);
            fade.setFromValue(0);
            fade.setToValue(1);

            javafx.animation.ScaleTransition scale = new javafx.animation.ScaleTransition(Duration.millis(300), next);
            scale.setFromX(0.98);
            scale.setFromY(0.98);
            scale.setToX(1.0);
            scale.setToY(1.0);

            new ParallelTransition(fade, scale).play();
            
            updateActiveNavItem(resource);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view " + resource, e);
        }
    }

    private void updateActiveNavItem(String resource) {
        navDashboard.getStyleClass().remove("active");
        navProducts.getStyleClass().remove("active");
        navCustomers.getStyleClass().remove("active");
        navCart.getStyleClass().remove("active");
        navOrders.getStyleClass().remove("active");

        if (resource.contains("dashboard")) navDashboard.getStyleClass().add("active");
        else if (resource.contains("products")) navProducts.getStyleClass().add("active");
        else if (resource.contains("customers")) navCustomers.getStyleClass().add("active");
        else if (resource.contains("cart")) navCart.getStyleClass().add("active");
        else if (resource.contains("orders")) navOrders.getStyleClass().add("active");
    }

    private void installNavAnimations(Label label) {
        if (label == null) return;
        label.getStyleClass().add("nav-item");

        label.setOnMouseEntered(e -> {
            if (!label.getStyleClass().contains("active")) {
                FadeTransition ft = new FadeTransition(Duration.millis(200), label);
                ft.setToValue(1.0);
                ft.play();
                label.setScaleX(1.02);
                label.setScaleY(1.02);
            }
        });
        label.setOnMouseExited(e -> {
            if (!label.getStyleClass().contains("active")) {
                label.setScaleX(1.0);
                label.setScaleY(1.0);
            }
        });
    }
}

