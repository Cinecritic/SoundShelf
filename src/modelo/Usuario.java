package modelo;

public class Usuario {
    private Long id;
    private String nombreUsuario;
    private String contrasena;

    // Constructores
    public Usuario() {}
    public Usuario(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}