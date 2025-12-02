package vista;

import controlador.AutenticacionControlador;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PruebaLogin extends Application {
    @Override
    public void start(Stage stage) {
        Button btn = new Button("Probar BCrypt");
        btn.setOnAction(e -> {
            AutenticacionControlador auth = new AutenticacionControlador();
            boolean registrado = auth.registrar("prueba", "123");
            if (registrado) {
                var sesion = auth.iniciarSesion("prueba", "123");
                if (sesion.isPresent()) {
                    new Alert(Alert.AlertType.INFORMATION, "¡BCrypt funciona!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Error en login").showAndWait();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Error al registrar").showAndWait();
            }
        });
        stage.setScene(new Scene(new StackPane(btn), 300, 200));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
