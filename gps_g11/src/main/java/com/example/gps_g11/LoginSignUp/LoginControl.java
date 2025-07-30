package com.example.gps_g11.LoginSignUp;

import com.example.gps_g11.DB.EnviaPedidosDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class LoginControl {

    public static String user;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;
    @FXML
    private Button btLogin;

    private EnviaPedidosDB enviaPedido; // Agora você terá uma instância de EnviaPedidosDB

    // Método para injetar a instância de EnviaPedidosDB
    public void setAuthService(EnviaPedidosDB authService) {
        this.enviaPedido = authService;
    }

    public void initialize() {
        btLogin.setOnMouseEntered(event -> btLogin.setCursor(Cursor.HAND));
        btLogin.setOnMouseExited(event -> btLogin.setCursor(Cursor.DEFAULT));
    }

    // Método chamado ao clicar no botão de login
    @FXML
    public void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validação de login
        if (enviaPedido.login(username, password)) {
            user=username;
            messageLabel.setTextFill(Color.DARKGREEN);
            messageLabel.setText("Login successful!");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g11/Home.fxml"));
                Parent homeRoot = loader.load();
                Stage stage = (Stage) messageLabel.getScene().getWindow();
                stage.setScene(new Scene(homeRoot));
                stage.setTitle("Home - SproutSmart");
                stage.show();
            } catch (IOException e) {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Fail to load home file.");
            }
        } else if(username.isEmpty() && password.isEmpty()){
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Please fill the fields.");
        }
        else if(username.isEmpty()) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Username field is empty.");
        } else if(password.isEmpty()) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Password field is empty.");
        } else {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Invalid username or password.");
        }
    }
    // Método para lidar com o evento de pressionar uma tecla
    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            handleLogin();
        } else if (event.getCode() == KeyCode.DOWN) {
            if (event.getSource() == usernameField) {
                passwordField.requestFocus(); // Mover para o PasswordField
            }
        } else if (event.getCode() == KeyCode.UP) {
            if (event.getSource() == passwordField) {
                usernameField.requestFocus(); // Mover para o TextField
            }
        }
    }

   public void goToSignUp() {
       try {
           FXMLLoader loaderSignUp = new FXMLLoader(getClass().getResource("/com/example/gps_g11/SignUp.fxml"));
           Parent signUpRoot = loaderSignUp.load();

           // Passa a instância de EnviaPedidosDB para o controlador do SignUp
           SignUpControl signUpController = loaderSignUp.getController();
           if (signUpController != null) {
               signUpController.setAuthService(this.enviaPedido); // Passa a instância corretamente
           }

           Stage stage = (Stage) usernameField.getScene().getWindow();
           stage.setScene(new Scene(signUpRoot));
           stage.setTitle("Sign Up - SproutSmart");
           stage.show();
       } catch (IOException e) {
           messageLabel.setTextFill(Color.RED);
           messageLabel.setText("Fail to load sign up file.");
       }
   }

}

