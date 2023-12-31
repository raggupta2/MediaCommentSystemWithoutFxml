package mcs.view;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
import mcs.Common;
import mcs.controller.MediaController;
import mcs.model.MediaInformation;
import mcs.utils.DateTimeUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class Component {

    private boolean isMoved = false;
    private MediaController mediaController;
    private DateTimeUtil dateTimeUtil;

    public Component() {
        mediaController = new MediaController();
        dateTimeUtil = new DateTimeUtil();
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
        TextArea textArea = new TextArea(str);
        return textArea;
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
    public StackPane drawItem(MediaInformation mediaInformation, String sortType) {
        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER_LEFT);
        //   String fullPath = mediaInformation.getLocation() + (mediaInformation.getLocation().endsWith(Common.childSlash) ? "" : Common.childSlash) + mediaInformation.getName();
        String fullPath = mediaController.getFullPath(mediaInformation);
        String iconPath = fullPath;
        if (mediaController.isImage(mediaInformation)) {
            iconPath = Common.iconImage;
        } else if (mediaController.isVideo(mediaInformation)) {
            iconPath = Common.iconVideo;
        }
        final ImageView icon = drawIcon(iconPath);
        final Label fileName = new Label(mediaInformation.getName());
        final Label property = new Label();
        final ImageView searchFix = drawIcon(Common.iconSearch);
        final ImageView loadingImg = drawIcon(Common.iconLoadingSearch);

        searchFix.setCursor(Cursor.HAND);

        searchFix.setVisible(false);
        loadingImg.setVisible(false);

        StackPane.setAlignment(searchFix, Pos.CENTER_RIGHT);
        StackPane.setAlignment(loadingImg, Pos.CENTER_RIGHT);
        property.setAlignment(Pos.CENTER_RIGHT);
        RotateTransition rotateTransition = makeRotationOfImage(loadingImg);

        stackPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            fileName.setMaxWidth((newValue.doubleValue() - 40) / 1.5);
            property.setMaxWidth((newValue.doubleValue() - 40) / 3);
        });
        //  property.setStyle("-fx-background-color: red");

        fileName.setTextFill(Color.rgb(50, 200, 50));
        stackPane.getChildren().add(icon);
        stackPane.getChildren().add(fileName);
        stackPane.getChildren().add(property);
        stackPane.getChildren().add(searchFix);
        stackPane.getChildren().add(loadingImg);
        StackPane.setMargin(fileName, new Insets(0, 0, 0, 20));
        StackPane.setMargin(property, new Insets(0, 20, 0, 0));
        StackPane.setAlignment(property, Pos.CENTER_RIGHT);

        if (sortType.equals(Common.sortTypes[0])) {
            String dateTime = dateTimeUtil.convertDateTimeFormat(mediaInformation.getTakenTime(), "dd-MM-yyyy HH:mm:ss");
            property.setText(dateTime);
        } else if (sortType.equals(Common.sortTypes[1])) {
            property.setText(mediaController.fromSizeToString(mediaInformation.getOriginalFileSize()));
        } else if (sortType.equals(Common.sortTypes[2])) {
            property.setText(mediaInformation.getExtension());
        }

        if (!mediaController.existFile(mediaController.getFullPath(mediaInformation))) {
            fileName.setTextFill(Color.rgb(200, 0, 0));
            if (mediaController.getMediaInfosSearching().get(fullPath) != null) {
                stackPane.getChildren().set(0, (Node) mediaController.getMediaInfosSearching().get(fullPath).get(0));
                stackPane.getChildren().set(1, (Node) mediaController.getMediaInfosSearching().get(fullPath).get(1));
                stackPane.getChildren().set(2, (Node) mediaController.getMediaInfosSearching().get(fullPath).get(2));
                stackPane.getChildren().set(3, (Node) mediaController.getMediaInfosSearching().get(fullPath).get(3));
                stackPane.getChildren().set(4, (Node) mediaController.getMediaInfosSearching().get(fullPath).get(4));
            } else {
                searchFix.setVisible(true);
                loadingImg.setVisible(false);
            }

            searchFix.setOnMouseClicked(event -> {
                rotateTransition.play();
                searchFix.setVisible(false);
                loadingImg.setVisible(true);

                ArrayList<Object> list = new ArrayList<>();
                list.add(icon);
                list.add(fileName);
                list.add(property);
                list.add(searchFix);
                list.add(loadingImg);
                mediaController.addMediaInfosSearching(mediaController.getFullPath(mediaInformation), list);

                LoadingService loadingService = new LoadingService();
                loadingService.setListener(new LoadingService.Listener() {
                    @Override
                    public void before() {
                        isMoved = mediaController.searchAndFix(mediaInformation);
                    }

                    @Override
                    public void body() {
                        loadingImg.setVisible(false);
                        if (isMoved) {
                            fileName.setTextFill(Color.rgb(50, 200, 50));
                            try {
                                icon.setImage(new Image(new FileInputStream(mediaController.getFullPath(mediaInformation))));
                            } catch (FileNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            searchFix.setVisible(true);
                        }
                    }

                    @Override
                    public void after() {
                        mediaController.removeMediaInfosSearching(mediaController.getFullPath(mediaInformation));
                        rotateTransition.stop();
                    }
                });
                loadingService.service();
            });
        } else if (!mediaController.existMetadata(mediaInformation)) {
            fileName.setTextFill(Color.rgb(0, 0, 0));
        } else if (!mediaController.isEqualChecksum(mediaInformation)) {
            fileName.setTextFill(Color.rgb(255, 150, 0));
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

    public Image getImage(String url) {
        Image image = null;
        try {
            image = new Image(new FileInputStream(url));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return image;
    }
}
