package com.example.gps_g11;

import com.example.gps_g11.GardenPlant.Plant;
import com.example.gps_g11.PlantsDetails.PlantDetails_libraryControl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlantLibraryControl {
    @FXML
    private ImageView logoImageView;
    @FXML
    private TextField searchField;
    @FXML
    private GridPane plantGrid;
    @FXML
    private ScrollPane plantScrollPane;

    private Connection connection;

    public void initialize() {
        logoImageView.setOnMouseEntered(event -> logoImageView.setCursor(javafx.scene.Cursor.HAND));
        logoImageView.setOnMouseExited(event -> logoImageView.setCursor(javafx.scene.Cursor.DEFAULT));
        connectoToDB();
        loadPlants("");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            loadPlants(newValue);
        });
        plantScrollPane.setStyle("-fx-background-color: #93c47d; -fx-background: #93c47d;");

    }

    private void connectoToDB() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:plant_database.db");
        } catch (SQLException e) {
            System.err.println("Erro ao conectar à base de dados: " + e.getMessage());
        }
    }

    private void loadPlants(String searchQuery) {
        plantGrid.getChildren().clear();
        List<Plant> plants = fetchPlantsFromDB(searchQuery);
        int column = 0;
        int row = 0;
        for (Plant plant : plants) {
            VBox plantBox = createPlantBox(plant);
            plantGrid.add(plantBox, column, row);
            column++;
            if (column == 4) { // Adjust columns as necessary
                column = 0;
                row++;
            }
        }
        // Ensure the grid adjusts dynamically:
        plantGrid.setPrefHeight((row) * 150); // Adjust 150 based on item size
    }

    private VBox createPlantBox(Plant plant) {
        VBox box = new VBox();
        ImageView imageView;

        try {
            String imagePath = plant.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                imageView = new ImageView(new Image(getClass().getResource(imagePath).toString()));
            } else {
                imageView = new ImageView(new Image(getClass().getResource("/images/template.jpg").toString()));
            }
        } catch (Exception e) {
            imageView = new ImageView(new Image(getClass().getResource("/images/error.jpg").toString()));
            e.printStackTrace();
        }

        imageView.setFitHeight(100);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(plant.getName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-alignment: center;");

        box.getChildren().addAll(imageView, nameLabel);
        box.setSpacing(10);
        box.setStyle("-fx-alignment: center;");

        // Add cursor change on hover
        box.setOnMouseEntered(event -> box.setCursor(javafx.scene.Cursor.HAND));
        box.setOnMouseExited(event -> box.setCursor(javafx.scene.Cursor.DEFAULT));

        // Add Mouse Click Event
        box.setOnMouseClicked(event -> {
            openPlantDetails(plant);
        });

        return box;
    }


    private void openPlantDetails(Plant plant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/plantdetails_Library.fxml"));
            Parent detailsRoot = loader.load();

            PlantDetails_libraryControl controller = loader.getController();
            controller.setPlant(plant);  // Pass the selected plant to the details view

            Stage stage = (Stage) plantGrid.getScene().getWindow();
            stage.setScene(new Scene(detailsRoot));
            stage.setTitle("Plant Details - " + plant.getName());
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading plant details: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private List<Plant> fetchPlantsFromDB(String searchQuery) {
        List<Plant> plants = new ArrayList<>();
        String sql = "SELECT * FROM plants WHERE common_name LIKE ? OR common_name LIKE ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, searchQuery + "%");
            statement.setString(2, "% " + searchQuery + "%");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                plants.add(new Plant(
                        rs.getString("common_name"),
                        rs.getString("environment"),
                        rs.getString("leaf_color"),
                        rs.getBoolean("blooms"),
                        rs.getString("growth_season"),
                        rs.getString("maintenance"),
                        rs.getBoolean("pet_safe"),
                        rs.getString("allergenic"),
                        rs.getString("size"),
                        rs.getString("humidity_requirement"),
                        rs.getString("image_path")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plants;
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
}
