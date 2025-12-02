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
        escenario.setTitle("SoundShelf - Login");

        TextField txtUsuario = new TextField();
        PasswordField txtContrasena = new PasswordField();
        Button btnIniciar = new Button("Iniciar Sesión");
        Button btnRegistrar = new Button("Registrarse");
        Label lblError = new Label();

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
                lblError.setText("¡Registrado! Inicia sesión.");
            } else {
                lblError.setText("Usuario ya existe");
            }
        });

        VBox raiz = new VBox(10);
        raiz.setPadding(new Insets(20));
        raiz.getChildren().addAll(
                new Label("Usuario:"),
                txtUsuario,
                new Label("Contraseña:"),
                txtContrasena,
                btnIniciar,
                btnRegistrar,
                lblError
        );

        Scene escena = new Scene(raiz, 300, 250);
        escenario.setScene(escena);
        escenario.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}