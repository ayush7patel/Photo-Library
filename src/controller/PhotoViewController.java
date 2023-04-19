package controller;

import java.io.*;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.AdminUser;
import model.Album;
import model.NonAdminUser;
import model.Photo;

/**
 *
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * this class focuses on the list of photos that are in a non-admin users' album
 */
public class PhotoViewController implements LogoutController
{
    @FXML
    public ListView<Photo> listview;
    @FXML
    public ImageView displayArea;
    @FXML
    public Button mLogOff, mBack, mAdd, mDelete, mSlideshow, mSearch, mDisplay, mCopy, mMove;
    @FXML
    public TextField tfCopy, tfMove;
    @FXML
    public Text tCaption, tDate;
    /**
     * list of photos in the form of an arraylist
     */
    public static ArrayList<Photo> listOfPhotos = new ArrayList<>();
    /**
     * a list in the form of an observable list that helps show the photos
     */
    public ObservableList<Photo> obsList;
    /**
     * adminUser is an instance that helps control the flow of users
     */
    public static AdminUser adminUser = Photos.driver;
    /**
     * nonAdminUser is an instance that helps control the flow of current non-admin user
     */
    public static NonAdminUser nonAdminUser;
    /**
     * list of albums in the form of an arraylist
     */
    public static ArrayList<Album> listOfAlbums;
    /**
     * for album storage for a particular non-admin user
     */
    public static Album albumStorage;
    /**
     *
     * @param stage
     *
     * this method helps set up the stage for the non-admin users' album photos that they choose to view/open
     */
    public void start(Stage stage)
    {
        stage.setTitle("Album Photos' for " + adminUser.getCurrentUser().getCurrAlbum().getAlbumName());
        displayArea.setFitHeight(100);
        displayArea.setFitWidth(100);
        displayArea.setPreserveRatio(false);
        update();
        if(adminUser.getCurrentUser().getCurrAlbum().getPhotos().size() == 0)
        {
            mDelete.setVisible(false);
        }
        if(!listOfPhotos.isEmpty())
        {
            listview.getSelectionModel().select(0);
        }
        if (listOfPhotos.size() > 0)
        {
            tCaption.setText("Caption: " + listOfPhotos.get(0).caption);
            tDate.setText("Date: " + listOfPhotos.get(0).date);
            displayThumbnail();
        }

        listview.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            displayThumbnail();
            updateCaption();
        });
    }
    /**
     *
     * @throws IOException
     *
     * this method aids in the help of moving the non-admin users photo to another album
     */
    public void move() throws IOException
    {
        String albumMoveDest = tfMove.getText().trim();
        boolean isInList = false;
        int indexOfUserAlbum = 0;
        for (int i = 0; i < listOfAlbums.size(); i++)
        {
            Album tempAlbumName = listOfAlbums.get(i);
            if (tempAlbumName.getAlbumName().equals(albumMoveDest))
            {
                isInList = true;
                indexOfUserAlbum = i;
            }
        }
        if (!albumMoveDest.isEmpty() && isInList)
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Photo Move Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Moving this photo to this album: " + albumMoveDest + "you sure to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                Album newAlbum = listOfAlbums.get(indexOfUserAlbum);
                Photo photo = listview.getSelectionModel().getSelectedItem();
                newAlbum.addNewUserPhoto(photo);
                albumStorage.delPhoto(listview.getSelectionModel().getSelectedIndex());
                newAlbum.saveToDat(newAlbum);
                albumStorage.saveToDat(albumStorage);
                update();
            }
            else
            {
                return;
            }
        }
        else
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Move Photo Error");
            alert.setHeaderText("Album not found or does not exist.");
            alert.showAndWait();
            Optional<ButtonType> buttonClicked=alert.showAndWait();
            if (buttonClicked.get()==ButtonType.OK)
            {
                alert.close();
            }
            else
            {
                alert.close();
            }

            return;
        }
    }
    /**
     *
     * @throws IOException
     *
     * this method aids in the help of copying the non-admin users photo to another album
     */
    public void copy() throws IOException
    {
        String albumCopyDest = tfCopy.getText().trim();
        boolean isInList = false;
        int indexOfUserAlbum = 0;
        for (int i = 0; i < listOfAlbums.size(); i++)
        {
            Album tempAlbumName = listOfAlbums.get(i);
            if (tempAlbumName.getAlbumName().equals(albumCopyDest))
            {
                isInList = true;
                indexOfUserAlbum = i;
            }
        }
        if (!albumCopyDest.isEmpty() && isInList)
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Photo Copy Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Copying this photo to this album: " + albumCopyDest + "you sure to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
            {
                Album newAlbum = listOfAlbums.get(indexOfUserAlbum);
                Photo photo = listview.getSelectionModel().getSelectedItem();
                newAlbum.addNewUserPhoto(photo);
                newAlbum.saveToDat(newAlbum);
            }
            else
            {
                return;
            }
        }
        else
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Copy Photo Error");
            alert.setHeaderText("Album not found or does not exist.");
            alert.showAndWait();
            Optional<ButtonType> buttonClicked=alert.showAndWait();
            if (buttonClicked.get()==ButtonType.OK)
            {
                alert.close();
            }
            else
            {
                alert.close();
            }

            return;
        }
    }
    /**
     * helps display the thumbnail larger
     */
    public void displayThumbnail()
    {
        Photo p = listview.getSelectionModel().getSelectedItem();
        File f;
        if (p != null)
        {
            f = p.getPicture();
            if(adminUser.getCurrentUser().getUserName().equals("stock") && p.isStockPic)
            {
                String str = f.getAbsolutePath();
                int sp = str.indexOf("StockPhotos");
                String fp = str.substring(sp, str.length());
                File img = new File(fp);
                Image image = new Image(img.toURI().toString());
                displayArea.setImage(image);
            }
            else
            {
                Image image = new Image(f.toURI().toString());
                displayArea.setImage(image);
            }
        }
        else
        {
            displayArea.setImage(null);
        }

        return;
    }
    /**
     * allows the non-admin user to re-caption the photo if wanted
     */
    public void updateCaption()
    {
        Photo p = listview.getSelectionModel().getSelectedItem();
        if (listOfPhotos.size() > 0 && p != null)
        {
            tCaption.setText("Caption: " + p.caption);
            tDate.setText("Date: " + p.date);
        }
        else
        {
            tCaption.setText("Caption: ");
            tDate.setText("Date: ");
        }
    }
    /**
     *
     * @throws IOException
     *
     * helps the non-admin user to add photo in current album
     */
    public void addPhoto() throws IOException
    {
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("*.bmp", "*.gif", "*jpeg", "*png", "*jpg");
        fc.getExtensionFilters().add(filter);
        File imgfile = fc.showOpenDialog(null);

        if (imgfile == null) {
            return;
        }
        else
        {
            String fp = imgfile.getAbsolutePath();
            Photo np;
            if(adminUser.getCurrentUser().getUserName().equals("stock"))
            {
                int index;
                if (fp.contains("StockPhotos")) {
                    index = fp.indexOf("StockPhotos");
                    String nFP = fp.substring(index,fp.length());
                    Photo nP2 = new Photo(imgfile, nFP);
                    nP2.isStockPic = true;
                    albumStorage.addNewUserPhoto(nP2);
                }
                else
                {
                    np = new Photo(imgfile, fp);
                    albumStorage.addNewUserPhoto(np);
                }
            }
            else
            {
                np = new Photo(imgfile, fp);
                albumStorage.addNewUserPhoto(np);
            }

            update();
        }
        if(adminUser.getCurrentUser().getCurrAlbum().getPhotos().size() > 0)
        {
            mDelete.setVisible(true);
        }
        if(!listOfPhotos.isEmpty())
        {
            listview.getSelectionModel().select(0); //select first user
        }

        Album.saveToDat(albumStorage);
    }
    /**
     *
     * @throws IOException
     *
     * helps the non-admin user to delete photo in current album
     */
    public void deletePhoto() throws IOException {

        int index = listview.getSelectionModel().getSelectedIndex();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Photo Delete Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Deleting this photo, you sure to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            albumStorage.delPhoto(index);
            update();
            if (adminUser.getCurrentUser().getCurrAlbum().getPhotos().size() == 0)
            {
                mDelete.setVisible(false);
            }
            else
            {
                int indexOfLastUser = albumStorage.getPhotos().size();
                if (albumStorage.getPhotos().size() == 1)
                {
                    listview.getSelectionModel().select(0);
                }
                else if (index == indexOfLastUser)
                {
                    listview.getSelectionModel().select(indexOfLastUser - 1);
                }
                else
                {
                    listview.getSelectionModel().select(index);
                }
            }

            Album.saveToDat(albumStorage);
        }
        else
        {
            return;
        }

        return;
    }
    /**
     * Updates the listview of photo's when a non-admin user adds or deletes
     */
    public void update()
    {
        listOfPhotos.clear();
        for (int i = 0; i < albumStorage.getPhotos().size(); i++)
        {
            listOfPhotos.add(albumStorage.getPhotos().get(i));
        }

        obsList = FXCollections.observableArrayList(listOfPhotos);
        listview.setItems(obsList);
        listview.refresh();
    }

    /**
     *
     * @param event
     * @throws IOException
     *
     * opens search scene
     */
    public void search(ActionEvent event) throws IOException {
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
     * enlarges the thumbnail image in a separate window
     * This window includes the properties of photos to remove tag, add tag, and caption/re-caption
     */
    public void display(ActionEvent e) throws IOException
    {
        if (listOfPhotos.size() > 0)
        {
            boolean photoChecked = false;
            for (int i = 0; i < listOfPhotos.size(); i++)
            {
                if (listview.getSelectionModel().isSelected(i))
                {
                    photoChecked = true;
                }
            }
            if (photoChecked)
            {
                int indexOfUserPhoto = listview.getSelectionModel().getSelectedIndex();
                Photo currPhotoOfUser = adminUser.getCurrentUser().getCurrAlbum().getPhotos().get(indexOfUserPhoto);
                adminUser.getCurrentUser().getCurrAlbum().setCurrUserPhoto(currPhotoOfUser);

                SinglePhotoController.photoStorage = listview.getSelectionModel().getSelectedItem();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SinglePhoto.fxml"));
                Parent sceneManager = (Parent) fxmlLoader.load();
                SinglePhotoController singlePhotoController = fxmlLoader.getController();
                Scene adminScene = new Scene(sceneManager);
                Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                singlePhotoController.start(appStage);
                appStage.setScene(adminScene);
                appStage.show();
            }
        }
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * Helps the non-admin user with user interactions in the form a viewing photos in the form of a slideshow
     */
    public void slideshow(ActionEvent e) throws IOException
    {
        if (listOfPhotos.size() == 0)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Slideshow Error");
            alert.setHeaderText("No Photos to Display");
            alert.showAndWait();
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
            SlideshowController.storageOfAlbumsByUser = listOfPhotos;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Slideshow.fxml"));
            Parent sceneManager = (Parent) fxmlLoader.load();
            SlideshowController slideshowController = fxmlLoader.getController();
            Scene adminScene = new Scene(sceneManager);
            Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            slideshowController.start();
            appStage.setScene(adminScene);
            appStage.show();
        }
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * helps non-admin user navigate back
     */
    public void back(ActionEvent e) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/NonAdminUser.fxml"));
        Parent sceneManager = (Parent) fxmlLoader.load();
        NonAdminUserController nonAdminUserController = fxmlLoader.getController();
        Scene adminScene = new Scene(sceneManager);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        nonAdminUserController.start(appStage);
        appStage.setScene(adminScene);
        appStage.show();
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * helps logout the non-admin user
     */
    public void logOut(ActionEvent e) throws IOException {
        logOutUser(e);
    }
}
