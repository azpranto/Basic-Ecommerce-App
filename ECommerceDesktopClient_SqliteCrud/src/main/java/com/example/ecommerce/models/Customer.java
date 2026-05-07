package com.example.ecommerce.models;

import javafx.beans.property.*;

public class Customer {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();

    public Customer() {}

    public Customer(long id, String name, String email, String phone, String address) {
        setId(id);
        setName(name);
        setEmail(email);
        setPhone(phone);
        setAddress(address);
    }

    public long getId() { return id.get(); }
    public LongProperty idProperty() { return id; }
    public void setId(long id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public StringProperty nameProperty() { return name; }
    public void setName(String name) { this.name.set(name); }

    public String getEmail() { return email.get(); }
    public StringProperty emailProperty() { return email; }
    public void setEmail(String email) { this.email.set(email); }

    public String getPhone() { return phone.get(); }
    public StringProperty phoneProperty() { return phone; }
    public void setPhone(String phone) { this.phone.set(phone); }

    public String getAddress() { return address.get(); }
    public StringProperty addressProperty() { return address; }
    public void setAddress(String address) { this.address.set(address); }
}

