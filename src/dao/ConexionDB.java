package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//Esta clase ConexionDB actúa como utilidad para obtener conexiones a MySQL
public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/inventario_discos?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "";
//Este es un bloque estático y se ejecuta solo una vez cuando la clase se carga en memoria.
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver de MySQL no encontrado", e);
        }
    }
   // Este es un método estático, así que se puede llamar sin crear un objeto
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
    }
}