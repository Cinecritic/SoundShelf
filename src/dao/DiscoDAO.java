package dao;

import modelo.Disco;
import java.util.List;

public interface DiscoDAO {
    Disco crear(Disco disco);
    Disco leer(Long id);
    List<Disco> listarPorUsuario(Long usuarioId);
    Disco actualizar(Disco disco);
    boolean eliminar(Long id);
    List<Disco> buscarAvanzado(Long usuarioId, String tipo, String categoria, String artista, Integer anio);
}