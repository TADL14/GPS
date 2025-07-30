package com.example.gps_g11.survey;

import com.example.gps_g11.GardenPlant.Plant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Survey2Control {

    private Plant surveyData;

    public void setSurveyData(Plant surveyData) {
        this.surveyData = surveyData;
    }

    @FXML
    private Label messageLabelQ2;

    ObservableList<String> Q4list = FXCollections
            .observableArrayList("Autumn", "Winter", "Spring", "Summer");

    ObservableList<String> Q5list = FXCollections
            .observableArrayList("low", "medium", "high");

    ObservableList<String> Q6list = FXCollections
            .observableArrayList("yes", "no", "indifferent");

    @FXML
    private ChoiceBox<String> cbQ4;

    @FXML
    private ChoiceBox<String> cbQ5;

    @FXML
    private ChoiceBox<String> cbQ6;

    @FXML
    private Button btNextS2;

    @FXML
    private Button btBackS2;

    @FXML
    public void initialize() {
        btNextS2.setOnMouseEntered(event -> btNextS2.setCursor(javafx.scene.Cursor.HAND));
        btNextS2.setOnMouseExited(event -> btNextS2.setCursor(javafx.scene.Cursor.DEFAULT));
        btBackS2.setOnMouseEntered(event -> btBackS2.setCursor(javafx.scene.Cursor.HAND));
        btBackS2.setOnMouseExited(event -> btBackS2.setCursor(javafx.scene.Cursor.DEFAULT));
        cbQ4.setOnMouseEntered(event -> cbQ4.setCursor(javafx.scene.Cursor.HAND));
        cbQ4.setOnMouseExited(event -> cbQ4.setCursor(javafx.scene.Cursor.DEFAULT));
        cbQ5.setOnMouseEntered(event -> cbQ5.setCursor(javafx.scene.Cursor.HAND));
        cbQ5.setOnMouseExited(event -> cbQ5.setCursor(javafx.scene.Cursor.DEFAULT));
        cbQ6.setOnMouseEntered(event -> cbQ6.setCursor(javafx.scene.Cursor.HAND));
        cbQ6.setOnMouseExited(event -> cbQ6.setCursor(javafx.scene.Cursor.DEFAULT));
        cbQ4.setItems(Q4list);
        cbQ5.setItems(Q5list);
        cbQ6.setItems(Q6list);
    }

    public void goToQ1() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Survey1.fxml"));
            Parent homeRoot = loader.load();

            Stage stage = (Stage) messageLabelQ2.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.show();
        } catch (IOException e) {
            messageLabelQ2.setTextFill(Color.RED);
            messageLabelQ2.setText("Fail to load Survey2 file.");
        }
    }

    public void goToQ3() {
        String Q4 = cbQ4.getValue(); //season
        String Q5 = cbQ5.getValue(); //maintance
        String Q6 = cbQ6.getValue(); //PetFriendly

        if (Q4 == null && Q5 == null && Q6 == null) {
            messageLabelQ2.setTextFill(Color.RED);
            messageLabelQ2.setText("Please fill the fields.");
        } else if (Q4 == null) {
            messageLabelQ2.setTextFill(Color.RED);
            messageLabelQ2.setText("Question 4 field is empty.");
        } else if (Q5 == null) {
            messageLabelQ2.setTextFill(Color.RED);
            messageLabelQ2.setText("Question 5 field is empty.");
        } else if (Q6 == null) {
            messageLabelQ2.setTextFill(Color.RED);
            messageLabelQ2.setText("Question 6 field is empty.");
        } else {
            try {
                surveyData.setSeason(cbQ4.getValue()); //season
                surveyData.setMaintenance(cbQ5.getValue()); //maintance
                surveyData.setPetFriendly(cbQ6.getValue().equals("yes")); //PetFriendly
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Survey3.fxml"));
                Parent homeRoot = loader.load();
                Survey3Control survey3Control = loader.getController();
                survey3Control.setSurveyData(surveyData);
                Stage stage = (Stage) messageLabelQ2.getScene().getWindow();
                stage.setScene(new Scene(homeRoot));
                stage.show();
            } catch (IOException e) {
                messageLabelQ2.setTextFill(Color.RED);
                messageLabelQ2.setText("Fail to load Survey3 file.");
            }
        }
    }
}
