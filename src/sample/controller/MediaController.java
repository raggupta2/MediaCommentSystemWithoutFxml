package sample.controller;


import org.apache.commons.io.FileUtils;
import sample.Common;
import sample.model.MediaInformation;
import sample.utils.DateTimeUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;


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
            File[] filesNoMeta = new File(location).listFiles();
            ArrayList<String> filesInLocation = getFileListFiltered(filesNoMeta, Common.extensionsImage, Common.extensionsVideo);

            String[] str = getSplitLocationByIcs(location);
            if (str != null) {
                String locationMeta = str[0] + Common.childSlash + Common.metadataFolderName + str[1];
                File[] filesMeta = new File(locationMeta).listFiles();
                ArrayList<String> filesInLocationMeta = getFileListFiltered(filesMeta, new String[]{Common.extensionsText}, new String[]{});
                mediaInfos = fileController.getLastInfosOfEachMedia(locationMeta, filesInLocationMeta);

                for (int i = 0; i < filesInLocation.size(); i++) {
                    if (filesInLocationMeta.contains(filesInLocation.get(i) + "." + Common.extensionsText)) {
                        filesInLocation.remove(filesInLocation.get(i));
                        i--;
                    }
                }
            }

            for (int i = 0; i < filesInLocation.size(); i++) {
                String url = location + Common.childSlash + filesInLocation.get(i);
                MediaInformation mediaInformation = fileController.getInfoFromImage(url);

                mediaInformation.setName(filesInLocation.get(i));
                mediaInformation.setExtension(filesInLocation.get(i).substring(filesInLocation.get(i).lastIndexOf(".") + 1));
                mediaInformation.setLocation(location);
                mediaInformation.setDescription("");
                mediaInformation.setModifier("");
                mediaInformation.setModificationTime("");
                mediaInformation.setCheckSum(fileController.getCheckSum(getFullPath(mediaInformation)));

                mediaInfos.add(mediaInformation);
            }

            mediaInfos.sort((o1, o2) -> {
                if (sortType.equals(Common.sortTypes[0])) {
                    return o2.getTakenTime().compareTo(o1.getTakenTime());
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

    public MediaInformation getMediaInfoOfFile(String url){
        return fileController.getInfoFromImage(url);
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
        String strCurrentDate = new Date().toString();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        information.setModificationTime(new DateTimeUtil().getDateTimeConvertedFormat(dtf.format(now),"yyyy/MM/dd HH:mm:ss","yyyy:MM:dd HH:mm:ss"));
        String[] str = getSplitLocationByIcs(information.getLocation());
        String[] foldersUnderIcs = str[1].replace(Common.childSlash, ":").split(":");
        String urlDirectory = str[0] + Common.childSlash + Common.metadataFolderName;
        for (int i = 0; i < foldersUnderIcs.length; i++) {
            urlDirectory += Common.childSlash + foldersUnderIcs[i];
            File directory = new File(urlDirectory);
            if (!directory.exists()) {
                directory.mkdir();
            }
        }
        String fullPath = urlDirectory + Common.childSlash + information.getName() + "." + Common.extensionsText;
        if (!new File(fullPath).exists()) {
            try {
                new File(fullPath).createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
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
        String fullPathOfFileSearched = fileSearchEngine.searchFile(mediaInformation.getName(), mediaInformation.getCheckSum());
      //  System.out.println(fullPathOfFileSearched);
        File sourceFile = new File(fullPathOfFileSearched);
        File createdFile = new File(getFullPath(mediaInformation));
        try {
            FileUtils.copyFile(sourceFile, createdFile);
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public HashMap<String, ArrayList<Object>> getMediaInfosSearching() {
        return mediaInfosSearching;
    }

    public void addMediaInfosSearching(String fullPath, ArrayList<Object> list) {
        mediaInfosSearching.put(fullPath, list);
    }

    public String getFullPath(MediaInformation mediaInformation){
        return mediaInformation.getLocation() + Common.childSlash + mediaInformation.getName();
    }

    public void removeMediaInfosSearching(String fullPath) {
        mediaInfosSearching.remove(fullPath);
    }

    /**
     * whether it is image or not
     *
     * @param mediaInformation current selected information
     * @return if exist, true
     */
    public boolean isImage(MediaInformation mediaInformation) {
        return Arrays.asList(Common.extensionsImage).contains(mediaInformation.getExtension().toLowerCase()) || Arrays.asList(Common.extensionsImageExtra).contains(mediaInformation.getExtension().toLowerCase());
    }

    /**
     * whether it is video or not
     *
     * @param mediaInformation current selected information
     * @return if exist, true
     */
    public boolean isVideo(MediaInformation mediaInformation) {
        return Arrays.asList(Common.extensionsVideo).contains(mediaInformation.getExtension().toLowerCase());
    }

    /**
     * whether .ics directory exist in folder or not
     *
     * @param path current selected folder
     * @return if exist, true
     */
    public String[] getSplitLocationByIcs(String path) {
        String first = path;
        String second = "";
        String last = "";
        do {
            File f = new File(first + Common.childSlash + Common.metadataFolderName);
            if (f.exists() && f.isDirectory()) {
                return new String[]{first, second};
            }
            String[] strSplit = splitLocationToTwoPartBySlash(first);
            first = strSplit[0];
            second = strSplit[1] + second;
            last = strSplit[1];
        } while (!last.isEmpty());
        return null;
    }

    /**
     * whether file exist in folder or not
     *
     * @param fullPath current selected file
     * @return if exist, true
     */
    public boolean existFile(String fullPath) {
        File f = new File(fullPath);
        return f.exists() && !f.isDirectory();
    }

    /**
     * whether file exist in folder or not
     *
     * @param path current location
     * @return if exist, true
     */
    public boolean existIcs(String path) {
        File f = new File(path + Common.childSlash + Common.metadataFolderName);
        return f.exists() && f.isDirectory();
    }

    /**
     * whether metadata exist in folder or not
     *
     * @param information current selected info
     * @return if exist, true
     */
    public boolean existMetadata(MediaInformation information) {
        String first = information.getLocation();
        String second = "";
        String last = "";
        do {
            File f = new File(first + Common.childSlash + Common.metadataFolderName + second + Common.childSlash + information.getName() + "." + Common.extensionsText);
            if (f.exists() && !f.isDirectory()) {
                return true;
            }
            String[] strSplit = splitLocationToTwoPartBySlash(first);
            first = strSplit[0];
            second = strSplit[1] + second;
            last = strSplit[1];
        } while (!last.isEmpty());
        return false;
    }

    /**
     * whether metadata exist in folder or not
     *
     * @param information current selected info
     * @return if equal checksum, true
     */
    public boolean isEqualChecksum(MediaInformation information) {
        String checksum = fileController.getCheckSum(information.getLocation() + Common.childSlash + information.getName());
        return information.getCheckSum().equals(checksum);
    }

    /**
     * split location two part by last "\"
     *
     * @param location to split
     * @return array string split
     */
    private String[] splitLocationToTwoPartBySlash(String location) {
        String[] str = new String[2];
        if (location.contains(Common.childSlash)) {
            str[0] = location.substring(0, location.lastIndexOf(Common.childSlash));
            str[1] = location.substring(location.lastIndexOf(Common.childSlash));
        } else {
            str[0] = location;
            str[1] = "";
        }
        return str;
    }


    /**
     * get file list of specific extensions
     *
     * @param files       all files of various extensions
     * @param extensions1 specific extensions
     * @param extensions2 specific extensions
     * @return matching files list
     */
    private ArrayList<String> getFileListFiltered(File[] files, String[] extensions1, String[] extensions2) {
        List<String> list1 = Arrays.asList(extensions1);
        List<String> list2 = Arrays.asList(extensions2);
        ArrayList<String> filesInLocation = new ArrayList<>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isFile()) {
                    String extension = fileName.substring(fileName.indexOf(".") + 1);
                    //  if (list1.contains(extension) || list2.contains(extension)) {
                    filesInLocation.add(fileName);
                    //    }
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

    /**
     * get jpeg from heic file
     *
     * @param fullPath path of heic
     * @return path of temp file
     */
    public String getJpegFromHEIC(String fullPath) {
        String tempFileFullPath = fullPath.replace(fullPath.substring(0, fullPath.indexOf(":") + 1), Common.temp);
        tempFileFullPath = tempFileFullPath.replace(fullPath.substring(fullPath.indexOf(".") + 1), "jpg");
        if (!new File(tempFileFullPath).exists()) {
            if (!fileController.convertImageToJpeg(fullPath, tempFileFullPath)) {
                return null;
            }
        }
        return tempFileFullPath;
    }

    public String fromSizeToString(double size) {
        double order = 1024;
        String str = "";
        int i = 0;
        while (size > order) {
            i++;
            size /= order;
        }
        str = String.format("%.2f", size);
        switch (i) {
            case 0 -> str += " bytes";
            case 1 -> str += " KB";
            case 2 -> str += " MB";
            case 3 -> str += " GB";
        }
        return str;
    }

}
