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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import network.Connection;
import network.JSONParser;

import java.io.IOException;

public class CalculationWindowController {

    @FXML
    private TextField finalEnergyCostField;

    @FXML
    private TextField finalGasCostField;

    @FXML
    private TextField finalWageCostField;

    @FXML
    private TextField finalMaterialCostField;

    @FXML
    private TextField finalRentCostField;

    @FXML
    private TextField finalCostField;

    @FXML
    private Button saveResultButtonClicked;

    @FXML
    private Button genereteReportButtonClicked;

    @FXML
    private Button diagrammButtonClicked;

    @FXML
    private Pane diagrammPane;

    Connection connection;
    AlertWindow alert;
    Result result;
    ObservableList<PieChart.Data> oblist = FXCollections.observableArrayList();

    @FXML
    void saveResultButtonClicked(ActionEvent event) {
        connection.makeQuery("saveResult//"+ JSONParser.jsonFromObject(result));
        alert.saveResult();
    }

    @FXML
    void generateReportButtonClicked(ActionEvent event) {

    }

    public void initialize(){
        connection = Connection.getInstance();
        alert = AlertWindow.getAlert();
        show();
    }

    public void show()
    {
        connection.makeQuery("calculateCost//" + JSONParser.jsonFromObject(
                new Result(null, null, null,
                        null,null,null,null)));
        result = JSONParser.objectFromJson(connection.getResponse(), Result.class);

        finalEnergyCostField.setText(String.valueOf(result.getFinalEnergyCost()));
        finalGasCostField.setText(String.valueOf(result.getFinalGasCost()));
        finalWageCostField.setText(String.valueOf(result.getFinalWageCost()));
        finalMaterialCostField.setText(String.valueOf(result.getFinalMaterialCost()));
        finalRentCostField.setText(String.valueOf(result.getFinalRentCost()));
        finalCostField.setText(String.valueOf(result.getFinalCost()));

        diagrammPane.getChildren().clear();
        oblist.add(new PieChart.Data("Затраты на энергию",result.getFinalEnergyCost()));
        oblist.add(new PieChart.Data("Затраты на газ",result.getFinalGasCost()));
        oblist.add(new PieChart.Data("Затраты на зарплаты",result.getFinalWageCost()));
        oblist.add(new PieChart.Data("Затраты на материалы",result.getFinalMaterialCost()));
        oblist.add(new PieChart.Data("Затраты на аренду",result.getFinalRentCost()));
        PieChart diagramChart = new PieChart(oblist);
        diagramChart.setTitle("Диаграмма долей затрат");
        diagrammPane.getChildren().add(diagramChart);

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
