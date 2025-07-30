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
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Survey1Control {

    @FXML
    private Label LoadingLabel;


    private Plant surveyData = new Plant();

    @FXML
    private Label messageLabelQ1;

    ObservableList<String> Q1list = FXCollections
            .observableArrayList("outdoor", "indoor");

    ObservableList<String> Q3list = FXCollections
            .observableArrayList("yes", "no", "indifferent");

    @FXML
    private ChoiceBox<String> cbQ1;

    @FXML
    private TextField tfQ2;

    @FXML
    private ChoiceBox<String> cbQ3;

    @FXML
    private Button btNextS1;

    @FXML
    public void initialize() {
        btNextS1.setOnMouseEntered(event -> btNextS1.setCursor(javafx.scene.Cursor.HAND));
        btNextS1.setOnMouseExited(event -> btNextS1.setCursor(javafx.scene.Cursor.DEFAULT));
        cbQ1.setOnMouseEntered(event -> cbQ1.setCursor(javafx.scene.Cursor.HAND));
        cbQ1.setOnMouseExited(event -> cbQ1.setCursor(javafx.scene.Cursor.DEFAULT));
        cbQ3.setOnMouseEntered(event -> cbQ3.setCursor(javafx.scene.Cursor.HAND));
        cbQ3.setOnMouseExited(event -> cbQ3.setCursor(javafx.scene.Cursor.DEFAULT));
        cbQ1.setItems(Q1list);
        cbQ3.setItems(Q3list);
    }

    public void goToQ2() {


        String Q1 = cbQ1.getValue(); //indoor_outdorr
        String Q2 = tfQ2.getText(); // Colors
        String Q3 = cbQ3.getValue(); // flowers


        if (Q1 == null && Q2.isEmpty() && Q3 == null) {
            messageLabelQ1.setTextFill(Color.RED);
            messageLabelQ1.setText("Please fill the fields.");
        } else if (Q1 == null) {
            messageLabelQ1.setTextFill(Color.RED);
            messageLabelQ1.setText("Question 1 field is empty.");
        } else if (Q2.isEmpty()) {
            messageLabelQ1.setTextFill(Color.RED);
            messageLabelQ1.setText("Question 2 field is empty.");
        } else if (Q3 == null) {
            messageLabelQ1.setTextFill(Color.RED);
            messageLabelQ1.setText("Question 3 field is empty.");
        } else {
            try {
                surveyData.setIndoorOutdoor(cbQ1.getValue());
                surveyData.setColor(tfQ2.getText());
                surveyData.setFlowers(cbQ3.getValue().equals("yes"));

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Survey2.fxml"));
                Parent homeRoot = loader.load();
                Survey2Control survey2Control = loader.getController();
                survey2Control.setSurveyData(surveyData);
                Stage stage = (Stage) messageLabelQ1.getScene().getWindow();
                stage.setScene(new Scene(homeRoot));
                stage.show();
            } catch (IOException e) {
                messageLabelQ1.setTextFill(Color.RED);
                messageLabelQ1.setText("Fail to load Survey2 file.");
            }
        }
    }




}
