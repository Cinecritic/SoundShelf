package dao;

import modelo.Usuario;
import java.sql.*;
import java.util.Optional;

public class UsuarioDAOMySQL implements UsuarioDAO {
    private static final String INSERT = "INSERT INTO usuarios (nombre_usuario, contrasena) VALUES (?, ?)";
    private static final String SELECT_POR_NOMBRE = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
    private static final String SELECT_POR_ID = "SELECT * FROM usuarios WHERE id = ?";

    @Override
    public Usuario crear(Usuario usuario) {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasena());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getLong(1));
            }
            return usuario;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Optional<Usuario> buscarPorNombre(String nombreUsuario) {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SELECT_POR_NOMBRE)) {

            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setNombreUsuario(rs.getString("nombre_usuario"));
                u.setContrasena(rs.getString("contrasena"));
                return Optional.of(u);
            }
            return Optional.empty();

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Usuario leer(Long id) {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SELECT_POR_ID)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getLong("id"));
                u.setNombreUsuario(rs.getString("nombre_usuario"));
                u.setContrasena(rs.getString("contrasena"));
                return u;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}