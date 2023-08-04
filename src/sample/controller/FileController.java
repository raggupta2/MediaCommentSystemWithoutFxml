package sample.controller;

import ca.fuzzlesoft.JsonParse;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import sample.Common;
import sample.model.MediaInformation;
import sample.model.User;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class FileController {

    /**
     * whether register in config or not
     *
     * @param name     user name
     * @param password user password
     * @return if register, true
     */
    public User login(String name, String password) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(Common.configFile));
            String str = br.readLine();
            while (str != null) {
                Map<String, Object> map = JsonParse.map(str);
                String n = (String) map.get("name");
                String p = (String) map.get("password");
                int r = Integer.parseInt(String.valueOf(map.get("role")));
                if (n.equals(name) && p.equals(password)) {
                    return new User(n, p, r);
                }
                str = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * register a new user
     *
     * @param name     user name
     * @param password user password
     * @return if success, true
     */
    public boolean register(String name, String password, int role) {
        String str = "";
        str += "{";
        str += "\"name\":\"" + name + "\",";
        str += "\"password\":\"" + password + "\",";
        str += "\"role\":" + role + "";
        str += "}";
        return writeFile(Common.configFile, str);
    }

    /**
     * this is to get info from a json string
     *
     * @param str json string to get info
     * @return image info
     */
    private MediaInformation fromJsonStringToObject(String str) {
        Map<String, Object> map = JsonParse.map(str);
        MediaInformation mediaInformation = new MediaInformation();

        mediaInformation.setName((String) map.get("name"));
        mediaInformation.setExtension((String) map.get("extension"));
        mediaInformation.setLocation((String) map.get("location"));
        mediaInformation.setDescription((String) map.get("description"));
        mediaInformation.setLatitude((String) map.get("latitude"));
        mediaInformation.setLongitude((String) map.get("longitude"));
        mediaInformation.setGoogleMapUrl((String) map.get("googleMapUrl"));
        mediaInformation.setModificationTime(new Date((String) map.get("modificationTime")));
        mediaInformation.setTakenTime(new Date((String) map.get("takenTime")));
        mediaInformation.setOriginalFileSize((double) map.get("originalFileSize"));
        mediaInformation.setCurrentFileSize((double) map.get("currentFileSize"));
        mediaInformation.setModifier((String) map.get("modifier"));
        mediaInformation.setCheckSum((String) map.get("checkSum"));
        mediaInformation.setQualityRating(Integer.parseInt(String.valueOf(map.get("qualityRating"))));

        return mediaInformation;
    }

    /**
     * this is to get json string from a info
     *
     * @param information
     * @return
     */
    private String fromObjectToString(MediaInformation information) {
        String str = "{";
        str += "\"name\":\"" + information.getName() + "\",";
        str += "\"extension\":\"" + information.getExtension() + "\",";
        str += "\"location\":\"" + information.getLocation() + "\",";
        str += "\"description\":\"" + information.getDescription() + "\",";
        str += "\"latitude\":\"" + information.getLatitude() + (information.getLatitude() != null && information.getLatitude().contains("\"") ? "," : "\",");
        str += "\"longitude\":\"" + information.getLongitude() + (information.getLongitude() != null && information.getLongitude().contains("\"") ? "," : "\",");
        str += "\"googleMapUrl\":\"" + information.getGoogleMapUrl() + "\",";
        str += "\"modificationTime\":\"" + information.getModificationTime() + "\",";
        str += "\"takenTime\":\"" + information.getTakenTime() + "\",";
        str += "\"originalFileSize\":" + information.getOriginalFileSize() + ",";
        str += "\"currentFileSize\":" + information.getCurrentFileSize() + ",";
        str += "\"modifier\":\"" + information.getModifier() + "\",";
        str += "\"checkSum\":\"" + information.getCheckSum() + "\",";
        str += "\"qualityRating\":" + information.getQualityRating() + "";
        str += "}";
        return str;
    }

    /**
     * this is to get info from a image
     *
     * @param fullPath of image to get info
     * @return media info
     */
    private MediaInformation getInfoFromImage(String fullPath) {
        MediaInformation mediaInformation = new MediaInformation("", "", "", "", "", "", "", new Date(), new Date(), 0, 0, "", "", 0);
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new File(fullPath));
            for (Directory directory : metadata.getDirectories()) {
                //    System.out.println(directory.getName());
                for (Tag tag : directory.getTags()) {
                    //  System.out.println(tag.getTagName() + " : " + tag.getDescription());

                    if (tag.getTagName().toLowerCase().equals("GPS Latitude".toLowerCase())) {
                        mediaInformation.setLatitude(tag.getDescription());
                    }
                    if (tag.getTagName().toLowerCase().equals("GPS Longitude".toLowerCase())) {
                        mediaInformation.setLongitude(tag.getDescription());
                    }
                    if (tag.getTagName().toLowerCase().equals("GPS Google map url".toLowerCase())) {
                        mediaInformation.setGoogleMapUrl(tag.getDescription());
                    }
                    if (tag.getTagName().toLowerCase().equals("Date/Time".toLowerCase())) {
                        //   mediaInformation.setTakenTime(new Date(tag.getDescription()));
                    }
                    if (tag.getTagName().toLowerCase().equals("File Size".toLowerCase())) {
                        mediaInformation.setOriginalFileSize(Double.parseDouble(tag.getDescription().substring(0, tag.getDescription().length() - 5)));
                    }
                    if (tag.getTagName().toLowerCase().equals("File Size".toLowerCase())) {
                        mediaInformation.setCurrentFileSize(Double.parseDouble(tag.getDescription().substring(0, tag.getDescription().length() - 5)));
                    }
                    if (tag.getTagName().toLowerCase().equals("JPEG Quality".toLowerCase())) {
                        mediaInformation.setQualityRating(Integer.parseInt(tag.getDescription().substring(0, 1)));
                    }
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaInformation;
    }

    /**
     * this is to get info from a video
     *
     * @param fullPath of video to get info
     * @return video info
     */
    public MediaInformation getInfoFromVideo(String fullPath) {
        MediaInformation mediaInformation = new MediaInformation("", "", "", "", "", "", "", new Date(), new Date(), 0, 0, "", "", 0);
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new File(fullPath));
            for (Directory directory : metadata.getDirectories()) {
                //    System.out.println(directory.getName());
                for (Tag tag : directory.getTags()) {
                    //  System.out.println(tag.getTagName() + " : " + tag.getDescription());

                    if (tag.getTagName().toLowerCase().equals("GPS Latitude".toLowerCase())) {
                        mediaInformation.setLatitude(tag.getDescription());
                    }
                    if (tag.getTagName().toLowerCase().equals("GPS Longitude".toLowerCase())) {
                        mediaInformation.setLongitude(tag.getDescription());
                    }
                    if (tag.getTagName().toLowerCase().equals("GPS Google map url".toLowerCase())) {
                        mediaInformation.setGoogleMapUrl(tag.getDescription());
                    }
                    if (tag.getTagName().toLowerCase().equals("Date/Time".toLowerCase())) {
                        //   mediaInformation.setTakenTime(new Date(tag.getDescription()));
                    }
                    if (tag.getTagName().toLowerCase().equals("File Size".toLowerCase())) {
                        mediaInformation.setOriginalFileSize(Double.parseDouble(tag.getDescription().substring(0, tag.getDescription().length() - 5)));
                    }
                    if (tag.getTagName().toLowerCase().equals("File Size".toLowerCase())) {
                        mediaInformation.setCurrentFileSize(Double.parseDouble(tag.getDescription().substring(0, tag.getDescription().length() - 5)));
                    }
                    if (tag.getTagName().toLowerCase().equals("JPEG Quality".toLowerCase())) {
                        mediaInformation.setQualityRating(Integer.parseInt(tag.getDescription().substring(0, 1)));
                    }
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaInformation;
    }


    /**
     * get all log of change of an media
     *
     * @param fullPath full path of the text file of a media
     * @return list of object
     */
    public ArrayList<MediaInformation> getAllInfosOfOneMedia(String fullPath) {
        ArrayList<MediaInformation> data = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fullPath));
            String str = br.readLine();
            while (str != null) {
                MediaInformation mediaInformation = fromJsonStringToObject(str);
                data.add(mediaInformation);
                str = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * get last info of each media
     *
     * @param mediaUrlMeta   url array with meta
     * @param mediaUrlNoMeta url array without meta
     * @return list of infos
     */
    public ArrayList<MediaInformation> getLastInfosOfEachMedia(String location, ArrayList<String> mediaUrlMeta, ArrayList<String> mediaUrlNoMeta) {
        ArrayList<MediaInformation> data = new ArrayList<>();
        for (int i = 0; i < mediaUrlMeta.size(); i++) {
            try {
                String url = location + Common.childSlash + Common.metadataFolderName + Common.childSlash + mediaUrlMeta.get(i);
                BufferedReader br = new BufferedReader(new FileReader(url));
                String str = br.readLine();
                String str1 = "";
                while (str != null) {
                    str1 = str;
                    str = br.readLine();
                }
                MediaInformation mediaInformation = fromJsonStringToObject(str1);
                data.add(mediaInformation);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < mediaUrlNoMeta.size(); i++) {
            String url = location + Common.childSlash + mediaUrlNoMeta.get(i);
            MediaInformation mediaInformation;
            String extension = mediaUrlNoMeta.get(i).substring(mediaUrlNoMeta.get(i).length() - 3);
            if (Arrays.asList(Common.extensionsImage).contains(extension)) {
                mediaInformation = getInfoFromImage(url);
            } else {
                mediaInformation = getInfoFromVideo(url);
            }
            mediaInformation.setName(mediaUrlNoMeta.get(i));
            mediaInformation.setExtension(mediaUrlNoMeta.get(i).substring(mediaUrlNoMeta.get(i).length() - 3));
            mediaInformation.setLocation(location);
            mediaInformation.setDescription("");
            mediaInformation.setModifier("");
            mediaInformation.setTakenTime(new Date());
            mediaInformation.setModificationTime(new Date());
            mediaInformation.setCheckSum(getCheckSum(mediaInformation.getLocation() + Common.childSlash + mediaInformation.getName()));

            data.add(mediaInformation);
        }
        return data;
    }

    /**
     * record information to file
     *
     * @param fullPath path of file recorded
     * @param info     information to record
     * @return if recorded, return true
     */
    public boolean recordInformation(String fullPath, MediaInformation info) {
        String str = fromObjectToString(info);
        return writeFile(fullPath, str);
    }

    /**
     * write to file some string
     *
     * @param fullPath path of file
     * @param str      string to write
     * @return if success, return true
     */
    private boolean writeFile(String fullPath, String str) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fullPath, true));
            bw.write(str + "\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * A method to generate a checksum from file.
     *
     * @param fullPath : A absolute path of file to be verified by checksum.
     * @return string : checksum
     * @throws IOException
     */
    private String getCheckSum(String fullPath) {
        File file = new File(fullPath);
        MessageDigest mdigest = null;
        try {
            mdigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        FileInputStream fis = null;
        StringBuilder sb = null;
        try {
            fis = new FileInputStream(file);
            byte[] byteArray = new byte[1024];
            int bytesCount = 0;
            while ((bytesCount = fis.read(byteArray)) != -1) {
                mdigest.update(byteArray, 0, bytesCount);
            }
            fis.close();
            byte[] bytes = mdigest.digest();
            sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * get valid url
     *
     * @param url url with space
     * @return url string without space and with '%20'
     */
    public static String getValidUrl(String url) {
        String ret = "";
        ret = url.replace(" ", "%20");
        return ret;
    }

}
