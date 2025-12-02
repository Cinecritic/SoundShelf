package controlador;

import dao.DiscoDAO;
import dao.DiscoDAOMySQL;
import modelo.Disco;
import java.util.List;

public class InventarioControlador {
    private DiscoDAO discoDAO = new DiscoDAOMySQL();

    public Disco crearDisco(Disco disco, Long usuarioId) {
        disco.setUsuarioId(usuarioId);
        return discoDAO.crear(disco);
    }

    public List<Disco> obtenerTodos(Long usuarioId) {
        return discoDAO.listarPorUsuario(usuarioId);
    }

    public List<Disco> buscarAvanzado(Long usuarioId, String tipo, String categoria, String artista, Integer anio) {
        return discoDAO.buscarAvanzado(usuarioId, tipo, categoria, artista, anio);
    }

    public Disco actualizarDisco(Disco disco) {
        return discoDAO.actualizar(disco);
    }

    public boolean eliminarDisco(Long id) {
        return discoDAO.eliminar(id);
    }
}