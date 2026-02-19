package duoc.modelo;

public enum CategoriaProducto {

    FRUTA, VERDURA, TUBERCULO, HORTALIZA;

    @Override
    public String toString() {
        String nomMinus = name().toLowerCase();
        return nomMinus.substring(0, 1).toUpperCase() + nomMinus.substring(1);
    }
}
