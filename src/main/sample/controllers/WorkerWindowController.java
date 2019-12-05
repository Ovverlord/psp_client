package controllers;

import AlertWindow.AlertWindow;
import Main.Main;
import animation.Shake;
import classes.Equipment;
import classes.Worker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import network.Connection;
import network.JSONParser;

import java.io.IOException;

public class WorkerWindowController {

    @FXML
    private TableView<Worker> workerTable;

    @FXML
    private TableColumn<Worker, String> surnameColumn;

    @FXML
    private TableColumn<Worker, String> nameColumn;

    @FXML
    private TableColumn<Worker, String> lastnameColumn;

    @FXML
    private TableColumn<Worker, String> positionColumn;

    @FXML
    private TableColumn<Worker, Integer> wageColumn;

    @FXML
    private TableColumn<Worker,Integer> hoursWorkedColumn;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField lastnameField;

    @FXML
    private TextField positionField;

    @FXML
    private TextField wageField;

    @FXML
    private TextField hoursWorkedField;

    @FXML
    private RadioButton surnameRadio;

    @FXML
    private RadioButton positionRadio;

    @FXML
    private RadioButton hoursWorkedRadio;

    @FXML
    private ToggleGroup radioGroup;



    Connection connection;
    AlertWindow alert;
    ObservableList<Worker> oblist = FXCollections.observableArrayList();

