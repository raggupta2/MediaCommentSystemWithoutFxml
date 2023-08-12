package sample;

import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import sample.controller.FileController;
import sample.controller.MediaController;
import sample.view.Login;

import java.io.File;
import java.io.FileInputStream;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox root = new VBox();
        String url = "file:" + FileController.getValidUrl(Common.backgroundMain);
        root.setStyle(
                "-fx-background-image: url(" + url + ");" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-background-size: stretch"
        );
        Login login = new Login(root);
        String version = "0.9.9.2";
        primaryStage.setTitle("Image Comment System" + " " + version);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(new FileInputStream(Common.iconCamera)));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
