package sample.view;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import sample.Common;
import sample.controller.FileController;
import sample.controller.MediaController;
import sample.controller.UserController;
import sample.model.MediaInformation;
import sample.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class ImageManagement {

    private final Component component;

    private final VBox mainPane;
    private HBox controlPane;
    private HBox listAndDetailPane;
    private VBox listPane;
    private VBox detailPane;
    private final MediaController mediaController;

    private Button open;
    private ComboBox<String> sort;
    private Button createRepo;
    private Button delMetaData;
    private Button saveMetaData;

    private Button back;
    private Button next;
    private Text locationView;
    private ListView listView;

    private TextField lastModifier;
    private TextField latitude;
    private TextField longitude;
    private TextField googleMapUrl;
    private TextField originalFileSize;
    private TextField currentFileSize;
    private TextField checksum;
    private TextArea description;
    private ImageView imageView;

    private final User currentUser;
    private String openedLocation;
    private int openedLocationIndex = -1;
    private MediaInformation selectedFile;
    private String selectedSortType;


    public ImageManagement(VBox gridPane, User user) {
        this.mediaController = new MediaController();
        this.component = new Component();
        this.mainPane = gridPane;
        this.currentUser = user;
        mainPane.setSpacing(20);
        mainPane.setAlignment(Pos.TOP_CENTER);
        drawMediaManagement();

     /*   openedLocation = "F:\\z---exam";
        updateList(openedLocation);*/
    }

    /**
     * draw media management page
     */
    private void drawMediaManagement() {
        mainPane.getChildren().clear();
        String url = "file:" + FileController.getValidUrl(Common.backgroundMain);
        mainPane.setStyle(
                "-fx-background-image: url(" + url + ");" +
                        "-fx-background-repeat: no-repeat;" +
                        "-fx-background-position: center;" +
                        "-fx-background-size: stretch;"
        );
        drawMenu();
        drawControlPane();
        drawListPane();
        drawDetailPane();
    }

    /**
     * draw menu
     */
    private void drawMenu() {
        MenuBar menuBar = new MenuBar();

        Menu menu = new Menu("File");
        MenuItem log_out = new MenuItem("Log out");
        MenuItem exit = new MenuItem("Exit");
        menu.getItems().add(log_out);
        menu.getItems().add(exit);

        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        help.getItems().add(about);

        menuBar.getMenus().add(menu);
        menuBar.getMenus().add(help);

        mainPane.getChildren().add(menuBar);

        log_out.setOnAction(event -> {
            new Login(mainPane);
        });
        exit.setOnAction(event -> {
            Window window = mainPane.getScene().getWindow();
            Stage s = (Stage) window;
            s.close();
        });
        about.setOnAction(event -> {
            Stage stage = new Stage();
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(new Label(Common.aboutString));
            stage.setTitle("About");
            stage.setScene(new Scene(vBox, 300, 200));
            stage.show();
        });
    }

    /**
     * draw control part of media management
     */
    private void drawControlPane() {
        controlPane = new HBox();
        controlPane.setAlignment(Pos.CENTER);
        controlPane.setSpacing(10);
        open = component.drawButton(controlPane, "Open");

        sort = new ComboBox<>();
        sort.setPrefWidth(120);
        sort.setPrefHeight(30);
        sort.getItems().addAll(Common.sortTypes);
        selectedSortType = Common.sortTypes[0];
        sort.setValue(selectedSortType);

        controlPane.getChildren().add(sort);
        createRepo = component.drawButton(controlPane, "Create Repo");
        delMetaData = component.drawButton(controlPane, "Delete Metadata");
        saveMetaData = component.drawButton(controlPane, "Save Metadata");
        controlPane.getChildren().add(new Text("User : " + currentUser.getName()));

        createRepo.setDisable(true);
        delMetaData.setDisable(true);
        saveMetaData.setDisable(true);

        open.setOnAction(event -> {
            open(event);
        });

        createRepo.setOnMouseClicked(event -> {
            boolean isCreated = mediaController.createRepo(openedLocation);
            if (isCreated) {
                createRepo.setDisable(mediaController.existICS(openedLocation));
            }
        });

        delMetaData.setOnMouseClicked(event -> {
            boolean isDeleted = mediaController.deleteMetadata(selectedFile);
            if (isDeleted) {
                updateList(openedLocation);
            }
        });

        saveMetaData.setOnMouseClicked(event -> {
            selectedFile.setModifier(currentUser.getName());
            selectedFile.setModificationTime(new Date());
            selectedFile.setDescription(description.getText());
            selectedFile.setLatitude(latitude.getText());
            selectedFile.setLongitude(longitude.getText());
            boolean isSaved = mediaController.saveMetadata(selectedFile);
            if (isSaved) {
                updateList(openedLocation);
            }
        });

        sort.setOnAction(event -> {
            selectedSortType = sort.getValue();
            updateList(openedLocation);
        });
        mainPane.getChildren().add(controlPane);
    }

    private void open(Event event) {
        Window window = ((Node) (event.getSource())).getScene().getWindow();
        DirectoryChooser chooser = new DirectoryChooser();
        if (openedLocation != null) {
            chooser.setInitialDirectory(new File(openedLocation));
        }
        File selectDialog = chooser.showDialog(window);
        if (selectDialog != null) {
            openedLocation = selectDialog.getAbsolutePath();
            gotoNewLocation();
        }
    }

    /**
     * draw list part of media management
     */
    private void drawListPane() {
        listAndDetailPane = new HBox();
        listPane = new VBox();
        listAndDetailPane.getChildren().add(listPane);
        listAndDetailPane.setAlignment(Pos.TOP_CENTER);
        listAndDetailPane.setSpacing(20);
        mainPane.getChildren().add(listAndDetailPane);

        listPane.setSpacing(5);
        HBox locationPane = new HBox();
        locationView = new Text("");
        back = component.drawImageButton(locationPane, Common.iconBack);
        next = component.drawImageButton(locationPane, Common.iconNext);
        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(0, 0, 0, 10));
        stackPane.getChildren().add(locationView);
        locationPane.getChildren().add(stackPane);
        back.setDisable(true);
        next.setDisable(true);
        back.setOnMouseClicked(event -> {
            openedLocationIndex--;
            if (openedLocationIndex <= 0) {
                back.setDisable(true);
            }
            next.setDisable(false);
            openedLocation = mediaController.getLocations().get(openedLocationIndex);
            updateList(openedLocation);
        });
        next.setOnMouseClicked(event -> {
            openedLocationIndex++;
            if (openedLocationIndex >= mediaController.getLocations().size() - 1) {
                next.setDisable(true);
            }
            back.setDisable(false);
            openedLocation = mediaController.getLocations().get(openedLocationIndex);
            updateList(openedLocation);
        });
        listView = new ListView<>();
        listPane.getChildren().add(locationPane);
        listPane.getChildren().add(listView);



        locationView.setFont(new Font(12));
        locationView.setFill(Color.rgb(0, 0, 0));
    }

    /**
     * draw detail part of media management
     */
    private void drawDetailPane() {
        detailPane = new VBox();
        listAndDetailPane.getChildren().add(detailPane);

        detailPane.setSpacing(10);
        HBox topPane = new HBox();
        GridPane topLeftPane = new GridPane();

        topPane.setSpacing(20);
        topLeftPane.setVgap(10);
        topLeftPane.setHgap(10);

        lastModifier = component.drawTextAndTextfield(topLeftPane, "Last Modifier : ", "");
        latitude = component.drawTextAndTextfield(topLeftPane, "Latitude : ", "");
        longitude = component.drawTextAndTextfield(topLeftPane, "Longitude : ", "");
        latitude.setEditable(true);
        longitude.setEditable(true);
        googleMapUrl = component.drawTextAndTextfield(topLeftPane, "Google map url : ", "");
        originalFileSize = component.drawTextAndTextfield(topLeftPane, "Original Size : ", "");
        currentFileSize = component.drawTextAndTextfield(topLeftPane, "Current Size : ", "");
        checksum = component.drawTextAndTextfield(topLeftPane, "Checksum : ", "");
        imageView = component.drawImage("");
        description = component.drawTextArea("");
        description.setPrefHeight(150);

        detailPane.getChildren().add(topPane);
        detailPane.getChildren().add(component.drawText("Description"));
        detailPane.getChildren().add(description);
        topPane.getChildren().add(topLeftPane);
        topPane.getChildren().add(imageView);
    }

    /**
     * update list by open or sort operation and show that
     *
     * @param location specific location
     */
    private void updateList(String location) {
        emptyDetail();
        createRepo.setDisable(mediaController.existICS(location));
        saveMetaData.setDisable(true);
        listView.getItems().clear();
        locationView.setText(mediaController.getSimpleLocation(openedLocation));
        ArrayList<MediaInformation> mediaInfos = mediaController.getMediaInfos(location, selectedSortType);
        ArrayList<String> directories = mediaController.getDirectoryList(location, selectedSortType);

        if (mediaInfos.size() > 0) {
            selectedFile = mediaInfos.get(0);
            updateDetail(selectedFile);
            listView.getSelectionModel().select(0);
        }
        for (int i = 0; i < mediaInfos.size(); i++) {
            MediaInformation mediaInformation = mediaInfos.get(i);
            StackPane item = component.drawItem(mediaInformation);
            listView.getItems().add(item);
            // gridPane.setStyle("-fx-background-color: aquamarine");
            int index = i;
            item.setOnMouseClicked(event -> {
                selectedFile = mediaInformation;
                if (currentUser.getRole() == 0) {
                    delMetaData.setDisable(mediaController.existFile(mediaInformation));
                }
                updateDetail(mediaInfos.get(index));
            });
        }

        for (int i = 0; i < directories.size(); i++) {
            String directory = directories.get(i);
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER_LEFT);
            ImageView icon = component.drawIcon(Common.iconFolder, true);
            stackPane.getChildren().add(icon);
            Label text = new Label(directory);
            text.setMaxWidth(120);
            stackPane.getChildren().add(text);
            listView.getItems().add(stackPane);
            StackPane.setMargin(text, new Insets(0, 0, 0, 20));
            stackPane.setOnMouseClicked(event -> {
                openedLocation += (openedLocation.endsWith("\\") ? "" : Common.childSlash) + directory;
                gotoNewLocation();
            });
        }
    }

    /**
     * empty content of detail pane
     */
    private void emptyDetail() {
        lastModifier.setText("");
        latitude.setText("");
        longitude.setText("");
        googleMapUrl.setText("");
        originalFileSize.setText("");
        currentFileSize.setText("");
        description.setText("");
        imageView.setImage(null);
        checksum.setText("");
    }

    /**
     * update detail of file and show that
     *
     * @param mediaInformation info to show
     */
    private void updateDetail(MediaInformation mediaInformation) {
        emptyDetail();
        saveMetaData.setDisable(!mediaController.existICS(openedLocation));
        if (currentUser.getRole() == 0) {
            delMetaData.setDisable(mediaController.existFile(mediaInformation));
        }
        lastModifier.setText(mediaInformation.getModifier());
        latitude.setText(String.valueOf(mediaInformation.getLatitude()));
        longitude.setText(String.valueOf(mediaInformation.getLongitude()));
        googleMapUrl.setText(mediaInformation.getGoogleMapUrl());
        originalFileSize.setText(mediaInformation.getOriginalFileSize() + " bytes");
        currentFileSize.setText(mediaInformation.getCurrentFileSize() + " bytes");
        description.setText(mediaInformation.getDescription());
        checksum.setText(mediaInformation.getCheckSum());
        try {
            String url = mediaInformation.getLocation() + Common.childSlash + mediaInformation.getName();
            Image image = new Image(new FileInputStream(url));
            double rateOfResolution = image.getWidth() / image.getHeight();
            double rateOfImageView = Common.mediaWidth / Common.mediaHeight;
            if (rateOfResolution > rateOfImageView) {
                imageView.setFitWidth(Common.mediaWidth);
                imageView.setFitHeight(Common.mediaWidth / rateOfResolution);
            } else {
                imageView.setFitHeight(Common.mediaHeight);
                imageView.setFitWidth(Common.mediaHeight * rateOfResolution);
            }
            imageView.setImage(new Image(new FileInputStream(url)));
            imageView.setOnMouseClicked(event -> {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        try {
                            String fileName = url;
                            String[] commands = {
                                    "cmd.exe", "/c", "start", "\"DummyTitle\"", "\"" + fileName + "\""
                            };
                            Process process = Runtime.getRuntime().exec(commands);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (FileNotFoundException e) {
            //  e.printStackTrace();
        }
    }

    /**
     * go to new location by open or click a directory
     */
    private void gotoNewLocation() {
        boolean isAdded = mediaController.addLocation(openedLocation, openedLocationIndex);
        if (isAdded) {
            openedLocationIndex++;
            next.setDisable(true);
        }
        if (mediaController.getLocations().size() > 1) {
            back.setDisable(false);
        }
        updateList(openedLocation);
    }

}
