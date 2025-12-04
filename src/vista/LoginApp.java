package vista;

import controlador.AutenticacionControlador;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.Usuario;
import java.util.Optional;

public class LoginApp extends Application {
    private AutenticacionControlador authControlador = new AutenticacionControlador();

    @Override
    public void start(Stage escenario) {
        escenario.setTitle("SoundShelf - Iniciar Sesión");

        // Campos
        TextField txtUsuario = new TextField();
        PasswordField txtContrasena = new PasswordField();
        txtUsuario.getStyleClass().add("campo-login");
        txtContrasena.getStyleClass().add("campo-login");

        // Botones
        Button btnIniciar = new Button("Iniciar Sesión");
        Button btnRegistrar = new Button("Registrarse");
        btnIniciar.getStyleClass().add("boton-login");
        btnRegistrar.getStyleClass().add("boton-registro");

        // Mensaje de error
        Label lblError = new Label();
        lblError.getStyleClass().add("error-login");

        // Eventos
        btnIniciar.setOnAction(e -> {
            String usuario = txtUsuario.getText().trim();
            String pass = txtContrasena.getText();
            Optional<Usuario> opt = authControlador.iniciarSesion(usuario, pass);
            if (opt.isPresent()) {
                new SoundShelfApp(opt.get()).start(new Stage());
                escenario.close();
            } else {
                lblError.setText("Usuario o contraseña incorrectos");
            }
        });

        btnRegistrar.setOnAction(e -> {
            String usuario = txtUsuario.getText().trim();
            String pass = txtContrasena.getText();
            if (authControlador.registrar(usuario, pass)) {
                lblError.setText("¡Usuario creado! Ahora inicia sesión.");
            } else {
                lblError.setText("El usuario ya existe o es inválido");
            }
        });

        // Layout
        VBox raiz = new VBox(15);
        raiz.getStyleClass().add("ventana-login");

        Label titulo = new Label("SoundShelf");
        titulo.getStyleClass().add("titulo-login");

        raiz.getChildren().addAll(
                titulo,
                new Label("Usuario:"),
                txtUsuario,
                new Label("Contraseña:"),
                txtContrasena,
                btnIniciar,
                btnRegistrar,
                lblError
        );

        // Escena
        Scene escena = new Scene(raiz, 350, 350);
        escena.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        escenario.setScene(escena);
        escenario.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}