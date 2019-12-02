package controllers;

import Main.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.Connection;

import java.io.IOException;
public class SignInWindowController {

    @FXML
    private Button SignInButton;

    @FXML
    private Button SignUpButton;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    Connection connection;

    @FXML
    void SignInButtonClicked(ActionEvent event)
    {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/UserWindow.fxml")));
            stage.setScene(scene);
            stage.show();
            connection.makeQuery("Hello from Client");
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.out.println("ошибка подключения");
        }
    }

    @FXML
    void SignUpButtonClicked(ActionEvent event)
    {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/User2Window.fxml")));
            stage.setScene(scene);
            stage.show();
            connection.makeQuery("Hello from Client2");
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            //System.out.println("ошибка подключения");
        }
    }

    public void initialize(){
        connection = Connection.getInstance();
    }
}
