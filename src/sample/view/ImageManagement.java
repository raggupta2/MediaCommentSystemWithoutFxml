package sample.view;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;
import sample.Common;
import sample.controller.FileController;
import sample.controller.MediaController;
import sample.model.MediaInformation;
import sample.model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

public class ImageManagement {

    private final Component component;

    private final VBox mainPane;
    private HBox listAndDetailPane;
    private VBox listPane;
    private VBox detailPane;
    private ImageView imageView;
    private final MediaController mediaController;

    private Button open;
    private ComboBox<String> sort;
    private Button createRepo;
    private Button delMetaData;
    private Button saveMetaData;

    private Button back;
    private Button next;
    private TextField locationView;
    private ListView listView;

    private TextField lastModifier;
    private TextField latitude;
    private TextField longitude;
    private TextField googleMapUrl;
    private TextField originalFileSize;
    private TextField currentFileSize;
    private TextField checksum;
    private TextArea description;
    private final User currentUser;
    private String openedLocation;
    private int openedLocationIndex = -1;
    private MediaInformation selectedInfo;
    private String selectedSortType;


    public ImageManagement(VBox gridPane, User user) {
        this.mediaController = new MediaController();
        this.component = new Component();
        this.mainPane = gridPane;
        this.currentUser = user;
        mainPane.setSpacing(20);
        mainPane.setAlignment(Pos.TOP_CENTER);
        drawMediaManagement();

      /*  openedLocation = "F:\\z---exam";
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
            vBox.setPadding(new Insets(20, 0, 0, 30));

            Label title = new Label("Media Comment System");
            Label content = new Label("Runtime version : 17.0.3.1");
            Label footer = new Label("Built on August 2023");

            title.setFont(new Font(20));

            vBox.getChildren().add(title);
            vBox.getChildren().add(content);
            vBox.getChildren().add(footer);

            stage.setTitle("About");
            stage.setScene(new Scene(vBox, 300, 200));
            stage.show();
        });
    }

    /**
     * draw control part of media management
     */
    private void drawControlPane() {
        HBox locationPane = new HBox();
        locationView = new TextField("");
        locationView.setFont(new Font(12));
        locationView.setStyle("-fx-text-fill: rgb(0, 0, 0)");
        locationView.setEditable(false);
        //    locationView.setStyle("-fx-background-color: transparent");

        back = component.drawImageButton(locationPane, Common.iconBack);
        next = component.drawImageButton(locationPane, Common.iconNext);
        StackPane stackPane = new StackPane();
        HBox.setHgrow(stackPane, Priority.ALWAYS);
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

        HBox controlPane = new HBox();
        controlPane.setSpacing(10);
        controlPane.setAlignment(Pos.CENTER_LEFT);
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
                createRepo.setDisable(mediaController.getSplitLocationByIcs(openedLocation) != null);
                saveMetaData.setDisable(mediaController.getSplitLocationByIcs(openedLocation) == null);
            }
        });

        delMetaData.setOnMouseClicked(event -> {
            boolean isDeleted = mediaController.deleteMetadata(selectedInfo);
            if (isDeleted) {
                updateList(openedLocation);
            }
        });

        saveMetaData.setOnMouseClicked(event -> {
            selectedInfo.setModifier(currentUser.getName());
            selectedInfo.setModificationTime(new Date().toString());
            selectedInfo.setDescription(description.getText());
            selectedInfo.setLatitude(latitude.getText());
            selectedInfo.setLongitude(longitude.getText());
            boolean isSaved = mediaController.saveMetadata(selectedInfo);
            if (isSaved) {
                updateList(openedLocation);
            }
        });

        sort.setOnAction(event -> {
            selectedSortType = sort.getValue();
            updateList(openedLocation);
        });
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(0, 20, 0, 20));
        vBox.setSpacing(10);
        vBox.getChildren().addAll(locationPane, controlPane);
        mainPane.getChildren().add(vBox);
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

        listView = new ListView<>();
        listPane.getChildren().add(listView);


        HBox.setHgrow(listPane, Priority.ALWAYS);
        listView.setMaxWidth(Double.MAX_VALUE);
        listPane.setMaxWidth(300);
        listView.setPrefHeight(10000);
        listAndDetailPane.setPadding(new Insets(0, 20, 20, 20));
    }

    /**
     * draw detail part of media management
     */
    private void drawDetailPane() {
        detailPane = new VBox();
        listAndDetailPane.getChildren().add(detailPane);

        detailPane.setSpacing(10);
        HBox topBox = new HBox();
        GridPane topLeftPane = new GridPane();

        topBox.setSpacing(20);
        topLeftPane.setVgap(10);
        topLeftPane.setHgap(10);

        lastModifier = component.drawTextAndTextfield(topLeftPane, "Last Modifier : ", "", false);
        latitude = component.drawTextAndTextfield(topLeftPane, "Latitude : ", "", true);
        longitude = component.drawTextAndTextfield(topLeftPane, "Longitude : ", "", true);
        googleMapUrl = component.drawTextAndTextfield(topLeftPane, "Google map url : ", "", false);
        originalFileSize = component.drawTextAndTextfield(topLeftPane, "Original Size : ", "", false);
        currentFileSize = component.drawTextAndTextfield(topLeftPane, "Current Size : ", "", false);
        checksum = component.drawTextAndTextfield(topLeftPane, "Checksum : ", "", false);
        imageView = component.drawImage("");
        description = component.drawTextArea("");

        VBox vBox = new VBox();
        vBox.getChildren().addAll(component.drawText("Description"), description);
        detailPane.getChildren().add(topBox);
        detailPane.getChildren().add(vBox);
        topBox.getChildren().add(topLeftPane);
        topBox.getChildren().add(imageView);

        HBox.setHgrow(detailPane, Priority.ALWAYS);
        VBox.setVgrow(detailPane, Priority.ALWAYS);
        description.setPrefHeight(1000);
        description.setWrapText(true);
    }

    /**
     * update list by open or sort operation and show that
     *
     * @param location specific location
     */
    private void updateList(String location) {
        emptyDetail();
        createRepo.setDisable(mediaController.getSplitLocationByIcs(location) != null);
        saveMetaData.setDisable(true);
        listView.getItems().clear();
        locationView.setText(openedLocation);

        LoadingService loadingService = new LoadingService();
        loadingService.service();
        //      loadingService.setListener(() -> {
        ArrayList<MediaInformation> mediaInfos = mediaController.getMediaInfos(location, selectedSortType);
        ArrayList<String> directories = mediaController.getDirectoryList(location, selectedSortType);

        if (!mediaInfos.isEmpty()) {
            selectedInfo = mediaInfos.get(0);
            updateDetail(selectedInfo);
            listView.getSelectionModel().select(0);
        }
        for (int i = 0; i < mediaInfos.size(); i++) {
            MediaInformation mediaInformation = mediaInfos.get(i);
            StackPane item = component.drawItem(mediaInformation);
            listView.getItems().add(item);
            int index = i;
            item.setOnMouseClicked(event -> {
                selectedInfo = mediaInformation;
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
        //    });
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
        saveMetaData.setDisable(mediaController.getSplitLocationByIcs(openedLocation) == null);
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
            String url = mediaInformation.getLocation() + (mediaInformation.getLocation().endsWith(Common.childSlash) ? "" : Common.childSlash) + mediaInformation.getName();

            File file = new File(url);
            String extension = file.getName().substring(file.getName().indexOf(".") + 1);
            if (Arrays.asList(Common.extensionsImageExtra).contains(extension.toLowerCase())) {
                String tempUrl = mediaController.getJpegFromHEIC(url);
                if (tempUrl == null) {
                    drawError("The Temp directory does not exist");
                } else {
                    file = new File(tempUrl);
                }
            }

            Image image = new Image(new FileInputStream(file));
            imageView.setImage(image);
            imageView.setCursor(Cursor.HAND);
            imageView.setPreserveRatio(true);

            imageView.setFitWidth(mainPane.widthProperty().doubleValue() / 3);
            mainPane.widthProperty().addListener((observable, oldValue, newValue) -> {
                imageView.setFitWidth(newValue.doubleValue() / 3);
            });

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
            //     e.printStackTrace();
        }
    }


    /**
     * draw error pane
     *
     * @param str string to show
     */
    private void drawError(String str) {
        mainPane.getChildren().clear();
        VBox box = new VBox();
        box.setSpacing(30);
        box.setPrefHeight(2000);
        box.setAlignment(Pos.CENTER);
        mainPane.getChildren().add(box);
        Label label = new Label(str);
        label.setFont(new Font(30));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        Button btn_ok = component.drawButton(hBox, "OK");

        box.getChildren().add(label);
        box.getChildren().add(hBox);
        btn_ok.setOnMouseClicked(event -> {
            System.exit(0);
        });

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
