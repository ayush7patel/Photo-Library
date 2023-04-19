package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.AdminUser;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 * @author Ayush Patel
 * @author Kirtan Patel
 *
 * This class starts the whole PhotoLibrary program
 */
public class Photos extends Application {

    public static AdminUser driver = new AdminUser();
    public Stage mainStage;

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            mainStage = primaryStage;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            AnchorPane root = (AnchorPane) fxmlLoader.load();
            Scene scene = new Scene(root);
            mainStage.setResizable(false);
            mainStage.setTitle("Photo Library");
            mainStage.setScene(scene);
            mainStage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>(){public void handle(WindowEvent we){} });
    }
    /**
     *
     * @param args
     * @throws IOException
     * @throws ClassNotFoundException
     *
     * this class launches args in the method main
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        launch(args);
    }
}