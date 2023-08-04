package sample.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import sample.Common;
import sample.model.MediaInformation;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class MediaController {

    private final FileController fileController;
    private FileSearchEngine fileSearchEngine;
    private ArrayList<String> locations;
    private HashMap<String, ArrayList<Object>> mediaInfosSearching;

    public MediaController() {
        fileController = new FileController();
        fileSearchEngine = new FileSearchEngine();
        locations = new ArrayList<>();
        mediaInfosSearching = new HashMap<>();
    }

    public boolean addLocation(String location, int index) {
        if (locations.size() > 0) {
            while (locations.size() > index + 1) {
                locations.remove(index + 1);
            }
            if (locations.get(locations.size() - 1).equals(location)) {
                return false;
            } else {
                return locations.add(location);
            }
        }
        return locations.add(location);
    }

    public ArrayList<String> getLocations() {
        return locations;
    }


    /**
     * this get all media information
     *
     * @param location location of specific folder
     * @param sortType type to sort: date:1, size:2, type:3
     * @return array list sorted
     */
    public ArrayList<MediaInformation> getMediaInfos(String location, String sortType) {
        ArrayList<MediaInformation> mediaInfos = new ArrayList<>();
        if (location != null) {
            String urlDirectory = location + Common.childSlash + Common.metadataFolderName;

            File[] filesMeta = new File(urlDirectory).listFiles();
            File[] filesNoMeta = new File(location).listFiles();

            ArrayList<String> filesInLocationMeta = getFileListFiltered(filesMeta, new String[]{Common.extensionsText});
            String[] arr = Arrays.copyOf(Common.extensionsImage, Common.extensionsImage.length + Common.extensionsVideo.length);
            System.arraycopy(Common.extensionsVideo, 0, arr, Common.extensionsImage.length, Common.extensionsVideo.length);
            ArrayList<String> filesInLocation = getFileListFiltered(filesNoMeta, arr);

            for (int i = 0; i < filesInLocation.size(); i++) {
                if (filesInLocationMeta.contains(filesInLocation.get(i) + "." + Common.extensionsText)) {
                    filesInLocation.remove(filesInLocation.get(i));
                    i--;
                }
            }
            mediaInfos = fileController.getLastInfosOfEachMedia(location, filesInLocationMeta, filesInLocation);
            mediaInfos.sort((o1, o2) -> {
                if (sortType.equals(Common.sortTypes[0])) {
                    return (int) (o1.getTakenTime().getTime() - o2.getTakenTime().getTime());
                } else if (sortType.equals(Common.sortTypes[1])) {
                    return (int) (o1.getOriginalFileSize() - o2.getOriginalFileSize());
                } else if (sortType.equals(Common.sortTypes[2])) {
                    return o1.getExtension().compareTo(o2.getExtension());
                }
                return 0;
            });
        }
        return mediaInfos;
    }

    /**
     * get directory list in specific files
     *
     * @param location location of specific folder
     * @param sortType type to sort: date:1, size:2, type:3
     * @return array list sorted
     */
    public ArrayList<String> getDirectoryList(String location, String sortType) {
        ArrayList<String> directories = new ArrayList<>();
        File[] files = new File(location).listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory() && !files[i].getName().equals(Common.metadataFolderName)) {
                directories.add(files[i].getName());
            }

        }
        return directories;
    }


    /**
     * create new folder .ics in specific folder and create corresponding .txt files
     *
     * @return if success to create, return true, or false
     */
    public boolean createRepo(String location) {
        String urlDirectory = location + Common.childSlash + Common.metadataFolderName;
        File directory = new File(urlDirectory);
        if (!directory.exists()) {
            return directory.mkdir();
        }
        return false;
    }

    /**
     * delete metadata of a file
     *
     * @param information currently selected info
     * @return if deleted, return true;
     */
    public boolean deleteMetadata(MediaInformation information) {
        String url = information.getLocation() + Common.childSlash + Common.metadataFolderName + Common.childSlash + information.getName() + "." + Common.extensionsText;
        File f = new File(url);
        return f.delete();
    }

    /**
     * save metadata of a file
     *
     * @param information current selected info
     * @return if saved, return true;
     */
    public boolean saveMetadata(MediaInformation information) {
        String fullPath = information.getLocation() + Common.childSlash + Common.metadataFolderName + Common.childSlash + information.getName() + "." + Common.extensionsText;
        if (!new File(fullPath).exists()) {
            try {
                new File(fullPath).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return fileController.recordInformation(fullPath, information);
    }

    /**
     * for media that have metadata and no media, find the media by checksum and move the media to corresponding directory
     *
     * @param mediaInformation info recorded in metadata
     * @return if find and move, return true
     */
    public boolean searchAndFix(MediaInformation mediaInformation) {
        //  String fullPathOfFileSearched = fileSearchEngine.searchFile(mediaInformation.getName().substring(0,mediaInformation.getName().length()-4));
        String fullPathOfFileSearched = fileSearchEngine.searchFile(mediaInformation.getName(), mediaInformation.getCheckSum());
        System.out.println(fullPathOfFileSearched);
        File sourceFile = new File(fullPathOfFileSearched);
        File createdFile = new File(mediaInformation.getLocation() + Common.childSlash + mediaInformation.getName());
        try {
            FileUtils.copyFile(sourceFile, createdFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public HashMap<String, ArrayList<Object>> getMediaInfosSearching() {
        return mediaInfosSearching;
    }

    public void addMediaInfosSearching(String fullPath, ArrayList<Object> list) {
        mediaInfosSearching.put(fullPath, list);
    }

    public void removeMediaInfosSearching(String fullPath) {
        mediaInfosSearching.remove(fullPath);
    }


    /**
     * whether it is image or not
     *
     * @param fullPath current selected file
     * @return if exist, true
     */
    public boolean isImage(String fullPath) {
        String extension = fullPath.substring(fullPath.length() - 3);
        return Arrays.asList(Common.extensionsImage).contains(extension);
    }

    /**
     * whether .ics directory exist in folder or not
     *
     * @param path current selected folder
     * @return if exist, true
     */
    public boolean existICS(String path) {
        File f = new File(path + Common.childSlash + Common.metadataFolderName);
        return f.exists() && f.isDirectory();
    }

    /**
     * whether file exist in folder or not
     *
     * @param information current selected info
     * @return if exist, true
     */
    public boolean existFile(MediaInformation information) {
        File f = new File(information.getLocation() + Common.childSlash + information.getName());
        return f.exists() && !f.isDirectory();
    }

    /**
     * whether metadata exist in folder or not
     *
     * @param information current selected info
     * @return if exist, true
     */
    public boolean existMetadata(MediaInformation information) {
        File f = new File(information.getLocation() + Common.childSlash + Common.metadataFolderName + Common.childSlash + information.getName() + "." + Common.extensionsText);
        return f.exists() && !f.isDirectory();
    }


    /**
     * get file list of specific extensions
     *
     * @param files      all files of various extensions
     * @param extensions specific extensions
     * @return matching files list
     */
    private ArrayList<String> getFileListFiltered(File[] files, String[] extensions) {
        List<String> list = Arrays.asList(extensions);
        ArrayList<String> filesInLocation = new ArrayList<>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isFile()) {
                    String extension = fileName.substring(fileName.length() - 3);
                    if (list.contains(extension)) {
                        filesInLocation.add(fileName);
                    }
                }
            }
        }
        return filesInLocation;
    }

    /**
     * when location is too long, get simple location
     *
     * @param location path of specific folder
     * @return simple location string
     */
    public String getSimpleLocation(String location) {
        if (location.length() > Common.locationLength) {
            return location.substring(0, Common.locationLength / 2) + "..." + location.substring(location.length() - Common.locationLength / 2);
        } else {
            return location;
        }
    }


}
