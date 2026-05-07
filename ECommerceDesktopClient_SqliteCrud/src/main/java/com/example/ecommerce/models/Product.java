package com.example.ecommerce.models;

import javafx.beans.property.*;

public class Product {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty stock = new SimpleIntegerProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty ratingAvg = new SimpleDoubleProperty();
    private final IntegerProperty ratingCount = new SimpleIntegerProperty();

    public Product() {}

    public Product(long id, String name, String category, double price, int stock, String description, double ratingAvg, int ratingCount) {
        setId(id);
        setName(name);
        setCategory(category);
        setPrice(price);
        setStock(stock);
        setDescription(description);
        setRatingAvg(ratingAvg);
        setRatingCount(ratingCount);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }

    public String getCategory() { return category.get(); }
    public StringProperty categoryProperty() { return category; }
    public void setCategory(String category) { this.category.set(category); }

    public double getPrice() { return price.get(); }
    public DoubleProperty priceProperty() { return price; }
    public void setPrice(double price) { this.price.set(price); }

    public int getStock() { return stock.get(); }
    public IntegerProperty stockProperty() { return stock; }
    public void setStock(int stock) { this.stock.set(stock); }

    public String getDescription() { return description.get(); }
    public StringProperty descriptionProperty() { return description; }
    public void setDescription(String description) { this.description.set(description); }

    public double getRatingAvg() { return ratingAvg.get(); }
    public DoubleProperty ratingAvgProperty() { return ratingAvg; }
    public void setRatingAvg(double ratingAvg) { this.ratingAvg.set(ratingAvg); }

    public int getRatingCount() { return ratingCount.get(); }
    public IntegerProperty ratingCountProperty() { return ratingCount; }
    public void setRatingCount(int ratingCount) { this.ratingCount.set(ratingCount); }
}

