package com.example.gps_g11.PlantsDetails;

import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.io.IOException;

public class ViewPlantDetailsControl {
    @FXML
    private ImageView viewPlantGoBack;

    @FXML
    private Label messageLabel;

    @FXML
    private ImageView logoImageView;

    public void initialize() {
        viewPlantGoBack.setOnMouseEntered(event -> viewPlantGoBack.setCursor(javafx.scene.Cursor.HAND));
        viewPlantGoBack.setOnMouseExited(event -> viewPlantGoBack.setCursor(javafx.scene.Cursor.DEFAULT));
        logoImageView.setOnMouseEntered(event -> logoImageView.setCursor(javafx.scene.Cursor.HAND));
        logoImageView.setOnMouseExited(event -> logoImageView.setCursor(javafx.scene.Cursor.DEFAULT));
    }

    @FXML
    public void goBack(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/MyGarden.fxml"));
            Parent homeRoot = loader.load();
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.setTitle("My Garden - SproutSmart");
            stage.show();
        } catch (IOException e) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Fail to load MyGarden file.");
        }
    }

    @FXML
    public void goToPlant(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/detail_plants.fxml"));
            Parent signUpRoot = loader.load();
            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set new scene
            stage.setScene(new Scene(signUpRoot));
            stage.setTitle("SproutSmart - Cactus" );
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void goToHome(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Home.fxml"));
            Parent homeRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.setTitle("Home - SproutSmart");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao carregar o FXML: " + e.getMessage());
        }
    }
}
