package duoc.controlador;

import duoc.conexion.ConexionBD;
import duoc.modelo.CategoriaProducto;
import duoc.modelo.Producto;
import duoc.modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ControladorProductos {

   public void cargarProductosDesdeBD(DefaultTableModel model){

       String sql = "SELECT id, nombre, categoria, stock, valor FROM productos";

       try(Connection conn = ConexionBD.obtenerConexion();
           PreparedStatement stmt = conn.prepareStatement(sql)){
           ResultSet rs = stmt.executeQuery();
           model.setRowCount(0);

           while(rs.next()){
               model.addRow(new Object[]{
                       rs.getInt("id"),
                       rs.getString("nombre"),
                       rs.getString("categoria"),
                       rs.getInt("stock"),
                       rs.getInt("valor")
               });
           }
       }catch (Exception ex){
           ex.printStackTrace();
           JOptionPane.showMessageDialog(null, "Error al cargr productos desde la BD");
       }

   }

   public void agregarProducto(Producto producto, DefaultTableModel model){

       String sql = "INSERT INTO productos (nombre, categoria, stock, valor) VALUES (?, ?, ?, ?)";

       try(Connection conn = ConexionBD.obtenerConexion();
           PreparedStatement stmt = conn.prepareStatement(sql)){

           stmt.setString(1, producto.getNombre());
           stmt.setString(2, "ITEM");
           stmt.setInt(3, producto.getStock());
           stmt.setInt(4, producto.getValor());
           stmt.executeUpdate();
           cargarProductosDesdeBD(model);
       }catch (Exception ex){
           ex.printStackTrace();
           JOptionPane.showMessageDialog(null, "Error al cargr productos desde la BD");
       }

   }

}
