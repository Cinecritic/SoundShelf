package vista;

import controlador.InventarioControlador;
import modelo.CD;
import modelo.Disco;
import modelo.Usuario;
import modelo.Vinilo;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SoundShelfApp extends Application {
    private final Usuario usuarioActual;
    private InventarioControlador controlador = new InventarioControlador();
    private ObservableList<Disco> listaDiscos = FXCollections.observableArrayList();
    private TableView<Disco> tablaDiscos;
    private TextField txtTitulo, txtArtista, txtCategoria, txtAnio, txtCantidad;
    private ComboBox<String> comboTipo;
    private ComboBox<String> comboTipoFiltro;
    private ComboBox<String> comboCategoriaFiltro;
    private TextField txtArtistaFiltro;
    private TextField txtAnioFiltro;
    private Label lblEstado;

    public SoundShelfApp(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    @Override
    public void start(Stage escenario) {
        escenario.setTitle("SoundShelf – Gestor de Discos");

        tablaDiscos = new TableView<>();
        configurarTabla();

        GridPane formulario = crearFormulario();
        HBox filtros = crearFiltros();

        HBox botones = new HBox(10);
        Button btnAgregar = new Button("Agregar");
        Button btnActualizar = new Button("Actualizar");
        Button btnEliminar = new Button("Eliminar");
        Button btnBuscar = new Button("Buscar");
        Button btnLimpiar = new Button("Limpiar");
        botones.getChildren().addAll(btnAgregar, btnActualizar, btnEliminar, btnBuscar, btnLimpiar);

        lblEstado = new Label();

        VBox raiz = new VBox(10);
        raiz.setPadding(new Insets(10));
        raiz.getChildren().addAll(
                new Label("SoundShelf – Gestor de Discos"),
                tablaDiscos,
                new Label("Formulario de Discos"),
                formulario,
                new Label("Filtros Avanzados"),
                filtros,
                botones,
                lblEstado
        );

        btnAgregar.setOnAction(e -> agregarDisco());
        btnActualizar.setOnAction(e -> actualizarDisco());
        btnEliminar.setOnAction(e -> eliminarDisco());
        btnBuscar.setOnAction(e -> buscarAvanzado());
        btnLimpiar.setOnAction(e -> {
            limpiarFiltros();
            recargarTabla();
        });

        recargarTabla();

        Scene escena = new Scene(raiz, 900, 700);
        // SIN CSS, SIN LOGO
        escenario.setScene(escena);
        escenario.show();
    }

    private void configurarTabla() {
        TableColumn<Disco, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Disco, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTipo()));

        TableColumn<Disco, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));

        TableColumn<Disco, String> colArtista = new TableColumn<>("Artista");
        colArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));

        TableColumn<Disco, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Disco, Integer> colAnio = new TableColumn<>("Año");
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));

        TableColumn<Disco, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        tablaDiscos.getColumns().addAll(colId, colTipo, colTitulo, colArtista, colCategoria, colAnio, colCantidad);
        tablaDiscos.setItems(listaDiscos);
    }

    private GridPane crearFormulario() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));

        txtTitulo = new TextField();
        txtArtista = new TextField();
        txtCategoria = new TextField();
        txtAnio = new TextField();
        txtCantidad = new TextField();
        comboTipo = new ComboBox<>();
        comboTipo.getItems().addAll("CD", "Vinilo");

        grid.add(new Label("Tipo:"), 0, 0);
        grid.add(comboTipo, 1, 0);
        grid.add(new Label("Título:"), 0, 1);
        grid.add(txtTitulo, 1, 1);
        grid.add(new Label("Artista:"), 0, 2);
        grid.add(txtArtista, 1, 2);
        grid.add(new Label("Categoría:"), 0, 3);
        grid.add(txtCategoria, 1, 3);
        grid.add(new Label("Año:"), 0, 4);
        grid.add(txtAnio, 1, 4);
        grid.add(new Label("Cantidad:"), 0, 5);
        grid.add(txtCantidad, 1, 5);

        return grid;
    }

    private HBox crearFiltros() {
        HBox box = new HBox(10);
        box.setPadding(new Insets(5));

        comboTipoFiltro = new ComboBox<>();
        comboTipoFiltro.getItems().addAll("Todos", "CD", "Vinilo");
        comboTipoFiltro.setValue("Todos");

        comboCategoriaFiltro = new ComboBox<>();
        comboCategoriaFiltro.getItems().addAll(
                "Todos", "Rock", "Pop", "Jazz", "Folk", "Hip Hop", "R&B",
                "Electronic", "Classical", "Reggae", "Latin", "Grunge", "Alternative"
        );
        comboCategoriaFiltro.setValue("Todos");

        txtArtistaFiltro = new TextField();
        txtAnioFiltro = new TextField();

        box.getChildren().addAll(
                new Label("Tipo:"),
                comboTipoFiltro,
                new Label("Categoría:"),
                comboCategoriaFiltro,
                new Label("Artista:"),
                txtArtistaFiltro,
                new Label("Año:"),
                txtAnioFiltro
        );

        return box;
    }

    private void agregarDisco() {
        try {
            String tipo = comboTipo.getValue();
            String titulo = txtTitulo.getText().trim();
            String artista = txtArtista.getText().trim();
            String categoria = txtCategoria.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (tipo == null || titulo.isEmpty() || artista.isEmpty() || categoria.isEmpty()) {
                mostrarMensaje("Todos los campos son obligatorios.");
                return;
            }

            Disco disco;
            if ("Vinilo".equals(tipo)) {
                disco = new Vinilo(null, titulo, artista, categoria, anio, cantidad, usuarioActual.getId());
            } else {
                disco = new CD(null, titulo, artista, categoria, anio, cantidad, usuarioActual.getId());
            }

            controlador.crearDisco(disco, usuarioActual.getId());
            recargarTabla();
            limpiarFormulario();
            mostrarMensaje("Disco agregado correctamente.");
        } catch (NumberFormatException e) {
            mostrarMensaje("Año y Cantidad deben ser números enteros.");
        }
    }

    private void actualizarDisco() {
        Disco seleccionado = tablaDiscos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje("Seleccione un disco de la tabla.");
            return;
        }

        try {
            String tipo = comboTipo.getValue();
            String titulo = txtTitulo.getText().trim();
            String artista = txtArtista.getText().trim();
            String categoria = txtCategoria.getText().trim();
            int anio = Integer.parseInt(txtAnio.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (tipo == null || titulo.isEmpty() || artista.isEmpty() || categoria.isEmpty()) {
                mostrarMensaje("Todos los campos son obligatorios.");
                return;
            }

            if (!tipo.equals(seleccionado.getTipo())) {
                mostrarMensaje("No se puede cambiar el tipo del disco.");
                return;
            }

            seleccionado.setTitulo(titulo);
            seleccionado.setArtista(artista);
            seleccionado.setCategoria(categoria);
            seleccionado.setAnio(anio);
            seleccionado.setCantidad(cantidad);

            controlador.actualizarDisco(seleccionado);
            recargarTabla();
            limpiarFormulario();
            mostrarMensaje("Disco actualizado.");
        } catch (NumberFormatException e) {
            mostrarMensaje("Año y Cantidad deben ser números enteros.");
        }
    }

    private void eliminarDisco() {
        Disco seleccionado = tablaDiscos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarMensaje("Seleccione un disco para eliminar.");
            return;
        }

        if (controlador.eliminarDisco(seleccionado.getId())) {
            recargarTabla();
            limpiarFormulario();
            mostrarMensaje("Disco eliminado.");
        } else {
            mostrarMensaje("Error al eliminar el disco.");
        }
    }

    private void buscarAvanzado() {
        String tipo = comboTipoFiltro.getValue();
        if ("Todos".equals(tipo)) tipo = null;

        String categoria = comboCategoriaFiltro.getValue();
        if ("Todos".equals(categoria)) categoria = null;

        String artista = txtArtistaFiltro.getText().trim();
        if (artista.isEmpty()) artista = null;

        Integer anio = null;
        String anioStr = txtAnioFiltro.getText().trim();
        if (!anioStr.isEmpty()) {
            try {
                anio = Integer.parseInt(anioStr);
            } catch (NumberFormatException e) {
                mostrarMensaje("Año debe ser un número.");
                return;
            }
        }

        listaDiscos.setAll(controlador.buscarAvanzado(usuarioActual.getId(), tipo, categoria, artista, anio));
    }

    private void limpiarFiltros() {
        comboTipoFiltro.setValue("Todos");
        comboCategoriaFiltro.setValue("Todos");
        txtArtistaFiltro.clear();
        txtAnioFiltro.clear();
    }

    private void recargarTabla() {
        listaDiscos.setAll(controlador.obtenerTodos(usuarioActual.getId()));
    }

    private void limpiarFormulario() {
        comboTipo.setValue(null);
        txtTitulo.clear();
        txtArtista.clear();
        txtCategoria.clear();
        txtAnio.clear();
        txtCantidad.clear();
    }

    private void mostrarMensaje(String mensaje) {
        lblEstado.setText(mensaje);
    }

    public static void main(String[] args) {
        // Se inicia desde LoginApp
    }
}