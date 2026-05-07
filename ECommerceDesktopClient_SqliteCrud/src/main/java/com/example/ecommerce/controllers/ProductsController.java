package com.example.ecommerce.controllers;

import com.example.ecommerce.models.Product;
import com.example.ecommerce.services.CartService;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.util.CsvExporter;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class ProductsController {
    private final ProductService productService = new ProductService();
    private final CartService cartService = new CartService();
    private final ObservableList<Product> products = FXCollections.observableArrayList();

    private Product selected;

    @FXML private TextField searchField;
    @FXML private TableView<Product> table;
    @FXML private TableColumn<Product, Number> colId;
    @FXML private TableColumn<Product, String> colName;
    @FXML private TableColumn<Product, String> colCategory;
    @FXML private TableColumn<Product, Number> colPrice;
    @FXML private TableColumn<Product, Number> colStock;

    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField descriptionField;
    @FXML private Spinner<Double> priceSpinner;
    @FXML private Spinner<Integer> stockSpinner;

    @FXML private Button saveBtn;
    @FXML private Button deleteBtn;
    @FXML private Button addToCartBtn;
    @FXML private Button detailsBtn;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> d.getValue().idProperty());
        colName.setCellValueFactory(d -> d.getValue().nameProperty());
        colCategory.setCellValueFactory(d -> d.getValue().categoryProperty());
        colPrice.setCellValueFactory(d -> d.getValue().priceProperty());
        colStock.setCellValueFactory(d -> d.getValue().stockProperty());

        table.setItems(products);
        
        // Apply animations
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(saveBtn);
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(deleteBtn);
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(addToCartBtn);
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(detailsBtn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            selected = newV;
            if (newV == null) {
                deleteBtn.setDisable(true);
                addToCartBtn.setDisable(true);
                detailsBtn.setDisable(true);
                return;
            }
            deleteBtn.setDisable(false);
            addToCartBtn.setDisable(false);
            detailsBtn.setDisable(false);
            nameField.setText(newV.getName());
            categoryField.setText(newV.getCategory());
            descriptionField.setText(newV.getDescription());
            priceSpinner.getValueFactory().setValue(newV.getPrice());
            stockSpinner.getValueFactory().setValue(newV.getStock());
            saveBtn.setText("Update");
        });

        priceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1_000_000, 0, 1));
        priceSpinner.setEditable(true);
        stockSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1_000_000, 0, 1));
        stockSpinner.setEditable(true);

        PauseTransition debounce = new PauseTransition(Duration.millis(180));
        searchField.textProperty().addListener((obs, o, n) -> {
            debounce.setOnFinished(e -> refresh());
            debounce.playFromStart();
        });

        deleteBtn.setDisable(true);
        addToCartBtn.setDisable(true);
        detailsBtn.setDisable(true);
        refresh();
    }

    @FXML
    private void onSave() {
        String name = safe(nameField.getText());
        String category = safe(categoryField.getText());
        if (name.isBlank() || category.isBlank()) {
            alert("Name and Category are required.");
            return;
        }

        double price = priceSpinner.getValue();
        int stock = stockSpinner.getValue();
        String desc = safe(descriptionField.getText());

        if (selected == null) {
            Product p = new Product(0, name, category, price, stock, desc, 0.0, 0);
            productService.create(p);
        } else {
            selected.setName(name);
            selected.setCategory(category);
            selected.setPrice(price);
            selected.setStock(stock);
            selected.setDescription(desc);
            productService.update(selected);
        }

        clearForm();
        refresh();
    }

    @FXML
    private void onDelete() {
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete selected product?", ButtonType.CANCEL, ButtonType.OK);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                productService.delete(selected.getId());
                clearForm();
                refresh();
            }
        });
    }

    @FXML
    private void onClear() {
        clearForm();
        table.getSelectionModel().clearSelection();
    }

    @FXML
    private void onExportCsv() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Export Products to CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fc.setInitialFileName("products.csv");
        File file = fc.showSaveDialog(table.getScene().getWindow());
        if (file == null) return;

        try {
            List<Product> rows = List.copyOf(products);
            CsvExporter.export(
                Path.of(file.toURI()),
                List.of("ID", "Name", "Category", "Price", "Stock", "Description"),
                rows,
                p -> List.of(
                    String.valueOf(p.getId()),
                    p.getName(),
                    p.getCategory(),
                    String.valueOf(p.getPrice()),
                    String.valueOf(p.getStock()),
                    p.getDescription()
                )
            );
        } catch (Exception e) {
            alert("Failed to export CSV: " + e.getMessage());
        }
    }

    @FXML
    private void onAddToCart() {
        if (selected == null) return;
        cartService.addProduct(selected.getId(), 1);
        alert("Added to cart: " + selected.getName());
    }

    @FXML
    private void onShowDetails() {
        if (selected == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/product_details.fxml")));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/app.css")).toExternalForm());

            ProductDetailsController controller = loader.getController();
            controller.setProduct(selected);
            controller.setOnRatingSaved(() -> refresh());

            Stage stage = new Stage();
            stage.setTitle("Product Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setMinWidth(520);
            stage.setMinHeight(360);
            stage.showAndWait();
        } catch (IOException e) {
            alert("Failed to open details window: " + e.getMessage());
        }
    }

    private void refresh() {
        products.setAll(productService.list(searchField.getText()));
    }

    private void clearForm() {
        selected = null;
        nameField.clear();
        categoryField.clear();
        descriptionField.clear();
        priceSpinner.getValueFactory().setValue(0.0);
        stockSpinner.getValueFactory().setValue(0);
        saveBtn.setText("Add");
        deleteBtn.setDisable(true);
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}

