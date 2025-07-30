/*package com.example.gps_g11.Profile;

import com.example.gps_g11.DB.DataBase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.awt.event.ActionEvent;
import java.util.List;

public class ProfilePageController {

    @FXML
    private Label userNameLabel;
    @FXML
    private Label userEmailLabel;
    @FXML
    private VBox plantsList;

    @FXML
    public void initialize() {
        // Carregar informações do usuário na inicialização
        loadUserProfile();
    }

    private void loadUserProfile() {
        // Obter o nome de usuário atual
        String username = com.example.gps_g11.LoginSignUp.LoginControl.user;

        // Buscar ID do usuário a partir do nome de usuário
        Integer userId = DataBase.getUserIdByUsername(username);
        if (userId == -1) {
            userNameLabel.setText("User not found");
            userEmailLabel.setText("");
            return;
        }

        // Definir o nome do usuário na label
        userNameLabel.setText(username);

        // TODO: Substituir pelo método que retorna o email real do usuário
        userEmailLabel.setText("email@example.com"); // Placeholder

        // Obter as plantas do usuário
        List<String> userPlants = DataBase.getNamesPlantsByUserId();
        if (userPlants != null && !userPlants.isEmpty()) {
            plantsList.getChildren().clear(); // Garantir que a lista começa limpa
            for (String plantName : userPlants) {
                Label plantLabel = new Label(plantName);
                plantsList.getChildren().add(plantLabel);
            }
        } else {
            plantsList.getChildren().add(new Label("No plants found."));
        }
    }

    @FXML
    public void handleLogoutAction(javafx.event.ActionEvent actionEvent) {
        System.out.println("Logout action triggered.");
        // Lógica para realizar o logout ou mudar de tela
    }
}
*/
package com.example.gps_g11.Profile;

import com.example.gps_g11.DB.DataBase;
import com.example.gps_g11.LoginSignUp.LoginControl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfilePageController {

    @FXML
    private Text firstNameLabel;
    @FXML
    private Text lastNameLabel;
    @FXML
    private Text usernameLabel;
    @FXML
    private Text emailLabel;

    private DataBase database;

    public void initialize() {

        database = new DataBase("SproutSmart.db");
        loadUserProfile();
    }

    private void loadUserProfile() {
        String loggedInUsername = LoginControl.user; // Obtém o username do utilizador autenticado
        if (loggedInUsername == null) {
            System.out.println("Erro: Utilizador não autenticado.");
            return;
        }

        String query = "SELECT first_name, last_name, username, email FROM users WHERE username = ?";
        try (Connection conn = database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, loggedInUsername);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                firstNameLabel.setText(rs.getString("first_name"));
                lastNameLabel.setText(rs.getString("last_name"));
                usernameLabel.setText(rs.getString("username"));
                emailLabel.setText(rs.getString("email"));
            } else {
                System.out.println("Erro: Dados do utilizador não encontrados.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao carregar dados do utilizador: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditProfileAction() {
        System.out.println("Botão de editar perfil clicado!");
        // Futuramente implementar a lógica para abrir uma nova janela para edição ou habilitar edição inline.
    }
    public void goToHome(MouseEvent mouseEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Home.fxml"));
            Parent homeRoot = loader.load();
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.setTitle("Home - SproutSmart");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar o FXML: " + e.getMessage());
        }
    }

}
