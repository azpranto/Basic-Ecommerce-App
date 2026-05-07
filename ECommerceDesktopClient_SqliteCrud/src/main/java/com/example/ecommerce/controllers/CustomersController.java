package com.example.ecommerce.controllers;

import com.example.ecommerce.models.Customer;
import com.example.ecommerce.services.CustomerService;
import com.example.ecommerce.util.CsvExporter;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class CustomersController {
    private final CustomerService customerService = new CustomerService();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    private Customer selected;

    @FXML private TextField searchField;
    @FXML private TableView<Customer> table;
    @FXML private TableColumn<Customer, Number> colId;
    @FXML private TableColumn<Customer, String> colName;
    @FXML private TableColumn<Customer, String> colEmail;
    @FXML private TableColumn<Customer, String> colPhone;
    @FXML private TableColumn<Customer, String> colAddress;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    @FXML private Button saveBtn;
    @FXML private Button deleteBtn;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> d.getValue().idProperty());
        colName.setCellValueFactory(d -> d.getValue().nameProperty());
        colEmail.setCellValueFactory(d -> d.getValue().emailProperty());
        colPhone.setCellValueFactory(d -> d.getValue().phoneProperty());
        colAddress.setCellValueFactory(d -> d.getValue().addressProperty());

        table.setItems(customers);
        
        // Apply animations
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(saveBtn);
        com.example.ecommerce.util.AnimationUtil.applyHoverScale(deleteBtn);

        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            selected = n;
            if (n == null) {
                deleteBtn.setDisable(true);
                return;
            }
            deleteBtn.setDisable(false);
            nameField.setText(n.getName());
            emailField.setText(n.getEmail());
            phoneField.setText(n.getPhone());
            addressField.setText(n.getAddress());
            saveBtn.setText("Update");
        });

        PauseTransition debounce = new PauseTransition(Duration.millis(180));
        searchField.textProperty().addListener((obs, o, n) -> {
            debounce.setOnFinished(e -> refresh());
            debounce.playFromStart();
        });

        deleteBtn.setDisable(true);
        refresh();
    }

    @FXML
    private void onSave() {
        String name = safe(nameField.getText());
        if (name.isBlank()) {
            alert("Name is required.");
            return;
        }

        String email = safe(emailField.getText());
        String phone = safe(phoneField.getText());
        String address = safe(addressField.getText());

        if (selected == null) {
            Customer c = new Customer(0, name, email, phone, address);
            customerService.create(c);
        } else {
            selected.setName(name);
            selected.setEmail(email);
            selected.setPhone(phone);
            selected.setAddress(address);
            customerService.update(selected);
        }

        clearForm();
        refresh();
    }

    @FXML
    private void onDelete() {
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete selected customer?", ButtonType.CANCEL, ButtonType.OK);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                customerService.delete(selected.getId());
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
        fc.setTitle("Export Customers to CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fc.setInitialFileName("customers.csv");
        File file = fc.showSaveDialog(table.getScene().getWindow());
        if (file == null) return;

        try {
            List<Customer> rows = List.copyOf(customers);
            CsvExporter.export(
                Path.of(file.toURI()),
                List.of("ID", "Name", "Email", "Phone", "Address"),
                rows,
                c -> List.of(
                    String.valueOf(c.getId()),
                    c.getName(),
                    c.getEmail(),
                    c.getPhone(),
                    c.getAddress()
                )
            );
        } catch (Exception e) {
            alert("Failed to export CSV: " + e.getMessage());
        }
    }

    private void refresh() {
        customers.setAll(customerService.list(searchField.getText()));
    }

    private void clearForm() {
        selected = null;
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
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

