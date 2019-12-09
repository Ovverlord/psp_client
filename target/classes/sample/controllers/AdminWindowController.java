package controllers;

import AlertWindow.AlertWindow;
import Main.Main;
import animation.Shake;
import classes.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import network.Connection;
import network.JSONParser;

import java.io.IOException;

public class AdminWindowController {

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> loginColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> adminColumn;

    @FXML
    private ComboBox roleComboBox;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    Connection connection;
    AlertWindow alert;
    ObservableList<User> oblist = FXCollections.observableArrayList();

    @FXML
    void addButtonClicked(ActionEvent event) {
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
        String role = "";
        if(roleComboBox.getSelectionModel().getSelectedIndex() == 0)
        {
            role = "0";
        }
        else if(roleComboBox.getSelectionModel().getSelectedIndex() == 1){
            role = "1";
        }
        else{
            alert.setRole();
            return;
        }

        connection.makeQuery("addUser//"+ JSONParser.jsonFromObject(new User(loginField.getText(),passwordField.getText(), role)));
        String response = "";
        response = connection.getResponse();
        if(response.equals("error"))
        {
            alert.userExist();
        }
        else
        {
            refresh();
        }
    }

    @FXML
    void deleteButtonClicked(ActionEvent event) {
        int selectedIndex = usersTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex>=0){
            User obj = usersTable.getSelectionModel().getSelectedItem();
            connection.makeQuery("deleteUser//"+ JSONParser.jsonFromObject(obj));
            String response = "";
            response = connection.getResponse();
            if(response.equals("error"))
            {
                alert.deleteYourself();
            }
            else
            {
                refresh();
            }
        }
        else{
            alert.chooseNote();
        }
    }

    @FXML
    void editButtonClicked(ActionEvent event) {
        int selectedIndex = usersTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex>=0){
            User obj = usersTable.getSelectionModel().getSelectedItem();
            if(loginField.getText().length()!=0)
            {
                obj.setLogin(loginField.getText());
            }
            if(passwordField.getText().length()!=0)
            {
                obj.setPassword(passwordField.getText());
            }
            connection.makeQuery("editUser//"+ JSONParser.jsonFromObject(obj));
            String response = "";
            response = connection.getResponse();
            if(response.equals("error"))
            {
                alert.userExist();
            }
            else
            {
                refresh();
            }
        }
        else{
            alert.chooseNote();
        }
    }



    @FXML
    void searchButtonClicked(ActionEvent event) {
        boolean flag = false;

        if(loginField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(loginField);
            LoginAnim.playAnimation();
            flag = true;
        }

        if(flag){return;}
        connection.makeQuery("searchUser//"+ JSONParser.jsonFromObject(new User(loginField.getText(),null,null)));
        String response = "";
        User user = JSONParser.objectFromJson(connection.getResponse(), User.class);
        if(user.isAdmin().equals("notFound"))
        {
            alert.userNotFound();
        }
        else
        {
            usersTable.getItems().clear();
            if(user.isAdmin().equals("0"))
            {
                user.setAdmin("Пользователь");
            }
            else{
                user.setAdmin("Администратор");
            }
            oblist.add(user);
        }
    }

    @FXML
    void refreshTableButtonClicked(ActionEvent event) {
        refresh();
    }

    @FXML
    void resetSearchButtonClicked(ActionEvent event) {
        refresh();
    }


    @FXML
    void statButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/UserResultWindow.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void exitButtonClicked(ActionEvent event) {
        connection.makeQuery("endSession");
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/SignInWindow.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void initialize(){
        connection = Connection.getInstance();
        alert = AlertWindow.getAlert();
        roleComboBox.getItems().add(0, "Пользователь");
        roleComboBox.getItems().add(1, "Администратор");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        adminColumn.setCellValueFactory(new PropertyValueFactory<>("admin"));
        show();
    }

    public void show(){
        connection.makeQuery("getAllUsers");
        User users[] = JSONParser.objectFromJson(connection.getResponse(), User[].class);
        for(User user: users)
        {
            if(user.isAdmin().equals("0"))
            {
                user.setAdmin("Пользователь");
            }
            else{
                user.setAdmin("Администратор");
            }
            oblist.add(user);
        }

        usersTable.setItems(oblist);
    }

    public void refresh()
    {
        usersTable.getItems().clear();
        show();
        loginField.setText("");
        passwordField.setText("");
    }
}
