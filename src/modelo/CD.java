package modelo;

public class CD extends Disco {
    public CD() {}
    public CD(Long id, String titulo, String artista, String categoria, int anio, int cantidad, Long usuarioId) {
        super(id, titulo, artista, categoria, anio, cantidad, usuarioId);
    }
    @Override
    public String getTipo() { return "CD"; }
}