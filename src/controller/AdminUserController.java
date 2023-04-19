package controller;

import java.io.IOException;
import java.util.*;
import application.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import model.AdminUser;


/**
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * This class controls Admin user features
 */
public class AdminUserController implements LogoutController
{
    @FXML
    public ListView<String> listview;
    @FXML
    public Button mCreate, mDelete, mLogOff;
    @FXML
    public TextField tfUsername;
    /**
     * A list of non-admin users in the form of an arraylist
     */
    public static ArrayList<String> listOfUsers = new ArrayList<>();
    /**
     * A list in the form of observable list
     */
    public ObservableList<String> obsList;
    /**
     * adminUser is an instance that helps control the flow of users
     */
    public static AdminUser adminUser = Photos.driver;
    /**
     *
     */
    public void start()
    {
        update();
        if(!listOfUsers.isEmpty())
        {
            listview.getSelectionModel().select(0);
        }
    }
    /**
     * @param e
     * @throws IOException
     *
     * Logs the user out
     */
    public void logOut(ActionEvent e) throws IOException
    {
        logOutUser(e);
    }
    /**
     * @param e
     * @throws IOException
     *
     * Adds a user to the user list
     */
    public void addUser(ActionEvent e) throws IOException
    {
        String username = tfUsername.getText().trim();

        if(username.isEmpty() || username == null)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Admin Username Error");
            alert.setContentText("Make sure you enter username in the text box");
            alert.showAndWait();
            return;
        }
        else if(adminUser.doesUserExist(username))
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Admin Username Error");
            alert.setContentText("Duplicate username entered. Provide a new unique username");
            alert.showAndWait();
            return;
        }
        else if (username.equals("admin"))
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Admin Username Error");
            alert.setContentText("You cannot use 'admin' as a username. Provide a new unique username");
            alert.showAndWait();
            return;
        }
        else
        {
            adminUser.addUser(username);
            update();
            tfUsername.clear();
        }
        AdminUser.saveToDat(adminUser);
    }
    /**
     * @param e
     * @throws IOException
     *
     *  Removes a user from the user list
     */
    public void deleteUser(ActionEvent e) throws IOException
    {
        int index = listview.getSelectionModel().getSelectedIndex();
        int adminList = listOfUsers.indexOf("Admin");

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Deleting this user, you sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {

            adminUser.deleteUserByIndex(index);
            update();
            AdminUser.saveToDat(adminUser);

            if(adminUser.getNonAdminUsers().size() == 1)
            {
                mDelete.setVisible(false);
            }
            else
            {
                int indexOfLastUser = adminUser.getNonAdminUsers().size();

                if(adminUser.getNonAdminUsers().size() == 1)
                {
                    listview.getSelectionModel().select(0);
                }
                else if(index == indexOfLastUser)
                {
                    listview.getSelectionModel().select(indexOfLastUser-1);
                }
                else
                {
                    listview.getSelectionModel().select(index);
                }
            }

        }
        else
        {
            return;
        }

        return;
    }
    /**
     * Updates the listview whenever admin adds non-admin user or deletes a non-admin user
     */
    public void update()
    {
        listOfUsers.clear();
        for (int i = 0; i < adminUser.getNonAdminUsers().size(); i++)
        {
            listOfUsers.add(adminUser.getNonAdminUsers().get(i).getUserName());
        }

        listview.refresh();
        obsList = FXCollections.observableArrayList(listOfUsers);
        listview.setItems(obsList);
        listview.refresh();
    }
}