package com.example.ecommerce.services;

import com.example.ecommerce.models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {

    public List<Customer> list(String query) {
        String q = query == null ? "" : query.trim();
        boolean hasQuery = !q.isBlank();

        String sql = """
            SELECT id, name, email, phone, address
            FROM customers
            %s
            ORDER BY id DESC
            """.formatted(hasQuery ? "WHERE name LIKE ? OR email LIKE ? OR phone LIKE ? OR address LIKE ?" : "");

        List<Customer> out = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (hasQuery) {
                String like = "%" + q + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
                ps.setString(4, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Customer(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list customers", e);
        }
        return out;
    }

    public Customer create(Customer c) {
        String sql = "INSERT INTO customers(name, email, phone, address) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getAddress());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getLong(1));
                }
            }
            return c;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create customer", e);
        }
    }

    public void update(Customer c) {
        String sql = "UPDATE customers SET name=?, email=?, phone=?, address=? WHERE id=?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getEmail());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getAddress());
            ps.setLong(5, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update customer", e);
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM customers WHERE id=?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete customer", e);
        }
    }
}

