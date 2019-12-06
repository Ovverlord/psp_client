package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import network.Connection;

import java.io.IOException;

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
        mainStage = stage;
        Connection obj = Connection.getInstance();
        try
        {
            obj.getClientSocket().isConnected();

            setMainScene(stage);
        }
        catch (NullPointerException ex)
        {
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/ErrorConnectionWindow.fxml")));
            mainStage.setTitle("Ошибка");
            mainStage.setScene(scene);
            mainStage.getIcons().add(new Image("@../../recources/images/ServerError.png"));
            mainStage.setResizable(false);
            mainStage.show();
        }
    }

    public void setMainScene(Stage stage) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../sample/forms/SignInWindow.fxml")));
        mainStage.setTitle("Себестоимость");
        mainStage.setScene(scene);
        mainStage.getIcons().add(new Image("@../../recources/images/Cost.png"));
        mainStage.setResizable(false);
        mainStage.show();

        mainStage.setOnCloseRequest(windowEvent ->
        {
            try
            {
                Connection serverSocket = Connection.getInstance().close();
            }
            catch (Exception ex)
            {
                System.out.println(ex);
            }
        });
    }

}
