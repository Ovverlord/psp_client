package controllers;

import AlertWindow.AlertWindow;
import Main.Main;
import animation.Shake;
import classes.Equipment;
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

public class EquipmentWindowController {


    @FXML
    private TableView<Equipment> equipmentTable;

    @FXML
    private TableColumn<Equipment, String> nameColumn;

    @FXML
    private TableColumn<Equipment, String> modelColumn;

    @FXML
    private TableColumn<Equipment, Integer> hoursWorkedColumn;

    @FXML
    private TableColumn<Equipment, Integer> energyColumn;

    @FXML
    private TableColumn<Equipment, Integer> gasColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField hoursWorkedField;

    @FXML
    private TextField energyField;

    @FXML
    private TextField gasField;

    @FXML
    private RadioButton nameRadio;

    @FXML
    private ToggleGroup radioGroup;

    @FXML
    private RadioButton hoursWorkedRadio;

    Connection connection;
    AlertWindow alert;
    ObservableList<Equipment> oblist = FXCollections.observableArrayList();


    @FXML
    void addButtonClicked(ActionEvent event) {
        Integer hoursWorked=0;
        Integer energy=0;
        Integer gas=0;
        boolean flag = false;

        if(nameField.getText().length()==0)
        {
            Shake PasswordAnim = new Shake(nameField);
            PasswordAnim.playAnimation();
            flag = true;
        }
        if(modelField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(modelField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(hoursWorkedField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(hoursWorkedField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(energyField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(energyField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(gasField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(gasField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(flag){return;}

        try
        {
            hoursWorked = Integer.valueOf(hoursWorkedField.getText().trim());
            energy = Integer.valueOf(energyField.getText().trim());
            gas = Integer.valueOf(gasField.getText().trim());
            if(hoursWorked<0 || energy<0 || gas<0)
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

        connection.makeQuery("addEquipment//"+ JSONParser.jsonFromObject(new Equipment(nameField.getText(),
               modelField.getText(), hoursWorked,energy,gas)));
        refresh();

    }

    @FXML
    void deleteButtonClicked(ActionEvent event) {
        int selectedIndex = equipmentTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex>=0){
            Equipment obj = equipmentTable.getSelectionModel().getSelectedItem();

            connection.makeQuery("deleteEquipment//"+ JSONParser.jsonFromObject(obj));
            refresh();
        }
        else{
            alert.chooseNote();
        }
    }

    @FXML
    void editButtonClicked(ActionEvent event) {
        int selectedIndex = equipmentTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex>=0){
            Equipment obj = equipmentTable.getSelectionModel().getSelectedItem();
            try
            {
                if(nameField.getText().length()!=0)
                {
                    obj.setName(nameField.getText());
                }
                if(modelField.getText().length()!=0)
                {
                    obj.setModel(modelField.getText());
                }
                if(hoursWorkedField.getText().length()!=0)
                {
                    obj.setHoursWorked(Integer.valueOf(hoursWorkedField.getText()));
                }
                if(energyField.getText().length()!=0)
                {
                    obj.setEnergy(Integer.valueOf(energyField.getText()));
                }
                if(gasField.getText().length()!=0)
                {
                    obj.setGas(Integer.valueOf(gasField.getText()));
                }
            }
            catch (NumberFormatException ex)
            {
                alert.notInteger();
                return;
            }

            connection.makeQuery("editEquipment//"+ JSONParser.jsonFromObject(obj));
            refresh();
        }
        else{
            alert.chooseNote();
        }
    }

    @FXML
    void searchButtonClicked(ActionEvent event) {
        if(nameRadio.isSelected())
        {
            boolean flag = false;

            if(nameField.getText().length()==0)
            {
                Shake LoginAnim = new Shake(nameField);
                LoginAnim.playAnimation();
                flag = true;
            }
            if(flag){return;}

            connection.makeQuery("searchEquipmentByName//"+ JSONParser.jsonFromObject(
                    new Equipment(nameField.getText(),null,null,null,null)));
            Equipment equipment[] = JSONParser.objectFromJson(connection.getResponse(), Equipment[].class);
            if(equipment.length==0)
            {
                alert.equipmentNotFound();
            }
            else
            {
                equipmentTable.getItems().clear();
                for(Equipment equipment1: equipment)
                {
                    oblist.add(equipment1);
                }
                equipmentTable.setItems(oblist);
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

            connection.makeQuery("searchEquipmentByHoursWorked//"+ JSONParser.jsonFromObject(
                    new Equipment(null,null,hoursWorked,null,null)));
            Equipment equipment[] = JSONParser.objectFromJson(connection.getResponse(), Equipment[].class);
            if(equipment.length==0)
            {
                alert.equipmentNotFound();
            }
            else
            {
                equipmentTable.getItems().clear();
                for(Equipment equipment1 : equipment)
                {
                    oblist.add(equipment1);
                }
                equipmentTable.setItems(oblist);
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
        nameRadio.setSelected(true);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        hoursWorkedColumn.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));
        energyColumn.setCellValueFactory(new PropertyValueFactory<>("energy"));
        gasColumn.setCellValueFactory(new PropertyValueFactory<>("gas"));
        show();
    }

    public void show()
    {
        connection.makeQuery("getAllEquipment");
        Equipment equipment[] = JSONParser.objectFromJson(connection.getResponse(), Equipment[].class);

        for(Equipment equipment1: equipment)
        {
            oblist.add(equipment1);
        }

        equipmentTable.setItems(oblist);
    }

    public void refresh()
    {
        equipmentTable.getItems().clear();
        show();
        nameField.setText("");
        modelField.setText("");
        hoursWorkedField.setText("");
        energyField.setText("");
        gasField.setText("");
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
        System.out.println("fix");
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
        System.out.println("history");
    }

    @FXML
    void resultButtonClicked(ActionEvent event) {
        System.out.println("result");
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






