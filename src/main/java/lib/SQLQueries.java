package database;

public class SQLQueries {
    // Usuarios
    public static final String SELECT_ALL_USERS = "SELECT usuario FROM Usuarios";
    public static final String SELECT_ALL_USERS_FULL = "SELECT row_number() OVER (ORDER BY usuario) AS id, usuario, rol FROM Usuarios";
    public static final String INSERT_USER = "INSERT INTO Usuarios (usuario, contraseña, rol) VALUES (?, ?, ?)";
    public static final String UPDATE_USER = "UPDATE Usuarios SET contraseña = ?, rol = ? WHERE usuario = ?";
    public static final String UPDATE_USER_FULL = "UPDATE Usuarios SET usuario = ?, contraseña = ?, rol = ? WHERE usuario = ?";
    public static final String DELETE_USER = "DELETE FROM Usuarios WHERE usuario = ?";
    public static final String SELECT_USER_BY_ID = "SELECT usuario, contraseña FROM (SELECT row_number() OVER (ORDER BY usuario) AS id, usuario, contraseña FROM Usuarios) AS temp WHERE id = ?";
    public static final String SELECT_USER_BY_NAME = "SELECT contraseña FROM Usuarios WHERE usuario = ?";
    public static final String SELECT_USER_NAME_BY_ID = "SELECT usuario FROM (SELECT row_number() OVER (ORDER BY usuario) AS id, usuario FROM Usuarios) AS temp WHERE id = ?";
    public static final String SELECT_PASSWORD_BY_USER = "SELECT contraseña FROM Usuarios WHERE usuario = ?";
    public static final String EXISTE_NOMBRE_USUARIO = "SELECT COUNT(*) FROM Usuarios WHERE usuario = ? AND usuario <> ?";

    // Productos
    public static final String SELECT_ALL_PRODUCTS = "SELECT * FROM Productos";
    public static final String SELECT_PRODUCT_BY_ID = "SELECT * FROM Productos WHERE id = ?";
    public static final String INSERT_PRODUCT = "INSERT INTO Productos (codigo, nombre, precioVenta, precioCompra, categoriaId, existencias, existenciaMinima, existenciaMaxima, idProveedor, iva, ieps, fechaDeCreacion, tipoProducto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_PRODUCT = "UPDATE Productos SET codigo = ?, nombre = ?, precioVenta = ?, precioCompra = ?, categoriaId = ?, existencias = ?, existenciaMinima = ?, existenciaMaxima = ?, idProveedor = ?, iva = ?, ieps = ?, fechaDeCreacion = ?, tipoProducto = ? WHERE id = ?";
    public static final String DELETE_PRODUCT = "DELETE FROM Productos WHERE id = ?";

    // Categorias
    public static final String INSERT_CATEGORIA = "INSERT INTO Categorias (nombre, notas) VALUES (?, ?)";
    public static final String SELECT_ALL_CATEGORIAS = "SELECT id, nombre, notas FROM Categorias ORDER BY nombre";
    public static final String SELECT_CATEGORIA_ID_BY_NOMBRE = "SELECT id FROM Categorias WHERE nombre = ?";
    public static final String SELECT_PROVEEDOR_ID_BY_NOMBRE = "SELECT id FROM Proveedores WHERE nombre = ?";

    // Proveedores
    public static final String INSERT_PROVEEDOR = "INSERT INTO Proveedores (nombre, telefono, direccion, email) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_PROVEEDOR = "UPDATE Proveedores SET nombre = ?, telefono = ?, direccion = ?, email = ? WHERE id = ?";
    public static final String SELECT_ALL_PROVEEDORES = "SELECT id, nombre, telefono, direccion, email FROM Proveedores ORDER BY nombre";
    public static final String DELETE_PROVEEDOR = "DELETE FROM Proveedores WHERE id = ?";

    // Reabastecimientos
    public static final String SELECT_ALL_REABASTECIMIENTOS =
        "SELECT r.id, p.nombre AS producto_nombre, r.cantidadRefill, r.cantidadNueva, r.fecha " +
        "FROM Reabastecimientos r " +
        "JOIN Productos p ON r.idProducto = p.id " +
        "ORDER BY r.fecha DESC";
    public static final String INSERT_REABASTECIMIENTO =
        "INSERT INTO Reabastecimientos (idProducto, cantidad, cantidadRefill, cantidadNueva, fecha, idUsuario, notas) VALUES (?, ?, ?, ?, ?, ?, ?)";

    // Tickets
    public static final String INSERT_TICKET =
        "INSERT INTO Tickets (fechaDeCreacion, idUsuario, numeroDeTicket, subtotal, total, ivaTotal, iepsTotal) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_TICKETS =
        "SELECT t.id, t.fechaDeCreacion, u.usuario, t.total " +
        "FROM Tickets t " +
        "JOIN Usuarios u ON t.idUsuario = u.id " +
        "ORDER BY t.fechaDeCreacion DESC";
    public static final String INSERT_TICKET_DETALLE =
        "INSERT INTO TicketDetalles (idTicket, idProducto, cantidad) VALUES (?, ?, ?)";

    // NUEVO: Actualizar existencias de productos
    public static final String UPDATE_PRODUCT_EXISTENCIAS =
        "UPDATE Productos SET existencias = existencias - ? WHERE id = ?";
}
