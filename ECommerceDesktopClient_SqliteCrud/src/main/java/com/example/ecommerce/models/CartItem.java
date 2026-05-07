package com.example.ecommerce.models;

import javafx.beans.property.*;

public class CartItem {
    private final LongProperty id = new SimpleLongProperty();
    private final LongProperty productId = new SimpleLongProperty();
    private final StringProperty productName = new SimpleStringProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final DoubleProperty unitPrice = new SimpleDoubleProperty();

    public CartItem() {}

    public CartItem(long id, long productId, String productName, int quantity, double unitPrice) {
        setId(id);
        setProductId(productId);
        setProductName(productName);
        setQuantity(quantity);
        setUnitPrice(unitPrice);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public long getProductId() { return productId.get(); }
    public LongProperty productIdProperty() { return productId; }
    public void setProductId(long productId) { this.productId.set(productId); }

    public String getProductName() { return productName.get(); }
    public StringProperty productNameProperty() { return productName; }
    public void setProductName(String productName) { this.productName.set(productName); }

    public int getQuantity() { return quantity.get(); }
    public IntegerProperty quantityProperty() { return quantity; }
    public void setQuantity(int quantity) { this.quantity.set(quantity); }

    public double getUnitPrice() { return unitPrice.get(); }
    public DoubleProperty unitPriceProperty() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice.set(unitPrice); }

    public double getLineTotal() {
        return getUnitPrice() * getQuantity();
    }
}

