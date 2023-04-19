package controller;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import application.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.AdminUser;
import model.NonAdminUser;

/**
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 */
public class LoginController
{
    @FXML
    public Button mLogIn, mExitBtn;
    @FXML
    public TextField tfUsername;
    /**
     * string variable that helps confirm if username is admin
     */
    public final String admin = "admin";
    /**
     * adminUser is an instance that helps control the flow of users
     */
    public static AdminUser driver = Photos.driver;
    /**
     * @param e
     * @throws IOException
     *
     * this login method helps determine if username is "admin" or a non-admin username, also checks if username is inputted correctly
     */
    public void login(ActionEvent e) throws IOException {

        String username = tfUsername.getText().trim().toLowerCase();

        if (username.equals(admin))
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AdminUser.fxml"));
            Parent sceneManager = (Parent) fxmlLoader.load();
            AdminUserController adminController = fxmlLoader.getController();
            Scene adminScene = new Scene(sceneManager);
            Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            adminController.start();
            appStage.setScene(adminScene);
            appStage.show();
        }
        else if (driver.checkUserExistsInAdminSubsystem(username))
        {
            NonAdminUser currentUser = driver.getCurrentUser();
            NonAdminUserController.username = username;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NonAdminUser.fxml"));
            Parent sceneManager = (Parent) fxmlLoader.load();
            NonAdminUserController userController = fxmlLoader.getController();
            Scene userScene = new Scene(sceneManager);
            Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            userController.start(appStage);
            appStage.setScene(userScene);
            appStage.show();
        }
        else if (username.isEmpty() || username == null)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Error Encountered");
            alert.setHeaderText("Username text box empty, go back and enter a username");
            Optional<ButtonType> buttonClicked=alert.showAndWait();
            if (buttonClicked.get()==ButtonType.OK)
            {
                alert.close();
            }
            else
            {
                alert.close();
            }
        }
        else
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Error Encountered");
            alert.setHeaderText("Something went wrong!");
            Optional<ButtonType> buttonClicked=alert.showAndWait();
            if (buttonClicked.get()==ButtonType.OK)
            {
                alert.close();
            }
            else
            {
                alert.close();
            }
        }
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * This method helps provide a safe exit for a non-admin user if needed
     */
    public void exitProgram(ActionEvent e) throws IOException
    {
        System.exit(0);
    }
}