package sample;

import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.view.Login;

import java.io.FileInputStream;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox root = new VBox();
        Login login = new Login(root);
        primaryStage.setTitle("Image Comment System");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        //   primaryStage.setFullScreen(true);
    //    primaryStage.getIcons().add(new Image(new FileInputStream(Common.iconCamera)));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
