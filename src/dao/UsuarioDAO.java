package dao;

import modelo.Usuario;
import java.util.Optional;

public interface UsuarioDAO {
    Usuario crear(Usuario usuario);
    Optional<Usuario> buscarPorNombre(String nombreUsuario);
    Usuario leer(Long id);
}
