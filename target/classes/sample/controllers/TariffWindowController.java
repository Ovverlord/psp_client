package controllers;

import AlertWindow.AlertWindow;
import Main.Main;
import animation.Shake;
import classes.Equipment;
import classes.Tariff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import network.Connection;
import network.JSONParser;

import java.io.IOException;

public class TariffWindowController {

    @FXML
    private TableView<Tariff> tariffTable;

    @FXML
    private TableColumn<Tariff, Double> energyCostColumn;

    @FXML
    private TableColumn<Tariff, Double> gasCostColumn;

    @FXML
    private TableColumn<Tariff, Double> rentCostColumn;

    @FXML
    private Button addButton;

    @FXML
    private TextField energyCostField;

    @FXML
    private TextField gasCostField;

    @FXML
    private TextField rentCostField;

    Connection connection;
    AlertWindow alert;
    ObservableList<Tariff> oblist = FXCollections.observableArrayList();

    @FXML
    void addButtonClicked(ActionEvent event) {
        Double energyCost = 0.0;
        Double gasCost = 0.0;
        Double rentCost = 0.0;
        boolean flag = false;

        if(energyCostField.getText().length()==0)
        {
            Shake PasswordAnim = new Shake(energyCostField);
            PasswordAnim.playAnimation();
            flag = true;
        }
        if(gasCostField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(gasCostField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(rentCostField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(rentCostField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(flag){return;}

        try
        {
            energyCost = Double.valueOf(energyCostField.getText().trim());
            gasCost = Double.valueOf(gasCostField.getText().trim());
            rentCost = Double.valueOf(rentCostField.getText().trim());
            if(energyCost<0 || gasCost<0 || rentCost<0)
            {
                throw new IOException();
            }
        }
        catch (NumberFormatException ex)
        {
            alert.incorrectNumberInput();
            return;
        }
        catch (IOException ex)
        {
            alert.belowZero();
            return;
        }

        connection.makeQuery("addTariff//"+ JSONParser.jsonFromObject(new Tariff(energyCost,gasCost,rentCost)));
        refresh();
    }

    @FXML
    void deleteButtonClicked(ActionEvent event) {
        int selectedIndex = tariffTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex>=0){
            Tariff obj = tariffTable.getSelectionModel().getSelectedItem();

            connection.makeQuery("deleteTariff//"+ JSONParser.jsonFromObject(obj));
            refresh();
        }
        else{
            alert.chooseNote();
        }
    }

    @FXML
    void editButtonClicked(ActionEvent event) {
        int selectedIndex = tariffTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex>=0){
            Tariff obj = tariffTable.getSelectionModel().getSelectedItem();
            try
            {
                if(energyCostField.getText().length()!=0)
                {
                    obj.setEnergyCost(Double.parseDouble(energyCostField.getText()));
                }
                if(gasCostField.getText().length()!=0)
                {
                    obj.setGasCost(Double.parseDouble(gasCostField.getText()));
                }
                if(rentCostField.getText().length()!=0)
                {
                    obj.setRentCost(Double.parseDouble(rentCostField.getText()));
                }
            }
            catch (NumberFormatException ex)
            {
                alert.incorrectNumberInput();
                return;
            }

            connection.makeQuery("editTariff//"+ JSONParser.jsonFromObject(obj));
            refresh();
        }
        else{
            alert.chooseNote();
        }
    }






    public void initialize(){
        connection = Connection.getInstance();
        alert = AlertWindow.getAlert();
        energyCostColumn.setCellValueFactory(new PropertyValueFactory<>("energyCost"));
        gasCostColumn.setCellValueFactory(new PropertyValueFactory<>("gasCost"));
        rentCostColumn.setCellValueFactory(new PropertyValueFactory<>("rentCost"));
        show();
    }


    public void show()
    {
        connection.makeQuery("getTariff");
        Tariff tariffs[] = JSONParser.objectFromJson(connection.getResponse(), Tariff[].class);

        for(Tariff tariff: tariffs)
        {
            oblist.add(tariff);
        }
        if(oblist.size()>0)
        {
            addButton.setDisable(true);
        }
        else{addButton.setDisable(false);}
        tariffTable.setItems(oblist);
    }

    public void refresh()
    {
        tariffTable.getItems().clear();
        show();
        energyCostField.setText("");
        gasCostField.setText("");
        rentCostField.setText("");
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
    void materialsButtonClicked(ActionEvent event) {

    }


    @FXML
    void historyButtonClicked(ActionEvent event) {

    }


    @FXML
    void resultButtonClicked(ActionEvent event) {

    }
}
