package com.example.gps_g11.Tasks;

import com.example.gps_g11.DB.GardenDatabase;
import com.example.gps_g11.llm.LLMIntegration;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TasksControl {

    @FXML
    private Label messageLabel;

    @FXML
    private ImageView logoImageView;

    @FXML
    private ListView<HBox> tasksListView;

    private final com.example.gps_g11.llm.LLMIntegration LLMIntegration = new LLMIntegration();



    public void initialize() {
        logoImageView.setOnMouseEntered(event -> logoImageView.setCursor(javafx.scene.Cursor.HAND));
        logoImageView.setOnMouseExited(event -> logoImageView.setCursor(javafx.scene.Cursor.DEFAULT));

        //loadTasksForPlant("Aloe vera");
        loadTasksFromGardenDatabase();
    }


    public void loadTasksFromGardenDatabase() {
        // Obtenha todos os nomes de plantas da base de dados do jardim
        List<String> plantNames = GardenDatabase.returnAllPlantsNames();

        // Limpa as tarefas antes de adicionar novas
        tasksListView.getItems().clear();

        // Carrega tarefas para cada planta
        for (String plantName : plantNames) {
            try {
                List<PlantTask> plantTasks = LLMIntegration.callTaskAPI(plantName);

                // Carrega as tarefas concluídas antes de filtrar as novas
                Set<CompletedTask> completedTasks = loadCompletedTasks();

                // Filtra tarefas para incluir somente as necessárias
                List<PlantTask> filteredTasks = new ArrayList<>();
                for (PlantTask task : plantTasks) {
                    CompletedTask completedTask = findCompletedTask(completedTasks, plantName, task);
                    if (completedTask == null || shouldTaskReappear(completedTask, task.getRecurrence())) {
                        filteredTasks.add(task);
                    }
                }

                // Se a tarefa for recorrente e já foi completada, remova a tarefa do JSON de completadas
                List<CompletedTask> tasksToRemove = new ArrayList<>();
                for (CompletedTask completedTask : completedTasks) {
                    if (shouldTaskReappear(completedTask, "daily") ||
                            shouldTaskReappear(completedTask, "weekly") ||
                            shouldTaskReappear(completedTask, "monthly")) {
                        tasksToRemove.add(completedTask); // Adiciona para remoção posterior
                    }
                }

                // Remove as tarefas após o loop
                completedTasks.removeAll(tasksToRemove);

                if (!filteredTasks.isEmpty()) {
                    displayTasksForPlant(plantName, filteredTasks, completedTasks);
                }

            } catch (Exception e) {
                messageLabel.setTextFill(javafx.scene.paint.Color.RED);
                messageLabel.setText("Erro ao carregar tarefas para a planta: " + plantName + ". " + e.getMessage());
                e.printStackTrace(); // Log para depuração
            }
        }
    }

    private CompletedTask findCompletedTask(Set<CompletedTask> completedTasks, String plantName, PlantTask task) {
        return completedTasks.stream()
                .filter(ct -> ct.getPlantName().equals(plantName) && ct.getTaskDescription().equals(task.getDescription()))
                .findFirst()
                .orElse(null);
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
    private void displayTasksForPlant(String plantName, List<PlantTask> tasks, Set<CompletedTask> completedTasks) {
        Label plantLabel = new Label(plantName);
        plantLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        plantLabel.setTextFill(javafx.scene.paint.Color.GREEN);

        List<HBox> tasksForPlant = new ArrayList<>();
        tasksForPlant.add(new HBox(10, plantLabel));

        for (PlantTask task : tasks) {
            HBox taskBox = new HBox(10);
            CheckBox taskCheckBox = new CheckBox(task.getDescription());
            taskCheckBox.setFont(new javafx.scene.text.Font(20));

            taskCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                taskCheckBox.setTextFill(newValue ? javafx.scene.paint.Color.GRAY : javafx.scene.paint.Color.BLACK);
                if (newValue) {
                    // Atualiza lista de tarefas concluídas e salva
                    completedTasks.add(new CompletedTask(plantName, task.getDescription(), LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString()));
                    saveCompletedTasks(completedTasks);

                    // Atualiza a interface
                    tasksListView.getItems().remove(taskBox);
                }
            });

            taskBox.getChildren().add(taskCheckBox);
            tasksForPlant.add(taskBox);
        }

        ObservableList<HBox> observableTasks = FXCollections.observableArrayList(tasksForPlant);
        tasksListView.getItems().addAll(observableTasks);
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




    private void loadTasksForPlant(String plantType) {
        try {
            List<PlantTask> plantTasks = LLMIntegration.callTaskAPI(plantType);

            if (plantTasks == null || plantTasks.isEmpty()) {
                messageLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
                messageLabel.setText("No tasks available for the selected plant type.");
                return;
            }

            List<HBox> tasksForPlant = new ArrayList<>();
            for (PlantTask plantTask : plantTasks) {
                HBox taskBox = new HBox(10);
                CheckBox taskCheckBox = new CheckBox(plantTask.getDescription());
                taskCheckBox.setFont(new javafx.scene.text.Font(20));

                taskCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    taskCheckBox.setTextFill(newValue ? javafx.scene.paint.Color.GRAY : javafx.scene.paint.Color.BLACK);
                });

                taskBox.getChildren().add(taskCheckBox);
                tasksForPlant.add(taskBox);
            }

            ObservableList<HBox> observableTasks = FXCollections.observableArrayList(tasksForPlant);
            tasksListView.setItems(observableTasks);

        } catch (IOException e) {
            messageLabel.setTextFill(javafx.scene.paint.Color.RED);
            messageLabel.setText("Error loading tasks: " + e.getMessage());
            e.printStackTrace(); // This will print the stack trace in the console for debugging
        }
    }

    public void gotoHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Home.fxml"));
            Parent homeRoot = loader.load();
            Stage stage = (Stage) messageLabel.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.setTitle("Home - SproutSmart");
            stage.show();
        } catch (IOException e) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Fail to load Home file.");
        }
    }
}