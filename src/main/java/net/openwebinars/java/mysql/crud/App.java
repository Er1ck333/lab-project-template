package net.openwebinars.java.mysql.crud;

import net.openwebinars.java.mysql.crud.dao.EmpleadoDaiImpl;
import net.openwebinars.java.mysql.crud.dao.EmpleadoDao;
import net.openwebinars.java.mysql.crud.model.Empleado;
import net.openwebinars.java.mysql.crud.pool.MyDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class App {

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.init();
        //testDao();

    }


    public  static  void testDao() {

        EmpleadoDao dao = EmpleadoDaiImpl.getInstance();

        Empleado emp = new Empleado("Erick", "Pauchet García", LocalDate.of(2005, 7, 19),
                "Alumno", "erick.pauchet@gmail.com");

        try {
            int n = dao.add(emp);
            System.out.println("El numero de registros es: " + n);

            List<Empleado> empleados = dao.getAll();

            if (empleados.isEmpty()) {
                System.out.println("No hay empleados registrados");
            } else {
                empleados.forEach(System.out::println);
            }

            Empleado emp1 = dao.getById(1);

            System.out.println("\n" + emp1 + "\n");

            emp1.setFechaNacimiento(LocalDate.of(2005, 8, 19));

            n = dao.update(emp1);

            emp = dao.getById(1);

            System.out.println("\n" + emp1 + "\n");

            dao.delete(1);
            empleados = dao.getAll();
            if (empleados.isEmpty()) {
                System.out.println("No hay empleados registrados");
            } else {
                empleados.forEach(System.out::println);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void testpool(){
        try (Connection conn = MyDataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet tables = metaData.getTables(null, null, "%", types);
            while (tables.next()) {
                System.out.println(tables.getString("TABLE_NAME"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
