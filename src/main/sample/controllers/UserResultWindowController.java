package controllers;

import AlertWindow.AlertWindow;
import Main.Main;
import animation.Shake;
import classes.Result;
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

public class UserResultWindowController {

    @FXML
    private TableView<Result> resultTable;

    @FXML
    private TableColumn<Result,Double> energyColumn;

    @FXML
    private TableColumn<Result,Double> gasColumn;

    @FXML
    private TableColumn<Result,Double> rentColumn;

    @FXML
    private TableColumn<Result,Integer> wageColumn;

    @FXML
    private TableColumn<Result,Double> materialColumn;

    @FXML
    private TableColumn<Result,Double> costColumn;

    @FXML
    private TableColumn<Result, String> userColumn;

    @FXML
    private RadioButton costRadio;

    @FXML
    private ToggleGroup radioGroup;

    @FXML
    private RadioButton wageCostRadio;

    @FXML
    private RadioButton materialCostRadio;

    @FXML
    private RadioButton loginRadio;

    @FXML
    private TextField costField;

    @FXML
    private TextField wageCostField;

    @FXML
    private TextField materialCostField;

    @FXML
    private TextField loginField;

    Connection connection;
    AlertWindow alert;
    ObservableList<Result> oblist = FXCollections.observableArrayList();



    @FXML
    void refreshTableButtonClicked(ActionEvent event) {
        refresh();
    }

    @FXML
    void resetSearchButtonClicked(ActionEvent event) {
        refresh();
    }

    @FXML
    void searchButtonClicked(ActionEvent event) {
        if (costRadio.isSelected()) {
            boolean flag = false;
            if (costField.getText().length() == 0) {
                Shake LoginAnim = new Shake(costField);
                LoginAnim.playAnimation();
                flag = true;
            }
            if (flag) {
                return;
            }

            Double cost = Double.valueOf(costField.getText().trim());
            connection.makeQuery("searchResultUserByCost//" + JSONParser.jsonFromObject(
                    new Result(null,null,null,
                            null,null,cost)));
            Result results[] = JSONParser.objectFromJson(connection.getResponse(), Result[].class);
            if (results.length == 0) {
                alert.resultsNotFound();
            }
            else {
                resultTable.getItems().clear();
                for (Result result : results) {
                    oblist.add(result);
                }
                resultTable.setItems(oblist);
            }
        }
        else if (wageCostRadio.isSelected()) {
            boolean flag = false;
            if (wageCostField.getText().length() == 0) {
                Shake LoginAnim = new Shake(wageCostField);
                LoginAnim.playAnimation();
                flag = true;
            }
            if (flag) {
                return;
            }

            Integer cost;
            try
            {
                cost = Integer.valueOf(wageCostField.getText().trim());
            }
            catch (NumberFormatException ex)
            {
                alert.notInteger();
                return;
            }
            connection.makeQuery("searchResultUserByWageCost//" + JSONParser.jsonFromObject(
                    new Result(null,null,null,
                            cost,null,null)));
            Result results[] = JSONParser.objectFromJson(connection.getResponse(), Result[].class);
            if (results.length == 0) {
                alert.resultsNotFound();
            }
            else {
                resultTable.getItems().clear();
                for (Result result : results) {
                    oblist.add(result);
                }
                resultTable.setItems(oblist);
            }
        }

        else if (materialCostRadio.isSelected()) {
            boolean flag = false;
            if (materialCostField.getText().length() == 0) {
                Shake LoginAnim = new Shake(materialCostField);
                LoginAnim.playAnimation();
                flag = true;
            }
            if (flag) {
                return;
            }

            Double cost;
            try
            {
                cost = Double.valueOf(materialCostField.getText().trim());
            }
            catch (NumberFormatException ex)
            {
                alert.incorrectNumberInput();
                return;
            }
            connection.makeQuery("searchResultUserByMaterialCost//" + JSONParser.jsonFromObject(
                    new Result(null,null,null,
                            null,cost,null)));
            Result results[] = JSONParser.objectFromJson(connection.getResponse(), Result[].class);
            if (results.length == 0) {
                alert.resultsNotFound();
            }
            else {
                resultTable.getItems().clear();
                for (Result result : results) {
                    oblist.add(result);
                }
                resultTable.setItems(oblist);
            }
        }

        else if(loginRadio.isSelected())
        {
            boolean flag = false;
            if (loginField.getText().length() == 0) {
                Shake LoginAnim = new Shake(loginField);
                LoginAnim.playAnimation();
                flag = true;
            }
            if (flag) {
                return;
            }


            connection.makeQuery("searchResultUserByUser//" + JSONParser.jsonFromObject(
                    new Result(null,null,null,
                            null,null,null,loginField.getText(),null)));
            Result results[] = JSONParser.objectFromJson(connection.getResponse(), Result[].class);
            if (results.length == 0) {
                alert.resultsNotFound();
            }
            else {
                resultTable.getItems().clear();
                for (Result result : results) {
                    oblist.add(result);
                }
                resultTable.setItems(oblist);
            }
        }
    }



    public void initialize(){
        connection = Connection.getInstance();
        alert = AlertWindow.getAlert();
        loginRadio.setSelected(true);
        energyColumn.setCellValueFactory(new PropertyValueFactory<>("finalEnergyCost"));
        gasColumn.setCellValueFactory(new PropertyValueFactory<>("finalGasCost"));
        rentColumn.setCellValueFactory(new PropertyValueFactory<>("finalRentCost"));
        wageColumn.setCellValueFactory(new PropertyValueFactory<>("finalWageCost"));
        materialColumn.setCellValueFactory(new PropertyValueFactory<>("finalMaterialCost"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("finalCost"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        show();
    }

    public void show()
    {
        connection.makeQuery("getAllUsersResults");
        Result results[] = JSONParser.objectFromJson(connection.getResponse(), Result[].class);
        for(Result result: results)
        {
            oblist.add(result);
        }
        resultTable.setItems(oblist);
    }

    public void refresh()
    {
        resultTable.getItems().clear();
        show();
        loginField.setText("");
        costField.setText("");
        wageCostField.setText("");
        materialCostField.setText("");
    }



    @FXML
    void userButtonClicked(ActionEvent event) {
        try {
            Stage stage = Main.getPrimaryStage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/AdminWindow.fxml")));
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
