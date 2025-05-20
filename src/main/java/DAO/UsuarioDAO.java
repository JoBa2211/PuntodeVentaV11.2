package DAO;

import clases.Usuario;
import lib.ConnectionFactory;
import lib.DatabaseUniversalTranslatorFactory;
import lib.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_ALL_USERS_FULL);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String usuario = rs.getString("usuario");
                String contrasena = rs.getString("contraseña");
                String rol = rs.getString("rol");
                usuarios.add(new Usuario(id, usuario, contrasena, rol));
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al obtener usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    public Usuario obtenerPorNombre(String nombre) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.SELECT_USER_BY_NAME)
        ) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String usuario = rs.getString("usuario");
                    String contrasena = rs.getString("contraseña");
                    String rol = rs.getString("rol");
                    return new Usuario(id, usuario, contrasena, rol);
                }
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al obtener usuario por nombre: " + e.getMessage());
        }
        return null;
    }

    public boolean insertar(Usuario usuario) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.INSERT_USER)
        ) {
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getContraseña());
            ps.setString(3, usuario.getRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Usuario usuario) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.UPDATE_USER_FULL)
        ) {
            ps.setString(1, usuario.getUsuario());
            ps.setString(2, usuario.getContraseña());
            ps.setString(3, usuario.getRol());
            ps.setString(4, usuario.getUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String nombreUsuario) {
        try (
            DatabaseUniversalTranslatorFactory traductor =
                new DatabaseUniversalTranslatorFactory(ConnectionFactory.TipoBD.SQLSERVER, ConnectionFactory.TipoBD.SQLSERVER);
            Connection conn = ConnectionFactory.getInstancia().getConexion();
            PreparedStatement ps = conn.prepareStatement(SQLQueries.DELETE_USER)
        ) {
            ps.setString(1, nombreUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}
