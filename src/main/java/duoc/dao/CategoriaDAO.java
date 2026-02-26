package duoc.dao;

import duoc.modelo.Categoria;
import duoc.modelo.Producto;

import java.util.List;

public interface CategoriaDAO {

    void insertar(Categoria categoria);
    void actualizar(Categoria categoria);
    void eliminar(int id);
    Categoria buscarPorId(int id);
    List<Categoria> listarTodos();
}


