package com.example.ecommerce.models;

import javafx.beans.property.*;

public class Order {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty customerName = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty paymentMethod = new SimpleStringProperty();
    private final DoubleProperty totalAmount = new SimpleDoubleProperty();
    private final StringProperty createdAt = new SimpleStringProperty();
    private final IntegerProperty totalQuantity = new SimpleIntegerProperty();

    public Order() {}

    public Order(long id, String customerName, String status, String paymentMethod, double totalAmount, String createdAt) {
        setId(id);
        setCustomerName(customerName);
        setStatus(status);
        setPaymentMethod(paymentMethod);
        setTotalAmount(totalAmount);
        setCreatedAt(createdAt);
        setTotalQuantity(0);
    }

    public Order(long id, String customerName, String status, String paymentMethod, double totalAmount, String createdAt, int totalQuantity) {
        setId(id);
        setCustomerName(customerName);
        setStatus(status);
        setPaymentMethod(paymentMethod);
        setTotalAmount(totalAmount);
        setCreatedAt(createdAt);
        setTotalQuantity(totalQuantity);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public String getCustomerName() { return customerName.get(); }
    public StringProperty customerNameProperty() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName.set(customerName); }

    public String getStatus() { return status.get(); }
    public StringProperty statusProperty() { return status; }
    public void setStatus(String status) { this.status.set(status); }

    public String getPaymentMethod() { return paymentMethod.get(); }
    public StringProperty paymentMethodProperty() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod.set(paymentMethod); }

    public double getTotalAmount() { return totalAmount.get(); }
    public DoubleProperty totalAmountProperty() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount.set(totalAmount); }

    public String getCreatedAt() { return createdAt.get(); }
    public StringProperty createdAtProperty() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt.set(createdAt); }

    public int getTotalQuantity() { return totalQuantity.get(); }
    public IntegerProperty totalQuantityProperty() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity.set(totalQuantity); }
}

