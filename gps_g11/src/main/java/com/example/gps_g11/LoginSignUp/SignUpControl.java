package com.example.gps_g11.LoginSignUp;

import com.example.gps_g11.DB.EnviaPedidosDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;


/*public class SignUpControl {


    @FXML
    private Label messageLabelSU;
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField tfPass;
    @FXML
    private Button btSignUp;

    public void initialize() {
        btSignUp.setOnMouseEntered(event -> btSignUp.setCursor(Cursor.HAND));
        btSignUp.setOnMouseExited(event -> btSignUp.setCursor(Cursor.DEFAULT));
    }

    public void returnToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/login.fxml"));
            Parent homeRoot = loader.load();
            Stage stage = (Stage) messageLabelSU.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.setTitle("Login - SproutSmart");
            stage.show();
        } catch (IOException e) {
            messageLabelSU.setTextFill(Color.RED);
            messageLabelSU.setText("Fail to load home file.");
        }
    }


    @FXML
    public void signUpValidation() {
        try {
            // Check for empty fields and character limits
            if (isAnyFieldEmpty() || isAnyFieldTooLong()) {
                messageLabelSU.setTextFill(Color.RED);
                messageLabelSU.setText("Please fill in all fields and ensure no field exceeds 30 characters.");
            } else if (!isValidEmail(tfEmail.getText())) {
                messageLabelSU.setTextFill(Color.RED);
                messageLabelSU.setText("Invalid email format.");
            } else {
                // If all fields are valid
                messageLabelSU.setTextFill(Color.GREEN);
                messageLabelSU.setText("Registration successful");
            }
        } catch (Exception e) {
            // Handle any unexpected exceptions
            messageLabelSU.setTextFill(Color.RED);
            messageLabelSU.setText("Failed to validate registration.");
        }
    }

    private boolean isAnyFieldEmpty() {
        return isFieldEmpty(tfFirstName) || isFieldEmpty(tfLastName) || isFieldEmpty(tfUserName) ||
                isFieldEmpty(tfEmail) || isFieldEmpty(tfPass) ;
    }

    private boolean isFieldEmpty(TextField field) {
        return field == null || field.getText().trim().isEmpty();
    }

    private boolean isAnyFieldTooLong() {
        return isFieldTooLong(tfFirstName) || isFieldTooLong(tfLastName) || isFieldTooLong(tfUserName) ||
                isFieldTooLong(tfEmail) || isFieldTooLong(tfPass);
    }

    private boolean isFieldTooLong(TextField field) {
        return field != null && field.getText().length() > 30;
    }

    private boolean isValidEmail(String email) {
        // Simple regex for email validation
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }


} */
public class SignUpControl {

    private EnviaPedidosDB authService;

    @FXML
    private Label messageLabelSU;
    @FXML
    private TextField tfUserName;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField tfPass;
    @FXML
    private Button btSignUp;

    public void initialize() {
        btSignUp.setOnMouseEntered(event -> btSignUp.setCursor(Cursor.HAND));
        btSignUp.setOnMouseExited(event -> btSignUp.setCursor(Cursor.DEFAULT));
    }

    public void setAuthService(EnviaPedidosDB authService) {
        this.authService = authService;
        System.out.println("AuthService inicializado com sucesso.");
    }
    public void returnToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/login.fxml"));
            Parent homeRoot = loader.load();

            // Configurar o controlador de login para receber a instância de EnviaPedidosDB
            LoginControl loginController = loader.getController();
            loginController.setAuthService(this.authService);

            Stage stage = (Stage) messageLabelSU.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
            stage.setTitle("Login - SproutSmart");
            stage.show();
        } catch (IOException e) {
            messageLabelSU.setTextFill(Color.RED);
            messageLabelSU.setText("Fail to load home file.");
        }
    }

    @FXML
    public void signUpValidation() {
        try {
            if (isAnyFieldEmpty() || isAnyFieldTooLong()) {
                messageLabelSU.setTextFill(Color.RED);
                messageLabelSU.setText("Please fill in all fields and ensure no field exceeds 30 characters.");
            } else if (!isValidEmail(tfEmail.getText())) {
                messageLabelSU.setTextFill(Color.RED);
                messageLabelSU.setText("Invalid email format.");
            } else {
                String firstName = tfFirstName.getText().trim();
                String lastName = tfLastName.getText().trim();
                String username = tfUserName.getText().trim();
                String email = tfEmail.getText().trim();
                String password = tfPass.getText();

                boolean success = authService.registrarUser(firstName, lastName, username, email, password);

                if (success) {
                    messageLabelSU.setTextFill(Color.GREEN);
                    messageLabelSU.setText("Registration successful");
                    clearFields();
                } else {
                    messageLabelSU.setTextFill(Color.RED);
                    messageLabelSU.setText("Registration failed. Username or email may already be taken.");
                }
            }
        } catch (Exception e) {

            messageLabelSU.setTextFill(Color.RED);
            messageLabelSU.setText("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean isAnyFieldEmpty() {
        return isFieldEmpty(tfFirstName) || isFieldEmpty(tfLastName) || isFieldEmpty(tfUserName) ||
                isFieldEmpty(tfEmail) || isFieldEmpty(tfPass);
    }

    private boolean isFieldEmpty(TextField field) {
        return field == null || field.getText().trim().isEmpty();
    }

    private boolean isAnyFieldTooLong() {
        return isFieldTooLong(tfFirstName) || isFieldTooLong(tfLastName) || isFieldTooLong(tfUserName) ||
                isFieldTooLong(tfEmail) || isFieldTooLong(tfPass);
    }

    private boolean isFieldTooLong(TextField field) {
        return field != null && field.getText().length() > 30;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private void clearFields() {
        tfFirstName.clear();
        tfLastName.clear();
        tfUserName.clear();
        tfEmail.clear();
        tfPass.clear();
    }
}