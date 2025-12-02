package modelo;

public abstract class Disco {
    private Long id;
    private String titulo;
    private String artista;
    private String categoria;
    private int anio;
    private int cantidad;
    private Long usuarioId;

    public Disco() {}

    public Disco(Long id, String titulo, String artista, String categoria, int anio, int cantidad, Long usuarioId) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.categoria = categoria;
        this.anio = anio;
        this.cantidad = cantidad;
        this.usuarioId = usuarioId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public abstract String getTipo();
}
