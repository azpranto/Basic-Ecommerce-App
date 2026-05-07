package com.example.ecommerce.controllers;

import com.example.ecommerce.models.CartItem;
import com.example.ecommerce.services.CartService;
import com.example.ecommerce.services.OrderService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Locale;

public class CartController {
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();

    private final ObservableList<CartItem> items = FXCollections.observableArrayList();
    private CartItem selected;

    @FXML private TableView<CartItem> table;
    @FXML private TableColumn<CartItem, Number> colId;
    @FXML private TableColumn<CartItem, String> colProduct;
    @FXML private TableColumn<CartItem, Number> colQty;
    @FXML private TableColumn<CartItem, Number> colUnit;
    @FXML private TableColumn<CartItem, Number> colTotal;

    @FXML private Spinner<Integer> qtySpinner;
    @FXML private Button updateQtyBtn;
    @FXML private Button removeBtn;
    @FXML private Button checkoutBtn;
    @FXML private Button clearBtn;
    @FXML private Label cartTotal;
    @FXML private TextField customerNameField;
    @FXML private ChoiceBox<String> paymentChoice;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> d.getValue().idProperty());
        colProduct.setCellValueFactory(d -> d.getValue().productNameProperty());
        colQty.setCellValueFactory(d -> d.getValue().quantityProperty());
        colUnit.setCellValueFactory(d -> d.getValue().unitPriceProperty());
        colTotal.setCellValueFactory(d -> new javafx.beans.property.SimpleDoubleProperty(d.getValue().getLineTotal()));

        table.setItems(items);
        
        // Apply animations
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(updateQtyBtn);
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(removeBtn);
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(clearBtn);
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(checkoutBtn);

        qtySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1_000_000, 1, 1));
        qtySpinner.setEditable(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            selected = n;
            boolean has = n != null;
            updateQtyBtn.setDisable(!has);
            removeBtn.setDisable(!has);
            if (has) qtySpinner.getValueFactory().setValue(n.getQuantity());
        });

        updateQtyBtn.setDisable(true);
        removeBtn.setDisable(true);

        paymentChoice.getItems().setAll("Cash", "Debit Card", "Credit Card", "PayPal");
        paymentChoice.setValue("Cash");

        refresh();
    }

    @FXML
    private void onUpdateQty() {
        if (selected == null) return;
        cartService.updateQuantity(selected.getId(), qtySpinner.getValue());
        refresh();
    }

    @FXML
    private void onRemove() {
        if (selected == null) return;
        cartService.remove(selected.getId());
        refresh();
    }

    @FXML
    private void onClear() {
        cartService.clear();
        refresh();
    }

    @FXML
    private void onCheckout() {
        String payment = paymentChoice.getValue();
        if (payment == null || payment.isBlank()) {
            alert("Select a payment method.");
            return;
        }

        long orderId = orderService.checkoutFromCart(customerNameField.getText(), payment);
        if (orderId < 0) {
            alert("Cart is empty.");
            return;
        }
        alert("Order created. ID: " + orderId + " (status: PENDING, payment: " + payment + ")");
        customerNameField.clear();
        refresh();
    }

    private void refresh() {
        items.setAll(cartService.list());
        table.getSelectionModel().clearSelection();
        selected = null;
        updateQtyBtn.setDisable(true);
        removeBtn.setDisable(true);
        updateTotal();
        checkoutBtn.setDisable(items.isEmpty());
        clearBtn.setDisable(items.isEmpty());
    }

    private void updateTotal() {
        double total = items.stream().mapToDouble(CartItem::getLineTotal).sum();
        cartTotal.setText(String.format(Locale.US, "%.2f", total));
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}

