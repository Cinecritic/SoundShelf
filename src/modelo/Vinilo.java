package modelo;

public class Vinilo extends Disco {
    public Vinilo() {}
    public Vinilo(Long id, String titulo, String artista, String categoria, int anio, int cantidad, Long usuarioId) {
        super(id, titulo, artista, categoria, anio, cantidad, usuarioId);
    }
    @Override
    public String getTipo() { return "Vinilo"; }
}