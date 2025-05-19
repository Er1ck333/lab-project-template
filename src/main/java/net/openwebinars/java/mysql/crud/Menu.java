package net.openwebinars.java.mysql.crud;

import net.openwebinars.java.mysql.crud.dao.*;
import net.openwebinars.java.mysql.crud.model.Categoria;
import net.openwebinars.java.mysql.crud.model.Empleado;
import net.openwebinars.java.mysql.crud.model.Producto;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

public class Menu {

    private KeyboardReader reader;
    private EmpleadoDao dao;
    private CategoriaDao categoriaDao;
    private ProductoDao productoDao;


    public Menu() {
        reader = new KeyboardReader();
        dao = EmpleadoDaiImpl.getInstance();
        categoriaDao = CategoriaDaoImpl.getInstance();
        productoDao = ProductoDaoImpl.getInstance();

    }
    public void init() {
        int option;

        do {
            menu();
            option = reader.nextInt();
            switch (option) {
                case 1:
                    listAll();
                    break;
                case 2:
                    listById();
                    break;
                case 3:
                    insert();
                    break;
                case 4:
                    update();
                    break;
                case 5:
                    delete();
                    break;
                case 6:
                    listarProductos();
                    break;
                case 7:
                    insertarProducto();
                    break;
                case 8:
                    eliminarProducto();
                    break;
                case 9:
                    listarCategorias();
                    break;
                case 10:
                    insertarCategoria();
                    break;
                case 11:
                    eliminarCategoria();
                    break;
                case 0:
                    System.out.println("\nSaliendo del programa...\n");
                    break;
                default:
                    System.out.println("\nOpcion no valida\n");
            }
        } while (option != 0);

    }

    public void menu() {

        System.out.println("SISTEMA DE GESTIÓN");
        System.out.println("==================\n");
        System.out.println("-> Introduzca una opcion de entre las siguientes: \n");

        System.out.println("0: Salir");

        System.out.println("\n-- EMPLEADOS --");
        System.out.println("1: Listar empleados");
        System.out.println("2: Listar un empleado por su ID");
        System.out.println("3: Insertar un empleado");
        System.out.println("4: Actualizar un empleado");
        System.out.println("5: Eliminar un empleado");

        System.out.println("\n-- PRODUCTOS --");
        System.out.println("6: Listar productos");
        System.out.println("7: Insertar producto");
        System.out.println("8: Eliminar producto");

        System.out.println("\n-- CATEGORÍAS --");
        System.out.println("9: Listar categorías");
        System.out.println("10: Insertar categoría");
        System.out.println("11: Eliminar categoría");

        System.out.print("\nOpción: ");
    }

    public void insert() {

        System.out.println("\nINSERCION DE UN NUEVO EMPLEADO");
        System.out.println("--------------------------------\n");

        System.out.println("\nIntroduzca el nombre del empleado:");
        String nombre = reader.nextLine();

        System.out.println("\nIntroduzca los apellidos del empleado:");
        String apellidos = reader.nextLine();

        System.out.println("\nIntroduzca la fecha de nacimiento del empleado (DD/MM/YYYY):");
        LocalDate fechaNacimiento = reader.nextLocalDate();

        System.out.println("\nIntroduzca el puesto del empleado:");
        String puesto = reader.nextLine();

        System.out.println("\nIntroduzca el email del empleado:");
        String email = reader.nextLine();

        try {
            dao.add(new Empleado(nombre, apellidos, fechaNacimiento, puesto, email));
            System.out.println("\nNuevo empleado registrado\n");
        } catch (SQLException e) {
            System.err.println("Error insertando al usuario " + e.getMessage());
        }

        System.out.println();
    }

    public void listAll() {
        System.out.println("\nLista de empleados");
        System.out.println("---------------------\n");

        try {
            List<Empleado> result = dao.getAll();

            if (result.isEmpty()){
                System.out.println("No hay empleados");
            } else {
                printCabeceraTablaEmpleado();
                result.forEach(this::printEmpleado);
            }
        } catch (SQLException e) {
            System.out.println("Error consultando la base de datos: " + e.getMessage());
        }
        System.out.println();
    }

    private void printCabeceraTablaEmpleado() {
        System.out.printf("%2s %30s %8s %10s %25s", "ID", "NOMBRE", "FEC. NAC.", "PUESTO", "EMAIL");
        System.out.println();
        IntStream.range(1, 100).forEach(x -> System.out.print("-"));
    }

    public void listById() {
        System.out.println("\nBusqueda por ID");
        System.out.println("-----------------------------\n");

        try {
            System.out.println("Introduzca el ID del empleado: ");
            int id = reader.nextInt();

            Empleado empleado = dao.getById(id);

            if (empleado == null) {
                System.out.println("No existe el empleado con ID " + id);
            } else {
                printCabeceraTablaEmpleado();
                printEmpleado(empleado);
            }

            System.out.println();

        } catch (SQLException e) {
            System.out.println("Error consultando la base de datos: " + e.getMessage());
        }
    }

