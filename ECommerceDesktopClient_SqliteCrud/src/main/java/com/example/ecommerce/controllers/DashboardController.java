package com.example.ecommerce.controllers;

import com.example.ecommerce.services.CustomerService;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {
    private final ProductService productService = new ProductService();
    private final CustomerService customerService = new CustomerService();
    private final OrderService orderService = new OrderService();

    @FXML private Label productsCount;
    @FXML private Label customersCount;
    @FXML private Label pendingOrdersCount;

    @FXML
    public void initialize() {
        animateNumber(productsCount, productService.list("").size());
        animateNumber(customersCount, customerService.list("").size());
        animateNumber(pendingOrdersCount, orderService.countPending());
    }

    private void animateNumber(Label label, int target) {
        javafx.animation.Timeline timeline = new javafx.animation.Timeline();
        for (int i = 0; i <= target; i++) {
            final int value = i;
            javafx.animation.KeyFrame frame = new javafx.animation.KeyFrame(
                    javafx.util.Duration.millis(Math.min(1000, target > 0 ? (1000.0 / target) * i : 0)),
                    e -> label.setText(String.valueOf(value))
            );
            timeline.getKeyFrames().add(frame);
        }
        timeline.play();
    }
}

