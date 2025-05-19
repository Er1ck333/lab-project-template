package net.openwebinars.java.mysql.crud.dao;

import net.openwebinars.java.mysql.crud.model.Categoria;

import java.sql.SQLException;
import java.util.List;

public interface CategoriaDao {

    int add(Categoria categoria) throws SQLException;

    Categoria getById(int id) throws SQLException;

    List<Categoria> getAll() throws SQLException;

    int update(Categoria categoria) throws SQLException;

    void delete(int id) throws SQLException;

}
