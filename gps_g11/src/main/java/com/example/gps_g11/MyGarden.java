package com.example.gps_g11;

import com.example.gps_g11.DB.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Cursor;

import java.io.IOException;
import java.util.List;

public class MyGarden {

    @FXML
    private Button addPlantButton;
    @FXML
    private ImageView logoImageView;
    @FXML
    private VBox plantsVBox;
    @FXML
    private Label messageLabel;

    public void initialize() {
        addPlantButton.setOnMouseEntered(event -> addPlantButton.setCursor(Cursor.HAND));
        addPlantButton.setOnMouseExited(event -> addPlantButton.setCursor(Cursor.DEFAULT));
        logoImageView.setOnMouseEntered(event -> logoImageView.setCursor(Cursor.HAND));
        logoImageView.setOnMouseExited(event -> logoImageView.setCursor(Cursor.DEFAULT));
        loadPlantLinks();
    }

    private void loadPlantLinks() {
        List<String> plantNames = DataBase.getNamesPlantsByUserId();
        if (plantNames != null) {
            for (String plantName : plantNames) {
                Hyperlink plantLink = new Hyperlink(plantName);
                plantLink.setStyle("-fx-font-size: 16px;");
                plantLink.setOnMouseClicked(event -> goToPlant(event, plantName));
                plantsVBox.getChildren().add(plantLink);
            }
        }
    }

    public void handleAddPlant(ActionEvent actionEvent) {
        try {
            // Carregar o arquivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Survey1.fxml"));
            Parent homeRoot = loader.load();

            // Obter a referência ao palco atual
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.setTitle("Survey - SproutSmart");
            stage.show();
        } catch (IOException e) {
            // Exibir erro no console se a carga do FXML falhar
            System.err.println("Erro ao carregar o FXML: " + e.getMessage());
            // Aqui você pode adicionar qualquer outra lógica, como um diálogo de erro, se necessário
        }
    }
    @FXML
    private void handleProfileIconClick() {

        try {
            // Carregar o FXML da página de perfil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/ProfilePage.fxml"));
            Parent profileRoot = loader.load();

            // Configurar nova cena com o perfil
            Stage stage = (Stage) messageLabel.getScene().getWindow(); // Obtém o Stage atual a partir do messageLabel
            stage.setScene(new Scene(profileRoot));
            stage.setTitle("User Profile - SproutSmart");
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Failed to load Profile page.");
        }
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

    public void goToPlant(MouseEvent mouseEvent, String plantName) {
        try {
            // Carrega o FXML de detalhes
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/details_text.fxml"));
            Parent detailsRoot = loader.load();

            // Obtém o controlador da página de detalhes
            PlantDetailsController controller = loader.getController();

            // Passa o nome da planta para o controlador de detalhes
            controller.loadPlantDetails(plantName);

            // Configura a nova cena e exibe a página de detalhes
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(detailsRoot));
            stage.setTitle("Plant Details - SproutSmart");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar a página de detalhes: " + e.getMessage());
        }
    }
}

