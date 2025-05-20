package clases;

public class Proveedor {
    private int id;
    private String nombre;
    private String telefono;
    private String mail;
    private String direccion;

    public Proveedor(int id, String nombre, String telefono, String mail, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.mail = mail;
        this.direccion = direccion;
    }

    public Proveedor(String nombre, String telefono, String mail, String direccion) {
        this(0, nombre, telefono, mail, direccion);
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
