package duoc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/tienda_verduras?useSSL=false&serverTimezone=UTC";

    private static final String USUARIO = "usuario_dev_2";
    private static final String CONTRASENA = "usuario_dev_2";

    private ConexionBD() {}

    /**
     * Devuelve una conexión JDBC.
     * En el enfoque 1, el DAO la obtiene una vez en el constructor y la reutiliza.
     */
    public static Connection getConexion() {
        try {
            return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        } catch (SQLException e) {
            throw new RuntimeException("Error al conectar con la base de datos", e);
        }
    }
}