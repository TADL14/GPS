package com.example.gps_g11.Home;

import com.example.gps_g11.Articles.ArticlesControl;
import com.example.gps_g11.Tasks.TasksControl;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;

public class HomeControl {

    @FXML
    private Label messageLabel;
    @FXML
    private VBox btMyGarden;
    @FXML
    private VBox btPlantLibrary;
    @FXML
    private VBox btTasks;
    @FXML
    private VBox btArticles;
    @FXML
    private VBox btProfileIcon;


    final String s1 = "Error load FXML file";

    public void initialize() {
        btMyGarden.setOnMouseEntered(event -> btMyGarden.setCursor(javafx.scene.Cursor.HAND));
        btMyGarden.setOnMouseExited(event -> btMyGarden.setCursor(javafx.scene.Cursor.DEFAULT));
        btPlantLibrary.setOnMouseEntered(event -> btPlantLibrary.setCursor(javafx.scene.Cursor.HAND));
        btPlantLibrary.setOnMouseExited(event -> btPlantLibrary.setCursor(javafx.scene.Cursor.DEFAULT));
        btTasks.setOnMouseEntered(event -> btTasks.setCursor(javafx.scene.Cursor.HAND));
        btTasks.setOnMouseExited(event -> btTasks.setCursor(javafx.scene.Cursor.DEFAULT));
        btArticles.setOnMouseEntered(event -> btArticles.setCursor(javafx.scene.Cursor.HAND));
        btArticles.setOnMouseExited(event -> btArticles.setCursor(javafx.scene.Cursor.DEFAULT));

        btProfileIcon.setOnMouseEntered(event -> btProfileIcon.setCursor(javafx.scene.Cursor.HAND));
        btProfileIcon.setOnMouseExited(event -> btProfileIcon.setCursor(javafx.scene.Cursor.DEFAULT));
    }

    // Método para ir para o Garden
    @FXML
    private void handleMyGardenButtonAction() {
//        loadFXML("/com/example/gps_g11/MyGarden.fxml", "My Garden - SproutSmart");
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

    // Método para ir para a PlantLibrary
    @FXML
    public void handlePlantLibraryButtonAction() {
//        loadFXML("/com/example/gps_g11/PlantLibrary.fxml" , "Plant Library - SproutSmart");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/PlantLibrary.fxml"));
            Parent homeRoot = loader.load();
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.setTitle("Plant Library - SproutSmart");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Fail to load PlantLibrary file.");
        }
    }

    // Método para ir para a Tasks
    @FXML
    public void handleTasksButtonAction() {
//        loadFXML("/com/example/gps_g11/Tasks.fxml", "Tasks - SproutSmart");
        messageLabel.setTextFill(Color.BLUE);
        messageLabel.setText("Loading Tasks...");

        Task<Parent> loadTasksTask = new Task<>() {
            @Override
            protected Parent call() throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Tasks.fxml"));
                Parent carregaTasks = loader.load();

                TasksControl tasksControl = loader.getController();
                tasksControl.loadTasksFromGardenDatabase();

                return carregaTasks;
            }
        };

        loadTasksTask.setOnSucceeded(event -> {
            try {
                Parent tasksRoot = loadTasksTask.getValue();
                Stage stage = (Stage) messageLabel.getScene().getWindow();
                stage.setScene(new Scene(tasksRoot));
                stage.setTitle("Tasks - SproutSmart");
            } finally {
                messageLabel.setText("");
            }
        });

        loadTasksTask.setOnFailed(event -> {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Failed to load Tasks.");
        });

        new Thread(loadTasksTask).start();
    }

    // Método para ir para a Articles
    @FXML
    public void handleArticlesButtonAction() {
        messageLabel.setTextFill(Color.BLUE);
        messageLabel.setText("Loading Articles...");

        Task<Parent> loadArticlesTask = new Task<>() {
            @Override
            protected Parent call() throws Exception {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Articles.fxml"));
                Parent carregaArticles = loader.load();

                ArticlesControl articlesControl = loader.getController();
                articlesControl.initialize();


                return carregaArticles;
            }
        };

        loadArticlesTask.setOnSucceeded(event -> {
            try {
                Parent articlesRoot = loadArticlesTask.getValue();
                Stage stage = (Stage) messageLabel.getScene().getWindow();
                stage.setScene(new Scene(articlesRoot));
                stage.setTitle("Articles - SproutSmart");
            } finally {
                messageLabel.setText("");
            }
        });

        loadArticlesTask.setOnFailed(event -> {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Failed to load Articles.");
        });

        new Thread(loadArticlesTask).start();
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




    private void loadFXML(String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/example/gps_g11/icons/logo.png")));
            stage.setTitle(title);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(s1);
        }
    }



}
