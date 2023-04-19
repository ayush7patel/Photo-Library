package controller;

import java.io.File;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Photo;
import model.AdminUser;
import model.Tag;

/**
 *
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * this class helps when viewing a single image
 */
public class SinglePhotoController implements LogoutController
{
    @FXML
    public ListView<String> listview;
    @FXML
    public ImageView displayArea;
    @FXML
    public Button mLogOff, mBack, mCaption, mAddTag, mRemoveTag;
    @FXML
    public TextField tfCaption, tfTagName, tfTagValue;
    /**
     * adminUser is an instance that helps control the flow of users
     */
    public static AdminUser adminUser = Photos.driver;
    /**
     * list of tags stored in an arraylist
     */
    public static ArrayList<Tag> listOfTags = new ArrayList<>();
    /**
     * helps display the tags stored as string in an arraylist
     */
    public static ArrayList<String> displayOfTags = new ArrayList<>();
    /**
     * observable list of string for tags aids in displaying them on screen
     */
    public ObservableList<String> obsListT;
    /**
     * helps keep track of curr non-admin user photo
     */
    public static Photo photoStorage;
    /**
     *
     * @param stage
     *
     * sets the scene on advance into this window
     */
    public void start(Stage stage)
    {
        stage.setTitle(adminUser.getCurrentUser().getCurrAlbum().getCurrUserPhoto().getCaption() + " ");
        update();
        if(!listOfTags.isEmpty())
        {
            listview.getSelectionModel().select(0);
        }
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * this method helps save the caption
     */
    public void saveCaption(ActionEvent e) throws IOException
    {
        String caption = tfCaption.getText().trim();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Caption Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("The new caption is: " + caption);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            photoStorage.setCaption(caption);
            photoStorage.saveToDat(photoStorage);
        }
        else
        {
            return;
        }
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * this method helps add the tag
     */
    public void addTag(ActionEvent e) throws IOException
    {
        String tName = tfTagName.getText().trim();
        String tValue = tfTagValue.getText().trim();

        if (tName.isEmpty() || tValue.isEmpty())
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Adding Tag Error");
            alert.setContentText("One or more input boxes are empty");
            alert.showAndWait();
            return;
        }
        else
        {
            Tag t = new Tag(tName, tValue);
            adminUser.getCurrentUser().getCurrAlbum().getCurrUserPhoto().addTag(t.tag_name, t.tag_value);
            update();
            AdminUser.saveToDat(adminUser);
        }
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * this method helps delete a tag
     */
    public void deleteTag(ActionEvent e) throws IOException
    {
        int index = listview.getSelectionModel().getSelectedIndex();
        ArrayList<Tag> tl = adminUser.getCurrentUser().getCurrAlbum().getCurrUserPhoto().getListOfTags();
        adminUser.getCurrentUser().getCurrAlbum().getCurrUserPhoto().deleteTag(tl.get(index).tag_name, tl.get(index).tag_value);
        update();
        AdminUser.saveToDat(adminUser);
    }
    /**
     * this method refreshes the listview after the non-admin user add/removes tag
     */
    public void update()
    {
        File f;
        if (photoStorage != null)
        {
            f = photoStorage.getPicture();
            Image image = new Image(f.toURI().toString());
            displayArea.setImage(image);
        }

        displayOfTags.clear();
        ArrayList<Tag> tags = adminUser.getCurrentUser().getCurrAlbum().getCurrUserPhoto().getListOfTags();
        for(Tag t : tags)
        {
            displayOfTags.add("Name: " + t.tag_name +    " | Value: " + t.tag_value);
        }

        obsListT = FXCollections.observableArrayList(displayOfTags);
        listview.setItems(obsListT);
        tfTagName.clear();
        tfTagValue.clear();
    }

    /**
     *
     * @param e
     * @throws IOException
     *
     * helps redirect non-admin user back to previous scene if needed
     */
    public void back(ActionEvent e) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/PhotoView.fxml"));
        Parent sceneManager = (Parent) fxmlLoader.load();
        PhotoViewController photoViewController = fxmlLoader.getController();
        Scene adminScene = new Scene(sceneManager);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        photoViewController.start(appStage);
        appStage.setScene(adminScene);
        appStage.show();
    }

    /**
     *
     * @param e
     * @throws IOException
     *
     * this method helps the non-admin user log out anytime
     */
    public void logOut(ActionEvent e) throws IOException {
        logOutUser(e);
    }
}
