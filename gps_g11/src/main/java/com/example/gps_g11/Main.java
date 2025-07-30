package com.example.gps_g11;

import com.example.gps_g11.DB.DataBase;
import com.example.gps_g11.DB.EnviaPedidosDB;
import com.example.gps_g11.LoginSignUp.LoginControl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;


public class Main extends Application {
    DataBase db;
    @Override
    public void start(Stage stage) throws IOException {
        // Carrega o arquivo FXML
        db = new DataBase("SproutSmart.db"); // Adapte conforme necessário
        db.connect();
        db.criarTabelas();

        //TESTE
        //db.adicionarUsuario("Martim", "Antunes", "martim", "martim@example.com", "123");

        EnviaPedidosDB enviaPedidosDB = new EnviaPedidosDB(db); // Criação da instância


        // Carrega o arquivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/login.fxml"));
        Parent root = loader.load();

        // Passa a instância de EnviaPedidosDB para o controlador
        LoginControl controller = loader.getController();
        controller.setAuthService(enviaPedidosDB); // Chama o método para injetar a instância

        
        // Define a cena e o tamanho da janela
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Login - SproutSmart");
        stage.setResizable(false);

        stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/gps_g11/icons/logo.png")));

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}