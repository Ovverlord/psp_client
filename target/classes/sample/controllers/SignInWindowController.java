package controllers;

import AlertWindow.AlertWindow;
import Main.Main;
import animation.Shake;
import classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.Connection;
import network.JSONParser;

import java.io.IOException;
public class SignInWindowController {

    @FXML
    private Button SignInButton;

    @FXML
    private Button SignUpButton;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    Connection connection;
    AlertWindow alert;

    @FXML
    void SignInButtonClicked(ActionEvent event)
    {
        boolean flag = false;

        if(passwordField.getText().length()==0)
        {
            Shake PasswordAnim = new Shake(passwordField);
            PasswordAnim.playAnimation();
            flag = true;
        }
        if(loginField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(loginField);
            LoginAnim.playAnimation();
            flag = true;
        }

        if(flag){return;}

        connection.makeQuery("signIn//"+ JSONParser.jsonFromObject(new User(loginField.getText(),passwordField.getText(), null)));
        String response = "";
        response = connection.getResponse();
        if(response.equals("error"))
        {
            alert.incorrectLoginOrPassword();
        }
        else if(response.equals("1"))
        {
            try {
                Stage stage = Main.getPrimaryStage();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/AdminWindow.fxml")));
                stage.setScene(scene);
                stage.show();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("user");
        }

//        try {
//            Stage stage = Main.getPrimaryStage();

//            stage.setScene(scene);
//            stage.show();
//
//        } catch (IOException | NullPointerException e) {
//            e.printStackTrace();
//            System.out.println("ошибка подключения");
//        }
    }

    @FXML
    void SignUpButtonClicked(ActionEvent event)
    {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/SignUpWindow.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void initialize(){
        connection = Connection.getInstance();
        alert = AlertWindow.getAlert();
    }
}
