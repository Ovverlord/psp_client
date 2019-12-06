package controllers;

import AlertWindow.AlertWindow;
import Main.Main;
import animation.Shake;
import classes.Material;
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

public class ResultWindowController {

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
    private RadioButton costRadio;

    @FXML
    private ToggleGroup radioGroup;

    @FXML
    private RadioButton wageCostRadio;

    @FXML
    private RadioButton materialCostRadio;

    @FXML
    private TextField costField;

    @FXML
    private TextField wageCostField;

    @FXML
    private TextField materialCostField;


    Connection connection;
    AlertWindow alert;
    ObservableList<Result> oblist = FXCollections.observableArrayList();

    @FXML
    void deleteButtonClicked(ActionEvent event) {
        int selectedIndex = resultTable.getSelectionModel().getSelectedIndex();
        if(selectedIndex>=0){
            Result obj = resultTable.getSelectionModel().getSelectedItem();

            connection.makeQuery("deleteResult//"+ JSONParser.jsonFromObject(obj));
            refresh();
        }
        else{
            alert.chooseNote();
        }
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
            connection.makeQuery("searchResultByCost//" + JSONParser.jsonFromObject(
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

            Integer cost = Integer.valueOf(wageCostField.getText().trim());
            connection.makeQuery("searchResultByWageCost//" + JSONParser.jsonFromObject(
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

            Double cost = Double.valueOf(materialCostField.getText().trim());
            connection.makeQuery("searchResultByMaterialCost//" + JSONParser.jsonFromObject(
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
    }

    @FXML
    void resetSearchButtonClicked(ActionEvent event) {
        refresh();
    }


    public void initialize(){
        connection = Connection.getInstance();
        alert = AlertWindow.getAlert();
        costRadio.setSelected(true);
        energyColumn.setCellValueFactory(new PropertyValueFactory<>("finalEnergyCost"));
        gasColumn.setCellValueFactory(new PropertyValueFactory<>("finalGasCost"));
        rentColumn.setCellValueFactory(new PropertyValueFactory<>("finalRentCost"));
        wageColumn.setCellValueFactory(new PropertyValueFactory<>("finalWageCost"));
        materialColumn.setCellValueFactory(new PropertyValueFactory<>("finalMaterialCost"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("finalCost"));
        show();
    }

    public void show()
    {
        connection.makeQuery("getAllResults");
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
        costField.setText("");
        wageCostField.setText("");
        materialCostField.setText("");
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
