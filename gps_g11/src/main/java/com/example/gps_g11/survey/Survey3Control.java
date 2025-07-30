package com.example.gps_g11.survey;

import com.example.gps_g11.GardenPlant.Plant;
import com.example.gps_g11.DB.DataBase;
//import com.sun.javafx.charts.Legend;
import com.example.gps_g11.DB.PlantDatabase;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Survey3Control {

    private StringBuilder repeatedPlants ;
    private Plant surveyData;
    int processa3Vezes;
    public void setSurveyData(Plant surveyData) {
        this.surveyData = surveyData;
    }

    @FXML
    private Label messageLabelQ3;

    @FXML
    private Label LoadingLabel;


    private String plantName;
    ObservableList<String> Q8list = FXCollections
            .observableArrayList("big", "small", "indifferent");

    ObservableList<String> Q9list = FXCollections
            .observableArrayList("dry", "humid");


    @FXML
    private TextField tfQ7;

    @FXML
    private ChoiceBox<String> cbQ8;

    @FXML
    private ChoiceBox<String> cbQ9;

    @FXML
    private Button btSubmit;

    @FXML
    private Button btBackS3;

    public void initialize() {
        btSubmit.setOnMouseEntered(event -> btSubmit.setCursor(Cursor.HAND));
        btSubmit.setOnMouseExited(event -> btSubmit.setCursor(Cursor.DEFAULT));
        btBackS3.setOnMouseEntered(event -> btBackS3.setCursor(Cursor.HAND));
        btBackS3.setOnMouseExited(event -> btBackS3.setCursor(Cursor.DEFAULT));
        cbQ8.setOnMouseEntered(event -> cbQ8.setCursor(Cursor.HAND));
        cbQ8.setOnMouseExited(event -> cbQ8.setCursor(Cursor.DEFAULT));
        cbQ9.setOnMouseEntered(event -> cbQ9.setCursor(Cursor.HAND));
        cbQ9.setOnMouseExited(event -> cbQ9.setCursor(Cursor.DEFAULT));
        cbQ8.setItems(Q8list);
        cbQ9.setItems(Q9list);
        this.repeatedPlants = new StringBuilder();
        this.processa3Vezes = 0;
    }

    public void goToQ2() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Survey2.fxml"));
            Parent homeRoot = loader.load();
            Stage stage = (Stage) messageLabelQ3.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.show();
        } catch (IOException e) {
            messageLabelQ3.setTextFill(Color.RED);
            messageLabelQ3.setText("Fail to load Survey2 file.");
        }
    }

    public void submitSurvey() {
        String Q7 = tfQ7.getText();  //alergies
        String Q8 = cbQ8.getValue(); //size
        String Q9 = cbQ9.getValue(); //dry or hummid

        if (Q7.isEmpty() && Q8 == null && Q9 == null) {
            messageLabelQ3.setTextFill(Color.RED);
            messageLabelQ3.setText("Please fill the fields.");
        } else if (Q7.isEmpty()) {
            messageLabelQ3.setTextFill(Color.RED);
            messageLabelQ3.setText("Question 7 field is empty.");
        } else if (Q8 == null) {
            messageLabelQ3.setTextFill(Color.RED);
            messageLabelQ3.setText("Question 8 field is empty.");
        } else if (Q9 == null) {
            messageLabelQ3.setTextFill(Color.RED);
            messageLabelQ3.setText("Question 9 field is empty.");
        } else {
            messageLabelQ3.setTextFill(Color.DARKGREEN);
            messageLabelQ3.setText("Survey submitted successfully!");

            surveyData.setAllergies(Q7); //alergies
            surveyData.setSize(Q8);      //SIZE
            surveyData.sethumidity_requirement(Q9);

            Timeline[] timelineRef = new Timeline[1];


            Thread backgroundThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // Start the loading animation on the FX thread
                    Platform.runLater(() -> {
                        animateLoading(LoadingLabel, timelineRef); // This is the animation call
                    });

                    // Call the blocking method (this simulates the long-running task)
                    plantName = SurveyManager.SurveyReply(surveyData, null);

                    // After the task is done, update the UI with the result and stop the animation
                    Platform.runLater(() -> {
                        // Stop the loading animation
                        if (timelineRef[0] != null) {
                            timelineRef[0].stop();  // Stop the animation
                        }
                        LoadingLabel.setText("");  // Clear the loading text

                        // Handle the result (e.g., show the plant name)
                        while (handlePlantRecommendation(plantName) != null && processa3Vezes < 3) {
                            Platform.runLater(() -> {
                                animateLoading(LoadingLabel, new Timeline[1]); // This is the animation call
                            });

                            processa3Vezes++;

                            if (repeatedPlants == null) {
                                repeatedPlants = new StringBuilder();
                            }
                            repeatedPlants.append(plantName).append(" ");
                            plantName = SurveyManager.SurveyReply(surveyData, repeatedPlants.toString());
                        }

                        if (processa3Vezes >= 3) {
                            finishSurvey();
                        }
                    });
                }
            });
