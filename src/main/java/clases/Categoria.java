package Clases;

public class Categoria {
    private int id;
    private String nombre;
    private String notas;

    // Constructor
    public Categoria(int id, String nombre, String notas) {
        this.id = id;
        this.nombre = nombre;
        this.notas = notas;
    }

    // Getters y Setters
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

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
