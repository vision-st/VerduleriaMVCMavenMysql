package duoc.controlador;

import duoc.dao.CategoriaDAO;
import duoc.dao.impl.CategoriaDAOImpl;
import duoc.modelo.Categoria;

import java.util.List;

public class ControladorCategorias {

    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();

    public List<Categoria> listarCategorias() {
        return categoriaDAO.listarTodos();
    }
}