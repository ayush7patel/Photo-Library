package controller;

import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * Interface class that helps non-admin users logout at any point in the program - used by every other class
 */
public interface LogoutController
{
    default void logOutUser(ActionEvent e) throws IOException
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText(null);
        alert.setContentText("Logging out of your account, confirm?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent sceneManager = (Parent) fxmlLoader.load();
            Scene adminScene = new Scene(sceneManager);
            Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            appStage.setScene(adminScene);
            appStage.show();
        }
        else
        {
            return;
        }
    }
}
