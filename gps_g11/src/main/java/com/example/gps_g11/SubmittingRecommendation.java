package com.example.gps_g11;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class SubmittingRecommendation {
    @FXML
    private Button submitPlantButton;

    @FXML
    private Label resultLabel;

    @FXML
    private ImageView plantImageView;
    public void handleSubmitPlant(ActionEvent actionEvent) {
        // Nome da planta
        String plantName = "Cactus";

        // Tentar exibir a imagem do Cacto
        try {

            Image cactusImage = new Image(getClass().getResourceAsStream("/com/example/gps_g11/icons/cacto.png"));

            // Criar uma nova janela de alerta
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Recommended Plant");

            // Criação do layout vertical
            VBox content = new VBox();
            content.setSpacing(10);  // Espaçamento entre os elementos

            // Adicionar o cabeçalho ao VBox
            Label headerLabel = new Label("Your plant is: " + plantName);
            headerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");  // Estilo do texto

            // Adicionar a imagem ao VBox
            ImageView imageView = new ImageView(cactusImage);
            imageView.setFitHeight(200);  // Define a altura da imagem
            imageView.setFitWidth(200);    // Define a largura da imagem
            imageView.setPreserveRatio(true);  // Mantém a proporção da imagem

            // Adiciona os elementos ao VBox
            content.getChildren().addAll(headerLabel, imageView);

            // Define o conteúdo do alerta
            alert.getDialogPane().setContent(content);
            alert.getButtonTypes().add(ButtonType.OK);

            // Exibe o alerta e espera que o usuário clique em OK
            alert.showAndWait();

        } catch (Exception e) {
            System.err.println("Error loading cactus image: " + e.getMessage());
            // Caso ocorra um erro ao carregar a imagem
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText("Error loading image");
            errorAlert.setContentText("The plant image could not be displayed.");
            errorAlert.showAndWait();
        }
    }
}
