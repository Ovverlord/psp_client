package AlertWindow;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;

public class AlertWindow {
    private static AlertWindow alert;
    AlertWindow() {
    }

    public static AlertWindow getAlert() {
        if (alert == null) {
            alert = new AlertWindow();
        }
        return alert;
    }

    public void incorrectLoginOrPassword()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Неверный логин или пароль");
        alert.showAndWait();
    }

    public void userExist()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Данный пользователь уже существует");
        alert.showAndWait();
    }

    public void chooseUser()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Укажите пользователя");
        alert.showAndWait();
    }

    public void deleteYourself()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Невозможно удалить самого себя");
        alert.showAndWait();
    }

    public void userNotFound()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Пользователь не найден");
        alert.showAndWait();
    }
}
