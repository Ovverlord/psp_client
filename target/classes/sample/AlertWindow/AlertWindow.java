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

    public void chooseNote()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Укажите запись");
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

    public void setRole()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Укажите роль пользователя");
        alert.showAndWait();
    }

    public void belowZero()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText("Значения не могут быть отрицательные");
        alert.showAndWait();
    }


    public void notInteger()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText("Введите целочисленные значения");
        alert.showAndWait();
    }

    public void equipmentNotFound()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Оборудование не найдено");
        alert.showAndWait();
    }

    public void workerNotFound()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Работники не найдены");
        alert.showAndWait();
    }

    public void incorrectNumberInput()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText("Введите числовые значения");
        alert.showAndWait();
    }

    public void materialNotFound()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.setContentText("Материал не найден");
        alert.showAndWait();
    }
}
