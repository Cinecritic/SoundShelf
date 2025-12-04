package vista;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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

        // BOTONES FUNCIONALES
        Button btnAgregar = new Button("Agregar");
        Button btnActualizar = new Button("Actualizar");
        Button btnEliminar = new Button("Eliminar");
        Button btnBuscar = new Button("Buscar");
        Button btnLimpiar = new Button("Limpiar");

        btnAgregar.getStyleClass().add("boton-primario");
        btnActualizar.getStyleClass().add("boton-primario");
        btnEliminar.getStyleClass().add("boton-primario");
        btnBuscar.getStyleClass().add("boton-secundario");
        btnLimpiar.getStyleClass().add("boton-secundario");

        btnAgregar.setOnAction(e -> agregarDisco());
        btnActualizar.setOnAction(e -> actualizarDisco());
        btnEliminar.setOnAction(e -> eliminarDisco());
        btnBuscar.setOnAction(e -> buscarAvanzado());
        btnLimpiar.setOnAction(e -> {
            limpiarFiltros();
            recargarTabla();
        });

        lblEstado = new Label();
        lblEstado.getStyleClass().add("barra-estado");

        // ==== LAYOUT PRINCIPAL CON SCROLL ====
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox raiz = new VBox(25);
        raiz.getStyleClass().add("contenedor-principal");
        raiz.setPadding(new Insets(25));
        raiz.setAlignment(Pos.TOP_CENTER);

        // LOGO + TÍTULO
        HBox tituloContainer = new HBox(15);
        tituloContainer.setAlignment(Pos.CENTER_LEFT);

        try {
            ImageView logo = new ImageView(new Image(getClass().getResource("/logo.png").toExternalForm()));
            logo.setFitHeight(60);
            logo.setPreserveRatio(true);
            tituloContainer.getChildren().add(logo);
        } catch (Exception e) {
            System.out.println("Logo no cargado");
        }

        Label titulo = new Label("SoundShelf – Gestor de Discos");
        titulo.getStyleClass().add("titulo-vintage");
        tituloContainer.getChildren().add(titulo);

        // AÑADIR TODOS LOS ELEMENTOS
        raiz.getChildren().addAll(
                tituloContainer,
                tablaDiscos,
                new Label("Formulario de Discos") {{ getStyleClass().add("etiqueta"); }},
                formulario,
                new Label("Filtros Avanzados") {{ getStyleClass().add("etiqueta"); }},
                filtros,
                new HBox(15, btnAgregar, btnActualizar, btnEliminar, btnBuscar, btnLimpiar) {{
                    setAlignment(Pos.CENTER);
                }},
                lblEstado
        );

        // CONFIGURACIÓN DE LA TABLA
        tablaDiscos.setPrefHeight(300);
        tablaDiscos.setMinHeight(200);

        // APLICAR LAYOUT
        scrollPane.setContent(raiz);
        Scene escena = new Scene(scrollPane, 950, 750);
        escena.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        escenario.setScene(escena);
        escenario.show();

        recargarTabla();
    }


    private void configurarTabla() {
        // Columna ID
        TableColumn<Disco, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setPrefWidth(60);

        // Columna Tipo
        TableColumn<Disco, String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTipo()));
        colTipo.setPrefWidth(90);

        // Columna Título
        TableColumn<Disco, String> colTitulo = new TableColumn<>("Título");
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colTitulo.setPrefWidth(180);

        // Columna Artista
        TableColumn<Disco, String> colArtista = new TableColumn<>("Artista");
        colArtista.setCellValueFactory(new PropertyValueFactory<>("artista"));
        colArtista.setPrefWidth(150);

        // Columna Categoría
        TableColumn<Disco, String> colCategoria = new TableColumn<>("Categoría");
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colCategoria.setPrefWidth(120);

        // Columna Año
        TableColumn<Disco, Integer> colAnio = new TableColumn<>("Año");
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anio"));
        colAnio.setPrefWidth(70);

        // Columna Cantidad
        TableColumn<Disco, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colCantidad.setPrefWidth(80);

        // Configuración final
        tablaDiscos.getColumns().addAll(colId, colTipo, colTitulo, colArtista, colCategoria, colAnio, colCantidad);
        tablaDiscos.setItems(listaDiscos);
        tablaDiscos.setPrefHeight(300);
        tablaDiscos.setMinHeight(200);
        tablaDiscos.setPlaceholder(new Label("No hay discos en el inventario"));

        // ¡Scrollbar automático cuando hay muchos datos!
        tablaDiscos.setTableMenuButtonVisible(true);
    }

    private GridPane crearFormulario() {
        GridPane grid = new GridPane();
        grid.getStyleClass().add("seccion-formulario");
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(15));

        txtTitulo = new TextField();
        txtArtista = new TextField();
        txtCategoria = new TextField();
        txtAnio = new TextField();
        txtCantidad = new TextField();
        comboTipo = new ComboBox<>();
        comboTipo.getItems().addAll("CD", "Vinilo");

        txtTitulo.getStyleClass().add("campo-texto");
        txtArtista.getStyleClass().add("campo-texto");
        txtCategoria.getStyleClass().add("campo-texto");
        txtAnio.getStyleClass().add("campo-texto");
        txtCantidad.getStyleClass().add("campo-texto");

        Label lblTipo = new Label("Tipo:");
        lblTipo.getStyleClass().add("etiqueta");
        Label lblTitulo = new Label("Título:");
        lblTitulo.getStyleClass().add("etiqueta");
        Label lblArtista = new Label("Artista:");
        lblArtista.getStyleClass().add("etiqueta");
        Label lblCategoria = new Label("Categoría:");
        lblCategoria.getStyleClass().add("etiqueta");
        Label lblAnio = new Label("Año:");
        lblAnio.getStyleClass().add("etiqueta");
        Label lblCantidad = new Label("Cantidad:");
        lblCantidad.getStyleClass().add("etiqueta");

        grid.add(lblTipo, 0, 0);
        grid.add(comboTipo, 1, 0);
        grid.add(lblTitulo, 0, 1);
        grid.add(txtTitulo, 1, 1);
        grid.add(lblArtista, 0, 2);
        grid.add(txtArtista, 1, 2);
        grid.add(lblCategoria, 0, 3);
        grid.add(txtCategoria, 1, 3);
        grid.add(lblAnio, 0, 4);
        grid.add(txtAnio, 1, 4);
        grid.add(lblCantidad, 0, 5);
        grid.add(txtCantidad, 1, 5);

        return grid;
    }

    private HBox crearFiltros() {
        HBox box = new HBox(10);
        box.getStyleClass().add("seccion-filtros");
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(15);

        // Tipo
        Label lblTipo = new Label("Tipo:");
        lblTipo.getStyleClass().add("etiqueta");
        comboTipoFiltro = new ComboBox<>();
        comboTipoFiltro.getItems().addAll("Todos", "CD", "Vinilo");
        comboTipoFiltro.setValue("Todos");
        comboTipoFiltro.getStyleClass().add("campo-texto");
        comboTipoFiltro.setPrefWidth(120);

        // Categoría
        Label lblCategoria = new Label("Categoría:");
        lblCategoria.getStyleClass().add("etiqueta");
        comboCategoriaFiltro = new ComboBox<>();
        comboCategoriaFiltro.getItems().addAll(
                "Todos", "Rock", "Pop", "Jazz", "Folk", "Hip Hop", "R&B",
                "Electronic", "Classical", "Reggae", "Latin", "Grunge", "Alternative"
        );
        comboCategoriaFiltro.setValue("Todos");
        comboCategoriaFiltro.getStyleClass().add("campo-texto");
        comboCategoriaFiltro.setPrefWidth(150);

        // Artista
        Label lblArtista = new Label("Artista:");
        lblArtista.getStyleClass().add("etiqueta");
        txtArtistaFiltro = new TextField();
        txtArtistaFiltro.getStyleClass().add("campo-texto");
        txtArtistaFiltro.setPromptText("Artista...");
        txtArtistaFiltro.setPrefWidth(180);

        // Año
        Label lblAnio = new Label("Año:");
        lblAnio.getStyleClass().add("etiqueta");
        txtAnioFiltro = new TextField();
        txtAnioFiltro.getStyleClass().add("campo-texto");
        txtAnioFiltro.setPromptText("Año");
        txtAnioFiltro.setPrefWidth(100);

        // Añadir al contenedor
        box.getChildren().addAll(
                lblTipo, comboTipoFiltro,
                lblCategoria, comboCategoriaFiltro,
                lblArtista, txtArtistaFiltro,
                lblAnio, txtAnioFiltro
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
        String tipo = "Todos".equals(comboTipoFiltro.getValue()) ? null : comboTipoFiltro.getValue();
        String categoria = "Todos".equals(comboCategoriaFiltro.getValue()) ? null : comboCategoriaFiltro.getValue();
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