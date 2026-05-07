package com.example.ecommerce.controllers;

import com.example.ecommerce.models.Product;
import com.example.ecommerce.services.ProductService;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.Duration;

import java.util.Locale;

public class ProductDetailsController {
    private final ProductService productService = new ProductService();
    private Product product;
    private Runnable onRatingSaved = () -> {};

    @FXML private Label name;
    @FXML private Label category;
    @FXML private Label price;
    @FXML private Label stock;
    @FXML private Label description;
    @FXML private Label rating;
    @FXML private Spinner<Integer> ratingSpinner;
    @FXML private Button rateBtn;

    @FXML
    public void initialize() {
        ratingSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5, 1));
        ratingSpinner.setEditable(false);
    }

    public void setProduct(Product product) {
        this.product = product;
        render();
    }

    public void setOnRatingSaved(Runnable onRatingSaved) {
        this.onRatingSaved = onRatingSaved == null ? () -> {} : onRatingSaved;
    }

    @FXML
    private void onRate() {
        if (product == null) return;

        int stars = ratingSpinner.getValue();
        productService.addRating(product.getId(), stars);

        // Reload from DB to show updated avg/count
        product = productService.list(product.getName()).stream()
            .filter(p -> p.getId() == product.getId())
            .findFirst()
            .orElse(product);

        render();
        onRatingSaved.run();

        FadeTransition ft = new FadeTransition(Duration.millis(180), rateBtn);
        ft.setFromValue(1.0);
        ft.setToValue(0.65);
        ft.setAutoReverse(true);
        ft.setCycleCount(2);
        ft.play();
    }

    private void render() {
        if (product == null) return;

        name.setText(product.getName());
        category.setText(product.getCategory());
        price.setText(String.format(Locale.US, "%.2f", product.getPrice()));
        stock.setText(String.valueOf(product.getStock()));
        description.setText(product.getDescription() == null ? "" : product.getDescription());
        rating.setText(String.format(Locale.US, "%.1f / 5  (%d)", product.getRatingAvg(), product.getRatingCount()));
    }
}

