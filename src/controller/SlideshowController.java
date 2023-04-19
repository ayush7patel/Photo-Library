package controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Photo;

/**
 *
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 */
public class SlideshowController implements LogoutController
{
    @FXML
    public ImageView displayArea;
    @FXML
    public Button mForward, mBackward, mBack, mLogOff;
    @FXML
    public TextField tfCaption, tfTagName, tfTagValue;
    @FXML
    public Text tStatus;
    /**
     * stores the albums for use
     */
    public static ArrayList<Photo> storageOfAlbumsByUser = new ArrayList<>();
    public static final int fi = 0;
    public static int bi;
    public static int ci;
    /**
     * on start updates slideshow when needed
     */
    public void start()
    {
        update();
    }
    /**
     * depending on back/next populates the current imageview with the image on the slide
     */
    public void update() {
        ci = 0;
        bi = storageOfAlbumsByUser.size()-1;

        File file;
        Photo photo = storageOfAlbumsByUser.get(0);
        if (photo != null) {
            file = photo.getPicture();
            Image image = new Image(file.toURI().toString());
            displayArea.setImage(image);
        }

        int ci = SlideshowController.ci +1;
        int bi = SlideshowController.bi +1;
        tStatus.setText("Photo: " + ci + " of " + bi);

    }
    /**
     *  this method switches to next slide picture
     */
    public void next()
    {
        if (ci +1 > bi)
        {
            return;
        }
        else
        {
            ci++;
            File file;
            Photo photo = storageOfAlbumsByUser.get(ci);
            if (photo != null) {
                file = photo.getPicture();
                Image image = new Image(file.toURI().toString());
                displayArea.setImage(image);
                int ci = SlideshowController.ci +1;
                int bi = SlideshowController.bi +1;
                tStatus.setText("Photo: " + ci + " of " + bi);
            }
        }
    }

    /**
     * this method switches back to previous slide picture
     */
    public void previous()
    {
        if (ci -1 < fi)
        {
            return;
        }
        else
        {
            ci--;
            File file;
            Photo photo = storageOfAlbumsByUser.get(ci);
            if (photo != null)
            {
                file = photo.getPicture();
                Image image = new Image(file.toURI().toString());
                displayArea.setImage(image);
                int ci = SlideshowController.ci +1;
                int bi = SlideshowController.bi +1;
                tStatus.setText("Photo: " + ci + " of " + bi);
            }
        }
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * helps the non-admin user navigate back from current scene
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
     * logs the non-admin user out
     */
    public void logOut(ActionEvent event) throws IOException
    {
        logOutUser(event);
    }
}