    @FXML
    void addButtonClicked(ActionEvent event) {
        Integer hoursWorked=0;
        Integer wage=0;
        boolean flag = false;
        if(surnameField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(surnameField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(nameField.getText().length()==0)
        {
            Shake PasswordAnim = new Shake(nameField);
            PasswordAnim.playAnimation();
            flag = true;
        }
        if(lastnameField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(lastnameField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(positionField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(positionField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(wageField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(wageField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(hoursWorkedField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(hoursWorkedField);
            LoginAnim.playAnimation();
            flag = true;
        }

        if(flag){return;}

        try
        {
            hoursWorked = Integer.valueOf(hoursWorkedField.getText().trim());
            wage = Integer.valueOf(wageField.getText().trim());
            if(hoursWorked<0 || wage<0)
            {
                throw new IOException();
            }
        }
        catch (NumberFormatException ex)
        {
            alert.notInteger();
            return;
        }
        catch (IOException ex)
        {
            alert.belowZero();
            return;
        }

        connection.makeQuery("addWorker//"+ JSONParser.jsonFromObject(new Worker(surnameField.getText(),
                nameField.getText(),
                lastnameField.getText(),
                positionField.getText(),
                wage,hoursWorked)));
        refresh();
    }

    @FXML
    void deleteButtonClicked(ActionEvent event) {
        int selectedIndex = workerTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex>=0){
            Worker obj = workerTable.getSelectionModel().getSelectedItem();

            connection.makeQuery("deleteWorker//"+ JSONParser.jsonFromObject(obj));
            refresh();
        }
        else{
            alert.chooseNote();
        }
    }

    @FXML
    void editButtonClicked(ActionEvent event) {
        int selectedIndex = workerTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex>=0){
            Worker obj = workerTable.getSelectionModel().getSelectedItem();
            try
            {
                if(surnameField.getText().length()!=0)
                {
                    obj.setSurname(surnameField.getText());
                }
                if(nameField.getText().length()!=0)
                {
                    obj.setName(nameField.getText());
                }
                if(lastnameField.getText().length()!=0)
                {
                    obj.setLastname(lastnameField.getText());
                }
                if(positionField.getText().length()!=0)
                {
                    obj.setPosition(positionField.getText());
                }
                if(wageField.getText().length()!=0)
                {
                    obj.setWage(Integer.valueOf(wageField.getText()));
                }
                if(hoursWorkedField.getText().length()!=0)
                {
                    obj.setHoursworked(Integer.valueOf(hoursWorkedField.getText()));
                }
            }
            catch (NumberFormatException ex)
            {
                alert.notInteger();
                return;
            }

            connection.makeQuery("editWorker//"+ JSONParser.jsonFromObject(obj));
            refresh();
        }
        else{
            alert.chooseNote();
        }
    }

    @FXML
    void searchButtonClicked(ActionEvent event) {
        if(surnameRadio.isSelected())
        {
            boolean flag = false;

            if(surnameField.getText().length()==0)
            {
                Shake LoginAnim = new Shake(surnameField);
                LoginAnim.playAnimation();
                flag = true;
            }
            if(flag){return;}

            connection.makeQuery("searchWorkerBySurname//"+ JSONParser.jsonFromObject(
                    new Worker(surnameField.getText(),null,null,null,null,null)));
            Worker workers[] = JSONParser.objectFromJson(connection.getResponse(), Worker[].class);
            if(workers.length==0)
            {
                alert.workerNotFound();
            }
            else
            {
                workerTable.getItems().clear();
                for(Worker worker: workers)
                {
                    oblist.add(worker);
                }
                workerTable.setItems(oblist);
            }

        }
        else if(hoursWorkedRadio.isSelected())
        {
            boolean flag = false;
            Integer hoursWorked;
            if(hoursWorkedField.getText().length()==0)
            {
                Shake LoginAnim = new Shake(hoursWorkedField);
                LoginAnim.playAnimation();
                flag = true;
            }
            if(flag){return;}
            try
            {
                hoursWorked= Integer.valueOf(hoursWorkedField.getText().trim());
            }
            catch (NumberFormatException ex)
            {
                alert.notInteger();
                return;
            }

            connection.makeQuery("searchWorkerByHoursWorked//"+ JSONParser.jsonFromObject(
                    new Worker(null,null,null,null,null,hoursWorked)));
            Worker workers[] = JSONParser.objectFromJson(connection.getResponse(), Worker[].class);
            if(workers.length==0)
            {
                alert.workerNotFound();
            }
            else
            {
                workerTable.getItems().clear();
                for(Worker worker: workers)
                {
                    oblist.add(worker);
                }
                workerTable.setItems(oblist);
            }
        }
        else if(positionRadio.isSelected())
        {
            boolean flag = false;

            if(positionField.getText().length()==0)
            {
                Shake LoginAnim = new Shake(positionField);
                LoginAnim.playAnimation();
                flag = true;
            }
            if(flag){return;}

            connection.makeQuery("searchWorkerByPosition//"+ JSONParser.jsonFromObject(
                    new Worker(null,null,null,positionField.getText(),null,null)));
            Worker workers[] = JSONParser.objectFromJson(connection.getResponse(), Worker[].class);
            if(workers.length==0)
            {
                alert.workerNotFound();
            }
            else
            {
                workerTable.getItems().clear();
                for(Worker worker: workers)
                {
                    oblist.add(worker);
                }
                workerTable.setItems(oblist);
            }

        }
    }

    @FXML
    void resetSearchButtonClicked(ActionEvent event) {
        refresh();
    }

    public void initialize(){
        connection = Connection.getInstance();
        alert = AlertWindow.getAlert();
        surnameRadio.setSelected(true);
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        wageColumn.setCellValueFactory(new PropertyValueFactory<>("wage"));
        hoursWorkedColumn.setCellValueFactory(new PropertyValueFactory<>("hoursworked"));
        show();
    }

    public void show()
    {
        connection.makeQuery("getAllWorkers");
        Worker workers[] = JSONParser.objectFromJson(connection.getResponse(), Worker[].class);
        for(Worker worker: workers)
        {
            oblist.add(worker);
        }

        workerTable.setItems(oblist);
    }

    public void refresh()
    {
        workerTable.getItems().clear();
        show();
        surnameField.setText("");
        nameField.setText("");
        lastnameField.setText("");
        positionField.setText("");
        wageField.setText("");
        hoursWorkedField.setText("");
    }









    @FXML
    void equipmentButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/EquipmentWindow.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void workerButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/WorkerWindow.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void materialsButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/MaterialWindow.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void tariffButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/TariffWindow.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void historyButtonClicked(ActionEvent event) {

    }



    @FXML
    void resultButtonClicked(ActionEvent event) {

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
}
