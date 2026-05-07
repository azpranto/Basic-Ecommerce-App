package com.example.ecommerce.services;

import com.example.ecommerce.models.CartItem;
import com.example.ecommerce.models.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    public List<Order> list() {
        String sql = """
            SELECT o.id, o.customer_name, o.status, o.payment_method, o.total_amount, o.created_at, 
                   COALESCE(SUM(oi.quantity), 0) AS total_quantity
            FROM orders o
            LEFT JOIN order_items oi ON o.id = oi.order_id
            GROUP BY o.id, o.customer_name, o.status, o.payment_method, o.total_amount, o.created_at
            ORDER BY o.id DESC
            """;
        List<Order> out = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Order(
                    rs.getLong("id"),
                    rs.getString("customer_name"),
                    rs.getString("status"),
                    rs.getString("payment_method"),
                    rs.getDouble("total_amount"),
                    rs.getString("created_at"),
                    rs.getInt("total_quantity")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list orders", e);
        }
        return out;
    }

    public List<CartItem> getOrderItems(long orderId) {
        String sql = """
            SELECT oi.product_id, p.name AS product_name, oi.quantity, oi.unit_price
            FROM order_items oi
            JOIN products p ON p.id = oi.product_id
            WHERE oi.order_id = ?
            """;
        List<CartItem> items = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new CartItem(
                        0,
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get order items", e);
        }
        return items;
    }

    public int countPending() {
        String sql = "SELECT COUNT(*) AS c FROM orders WHERE status = 'PENDING'";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt("c") : 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count pending orders", e);
        }
    }

    public long checkoutFromCart(String customerName, String paymentMethod) {
        String cartSql = """
            SELECT ci.product_id, p.name AS product_name, ci.quantity, p.price AS unit_price
            FROM cart_items ci
            JOIN products p ON p.id = ci.product_id
            ORDER BY ci.id ASC
            """;
        String insertOrderSql = "INSERT INTO orders(customer_name, status, payment_method, total_amount) VALUES(?, 'PENDING', ?, ?)";
        String insertItemSql = "INSERT INTO order_items(order_id, product_id, quantity, unit_price) VALUES(?,?,?,?)";
        String clearCartSql = "DELETE FROM cart_items";

        try (Connection conn = DatabaseService.getConnection()) {
            conn.setAutoCommit(false);

            List<CartItem> items = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement(cartSql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new CartItem(
                        0,
                        rs.getLong("product_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                    ));
                }
            }

            if (items.isEmpty()) {
                conn.rollback();
                return -1;
            }

            double total = items.stream().mapToDouble(CartItem::getLineTotal).sum();

            long orderId;
            try (PreparedStatement ps = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, (customerName == null || customerName.isBlank()) ? null : customerName.trim());
                ps.setString(2, normalizePayment(paymentMethod));
                ps.setDouble(3, total);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("No order id returned");
                    orderId = keys.getLong(1);
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insertItemSql)) {
                for (CartItem item : items) {
                    ps.setLong(1, orderId);
                    ps.setLong(2, item.getProductId());
                    ps.setInt(3, item.getQuantity());
                    ps.setDouble(4, item.getUnitPrice());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement ps = conn.prepareStatement(clearCartSql)) {
                ps.executeUpdate();
            }

            conn.commit();
            return orderId;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to checkout from cart", e);
        }
    }

    private static String normalizePayment(String paymentMethod) {
        String pm = paymentMethod == null ? "" : paymentMethod.trim().toUpperCase();
        return switch (pm) {
            case "DEBIT CARD", "DEBIT" -> "DEBIT_CARD";
            case "CREDIT CARD", "CREDIT" -> "CREDIT_CARD";
            case "PAYPAL" -> "PAYPAL";
            case "CASH" -> "CASH";
            default -> "CASH";
        };
    }

    public void updateStatus(long orderId, String status) {
        String st = status == null ? "PENDING" : status.trim().toUpperCase();
        if (!List.of("PENDING", "PAID", "SHIPPED", "CANCELLED").contains(st)) {
            st = "PENDING";
        }
        String sql = "UPDATE orders SET status=? WHERE id=?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, st);
            ps.setLong(2, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update order status", e);
        }
    }

    public void deleteOrder(long orderId) {
        String sql = "DELETE FROM orders WHERE id=?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order", e);
        }
    }
}

