package controlador;

import dao.UsuarioDAO;
import dao.UsuarioDAOMySQL;
import modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;

public class AutenticacionControlador {
    private UsuarioDAO usuarioDAO = new UsuarioDAOMySQL();

    public boolean registrar(String nombre, String contrasenaPlana) {
        if (nombre == null || contrasenaPlana == null || nombre.trim().isEmpty()) {
            return false;
        }
        if (usuarioDAO.buscarPorNombre(nombre).isPresent()) {
            return false;
        }
        String hash = BCrypt.hashpw(contrasenaPlana, BCrypt.gensalt());
        Usuario u = new Usuario(nombre, hash);
        return usuarioDAO.crear(u) != null;
    }

    public Optional<Usuario> iniciarSesion(String nombre, String contrasenaPlana) {
        if (nombre == null || contrasenaPlana == null) {
            return Optional.empty();
        }
        Optional<Usuario> opt = usuarioDAO.buscarPorNombre(nombre);
        if (opt.isPresent()) {
            // ¡Aquí se usa BCrypt!
            String hashGuardado = opt.get().getContrasena();
            if (BCrypt.checkpw(contrasenaPlana, hashGuardado)) {
                return opt;
            }
        }
        return Optional.empty();
    }
}
