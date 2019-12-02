package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import network.Connection;

public class Main extends Application {

    public static Stage mainStage;


    private void setPrimaryStage(Stage stage) {
        Main.mainStage = stage;
    }

    static public Stage getPrimaryStage() {
        return Main.mainStage;
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        Connection obj = Connection.getInstance();
        mainStage = stage;
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/SignInWindow.fxml")));
        mainStage.setTitle("Себестоимость");
        mainStage.setScene(scene);
        mainStage.getIcons().add(new Image("resources/images/Cost.png"));
        mainStage.setResizable(false);
        mainStage.show();
    }
}
