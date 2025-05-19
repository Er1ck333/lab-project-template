package net.openwebinars.java.mysql.crud.model;

import java.util.Objects;

public class Categoria {

    private int id_Categoria;
    private String nombre;

    public Categoria() {}


    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Categoria(int id_Categoria, String nombre) {
        this.id_Categoria = id_Categoria;
        this.nombre = nombre;
    }

    public int getId_Categoria() {
        return id_Categoria;
    }

    public void setId_Categoria(int id_Categoria) {
        this.id_Categoria = id_Categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return id_Categoria == categoria.id_Categoria && Objects.equals(nombre, categoria.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_Categoria, nombre);
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id_Categoria=" + id_Categoria +
                ", nombre='" + nombre + '\'' +
                '}';
    }

}
