package mcs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mcs.controller.FileController;
import mcs.view.Login;

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
        primaryStage.setTitle(Common.appName + " " + Common.version);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(new FileInputStream(Common.iconCamera)));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
