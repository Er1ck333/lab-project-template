package net.openwebinars.java.mysql.crud.dao;

import net.openwebinars.java.mysql.crud.model.Categoria;
import net.openwebinars.java.mysql.crud.model.Producto;
import net.openwebinars.java.mysql.crud.pool.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDaoImpl implements ProductoDao {

    private static ProductoDaoImpl instance;

    private ProductoDaoImpl() {}

    public static ProductoDaoImpl getInstance() {
        if (instance == null) {
            instance = new ProductoDaoImpl();
        }
        return instance;
    }

    @Override
    public int add(Producto producto) throws SQLException {
        String sql = "INSERT INTO producto (nombre, precio, categoria_id) VALUES (?, ?, ?)";
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getCategoria().getId_Categoria());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    producto.setId_producto(rs.getInt(1));
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    @Override
    public Producto getById(int id) throws SQLException {
        String sql = "SELECT p.*, c.nombre AS categoria_nombre FROM producto p JOIN categoria c ON p.categoria_id = c.id WHERE p.id = ?";
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Categoria cat = new Categoria(rs.getInt("categoria_id"), rs.getString("categoria_nombre"));
                    return new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), cat);
                }
            }
        }
        return null;
    }

    @Override
    public List<Producto> getAll() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS categoria_nombre FROM producto p JOIN categoria c ON p.categoria_id = c.id";
        try (Connection conn = MyDataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Categoria cat = new Categoria(rs.getInt("categoria_id"), rs.getString("categoria_nombre"));
                productos.add(new Producto(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio"), cat));
            }
        }
        return productos;
    }

    @Override
    public int update(Producto producto) throws SQLException {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, categoria_id = ? WHERE id = ?";
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setInt(3, producto.getCategoria().getId_Categoria());
            stmt.setInt(4, producto.getId_producto());
            return stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}