// Start the background thread
            backgroundThread.start();

        }
    }


    private void stopLoadingAnimation(Timeline[] timeline) {
        if (timeline[0] != null) {
            timeline[0].stop();  // Stop the animation
        }
    }


    private void processPlantSuggestions(String plantName) {
        AtomicInteger processCount = new AtomicInteger(0);  // To safely manage loop count
        StringBuilder repeatedPlants = new StringBuilder();

        while (plantName != null && processCount.get() < 3) {
            processCount.incrementAndGet();

            // Avoid repeating plant suggestions
            repeatedPlants.append(plantName).append(" ");
            plantName = SurveyManager.SurveyReply(surveyData, repeatedPlants.toString());

            // Handle the plant recommendation UI update
            String result = handlePlantRecommendation(plantName);
            if (result == null) break;  // Exit loop if there's no valid result
        }

        // Finalize the survey if maximum iterations are reached
        if (processCount.get() >= 3) {
            finishSurvey();
        }
    }
        private String handlePlantRecommendation(String plantName ){
            AtomicReference<String> teste = new AtomicReference<>();
            Image image;
            ImageView imageView = null;
            String imagePath = PlantDatabase.getImage(plantName);
            if(imagePath==null){
                System.err.println("No image found for plant: " + plantName);
            }else {

                try {
                    image = new Image(getClass().getResourceAsStream(imagePath));
                    imageView = new ImageView(image);
                } catch (Exception e) {
                    System.err.println("Error loading cactus image: " + e.getMessage());
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Error loading image");
                    errorAlert.setContentText("The plant image could not be displayed.");
                    errorAlert.showAndWait();
                }

            }
            // creates a new alert window
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Recommended Plant");




            // creates a vertical layout
            VBox content = new VBox();
            content.setSpacing(10);  // Espaçamento entre os elementos

            // Add the header to the VBox
            Label headerLabel = new Label("Your plant is: " + plantName);
            headerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");  // Estilo do texto

            // Add the image to the VBox
            if (imageView != null) {
                imageView.setFitHeight(400);  // Define a altura da imagem
                imageView.setFitWidth(400);    // Define a largura da imagem
                imageView.setPreserveRatio(true);  // Mantém a proporção da imagem

                // Add the elements to the VBox
                content.getChildren().addAll(headerLabel, imageView);
            } else {
                content.getChildren().addAll(headerLabel);
            }

            // Add the additional question: "Do you want to add this plant?"
            Label questionLabel = new Label("Do you want to add this plant?");
            questionLabel.setStyle("-fx-font-size: 14px;");
            content.getChildren().add(questionLabel);

            // Set the content of the alert
            alert.getDialogPane().setContent(content);

            ButtonType yesButton = new ButtonType("Yes");
            ButtonType noButton = new ButtonType("No");
            ButtonType generateOtherButton = new ButtonType("Generate Other Plant");
            alert.getButtonTypes().setAll(yesButton, noButton,generateOtherButton);


            // Show the alert and wait for the user to click a button
            alert.showAndWait().ifPresent(response -> {
                if (response == yesButton) {
                    // If the user clicks Yes, add the plant to the garden
                    DataBase.savePlantForUser(plantName);
                    finishSurvey();
                } else if (response == noButton) {
                    // If the user clicks No, just close the alert
                    System.out.println("User declined the plant.");
                    finishSurvey();
                } else if (response == generateOtherButton) {

                    teste.set(plantName);
                }
            });

            return teste.get();
        }


        private void finishSurvey(){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/MyGarden.fxml"));
                Parent homeRoot = loader.load();
                Stage stage = (Stage) messageLabelQ3.getScene().getWindow();
                stage.setScene(new Scene(homeRoot));
                stage.setTitle("My Garden - SproutSmart");
                stage.show();
            } catch (IOException e) {
                messageLabelQ3.setTextFill(Color.RED);
                messageLabelQ3.setText("Fail to load SubmittingRecommendation file.");
            }
        }



private void animateLoading(Label loadingLabel, Timeline[] timelineRef) {
    // Set the total time of animation (5 seconds) and interval (0.5 seconds per cycle)
    double interval = 0.5;  // 0.5 seconds per cycle
    int cycles = 30; // 10 cycles to complete 5 seconds of animation

    // Create the animation for three dots
    timelineRef[0] = new Timeline(
            new KeyFrame(Duration.seconds(interval), event -> {
                String currentText = loadingLabel.getText();
                if (currentText.equals("Loading")) {
                    loadingLabel.setText("Loading.");
                } else if (currentText.equals("Loading.")) {
                    loadingLabel.setText("Loading..");
                } else if (currentText.equals("Loading..")) {
                    loadingLabel.setText("Loading...");
                } else {
                    loadingLabel.setText("Loading");
                }
            })
    );
    timelineRef[0].setCycleCount(cycles);  // Set to run for 10 cycles (5 seconds)
    timelineRef[0].play();

    // Create a PauseTransition to wait for 5 seconds (after animation is done)
    PauseTransition pause = new PauseTransition(Duration.seconds(cycles * interval));
    pause.setOnFinished(event -> {
        // Clear the label text after 5 seconds
        loadingLabel.setText("");
    });
    pause.play();
}

}
