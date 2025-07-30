package com.example.gps_g11;

import com.example.gps_g11.Tasks.CompletedTask;
import com.example.gps_g11.Tasks.PlantTask;
import com.example.gps_g11.llm.LLMIntegration;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class PlantDetailsController extends Application {
    @FXML
    private Label plantNameLabel;
    @FXML
    private ImageView plantImageView;
    @FXML
    private Label labelStatus;
    @FXML
    private Label lightLabel;
    @FXML
    private Label waterLabel;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label soilLabel;
    @FXML
    private ImageView growingConditionsIcon;
    @FXML
    private Label messageLabel;
    @FXML
    private VBox tasksContainer;
    @FXML
    private ImageView logoImageView;
    @FXML
    private ImageView emoji;
    @FXML
    private ImageView viewPlantGoBack;

    private String plantName;

    private final com.example.gps_g11.llm.LLMIntegration LLMIntegration = new LLMIntegration();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Carrega o FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/details_text.fxml"));
        Scene scene = new Scene(loader.load());


        // Inicializa o Stage
        primaryStage.setTitle("Plant Details - SproutSmart");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Inicializa a classe controladora
        PlantDetailsController controller = loader.getController();
        // Testes com diferentes plantas
       // controller.loadPlantDetails("Aloe Vera");
        //controller.loadPlantDetails("Spider Plant");
    }

    public void initialize() {
        logoImageView.setOnMouseEntered(event -> logoImageView.setCursor(javafx.scene.Cursor.HAND));
        logoImageView.setOnMouseExited(event -> logoImageView.setCursor(javafx.scene.Cursor.DEFAULT));
        viewPlantGoBack.setOnMouseEntered(event -> viewPlantGoBack.setCursor(javafx.scene.Cursor.HAND));
        viewPlantGoBack.setOnMouseExited(event -> viewPlantGoBack.setCursor(javafx.scene.Cursor.DEFAULT));
        growingConditionsIcon.setOnMouseEntered(event -> growingConditionsIcon.setCursor(javafx.scene.Cursor.HAND));
        growingConditionsIcon.setOnMouseExited(event -> growingConditionsIcon.setCursor(javafx.scene.Cursor.DEFAULT));

        // Adiciona um  evento para mostrar as growing conditions
        growingConditionsIcon.setOnMouseClicked(event -> toggleGrowingConditionsVisibility());
    }

    void loadPlantDetails(String plantName) {
        this.plantName = plantName;
        System.out.println("This is the plant name: " + this.plantName);
        String url = "jdbc:sqlite:garden_database.db";
        String sql = "SELECT * FROM garden_plants WHERE common_name = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, plantName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                plantNameLabel.setText(rs.getString("common_name"));
                lightLabel.setText("Light: " + rs.getString("light"));
                waterLabel.setText("Water: " + rs.getString("water"));
                temperatureLabel.setText("Temperature: " + rs.getString("temperature"));
                soilLabel.setText("Soil: " + rs.getString("soil"));

                // Carrega a imagem da planta
                String imagePath = "src/main/resources/" + rs.getString("image_path");
                //String imagePath = "src/main/resources/images/aloe_vera.jpg";
                File file = new File(imagePath);
                if (file.exists()) {
                    Image img = new Image(file.toURI().toString());
                    System.out.println("Imagem carregada com sucesso: " + img.getUrl());

                    plantImageView.setImage(img);
                    plantImageView.setFitWidth(200);
                    plantImageView.setFitHeight(200);
                    plantImageView.setVisible(true);



                } else {
                    System.out.println("Imagem não encontrada em: " + imagePath);
                }

            }
        } catch (SQLException e) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Erro ao carregar detalhes da planta.");
            e.printStackTrace();
        }
        loadTasksForSpecificPlant(plantName);
    }

    // Alterna a visibilidade da caixa de growing conditions
    private void toggleGrowingConditionsVisibility() {

        // Cria uma janela para mostrar as growing conditions
        Stage growingConditionsStage = new Stage();

        // Layout da nova janela
        VBox growingConditionsLayout = new VBox(10);
        growingConditionsLayout.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20;");

        // Adiciona os Labels com as growing conditions
        Label titleLabel = new Label("Growing Conditions");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label lightConditionLabel = new Label("Light: " + lightLabel.getText().substring(7)); // Remove o prefixo "Light: "
        Label waterConditionLabel = new Label("Water: " + waterLabel.getText().substring(7)); // Remove o prefixo "Water: "
        Label temperatureConditionLabel = new Label("Temperature: " + temperatureLabel.getText().substring(12)); // Remove o prefixo "Temperature: "
        Label soilConditionLabel = new Label("Soil: " + soilLabel.getText().substring(6)); // Remove o prefixo "Soil: "

        // Adiciona as Labels ao layout
        growingConditionsLayout.getChildren().addAll(titleLabel, lightConditionLabel, waterConditionLabel,
                temperatureConditionLabel, soilConditionLabel);

        // Configura a nova janela
        Scene growingConditionsScene = new Scene(growingConditionsLayout, 300, 250);  // Tamanho ajustado
        growingConditionsStage.setTitle("Growing Conditions - " + plantNameLabel.getText());
        growingConditionsStage.setScene(growingConditionsScene);

        // Mostra a nova janela
        growingConditionsStage.show();
    }

    public void loadTasksForSpecificPlant(String plantName) {
        if (plantName == null || plantName.isEmpty()) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Plant name not specified.");
            return;
        }

        tasksContainer.getChildren().clear();  // Clear previous tasks

        try {
            List<PlantTask> plantTasks = LLMIntegration.callTaskAPI(plantName);
            Set<CompletedTask> completedTasks = loadCompletedTasks();  // Load completed tasks

            List<PlantTask> filteredTasks = new ArrayList<>();
            for (PlantTask task : plantTasks) {
                CompletedTask completedTask = findCompletedTask(completedTasks, plantName, task);
                if (completedTask == null || shouldTaskReappear(completedTask, task.getRecurrence())) {
                    filteredTasks.add(task);
                }
            }

            if (filteredTasks.isEmpty()) {
                messageLabel.setTextFill(Color.GREEN);
                messageLabel.setText("No pending tasks for this plant.");
            } else {
                displayTasksForPlant(filteredTasks, completedTasks);  // Update the tasksContainer
                if (isPlantHealthy(plantTasks, completedTasks)) {
                    labelStatus.setTextFill(Color.GREEN);
                    labelStatus.setText("Healthy");
                    emoji.setImage(new Image(getClass().getResource("icons/happiness.png").toExternalForm()));
                } else {
                    labelStatus.setTextFill(Color.RED);
                    labelStatus.setText("Unhealthy");
                    emoji.setImage(new Image(getClass().getResource("icons/sadness.png").toExternalForm()));
                }
            }

        } catch (Exception e) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Error loading tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to dynamically create and add CheckBoxes to the tasksContainer
    private void displayTasksForPlant(List<PlantTask> tasks, Set<CompletedTask> completedTasks ) {

        List<CheckBox> tasksForPlant = new ArrayList<>();
        for (PlantTask task : tasks) {
            CheckBox taskCheckBox = new CheckBox(task.getDescription());
            taskCheckBox.setOnAction(event -> handleTaskCompletion(task, taskCheckBox.isSelected()));

            taskCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                taskCheckBox.setTextFill(newValue ? javafx.scene.paint.Color.GRAY : javafx.scene.paint.Color.BLACK);

                if (newValue) {
                    // Update completed tasks set and save it
                    completedTasks.add(new CompletedTask(plantName, task.getDescription(), LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString()));
                    saveCompletedTasks(completedTasks);

                    // Remove the task from the container
                    tasksContainer.getChildren().remove(taskCheckBox);
                }

                // Refresh plant health status
                try {
                    refreshPlantHealthStatus();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });


            tasksForPlant.add(taskCheckBox);
        }
        ObservableList<CheckBox> observableTasks = FXCollections.observableArrayList(tasksForPlant);
        tasksContainer.getChildren().addAll(observableTasks);
    }

    // Handle task completion logic (simplified for example purposes)
    private void handleTaskCompletion(PlantTask task, boolean completed) {
        if (completed) {
            // Logic to mark the task as completed (update the JSON file or database)
            System.out.println("Task completed: " + task.getDescription());
        }
    }

    private Set<CompletedTask> loadCompletedTasks() {
        Set<CompletedTask> completedTasks = new HashSet<>();
        File file = new File("src/main/resources/Tasks/completed_tasks.json");  // Caminho do ficheiro

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Gson gson = new Gson();
                CompletedTask[] tasksArray = gson.fromJson(reader, CompletedTask[].class);

                if (tasksArray != null) {
                    completedTasks.addAll(Arrays.asList(tasksArray));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return completedTasks;
    }

    private CompletedTask findCompletedTask(Set<CompletedTask> completedTasks, String plantName, PlantTask task) {
        return completedTasks.stream()
                .filter(ct -> ct.getPlantName().equals(plantName) && ct.getTaskDescription().equals(task.getDescription()))
                .findFirst()
                .orElse(null);
    }

    private void refreshPlantHealthStatus() throws IOException {
        Set<CompletedTask> updatedCompletedTasks = loadCompletedTasks();  // Reload the completed tasks
        List<PlantTask> plantTasks = LLMIntegration.callTaskAPI(plantName);  // Reload the tasks from the API

        if (isPlantHealthy(plantTasks, updatedCompletedTasks)) {
            labelStatus.setTextFill(Color.GREEN);
            labelStatus.setText("Healthy");
            emoji.setImage(new Image(getClass().getResource("icons/happiness.png").toExternalForm()));
        } else {
            labelStatus.setTextFill(Color.RED);
            labelStatus.setText("Unhealthy");
            emoji.setImage(new Image(getClass().getResource("icons/sadness.png").toExternalForm()));
        }
    }


    private boolean shouldTaskReappear(CompletedTask completedTask, String recurrence) {
        try {
            // Adicionar hora padrão se necessário
            String completionDate = completedTask.getCompletionDate();
            if (completionDate.length() == 10) { // formato "yyyy-MM-dd"
                completionDate = completionDate + "T00:00:00";
            }

            LocalDateTime lastCompletionDate = LocalDateTime.parse(completionDate);
            LocalDateTime now = LocalDateTime.now();

            switch (recurrence.toLowerCase()) {
                case "daily":
                    return lastCompletionDate.plusDays(1).isBefore(now) || lastCompletionDate.plusDays(1).isEqual(now);
                case "weekly":
                    return lastCompletionDate.plusWeeks(1).isBefore(now) || lastCompletionDate.plusWeeks(1).isEqual(now);
                case "monthly":
                    return lastCompletionDate.plusMonths(1).isBefore(now) || lastCompletionDate.plusMonths(1).isEqual(now);
                case "annual":
                    return lastCompletionDate.plusYears(1).isBefore(now) || lastCompletionDate.plusYears(1).isEqual(now);
                default:
                    return false;
            }
        } catch (DateTimeParseException e) {
            System.err.println("Erro ao analisar data de conclusão: " + completedTask.getCompletionDate());
            return false;
        }
    }

    private void saveCompletedTasks(Set<CompletedTask> completedTasks) {
        File directory = new File("src/main/resources/Tasks");
        if (!directory.exists()) {
            directory.mkdirs(); // Cria a pasta se ela não existir
        }
        File file = new File(directory,"completed_tasks.json");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            Gson gson = new Gson();
            String json = gson.toJson(completedTasks);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isPlantHealthy(List<PlantTask> tasks, Set<CompletedTask> completedTasks) {
        for (PlantTask task : tasks) {
            if (task.isCritical()) {
                CompletedTask completedTask = findCompletedTask(completedTasks, plantName, task);
                if (completedTask == null || shouldTaskReappear(completedTask, task.getRecurrence())) {
                    return false;
                }
            }
        }
        return true;
    }


    @FXML
    public void goBack(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/MyGarden.fxml"));
            Parent homeRoot = loader.load();
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
        } catch (IOException e) {
            e.printStackTrace();
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
