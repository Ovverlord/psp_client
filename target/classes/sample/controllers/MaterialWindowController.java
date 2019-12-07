package controllers;

import AlertWindow.AlertWindow;
import Main.Main;
import animation.Shake;
import classes.Material;
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

public class MaterialWindowController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField unitCostField;

    @FXML
    private TextField usedAmountField;

    @FXML
    private TableView<Material> materialTable;

    @FXML
    private TableColumn<Material,String> nameColumn;

    @FXML
    private TableColumn<Material,Double> unitCostColumn;

    @FXML
    private TableColumn<Material,Double> usedAmountColumn;

    @FXML
    private RadioButton nameRadio;

    @FXML
    private ToggleGroup radioGroup;

    @FXML
    private RadioButton unitCostRadio;

    @FXML
    private RadioButton usedAmountRadio;

    Connection connection;
    AlertWindow alert;
    ObservableList<Material> oblist = FXCollections.observableArrayList();

    @FXML
    void addButtonClicked(ActionEvent event) {
        Double unitCost = 0.0;
        Double usedAmount = 0.0;
        boolean flag = false;

        if(nameField.getText().length()==0)
        {
            Shake PasswordAnim = new Shake(nameField);
            PasswordAnim.playAnimation();
            flag = true;
        }
        if(unitCostField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(unitCostField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(usedAmountField.getText().length()==0)
        {
            Shake LoginAnim = new Shake(usedAmountField);
            LoginAnim.playAnimation();
            flag = true;
        }
        if(flag){return;}

        try
        {
            unitCost = Double.valueOf(unitCostField.getText().trim());
            usedAmount = Double.valueOf(usedAmountField.getText().trim());
            if(unitCost<0 || usedAmount<0 )
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
        connection.makeQuery("addMaterial//"+ JSONParser.jsonFromObject(new Material(nameField.getText(),unitCost,usedAmount)));
        refresh();
    }

    @FXML
    void deleteButtonClicked(ActionEvent event) {
        int selectedIndex = materialTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex>=0){
            Material obj = materialTable.getSelectionModel().getSelectedItem();

            connection.makeQuery("deleteMaterial//"+ JSONParser.jsonFromObject(obj));
            refresh();
        }
        else{
            alert.chooseNote();
        }
    }

    @FXML
    void editButtonClicked(ActionEvent event) {
        int selectedIndex = materialTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex>=0){
            Material obj = materialTable.getSelectionModel().getSelectedItem();
            try
            {
                if(nameField.getText().length()!=0)
                {
                    obj.setName(nameField.getText());
                }
                if(unitCostField.getText().length()!=0)
                {
                    obj.setUnitcost(Double.valueOf(unitCostField.getText()));
                }
                if(usedAmountField.getText().length()!=0)
                {
                    obj.setUsedamount(Double.parseDouble(usedAmountField.getText()));
                }
            }
            catch (NumberFormatException ex)
            {
                alert.incorrectNumberInput();
                return;
            }

            connection.makeQuery("editMaterial//"+ JSONParser.jsonFromObject(obj));
            refresh();
        }
        else{
            alert.chooseNote();
        }
    }


    @FXML
    void searchButtonClicked(ActionEvent event) {
        if (nameRadio.isSelected()) {
            boolean flag = false;

            if (nameField.getText().length() == 0) {
                Shake LoginAnim = new Shake(nameField);
                LoginAnim.playAnimation();
                flag = true;
            }
            if (flag) {
                return;
            }

            connection.makeQuery("searchMaterialByName//" + JSONParser.jsonFromObject(
                    new Material(nameField.getText(), null, null)));
            Material materials[] = JSONParser.objectFromJson(connection.getResponse(), Material[].class);
            if (materials.length == 0) {
                alert.materialNotFound();
            } else {
                materialTable.getItems().clear();
                for (Material material : materials) {
                    oblist.add(material);
                }
                materialTable.setItems(oblist);
            }

        }
        else if (unitCostRadio.isSelected()) {
            boolean flag = false;
            Double unitCost = 0.0;
            if (unitCostField.getText().length() == 0) {
                Shake LoginAnim = new Shake(unitCostField);
                LoginAnim.playAnimation();
                flag = true;
            }
            if (flag) {
                return;
            }
            try
            {
                unitCost = Double.valueOf(unitCostField.getText().trim());
            }
            catch (NumberFormatException ex)
            {
                alert.incorrectNumberInput();
                return;
            }

            connection.makeQuery("searchMaterialByUnitCost//" + JSONParser.jsonFromObject(
                    new Material(null, unitCost, null)));
            Material materials[] = JSONParser.objectFromJson(connection.getResponse(), Material[].class);
            if (materials.length == 0) {
                alert.materialNotFound();
            } else {
                materialTable.getItems().clear();
                for (Material material : materials) {
                    oblist.add(material);
                }
                materialTable.setItems(oblist);
            }

        }

        else if (usedAmountRadio.isSelected()) {
            boolean flag = false;
            Double usedAmount = 0.0;
            if (usedAmountField.getText().length() == 0) {
                Shake LoginAnim = new Shake(usedAmountField);
                LoginAnim.playAnimation();
                flag = true;
            }
            if (flag) {
                return;
            }

            try
            {
                usedAmount = Double.valueOf(usedAmountField.getText().trim());
            }
            catch (NumberFormatException ex)
            {
                alert.incorrectNumberInput();
                return;
            }
            connection.makeQuery("searchMaterialByUnitCost//" + JSONParser.jsonFromObject(
                    new Material(null, null, usedAmount)));
            Material materials[] = JSONParser.objectFromJson(connection.getResponse(), Material[].class);
            if (materials.length == 0) {
                alert.materialNotFound();
            } else {
                materialTable.getItems().clear();
                for (Material material : materials) {
                    oblist.add(material);
                }
                materialTable.setItems(oblist);
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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        unitCostColumn.setCellValueFactory(new PropertyValueFactory<>("unitcost"));
        usedAmountColumn.setCellValueFactory(new PropertyValueFactory<>("usedamount"));
        nameRadio.setSelected(true);
        show();
    }

    public void show()
    {
        connection.makeQuery("getAllMaterials");
        Material materials[] = JSONParser.objectFromJson(connection.getResponse(), Material[].class);
        for(Material material: materials)
        {
            oblist.add(material);
        }

        materialTable.setItems(oblist);
    }

    public void refresh()
    {
        materialTable.getItems().clear();
        show();
        nameField.setText("");
        unitCostField.setText("");
        usedAmountField.setText("");
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

        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/ResultWindow.fxml")));
            stage.setScene(scene);
            stage.show();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void resultButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/CalculationWindow.fxml")));
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

}
