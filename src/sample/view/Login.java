package sample.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import sample.Common;
import sample.controller.FileController;
import sample.controller.UserController;
import sample.model.User;

public class Login {

    private Component component;
    private UserController userController;
    private VBox mainPane;
    private ImageManagement imageManagement;

    public Login(VBox vBox) {
        component = new Component();
        userController = new UserController();
        this.mainPane = vBox;
        drawLogin();
    }

    /**
     * draw login page
     */
    private void drawLogin() {
        mainPane.getChildren().clear();
        mainPane.setAlignment(Pos.CENTER);
        String url = "file:" + FileController.getValidUrl(Common.backgroundLogin);
        mainPane.setStyle(
                "-fx-background-image: url(" + url + ");" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-background-size: stretch"
        );

        Text labelLogin = component.drawText("Login");
        labelLogin.setFont(new Font(40));

        GridPane inputPane = new GridPane();
        Text labelUserName = component.drawText("User name");
        labelUserName.setFont(new Font(25));
        Text labelPassword = component.drawText("Password");
        labelPassword.setFont(new Font(25));
        TextField textName = component.drawTextField("");
        PasswordField textPassword = component.drawPasswordField("");

        inputPane.add(labelUserName, 0, 0);
        inputPane.add(textName, 1, 0);
        inputPane.add(labelPassword, 0, 1);
        inputPane.add(textPassword, 1, 1);

        textName.setPrefSize(250, 50);
        textName.setPadding(new Insets(10, 20, 10, 20));
        textName.setFont(new Font(20));
        textName.setStyle("-fx-background-color: rgba(200,200,200,0);" +
                "-fx-border-color: rgba(115,177,224,0.8);" +
                "-fx-border-radius: 20;" +
                "-fx-border-width: 3;" +
                "-fx-text-fill: black");

        textPassword.setPrefSize(250, 50);
        textPassword.setPadding(new Insets(10, 20, 10, 20));
        textPassword.setFont(new Font(20));
        textPassword.setStyle("-fx-background-color: rgba(200,200,200,0);" +
                "-fx-border-color: rgba(115,177,224,0.8);" +
                "-fx-border-radius: 20;" +
                "-fx-border-width: 3;" +
                "-fx-text-fill: black");

        HBox groupButton = new HBox();
        Button login = component.drawButton(groupButton, "Log in");
        Button register = component.drawButton(groupButton, "Register");

        mainPane.setSpacing(30);
        inputPane.setHgap(20);
        inputPane.setVgap(20);
        inputPane.setAlignment(Pos.CENTER);
        groupButton.setAlignment(Pos.CENTER);
        groupButton.setSpacing(10);
        mainPane.getChildren().add(labelLogin);
        mainPane.getChildren().add(inputPane);
        mainPane.getChildren().add(groupButton);
        //  imageManagement = new ImageManagement(mainPane, new User("a", "b", 1));

        login.setOnMouseClicked(event -> {
            String name = textName.getText();
            String password = textPassword.getText();
            User user = userController.login(name, password);
            if (user != null) {
                imageManagement = new ImageManagement(mainPane, user);
            } else {
                drawResult("The user does not register", true);
            }
        });
        register.setOnMouseClicked(event -> {
            drawRegister();
        });
    }

    private void drawResult(String str, boolean isLogin) {
        mainPane.getChildren().clear();
        Label label = new Label(str);
        label.setFont(new Font(30));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        Button btn_ok = component.drawButton(hBox, "OK");

        mainPane.getChildren().add(label);
        mainPane.getChildren().add(hBox);
        btn_ok.setOnMouseClicked(event -> {
            if (isLogin) {
                drawLogin();
            } else {
                drawRegister();
            }
        });
    }

    /**
     * draw register page
     */
    private void drawRegister() {
        mainPane.getChildren().clear();
        mainPane.setAlignment(Pos.CENTER);
        String url = "file:" + FileController.getValidUrl(Common.backgroundLogin);
        mainPane.setStyle(
                "-fx-background-image: url(" + url + ");" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-background-size: stretch"
        );

        Text labelLogin = component.drawText("Register");
        labelLogin.setFont(new Font(40));

        GridPane inputPane = new GridPane();
        Text labelUserName = component.drawText("User name");
        labelUserName.setFont(new Font(25));
        Text labelPassword = component.drawText("Password");
        labelPassword.setFont(new Font(25));
        Text labelConfirmPassword = component.drawText("Confirm Password");
        labelConfirmPassword.setFont(new Font(25));
        TextField textName = component.drawTextField("");
        PasswordField textPassword = component.drawPasswordField("");
        PasswordField textConfirmPassword = component.drawPasswordField("");

        inputPane.add(labelUserName, 0, 0);
        inputPane.add(textName, 1, 0);
        inputPane.add(labelPassword, 0, 1);
        inputPane.add(textPassword, 1, 1);
        inputPane.add(labelConfirmPassword, 0, 2);
        inputPane.add(textConfirmPassword, 1, 2);

        textName.setPrefSize(250, 50);
        textName.setPadding(new Insets(10, 20, 10, 20));
        textName.setFont(new Font(20));
        textName.setStyle("-fx-background-color: rgba(200,200,200,0);" +
                "-fx-border-color: rgba(115,177,224,0.8);" +
                "-fx-border-radius: 20;" +
                "-fx-border-width: 3;" +
                "-fx-text-fill: black");

        textPassword.setPrefSize(250, 50);
        textPassword.setPadding(new Insets(10, 20, 10, 20));
        textPassword.setFont(new Font(20));
        textPassword.setStyle("-fx-background-color: rgba(200,200,200,0);" +
                "-fx-border-color: rgba(115,177,224,0.8);" +
                "-fx-border-radius: 20;" +
                "-fx-border-width: 3;" +
                "-fx-text-fill: black");

        textConfirmPassword.setPrefSize(250, 50);
        textConfirmPassword.setPadding(new Insets(10, 20, 10, 20));
        textConfirmPassword.setFont(new Font(20));
        textConfirmPassword.setStyle("-fx-background-color: rgba(200,200,200,0);" +
                "-fx-border-color: rgba(115,177,224,0.8);" +
                "-fx-border-radius: 20;" +
                "-fx-border-width: 3;" +
                "-fx-text-fill: black");

        HBox groupButton = new HBox();
        Button register = component.drawButton(groupButton, "Register");
        Button back = component.drawButton(groupButton, "Back");

        mainPane.setSpacing(30);
        inputPane.setHgap(20);
        inputPane.setVgap(20);
        inputPane.setAlignment(Pos.CENTER);
        groupButton.setAlignment(Pos.CENTER);
        groupButton.setSpacing(10);
        mainPane.getChildren().add(labelLogin);
        mainPane.getChildren().add(inputPane);
        mainPane.getChildren().add(groupButton);

        register.setOnMouseClicked(event -> {
            String name = textName.getText();
            String password = textPassword.getText();
            String passwordConfirm = textConfirmPassword.getText();
            if (password.equals(passwordConfirm)) {
                if (userController.register(name, password)) {
                    drawResult("Successful register", true);
                } else {
                    drawResult("Failed register", false);
                }
            } else {
                drawResult("Password is not correct", false);
            }
        });

        back.setOnMouseClicked(event -> {
            drawLogin();
        });
    }
}
