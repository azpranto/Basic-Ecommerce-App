package com.example.ecommerce.services;

import com.example.ecommerce.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    public List<Product> list(String query) {
        String q = query == null ? "" : query.trim();
        boolean hasQuery = !q.isBlank();

        String sql = """
            SELECT id, name, category, price, stock, description, rating_avg, rating_count
            FROM products
            %s
            ORDER BY id DESC
            """.formatted(hasQuery ? "WHERE name LIKE ? OR category LIKE ? OR description LIKE ?" : "");

        List<Product> out = new ArrayList<>();
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (hasQuery) {
                String like = "%" + q + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getDouble("rating_avg"),
                        rs.getInt("rating_count")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list products", e);
        }
        return out;
    }

    public Product getById(long id) {
        String sql = """
            SELECT id, name, category, price, stock, description, rating_avg, rating_count
            FROM products
            WHERE id = ?
            """;
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Product(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getString("description"),
                        rs.getDouble("rating_avg"),
                        rs.getInt("rating_count")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get product by id", e);
        }
        return null;
    }

    public Product create(Product p) {
        String sql = "INSERT INTO products(name, category, price, stock, description) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getDescription());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getLong(1));
                }
            }
            return p;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create product", e);
        }
    }

    public void update(Product p) {
        String sql = "UPDATE products SET name=?, category=?, price=?, stock=?, description=? WHERE id=?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getCategory());
            ps.setDouble(3, p.getPrice());
            ps.setInt(4, p.getStock());
            ps.setString(5, p.getDescription());
            ps.setLong(6, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update product", e);
        }
    }

    public void decreaseStock(long productId, int quantity) {
        String sql = "UPDATE products SET stock = MAX(0, stock - ?) WHERE id = ?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            // SQLite doesn't have MAX function in UPDATE, so we'll do it manually
            Product product = getById(productId);
            if (product != null) {
                int newStock = Math.max(0, product.getStock() - quantity);
                product.setStock(newStock);
                update(product);
            }
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete product", e);
        }
    }

    public void addRating(long productId, int stars) {
        int s = Math.max(1, Math.min(5, stars));
        String sql = """
            UPDATE products
            SET rating_avg = ((rating_avg * rating_count) + ?) / (rating_count + 1),
                rating_count = rating_count + 1
            WHERE id = ?
            """;
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s);
            ps.setLong(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to rate product", e);
        }
    }
}

