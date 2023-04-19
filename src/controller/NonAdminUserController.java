package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import application.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import model.AdminUser;
import model.Album;
import model.NonAdminUser;

/**
 *
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * This class is controls all features that non-admin users have access to
 */
public class NonAdminUserController implements LogoutController
{
    @FXML
    public ListView<Album> listview;
    @FXML
    public Button mLogOff, mDisplay, mOpenAlbum, mRenameAlbum, mDeleteAlbum, mSearch, mAddAlbum;
    @FXML
    public MenuButton mSortBy;
    @FXML
    public Text tUser, tNumber, tDateSpan;
    @FXML
    public TextField tfName, tfNewAlbum;
    /**
     * string username of the current non-admin username logged in
     */
    public static String username;
    /**
     * list of albums stored as an arraylist
     */
    public static ArrayList<Album> listOfAlbums = new ArrayList<>();
    /**
     * observable list used to aid in listing
     */
    public ObservableList<Album> obsList;
    /**
     * adminUser is an instance that helps control the flow of users
     */
    public static AdminUser adminUser = Photos.driver;
    /**
     * nonAdminUser is an instance that helps control the flow of current non-admin user
     */
    public static NonAdminUser nonAdminUser;
    /**
     *
     * @param stage
     *
     * sets the stage for the non-admin user's albums
     */
    public void start(Stage stage)
    {

        update();
        stage.setTitle(adminUser.getCurrentUser().getUserName() + " Album Collection");
        if(!listOfAlbums.isEmpty())
        {
            listview.getSelectionModel().select(0);
        }

        if (listOfAlbums.size() > 0)
        {
            tfName.setText(listOfAlbums.get(0).albumName);
            tNumber.setText("Number of Photos: " + listOfAlbums.get(0).countOfPhotos);
            tDateSpan.setText("Date Span: \n\t" + listOfAlbums.get(0).getFirstDate() + "\n\t" + listOfAlbums.get(0).getLastDate());
        }

        listview.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> refreshContent(newValue) );
    }
    /**
     *
     * @param usersNewAlbumVal The new album value
     *
     *  Helps refresh the listview of non-admin users' albums
     */
    private void refreshContent(Album usersNewAlbumVal)
    {
        if (usersNewAlbumVal != null)
        {
            tfName.setText(usersNewAlbumVal.albumName);
            tNumber.setText("Number of Photos: " + usersNewAlbumVal.countOfPhotos);
            tDateSpan.setText("Date Span: \n\t" + usersNewAlbumVal.getFirstDate() + " \n\t" + usersNewAlbumVal.getLastDate());
        }
    }
    /**
     *
     * @throws IOException
     *
     * this method helps aid in adding a new non-admin user created album to their collection
     */
    public void addAlbum() throws IOException
    {
        String nameOfAlbumAdded = tfNewAlbum.getText().trim();
        Album album = new Album(nameOfAlbumAdded);

        if(nameOfAlbumAdded.isEmpty() || nameOfAlbumAdded == null)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Adding Album Error");
            alert.setContentText("The text box is empty, make sure you enter a name");
            alert.showAndWait();
            return;
        }
        else if(nonAdminUser.checkIfAlbumExistsInNonAdminUsers(album))
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Adding Album Error");
            alert.setContentText("Album already exists, enter a new unique name");
            alert.showAndWait();
            return;
        }
        else
        {
            nonAdminUser.addUserAlbum(album);
            update();
            tfNewAlbum.clear();
        }

        NonAdminUser.saveToDat(nonAdminUser);
    }
    /**
     *
     * @throws IOException
     *
     * this method helps aid in re-naming a current non-admin user created album to their collection
     */
    public void renameAlbum() throws IOException
    {
        String newAlbumNameOfUser = tfName.getText().trim();

        int index = listview.getSelectionModel().getSelectedIndex();
        Album album = nonAdminUser.getUserAlbumGivenIndex(index);
        Optional<ButtonType> result;
        Album tempAlbumName = new Album(newAlbumNameOfUser);

        if (newAlbumNameOfUser.length() == 0)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Album Renaming Error");
            alert.setContentText("Please enter a valid album name.");
            alert.showAndWait();
            return;
        }
        else if (newAlbumNameOfUser.equals(album.albumName))
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Album Renaming Error");
            alert.setContentText("Enter new album name then click 'Rename'");
            alert.showAndWait();
            return;
        }
        else if (nonAdminUser.checkIfAlbumExistsInNonAdminUsers(tempAlbumName))
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Album Renaming Error");
            alert.setContentText("The new renamed album name is the same as the old, enter a new unique name");
            alert.showAndWait();
            return;
        }
        else
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Album Renaming Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Changing album name to this, you sure to continue?");
            result = alert.showAndWait();
        }

        if (result.get() == ButtonType.OK)
        {
            album.renamedAlbumInput(newAlbumNameOfUser);
            update();
            NonAdminUser.saveToDat(nonAdminUser);
        }
        else
        {
            return;
        }

        return;
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * this method re-directs the non-admin user to a scene where it opens the album they selected
     * thrwos alert error if the album doesn't exist
     */
    public void openAlbum(ActionEvent e) throws IOException
    {
        PhotoViewController.nonAdminUser = nonAdminUser;
        PhotoViewController.albumStorage = listview.getSelectionModel().getSelectedItem();
        PhotoViewController.listOfAlbums = listOfAlbums;

        int albumindex = listview.getSelectionModel().getSelectedIndex();
        int currentuserindex = adminUser.getIndexOfUser();
        if(adminUser.getNonAdminUsers().get(currentuserindex).getUserAlbums().size() == 0)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Album Opening Error");
            alert.setHeaderText(null);
            alert.setContentText("Cannot open something that isn't there");
            alert.showAndWait();
            return;
        }

        Album album = adminUser.getNonAdminUsers().get(currentuserindex).getUserAlbums().get(albumindex);
        adminUser.getNonAdminUsers().get(currentuserindex).setCurrUserAlbum(album);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PhotoView.fxml"));
        Parent sceneManager = (Parent) fxmlLoader.load();
        PhotoViewController photoController = fxmlLoader.getController();
        Scene adminScene = new Scene(sceneManager);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        photoController.start(appStage);
        appStage.setScene(adminScene);
        appStage.show();
    }
    /**
     *
     * @throws IOException
     *
     * this method helps the non-admin user delete an album they created
     */
    public void deleteAlbum() throws IOException
    {
        int index = listview.getSelectionModel().getSelectedIndex();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation PopUp");
        alert.setHeaderText(null);
        alert.setContentText("Deleting this album, you sure to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            nonAdminUser.deleteUserAlbum(index);
            update();
            NonAdminUser.saveToDat(nonAdminUser);

            if (nonAdminUser.getUserAlbums().size() == 0)
            {
                mDeleteAlbum.setVisible(false);
            }
            else
            {
                int indexOfLastUser = nonAdminUser.getUserAlbums().size();
                if (nonAdminUser.getUserAlbums().size() == 1)
                {
                    listview.getSelectionModel().select(0);
                }
                else if (index == indexOfLastUser)
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
     *
     * @param event
     * @throws IOException
     *
     * this method helps transition the non-admin user to search scene when non-admin user wants to search
     */
    public void search(ActionEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Search.fxml"));
        Parent sceneManager = (Parent) fxmlLoader.load();
        SearchController searchController = fxmlLoader.getController();
        Scene adminScene = new Scene(sceneManager);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        searchController.start();
        appStage.setScene(adminScene);
        appStage.show();
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * this helps the user logout at any stage
     */
    public void logOut(ActionEvent e) throws IOException
    {
        logOutUser(e);
    }
    /**
     * Refreshes the album list everytime the non-admin user goes to input/change/delete an album
     */
    public void update()
    {
        tUser.setText("Album List of " + username);
        nonAdminUser = adminUser.getNonAdminUserByStringInput(username);
        listOfAlbums.clear();
        for (int i = 0; i < nonAdminUser.getUserAlbums().size(); i++)
        {
            listOfAlbums.add(nonAdminUser.getUserAlbums().get(i));
        }

        obsList = FXCollections.observableArrayList(listOfAlbums);
        listview.setItems(obsList);
        listview.refresh();
    }
}