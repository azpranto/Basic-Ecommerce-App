package com.example.ecommerce.controllers;

import com.example.ecommerce.models.CartItem;
import com.example.ecommerce.models.Order;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.util.CsvExporter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class OrdersController {
    private final OrderService orderService = new OrderService();
    private final ProductService productService = new ProductService();
    private final ObservableList<Order> orders = FXCollections.observableArrayList();

    private Order selected;

    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, Number> colId;
    @FXML private TableColumn<Order, String> colCustomer;
    @FXML private TableColumn<Order, String> colStatus;
    @FXML private TableColumn<Order, String> colPayment;
    @FXML private TableColumn<Order, Number> colTotal;
    @FXML private TableColumn<Order, String> colCreated;
    @FXML private TableColumn<Order, Number> colQuantity;

    @FXML private ChoiceBox<String> statusChoice;
    @FXML private Button updateStatusBtn;
    @FXML private Button deleteBtn;
    @FXML private Label statusHint;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> d.getValue().idProperty());
        colCustomer.setCellValueFactory(d -> d.getValue().customerNameProperty());
        colStatus.setCellValueFactory(d -> d.getValue().statusProperty());
        colPayment.setCellValueFactory(d -> d.getValue().paymentMethodProperty());
        colTotal.setCellValueFactory(d -> d.getValue().totalAmountProperty());
        colCreated.setCellValueFactory(d -> d.getValue().createdAtProperty());
        colQuantity.setCellValueFactory(d -> d.getValue().totalQuantityProperty());

        table.setItems(orders);
        
        // Apply animations
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(updateStatusBtn);
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(deleteBtn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            selected = n;
            updateButtonStates();
            if (n != null) statusChoice.setValue(n.getStatus());
        });

        statusChoice.getItems().setAll("PENDING", "PAID", "SHIPPED", "CANCELLED");
        statusChoice.setValue("PENDING");
        updateButtonStates();

        refresh();
    }

    private void updateButtonStates() {
        if (selected == null) {
            updateStatusBtn.setDisable(true);
            deleteBtn.setDisable(true);
            statusHint.setText("");
            return;
        }

        String status = selected.getStatus();
        
        // Check if order is locked (PAID or CANCELLED)
        boolean isLocked = "PAID".equals(status) || "CANCELLED".equals(status);
        
        // Update status button is disabled if order is locked
        updateStatusBtn.setDisable(isLocked);
        
        // Delete button is only visible/enabled for PAID and CANCELLED orders
        deleteBtn.setDisable(!isLocked);
        
        if (isLocked) {
            statusHint.setText("⚠️ This order is locked and cannot be modified.");
        } else {
            statusHint.setText("✓ This order can be updated.");
        }
    }

    @FXML
    private void onUpdateStatus() {
        if (selected == null) return;
        
        String newStatus = statusChoice.getValue();
        String oldStatus = selected.getStatus();
        
        // If changing to PAID, decrease product stock
        if ("PAID".equals(newStatus) && !"PAID".equals(oldStatus)) {
            decreaseStockForOrder(selected.getId());
        }
        
        orderService.updateStatus(selected.getId(), newStatus);
        refresh();
    }

    private void decreaseStockForOrder(long orderId) {
        List<CartItem> items = orderService.getOrderItems(orderId);
        for (CartItem item : items) {
            productService.decreaseStock(item.getProductId(), item.getQuantity());
        }
    }

    @FXML
    private void onDeleteOrder() {
        if (selected == null) return;
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Order");
        confirm.setHeaderText("Are you sure you want to delete this order?");
        confirm.setContentText("Order ID: " + selected.getId());
        
        if (confirm.showAndWait().isPresent() && confirm.getResult() == ButtonType.OK) {
            orderService.deleteOrder(selected.getId());
            refresh();
        }
    }

    @FXML
    private void onRefresh() {
        refresh();
    }

    @FXML
    private void onExportCsv() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Export Orders to CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fc.setInitialFileName("orders.csv");
        File file = fc.showSaveDialog(table.getScene().getWindow());
        if (file == null) return;

        try {
            List<Order> rows = List.copyOf(orders);
            CsvExporter.export(
                Path.of(file.toURI()),
                List.of("ID", "Customer", "Status", "Quantity", "Payment", "Total", "Created"),
                rows,
                o -> List.of(
                    String.valueOf(o.getId()),
                    o.getCustomerName() != null ? o.getCustomerName() : "",
                    o.getStatus(),
                    String.valueOf(o.getTotalQuantity()),
                    o.getPaymentMethod(),
                    String.valueOf(o.getTotalAmount()),
                    o.getCreatedAt()
                )
            );
            alert("Orders exported successfully to " + file.getAbsolutePath());
        } catch (Exception e) {
            alert("Failed to export CSV: " + e.getMessage());
        }
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void refresh() {
        orders.setAll(orderService.list());
        table.getSelectionModel().clearSelection();
        selected = null;
        updateButtonStates();
    }
}

