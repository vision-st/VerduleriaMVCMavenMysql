package duoc.controlador;



import duoc.conexion.ConexionBD;
import duoc.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ControladorUsuarios {

    public Usuario autenticar(String nombreUsuario, String contrasenia){
        String sql = """
                 SELECT nombre_usuario, contrasenia, rol 
                 FROM usuarios 
                 WHERE nombre_usuario = ? AND contrasenia = ?;
               """;

        try(Connection conn = ConexionBD.obtenerConexion();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, nombreUsuario);
            stmt.setString(2,contrasenia);

            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    return new Usuario(
                            rs.getString("nombre_usuario"),
                            rs.getString("contrasenia"),
                            rs.getString("rol")
                    );
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}