    public void update() {
        System.out.println("\nActualizar empleado");
        System.out.println("------------------------------\n");

        try {

            System.out.printf("Introduzca el ID del empleado: ");
            int id = reader.nextInt();

            Empleado empleado = dao.getById(id);

            if (empleado == null) {
                System.out.println("No existe el empleado con ID " + id);
            } else {
                printCabeceraTablaEmpleado();
                printEmpleado(empleado);
                System.out.println();

                System.out.printf("Introduzca el nombre del empleado (%s): ", empleado.getNombre());
                String nombre = reader.nextLine();
                nombre = (nombre.isBlank()) ? empleado.getNombre() : nombre;

                System.out.printf("Introduzca apellidos del empleado (%s): ", empleado.getApellidos());
                String apellidos = reader.nextLine();
                apellidos = (apellidos.isBlank()) ? empleado.getApellidos() : apellidos;

                System.out.printf("Introduzca la fecha de nacimiento del empleado (dd/MM/aaaa) (%s): ", empleado.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                String strFechaNacimiento = reader.nextLine();
                LocalDate fechaNaciemiento = (strFechaNacimiento.isBlank()) ? empleado.getFechaNacimiento() : LocalDate.parse(strFechaNacimiento, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                System.out.printf("Introduzca el puesto del empleado (%s): ", empleado.getPuesto());
                String puesto = reader.nextLine();
                puesto = (puesto.isBlank()) ? empleado.getPuesto() : puesto;

                System.out.printf("Introduzca el email del empleado (%s): ", empleado.getEmail());
                String email = reader.nextLine();
                email = (email.isBlank()) ? empleado.getEmail() : email;

                empleado.setNombre(nombre);
                empleado.setApellidos(apellidos);
                empleado.setFechaNacimiento(fechaNaciemiento);
                empleado.setPuesto(puesto);
                empleado.setEmail(email);
                dao.update(empleado);

                System.out.println();
                System.out.printf("Empleado con ID %s actualizado con exito ", empleado.getId_empleado());
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println("Error consultando la base de datos: " + e.getMessage());
        }
    }

    public void delete() {
        System.out.println("\nEliminar empleado");
        System.out.println("------------------------------\n");

        try {

            System.out.println("Introduzca el ID del empleado: ");
            int id = reader.nextInt();

            System.out.printf("¿Esta seguro de querer borrar el empleado con ID=%s? (s/n): ", id);
            String borrar = reader.nextLine();

            if (borrar.equalsIgnoreCase("s")) {
                dao.delete(id);
                System.out.println("Empleado eliminado con ID " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error consultando la base de datos: " + e.getMessage());
        }

        System.out.println();

    }

    private void printEmpleado(Empleado emp) {
        System.out.println();
        System.out.printf("%2s %30s %9s %10s %25s\n",
                emp.getId_empleado(),
                emp.getNombre() + " " + emp.getApellidos(),
                emp.getFechaNacimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                emp.getPuesto(),
                emp.getEmail());
    }

    public void listarCategorias() {
        System.out.println("\nLISTA DE CATEGORÍAS\n--------------------");
        try {
            List<Categoria> categorias = categoriaDao.getAll();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorías registradas.");
            } else {
                categorias.forEach(cat -> System.out.printf("ID: %d | Nombre: %s\n", cat.getId_Categoria(), cat.getNombre()));
            }
        } catch (SQLException e) {
            System.err.println("Error consultando categorías: " + e.getMessage());
        }
        System.out.println();
    }

    public void insertarCategoria() {
        System.out.println("\nINSERTAR NUEVA CATEGORÍA\n--------------------------");
        System.out.println("Nombre de la categoría:");
        String nombre = reader.nextLine();
        try {
            categoriaDao.add(new Categoria(nombre));
            System.out.println("Categoría insertada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error insertando categoría: " + e.getMessage());
        }
        System.out.println();
    }

    public void eliminarCategoria() {
        System.out.println("\nELIMINAR CATEGORÍA\n---------------------");
        System.out.println("ID de la categoría a eliminar:");
        int id = reader.nextInt();
        try {
            categoriaDao.delete(id);
            System.out.println("Categoría eliminada correctamente.");
        } catch (SQLException e) {
            System.err.println("Error eliminando categoría: " + e.getMessage());
        }
        System.out.println();
    }

    public void listarProductos() {
        System.out.println("\nLISTA DE PRODUCTOS\n---------------------");
        try {
            List<Producto> productos = productoDao.getAll();
            if (productos.isEmpty()) {
                System.out.println("No hay productos registrados.");
            } else {
                productos.forEach(prod -> System.out.printf("ID: %d | Nombre: %s | Precio: %.2f | Categoría: %s\n",
                        prod.getId_producto(), prod.getNombre(), prod.getPrecio(),
                        prod.getCategoria() != null ? prod.getCategoria().getNombre() : "Sin categoría"));
            }
        } catch (SQLException e) {
            System.err.println("Error consultando productos: " + e.getMessage());
        }
        System.out.println();
    }

    public void insertarProducto() {
        System.out.println("\nINSERTAR NUEVO PRODUCTO\n--------------------------");
        System.out.println("Nombre del producto:");
        String nombre = reader.nextLine();
        System.out.println("Precio del producto:");
        double precio = reader.nextDouble();

        System.out.println("ID de la categoría:");
        int idCategoria = reader.nextInt();

        try {
            Categoria categoria = categoriaDao.getById(idCategoria);
            if (categoria == null) {
                System.out.println("Categoría no encontrada.");
                return;
            }
            productoDao.add(new Producto(nombre, precio, categoria));
            System.out.println("Producto insertado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error insertando producto: " + e.getMessage());
        }
        System.out.println();
    }

    public void eliminarProducto() {
        System.out.println("\nELIMINAR PRODUCTO\n------------------");
        System.out.println("ID del producto a eliminar:");
        int id = reader.nextInt();
        try {
            productoDao.delete(id);
            System.out.println("Producto eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error eliminando producto: " + e.getMessage());
        }
        System.out.println();
    }



    static class KeyboardReader {

        BufferedReader br;
        StringTokenizer st;

        public KeyboardReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {

            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IIOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        LocalDate nextLocalDate() {
            return LocalDate.parse(next(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        String nextLine() {
            try {
                return br.readLine();
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
                return "";
            }
        }


    }

}
