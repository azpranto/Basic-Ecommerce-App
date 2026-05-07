package com.example.ecommerce.services;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseService {
    private static final String DB_FILE = "ecommerce.db";
    private static final String JDBC_URL = "jdbc:sqlite:" + Path.of(DB_FILE).toAbsolutePath();

    private DatabaseService() {}

    public static void init() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  name TEXT NOT NULL,
                  category TEXT NOT NULL,
                  price REAL NOT NULL,
                  stock INTEGER NOT NULL,
                  description TEXT,
                  rating_avg REAL NOT NULL DEFAULT 0,
                  rating_count INTEGER NOT NULL DEFAULT 0
                )
                """);

            // Lightweight migrations for existing DBs (ignore if already applied)
            execIgnore(stmt, "ALTER TABLE products ADD COLUMN rating_avg REAL NOT NULL DEFAULT 0");
            execIgnore(stmt, "ALTER TABLE products ADD COLUMN rating_count INTEGER NOT NULL DEFAULT 0");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  name TEXT NOT NULL,
                  email TEXT,
                  phone TEXT,
                  address TEXT
                )
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cart_items (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  product_id INTEGER NOT NULL,
                  quantity INTEGER NOT NULL,
                  created_at TEXT NOT NULL DEFAULT (datetime('now')),
                  FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
                )
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  customer_name TEXT,
                  status TEXT NOT NULL DEFAULT 'PENDING',
                  payment_method TEXT NOT NULL DEFAULT 'CASH',
                  total_amount REAL NOT NULL DEFAULT 0,
                  created_at TEXT NOT NULL DEFAULT (datetime('now'))
                )
                """);

            execIgnore(stmt, "ALTER TABLE orders ADD COLUMN payment_method TEXT NOT NULL DEFAULT 'CASH'");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS order_items (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  order_id INTEGER NOT NULL,
                  product_id INTEGER NOT NULL,
                  quantity INTEGER NOT NULL,
                  unit_price REAL NOT NULL,
                  FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
                  FOREIGN KEY(product_id) REFERENCES products(id)
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize SQLite database", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    private static void execIgnore(Statement stmt, String sql) {
        try {
            stmt.execute(sql);
        } catch (SQLException ignored) {
            // Intentionally ignored (column/table may already exist)
        }
    }
}

