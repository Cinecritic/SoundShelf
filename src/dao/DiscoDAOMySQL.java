package dao;

import modelo.CD;
import modelo.Disco;
import modelo.Vinilo;
import java.sql.Types;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscoDAOMySQL implements DiscoDAO {
    private static final String INSERT = "INSERT INTO discos (tipo, titulo, artista, categoria, anio, cantidad, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_POR_ID = "SELECT * FROM discos WHERE id = ?";
    private static final String SELECT_TODOS_POR_USUARIO = "SELECT * FROM discos WHERE usuario_id = ?";
    private static final String UPDATE = "UPDATE discos SET tipo = ?, titulo = ?, artista = ?, categoria = ?, anio = ?, cantidad = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM discos WHERE id = ?";
    private static final String BUSCAR_AVANZADO =
            "SELECT * FROM discos WHERE usuario_id = ? " +
                    "AND (? IS NULL OR tipo = ?) " +
                    "AND (? IS NULL OR categoria = ?) " +
                    "AND (? IS NULL OR artista LIKE CONCAT('%', ?, '%')) " +
                    "AND (? IS NULL OR anio = ?)";

    @Override
    public Disco crear(Disco disco) {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, disco.getTipo());
            ps.setString(2, disco.getTitulo());
            ps.setString(3, disco.getArtista());
            ps.setString(4, disco.getCategoria());
            ps.setInt(5, disco.getAnio());
            ps.setInt(6, disco.getCantidad());
            ps.setLong(7, disco.getUsuarioId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                disco.setId(rs.getLong(1));
            }
            return disco;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Disco leer(Long id) {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SELECT_POR_ID)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapearDisco(rs);
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Disco> listarPorUsuario(Long usuarioId) {
        List<Disco> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SELECT_TODOS_POR_USUARIO)) {

            ps.setLong(1, usuarioId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearDisco(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Disco actualizar(Disco disco) {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {

            ps.setString(1, disco.getTipo());
            ps.setString(2, disco.getTitulo());
            ps.setString(3, disco.getArtista());
            ps.setString(4, disco.getCategoria());
            ps.setInt(5, disco.getAnio());
            ps.setInt(6, disco.getCantidad());
            ps.setLong(7, disco.getId());

            int filas = ps.executeUpdate();
            return filas > 0 ? disco : null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean eliminar(Long id) {
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(DELETE)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Disco> buscarAvanzado(Long usuarioId, String tipo, String categoria, String artista, Integer anio) {
        List<Disco> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(BUSCAR_AVANZADO)) {

            // Usuario ID (siempre requerido)
            ps.setLong(1, usuarioId);

            // Tipo
            if (tipo == null) {
                ps.setNull(2, Types.VARCHAR);
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(2, tipo);
                ps.setString(3, tipo);
            }

            // Categoría
            if (categoria == null) {
                ps.setNull(4, Types.VARCHAR);
                ps.setNull(5, Types.VARCHAR);
            } else {
                ps.setString(4, categoria);
                ps.setString(5, categoria);
            }

            // Artista
            if (artista == null) {
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
            } else {
                ps.setString(6, artista);
                ps.setString(7, artista);
            }

            // Año
            if (anio == null) {
                ps.setNull(8, Types.INTEGER);
                ps.setNull(9, Types.INTEGER);
            } else {
                ps.setInt(8, anio);
                ps.setInt(9, anio);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearDisco(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Disco mapearDisco(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        Long id = rs.getLong("id");
        String titulo = rs.getString("titulo");
        String artista = rs.getString("artista");
        String categoria = rs.getString("categoria");
        int anio = rs.getInt("anio");
        int cantidad = rs.getInt("cantidad");
        Long usuarioId = rs.getLong("usuario_id");

        if ("Vinilo".equals(tipo)) {
            return new Vinilo(id, titulo, artista, categoria, anio, cantidad, usuarioId);
        } else {
            return new CD(id, titulo, artista, categoria, anio, cantidad, usuarioId);
        }
    }
}