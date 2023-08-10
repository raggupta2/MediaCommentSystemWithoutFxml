package sample.view;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sample.Common;
import sample.controller.MediaController;
import sample.model.MediaInformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class Component {

    private MediaController mediaController;

    public Component() {
        mediaController = new MediaController();
    }

    /**
     * insert a new text in grid pane and return text created
     *
     * @param str string to insert
     * @return created text
     */
    public Text drawText(String str) {
        Text text = new Text(str);
        return text;
    }

    /**
     * insert two new text in grid pane and return second text created
     *
     * @param gridPane pane to insert
     * @param str1     first string to insert
     * @param str2     second string to insert
     * @return second text created
     */
    public Text drawTwoText(GridPane gridPane, String str1, String str2) {
        Text text1 = new Text(str1);
        Text text2 = new Text(str2);
        text2.setFont(new Font(15));
        text2.setFill(Color.rgb(50, 50, 200));
        gridPane.addColumn(0, text1);
        gridPane.addColumn(1, text2);
        return text2;
    }

    /**
     * insert a new textfield in grid pane and return textfield created
     *
     * @param str string to insert
     * @return created textfield
     */
    public TextField drawTextField(String str) {
        TextField textField = new TextField(str);
        return textField;
    }

    /**
     * insert two new text in grid pane and return second text created
     *
     * @param gridPane pane to insert
     * @param str1     first string to insert
     * @param str2     second string to insert
     * @return second text created
     */
    public TextField drawTextAndTextfield(GridPane gridPane, String str1, String str2, boolean isEditable) {
        Text text1 = new Text(str1);
        TextField text2 = new TextField(str2);
        text1.setFont(new Font(12));
        text2.setFont(new Font(15));
        text2.setEditable(isEditable);
        if (isEditable) {

        } else {
            text2.setStyle("-fx-text-fill: rgb(50,50,200);-fx-background-color: rgb(200,200,200,0.4)");
        }
        gridPane.addColumn(0, text1);
        gridPane.addColumn(1, text2);
        return text2;
    }

    /**
     * insert a new textfield in grid pane and return textfield created
     *
     * @param str string to insert
     * @return created textfield
     */
    public TextArea drawTextArea(String str) {
        TextArea t = new TextArea(str);
        return t;
    }

    /**
     * insert a new password field in grid pane and return  password field created
     *
     * @param str string to insert
     * @return created  password field
     */
    public PasswordField drawPasswordField(String str) {
        PasswordField passwordField = new PasswordField();
        return passwordField;
    }

    /**
     * insert a new button in grid pane and return button created
     *
     * @param hBox hbox to insert
     * @param str  string to insert
     * @return created button
     */
    public Button drawButton(HBox hBox, String str) {
        Button button = new Button(str);
        button.setPrefWidth(120);
        button.setPrefHeight(30);
        hBox.getChildren().add(button);
        return button;
    }

    public Button drawImageButton(HBox hBox, String url) {
        Button button = new Button();
        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream(url)));
            imageView.setFitWidth(Common.buttonImageSize);
            imageView.setFitHeight(Common.buttonImageSize);
            button.setGraphic(imageView);
            button.setPadding(Insets.EMPTY);
            button.setStyle("-fx-background-color: transparent");
            button.setPrefWidth(0);
            button.setPrefHeight(0);
            hBox.getChildren().add(button);
        } catch (FileNotFoundException e) {
           System.out.println(e.getMessage());
        }
        return button;
    }

    /**
     * insert a new image view in grid pane and return image view created
     *
     * @param imgUrl string url of image file
     * @return created image view
     */
    public ImageView drawImage(String imgUrl) {
        ImageView imageView = new ImageView();
        try {
            Image image = new Image(new FileInputStream(imgUrl));
            imageView = new ImageView(image);
        } catch (FileNotFoundException e) {
            //System.out.println(e.getMessage());
        }
        return imageView;
    }

    /**
     * insert a new image view in grid pane and return image view created
     *
     * @param fullPath string url of image file
     * @return created image view
     */
    public ImageView drawIcon(String fullPath) {
        ImageView imageView = null;
        try {
            File file = new File(fullPath);
            if (file.exists()) {
                String extension = file.getName().substring(file.getName().indexOf(".") + 1);
                if (Arrays.asList(Common.extensionsImageExtra).contains(extension.toLowerCase())) {
                    String tempFileName = mediaController.getJpegFromHEIC(fullPath);
                    if (tempFileName != null) {
                        file = new File(mediaController.getJpegFromHEIC(fullPath));
                    }
                } else if (Arrays.asList(Common.extensionsVideo).contains(extension.toLowerCase())) {
                    file = new File(Common.iconVideo);
                }
                imageView = new ImageView(new Image(new FileInputStream(file)));
            } else {
                imageView = new ImageView(new Image(new FileInputStream(Common.iconMissMedia)));
            }
            imageView.setFitHeight(Common.iconHeight);
            imageView.setFitWidth(Common.iconWidth);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return imageView;
    }

    /**
     * insert file name and corresponding operation in grid pane
     *
     * @param mediaInformation file info to insert
     * @return grid pane to insert to list view
     */
    public StackPane drawItem(MediaInformation mediaInformation) {
        final StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_LEFT);
        String fullPath = mediaInformation.getLocation() + (mediaInformation.getLocation().endsWith(Common.childSlash) ? "" : Common.childSlash) + mediaInformation.getName();
        final ImageView icon = drawIcon(fullPath);
        final Label text = new Label(mediaInformation.getName());
        text.setMaxWidth(120);
        text.setTextFill(Color.rgb(50, 200, 50));
        stackPane.getChildren().add(icon);
        stackPane.getChildren().add(text);
        StackPane.setMargin(text, new Insets(0, 0, 0, 20));
        if (!mediaController.existFile(mediaInformation)) {
            text.setTextFill(Color.rgb(200, 0, 0));

            final ImageView loadingImg = drawIcon(Common.iconLoadingSearch);
            stackPane.getChildren().add(loadingImg);
            StackPane.setAlignment(loadingImg, Pos.CENTER_RIGHT);

            final Text searchFix = new Text("Search&Fix");
            searchFix.setFill(Color.rgb(0, 0, 200));
            stackPane.getChildren().add(searchFix);
            StackPane.setAlignment(searchFix, Pos.CENTER_RIGHT);

            loadingImg.setVisible(false);
            searchFix.setVisible(true);

            if (mediaController.getMediaInfosSearching().get(fullPath) != null) {
                ArrayList<Object> state = mediaController.getMediaInfosSearching().get(fullPath);
                stackPane.getChildren().set(0, (Node) state.get(0));
                stackPane.getChildren().set(1, (Node) state.get(1));
                stackPane.getChildren().set(2, (Node) state.get(2));
                searchFix.setVisible(false);
                loadingImg.setVisible(true);
            }
            searchFix.setOnMouseClicked(event -> {
                searchFix.setVisible(false);
                loadingImg.setVisible(true);
                RotateTransition rotateTransition = makeRotationOfImage(loadingImg);
                rotateTransition.play();

                ArrayList<Object> list = new ArrayList<>();
                list.add(icon);
                list.add(text);
                list.add(loadingImg);
                mediaController.addMediaInfosSearching(fullPath, list);

                LoadingService loadingService = new LoadingService();
                loadingService.setListener(new LoadingService.Listener() {
                    @Override
                    public void before() {
                        boolean isMoved = mediaController.searchAndFix(mediaInformation);
                        if (isMoved) {
                            rotateTransition.stop();
                            text.setTextFill(Color.rgb(50, 200, 50));
                            try {
                                icon.setImage(new Image(new FileInputStream(fullPath)));
                            } catch (FileNotFoundException e) {
                               System.out.println(e.getMessage());
                            }
                            loadingImg.setVisible(false);
                        } else {
                            loadingImg.setVisible(false);
                            searchFix.setVisible(true);
                        }
                    }

                    @Override
                    public void body() {

                    }

                    @Override
                    public void after() {

                    }
                });
                loadingService.service();
            });
            searchFix.setOnMouseEntered(event -> {
                searchFix.setFill(Color.rgb(0, 0, 0));
                searchFix.setUnderline(true);
            });
            searchFix.setOnMouseExited(event -> {
                searchFix.setFill(Color.rgb(0, 0, 200));
                searchFix.setUnderline(false);
            });
        } else if (!mediaController.existMetadata(mediaInformation)) {
            text.setTextFill(Color.rgb(0, 0, 0));
        } else if (!mediaController.isEqualChecksum(mediaInformation)) {
            text.setTextFill(Color.rgb(255, 150, 0));
        }
        return stackPane;
    }

    /**
     * make rotation of image view
     *
     * @param i image to make rotation
     * @return rotation transition of image
     */
    public RotateTransition makeRotationOfImage(ImageView i) {
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.millis(1000));
        rotateTransition.setNode(i);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        return rotateTransition;
    }
}
