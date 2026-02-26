package duoc.modelo;

public class Producto {

    private int id;
    private String nombre;
    private int idCategoria;
    private String nombreCategoria;
    private int stock;
    private int valor;

    public Producto() {
    }

    public Producto(int id, String nombre, int idCategoria, String nombreCategoria, int stock, int valor) {
        this.id = id;
        this.nombre = nombre;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
        this.stock = stock;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }
}
