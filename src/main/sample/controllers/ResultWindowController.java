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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    @FXML
    private Pane graphicPane;


    Connection connection;
    AlertWindow alert;
    ObservableList<Result> oblist = FXCollections.observableArrayList();
    ObservableList<LineChart.Data> graphicList = FXCollections.observableArrayList();

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


    @FXML
    void graphicButtonClicked(ActionEvent event) {
        Stage stage = new Stage();
        HBox root = new HBox();
        Scene scene = new Scene(root,500,500);
        stage.getIcons().add(new Image("@../../recources/images/Graphic.png"));
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        series.setName("Изменение себестоимости выпуска");
        int i =0;
        while(i<oblist.size())
        {
            Result result = oblist.get(i);
            series.getData().add(new XYChart.Data(i,result.getFinalCost()));
            i++;
        }
        lineChart.getData().add(series);
        root.getChildren().add(lineChart);
        stage.setScene(scene);
        stage.show();
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


//        graphicPane.getChildren().clear();
//
//        graphicPane.getChildren().add(lineChart);
//        LineChart<Double,Integer> lineChart = null;
//        XYChart.Series<Double,Integer> graphic = new XYChart.Series<Double, Integer>();
//        for(int i=0;i<oblist.size();i++)
//        {
//            Double cost = Double.valueOf(costColumn.getText());
//        graphic.getData().add(new XYChart.Data<Double, Integer>(cost,i));
//        }
//
//        lineChart.getData().addAll();

//        diagrammPane.getChildren().clear();
//        oblist.add(new PieChart.Data("Затраты на энергию",result.getFinalEnergyCost()));
//        oblist.add(new PieChart.Data("Затраты на газ",result.getFinalGasCost()));
//        oblist.add(new PieChart.Data("Затраты на зарплаты",result.getFinalWageCost()));
//        oblist.add(new PieChart.Data("Затраты на материалы",result.getFinalMaterialCost()));
//        oblist.add(new PieChart.Data("Затраты на аренду",result.getFinalRentCost()));
//        PieChart diagramChart = new PieChart(oblist);
//        diagramChart.setTitle("Диаграмма долей затрат");
//        diagrammPane.getChildren().add(diagramChart);
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
