package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import application.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Album;
import model.Photo;
import model.Tag;

/**
 *
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * this class helps the non-admin user perform searches to the non-admin user's wishes as well as creates an album based on those searches
 */
public class SearchController implements LogoutController
{
    @FXML
    public Button mBack, mLogOff, mAddTag, mAnySearch, mAllSearch, mSearchDate, mCreateAlbum;
    @FXML
    public TextField tfName, tfValue;
    @FXML
    public ListView<String> listview;
    @FXML
    public ListView<Photo> listOfPhotoView;
    @FXML
    public ImageView imageview;
    @FXML
    public DatePicker dTo, dFrom;
    /**
     * list of tags in the form of an arraylist
     */
    public ArrayList<Tag> listOfTags = new ArrayList<>();
    /**
     * helps display the tags stored as string in an arraylist
     */
    public ArrayList<String> displayOfTags = new ArrayList<String>();
    /**
     * observable list of string for tags aids in displaying them on screen
     */
    public ObservableList<String> obsListT;
    /**
     * observable list of photos for photos aids in displaying them on screen
     */
    public ObservableList<Photo> obsListP;
    /**
     * stores the photos returned from searches in the form of an arraylist
     */
    public ArrayList<Photo> listOfPhotos = new ArrayList<Photo>();
    /**
     * does nothing but help control the flow of the program
     */
    public void start()
    {

    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * helps the non-admin user search for photos within a certain date range
     */
    public void dateSearch(ActionEvent e) throws IOException
    {
        this.listOfPhotos.clear();
        LocalDate beg = dFrom.getValue();
        LocalDate end = dTo.getValue();
        if(beg == null || end == null)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Date Error");
            alert.setHeaderText("One or more input boxes are empty");
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
        if(end.isBefore(beg))
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Date Error");
            alert.setHeaderText("Invalid Date Range -> End Date is before Start Date");
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

        mAnySearch.setVisible(false);
        mAnySearch.setDisable(true);
        mAllSearch.setVisible(false);
        mAllSearch.setVisible(true);
        this.listOfPhotos = Photos.driver.getCurrentUser().getPhotosFromDateRange(beg, end);
        displayPhotos();
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * allows the non-admin user to search based on combination of two tag values (one or the other)
     */
    public void disjunctiveSearch(ActionEvent e) throws IOException
    {
        this.listOfPhotos.clear();
        if(listOfTags.isEmpty() || listOfTags == null)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Empty Result Error");
            alert.setHeaderText("Add tags before clicking search");
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

        mAllSearch.setVisible(false);
        mAllSearch.setDisable(true);
        mSearchDate.setVisible(false);
        mSearchDate.setDisable(true);
        this.listOfPhotos = Photos.driver.getCurrentUser().getDisjunctiveTagPhotos(listOfTags);
        displayPhotos();
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * allows the non-admin user to search based on combination of two tag values (both values are used in search)
     */
    public void conjunctiveSearch(ActionEvent e) throws IOException
    {
        if(listOfTags.isEmpty() || listOfTags == null)
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Empty Result Error");
            alert.setHeaderText("Add tags before clicking search");
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
        mSearchDate.setVisible(false);
        mSearchDate.setDisable(true);
        this.listOfPhotos = Photos.driver.getCurrentUser().getConjunctiveTagPhotos(listOfTags);
        displayPhotos();
    }
    /**
     *
     * @param event
     * @throws IOException
     *
     * allows the non-admin user to add tag to photos
     */
    public void addTag(ActionEvent event) throws IOException
    {
        if(tfName.getText().trim().isEmpty() || tfValue.getText().trim().isEmpty())
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Tag Missing Values");
            alert.setHeaderText("One or more input boxes are empty");
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

        Tag tag = new Tag(tfName.getText().trim(), tfValue.getText().trim());
        listOfTags.add(tag);
        refreshListOfTags();
    }
    /**
     * tag listview is refreshed after non0-admin user adds/removes
     */
    public void refreshListOfTags()
    {
        displayOfTags.clear();
        for(Tag t : listOfTags)
        {
            displayOfTags.add("Name: " + t.tag_name +    " | Value: " + t.tag_value);
        }

        obsListT = FXCollections.observableArrayList(displayOfTags);
        listview.setItems(obsListT);
        tfName.clear();
        tfValue.clear();
    }
    /**
     * Displays the photos in the listview
     */
    public void displayPhotos()
    {
        obsListP = FXCollections.observableArrayList(listOfPhotos);
        listOfPhotoView.setCellFactory(new Callback<ListView<Photo>, ListCell<Photo>>()
        {
            @Override
            public ListCell<Photo> call(ListView<Photo> p){
                return new Results();
            }

        });

        listOfPhotoView.setItems(obsListP);
        if(!obsListP.isEmpty())
        {
            listOfPhotoView.getSelectionModel().select(0);
        }
    }
    /**
     *
     * a helper methods that shows the image in the view after searches
     *
     */
    private class Results extends ListCell<Photo>
    {
        AnchorPane anchor = new AnchorPane();
        StackPane stackpane = new StackPane();
        ImageView imageView = new ImageView();

        public Results() {
            super();
            imageView.setFitWidth(100.0);
            imageView.setFitHeight(100.0);
            imageView.setPreserveRatio(true);
            StackPane.setAlignment(imageView, Pos.TOP_LEFT);
            stackpane.getChildren().add(imageView);
            stackpane.setPrefHeight(110.0);
            stackpane.setPrefWidth(90.0);
            AnchorPane.setLeftAnchor(stackpane, 0.0);
            anchor.getChildren().addAll(stackpane);
            anchor.setPrefHeight(110.0);
            setGraphic(anchor);
        }
        @Override
        public void updateItem(Photo photo, boolean empty)
        {
            super.updateItem(photo, empty);

            setText(null);
            if(photo == null)
            {

            }
            else{

                Image img = new Image(photo.picture.toURI().toString());
                imageView.setImage(img);
            }
        }
    }
    /**
     *
     * @param event
     * @throws IOException
     *
     * creates the album based on searches by the non-admin user
     */
    public void createAlbumOnSearch(ActionEvent event) throws IOException
    {
        if(listOfPhotos.isEmpty())
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Album Error");
            alert.setHeaderText("Cannot create album since there no photos from search results");
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

        Dialog<String> d = new Dialog<>();
        d.setTitle("Creating a new album from search results");
        d.setHeaderText("Name for album created from search results ");
        d.setResizable(true);
        Label al = new Label("Album Name: ");
        TextField aTF = new TextField();
        GridPane g = new GridPane();
        g.add(al, 1, 1);
        g.add(aTF, 2, 1);
        d.getDialogPane().setContent(g);
        ButtonType btOK = new ButtonType("Add", ButtonData.OK_DONE);
        d.getDialogPane().getButtonTypes().add(btOK);
        d.setResultConverter(new Callback<ButtonType, String>()
        {
            @Override
            public String call(ButtonType b)
            {
                if (b == btOK) {
                    if (aTF.getText().trim().isEmpty())
                    {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Album Error");
                        alert.setHeaderText("Album name required");
                        Optional<ButtonType> buttonClicked=alert.showAndWait();
                        if (buttonClicked.get()==ButtonType.OK)
                        {
                            alert.close();
                        }
                        else
                        {
                            alert.close();
                        }

                        return null;
                    }
                    return aTF.getText().trim();
                }

                return null;
            }
        });
        Optional<String> result = d.showAndWait();
        if (result.isPresent())
        {
            Album albumFromSearch = new Album(result.get());
            Photos.driver.getCurrentUser().addUserAlbum(albumFromSearch);
            for(Photo photo : listOfPhotos)
            {
                albumFromSearch.addNewUserPhoto(photo);
            }
        }
    }
    /**
     *
     * @param e
     * @throws IOException
     *
     * directs the non-admin user back at any scene
     */
    public void back(ActionEvent e) throws IOException{

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
     * helps log out the non-admin user whenever
     */
    public void logOut(ActionEvent e) throws IOException
    {
        logOutUser(e);
    }
}

