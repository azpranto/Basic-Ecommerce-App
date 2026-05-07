package com.example.ecommerce.services;

import com.example.ecommerce.models.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartService {

    public List<CartItem> list() {
        String sql = """
            SELECT ci.id, ci.product_id, p.name AS product_name, ci.quantity, p.price AS unit_price
            FROM cart_items ci
            JOIN products p ON p.id = ci.product_id
            ORDER BY ci.id DESC
            """;
        List<CartItem> out = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new CartItem(
                    rs.getLong("id"),
                    rs.getLong("product_id"),
                    rs.getString("product_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("unit_price")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list cart items", e);
        }
        return out;
    }

    public void addProduct(long productId, int quantity) {
        int qty = Math.max(1, quantity);

        String existsSql = "SELECT id, quantity FROM cart_items WHERE product_id = ?";
        String insertSql = "INSERT INTO cart_items(product_id, quantity) VALUES(?, ?)";
        String updateSql = "UPDATE cart_items SET quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseService.getConnection()) {
            conn.setAutoCommit(false);

            Long existingId = null;
            int existingQty = 0;
            try (PreparedStatement ps = conn.prepareStatement(existsSql)) {
                ps.setLong(1, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        existingId = rs.getLong("id");
                        existingQty = rs.getInt("quantity");
                    }
                }
            }

            if (existingId == null) {
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setLong(1, productId);
                    ps.setInt(2, qty);
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, existingQty + qty);
                    ps.setLong(2, existingId);
                    ps.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add to cart", e);
        }
    }

    public void updateQuantity(long cartItemId, int quantity) {
        int qty = Math.max(1, quantity);
        String sql = "UPDATE cart_items SET quantity=? WHERE id=?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setLong(2, cartItemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update cart item", e);
        }
    }

    public void remove(long cartItemId) {
        String sql = "DELETE FROM cart_items WHERE id=?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove cart item", e);
        }
    }

    public void clear() {
        String sql = "DELETE FROM cart_items";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear cart", e);
        }
    }
}

