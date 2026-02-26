package duoc.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL= "jdbc:mysql://localhost:3306/verduleria?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "usuario_dev";
    private static final String CONTRASENA = "usuario_dev";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}
