package com.example.gps_g11.PlantsDetails;

import com.example.gps_g11.GardenPlant.Plant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class PlantDetails_libraryControl {
    @FXML
    private ImageView plantImageView;
    @FXML
    private Label plantNameLabel;
    @FXML
    private Label plantDetailsLabel;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView goBackButton;

    private Plant plant;

    public void initialize() {
        logoImageView.setOnMouseEntered(event -> logoImageView.setCursor(javafx.scene.Cursor.HAND));
        logoImageView.setOnMouseExited(event -> logoImageView.setCursor(javafx.scene.Cursor.DEFAULT));
        goBackButton.setOnMouseEntered(event -> goBackButton.setCursor(javafx.scene.Cursor.HAND));
        goBackButton.setOnMouseExited(event -> goBackButton.setCursor(javafx.scene.Cursor.DEFAULT));
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
        plantNameLabel.setText(plant.getName());

        // Load the image resource and ensure correct sizing:
        Image plantImage = new Image(getClass().getResource(plant.getImagePath()).toString(), 300, 300, true, true);
        plantImageView.setImage(plantImage);

        // Set formatted plant details:
        String details = String.format(
                "Type: %s\nColor: %s\nFlowers: %s\nSeason: %s\nMaintenance: %s\nPet Friendly: %s\nAllergies: %s\nSize: %s\nHumidity Requirement: %s",
                plant.getIndoorOutdoor(),
                plant.getColor(),
                plant.isFlowers() ? "Yes" : "No",
                plant.getSeason(),
                plant.getMaintenance(),
                plant.isPetFriendly() ? "Yes" : "No",
                plant.getAllergies(),
                plant.getSize(),
                plant.gethumidity_requirement()
        );

        plantDetailsLabel.setText(details);
    }

    public void gotoHome(MouseEvent event) {
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

    public void goBack(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/PlantLibrary.fxml"));
            Parent libraryRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(libraryRoot));
            stage.setTitle("Plant Library - SproutSmart");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading plant library: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
