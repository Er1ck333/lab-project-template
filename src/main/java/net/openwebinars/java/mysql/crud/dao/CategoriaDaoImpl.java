package net.openwebinars.java.mysql.crud.dao;

import net.openwebinars.java.mysql.crud.model.Categoria;
import net.openwebinars.java.mysql.crud.pool.MyDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDaoImpl implements CategoriaDao {

    private static CategoriaDaoImpl instance;

    private CategoriaDaoImpl() {}

    public static CategoriaDaoImpl getInstance() {
        if (instance == null) {
            instance = new CategoriaDaoImpl();
        }
        return instance;
    }

    @Override
    public int add(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nombre) VALUES (?)";
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNombre());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    categoria.setId_Categoria(rs.getInt(1));
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    @Override
    public Categoria getById(int id) throws SQLException {
        String sql = "SELECT * FROM categoria WHERE id = ?";
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(rs.getInt("id"), rs.getString("nombre"));
                }
            }
        }
        return null;
    }

    @Override
    public List<Categoria> getAll() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categoria";
        try (Connection conn = MyDataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categorias.add(new Categoria(rs.getInt("id"), rs.getString("nombre")));
            }
        }
        return categorias;
    }

    @Override
    public int update(Categoria categoria) throws SQLException {
        String sql = "UPDATE categoria SET nombre = ? WHERE id = ?";
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setInt(2, categoria.getId_Categoria());
            return stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id = ?";
        try (Connection conn = MyDataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}